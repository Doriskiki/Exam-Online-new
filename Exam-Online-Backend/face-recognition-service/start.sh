#!/bin/bash

echo "========================================"
echo "启动人脸识别微服务"
echo "========================================"
echo ""

# 检查Python是否安装
if ! command -v python3 &> /dev/null; then
    echo "错误: 未检测到Python，请先安装Python 3.8+"
    exit 1
fi

echo "[1/3] 检查依赖..."
if ! python3 -c "import flask" &> /dev/null; then
    echo "正在安装依赖..."
    pip3 install -r requirements.txt
else
    echo "依赖已安装"
fi

echo ""
echo "[2/3] 启动服务..."
echo "服务地址: http://localhost:5000"
echo "按 Ctrl+C 停止服务"
echo ""

python3 app.py
