/**
 * 发票详情(商品)
 * 
 * @returns
 */
function InvoiceDetail() {

	/*  
	  	SPMC	商品名称	字符	92	是	
		HSBZ	含税标志	字符	2	是	固定值
								0：不含税
								1：含税
		SLV		税率		数值	10,6	是	
		JE		金额		数值	16,2	是	
		DJ		单价		数值	36,15	否	
		JLDW	计量单位	字符	22	否	
		GGXH	规格型号	字符	40	否	
		SE		税额		数值	16,2	是	
		SL		数量		数值	36,15	否	
		BMBBH	编码版本号	字符		是	详见国税局网站发布的分类编码表格
		SSFLBM	税收分类编码	字符		是	详见国税局网站发布的分类编码表格
		YHZC	是否享受优惠政策	字符		是	固定值
											0：不享受
											1：享受
		YHZCNR	享受优惠政策内容	字符		否	如果不享受优惠政策，该字段为空；
				如果享受优惠政策，该字段不能为空，内容填写为对应的优惠政策
		LSLBS	零税率标识	字符		否	固定值
									空：非零税率
									0：出口退税
									1：免税
									2：不征税
									3：普通零税率
		QYZBM	企业自编码	字符		否	企业内部自编的商品编码，可以为空
		KCE		扣除额		数值		否	差额税使用，非差额税可以为空
	 */

	this.SPMC = "自来水"; // 商品名称
	this.HSBZ = "1"; // 含税标志  水司默认为含税价  0:不含税  1:含税   采用默认值
	this.SLV = "0.03"; // 税率
	this.JE = ""; // 金额
	this.DJ=""; // 单价
	this.JLDW="吨"; // 计量单位  采用默认值
	this.GGXH=""; // 规格型号 不必设置
	this.SE=""; // 税额  系统会自动计算
	this.SL=""; // 数量  需要设置值
	
	this.BMBBH="33.0"; //编码版本号  不必设置值   采用默认值
	
	this.SSFLBM="110030101"; // 税收分类编码 采用默认值
	this.YHZC="0"; // 是否享受优惠政策 [优惠政策],默认值为不享受  采用默认值
	this.YHZCNR=""; // 享受优惠政策内容  采用默认值
	this.LSLBS=""; // 零税率标识 空为非零税率  采用默认值
	this.QYZBM=""; // 企业自编码  采用默认值
	this.KCE=""; // 扣除额  采用默认值	

}

//-------------getter and setter-------------

//商品名称
InvoiceDetail.prototype.getSPMC = function() {
	return this.SPMC;
}

InvoiceDetail.prototype.setSPMC = function(spmc) {
	this.SPMC = spmc;
}

// 含税标志  含税标志
InvoiceDetail.prototype.getHSBZ=function(){
	return this.HSBZ;
}
InvoiceDetail.prototype.setHSBZ=function(hsbz){
	this.HSBZ=hsbz;
}

// 税率
InvoiceDetail.prototype.getSLV=function(){
	return this.SLV;
}
InvoiceDetail.prototype.setSLV=function(slv){
	this.SLV=slv;
}

// 金额
InvoiceDetail.prototype.getJE=function(){
	return this.JE;
}
InvoiceDetail.prototype.setJE=function(je){
	this.JE=je;
}

// 单价
InvoiceDetail.prototype.getDJ=function(){
	return this.DJ; // 单价
}
InvoiceDetail.prototype.setDJ=function(dj){
	this.DJ=dj;
}

// 计量单位
InvoiceDetail.prototype.getJLDW=function(){
	return this.JLDW;
}
InvoiceDetail.prototype.setJLDW=function(jldw){
	this.JLDW=jldw;
}

// 规格型号
InvoiceDetail.prototype.getGGXH=function(){
	return this.GGXH;
}
InvoiceDetail.prototype.setGGXH=function(ggxh){
	this.GGXH=ggxh;
}

// 税额
InvoiceDetail.prototype.getSE=function(){
	return this.SE;
}
InvoiceDetail.prototype.setSE=function(se){
	this.SE=se;
}

// 数量
InvoiceDetail.prototype.getSL=function(){
	return this.SL;
}
InvoiceDetail.prototype.setSL=function(sl){
	this.SL=sl;
}

// 编码版本号
InvoiceDetail.prototype.getBMBBH=function(){
	return this.BMBBH;
}
InvoiceDetail.prototype.setBMBBH=function(bmbbh){
	this.BMBBH=bmbbh;
}

// 税收分类编码
InvoiceDetail.prototype.getSSFLBM=function(){
	return this.SSFLBM;
}
InvoiceDetail.prototype.setSSFLBM=function(ssflbm){
	this.SSFLBM=ssflbm;
}

// 是否享受优惠政策 [优惠政策]
InvoiceDetail.prototype.getYHZC=function(){
	return this.YHZC;
}
InvoiceDetail.prototype.setYHZC=function(yhzc){
	this.YHZC=yhzc;
}

 // 享受优惠政策内容
InvoiceDetail.prototype.getYHZCNR=function(){
	return this.YHZCNR;
}
InvoiceDetail.prototype.setYHZCNR=function(yhzcnr){
	this.YHZCNR=yhzcnr;
}

// 零税率标识
InvoiceDetail.prototype.getLSLBS=function(){
	return this.LSLBS;
}
InvoiceDetail.prototype.setLSLBS=function(lslbs){
	this.LSLBS=lslbs;
}

 // 企业自编码
InvoiceDetail.prototype.getQYZBM=function(){
	return this.QYZBM;
}
InvoiceDetail.prototype.setQYZBM=function(qyzbm){
	this.QYZBM=qyzbm;
}

// 扣除额
InvoiceDetail.prototype.getKCE=function(){
	return this.KCE;
}
InvoiceDetail.prototype.setKCE=function(kce){
	this.KCE=kce;
}
