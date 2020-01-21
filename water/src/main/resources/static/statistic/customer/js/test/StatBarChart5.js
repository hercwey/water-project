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
	function StatBarChart5(statisticData,chartId){		
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
				        trigger: 'axis'
				    },
				   /* toolbox: {
				        show: true,
				        feature: {
				            dataView: {show: true, readOnly: false},
				            magicType: {show: true, type: ['line', 'bar']},
				            restore: {show: true},
				            saveAsImage: {show: true}
				        }
				    },*/
				    calculable: true,
				    xAxis: [
				        {
				            type: 'category',
				            data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
				        }
				    ],
				    yAxis: [
				        {
				            type: 'value'
				        }
				    ],
				    series: [
				        {
				            name: '实收',
				            type: 'bar',
				            data: [2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
				          
				        },
				        {
				            name: '应收',
				            type: 'bar',
				            data: [2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
				          
				        }
				    ]
				};
			
			//显示数据
			if (option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}
		
	}  //end of object