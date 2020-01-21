/**
 * 发票库存对象
 */

function InvoiceInventory(){
	/*{
	    "RETCODE": "3011",
	    "RETMSG": "3011-金税盘状态信息读取成功 [TCD_0_15,]",
	    "FPDM": "1300141140",
	    "FPHM": "14907748",
	    "KCFPSL": "1000",
	    "JSSBRQ": "2019-10-21 11:55:23",
	    "XFSH": "130100999999220",
	    "SCFS": "1",
	    "KPDH": "1",
	    "CSQBZ": "0",
	    "SSQBZ": "0",
	    "KPFS": "0",
	    "KPFWQH": "",
	    "JSPH": "661555297643",
	    "XFMC": "航信培训企业"
	}*/
	
	this.RETCODE="";	
    this.RETMSG="";
    this.FPDM="";
    this.FPHM="";
    this.KCFPSL="";
    this.JSSBRQ="";
    this.XFSH="";
    this.SCFS="";
    this.KPDH=""
    this.CSQBZ="";
    this.SSQBZ="";    
    this.KPFS="";
    this.KPFWQH="";
    this.JSPH="";
    this.XFMC="";	
}

//----------getter and setter--------------
//RETCODE
InvoiceInventory.prototype.getRETCODE=function(){
	return this.RETCODE;
}
InvoiceInventory.prototype.setRETCODE=function(RETCODE){
	this.RETCODE=RETCODE;
}

//RETMSG
InvoiceInventory.prototype.getREGMSG=function(){
	return this.RETMSG;
}

InvoiceInventory.prototype.setRETMSG=function(RETMSG){
	this.RETMSG=RETMSG;
}

//FPDM
InvoiceInventory.prototype.getFPDM=function(){
	return this.FPDM
}

InvoiceInventory.prototype.setFPDM=function(FPDM){
	this.FPDM=FPDM;
}

//FPHM
InvoiceInventory.prototype.getFPHM=function(){
	return this.FPHM
}

InvoiceInventory.prototype.setFPHM=function(FPHM){
	this.FPHM=FPHM;
}

//KCFPSL
InvoiceInventory.prototype.getKCFPSL=function(){
	return this.KCFPSL;
}
InvoiceInventory.prototype.setKCFPSL=function(KCFPSL){
	this.KCFPSL=KCFPSL;
}

//JSSBRQ
InvoiceInventory.prototype.getJSSBRQ=function(){
	return this.JSSBRQ;
}
InvoiceInventory.prototype.setJSSBRQ=function(JSSBRQ){
	this.JSSBRQ=JSSBRQ;
}

//XFSH
InvoiceInventory.prototype.getXFSH=function(){
	return this.XFSH;
}
InvoiceInventory.prototype.setXFSH=function(XFSH){
	this.XFSH=XFSH;
}

//SCFS
InvoiceInventory.prototype.getSCFS=function(){
	return this.SCFS;
}
InvoiceInventory.prototype.setSCFS=function(SCFS){
	this.SCFS=SCFS;
}

//KPDH
InvoiceInventory.prototype.getKPDH=function(){
	return this.KPDH;
}
InvoiceInventory.prototype.setKPDH=function(KPDH){
	this.KPDH=KPDH;
}

//CSQBZ
InvoiceInventory.prototype.getCSQBZ=function(){
	return this.CSQBZ;
}
InvoiceInventory.prototype.setCSQBZ=function(CSQBZ){
	this.CSQBZ=CSQBZ;
}

//SSQBZ
InvoiceInventory.prototype.getSSQBZ=function(){
	return this.SSQBZ;
}
InvoiceInventory.prototype.setSSQBZ=function(SSQBZ){
	this.SSQBZ=SSQBZ;
}

//KPFS
InvoiceInventory.prototype.getKPFS=function(){
	return this.KPFS;
}
InvoiceInventory.prototype.setKPFS=function(KPFS){
	this.KPFS=KPFS
}

//KPFWQH
InvoiceInventory.prototype.getKPFWQH=function(){
	return this.KPFWQH;
}
InvoiceInventory.prototype.setKPFWQH=function(KPFWQH){
	this.KPFWQH=KPFWQH;
}

//JSPH
InvoiceInventory.prototype.getJSPH=function(){
	return this.JSPH;
}
InvoiceInventory.prototype.setJSPH=function(JSPH){
	this.JSPH=JSPH;
}

//XFMC
InvoiceInventory.prototype.getXFMC=function(){
	return this.XFMC;
}
InvoiceInventory.prototype.setXFMC=function(XFMC){
	this.XFMC=XFMC;
}
