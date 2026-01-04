(function(){
  var stage = document.getElementById('stage');
  function resizeStage(){
    var vw = window.innerWidth;
    var vh = window.innerHeight;
    var scale = Math.min(vw/1920, vh/1080);
    var tx = (vw - 1920*scale)/2;
    var ty = (vh - 1080*scale)/2;
    stage.style.transform = 'translate('+tx+'px,'+ty+'px) scale('+scale+')';
  }

  function startClock(){
    var el = document.getElementById('nowtime');
    if(!el) return;
    function pad(n){return n<10?('0'+n):n}
    function tick(){
      var d = new Date();
      var s = d.getFullYear()+ '-' + pad(d.getMonth()+1) + '-' + pad(d.getDate()) + '  ' + pad(d.getHours()) + ':' + pad(d.getMinutes()) + ':' + pad(d.getSeconds());
      el.textContent = s;
    }
    tick();
    setInterval(tick, 1000);
  }
  function initModal(){
    var title = document.getElementById('gen-data-title');
    var mask = document.getElementById('gen-modal');
    var closeBtn = document.getElementById('gen-modal-close');
    if(!mask) return;
    if(title){
      title.addEventListener('click', function(){
        mask.classList.add('show');
      });
    }
    if(closeBtn){
      closeBtn.addEventListener('click', function(){
        mask.classList.remove('show');
      });
    }
    mask.addEventListener('click', function(e){
      if(e.target === mask){ mask.classList.remove('show'); }
    });
  }
  // 筛选状态
  var filterState = {
    dimension: 'class', // 'class' 或 'subject'
    timeRange: '3month', // '1month' 或 '3month'
    subject: 'all', // 'all' 或 'key'
    examType: 'all' // 'all' 或 'key'
  };

  // 当前视图状态
  var currentView = 'teaching'; // 'teaching', 'student', 'paper', 'comprehensive', 'compare', 'trend'

  // 初始化数据筛选条件交互
  function initDataFilter(){
    var heaterRows = document.querySelectorAll('.heater-rows .heater-row');
    heaterRows.forEach(function(row, rowIndex){
      var options = row.querySelectorAll('.wdk');
      
      // 根据HTML中的selected类初始化筛选状态
      options.forEach(function(option, optIndex){
        if(option.classList.contains('selected')){
          if(rowIndex === 0){ // 对比维度
            filterState.dimension = optIndex === 0 ? 'class' : 'subject';
          } else if(rowIndex === 1){ // 时间范围
            filterState.timeRange = optIndex === 0 ? '1month' : '3month';
          } else if(rowIndex === 2){ // 学科筛选
            filterState.subject = optIndex === 0 ? 'all' : 'key';
          } else if(rowIndex === 3){ // 考试类型
            filterState.examType = optIndex === 0 ? 'all' : 'key';
          }
        }
        
        option.addEventListener('click', function(){
          // 移除同组其他选项的选中状态
          options.forEach(function(opt){
            opt.classList.remove('selected');
          });
          // 添加当前选项的选中状态
          this.classList.add('selected');
          
          // 更新筛选状态
          if(rowIndex === 0){ // 对比维度
            filterState.dimension = optIndex === 0 ? 'class' : 'subject';
          } else if(rowIndex === 1){ // 时间范围
            filterState.timeRange = optIndex === 0 ? '1month' : '3month';
          } else if(rowIndex === 2){ // 学科筛选
            filterState.subject = optIndex === 0 ? 'all' : 'key';
          } else if(rowIndex === 3){ // 考试类型
            filterState.examType = optIndex === 0 ? 'all' : 'key';
          }
          
          // 应用筛选，更新图表和数据
          applyFilter();
        });
      });
    });
  }

  // 应用筛选条件，更新图表
  function applyFilter(){
    updateChartsByFilter();
    updateTableByFilter();
  }

  // 根据筛选条件更新图表
  function updateChartsByFilter(){
    var rand = function(min,max){return Math.round(min + Math.random()*(max-min));};
    
    // 更新第一个图表（知识点掌握率趋势对比）
    if(charts[0]){
      var times = filterState.timeRange === '1month' 
        ? ['1周','2周','3周','4周'] 
        : ['10月','11月','12月','1月','2月','3月'];
      
      var subjects = filterState.subject === 'all' 
        ? ['数学','语文','英语','物理'] 
        : ['数学','语文'];
      
      var seriesData = subjects.map(function(subject, i){
        return {
          name: subject,
          type: 'line',
          smooth: true,
          showSymbol: false,
          data: times.map(function(){return rand(65,95);}),
          lineStyle: {width: 2, color: ['#6FC2FF','#FFB85C','#8CE08C','#B58CFF','#FF7E7E'][i%5]},
          areaStyle: {opacity: 0.15, color: ['#6FC2FF','#FFB85C','#8CE08C','#B58CFF','#FF7E7E'][i%5]}
        };
      });
      
      charts[0].setOption({
        xAxis: {data: times},
        series: seriesData
      });
    }
    
    // 更新第二个图表（班级知识点掌握率曲线对比）
    if(charts[1]){
      var times = filterState.timeRange === '1month' 
        ? ['第1次','第2次','第3次','第4次'] 
        : ['第1次','第2次','第3次','第4次','第5次','第6次'];
      
      var classes = filterState.dimension === 'class' 
        ? ['高一(1)班','高一(2)班','高一(3)班','高一(4)班'] 
        : ['数学','语文','英语','物理'];
      
      var seriesData = classes.map(function(cls, i){
        return {
          name: cls,
          type: 'line',
          smooth: true,
          showSymbol: false,
          data: times.map(function(){return rand(70,90);}),
          lineStyle: {width: 2, color: ['#6FC2FF','#FFB85C','#8CE08C','#B58CFF','#FF7E7E'][i%5]},
          areaStyle: {opacity: 0.15, color: ['#6FC2FF','#FFB85C','#8CE08C','#B58CFF','#FF7E7E'][i%5]}
        };
      });
      
      charts[1].setOption({
        xAxis: {data: times},
        series: seriesData
      });
    }
  }

  // 根据筛选条件更新表格
  function updateTableByFilter(){
    var tbody = document.querySelector('.data-table tbody');
    if(!tbody) return;
    
    var rand = function(min,max){return Math.round(min + Math.random()*(max-min));};
    var classes = filterState.dimension === 'class' 
      ? ['高一(1)班','高一(2)班','高一(3)班','高一(4)班'] 
      : ['数学','语文','英语','物理'];
    
    var rows = classes.map(function(cls){
      var mastery = rand(75,95);
      var avgScore = rand(70,90);
      var progress = '+' + rand(5,20) + '%';
      var excellent = rand(20,45) + '%';
      var pass = rand(80,98) + '%';
      var improvement = ['一般','良好','显著'][Math.floor(Math.random()*3)];
      
      return '<tr>' +
        '<td>' + cls + '</td>' +
        '<td>' + mastery + '%</td>' +
        '<td>' + avgScore + '分</td>' +
        '<td>' + progress + '</td>' +
        '<td>' + excellent + '</td>' +
        '<td>' + pass + '</td>' +
        '<td>' + improvement + '</td>' +
      '</tr>';
    });
    
    tbody.innerHTML = rows.join('');
  }

  // 初始化功能视图切换交互
  function initViewSwitch(){
    var viewButtons = document.querySelectorAll('.view-buttons-grid .btn-b');
    
    viewButtons.forEach(function(btn){
      btn.addEventListener('click', function(){
        var viewType = this.getAttribute('data-view');
        if(!viewType) return;
        
        // 移除所有按钮的选中状态
        viewButtons.forEach(function(b){
          b.classList.remove('on');
        });
        // 添加当前按钮的选中状态
        this.classList.add('on');
        
        // 切换视图
        currentView = viewType;
        switchView(viewType);
      });
    });
  }

  // 切换视图显示
  function switchView(viewType){
    var leftPanels = document.querySelectorAll('.col-left .panel');
    var middlePanels = document.querySelectorAll('.col-middle .panel');
    
    // 根据视图类型显示/隐藏相应面板
    switch(viewType){
      case 'teaching':
        // 教学效果反馈：显示教学效果表格 + 知识点掌握率趋势图
        // 逻辑：表格展示各班级教学效果数据，图表展示知识点掌握率趋势
        if(leftPanels[0]) leftPanels[0].style.display = 'flex';  // 教学效果表格
        if(leftPanels[1]) leftPanels[1].style.display = 'none';  // 学生画像
        if(leftPanels[2]) leftPanels[2].style.display = 'none';  // 试卷质量
        if(middlePanels[0]) middlePanels[0].style.display = 'flex';  // 知识点掌握率趋势图
        if(middlePanels[1]) middlePanels[1].style.display = 'none'; // 班级对比图
        if(middlePanels[2]) middlePanels[2].style.display = 'none';  // 试卷质量图
        break;
        
      case 'student':
        // 学生画像分析：显示学生画像标签 + 班级对比图
        // 逻辑：标签展示学生画像指标，图表展示各班级对比
        if(leftPanels[0]) leftPanels[0].style.display = 'none';  // 教学效果表格
        if(leftPanels[1]) leftPanels[1].style.display = 'flex';  // 学生画像
        if(leftPanels[2]) leftPanels[2].style.display = 'none';  // 试卷质量
        if(middlePanels[0]) middlePanels[0].style.display = 'none'; // 知识点掌握率趋势图
        if(middlePanels[1]) middlePanels[1].style.display = 'flex'; // 班级对比图
        if(middlePanels[2]) middlePanels[2].style.display = 'none';  // 试卷质量图
        break;
        
      case 'paper':
        // 试卷质量评估：显示试卷质量标签 + 试卷质量变化图
        // 逻辑：标签展示试卷质量指标，图表展示质量变化趋势
        if(leftPanels[0]) leftPanels[0].style.display = 'none';  // 教学效果表格
        if(leftPanels[1]) leftPanels[1].style.display = 'none';  // 学生画像
        if(leftPanels[2]) leftPanels[2].style.display = 'flex';  // 试卷质量
        if(middlePanels[0]) middlePanels[0].style.display = 'none'; // 知识点掌握率趋势图
        if(middlePanels[1]) middlePanels[1].style.display = 'none'; // 班级对比图
        if(middlePanels[2]) middlePanels[2].style.display = 'flex';  // 试卷质量图
        break;
        
      case 'comprehensive':
        // 综合分析报告：显示所有面板和所有图表
        // 逻辑：完整视图，展示所有数据
        leftPanels.forEach(function(p){ if(p) p.style.display = 'flex'; });
        middlePanels.forEach(function(p){ if(p) p.style.display = 'flex'; });
        break;
        
      case 'compare':
        // 数据对比分析：显示所有面板和所有图表
        // 逻辑：对比分析需要同时看到数据和图表
        leftPanels.forEach(function(p){ if(p) p.style.display = 'flex'; });
        middlePanels.forEach(function(p){ if(p) p.style.display = 'flex'; });
        break;
        
      case 'trend':
        // 趋势预测分析：只显示所有图表，不显示数据面板
        // 逻辑：趋势分析专注于图表趋势，不需要看详细数据
        leftPanels.forEach(function(p){ if(p) p.style.display = 'none'; });
        middlePanels.forEach(function(p){ if(p) p.style.display = 'flex'; });
        break;
    }
    
    // 调整图表大小
    setTimeout(function(){
      charts.forEach(function(c){
        if(c){ c.resize(); }
      });
    }, 100);
  }

  // 初始化图表chip筛选交互
  function initChartChips(){
    var chips = document.querySelectorAll('.panel__title .chip');
    chips.forEach(function(chip){
      chip.addEventListener('click', function(e){
        e.stopPropagation();
        var chipText = this.textContent.trim().replace(/\s+/g, ' ');
        var firstSpan = this.querySelector('span:first-child');
        var currentText = firstSpan ? firstSpan.textContent.trim() : chipText.split(' ')[0];
        
        // 根据chip类型显示筛选选项
        if(chipText.indexOf('学科') !== -1 || chipText.indexOf('维度') !== -1 || currentText.indexOf('学科') !== -1){
          showChipFilter(this, ['全部学科', '数学', '语文', '英语', '物理'], function(selected){
            if(firstSpan){
              firstSpan.textContent = selected;
            } else {
              chip.childNodes[0].textContent = selected;
            }
            applyFilter();
          });
        } else if(chipText.indexOf('班级') !== -1 || currentText.indexOf('班级') !== -1){
          showChipFilter(this, ['全部班级', '高一(1)班', '高一(2)班', '高一(3)班', '高一(4)班'], function(selected){
            if(firstSpan){
              firstSpan.textContent = selected;
            } else {
              chip.childNodes[0].textContent = selected;
            }
            applyFilter();
          });
        } else if(chipText.indexOf('至') !== -1 || chipText.indexOf('-') !== -1 || chipText.match(/\d{4}/)){
          // 时间范围选择
          var timeOptions = ['近1周', '近1个月', '近3个月', '近6个月', '近1年', '2025-10至2025-12', '2025-8-11 - 2025-9-11'];
          showChipFilter(this, timeOptions, function(selected){
            if(firstSpan){
              firstSpan.textContent = selected;
            } else {
              chip.childNodes[0].textContent = selected;
            }
            // 更新筛选状态
            if(selected.indexOf('1个月') !== -1){
              filterState.timeRange = '1month';
            } else if(selected.indexOf('3个月') !== -1){
              filterState.timeRange = '3month';
            }
            applyFilter();
          });
        } else if(chipText.indexOf('难度') !== -1 || chipText.indexOf('区分度') !== -1 || currentText.indexOf('难度') !== -1){
          showChipFilter(this, ['全部', '难度系数', '区分度', '有效题占比'], function(selected){
            if(firstSpan){
              firstSpan.textContent = selected;
            } else {
              chip.childNodes[0].textContent = selected;
            }
            applyFilter();
          });
        }
      });
    });
  }

  // 显示chip筛选下拉菜单
  function showChipFilter(chip, options, callback){
    // 移除已存在的下拉菜单
    var existing = document.querySelector('.chip-dropdown');
    if(existing) existing.remove();
    
    var rect = chip.getBoundingClientRect();
    var dropdown = document.createElement('div');
    dropdown.className = 'chip-dropdown';
    dropdown.style.cssText = 'position:fixed;left:' + rect.left + 'px;top:' + (rect.bottom + 5) + 'px;z-index:1000;background:rgba(12,32,64,0.95);border:1px solid rgba(111,194,255,0.5);border-radius:6px;padding:8px 0;min-width:120px;box-shadow:0 4px 12px rgba(0,0,0,0.3);';
    
    options.forEach(function(opt){
      var item = document.createElement('div');
      item.textContent = opt;
      item.style.cssText = 'padding:8px 16px;color:#cfe8ff;cursor:pointer;font-size:12px;transition:all 0.2s;';
      item.addEventListener('mouseenter', function(){
        this.style.background = 'rgba(111,194,255,0.2)';
        this.style.color = '#fff';
      });
      item.addEventListener('mouseleave', function(){
        this.style.background = 'transparent';
        this.style.color = '#cfe8ff';
      });
      item.addEventListener('click', function(e){
        e.stopPropagation();
        callback(opt);
        dropdown.remove();
      });
      dropdown.appendChild(item);
    });
    
    document.body.appendChild(dropdown);
    
    // 点击外部关闭
    setTimeout(function(){
      document.addEventListener('click', function closeHandler(e){
        if(!dropdown.contains(e.target) && e.target !== chip){
          dropdown.remove();
          document.removeEventListener('click', closeHandler);
        }
      });
    }, 0);
  }

  // 初始化数据更新设置交互
  function initDataUpdate(){
    var updateButtons = document.querySelectorAll('.row-ctrl .btn-a');
    var autoUpdateInterval = null;
    var updateFrequency = 3600000; // 默认1小时
    
    updateButtons.forEach(function(btn){
      btn.addEventListener('click', function(){
        var btnText = this.textContent.trim();
        
        if(btnText === '手动更新' || btnText === '自动更新'){
          var updateGroup = [updateButtons[0], updateButtons[1]];
          updateGroup.forEach(function(b){
            b.classList.remove('on');
          });
          this.classList.add('on');
          
          // 停止之前的自动更新
          if(autoUpdateInterval){
            clearInterval(autoUpdateInterval);
            autoUpdateInterval = null;
          }
          
          // 如果是自动更新，启动定时器
          if(btnText === '自动更新'){
            autoUpdateInterval = setInterval(function(){
              refreshAllData();
            }, updateFrequency);
          }
        } else if(btnText === '导出数据'){
          exportData();
        } else if(btnText === '刷新图表'){
          refreshCharts();
        }
      });
    });

    // 更新频率设置点击事件
    var freqPill = document.querySelector('.k-pill');
    if(freqPill){
      var frequencies = [
        {text: '每5分钟', value: 300000},
        {text: '每15分钟', value: 900000},
        {text: '每30分钟', value: 1800000},
        {text: '每小时', value: 3600000},
        {text: '每3小时', value: 10800000}
      ];
      var currentFreqIndex = 3; // 默认每小时
      
      freqPill.addEventListener('click', function(){
        currentFreqIndex = (currentFreqIndex + 1) % frequencies.length;
        var freq = frequencies[currentFreqIndex];
        updateFrequency = freq.value;
        this.querySelector('.num').textContent = freq.text;
        
        // 如果正在自动更新，重启定时器
        if(autoUpdateInterval){
          clearInterval(autoUpdateInterval);
          autoUpdateInterval = setInterval(function(){
            refreshAllData();
          }, updateFrequency);
        }
      });
    }
  }

  // 刷新所有数据
  function refreshAllData(){
    applyFilter();
    updateTagsData();
    var envInfo = document.querySelector('.env-info');
    if(envInfo){
      var now = new Date();
      var timeStr = now.getFullYear() + '-' + 
        String(now.getMonth()+1).padStart(2,'0') + '-' + 
        String(now.getDate()).padStart(2,'0') + ' ' + 
        String(now.getHours()).padStart(2,'0') + ':' + 
        String(now.getMinutes()).padStart(2,'0');
      envInfo.textContent = '数据更新时间：' + timeStr;
    }
  }

  // 刷新图表
  function refreshCharts(){
    charts.forEach(function(c){
      if(c){ 
        c.resize();
        applyFilter();
      }
    });
  }

  // 更新标签数据
  function updateTagsData(){
    var rand = function(min,max){return Math.round(min + Math.random()*(max-min));};
    
    // 更新学生画像数据
    var studentTags = document.querySelectorAll('.tags-gen .value b');
    if(studentTags.length > 0){
      studentTags[0].textContent = rand(75,90); // 平均知识点掌握度
      studentTags[1].textContent = rand(2,5); // 薄弱环节数量
      studentTags[2].textContent = rand(70,85); // 个性化练习完成率
      studentTags[3].textContent = rand(8,18); // 成绩提升幅度
      studentTags[4].textContent = (rand(35,50)/10).toFixed(1); // 学习时长
      studentTags[5].textContent = rand(60,75); // 错题订正率
    }
    
    // 更新试卷质量数据
    var paperTags = document.querySelectorAll('.tags-load .value b');
    if(paperTags.length > 0){
      paperTags[0].textContent = (rand(30,70)/100).toFixed(2); // 难度系数
      paperTags[1].textContent = (rand(30,60)/100).toFixed(2); // 区分度指数
      paperTags[2].textContent = rand(80,95); // 有效题占比
      paperTags[3].textContent = rand(85,98); // 知识点覆盖率
      paperTags[4].textContent = rand(2,5); // 无效难题数
      paperTags[5].textContent = rand(1,4); // 送分题数量
    }
  }

  // 导出数据
  function exportData(){
    var data = {
      filter: filterState,
      view: currentView,
      timestamp: new Date().toISOString(),
      tableData: []
    };
    
    // 收集表格数据
    var rows = document.querySelectorAll('.data-table tbody tr');
    rows.forEach(function(row){
      var cells = row.querySelectorAll('td');
      if(cells.length > 0){
        data.tableData.push({
          class: cells[0].textContent,
          mastery: cells[1].textContent,
          avgScore: cells[2].textContent,
          progress: cells[3].textContent,
          excellent: cells[4].textContent,
          pass: cells[5].textContent,
          improvement: cells[6].textContent
        });
      }
    });
    
    // 创建下载链接
    var blob = new Blob([JSON.stringify(data, null, 2)], {type: 'application/json'});
    var url = URL.createObjectURL(blob);
    var a = document.createElement('a');
    a.href = url;
    a.download = '教育数据_' + new Date().getTime() + '.json';
    a.click();
    URL.revokeObjectURL(url);
  }

  window.addEventListener('resize', function(){ resizeStage(); charts.forEach(function(c){ if(c){ c.resize(); } }); });
  document.addEventListener('DOMContentLoaded', function(){ 
    resizeStage(); 
    initCharts(); 
    startClock(); 
    initModal(); 
    initDataFilter();
    initViewSwitch();
    initChartChips();
    initDataUpdate();
    // 初始化默认视图
    switchView('teaching');
    // 初始化实时数据 & Web Worker
    if(typeof initRealtimeAndWorker === 'function') initRealtimeAndWorker();
  });

  var charts = [];

  function createMultiLineChart(el, times, seriesDefs, yAxisFormatter){
    var chart = echarts.init(el);
    var colors = ['#6FC2FF','#FFB85C','#8CE08C','#B58CFF','#FF7E7E'];
    var series = seriesDefs.map(function(def, i){
      return {
        name:def.name,
        type:'line',
        smooth:true,
        showSymbol:false,
        data:def.data,
        lineStyle:{width:2,color:colors[i%colors.length]},
        areaStyle:{opacity:0.15,color:colors[i%colors.length]}
      };
    });
    var yAxisOption = {
      type:'value',
      axisLine:{show:false},
      splitLine:{lineStyle:{color:'rgba(111,194,255,.18)'}},
      axisLabel:{color:'#CCDAE6'}
    };
    if(yAxisFormatter){
      yAxisOption.axisLabel.formatter = yAxisFormatter;
    }
    var option = {
      color: colors,
      grid:{left:40,right:20,top:30,bottom:50,containLabel:true},
      tooltip:{trigger:'axis', formatter: function(params){
        var result = params[0].name + '<br/>';
        params.forEach(function(item){
          var val = item.value;
          var displayVal = val;
          // 如果是字符串且包含小数点，直接显示；否则添加%
          if(typeof val === 'string' && val.indexOf('.') !== -1){
            displayVal = val;
          } else if(typeof val === 'number' && val < 1){
            displayVal = val.toFixed(2);
          } else {
            displayVal = val + '%';
          }
          result += item.marker + item.seriesName + ': ' + displayVal + '<br/>';
        });
        return result;
      }},
      legend:{bottom:6,textStyle:{color:'#CCDAE6'}},
      xAxis:{type:'category',data:times,axisLine:{lineStyle:{color:'rgba(111,194,255,.8)'}},axisLabel:{color:'#CCDAE6'},axisTick:{show:false}},
      yAxis: yAxisOption,
      series: series
    };
    chart.setOption(option);
    return chart;
  }

  function initCharts(){
    var rand = function(min,max){return Math.round(min + Math.random()*(max-min));};
    // 知识点掌握率趋势图：按月份展示（2025-10至2025-12）
    var timesKnowledge = ['10月','11月','12月','1月','2月','3月'];
    // 班级对比图：按考试次数展示
    var timesClass = ['第1次','第2次','第3次','第4次','第5次','第6次'];
    // 试卷质量图：按月份展示
    var timesPaper = ['10月','11月','12月','1月','2月','3月'];

    // 知识点掌握率趋势对比（按学科：数学 / 语文 / 英语 / 物理）
    var namesKnowledge = ['数学','语文','英语','物理'];
    charts.push(
      createMultiLineChart(
        document.getElementById('chart-heater-line'),
        timesKnowledge,
        namesKnowledge.map(function(n){
          return {name:n,data:timesKnowledge.map(function(){return rand(65,95)})};
        }),
        function(value){return value + '%';}
      )
    );

    // 班级知识点掌握率曲线对比（高一(1)班 / 高一(2)班 / 高一(3)班 / 高一(4)班）
    var namesClass = ['高一(1)班','高一(2)班','高一(3)班','高一(4)班'];
    charts.push(
      createMultiLineChart(
        document.getElementById('chart-generator-line'),
        timesClass,
        namesClass.map(function(n){
          return {name:n,data:timesClass.map(function(){return rand(70,90)})};
        }),
        function(value){return value + '%';}
      )
    );

    // 试卷质量评估分析（难度系数 / 区分度 / 有效题占比）
    var namesPaper = ['难度系数','区分度','有效题占比'];
    charts.push(
      createMultiLineChart(
        document.getElementById('chart-load-line'),
        timesPaper,
        namesPaper.map(function(n, idx){
          // 难度系数范围 0.3-0.7，区分度范围 0.3-0.6，有效题占比范围 75-95
          if(idx === 0){
            // 难度系数：0.3-0.7
            return {name:n,data:timesPaper.map(function(){return (rand(30,70)/100).toFixed(2);})};
          } else if(idx === 1){
            // 区分度：0.3-0.6
            return {name:n,data:timesPaper.map(function(){return (rand(30,60)/100).toFixed(2);})};
          } else {
            // 有效题占比：75-95%
            return {name:n,data:timesPaper.map(function(){return rand(75,95);})};
          }
        }),
        function(value){
          // 如果是字符串且包含小数点，显示为小数，否则显示为百分比
          if(typeof value === 'string' && value.indexOf('.') !== -1){
            return value;
          }
          return value + '%';
        }
      )
    );
  }

  // 初始化 Web Worker, 从 json-server 拉取初始数据，并连接本地 WebSocket 以接收实时更新
  function initRealtimeAndWorker(){
    var dataWorker = null;
    if(window.Worker){
      try{
        dataWorker = new Worker('worker.js');
        dataWorker.addEventListener('message', function(ev){
          try{
            if(ev.data && ev.data.type === 'agg'){
              // 示例：在控制台展示聚合结果，或更新页面某处
              console.log('聚合结果:', ev.data.payload);
              // 如需展示可在此更新 DOM
            }
          }catch(err){ console.warn(err); }
        });
      }catch(err){ console.warn('创建 Worker 失败', err); }
    }

    function applyTableFromData(rows){
      var tbody = document.querySelector('.data-table tbody');
      if(!tbody) return;
      var rowsHtml = rows.map(function(item){
        return '<tr>' +
          '<td>' + (item.class || '') + '</td>' +
          '<td>' + (item.mastery || '') + '</td>' +
          '<td>' + (item.avgScore || '') + '</td>' +
          '<td>' + (item.progress || '') + '</td>' +
          '<td>' + (item.excellent || '') + '</td>' +
          '<td>' + (item.pass || '') + '</td>' +
          '<td>' + (item.improvement || '') + '</td>' +
        '</tr>';
      });
      tbody.innerHTML = rowsHtml.join('');
    }

    // 从 json-server 拉取初始数据（请先运行 json-server: npm run start:api）
    try{
      fetch('http://localhost:3000/table').then(function(resp){ return resp.json(); }).then(function(data){
        if(Array.isArray(data)){
          applyTableFromData(data);
          if(dataWorker) dataWorker.postMessage({type:'aggregate', payload: data});
        }
      }).catch(function(err){ console.warn('fetch table error', err); });
    }catch(err){ console.warn('fetch not available', err); }

    // 连接本地 WebSocket（请先运行 ws-server: npm run start:ws）
    try{
      var socket = new WebSocket('ws://localhost:8080');
      socket.addEventListener('open', function(){ console.log('ws connected'); });
      socket.addEventListener('message', function(ev){
        try{
          var msg = JSON.parse(ev.data);
          if(msg.type === 'table_update' && Array.isArray(msg.payload)){
            applyTableFromData(msg.payload);
            if(dataWorker) dataWorker.postMessage({type:'aggregate', payload: msg.payload});
            // 触发页面更新
            updateTagsData();
            applyFilter();
          }
        }catch(err){ /* ignore non-json messages */ }
      });
      socket.addEventListener('close', function(){ console.log('ws closed'); setTimeout(function(){ initRealtimeAndWorker(); }, 3000); });
    }catch(err){ console.warn('WebSocket error', err); }
  }
})();
