/*
		显示折线图-前端
		构造函数参数:			
			statisticData 统计数据
			结构如下:	
			 * 封装后的结构如下所示: 
			 * legendList:['直接访问','邮件营销','联盟广告','视频广告','搜索引擎'], 
			 * statData:[
			 * 		{value:335, name:'直接访问'}, 
			 * 		{value:310, name:'邮件营销'}, 
			 * 		{value:234, name:'联盟广告'},
			 * 		{value:135, name:'视频广告'}, 
			 * 		{value:1548, name:'搜索引擎'} ]		
			
	*/
	function StatPieChart(statisticData,chartId){		
		var statData=statisticData;  	//统计数据
		var chartContainerId=chartId  	//统计图ID
			
		var title=""; 			//title标题
		var statPropName="";  	//统计属性名称
		
		/**
		 * 设置标题
		 */
		this.setTitle=function(titlex){
			title=titlex
		}
		
		/**
		 * 设置图例
		 */
		this.setStatPropName=function(statPropNamex){
			statPropName=statPropNamex
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
					title : {
				        text: title,			 //标题
				        x:'center'
				    },
				    tooltip: {
				        trigger: 'item',
				        formatter: '{a} <br/>{b}: {c} ({d}%)'
				    },
//				    legend: {
//				        orient: 'vertical',
//				        left: 1,
//				        data: ['居民生活用水', '非居民生活用水', '特种行业用水', '趸价水', '执行居民价格的非居民用水']
//				    },
				    series: [
				        {
				            name: '访问来源',
				            type: 'pie',
				            radius: ['50%', '70%'],
				            avoidLabelOverlap: false,
				            label: {
				                normal: {
				                    show: false,
				                    position: 'center'
				                },
				                emphasis: {
				                    show: true,
				                    textStyle: {
				                        fontSize: '30',
				                        fontWeight: 'bold'
				                    }
				                }
				            },
				            labelLine: {
				                normal: {
				                    show: false
				                }
				            },
				            data: [
				                {value: 335, name: '居民生活用水'},
				                {value: 310, name: '非居民生活用水'},
				                {value: 234, name: '特种行业用水'},
				                {value: 135, name: '趸价水'},
				                {value: 1548, name: '执行居民价格的非居民用水'}
				            ]
				        }
				    ]
				};
			
			//显示数据
			if (option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}
		
	}  //end of object