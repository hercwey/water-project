/*
		对比柱状图-前端
		构造函数参数:			
			statisticData 统计数据
			结构如下:
			{
			
				monthList:[],        	//月份列表
				legendList:[],			//图例列表
				statDataList:[			//统计数据
					[],
					[]
				]   	//对比数据列表
			}
			periodx 期间 格式为yyyy-mm
			chartId 统计所在容器id
			
	*/
	function StatBarChart(statisticData,chartId){		
		var statData=statisticData;  //统计数据
		var chartContainerId=chartId  //柱状图ID
			
		var title=""; 		//title标题
		//var legendName="";  //图例名称
		
		/**
		 * 设置标题
		 */
		this.setTitle=function(titlex){
			title=titlex
		}
		
		/**
		 * 设置图例
		 */
		/*this.setLegendName=function(legendNamex){
			lengendName=legendNamex
		}*/
		
		/*
			显示图表
		*/
		this.displayChart=function(){
			const CHART_CONTAINER= "#"+chartContainerId;						
			
			var chartContainer=$(CHART_CONTAINER).get(0);
			var myChart = echarts.init(chartContainer);			
			var app = {};
			
			//设置数值参数
			option = null;	
			option = {
				    title : {
				        text: title,
				        subtext: '水量'
				    },
				    tooltip : {
				        trigger: 'axis'
				    },
				    legend: {
				        data:statData.legendList
				    },
				    toolbox: {
				        show : true,
				        feature : {
				            dataView : {show: true, readOnly: false},
				            magicType : {show: true, type: ['line', 'bar']},
				            restore : {show: true},
				            saveAsImage : {show: true}
				        }
				    },
				    calculable : true,
				    xAxis : [
				        {
				            type : 'category',
				            data : statData.monthList
				        }
				    ],
				    yAxis : [
				        {
				            type : 'value'
				        }
				    ],
				    series : [
				        {
				            name:statData.legendList[0],
				            type:'bar',
				            data:statData.statDataList[0],
				            markPoint : {
				                data : [
				                    {type : 'max', name: '最大值'},
				                    {type : 'min', name: '最小值'}
				                ]
				            },
				            markLine : {
				                data : [
				                    {type : 'average', name: '平均值'}
				                ]
				            }
				        },
				        {
				            name:statData.legendList[1],
				            type:'bar',
				            data:statData.statDataList[1],
				            markPoint : {
				                /*data : [
				                    {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183},
				                    {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
				                ]*/
				            	data : [
				                    {type : 'max', name: '最大值'},
				                    {type : 'min', name: '最小值'}
				                ]
				            },
				            markLine : {
				                data : [
				                    {type : 'average', name : '平均值'}
				                ]
				            }
				        }
				    ]
				};
			
			
			if (option && typeof option === "object") {
			    myChart.setOption(option, true);
			}
		}
		
	}  //end of object