# 大屏项目 - 本地 Mock 与实时开发说明

本项目已加入示例的本地 mock 与实时推送支持：

- `json-server`：用于提供 REST API（mock 数据）。
- `ws-server.js`：基于 `ws` 的简单 WebSocket 服务，向客户端周期性推送随机表格更新。
- `worker.js`：浏览器端 Web Worker，用于在后台进行数据聚合计算。

快速运行步骤（在项目根目录执行）：

1. 安装依赖：

```bash
npm install
```

2. 启动 mock API：

```bash
npm run start:api
```

3. 启动 WebSocket 服务（另开终端）：

```bash
npm run start:ws
```

4. 在浏览器中打开 `index.html`（或用本地静态服务器），`app.js` 会从 `http://localhost:3000/table` 拉取初始数据，并连接 `ws://localhost:8080` 接收实时更新。同时会创建 `worker.js` 来计算聚合。 

注意：如果想在一个终端同时运行两个服务，可以先全局安装 `concurrently`，或使用 `npm run start:all`（package.json 中已包含脚本，但需安装 `concurrently`）。
