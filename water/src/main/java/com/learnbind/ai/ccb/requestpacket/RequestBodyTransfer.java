package com.learnbind.ai.ccb.requestpacket;

import java.util.Date;

import com.learnbind.ai.base.common.DateUtil;

/**
 * P1CMSET35  人民币单笔付款
 * 转账 请求报文  BODY
 * @author lenovo
 * 有13个参数需要进行设置.有一些参数是需要在接口中进行配置的,有一些是需要动态加入的.稍后进行分类处理.
 */
public class RequestBodyTransfer {
	
	/**
	  可售产品编码
		常用值如下：
			客户内部账户管理
			业务 00000879
			票据池 00000880
			现金管理收款	00000881
			现金管理付款	00000882   转帐时使用此代码
			定时资金池	00000883
			资金预算管理	00000887
			现金管理信息服务	00000886
			智能理财现金池	00104080
			价格 99999999
			第三方支付	00000915
			账簿现金池	00107826
	 */
	private String   ASPD_ECD="00000882";		//<![CDATA[00000882]]></ASPD_ECD>
    /**
     * #子渠道号 15!n，如果暂无，请填写全0
     */
    private String   SChl_No="000000000000000";		//<![CDATA[000000000000000]]></SChl_No>
    /**
     * #流控节点编号  an..15，如果暂无，请填写全0
     */
    private String   FwCtl_Node_ID="000000000000000";	//<![CDATA[000000000000000]]></FwCtl_Node_ID>
    /**
     * #发起方业务日期 
     */
    private String   IttParty_Bsn_Dt="";	//<![CDATA[20181009]]></IttParty_Bsn_Dt>
    /**
     *  集团编号  n..19,客户通过签约唯一确认，若无则空
     */
    private String   Grp_ID="";			//<![CDATA[]]></Grp_ID>
    /**
     * 客户编号  8!n,客户通过签约 唯一确认，固定
     * 需要设置
     */
    private String   Cst_ID;			//<![CDATA[973850458304234973]]></Cst_ID>
    private String   Prim_AR_No;			//<![CDATA[]]></Prim_AR_No>
    private String   Root_Node_PdAr_ID;	//<![CDATA[]]></Root_Node_PdAr_ID>
    private String   PdAr_ID;				//<![CDATA[]]></PdAr_ID>
    /**
     * #7X24小时标志 固定0
     */
    private String   SvM24Hr_Ind="0";			//<![CDATA[0]]></SvM24Hr_Ind>
    /**
     * 时区编码  #时区编码  固定
     */
    private String   Tmzon_ECD="08";			//<![CDATA[08]]></Tmzon_ECD>
    private String   Rqs_Upload_File_Nm;	//<![CDATA[]]></Rqs_Upload_File_Nm>
    /**
     * #组件实体编号  9!n,固定0000CN000
     */
    private String   Cmpt_Ent_ID="0000CN000";			//<![CDATA[0000CN000]]></Cmpt_Ent_ID>
    private String   WF_BIZ_ID;			//<![CDATA[]]></WF_BIZ_ID>
    private String   WF_MNPLT_TP;			//<![CDATA[]]></WF_MNPLT_TP>
    private String   WF_FCN_ID;			//<![CDATA[]]></WF_FCN_ID>
    /**
     * 现金客户树编号 13!an CMN+10位顺序号，客户通过签约唯一确认，固定
     * 需要设置
     */
    private String   CCstTr_ID;			//<![CDATA[CMN0000093068]]></CCstTr_ID>
    /**
     * 现金客户树节点编号   25!an ND+18位客户编号+5位顺序号，客户通过签约唯一确认，固定
     * 需要设置
     */
    private String   CCstTrNdID;			//<![CDATA[ND97385045830423497300001]]></CCstTrNdID>
    private String   Rule_Chk_Ind_1;//<![CDATA[]]></Rule_Chk_Ind_1>
    private String   Cmpt_Rltv_Jrnl_No;//<![CDATA[]]></Cmpt_Rltv_Jrnl_No>
    /**
     * #客户方交易流水号  DF数据格式：an..60
     * 需要设置
     */
    private String   CstPty_TxnSrlNo;//<![CDATA[1484304938977]]></CstPty_TxnSrlNo>
    private String   Pyr_DpBkNm;//<![CDATA[]]></Pyr_DpBkNm>
    private String   Pyr_DepBnk_No;//<![CDATA[]]></Pyr_DepBnk_No>
    private String   Pyr_BnkCD;//<![CDATA[]]></Pyr_BnkCD>
    /**
     * 付款方行别代码 （01本行02国内他行03国外他行）DF数据格式：2!n
     * 需要设置
     */
    private String   Pyr_BkCgyCd;//<![CDATA[01]]></Pyr_BkCgyCd>
    /**
     * 付款方客户账号
     * 需要设置
     */
    private String   Pyr_Cst_AccNo;//<![CDATA[13001635808050514014]]></Pyr_Cst_AccNo>
    /**
     * 付款方账户名称
     * 需要设置
     */
    private String   Pyr_AccNm;//<![CDATA[公司九七]]></Pyr_AccNm>
    private String   Pyr_Acc_CgyCd;//<![CDATA[]]></Pyr_Acc_CgyCd>
    private String   Pyr_Adr;//<![CDATA[]]></Pyr_Adr>
    private String   RcvPrt_CCstTr_ID;//<![CDATA[]]></RcvPrt_CCstTr_ID>
    private String   RcvPrt_CCstTrNdID;//<![CDATA[]]></RcvPrt_CCstTrNdID>
    private String   RcvPrt_DpBkNm;//<![CDATA[]]></RcvPrt_DpBkNm>
    private String   RcvPrt_DepBnk_No;//<![CDATA[]]></RcvPrt_DepBnk_No>
    private String   RcvPrt_BnkCD;//<![CDATA[]]></RcvPrt_BnkCD>
    /**
     * 收款方行别代码 （01本行 02国内他行 03国外他行）DF数据格式：2!n
     * 需要设置
     */
    private String   RcvPrt_BkCgyCd;//<![CDATA[01]]></RcvPrt_BkCgyCd>
    /**
     * 收款方客户账号
     * 需要设置
     */
    private String   RcvPrt_Cst_AccNo;//<![CDATA[130016101050519604]]></RcvPrt_Cst_AccNo>
    /**
     * 收款方账户名称
     * 需要设置
     */
    private String   RcvPtAc_Nm;//<![CDATA[公司某某]]></RcvPtAc_Nm>
    private String   RcvPtAc_CgyCd;//<![CDATA[]]></RcvPtAc_CgyCd>
    private String   RcvPrt_Adr;//<![CDATA[]]></RcvPrt_Adr>
    /**
     * 请求金额
     * 需要设置
     */
    private String   Rqs_Amt;//<![CDATA[190.00]]></Rqs_Amt>
    private String   Urgnt_TpCd;//<![CDATA[]]></Urgnt_TpCd>
    private String   Use_Nm;//<![CDATA[]]></Use_Nm>
    private String   CshMgt_Use_ID;//<![CDATA[]]></CshMgt_Use_ID>
    /**
     * 备注
     * 需要设置
     */
    private String   Rmrk;//<![CDATA[11]]></Rmrk>
    private String   RvPy_Bdgt_Use_ID;//<![CDATA[]]></RvPy_Bdgt_Use_ID>
    private String   RvPy_Bdgt_Use_Nm;//<![CDATA[]]></RvPy_Bdgt_Use_Nm>
    private String   Trgr_Amt;//<![CDATA[]]></Trgr_Amt>
    private String   Csz_Txn_Nm;//<![CDATA[]]></Csz_Txn_Nm>
    /**
     * 付款条件类型代码
     * 00：定金额；
     * 01：定余额；
     * 02：零余额；
     * 03：定比例；
     * 04：取整；
     * 05：定余额取整；
     ，此项值仅允许为00定金额；DF数据格式：2!n
     */
    private String   Py_Cnd_TpCd="00";//<![CDATA[00]]></Py_Cnd_TpCd>
    private String   STBal_Val;//<![CDATA[]]></STBal_Val>
    private String   Scl_Fctr;//<![CDATA[]]></Scl_Fctr>
    private String   Roud_NbrOfBit;//<![CDATA[]]></Roud_NbrOfBit>
    /**
     * 收付款执行方式代码  0：实时1：定时2：定频DF数据格式：1!n
     */
    private String   RvPy_ExMd_Cd="0";//<![CDATA[0]]></RvPy_ExMd_Cd>
    private String   Rsrvtn_Exec_Dt;//<![CDATA[]]></Rsrvtn_Exec_Dt>
    private String   Rsrvtn_Exec_Tm;//<![CDATA[]]></Rsrvtn_Exec_Tm>
    private String   Frq_MtdCd;//<![CDATA[]]></Frq_MtdCd>
    private String   FxFrq_Exec_Dt_Dsc;//<![CDATA[]]></FxFrq_Exec_Dt_Dsc>
    private String   Prsz_Inf_1;//<![CDATA[]]></Prsz_Inf_1>
    private String   Prsz_Inf_2;//<![CDATA[]]></Prsz_Inf_2>
    private String   Prsz_Inf_3;//<![CDATA[]]></Prsz_Inf_3>
    private String   Prsz_Inf_4;//<![CDATA[]]></Prsz_Inf_4>
    private String   Prsz_Inf_5;//<![CDATA[]]></Prsz_Inf_5>
    private String   Prsz_Inf_6;//<![CDATA[]]></Prsz_Inf_6>
    private String   Prsz_Inf_7;//<![CDATA[]]></Prsz_Inf_7>
    private String   Prsz_Inf_8;//<![CDATA[]]></Prsz_Inf_8>
    private String   Prsz_Inf_9;//<![CDATA[]]></Prsz_Inf_9>
    private String   Prsz_Inf_10;//<![CDATA[]]></Prsz_Inf_10>
    private String   Prsz_Inf_Nm_1;//<![CDATA[]]></Prsz_Inf_Nm_1>
    private String   Prsz_Inf_Nm_2;//<![CDATA[]]></Prsz_Inf_Nm_2>
    private String   Prsz_Inf_Nm_3;//<![CDATA[]]></Prsz_Inf_Nm_3>
    private String   Prsz_Inf_Nm_4;//<![CDATA[]]></Prsz_Inf_Nm_4>
    private String   Prsz_Inf_Nm_5;//<![CDATA[]]></Prsz_Inf_Nm_5>
    private String   Prsz_Inf_Nm_6;//<![CDATA[]]></Prsz_Inf_Nm_6>
    private String   Prsz_Inf_Nm_7;//<![CDATA[]]></Prsz_Inf_Nm_7>
    private String   Prsz_Inf_Nm_8;//<![CDATA[]]></Prsz_Inf_Nm_8>
    private String   Prsz_Inf_Nm_9;//<![CDATA[]]></Prsz_Inf_Nm_9>
    private String   Prsz_Inf_Nm_10;//<![CDATA[]]></Prsz_Inf_Nm_10>
    private String   Doc_Inf;//<![CDATA[]]></Doc_Inf>
    /**
     * #客户提交日期 DF数据格式：yyyymmdd
     */
    private String   Cst_Dlv_Dt;//<![CDATA[20181009]]></Cst_Dlv_Dt>
    /**
     * #客户提交时间  DF数据格式：hhmmssnnn
     */
    private String   Cst_Dlv_Tm;//<![CDATA[114504215]]> </Cst_Dlv_Tm>
    private String   Cst_Csz_Dt;//<![CDATA[]]></Cst_Csz_Dt>
    private String   Cst_Csz_Tm;//<![CDATA[]]></Cst_Csz_Tm>
    private String   Rsrv_Fld_1;//<![CDATA[]]></Rsrv_Fld_1>
    private String   Rsrv_Fld_2;//<![CDATA[]]></Rsrv_Fld_2>
    private String   Rsrv_Fld_3;//<![CDATA[]]></Rsrv_Fld_3>
    private String   Rsrv_Fld_4;//<![CDATA[]]></Rsrv_Fld_4>
    private String   Rsrv_Fld_5;//<![CDATA[]]></Rsrv_Fld_5>
    private String   Rsrv_Fld_6;//<![CDATA[]]></Rsrv_Fld_6>
    private String   Rsrv_Fld_7;//<![CDATA[]]></Rsrv_Fld_7>
    private String   Rsrv_Fld_8;//<![CDATA[]]></Rsrv_Fld_8>
    private String   Rsrv_Fld_9;//<![CDATA[]]></Rsrv_Fld_9>
    private String   Rsrv_Fld_10;//<![CDATA[]]></Rsrv_Fld_10>
    private String   CCBS_Fee_Sbj_Cd;//<![CDATA[]]></CCBS_Fee_Sbj_Cd>
    private String   Cst_RecAmt;//<![CDATA[]]></Cst_RecAmt>
    private String   Ahn_Py_Agrm_No;//<![CDATA[]]></Ahn_Py_Agrm_No>
    private String   Pyr_AccNm_Verf_Cd;//<![CDATA[]]></Pyr_AccNm_Verf_Cd>
    private String   RcvPrt_AccNm_Verf_Cd;//<![CDATA[]]></RcvPrt_AccNm_Verf_Cd>
    private String   Cst_MblPh_No;//<![CDATA[]]></Cst_MblPh_No>
    private String   SMS_Cntnt;//<![CDATA[]]></SMS_Cntnt>
    private String   Email_Adr;//<![CDATA[]]></Email_Adr>
    private String   Mail_CntDsc;//<![CDATA[]]></Mail_CntDsc>
    /**
     * #客户方支付流水号(暂定:与客户方交易流水号相同) 
     * 需要设置 
     */
    private String   CstPty_Py_Jrnl_No;  //>1484304938977</CstPty_Py_Jrnl_No>
    
    public RequestBodyTransfer() {
    	Date nowTime = new Date(); // 获取当前日期

		// 客户提交日期
		String txnDt = DateUtil.getYYYYMMDDDate(nowTime);
		Cst_Dlv_Dt=txnDt;
		
		IttParty_Bsn_Dt=txnDt;  //#发起方业务日期
		
		// 客户提交时间
		String reqTime = DateUtil.getHHmmssSSS(nowTime);
		Cst_Dlv_Tm=reqTime;

    }
    
    
	public String getASPD_ECD() {
		return ASPD_ECD;
	}
	public void setASPD_ECD(String aSPD_ECD) {
		ASPD_ECD = aSPD_ECD;
	}
	public String getSChl_No() {
		return SChl_No;
	}
	public void setSChl_No(String sChl_No) {
		SChl_No = sChl_No;
	}
	public String getFwCtl_Node_ID() {
		return FwCtl_Node_ID;
	}
	public void setFwCtl_Node_ID(String fwCtl_Node_ID) {
		FwCtl_Node_ID = fwCtl_Node_ID;
	}
	public String getIttParty_Bsn_Dt() {
		return IttParty_Bsn_Dt;
	}
	public void setIttParty_Bsn_Dt(String ittParty_Bsn_Dt) {
		IttParty_Bsn_Dt = ittParty_Bsn_Dt;
	}
	public String getGrp_ID() {
		return Grp_ID;
	}
	public void setGrp_ID(String grp_ID) {
		Grp_ID = grp_ID;
	}
	public String getCst_ID() {
		return Cst_ID;
	}
	public void setCst_ID(String cst_ID) {
		Cst_ID = cst_ID;
	}
	public String getPrim_AR_No() {
		return Prim_AR_No;
	}
	public void setPrim_AR_No(String prim_AR_No) {
		Prim_AR_No = prim_AR_No;
	}
	public String getRoot_Node_PdAr_ID() {
		return Root_Node_PdAr_ID;
	}
	public void setRoot_Node_PdAr_ID(String root_Node_PdAr_ID) {
		Root_Node_PdAr_ID = root_Node_PdAr_ID;
	}
	public String getPdAr_ID() {
		return PdAr_ID;
	}
	public void setPdAr_ID(String pdAr_ID) {
		PdAr_ID = pdAr_ID;
	}
	public String getSvM24Hr_Ind() {
		return SvM24Hr_Ind;
	}
	public void setSvM24Hr_Ind(String svM24Hr_Ind) {
		SvM24Hr_Ind = svM24Hr_Ind;
	}
	public String getTmzon_ECD() {
		return Tmzon_ECD;
	}
	public void setTmzon_ECD(String tmzon_ECD) {
		Tmzon_ECD = tmzon_ECD;
	}
	public String getRqs_Upload_File_Nm() {
		return Rqs_Upload_File_Nm;
	}
	public void setRqs_Upload_File_Nm(String rqs_Upload_File_Nm) {
		Rqs_Upload_File_Nm = rqs_Upload_File_Nm;
	}
	public String getCmpt_Ent_ID() {
		return Cmpt_Ent_ID;
	}
	public void setCmpt_Ent_ID(String cmpt_Ent_ID) {
		Cmpt_Ent_ID = cmpt_Ent_ID;
	}
	public String getWF_BIZ_ID() {
		return WF_BIZ_ID;
	}
	public void setWF_BIZ_ID(String wF_BIZ_ID) {
		WF_BIZ_ID = wF_BIZ_ID;
	}
	public String getWF_MNPLT_TP() {
		return WF_MNPLT_TP;
	}
	public void setWF_MNPLT_TP(String wF_MNPLT_TP) {
		WF_MNPLT_TP = wF_MNPLT_TP;
	}
	public String getWF_FCN_ID() {
		return WF_FCN_ID;
	}
	public void setWF_FCN_ID(String wF_FCN_ID) {
		WF_FCN_ID = wF_FCN_ID;
	}
	public String getCCstTr_ID() {
		return CCstTr_ID;
	}
	public void setCCstTr_ID(String cCstTr_ID) {
		CCstTr_ID = cCstTr_ID;
	}
	public String getCCstTrNdID() {
		return CCstTrNdID;
	}
	public void setCCstTrNdID(String cCstTrNdID) {
		CCstTrNdID = cCstTrNdID;
	}
	public String getRule_Chk_Ind_1() {
		return Rule_Chk_Ind_1;
	}
	public void setRule_Chk_Ind_1(String rule_Chk_Ind_1) {
		Rule_Chk_Ind_1 = rule_Chk_Ind_1;
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
	public String getCsz_Txn_Nm() {
		return Csz_Txn_Nm;
	}
	public void setCsz_Txn_Nm(String csz_Txn_Nm) {
		Csz_Txn_Nm = csz_Txn_Nm;
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
	public String getCCBS_Fee_Sbj_Cd() {
		return CCBS_Fee_Sbj_Cd;
	}
	public void setCCBS_Fee_Sbj_Cd(String cCBS_Fee_Sbj_Cd) {
		CCBS_Fee_Sbj_Cd = cCBS_Fee_Sbj_Cd;
	}
	public String getCst_RecAmt() {
		return Cst_RecAmt;
	}
	public void setCst_RecAmt(String cst_RecAmt) {
		Cst_RecAmt = cst_RecAmt;
	}
	public String getAhn_Py_Agrm_No() {
		return Ahn_Py_Agrm_No;
	}
	public void setAhn_Py_Agrm_No(String ahn_Py_Agrm_No) {
		Ahn_Py_Agrm_No = ahn_Py_Agrm_No;
	}
	public String getPyr_AccNm_Verf_Cd() {
		return Pyr_AccNm_Verf_Cd;
	}
	public void setPyr_AccNm_Verf_Cd(String pyr_AccNm_Verf_Cd) {
		Pyr_AccNm_Verf_Cd = pyr_AccNm_Verf_Cd;
	}
	public String getRcvPrt_AccNm_Verf_Cd() {
		return RcvPrt_AccNm_Verf_Cd;
	}
	public void setRcvPrt_AccNm_Verf_Cd(String rcvPrt_AccNm_Verf_Cd) {
		RcvPrt_AccNm_Verf_Cd = rcvPrt_AccNm_Verf_Cd;
	}
	public String getCst_MblPh_No() {
		return Cst_MblPh_No;
	}
	public void setCst_MblPh_No(String cst_MblPh_No) {
		Cst_MblPh_No = cst_MblPh_No;
	}
	public String getSMS_Cntnt() {
		return SMS_Cntnt;
	}
	public void setSMS_Cntnt(String sMS_Cntnt) {
		SMS_Cntnt = sMS_Cntnt;
	}
	public String getEmail_Adr() {
		return Email_Adr;
	}
	public void setEmail_Adr(String email_Adr) {
		Email_Adr = email_Adr;
	}
	public String getMail_CntDsc() {
		return Mail_CntDsc;
	}
	public void setMail_CntDsc(String mail_CntDsc) {
		Mail_CntDsc = mail_CntDsc;
	}
	public String getCstPty_Py_Jrnl_No() {
		return CstPty_Py_Jrnl_No;
	}
	public void setCstPty_Py_Jrnl_No(String cstPty_Py_Jrnl_No) {
		CstPty_Py_Jrnl_No = cstPty_Py_Jrnl_No;
	}
    
    

}
