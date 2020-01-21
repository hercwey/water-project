/*
		规则配置对象
		参数:
			editorId:编辑器ID
	*/
	function RuleConfig(editorId){
		//const
		this.RULE_INPUT_ID="#rule-input-"+editorId;          //input容器,用于存放标签
		this.BTN_SEARCH_MEBTER="#btn-search-meter"+editorId; //查询表计按钮
		//member
		this.mEditorId=editorId;  				//编辑器ID			
		//business 
		this.mRule=null;						//规则字符串
		this.mSearchMeterFunc=null;				//查询表计函数	
		
		this.mValid=true;						//规则是否有效.
	}

	/*
		功能:
			初始化函数
	*/
	RuleConfig.prototype.init=function(){
		console.log("mEditorId is"+this.mEditorId);	
		this.mInput = $(this.RULE_INPUT_ID);   //标签输入对象
		console.log("input id is:"+this.RULE_INPUT_ID);
		//console.log("mInput is:"+JSON.stringify(this.mInput));
		this.initInputProperties();
		this.initInputKeyupListener();
		this.initBtnSearchMeterClickListener(this);  
		this.initItemAddedListener();
		this.initItemRemovedListener();
	}
	
	/**
	 * 功能:
	 * 		设置查询表计回调函数
	 * 参数:
	 * 		查询表计函数(用户端提供)
	 * 		函数签名:
	 * 			function xx();
	 */
	RuleConfig.prototype.setSearchMeterFunc=function(searchFunc){
		this.mSearchMeterFunc=searchFunc;		
	}
	
	/*
		功能:
			初始input属性
	*/
	RuleConfig.prototype.initInputProperties=function(){
		this.mInput.tagsinput(
				{
					allowDuplicates: true,
					itemValue: 'value',
					itemText: 'text',				
				}
		);		
	}
	
	/*
		功能:
			初始化input的按键事件监控器
	*/
	RuleConfig.prototype.initInputKeyupListener=function(){
			var elt=this.mInput;

			elt.tagsinput('input').on('keyup',
				function(event){			
					if(event.keyCode ==13){
					addLabel();
					}
				}
			); 
		
		 //加入新的表达元素	
		 function addLabel(){
			console.log("key up is called");
			var inputx=elt.tagsinput('input').val();
			var data=new Object();
			var i=10;
			 data.value=i;		 
			 data.text=inputx;			 
			 elt.tagsinput('add',data);  		//加入新的label;
			 //elt.tagsinput('itemAdded');			 
			 elt.tagsinput('input').val("");  	//清除
		}	 
	}


	/*
		功能:
			init item added event listener
			初始化条目增加事件监听器
		
	 */
	RuleConfig.prototype.initItemAddedListener=function(){
		var elt=this.mInput;
		var that=this;

		console.log("init item")
		/**
		 * Triggered just after an item got added. Example:
			$('input').on('itemAdded', function(event) {
  			// event.item: contains the item
			});
		 */
		this.mInput.on('itemAdded',	function(event){			
			console.log('itemAdded is triggered!'); 
			that.validateRule();  //对表达式进行校验
		}); 
	}

	/*
		功能:
			初始化条目移除时事件监听器
	 */	
	RuleConfig.prototype.initItemRemovedListener=function(){
		var elt=this.mInput;
		that=this;
		this.mInput.on('itemRemoved',	function(event){			
			console.log('itemRemoved is triggered!');						
			that.validateRule();  //对表达式进行校验
		}); 
	}

	/**
	 * 功能:
	 * 		校验当前的rule
	 * 		方案:在后台进行校验
	 * 返回:
	 * 		如果校验成功,则会置mValid=true;否则置mValid=false;
	 */

	RuleConfig.prototype.validateRule=function(){
		//校验的URL
		var url="/rule/validate";  

		//准备校验参数
		var param=new Object();
		var tokenArr=this.getInputItemsText();
		var that=this;

		//生成rule字符串
		var ruleStr=""
		for(var i=0;i<tokenArr.length;i++){
			ruleStr=ruleStr+tokenArr[i];
		}		

		//准备查询参数
		param.ruleStr=ruleStr;

		//发送请求,请求时返回结构为json格式
		$.post(url,param,validRequestCallback);

		
		/**
		 * 回调函数
		 * @param {*} response 
		 */
		function validRequestCallback(response){
			if (response != null && response != "") {
				if (response.result_code == "success") {
					that.mValid=true;
				} else {
					//dialog type: success warning info error,默认为info
					//util.message(res.result_msg,null,"warning");
					that.mValid=false;
				}
			}
		}
	}

	
	/**
	 * 功能:
	 * 		设置查询表计按钮click's listener
	 */
	RuleConfig.prototype.initBtnSearchMeterClickListener=function(that){		
		$(this.BTN_SEARCH_MEBTER).on('click',function(){
			that.mSearchMeterFunc(that.mEditorId);
		});		 
	}
	
	/*
		功能:
			设置规则
		参数:
			rule 字符串类型,约定采用字符串格式进行分隔.		
	*/
	RuleConfig.prototype.setRule=function(rule){
		this.mRule=rule;
		this.removeAll(this.mInput);  //清除所有
		this.displayRule(this.mRule,this.mInput);  //加入规则
	}
	
	/*
		功能:
			清除所有项
		参数:
			inputElt 容器对象
	*/
	RuleConfig.prototype.removeAll=function(inputElt){
		inputElt.tagsinput('removeAll');
	}
	
	/*
		功能:
			显示规则
		参数:
			rule:规则字符串
			inputElt:容器对象
		
	*/
	RuleConfig.prototype.displayRule=function(rule,inputElt){
		if(rule!=null){
			var tokenArr=rule.split(",");
			console.log("rule token arr is:"+JSON.stringify(tokenArr));
			for(var i=0;i<tokenArr.length;i++){
				var data=new Object();				
				 data.value=i;		 
				 data.text=tokenArr[i];			 
				 this.addToken(inputElt,data);
			}
		}		
	}
	
	/*
	 	功能:
	 		向input中增加token
	  	参数:
	  		inputElt容器
	  		token 字符串类型
	 */
	RuleConfig.prototype.addToken=function(inputElt,data){
		inputElt.tagsinput('add',data);  		//加入新的label;
	}
	
	/*
		功能:
			获取input中的value值
		返回值:
			逗号分隔的字符串
	*/
	RuleConfig.prototype.getInputValue=function(){
		 var val=mInput.val();
		 return val;
	}
	
	/*
	功能:
		获取input的列表
	返回值:
		结构:
			[{"value":3,"text":"1"},{"value":4,"text":"1"}]
	注:	value的值在业务中暂未使用.
	*/
	RuleConfig.prototype.getInputItems=function(){
		var arr=this.mInput.tagsinput('items');
		return arr;
	}

	/**
	 	功能:
		 	获取input  item's text列表
		返回:
			返回item's text列表(array)
			形式如下:  1,2,3,4
			如果itemg列表为空时,则返回一个长度为0的数组
	 */
	RuleConfig.prototype.getInputItemsText=function(){
		var itemArr=this.mInput.tagsinput('items');
		var textArr=new Array();
		for(var i=0;i<itemArr.length;i++){
			var item=itemArr[i];
			textArr.push(item.text);
		}
		return textArr;
	}

	/**
	 * 功能:
	 * 		获取当前规则有效性;
	 */
	RuleConfig.prototype.getValid=function(){
		return this.mValid;
	}

	
	/*
		功能:
			获取用户在输入框中的输入
	*/	
	RuleConfig.prototype.getUserInput=function(){
		var inputx=this.mInput.tagsinput('input').val();
		return inputx;
	}