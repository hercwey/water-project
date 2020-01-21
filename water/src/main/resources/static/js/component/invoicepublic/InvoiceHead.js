//发票头对象
/**
 * 发票头对象
 * @returns
 */
function InvoiceHead(){
	/* 
	 * FPZL	发票种类	字符	2	是	固定值：
							0：专用发票 
							2：普通发票
							12：机动车票
							51：电子发票
		GFMC	购方名称	字符	100	是	
		GFSH	购方税号	字符	20	是	
		GFDZDH	购方地址及电话	字符	100	否	
		GFYHZH	购方银行及账户	字符	100	否	
		BZ	备注	字符	230	否	专票开具红字发票时，该字段需要填写如下内容：
							开具红字增值税专用发票信息表编号XXXXXXXXXXXXXXXX
		
							XXXX表示信息表编号，备注内容为以上那句话，不能有差错，否则无法开具红字发票。
		
							普票开具负数发票时，该字段需要填写如下内容：
							对应正数发票代码:YYYYYYYY号码:ZZZZZZ
		
							YYYYY：是发票代码
							ZZZZ:是发票号码
							普票负数票备注除这句内容外可以有其他内容
		SKR	收款人	字符	8	是	8个字节，4个汉字
		FHR	复核人	字符	8	是	8个字节，4个汉字
		KPR	开票人	字符	8	是	8个字节，4个汉字
		XFYHZH	销方银行及账户	字符	100	否	
		XFDZDH	销方地址及电话	字符	100	否	
		QDBZ	清单标志	字符	2	是	固定值
								0：不开具清单
								1：开具清单
		XSDJBH	销售单据编号	字符	50	否	该字段的内容保存于开票客户端中，不做逻辑处理，内容不做限制，由接口调用方根据需要填写内容
		KPBZ	开票标志	字符	2	是	固定值
									0：开票
									1：校验
		JPGG	卷票规格	字符	2	否	卷票时，该节点必填；
									固定值：
									76：76mm*177mm；
									57：57mm*177mm。
									卷票时，若该节点不填或填其他值，则默认为76mm*177mm规格的卷票。
	 */	
	
	this.FPZL="";	 	//发票种类
	this.GFMC="";		//购方名称
	this.GFSH="";		//购方税号
	this.GFDZDH="";	  	//购方地址及电话
	this.GFYHZH="";	  	//购方银行账号
	this.BZ="";	      	//备注
	this.SKR="";	  	//收款人
	this.FHR="";	  	//复核人
	this.KPR="";	  	//开票人
	this.XFYHZH="";	 	//销方银行及账号
	this.XFDZDH="";	  	//销方地址及电话
	this.QDBZ="0";	  	//清单标志,默认值:0-不开具清单
	this.XSDJBH="";	  	//销售单据编号
	this.KPBZ="0";	  	//开票标志. 默认值: 0-开票  1:校验
	this.JPGG="";   	//卷票规格  此值可以不填写.采用默认值,而后进行测试.	
}

//-------------------getter and setter-----------------
//----发票种类 getter and setter------
InvoiceHead.prototype.getFPZL=function(){	
	return this.FPZL;
}
InvoiceHead.prototype.setFPZL=function(fpzl){	
	this.FPZL=fpzl;
}
//----购方名称 getter and setter-----
InvoiceHead.prototype.getGFMC=function(){	
	return this.GFMC;
}
InvoiceHead.prototype.setGFMC=function(gfmc){
	this.GFMC=gfmc;
}


//------购方税号 getter and setter------
InvoiceHead.prototype.getGFSH=function(){	
	return this.GFSH;
}
InvoiceHead.prototype.setGFSH=function(gfsh){	
	this.GFSH=gfsh;
}

//------购方地址及电话 getter and setter-----
InvoiceHead.prototype.getGFDZDH=function(){
	console.log("get 购方地址电话!......."+this.GFDZDH);
	/*
	if(typeof this.GFDZDH == "undefined" || this.GFDZDH==null){
		console.log("get 购方地址电话!....返回空字符串");
		return "";
	}	
	else{
		return this.GFDZDH;
	}*/
	return this.getStr(this.GFDZDH);
}

InvoiceHead.prototype.getStr=function(data){
    if(!data){
        return '';
    }else if(typeof(data) == "undefined"){
        return '';
    }

    return data.toString();
}


InvoiceHead.prototype.setGFDZDH=function(gfdzdh){	
	this.GFDZDH=gfdzdh;
}


// -------购方银行及账号 getter and setter------
InvoiceHead.prototype.getGFYHZH=function(){
	if(typeof this.GFYHZH == "undefined" || this.GFYHZH==null){
		return "";
	}	
	else{
		return this.GFYHZH;
	}
}
InvoiceHead.prototype.setGFYHZH=function(gfyhzh){
	this.GFYHZH=gfyhzh;
}

//-----备注 getter and setter------
InvoiceHead.prototype.getBZ=function(){
	return this.BZ;
}

InvoiceHead.prototype.setBZ=function(bz){
	this.BZ=bz;
}

//收款人
InvoiceHead.prototype.getSKR=function(){
	return this.SKR;
}	  	
InvoiceHead.prototype.setSKR=function(skr){
	this.SKR=skr;
}

//复核人
InvoiceHead.prototype.getFHR=function(){
	return this.FHR;
}
InvoiceHead.prototype.setFHR=function(fhr){
	this.FHR=fhr;
}

//开票人
InvoiceHead.prototype.getKPR=function(){
	return this.KPR;
}

InvoiceHead.prototype.setKPR=function(kpr){
	this.KPR=kpr;
}


//销方银行及账号
InvoiceHead.prototype.getXFYHZH=function(){
	return this.XFYHZH;
}
InvoiceHead.prototype.setXFYHZH=function(xfyhzh){
	this.XFYHZH=xfyhzh;
}

//销方地址及电话
InvoiceHead.prototype.getXFDZDH=function(){
	return this.XFDZDH;
}
InvoiceHead.prototype.setXFDZDH=function(xfdzdh){
	this.XFDZDH=xfdzdh;
}


//清单标志,默认值:0-不开具清单
InvoiceHead.prototype.getQDBZ=function(){
	return this.QDBZ;
}
InvoiceHead.prototype.setQDBZ=function(qdbz){
	this.QDBZ=qdbz;
}

//销售单据编号
InvoiceHead.prototype.getXSDJBH=function(){
	return this.XSDJBH;
}
InvoiceHead.prototype.setXSDJBH=function(xsdjbh){
	this.XSDJBH=xsdjbh;
}
//开票标志. 默认值: 0-开票
InvoiceHead.prototype.getKPBZ=function(){
	return this.KPBZ;
}
InvoiceHead.prototype.setKPBZ=function(kpbz){
	this.KPBZ=kpbz;
}
//此值可以不填写.采用默认值,而后进行测试.
InvoiceHead.prototype.getJPGG=function(){
	return this.JPGG;
}
InvoiceHead.prototype.setJPGG=function(jpgg){
	this.JPGG=jpgg;
}
//复位发票头 added by hz 2019/12/16
InvoiceHead.prototype.reset=function(){
	//this.FPZL="";	 	//发票种类
	this.GFMC="";		//购方名称
	this.GFSH="";		//购方税号
	this.GFDZDH="";	  	//购方地址及电话
	this.GFYHZH="";	  	//购方银行账号
	this.BZ="";	      	//备注
	//this.SKR="";	  	//收款人
	//this.FHR="";	  	//复核人
	//this.KPR="";	  	//开票人
	this.XFYHZH="";	 	//销方银行及账号
	this.XFDZDH="";	  	//销方地址及电话
	this.QDBZ="0";	  	//清单标志,默认值:0-不开具清单
	this.XSDJBH="";	  	//销售单据编号
	this.KPBZ="0";	  	//开票标志. 默认值: 0-开票  1:校验
	this.JPGG="";   	//卷票规格  此值可以不填写.采用默认值,而后进行测试.	
}

