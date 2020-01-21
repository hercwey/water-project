package com.learnbind.ai.ccb.responsepacket;

/**
 *
 * TODO 稍后明确各字段的含义
 * RESPONSE报文
 * 帐户余额对象
 * @author lenovo
 *
 */
public class ResponseAccountBalance {
	
	/**
	 * 账户开户行机构号
	 */
	private String Acc_DpBkInNo		; //<![CDATA[130618801]]></Acc_DpBkInNo>
	/**
	 * 账户状态代码
	 * 
	 */
	private String Acc_StCd			; //<![CDATA[0]]></Acc_StCd>
	/**
	 * #账户控制状态代码
	 */
	private String Acc_Ctrl_StCd		; //<![CDATA[0]]></Acc_Ctrl_StCd>
	/**
	 * CCBS客户编号
	 */
	private String CCBS_Cst_ID		; //<![CDATA[973850458304234973]]></CCBS_Cst_ID>
	/**
	 * 客户名称
	 */
	private String Cst_Nm			; //<![CDATA[公司七八]]></Cst_Nm>
	/**
	 * 开户日期
	 */
	private String OpnAcc_Dt			; //<![CDATA[20140710]]></OpnAcc_Dt>
	/**
	 * 销户日期
	 */
	private String CnclAcct_Dt		; //<![CDATA[]]></CnclAcct_Dt>
	/**
	 * #指令序列号
	 */
	private String Insn_Seq_No		; //<![CDATA[]]></Insn_Seq_No>
	/**
	 * ------------------
	 * 现金客户账号
	 * ------------------
	 * 注:在项目中可以使用
	 */
	private String Cash_Cst_AccNo	; //<![CDATA[13001618801050519642]]></Cash_Cst_AccNo>
	/**
	 * 现金管理账户类型代码
	 */
	private String CshMgt_Acc_TpCd	; //<![CDATA[1]]></CshMgt_Acc_TpCd>
	/**
	 * 币种代码
	 */
	private String CcyCd				; //<![CDATA[156]]></CcyCd>
	/**
	 * 存折册号
	 */
	private String BkltNo			; //<![CDATA[]]></BkltNo>
	/**
	 * 存款笔号
	 */
	private String Dep_DepSeqNo		; //<![CDATA[]]></Dep_DepSeqNo>
	/**
	 * 钞汇代码
	 */
	private String CshEx_Cd			; //<![CDATA[1]]></CshEx_Cd>
	/**
	 * 账户性质代码
	 */
	private String AcChar_Cd			; //<![CDATA[1300]]></AcChar_Cd>
	/**
	 * 昨日余额
	 */
	private String YstdBl			; //<![CDATA[]]></YstdBl>
	/**
	 * 信息报告账户余额
	 */
	private String InfRpt_AcBa		; //<![CDATA[0]]></InfRpt_AcBa>
	/**
	 * ---------------------
	 * 账户可用余额
	 * ---------------------
	 * 注:可用于项目中.
	 */
	private String Acc_Avl_Bal		; //<![CDATA[665.07]]></Acc_Avl_Bal>
	/**
	 * 冻结金额
	 */
	private String Frz_Amt			; //<![CDATA[0]]></Frz_Amt>
	/**
	 * 计息标志
	 */
	private String IntAr_Ind			; //<![CDATA[Y]]></IntAr_Ind>
	/**
	 * 活期存款积数
	 */
	private String DmdDep_Acm		; //<![CDATA[0]]></DmdDep_Acm>
	/**
	 * 透支金额
	 */
	private String Od_Amt			; //<![CDATA[0]]></Od_Amt>
	/**
	 * 流动性账户性质标志
	 */
	private String Lqd_AcChar_Ind	; //<![CDATA[0]]></Lqd_AcChar_Ind>
	/**
	 * 透支天数
	 */
	private String Od_Dys			; //<![CDATA[0]]></Od_Dys>
	/**
	 * 开户行机构名称
	 */
	private String DepBnk_Inst_Nm	; //<![CDATA[中国建设银行股份有限公司河北省分行直属支行]]></DepBnk_Inst_Nm>
	/**
	 * #时间戳
	 */
	private String Tms				; //<![CDATA[20181009093201875]]></Tms>
	/**
	 * #现金管理响应代码
	 */
	private String CshMgt_Rsp_Cd		; //<![CDATA[000000000000]]></CshMgt_Rsp_Cd>
	/**
	 * #主机响应信息
	 */
	private String Hst_Rsp_Inf		; //<![CDATA[成功]]></Hst_Rsp_Inf>
	
	
	public String getAcc_DpBkInNo() {
		return Acc_DpBkInNo;
	}
	public void setAcc_DpBkInNo(String acc_DpBkInNo) {
		Acc_DpBkInNo = acc_DpBkInNo;
	}
	public String getAcc_StCd() {
		return Acc_StCd;
	}
	public void setAcc_StCd(String acc_StCd) {
		Acc_StCd = acc_StCd;
	}
	public String getAcc_Ctrl_StCd() {
		return Acc_Ctrl_StCd;
	}
	public void setAcc_Ctrl_StCd(String acc_Ctrl_StCd) {
		Acc_Ctrl_StCd = acc_Ctrl_StCd;
	}
	public String getCCBS_Cst_ID() {
		return CCBS_Cst_ID;
	}
	public void setCCBS_Cst_ID(String cCBS_Cst_ID) {
		CCBS_Cst_ID = cCBS_Cst_ID;
	}
	public String getCst_Nm() {
		return Cst_Nm;
	}
	public void setCst_Nm(String cst_Nm) {
		Cst_Nm = cst_Nm;
	}
	public String getOpnAcc_Dt() {
		return OpnAcc_Dt;
	}
	public void setOpnAcc_Dt(String opnAcc_Dt) {
		OpnAcc_Dt = opnAcc_Dt;
	}
	public String getCnclAcct_Dt() {
		return CnclAcct_Dt;
	}
	public void setCnclAcct_Dt(String cnclAcct_Dt) {
		CnclAcct_Dt = cnclAcct_Dt;
	}
	public String getInsn_Seq_No() {
		return Insn_Seq_No;
	}
	public void setInsn_Seq_No(String insn_Seq_No) {
		Insn_Seq_No = insn_Seq_No;
	}
	public String getCash_Cst_AccNo() {
		return Cash_Cst_AccNo;
	}
	public void setCash_Cst_AccNo(String cash_Cst_AccNo) {
		Cash_Cst_AccNo = cash_Cst_AccNo;
	}
	public String getCshMgt_Acc_TpCd() {
		return CshMgt_Acc_TpCd;
	}
	public void setCshMgt_Acc_TpCd(String cshMgt_Acc_TpCd) {
		CshMgt_Acc_TpCd = cshMgt_Acc_TpCd;
	}
	public String getCcyCd() {
		return CcyCd;
	}
	public void setCcyCd(String ccyCd) {
		CcyCd = ccyCd;
	}
	public String getBkltNo() {
		return BkltNo;
	}
	public void setBkltNo(String bkltNo) {
		BkltNo = bkltNo;
	}
	public String getDep_DepSeqNo() {
		return Dep_DepSeqNo;
	}
	public void setDep_DepSeqNo(String dep_DepSeqNo) {
		Dep_DepSeqNo = dep_DepSeqNo;
	}
	public String getCshEx_Cd() {
		return CshEx_Cd;
	}
	public void setCshEx_Cd(String cshEx_Cd) {
		CshEx_Cd = cshEx_Cd;
	}
	public String getAcChar_Cd() {
		return AcChar_Cd;
	}
	public void setAcChar_Cd(String acChar_Cd) {
		AcChar_Cd = acChar_Cd;
	}
	public String getYstdBl() {
		return YstdBl;
	}
	public void setYstdBl(String ystdBl) {
		YstdBl = ystdBl;
	}
	public String getInfRpt_AcBa() {
		return InfRpt_AcBa;
	}
	public void setInfRpt_AcBa(String infRpt_AcBa) {
		InfRpt_AcBa = infRpt_AcBa;
	}
	public String getAcc_Avl_Bal() {
		return Acc_Avl_Bal;
	}
	public void setAcc_Avl_Bal(String acc_Avl_Bal) {
		Acc_Avl_Bal = acc_Avl_Bal;
	}
	public String getFrz_Amt() {
		return Frz_Amt;
	}
	public void setFrz_Amt(String frz_Amt) {
		Frz_Amt = frz_Amt;
	}
	public String getIntAr_Ind() {
		return IntAr_Ind;
	}
	public void setIntAr_Ind(String intAr_Ind) {
		IntAr_Ind = intAr_Ind;
	}
	public String getDmdDep_Acm() {
		return DmdDep_Acm;
	}
	public void setDmdDep_Acm(String dmdDep_Acm) {
		DmdDep_Acm = dmdDep_Acm;
	}
	public String getOd_Amt() {
		return Od_Amt;
	}
	public void setOd_Amt(String od_Amt) {
		Od_Amt = od_Amt;
	}
	public String getLqd_AcChar_Ind() {
		return Lqd_AcChar_Ind;
	}
	public void setLqd_AcChar_Ind(String lqd_AcChar_Ind) {
		Lqd_AcChar_Ind = lqd_AcChar_Ind;
	}
	public String getOd_Dys() {
		return Od_Dys;
	}
	public void setOd_Dys(String od_Dys) {
		Od_Dys = od_Dys;
	}
	public String getDepBnk_Inst_Nm() {
		return DepBnk_Inst_Nm;
	}
	public void setDepBnk_Inst_Nm(String depBnk_Inst_Nm) {
		DepBnk_Inst_Nm = depBnk_Inst_Nm;
	}
	public String getTms() {
		return Tms;
	}
	public void setTms(String tms) {
		Tms = tms;
	}
	public String getCshMgt_Rsp_Cd() {
		return CshMgt_Rsp_Cd;
	}
	public void setCshMgt_Rsp_Cd(String cshMgt_Rsp_Cd) {
		CshMgt_Rsp_Cd = cshMgt_Rsp_Cd;
	}
	public String getHst_Rsp_Inf() {
		return Hst_Rsp_Inf;
	}
	public void setHst_Rsp_Inf(String hst_Rsp_Inf) {
		Hst_Rsp_Inf = hst_Rsp_Inf;
	}
	
	@Override
	public String toString() {
		return "ResponseAccountBalance [Acc_DpBkInNo=" + Acc_DpBkInNo + ", Acc_StCd=" + Acc_StCd + ", Acc_Ctrl_StCd="
				+ Acc_Ctrl_StCd + ", CCBS_Cst_ID=" + CCBS_Cst_ID + ", Cst_Nm=" + Cst_Nm + ", OpnAcc_Dt=" + OpnAcc_Dt
				+ ", CnclAcct_Dt=" + CnclAcct_Dt + ", Insn_Seq_No=" + Insn_Seq_No + ", Cash_Cst_AccNo=" + Cash_Cst_AccNo
				+ ", CshMgt_Acc_TpCd=" + CshMgt_Acc_TpCd + ", CcyCd=" + CcyCd + ", BkltNo=" + BkltNo + ", Dep_DepSeqNo="
				+ Dep_DepSeqNo + ", CshEx_Cd=" + CshEx_Cd + ", AcChar_Cd=" + AcChar_Cd + ", YstdBl=" + YstdBl
				+ ", InfRpt_AcBa=" + InfRpt_AcBa + ", Acc_Avl_Bal=" + Acc_Avl_Bal + ", Frz_Amt=" + Frz_Amt
				+ ", IntAr_Ind=" + IntAr_Ind + ", DmdDep_Acm=" + DmdDep_Acm + ", Od_Amt=" + Od_Amt + ", Lqd_AcChar_Ind="
				+ Lqd_AcChar_Ind + ", Od_Dys=" + Od_Dys + ", DepBnk_Inst_Nm=" + DepBnk_Inst_Nm + ", Tms=" + Tms
				+ ", CshMgt_Rsp_Cd=" + CshMgt_Rsp_Cd + ", Hst_Rsp_Inf=" + Hst_Rsp_Inf + "]";
	}
	
}
