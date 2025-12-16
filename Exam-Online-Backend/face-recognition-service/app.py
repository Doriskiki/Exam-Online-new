"""
人脸识别微服务
基于 InsightFace (ArcFace) 和 RetinaFace
"""
from flask import Flask, request, jsonify
from flask_cors import CORS
import cv2
import numpy as np
import base64
import os
from insightface.app import FaceAnalysis
import pickle
from datetime import datetime

app = Flask(__name__)
CORS(app)

# 初始化人脸分析模型
face_app = FaceAnalysis(providers=['CPUExecutionProvider'])
face_app.prepare(ctx_id=0, det_size=(640, 640))

# 人脸特征数据库（实际应该存储在数据库中）
FACE_DB_PATH = 'face_database.pkl'
face_database = {}

# 加载已有的人脸数据库
if os.path.exists(FACE_DB_PATH):
    with open(FACE_DB_PATH, 'rb') as f:
        face_database = pickle.load(f)


def save_face_database():
    """保存人脸数据库到文件"""
    with open(FACE_DB_PATH, 'wb') as f:
        pickle.dump(face_database, f)


def base64_to_image(base64_str):
    """将base64字符串转换为OpenCV图像"""
    if ',' in base64_str:
        base64_str = base64_str.split(',')[1]
    img_data = base64.b64decode(base64_str)
    nparr = np.frombuffer(img_data, np.uint8)
    img = cv2.imdecode(nparr, cv2.IMREAD_COLOR)
    return img


def image_to_base64(img):
    """将OpenCV图像转换为base64字符串"""
    _, buffer = cv2.imencode('.jpg', img)
    return base64.b64encode(buffer).decode('utf-8')


def calculate_similarity(embedding1, embedding2):
    """计算两个人脸特征向量的相似度（余弦相似度）"""
    return np.dot(embedding1, embedding2) / (np.linalg.norm(embedding1) * np.linalg.norm(embedding2))


@app.route('/', methods=['GET'])
def index():
    """首页 - 返回测试页面"""
    return app.send_static_file('demo.html')


@app.route('/health', methods=['GET'])
def health_check():
    """健康检查接口"""
    return jsonify({
        'status': 'ok',
        'message': 'Face Recognition Service is running',
        'timestamp': datetime.now().isoformat()
    })


@app.route('/api/face/detect', methods=['POST'])
def detect_face():
    """
    人脸检测接口
    输入: { "image": "base64编码的图片" }
    输出: { "success": true, "faces": [...], "count": 1 }
    """
    try:
        data = request.json
        if 'image' not in data:
            return jsonify({'success': False, 'message': '缺少图片数据'}), 400
        
        # 解码图片
        img = base64_to_image(data['image'])
        
        # 检测人脸
        faces = face_app.get(img)
        
        if len(faces) == 0:
            return jsonify({
                'success': True,
                'count': 0,
                'faces': [],
                'message': '未检测到人脸'
            })
        
        # 构建返回结果
        face_list = []
        for face in faces:
            bbox = face.bbox.astype(int).tolist()
            face_info = {
                'bbox': bbox,  # [x1, y1, x2, y2]
                'confidence': float(face.det_score),
                'landmarks': face.kps.astype(int).tolist() if hasattr(face, 'kps') else None
            }
            face_list.append(face_info)
        
        return jsonify({
            'success': True,
            'count': len(faces),
            'faces': face_list,
            'message': f'检测到 {len(faces)} 张人脸'
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'人脸检测失败: {str(e)}'
        }), 500


@app.route('/api/face/register', methods=['POST'])
def register_face():
    """
    人脸注册接口
    输入: { "userId": "123", "userName": "张三", "image": "base64编码的图片" }
    输出: { "success": true, "message": "注册成功", "embedding": [...] }
    """
    try:
        data = request.json
        if 'userId' not in data or 'image' not in data:
            return jsonify({'success': False, 'message': '缺少必要参数'}), 400
        
        user_id = str(data['userId'])
        user_name = data.get('userName', '')
        
        # 解码图片
        img = base64_to_image(data['image'])
        
        # 检测人脸
        faces = face_app.get(img)
        
        if len(faces) == 0:
            return jsonify({
                'success': False,
                'message': '未检测到人脸，请确保照片中有清晰的人脸'
            }), 400
        
        if len(faces) > 1:
            return jsonify({
                'success': False,
                'message': '检测到多张人脸，请确保照片中只有一张人脸'
            }), 400
        
        # 提取人脸特征（512维向量）
        face = faces[0]
        embedding = face.normed_embedding
        
        # 保存到数据库
        face_database[user_id] = {
            'userId': user_id,
            'userName': user_name,
            'embedding': embedding.tolist(),
            'registerTime': datetime.now().isoformat()
        }
        save_face_database()
        
        return jsonify({
            'success': True,
            'message': '人脸注册成功',
            'userId': user_id,
            'userName': user_name,
            'embedding': embedding.tolist()
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'人脸注册失败: {str(e)}'
        }), 500


@app.route('/api/face/recognize', methods=['POST'])
def recognize_face():
    """
    人脸识别接口
    输入: { "image": "base64编码的图片", "threshold": 0.5 }
    输出: { "success": true, "matched": true, "userId": "123", "similarity": 0.85 }
    """
    try:
        data = request.json
        if 'image' not in data:
            return jsonify({'success': False, 'message': '缺少图片数据'}), 400
        
        threshold = data.get('threshold', 0.5)  # 相似度阈值
        
        # 解码图片
        img = base64_to_image(data['image'])
        
        # 检测人脸
        faces = face_app.get(img)
        
        if len(faces) == 0:
            return jsonify({
                'success': True,
                'matched': False,
                'message': '未检测到人脸'
            })
        
        # 使用第一张检测到的人脸
        face = faces[0]
        embedding = face.normed_embedding
        
        # 在数据库中查找最相似的人脸
        best_match = None
        best_similarity = 0
        
        for user_id, user_data in face_database.items():
            stored_embedding = np.array(user_data['embedding'])
            similarity = calculate_similarity(embedding, stored_embedding)
            
            if similarity > best_similarity:
                best_similarity = similarity
                best_match = user_data
        
        # 判断是否匹配
        if best_match and best_similarity >= threshold:
            return jsonify({
                'success': True,
                'matched': True,
                'userId': best_match['userId'],
                'userName': best_match.get('userName', ''),
                'similarity': float(best_similarity),
                'threshold': threshold,
                'message': '人脸识别成功'
            })
        else:
            return jsonify({
                'success': True,
                'matched': False,
                'similarity': float(best_similarity) if best_match else 0,
                'threshold': threshold,
                'message': '未找到匹配的人脸'
            })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'人脸识别失败: {str(e)}'
        }), 500


@app.route('/api/face/delete', methods=['POST'])
def delete_face():
    """
    删除人脸数据
    输入: { "userId": "123" }
    输出: { "success": true, "message": "删除成功" }
    """
    try:
        data = request.json
        if 'userId' not in data:
            return jsonify({'success': False, 'message': '缺少userId参数'}), 400
        
        user_id = str(data['userId'])
        
        if user_id in face_database:
            del face_database[user_id]
            save_face_database()
            return jsonify({
                'success': True,
                'message': '人脸数据删除成功'
            })
        else:
            return jsonify({
                'success': False,
                'message': '未找到该用户的人脸数据'
            }), 404
    
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'删除失败: {str(e)}'
        }), 500


@app.route('/api/face/list', methods=['GET'])
def list_faces():
    """
    获取所有已注册的人脸列表
    输出: { "success": true, "count": 10, "users": [...] }
    """
    try:
        users = []
        for user_id, user_data in face_database.items():
            users.append({
                'userId': user_data['userId'],
                'userName': user_data.get('userName', ''),
                'registerTime': user_data.get('registerTime', '')
            })
        
        return jsonify({
            'success': True,
            'count': len(users),
            'users': users
        })
    
    except Exception as e:
        return jsonify({
            'success': False,
            'message': f'获取列表失败: {str(e)}'
        }), 500


if __name__ == '__main__':
    print("=" * 50)
    print("人脸识别微服务启动中...")
    print("使用模型: InsightFace (ArcFace + RetinaFace)")
    print("服务地址: http://localhost:5000")
    print("=" * 50)
    app.run(host='0.0.0.0', port=5000, debug=True)
