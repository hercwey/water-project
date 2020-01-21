
/**
 * 发票字段编辑器对象.用于发票字段的编辑.
 * @param invoiceId
 *            发票ID
 * @param fieldClasses
 * 			  参数示例如下:
 * 
 *            字段class属性 
 * 			  fieldClasses.col="buyer-name-col";
 *            fieldClasses.span="buyer-name";
 *            fieldClasses.editor="buyer-name-editor";
 *            fieldClasses.input="buyer-name-input";
 *            fieldClasses.btnEdit="btn-buyer-name";
 *            fieldClasses.btnClear="btn-buyer-name-clear";
 * 
 * @returns
 */
function InvoiceFieldEditor(invoiceId, fieldClasses) {

	console.log("new Field Editor.....");

	// const PREFIX = "#" + self.invoiceId + " ";
	this.PREFIX = "#" + invoiceId + " ";
	this.FIELD_COL = this.PREFIX + "." + fieldClasses.col; // 列
	this.FIELD_SPAN = this.PREFIX + "." + fieldClasses.span; // 显示span
	this.FIELD_EDITOR = this.PREFIX + "." + fieldClasses.editor; // 编辑器
	this.FIELD_INPUT = this.PREFIX + "." + fieldClasses.input; // 输入框
	this.BTN_EDIT = this.PREFIX + "." + fieldClasses.btnEdit; // 编辑按钮
	this.BTN_CLEAR = this.PREFIX + "." + fieldClasses.btnClear; // 清除按钮

	this.editStatus=false;  // 编辑状态  true:正在编辑; false:未在编辑;

	this.init();
	
	this.startEditFunc=null;  	//Event startEdit 回调函数
	this.endEditFunc=null;		//Event endEdit回调函数
}

//是否处理于编辑状态常量
InvoiceFieldEditor.EDITING_NO=false;
InvoiceFieldEditor.EDITING_YES=true;  
InvoiceFieldEditor.EVENT_START_EDIT="startEdit";  	//开始编辑事件
InvoiceFieldEditor.EVENT_END_EDIT="endEdit";  		//结束编辑事件

/**
 * 功能:
 * 	初始化编辑器组件
 * 	(1)事件绑定
 */
InvoiceFieldEditor.prototype.init=function(){
	this.initListener();
}

//----- editStatus(编辑状态): getter and setter-----------
InvoiceFieldEditor.prototype.getEditStatus=function(){
	return this.editStatus;
}

InvoiceFieldEditor.prototype.setEditStatus=function(status){
	this.editStatus=status;
}

//-------------自定义事件绑定-------------

/**
 * Event:startEdit
 */
InvoiceFieldEditor.prototype.addStartEditCallbackFunc=function(eventType,callbackFunc){
	this.startEditFunc=callbackFunc;
}

/**
 * Event:endEdit
 */
InvoiceFieldEditor.prototype.addEndEditCallbackFunc=function(eventType,callbackFunc){	
	this.endEditFunc=callbackFunc;
}

//------------------------------------
/**
 * 隐藏编辑器
 * 	(1)
 */
InvoiceFieldEditor.prototype.hideFieldEditor=function(){
	var self=this;

	//如果是编辑状态时则隐藏
	var isEditing=self.getEditStatus();
	if(isEditing){
		$(self.FIELD_SPAN).show();
		$(self.FIELD_EDITOR).hide();
		self.setEditStatus(InvoiceFieldEditor.EDITING_NO);
	}	
}

/**
 * 初始化监听器
 */
InvoiceFieldEditor.prototype.initListener=function(){
	var self=this;  //组件实例引用.

	console.log("initListener  function is called.................");

	// col 购方名称列mouse enter and leave event listener;
	console.log("field col is:"+this.FIELD_COL);
	
	$(self.FIELD_COL).on('mouseenter', fieldColMouseEnter);
	$(self.FIELD_COL).on('mouseleave', fieldColMouseLeave);
	$(self.FIELD_COL).on('click', fieldColClick);
	// button
	$(self.BTN_EDIT).on('click', fieldEdit);
	$(self.BTN_EDIT).on('keyup', fieldEditEsc);
	$(self.BTN_CLEAR).on('click', fieldClear);
	// input enter
	$(self.FIELD_INPUT).on('keyup', fieldInputEnter);

	/**
	 * 输入框中按回车
	 * @param {*} event 	 
	 */
	function fieldInputEnter(event){
		switch(event.which){
			case 13:  //enter key
				//(1)TODO SAVE DATA 采用endEdit 事件进行触发,在父层进行数据保存.
				triggerEndEditEvent();
				//(2)UI显示输入的值
				dispFieldFromInput();
				//(3)隐藏编辑器,显示编辑后值
				$(self.FIELD_EDITOR).hide();
				$(self.FIELD_SPAN).show();
				//(4)退出编辑状态
				self.setEditStatus(InvoiceFieldEditor.EDITING_NO);
				break;
			case 27:  //esc key  取消编辑
					self.hideFieldEditor();		
				break;
		}

		return false;
	}

	/**
	 * button-edit, keyup event listener
	 * @param {*} event 
	 */
	function fieldEditEsc(event){
		switch(event.which){
			case 27:  //esc键时取消编辑
				self.hideFieldEditor();		
				break;
		}
		return false;
	}

	/**
	 * 显示用户所输入的内容
	 */
	function dispFieldFromInput(){
		var inputValue=$(self.FIELD_INPUT).val();
		console.log("input value is:"+inputValue);
		var isEmpty=isNull(inputValue);
		if(isEmpty){			
			inputValue="";
		}
		
		$(self.FIELD_SPAN).html(inputValue);
	}
	
	/**
	 * 将span中的值赋值到input中
	 */
	function getFieldValueToInput(){
		var spanValue=$(self.FIELD_SPAN).html();
		console.log("span value is:"+spanValue);
		var isEmpty=isNull(spanValue);
		if(isEmpty){			
			spanValue="";
		}		
		$(self.FIELD_INPUT).val(spanValue);
	}
	
	/**
	 * 功能:判定字段是否为空
	 * 参数:str需要判定的字符串
	 */
	function isNull (str) {
		if (typeof (str) == "undefined" || str == null || str === "")
			return true;
		else
			return false;
	};
	
	

	//choice buyer 
	//todo: 调用外部选择购方的业务逻辑.
    function fieldEdit(){
		console.log("调用外部选择购方业务逻辑......,稍后处理");
	}

	/**
	 * clear input
	 * process flow:
	 * 	(1)clear input
	 * 	(2)set focus
	 */	
	function fieldClear(){
		//console.log("clear  btn is clicked!");
		$(self.FIELD_INPUT).val("");
		$(self.FIELD_INPUT)[0].focus();
	}

	/**
	 * mouse enter event listener
	 * 如果span可见时:
	 * 		span为不可见;
	 * 		编辑器可见
	 */
    function fieldColMouseEnter(){				

		if(self.getEditStatus()==InvoiceFieldEditor.EDITING_YES){
			//console.log("in mouse leave, edit status is:"+self.getEditStatus());
			return;
		}

		var isVisibleOfSpan = $(self.FIELD_SPAN).is(":visible");
		//console.log("field span visible status:"+isVisibleOfSpan);
		if(isVisibleOfSpan){
			console.log("field span is visible!");
			$(self.FIELD_SPAN).hide();
			$(self.FIELD_EDITOR).show();
			getFieldValueToInput();
		}		
	}

	/**
	 * mouse leave event listener
	 * 
	 */
	function fieldColMouseLeave(){
		//处于编辑状态时
        if(self.getEditStatus()==InvoiceFieldEditor.EDITING_YES){
        	//console.log("in mouse leave, edit status is:"+self.getEditStatus());
			return;
		}

		//console.log("mouse leave event is triggered!");	

		//如果未处于编辑状态时,则隐藏编辑器
		var isVisibleOfSpan = $(self.FIELD_SPAN).is(":visible");
		console.log("in mouse leave, field span visible is:"+isVisibleOfSpan);
		if(!isVisibleOfSpan){
			$(self.FIELD_SPAN).show();
			$(self.FIELD_EDITOR).hide();
	
		}
	}

	/**
	 * mouse click event listener
	 * 
	 */    
    function fieldColClick(){

		var isVisibleOfEditor=$(self.FIELD_EDITOR).is(":visible");
		var isEditing=self.getEditStatus();
		//如果编辑器可见,click列时即进入编辑状态.
		if(isVisibleOfEditor && !isEditing){
			console.log("进入编辑模式中enter edit mode..................");
			//getFieldValueToInput();
			self.setEditStatus(InvoiceFieldEditor.EDITING_YES);
			
			//生成自定义事件,而后在调用方进行监听
			triggerStartEditEvent();  //trigger startEdit event
		}
		else{
			//do nothing
		}
		return false;
		
	}

	//-------------trigger event and execute callback function--------------
	/**
	 * 触发 startEdit event
	 */
	function triggerStartEditEvent(){
		//先解除绑定,再绑定.
		$(self).unbind(InvoiceFieldEditor.EVENT_START_EDIT);		
		$(self).bind(InvoiceFieldEditor.EVENT_START_EDIT,function(){
			if(self.startEditFunc!=null){
				self.startEditFunc();
			}				
		});
		
		$(self).trigger(InvoiceFieldEditor.EVENT_START_EDIT);
	}

	/**
	 * 触发 endEdit event
	 */
	function triggerEndEditEvent(){
		//先解除绑定,再绑定.
		$(self).unbind(InvoiceFieldEditor.EVENT_END_EDIT);		
		$(self).bind(InvoiceFieldEditor.EVENT_END_EDIT,function(){
			var callbackFunc=self.endEditFunc;
			if(callbackFunc!=null){
				callbackFunc();
			}				
		});
		
		$(self).trigger(InvoiceFieldEditor.EVENT_END_EDIT);
	}


}


