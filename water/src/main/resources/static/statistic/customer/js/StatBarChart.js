/*
		显示柱状图-前端
		构造函数参数:			
			statisticData 统计数据
			结构如下:
			{
				periodList:[],        //天/月份列表
				numPerPeriodList:[]   //每天/月  新增客户列表   与上面的时间一一对应
			}
			periodx 期间 格式为yyyy-mm
			chartId 统计所在容器id
			
	*/
	function StatBarChart(statisticData,chartId){		
		var statData=statisticData;  //统计数据
		var chartContainerId=chartId  //柱状图ID
			
		var title=""; 		//title标题
		var legendName="";  //图例名称
		
		/**
		 * 设置标题
		 */
		this.setTitle=function(titlex){
			title=titlex
		}
		
		/**
		 * 设置图例
		 */
		this.setLegendName=function(legendNamex){
			lengendName=legendNamex
		}
		
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
				title: {
		               text: title
		           	},
		        tooltip: {},
		        legend: {
		               data:[lengendName]
		           		},
				xAxis : {			
					data : statData.periodList   //时间列表
				},
				yAxis : {
					type : 'value'
				},
				series : [ {
					name : lengendName,
					data : statData.numPerPeriodList,  //新增数量/时间 列表
					type : 'bar'
				} ]
			};
			
			//显示数据
			if (option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}
		
	}  //end of object