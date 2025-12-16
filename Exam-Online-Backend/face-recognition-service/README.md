# 人脸识别微服务

基于 InsightFace (ArcFace + RetinaFace) 的人脸识别服务

## 安装依赖

```bash
pip install -r requirements.txt
```

## 启动服务

```bash
python app.py
```

服务将在 `http://localhost:5000` 启动

## API 接口

### 1. 健康检查
- **URL**: `/health`
- **方法**: GET
- **返回**: 服务状态

### 2. 人脸检测
- **URL**: `/api/face/detect`
- **方法**: POST
- **参数**: 
  ```json
  {
    "image": "base64编码的图片"
  }
  ```

### 3. 人脸注册
- **URL**: `/api/face/register`
- **方法**: POST
- **参数**:
  ```json
  {
    "userId": "123",
    "userName": "张三",
    "image": "base64编码的图片"
  }
  ```

### 4. 人脸识别
- **URL**: `/api/face/recognize`
- **方法**: POST
- **参数**:
  ```json
  {
    "image": "base64编码的图片",
    "threshold": 0.5
  }
  ```

### 5. 删除人脸
- **URL**: `/api/face/delete`
- **方法**: POST
- **参数**:
  ```json
  {
    "userId": "123"
  }
  ```

### 6. 人脸列表
- **URL**: `/api/face/list`
- **方法**: GET

## 注意事项

1. 首次运行会自动下载模型文件（约200MB）
2. 建议使用GPU加速（需要安装onnxruntime-gpu）
3. 生产环境建议使用 gunicorn 部署
