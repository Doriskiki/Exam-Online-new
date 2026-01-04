// 简单的 WebSocket 广播服务，用于向前端推送实时更新
const WebSocket = require('ws');

const wss = new WebSocket.Server({ port: 8080 });

function rand(min, max){ return Math.floor(min + Math.random() * (max - min + 1)); }

function randomTableUpdate(){
  // 生成随机表格数据（模拟每次推送的表格快照）
  const classes = ['高一(1)班','高一(2)班','高一(3)班','高一(4)班'];
  const payload = classes.map(c => ({
    class: c,
    mastery: rand(75,95) + '%',
    avgScore: rand(70,95) + '分',
    progress: '+' + rand(0,20) + '%',
    excellent: rand(20,50) + '%',
    pass: rand(80,99) + '%',
    improvement: ['一般','良好','显著'][rand(0,2)]
  }));

  return JSON.stringify({ type: 'table_update', payload });
}

wss.on('connection', function connection(ws){
  console.log('client connected');
  ws.send(JSON.stringify({ type: 'welcome', msg: 'connected' }));

  const timer = setInterval(() => {
    if(ws.readyState === WebSocket.OPEN){
      ws.send(randomTableUpdate());
    }
  }, 5000);

  ws.on('close', () => { clearInterval(timer); console.log('client disconnected'); });
});

console.log('WebSocket server started on ws://localhost:8080');
