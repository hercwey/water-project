/**
 * 打印参数对象: 与后台的PrintParam相对应.
	 	DYJMC	打印机名称	字符	92	否	为空时使用开票软件默认打印机
		QDDYFS	清单打印方式	字符	2	是	固定值：
									1：全打
									0：套打
		LEFT	左右偏移量	数值	16,2	否	为空时默认为 0
		TOP	上下偏移量	数值	10,6	否	为空时默认为 0
*/
function PrintParam(){
	this.DYJMC="";		//采用默认打印机打印.
	this.QDDYFS="0";  	//默认采用套打
	this.LEFT="0";		
	this.TOP="0";
}

//----------打印机名称---------------
PrintParam.prototype.getDYJMC=function(){
	return this.DYJMC;
}

/**
 * dyjmc:打印机名称
 */
PrintParam.prototype.setDYJMC=function(dyjmc){
	return this.DYJMC=dyjmc;
}

//------------清单打印方式 getter and setter-----------
PrintParam.prototype.getQDDYFS=function(){
	return this.QDDYFS;
}

/**
 * qddyfs:清单打印方式
 * 				必填字段 
 * 				1：全打
				0：套打
 */
PrintParam.prototype.setQDDYFS=function(qddyfs){
	return this.QDDYFS=qddyfs;
}

//------------左侧偏移量-------------
PrintParam.prototype.getLEFT=function(){
	return this.LEFT;
}

PrintParam.prototype.setLEFT=function(left){
	return this.LEFT=left;
}
//------------右侧偏移量-------------
PrintParam.prototype.getRIGHT=function(){
	return this.RIGHT;
}

PrintParam.prototype.setRIGHT=function(right){
	return this.RIGHT=right;
}

