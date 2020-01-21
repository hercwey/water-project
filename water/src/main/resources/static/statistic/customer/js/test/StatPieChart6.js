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
	function StatPieChart6(statisticData,chartId){		
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
				        formatter: '{a} <br/>{b} : {c} ({d}%)'
				    },

				    visualMap: {
				        show: false,
				        min: 80,
				        max: 600,
				        inRange: {
				            colorLightness: [0, 1]
				        }
				    },
				    series: [
				        {
				            name: '非居民用水',
				            type: 'pie',
				            radius: '55%',
				            center: ['50%', '50%'],
				            data: [
				                {value: 335, name: '商业用水'},
				                {value: 310, name: '绿化用水'},
				                {value: 274, name: '施工用水'},
				                {value: 235, name: '环卫用水'},
				                {value: 400, name: '消防用水'}
				            ].sort(function (a, b) { return a.value - b.value; }),
				            roseType: 'radius',
				            label: {
				                color: 'rgba(255, 255, 255, 0.3)'
				            },
				            labelLine: {
				                lineStyle: {
				                    color: 'rgba(255, 255, 255, 0.3)'
				                },
				                smooth: 0.2,
				                length: 10,
				                length2: 20
				            },
				            itemStyle: {
				                color: '#c23531',
				                shadowBlur: 200,
				                shadowColor: 'rgba(0, 0, 0, 0.5)'
				            },
				          
				        }
				    ]
				};
			
			//显示数据
			if (option && typeof option === "object") {
				myChart.setOption(option, true);
			}
		}
		
	}  //end of object