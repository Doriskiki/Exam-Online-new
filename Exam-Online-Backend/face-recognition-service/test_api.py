"""
人脸识别API测试脚本
"""
import requests
import base64
import json

# 服务地址
BASE_URL = "http://localhost:5000"

def test_health():
    """测试健康检查接口"""
    print("\n=== 测试健康检查 ===")
    response = requests.get(f"{BASE_URL}/health")
    print(f"状态码: {response.status_code}")
    print(f"响应: {response.json()}")

def image_to_base64(image_path):
    """将图片转换为base64"""
    with open(image_path, 'rb') as f:
        return base64.b64encode(f.read()).decode('utf-8')

def test_detect(image_path):
    """测试人脸检测"""
    print("\n=== 测试人脸检测 ===")
    image_base64 = image_to_base64(image_path)
    
    response = requests.post(
        f"{BASE_URL}/api/face/detect",
        json={"image": image_base64}
    )
    
    print(f"状态码: {response.status_code}")
    result = response.json()
    print(f"检测结果: {json.dumps(result, indent=2, ensure_ascii=False)}")
    return result

def test_register(user_id, user_name, image_path):
    """测试人脸注册"""
    print(f"\n=== 测试人脸注册 (用户: {user_name}) ===")
    image_base64 = image_to_base64(image_path)
    
    response = requests.post(
        f"{BASE_URL}/api/face/register",
        json={
            "userId": str(user_id),
            "userName": user_name,
            "image": image_base64
        }
    )
    
    print(f"状态码: {response.status_code}")
    result = response.json()
    print(f"注册结果: {json.dumps(result, indent=2, ensure_ascii=False)}")
    return result

def test_recognize(image_path, threshold=0.5):
    """测试人脸识别"""
    print(f"\n=== 测试人脸识别 (阈值: {threshold}) ===")
    image_base64 = image_to_base64(image_path)
    
    response = requests.post(
        f"{BASE_URL}/api/face/recognize",
        json={
            "image": image_base64,
            "threshold": threshold
        }
    )
    
    print(f"状态码: {response.status_code}")
    result = response.json()
    print(f"识别结果: {json.dumps(result, indent=2, ensure_ascii=False)}")
    return result

def test_list():
    """测试获取人脸列表"""
    print("\n=== 测试获取人脸列表 ===")
    response = requests.get(f"{BASE_URL}/api/face/list")
    
    print(f"状态码: {response.status_code}")
    result = response.json()
    print(f"列表结果: {json.dumps(result, indent=2, ensure_ascii=False)}")
    return result

def test_delete(user_id):
    """测试删除人脸"""
    print(f"\n=== 测试删除人脸 (用户ID: {user_id}) ===")
    response = requests.post(
        f"{BASE_URL}/api/face/delete",
        json={"userId": str(user_id)}
    )
    
    print(f"状态码: {response.status_code}")
    result = response.json()
    print(f"删除结果: {json.dumps(result, indent=2, ensure_ascii=False)}")
    return result

if __name__ == "__main__":
    print("=" * 50)
    print("人脸识别API测试")
    print("=" * 50)
    
    # 1. 健康检查
    test_health()
    
    # 2. 测试人脸检测
    # 请替换为实际的图片路径
    # test_detect("test_image.jpg")
    
    # 3. 测试人脸注册
    # test_register(123, "张三", "test_image.jpg")
    
    # 4. 测试人脸识别
    # test_recognize("test_image.jpg", threshold=0.5)
    
    # 5. 测试获取列表
    # test_list()
    
    # 6. 测试删除
    # test_delete(123)
    
    print("\n" + "=" * 50)
    print("测试完成！")
    print("=" * 50)
