// Web Worker: 接收表格数据并做聚合计算（示例：计算平均掌握率、平均分）
self.addEventListener('message', function(e){
  var data = e.data || {};
  if(data.type === 'aggregate' && Array.isArray(data.payload)){
    var arr = data.payload;
    var totalMastery = 0;
    var totalScore = 0;
    var count = arr.length;

    arr.forEach(function(item){
      // 解析 mastery 和 avgScore 字段（例如 "82%" / "82分"）
      var m = 0, s = 0;
      try{
        if(item.mastery){ m = parseFloat(String(item.mastery).replace('%','')) || 0; }
        if(item.avgScore){ s = parseFloat(String(item.avgScore).replace('分','')) || 0; }
      }catch(err){ }
      totalMastery += m;
      totalScore += s;
    });

    var avgMastery = count ? (totalMastery / count) : 0;
    var avgScore = count ? (totalScore / count) : 0;

    self.postMessage({ type: 'agg', payload: { avgMastery: avgMastery, avgScore: avgScore, count: count } });
  }
});
