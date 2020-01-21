package com.learnbind.ai.ccb.responsepacket;

/**
 * 响应报文-转账结果(查询)
 * @author lenovo
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 */
public class ResponseTransferResult {	
	/**
	 * 交易日期
	 */
	private String Txn_Dt;   // <![CDATA[20181009]]></Txn_Dt>
	/**
	 * 交易时间
	 */
	private String Txn_Tm;   // <![CDATA[150933]]></Txn_Tm>
	/**
	 * 定制交易编号
	 */
	private String Csz_Txn_ID;   // <![CDATA[]]></Csz_Txn_ID>
	/**
	 * 定制交易名称
	 */
	private String Csz_Txn_Nm;   // <![CDATA[]]></Csz_Txn_Nm>
	/**
	 * #现金管理交易流水号
	 */
	private String CshMgt_TxnSrlNo;   // <![CDATA[CN000s61201810091509334912307176]]></CshMgt_TxnSrlNo>
	/**
	 * #流程业务标识
	 */
	private String WF_BIZ_ID;   // <![CDATA[]]></WF_BIZ_ID>
	/**
	 * 组件关联流水号
	 */
	private String Cmpt_Rltv_Jrnl_No;   // <![CDATA[]]></Cmpt_Rltv_Jrnl_No>
	/**
	 * -----------------
	 * 客户方交易流水号
	 * ----------------
	 * 注:可以根据此号在客户业务端进行查询.
	 */
	private String CstPty_TxnSrlNo;   // <![CDATA[1484304938975]]></CstPty_TxnSrlNo>
	/**
	 * 付款方现金客户树编号
	 */
	private String Pyr_CCstTr_ID;   // <![CDATA[CMN0000093068]]></Pyr_CCstTr_ID>
	/**
	 * 付款方现金客户树节点编号
	 */
	private String Pyr_CCstTrNdID;   // <![CDATA[ND97385045830423497300001]]></Pyr_CCstTrNdID>
	/**
	 * 付款方开户行名称
	 */
	private String Pyr_DpBkNm;   // <![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></Pyr_DpBkNm>
	/**
	 * 付款方开户行号
	 */
	private String Pyr_DepBnk_No;   // <![CDATA[130618801]]></Pyr_DepBnk_No>
	/**
	 * 付款方联行号
	 */
	private String Pyr_BnkCD;   // <![CDATA[]]></Pyr_BnkCD>
	/**
	 * 付款方行别代码
	 */
	private String Pyr_BkCgyCd;   // <![CDATA[01]]></Pyr_BkCgyCd>
	/**
	 * 付款方客户账号
	 */
	private String Pyr_Cst_AccNo;   // <![CDATA[13001618801050519642]]></Pyr_Cst_AccNo>
	/**
	 * 付款方账户名称
	 */
	private String Pyr_AccNm;   // <![CDATA[公司某某]]></Pyr_AccNm>
	/**
	 * 付款方账户类别代码
	 */
	private String Pyr_Acc_CgyCd;   // <![CDATA[02]]></Pyr_Acc_CgyCd>
	/**
	 * 付款方地址
	 */
	private String Pyr_Adr;   // <![CDATA[]]></Pyr_Adr>
	
	/**
	 * 收款方现金客户树编号
	 */
	private String RcvPrt_CCstTr_ID;   // <![CDATA[]]></RcvPrt_CCstTr_ID>
	/**
	 * 收款方现金客户树节点编号
	 */
	private String RcvPrt_CCstTrNdID;   // <![CDATA[]]></RcvPrt_CCstTrNdID>
	/**
	 * 收款方开户行名称
	 */
	private String RcvPrt_DpBkNm;   // <![CDATA[中国建设银行股份有限公司秦皇岛住房城建支行]]></RcvPrt_DpBkNm>
	/**
	 * 收款方开户行号
	 */
	private String RcvPrt_DepBnk_No;   // <![CDATA[130635808]]></RcvPrt_DepBnk_No>
	/**
	 * 收款方联行号
	 */
	private String RcvPrt_BnkCD;   // <![CDATA[]]></RcvPrt_BnkCD>
	/**
	 * 收款方行别代码
	 */
	private String RcvPrt_BkCgyCd;   // <![CDATA[01]]></RcvPrt_BkCgyCd>
	/**
	 * 收款方客户账号
	 */
	private String RcvPrt_Cst_AccNo;   // <![CDATA[13001635808050514014]]></RcvPrt_Cst_AccNo>
	/**
	 * 收款方账户名称
	 */
	private String RcvPtAc_Nm;   // <![CDATA[公司九七]]></RcvPtAc_Nm>
	/**
	 * 收款方账户类别代码
	 * DF数据格式:2!n
			01：对私；02：对公；03：银行
			内部户DF数据格式：2!n
	 */
	private String RcvPtAc_CgyCd;   // <![CDATA[02]]></RcvPtAc_CgyCd>
	/**
	 * 收款方地址
	 */
	private String RcvPrt_Adr;   // <![CDATA[]]></RcvPrt_Adr>
	/**
	 * 请求金额
	 */
	private String Rqs_Amt;   //190.00</Rqs_Amt>
	/**
	 * 发生额
	 */
	private String HpnAm;   //190.00</HpnAm>
	/**
	 * 加急类型代码  DF数据格式:2!n
01-普通 02-加急
	 */
	private String Urgnt_TpCd;   // <![CDATA[01]]></Urgnt_TpCd>
	/**
	 * 用途名称
	 */
	private String Use_Nm;   // <![CDATA[]]></Use_Nm>
	/**
	 * 现金管理用途编号
	 */
	private String CshMgt_Use_ID;   // <![CDATA[]]></CshMgt_Use_ID>
	/**
	 * 备注
	 */
	private String Rmrk;   // <![CDATA[11]]></Rmrk>
	/**
	 * 收付款预算用途编号
	 */
	private String RvPy_Bdgt_Use_ID;   // <![CDATA[]]></RvPy_Bdgt_Use_ID>
	/**
	 * 收付款预算用途名称
	 */
	private String RvPy_Bdgt_Use_Nm;   // <![CDATA[]]></RvPy_Bdgt_Use_Nm>
	/**
	 * 触发金额
	 * DF数据格式:20n(2)
付款条件类型代码为1-5时，此
项必输DF数据格式：20n(2)
	 */
	private String Trgr_Amt;            //>0.00</Trgr_Amt>
	/**
	 * 付款条件类型代码
	 * DF数据格式:2!n
00：定金额；01：定余额
；02：零余额；03：定比例
；04：取整；05：定余额取整
；付款方行别代码为02国内他行
或03国外他行时，此项值仅允许
为00定金额；DF数据格式：2!n
	 */
	private String Py_Cnd_TpCd;   // <![CDATA[00]]></Py_Cnd_TpCd>
	/**
	 * 定余额值
	 * DF数据格式:20n(2)
付款条件类型代码为01定余额或
05定余额取整时，此项必输
DF数据格式：20n(2)
	 */
	private String STBal_Val;   //		0.00</STBal_Val>
	/**
	 * 定比例系数 DF数据格式:9n(5)
付款条件类型代码为03定比例时
，此项必输DF数据格式：9n(5)
	 */
	private String Scl_Fctr;   //		0.00000</Scl_Fctr>
	/**
	 * 取整位数  DF数据格式:n..2
付款条件类型代码为04取整或
05定余额取整时，此项必输
DF数据格式：n..2
	 */
	private String Roud_NbrOfBit;   // <![CDATA[]]></Roud_NbrOfBit>
	/**
	 * 收付款执行方式代码
	 * DF数据格式:1!n
0：实时1：定时2：定频DF数据
格式：1!n
	 */
	private String RvPy_ExMd_Cd;   // <![CDATA[0]]></RvPy_ExMd_Cd>
	/**
	 * 预约执行日期
	 * DF数据格式:yyyymmdd
（定时执行日期）收付款执行方
式代码为1定时时，此项必输
DF数据格式：yyyymmdd
	 */
	private String Rsrvtn_Exec_Dt;   // <![CDATA[]]></Rsrvtn_Exec_Dt>
	/**
	 * 预约执行时间
	 * DF数据格式:hhmmss
（定时/定频执行时间）收付款
执行方式代码为1定时或2定频时
，此项必输DF数据格式
：hhmmss
	 */
	private String Rsrvtn_Exec_Tm;   // <![CDATA[]]></Rsrvtn_Exec_Tm>
	/**
	 * 频率方式代码 DF数据格式:1!n
1：日；2：周；3：月；收付款
执行方式代码为2定频时，此项
必输DF数据格式：1!n
	 */
	private String Frq_MtdCd;   // <![CDATA[]]></Frq_MtdCd>
	/**
	 * #定频执行日期描述
	 * DF数据格式:anc..200
当频率方式代码为2周或3月时
，此项必输； 频率方式代码为
2月时，取值1-31（1代表1号
，31代表月末）；频率方式代码
为3周时，取值1-7（1代表周一
，7代表周日）；各执行日期之
间用“,”隔开，如:1,15表示选
择每月1号、15号；DF数据格式
：anc..200
	 */
	private String FxFrq_Exec_Dt_Dsc;   // <![CDATA[]]></FxFrq_Exec_Dt_Dsc>
	/**
	 * #个性化信息一
	 */
	private String Prsz_Inf_1;   // <![CDATA[]]></Prsz_Inf_1>
	private String Prsz_Inf_2;   // <![CDATA[]]></Prsz_Inf_2>
	private String Prsz_Inf_3;   // <![CDATA[]]></Prsz_Inf_3>
	private String Prsz_Inf_4;   // <![CDATA[]]></Prsz_Inf_4>
	private String Prsz_Inf_5;   // <![CDATA[]]></Prsz_Inf_5>
	private String Prsz_Inf_6;   // <![CDATA[]]></Prsz_Inf_6>
	private String Prsz_Inf_7;   // <![CDATA[]]></Prsz_Inf_7>
	private String Prsz_Inf_8;   // <![CDATA[]]></Prsz_Inf_8>
	private String Prsz_Inf_9;   // <![CDATA[]]></Prsz_Inf_9>
	private String Prsz_Inf_10;   // <![CDATA[]]></Prsz_Inf_10>
	/**
	 * #个性化信息名称一
	 */
	private String Prsz_Inf_Nm_1;   // <![CDATA[]]></Prsz_Inf_Nm_1>
	private String Prsz_Inf_Nm_2;   // <![CDATA[]]></Prsz_Inf_Nm_2>
	private String Prsz_Inf_Nm_3;   // <![CDATA[]]></Prsz_Inf_Nm_3>
	private String Prsz_Inf_Nm_4;   // <![CDATA[]]></Prsz_Inf_Nm_4>
	private String Prsz_Inf_Nm_5;   // <![CDATA[]]></Prsz_Inf_Nm_5>
	private String Prsz_Inf_Nm_6;   // <![CDATA[]]></Prsz_Inf_Nm_6>
	private String Prsz_Inf_Nm_7;   // <![CDATA[]]></Prsz_Inf_Nm_7>
	private String Prsz_Inf_Nm_8;   // <![CDATA[]]></Prsz_Inf_Nm_8>
	private String Prsz_Inf_Nm_9;   // <![CDATA[]]></Prsz_Inf_Nm_9>
	private String Prsz_Inf_Nm_10;   // <![CDATA[]]></Prsz_Inf_Nm_10>
	/**
	 * #跟单信息
	 */
	private String Doc_Inf;   // <![CDATA[]]></Doc_Inf>
	/**
	 * #现金管理交易结果代码
	 DF数据格式:1!n
1-交易成功2-交易失败3-处理中
4-部分成功a-银行已汇出b-落地
网点c-大额关账转预约d-定制交
易已预约e-跨行交易等待补录f-
结果不确定等待对账DF数据格
式：1!n
	 */
	private String CshMgt_Txn_Rslt_Cd;   // <![CDATA[1]]></CshMgt_Txn_Rslt_Cd>
	/**
	 * #CCBS流水号
	 */
	private String CCBS_Jrnl_No;   // <![CDATA[1306188010N0PG6KXHC]]></CCBS_Jrnl_No>
	/**
	 * #主机交易日期
	 */
	private String Hst_Txn_Dt;   // <![CDATA[20181009]]></Hst_Txn_Dt>
	/**
	 * #客户录入日期
	 */
	private String Cst_Inpt_Dt;   // <![CDATA[]]></Cst_Inpt_Dt>
	/**
	 * #客户录入时间
	 */
	private String Cst_Inpt_Tm;   // <![CDATA[]]></Cst_Inpt_Tm>
	/**
	 * #客户提交日期
	 */
	private String Cst_Dlv_Dt;   // <![CDATA[20181009]]></Cst_Dlv_Dt>
	/**
	 * #客户提交时间
	 */
	private String Cst_Dlv_Tm;   // <![CDATA[114504215]]></Cst_Dlv_Tm>
	/**
	 * 客户定制日期  DF数据格式:yyyymmdd
（收付款执行方式代码为1定时
或2定频时，此项必输）DF数据
格式：yyyy-mm-dd
	 */
	private String Cst_Csz_Dt;   // <![CDATA[]]></Cst_Csz_Dt>
	/**
	 * 客户定制时间
	 * DF数据格式:hhmmssnnn
（收付款执行方式代码为1定时
或2定频时，此项必输）DF数据
格式：hh:mm:ss:nnn
	 */
	private String Cst_Csz_Tm;   // <![CDATA[]]></Cst_Csz_Tm>
	/**
	 * 转账方式代码  DF数据格式:1!n
0-行内实时1-同城(人行交换系
统）2-大额支付系统3-小额系统
4-集中待补录5-落网点6-二代支
付DF数据格式：1!n
	 */
	private String Tfr_MtdCd;   // <![CDATA[5]]></Tfr_MtdCd>
	/**
	 * #保留字段一
	 */
	private String Rsrv_Fld_1;   // <![CDATA[]]></Rsrv_Fld_1>
	private String Rsrv_Fld_2;   // <![CDATA[]]></Rsrv_Fld_2>
	private String Rsrv_Fld_3;   // <![CDATA[]]></Rsrv_Fld_3>
	private String Rsrv_Fld_4;   // <![CDATA[]]></Rsrv_Fld_4>
	private String Rsrv_Fld_5;   // <![CDATA[]]></Rsrv_Fld_5>
	private String Rsrv_Fld_6;   // <![CDATA[]]></Rsrv_Fld_6>
	private String Rsrv_Fld_7;   // <![CDATA[]]></Rsrv_Fld_7>
	private String Rsrv_Fld_8;   // <![CDATA[]]></Rsrv_Fld_8>
	private String Rsrv_Fld_9;   // <![CDATA[]]></Rsrv_Fld_9>
	private String Rsrv_Fld_10;   // <![CDATA[]]></Rsrv_Fld_10>
	
	/**
	 * #错误信息
	 */
	private String Err_Inf;   // <![CDATA[]]></Err_Inf>
	/**
	 * #现金管理错误代码
	 */
	private String CshMgt_Err_Cd;   // <![CDATA[]]></CshMgt_Err_Cd>
	/**
	 * #客户方支付流水号
	 */
	private String CstPty_Py_Jrnl_No;   // <![CDATA[1484304938975]]></CstPty_Py_Jrnl_No>
	/**
	 * 资金冻结类型代码
	 * DF数据格式:1!n
1：按发生额冻结2：全部冻结
DF数据格式：1!n
	 */
	private String Cptl_Frz_TpCd;   // <![CDATA[]]></Cptl_Frz_TpCd>
	
	
	//-------------------------getter and setter---------------------------
	public String getTxn_Dt() {
		return Txn_Dt;
	}
	public void setTxn_Dt(String txn_Dt) {
		Txn_Dt = txn_Dt;
	}
	public String getTxn_Tm() {
		return Txn_Tm;
	}
	public void setTxn_Tm(String txn_Tm) {
		Txn_Tm = txn_Tm;
	}
	public String getCsz_Txn_ID() {
		return Csz_Txn_ID;
	}
	public void setCsz_Txn_ID(String csz_Txn_ID) {
		Csz_Txn_ID = csz_Txn_ID;
	}
	public String getCsz_Txn_Nm() {
		return Csz_Txn_Nm;
	}
	public void setCsz_Txn_Nm(String csz_Txn_Nm) {
		Csz_Txn_Nm = csz_Txn_Nm;
	}
	public String getCshMgt_TxnSrlNo() {
		return CshMgt_TxnSrlNo;
	}
	public void setCshMgt_TxnSrlNo(String cshMgt_TxnSrlNo) {
		CshMgt_TxnSrlNo = cshMgt_TxnSrlNo;
	}
	public String getWF_BIZ_ID() {
		return WF_BIZ_ID;
	}
	public void setWF_BIZ_ID(String wF_BIZ_ID) {
		WF_BIZ_ID = wF_BIZ_ID;
	}
	public String getCmpt_Rltv_Jrnl_No() {
		return Cmpt_Rltv_Jrnl_No;
	}
	public void setCmpt_Rltv_Jrnl_No(String cmpt_Rltv_Jrnl_No) {
		Cmpt_Rltv_Jrnl_No = cmpt_Rltv_Jrnl_No;
	}
	public String getCstPty_TxnSrlNo() {
		return CstPty_TxnSrlNo;
	}
	public void setCstPty_TxnSrlNo(String cstPty_TxnSrlNo) {
		CstPty_TxnSrlNo = cstPty_TxnSrlNo;
	}
	public String getPyr_CCstTr_ID() {
		return Pyr_CCstTr_ID;
	}
	public void setPyr_CCstTr_ID(String pyr_CCstTr_ID) {
		Pyr_CCstTr_ID = pyr_CCstTr_ID;
	}
	public String getPyr_CCstTrNdID() {
		return Pyr_CCstTrNdID;
	}
	public void setPyr_CCstTrNdID(String pyr_CCstTrNdID) {
		Pyr_CCstTrNdID = pyr_CCstTrNdID;
	}
	public String getPyr_DpBkNm() {
		return Pyr_DpBkNm;
	}
	public void setPyr_DpBkNm(String pyr_DpBkNm) {
		Pyr_DpBkNm = pyr_DpBkNm;
	}
	public String getPyr_DepBnk_No() {
		return Pyr_DepBnk_No;
	}
	public void setPyr_DepBnk_No(String pyr_DepBnk_No) {
		Pyr_DepBnk_No = pyr_DepBnk_No;
	}
	public String getPyr_BnkCD() {
		return Pyr_BnkCD;
	}
	public void setPyr_BnkCD(String pyr_BnkCD) {
		Pyr_BnkCD = pyr_BnkCD;
	}
	public String getPyr_BkCgyCd() {
		return Pyr_BkCgyCd;
	}
	public void setPyr_BkCgyCd(String pyr_BkCgyCd) {
		Pyr_BkCgyCd = pyr_BkCgyCd;
	}
	public String getPyr_Cst_AccNo() {
		return Pyr_Cst_AccNo;
	}
	public void setPyr_Cst_AccNo(String pyr_Cst_AccNo) {
		Pyr_Cst_AccNo = pyr_Cst_AccNo;
	}
	public String getPyr_AccNm() {
		return Pyr_AccNm;
	}
	public void setPyr_AccNm(String pyr_AccNm) {
		Pyr_AccNm = pyr_AccNm;
	}
	public String getPyr_Acc_CgyCd() {
		return Pyr_Acc_CgyCd;
	}
	public void setPyr_Acc_CgyCd(String pyr_Acc_CgyCd) {
		Pyr_Acc_CgyCd = pyr_Acc_CgyCd;
	}
	public String getPyr_Adr() {
		return Pyr_Adr;
	}
	public void setPyr_Adr(String pyr_Adr) {
		Pyr_Adr = pyr_Adr;
	}
	public String getRcvPrt_CCstTr_ID() {
		return RcvPrt_CCstTr_ID;
	}
	public void setRcvPrt_CCstTr_ID(String rcvPrt_CCstTr_ID) {
		RcvPrt_CCstTr_ID = rcvPrt_CCstTr_ID;
	}
	public String getRcvPrt_CCstTrNdID() {
		return RcvPrt_CCstTrNdID;
	}
	public void setRcvPrt_CCstTrNdID(String rcvPrt_CCstTrNdID) {
		RcvPrt_CCstTrNdID = rcvPrt_CCstTrNdID;
	}
	public String getRcvPrt_DpBkNm() {
		return RcvPrt_DpBkNm;
	}
	public void setRcvPrt_DpBkNm(String rcvPrt_DpBkNm) {
		RcvPrt_DpBkNm = rcvPrt_DpBkNm;
	}
	public String getRcvPrt_DepBnk_No() {
		return RcvPrt_DepBnk_No;
	}
	public void setRcvPrt_DepBnk_No(String rcvPrt_DepBnk_No) {
		RcvPrt_DepBnk_No = rcvPrt_DepBnk_No;
	}
	public String getRcvPrt_BnkCD() {
		return RcvPrt_BnkCD;
	}
	public void setRcvPrt_BnkCD(String rcvPrt_BnkCD) {
		RcvPrt_BnkCD = rcvPrt_BnkCD;
	}
	public String getRcvPrt_BkCgyCd() {
		return RcvPrt_BkCgyCd;
	}
	public void setRcvPrt_BkCgyCd(String rcvPrt_BkCgyCd) {
		RcvPrt_BkCgyCd = rcvPrt_BkCgyCd;
	}
	public String getRcvPrt_Cst_AccNo() {
		return RcvPrt_Cst_AccNo;
	}
	public void setRcvPrt_Cst_AccNo(String rcvPrt_Cst_AccNo) {
		RcvPrt_Cst_AccNo = rcvPrt_Cst_AccNo;
	}
	public String getRcvPtAc_Nm() {
		return RcvPtAc_Nm;
	}
	public void setRcvPtAc_Nm(String rcvPtAc_Nm) {
		RcvPtAc_Nm = rcvPtAc_Nm;
	}
	public String getRcvPtAc_CgyCd() {
		return RcvPtAc_CgyCd;
	}
	public void setRcvPtAc_CgyCd(String rcvPtAc_CgyCd) {
		RcvPtAc_CgyCd = rcvPtAc_CgyCd;
	}
	public String getRcvPrt_Adr() {
		return RcvPrt_Adr;
	}
	public void setRcvPrt_Adr(String rcvPrt_Adr) {
		RcvPrt_Adr = rcvPrt_Adr;
	}
	public String getRqs_Amt() {
		return Rqs_Amt;
	}
	public void setRqs_Amt(String rqs_Amt) {
		Rqs_Amt = rqs_Amt;
	}
	public String getHpnAm() {
		return HpnAm;
	}
	public void setHpnAm(String hpnAm) {
		HpnAm = hpnAm;
	}
	public String getUrgnt_TpCd() {
		return Urgnt_TpCd;
	}
	public void setUrgnt_TpCd(String urgnt_TpCd) {
		Urgnt_TpCd = urgnt_TpCd;
	}
	public String getUse_Nm() {
		return Use_Nm;
	}
	public void setUse_Nm(String use_Nm) {
		Use_Nm = use_Nm;
	}
	public String getCshMgt_Use_ID() {
		return CshMgt_Use_ID;
	}
	public void setCshMgt_Use_ID(String cshMgt_Use_ID) {
		CshMgt_Use_ID = cshMgt_Use_ID;
	}
	public String getRmrk() {
		return Rmrk;
	}
	public void setRmrk(String rmrk) {
		Rmrk = rmrk;
	}
	public String getRvPy_Bdgt_Use_ID() {
		return RvPy_Bdgt_Use_ID;
	}
	public void setRvPy_Bdgt_Use_ID(String rvPy_Bdgt_Use_ID) {
		RvPy_Bdgt_Use_ID = rvPy_Bdgt_Use_ID;
	}
	public String getRvPy_Bdgt_Use_Nm() {
		return RvPy_Bdgt_Use_Nm;
	}
	public void setRvPy_Bdgt_Use_Nm(String rvPy_Bdgt_Use_Nm) {
		RvPy_Bdgt_Use_Nm = rvPy_Bdgt_Use_Nm;
	}
	public String getTrgr_Amt() {
		return Trgr_Amt;
	}
	public void setTrgr_Amt(String trgr_Amt) {
		Trgr_Amt = trgr_Amt;
	}
	public String getPy_Cnd_TpCd() {
		return Py_Cnd_TpCd;
	}
	public void setPy_Cnd_TpCd(String py_Cnd_TpCd) {
		Py_Cnd_TpCd = py_Cnd_TpCd;
	}
	public String getSTBal_Val() {
		return STBal_Val;
	}
	public void setSTBal_Val(String sTBal_Val) {
		STBal_Val = sTBal_Val;
	}
	public String getScl_Fctr() {
		return Scl_Fctr;
	}
	public void setScl_Fctr(String scl_Fctr) {
		Scl_Fctr = scl_Fctr;
	}
	public String getRoud_NbrOfBit() {
		return Roud_NbrOfBit;
	}
	public void setRoud_NbrOfBit(String roud_NbrOfBit) {
		Roud_NbrOfBit = roud_NbrOfBit;
	}
	public String getRvPy_ExMd_Cd() {
		return RvPy_ExMd_Cd;
	}
	public void setRvPy_ExMd_Cd(String rvPy_ExMd_Cd) {
		RvPy_ExMd_Cd = rvPy_ExMd_Cd;
	}
	public String getRsrvtn_Exec_Dt() {
		return Rsrvtn_Exec_Dt;
	}
	public void setRsrvtn_Exec_Dt(String rsrvtn_Exec_Dt) {
		Rsrvtn_Exec_Dt = rsrvtn_Exec_Dt;
	}
	public String getRsrvtn_Exec_Tm() {
		return Rsrvtn_Exec_Tm;
	}
	public void setRsrvtn_Exec_Tm(String rsrvtn_Exec_Tm) {
		Rsrvtn_Exec_Tm = rsrvtn_Exec_Tm;
	}
	public String getFrq_MtdCd() {
		return Frq_MtdCd;
	}
	public void setFrq_MtdCd(String frq_MtdCd) {
		Frq_MtdCd = frq_MtdCd;
	}
	public String getFxFrq_Exec_Dt_Dsc() {
		return FxFrq_Exec_Dt_Dsc;
	}
	public void setFxFrq_Exec_Dt_Dsc(String fxFrq_Exec_Dt_Dsc) {
		FxFrq_Exec_Dt_Dsc = fxFrq_Exec_Dt_Dsc;
	}
	public String getPrsz_Inf_1() {
		return Prsz_Inf_1;
	}
	public void setPrsz_Inf_1(String prsz_Inf_1) {
		Prsz_Inf_1 = prsz_Inf_1;
	}
	public String getPrsz_Inf_2() {
		return Prsz_Inf_2;
	}
	public void setPrsz_Inf_2(String prsz_Inf_2) {
		Prsz_Inf_2 = prsz_Inf_2;
	}
	public String getPrsz_Inf_3() {
		return Prsz_Inf_3;
	}
	public void setPrsz_Inf_3(String prsz_Inf_3) {
		Prsz_Inf_3 = prsz_Inf_3;
	}
	public String getPrsz_Inf_4() {
		return Prsz_Inf_4;
	}
	public void setPrsz_Inf_4(String prsz_Inf_4) {
		Prsz_Inf_4 = prsz_Inf_4;
	}
	public String getPrsz_Inf_5() {
		return Prsz_Inf_5;
	}
	public void setPrsz_Inf_5(String prsz_Inf_5) {
		Prsz_Inf_5 = prsz_Inf_5;
	}
	public String getPrsz_Inf_6() {
		return Prsz_Inf_6;
	}
	public void setPrsz_Inf_6(String prsz_Inf_6) {
		Prsz_Inf_6 = prsz_Inf_6;
	}
	public String getPrsz_Inf_7() {
		return Prsz_Inf_7;
	}
	public void setPrsz_Inf_7(String prsz_Inf_7) {
		Prsz_Inf_7 = prsz_Inf_7;
	}
	public String getPrsz_Inf_8() {
		return Prsz_Inf_8;
	}
	public void setPrsz_Inf_8(String prsz_Inf_8) {
		Prsz_Inf_8 = prsz_Inf_8;
	}
	public String getPrsz_Inf_9() {
		return Prsz_Inf_9;
	}
	public void setPrsz_Inf_9(String prsz_Inf_9) {
		Prsz_Inf_9 = prsz_Inf_9;
	}
	public String getPrsz_Inf_10() {
		return Prsz_Inf_10;
	}
	public void setPrsz_Inf_10(String prsz_Inf_10) {
		Prsz_Inf_10 = prsz_Inf_10;
	}
	public String getPrsz_Inf_Nm_1() {
		return Prsz_Inf_Nm_1;
	}
	public void setPrsz_Inf_Nm_1(String prsz_Inf_Nm_1) {
		Prsz_Inf_Nm_1 = prsz_Inf_Nm_1;
	}
	public String getPrsz_Inf_Nm_2() {
		return Prsz_Inf_Nm_2;
	}
	public void setPrsz_Inf_Nm_2(String prsz_Inf_Nm_2) {
		Prsz_Inf_Nm_2 = prsz_Inf_Nm_2;
	}
	public String getPrsz_Inf_Nm_3() {
		return Prsz_Inf_Nm_3;
	}
	public void setPrsz_Inf_Nm_3(String prsz_Inf_Nm_3) {
		Prsz_Inf_Nm_3 = prsz_Inf_Nm_3;
	}
	public String getPrsz_Inf_Nm_4() {
		return Prsz_Inf_Nm_4;
	}
	public void setPrsz_Inf_Nm_4(String prsz_Inf_Nm_4) {
		Prsz_Inf_Nm_4 = prsz_Inf_Nm_4;
	}
	public String getPrsz_Inf_Nm_5() {
		return Prsz_Inf_Nm_5;
	}
	public void setPrsz_Inf_Nm_5(String prsz_Inf_Nm_5) {
		Prsz_Inf_Nm_5 = prsz_Inf_Nm_5;
	}
	public String getPrsz_Inf_Nm_6() {
		return Prsz_Inf_Nm_6;
	}
	public void setPrsz_Inf_Nm_6(String prsz_Inf_Nm_6) {
		Prsz_Inf_Nm_6 = prsz_Inf_Nm_6;
	}
	public String getPrsz_Inf_Nm_7() {
		return Prsz_Inf_Nm_7;
	}
	public void setPrsz_Inf_Nm_7(String prsz_Inf_Nm_7) {
		Prsz_Inf_Nm_7 = prsz_Inf_Nm_7;
	}
	public String getPrsz_Inf_Nm_8() {
		return Prsz_Inf_Nm_8;
	}
	public void setPrsz_Inf_Nm_8(String prsz_Inf_Nm_8) {
		Prsz_Inf_Nm_8 = prsz_Inf_Nm_8;
	}
	public String getPrsz_Inf_Nm_9() {
		return Prsz_Inf_Nm_9;
	}
	public void setPrsz_Inf_Nm_9(String prsz_Inf_Nm_9) {
		Prsz_Inf_Nm_9 = prsz_Inf_Nm_9;
	}
	public String getPrsz_Inf_Nm_10() {
		return Prsz_Inf_Nm_10;
	}
	public void setPrsz_Inf_Nm_10(String prsz_Inf_Nm_10) {
		Prsz_Inf_Nm_10 = prsz_Inf_Nm_10;
	}
	public String getDoc_Inf() {
		return Doc_Inf;
	}
	public void setDoc_Inf(String doc_Inf) {
		Doc_Inf = doc_Inf;
	}
	public String getCshMgt_Txn_Rslt_Cd() {
		return CshMgt_Txn_Rslt_Cd;
	}
	public void setCshMgt_Txn_Rslt_Cd(String cshMgt_Txn_Rslt_Cd) {
		CshMgt_Txn_Rslt_Cd = cshMgt_Txn_Rslt_Cd;
	}
	public String getCCBS_Jrnl_No() {
		return CCBS_Jrnl_No;
	}
	public void setCCBS_Jrnl_No(String cCBS_Jrnl_No) {
		CCBS_Jrnl_No = cCBS_Jrnl_No;
	}
	public String getHst_Txn_Dt() {
		return Hst_Txn_Dt;
	}
	public void setHst_Txn_Dt(String hst_Txn_Dt) {
		Hst_Txn_Dt = hst_Txn_Dt;
	}
	public String getCst_Inpt_Dt() {
		return Cst_Inpt_Dt;
	}
	public void setCst_Inpt_Dt(String cst_Inpt_Dt) {
		Cst_Inpt_Dt = cst_Inpt_Dt;
	}
	public String getCst_Inpt_Tm() {
		return Cst_Inpt_Tm;
	}
	public void setCst_Inpt_Tm(String cst_Inpt_Tm) {
		Cst_Inpt_Tm = cst_Inpt_Tm;
	}
	public String getCst_Dlv_Dt() {
		return Cst_Dlv_Dt;
	}
	public void setCst_Dlv_Dt(String cst_Dlv_Dt) {
		Cst_Dlv_Dt = cst_Dlv_Dt;
	}
	public String getCst_Dlv_Tm() {
		return Cst_Dlv_Tm;
	}
	public void setCst_Dlv_Tm(String cst_Dlv_Tm) {
		Cst_Dlv_Tm = cst_Dlv_Tm;
	}
	public String getCst_Csz_Dt() {
		return Cst_Csz_Dt;
	}
	public void setCst_Csz_Dt(String cst_Csz_Dt) {
		Cst_Csz_Dt = cst_Csz_Dt;
	}
	public String getCst_Csz_Tm() {
		return Cst_Csz_Tm;
	}
	public void setCst_Csz_Tm(String cst_Csz_Tm) {
		Cst_Csz_Tm = cst_Csz_Tm;
	}
	public String getTfr_MtdCd() {
		return Tfr_MtdCd;
	}
	public void setTfr_MtdCd(String tfr_MtdCd) {
		Tfr_MtdCd = tfr_MtdCd;
	}
	public String getRsrv_Fld_1() {
		return Rsrv_Fld_1;
	}
	public void setRsrv_Fld_1(String rsrv_Fld_1) {
		Rsrv_Fld_1 = rsrv_Fld_1;
	}
	public String getRsrv_Fld_2() {
		return Rsrv_Fld_2;
	}
	public void setRsrv_Fld_2(String rsrv_Fld_2) {
		Rsrv_Fld_2 = rsrv_Fld_2;
	}
	public String getRsrv_Fld_3() {
		return Rsrv_Fld_3;
	}
	public void setRsrv_Fld_3(String rsrv_Fld_3) {
		Rsrv_Fld_3 = rsrv_Fld_3;
	}
	public String getRsrv_Fld_4() {
		return Rsrv_Fld_4;
	}
	public void setRsrv_Fld_4(String rsrv_Fld_4) {
		Rsrv_Fld_4 = rsrv_Fld_4;
	}
	public String getRsrv_Fld_5() {
		return Rsrv_Fld_5;
	}
	public void setRsrv_Fld_5(String rsrv_Fld_5) {
		Rsrv_Fld_5 = rsrv_Fld_5;
	}
	public String getRsrv_Fld_6() {
		return Rsrv_Fld_6;
	}
	public void setRsrv_Fld_6(String rsrv_Fld_6) {
		Rsrv_Fld_6 = rsrv_Fld_6;
	}
	public String getRsrv_Fld_7() {
		return Rsrv_Fld_7;
	}
	public void setRsrv_Fld_7(String rsrv_Fld_7) {
		Rsrv_Fld_7 = rsrv_Fld_7;
	}
	public String getRsrv_Fld_8() {
		return Rsrv_Fld_8;
	}
	public void setRsrv_Fld_8(String rsrv_Fld_8) {
		Rsrv_Fld_8 = rsrv_Fld_8;
	}
	public String getRsrv_Fld_9() {
		return Rsrv_Fld_9;
	}
	public void setRsrv_Fld_9(String rsrv_Fld_9) {
		Rsrv_Fld_9 = rsrv_Fld_9;
	}
	public String getRsrv_Fld_10() {
		return Rsrv_Fld_10;
	}
	public void setRsrv_Fld_10(String rsrv_Fld_10) {
		Rsrv_Fld_10 = rsrv_Fld_10;
	}
	public String getErr_Inf() {
		return Err_Inf;
	}
	public void setErr_Inf(String err_Inf) {
		Err_Inf = err_Inf;
	}
	public String getCshMgt_Err_Cd() {
		return CshMgt_Err_Cd;
	}
	public void setCshMgt_Err_Cd(String cshMgt_Err_Cd) {
		CshMgt_Err_Cd = cshMgt_Err_Cd;
	}
	public String getCstPty_Py_Jrnl_No() {
		return CstPty_Py_Jrnl_No;
	}
	public void setCstPty_Py_Jrnl_No(String cstPty_Py_Jrnl_No) {
		CstPty_Py_Jrnl_No = cstPty_Py_Jrnl_No;
	}
	public String getCptl_Frz_TpCd() {
		return Cptl_Frz_TpCd;
	}
	public void setCptl_Frz_TpCd(String cptl_Frz_TpCd) {
		Cptl_Frz_TpCd = cptl_Frz_TpCd;
	}
	
	
	
	
}
