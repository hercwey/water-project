package com.learnbind.ai.service.ccb;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.learnbind.ai.ccb.CcbBusinessUtils;
import com.learnbind.ai.ccb.business.BodyParams;
import com.learnbind.ai.ccb.business.CCBBusiness;
import com.learnbind.ai.ccb.business.HeaderParams;
import com.learnbind.ai.ccb.business.KeyParams;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.requestpacket.FileInfo;
import com.learnbind.ai.ccb.requestpacket.FileInfoList;
import com.learnbind.ai.ccb.responsepacket.ResponseBatchWithhold;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileMode;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertState;
import com.learnbind.ai.ccb.responsepacket.ResponseCertStateList;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCcbRsponseCertStatus;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.service.ccb
 *
 * @Title: CcbBusinessBatchWithholdService.java
 * @Description: 中国建设银行业务处理-批量代扣服务
 *
 * @author Administrator
 * @date 2019年8月24日 上午1:22:32
 * @version V1.0 
 *
 */
@Service
public class CcbInterfaceService {

	private static final Logger logger = LoggerFactory.getLogger(CcbInterfaceService.class);
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private CCBBusiness ccbBusiness;//ccb业务处理
	
	/**
	 * @Title: login
	 * @Description: CCB签到
	 * @return 
	 */
	public boolean login() {
		
		boolean flag = false;//签到成功/失败标志
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			//CCBBusiness ccb = new CCBBusiness();
			flag = ccbBusiness.login(bus_url, headerParams, keyParams);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @Title: logout
	 * @Description: CCB签退
	 * @return 
	 */
	public boolean logout() {
		
		boolean flag = false;//签退成功/失败标志
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			//CCBBusiness ccb = new CCBBusiness();
			flag = ccbBusiness.logout(bus_url, headerParams, keyParams);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	/**
	 * @Title: uploadWithholdFile
	 * @Description: 上传批量代扣文件
	 * @param record
	 * @return 
	 */
	public Map<String, Object> uploadWithholdFile(CcbBatchWithholdRecord record) {
		
		try {
			
			logger.info("----------代扣文件记录信息："+record.toString());
			
			String withholdFile = record.getWithholdFile();
			String filePath = withholdFile.substring(0, withholdFile.lastIndexOf(File.separator)+1);
			String fileName = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			logger.info("----------上传文件路径："+filePath);
			logger.info("----------上传文件名称："+fileName);
			
			String uploadUrl = this.getCCBConfigParm(InterfaceConstant.CCB_FILE_UPLOAD_URL);//向CCB上传文件时的URL路径
			logger.info("----------向CCB上传文件时的URL路径："+uploadUrl);
			String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
			logger.info("----------电子银行合约编号："+chanl_cust_no);
			KeyParams keyParams = this.getKeyParams();
			logger.info("----------上传代扣文件请求 KEY 参数："+keyParams.toString());
			
			//CCBBusiness ccb = new CCBBusiness();
			String ccbVchrFile = ccbBusiness.uploadFileToCCB(uploadUrl, chanl_cust_no, filePath, fileName, keyParams);
			logger.info("----------自CCB端响应的文件名称："+ccbVchrFile);
			
			if (StringUtils.isNotBlank(ccbVchrFile)) {
				Long id = record.getId();
				record = new CcbBatchWithholdRecord();
				record.setId(id);
				record.setCcbVchrFile(ccbVchrFile);
				record.setStatus(EnumCcbBatchStatus.UPLOAD.getValue());
				int rows = ccbBatchWithholdRecordService.updateByPrimaryKeySelective(record);
				if(rows<=0) {
					logger.info("----------更新代扣文件信息失败："+record.toString());
					return RequestResultUtil.getResultFail("更新代扣文件失败，请查看相关日志！");
				}
				return RequestResultUtil.getResultSuccess("上传批量代扣文件成功！");
			}
			return RequestResultUtil.getResultFail("上传批量代扣文件错误，请查看相关日志！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("上传批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: applyProcessWithholdFile
	 * @Description: 申请处理批量代扣文件
	 * @param record
	 * @return 
	 */
	public Map<String, Object> applyProcessWithholdFile(CcbBatchWithholdRecord record) {
		
		try {
			
			String withholdFile = record.getWithholdFile();//批量代扣本地文件名称
			String ccbVchrFile = record.getCcbVchrFile();//上传后CCB返回的文件名
			String txn_SN = record.getFileSn();//代扣文件记录唯一编号
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//与CCB正常业务交互的URL路径
			HeaderParams headerParams = this.getHearderParams();//获取hearder参数 
			BodyParams bodyParams = this.getBodyParams();//获取body参数
			KeyParams keyParams = this.getKeyParams();//获取 KEY 参数
			FileInfoList fileListPack = this.getFileListPack(withholdFile, ccbVchrFile, txn_SN);//获取申请处理批量代扣文件列表
			
			//CCBBusiness ccb = new CCBBusiness();
			//发送批量代扣请求 注:此功能的调用需要在上传批量代后文件后进行.
			ResponseBatchWithhold batchWithhold = ccbBusiness.requestBatchWithhold(bus_url, headerParams, bodyParams, keyParams, fileListPack);
			logger.info("申请处理批量代扣文件返回结果："+batchWithhold);
			if (batchWithhold!=null) {
				//更新批量代扣记录状态为已申请处理
				boolean flag = this.updateWithholdFileRecord(txn_SN, EnumCcbBatchStatus.APPLY_PROCESS);
				if(flag) {
					return RequestResultUtil.getResultSuccess("申请处理批量代扣文件成功！");
				}else {
					return RequestResultUtil.getResultFail("申请处理批量代扣文件成功，更新状态失败，请重新操作！");
				}
				
			}
			return RequestResultUtil.getResultFail("申请处理批量代扣文件错误，请查看相关日志！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("申请处理批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: queryCertStatus
	 * @Description: 查询批量代扣文件处理状态（查询凭证状态）
	 * @param record
	 * @return 
	 */
	public Map<String, Object> queryCertStatus(CcbBatchWithholdRecord record) {
		
		try {
			
			String txn_SN = record.getFileSn();//代扣文件记录唯一编号
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//与CCB正常业务交互的URL路径
			HeaderParams headerParams = this.getHearderParams();//获取hearder参数 
			BodyParams bodyParams = this.getBodyParams();//获取body参数
			KeyParams keyParams = this.getKeyParams();//获取 KEY 参数
			
			//CCBBusiness ccb = new CCBBusiness();
			//代发代扣直联单据查询
			ResponseCertStateList certStateList = ccbBusiness.queryWithholdCertState(bus_url, txn_SN, headerParams, bodyParams, keyParams);
			logger.info("查询批量代扣文件处理状态返回结果："+certStateList);
			if (certStateList!=null) {
				
				List<ResponseCertState> stateList = certStateList.getCertificateList();
				if(stateList!=null && stateList.size()>0) {
					ResponseCertState state = stateList.get(0);
					String vchrSt = state.getVchr_St();
					String statusName = EnumCcbRsponseCertStatus.getName(vchrSt);
					logger.info("查询凭证状态结果："+vchrSt+"，"+statusName);
					if(vchrSt.equals(EnumCcbRsponseCertStatus.STATUS_700.getStatus())) {
						int rows = this.updateWithholdFileRecord(state);
						if(rows>0) {
							return RequestResultUtil.getResultSuccess("查询凭证状态结果为700，CCB处理完成！");
						}
					}
					return RequestResultUtil.getResultFail("凭证状态："+vchrSt+"，"+statusName);
				}
				
			}
			return RequestResultUtil.getResultFail("查询凭证状态返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询凭证状态异常，请检查日志！");
	}
	
	/**
	 * @Title: queryWithholdCertDetailFile
	 * @Description: 查询凭证明细-文件形式
	 * @return 
	 */
	public Map<String, Object> queryWithholdCertDetailFile(CcbBatchWithholdRecord record) {
		
		try {
			
			String txn_SN = record.getFileSn();//代扣文件记录唯一编号
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//与CCB正常业务交互的URL路径
			HeaderParams headerParams = this.getHearderParams();//获取hearder参数 
			KeyParams keyParams = this.getKeyParams();//获取 KEY 参数
			
			//CCBBusiness ccb = new CCBBusiness();
			ResponseCertDetailFileModeList certDetailList = ccbBusiness.queryWithholdCertDetailFileMode(bus_url, txn_SN, headerParams, keyParams);
			logger.info("查询凭证明细-文件形式返回结果："+certDetailList);
			if (certDetailList!=null) {
				
				List<ResponseCertDetailFileMode> detailList = certDetailList.getCertFileDetailList();
				if(detailList!=null && detailList.size()>0) {
					boolean flag = true;
					for(ResponseCertDetailFileMode detail : detailList) {
						logger.info("查询凭证明细-文件形式："+detail);
						String filePath = detail.getFILE_PATH();
						String fileName = detail.getFILE_NAME();
						//根据CCB处理批量代扣文件结果更新本地保存的批量处理文件相关信息
						flag = this.updateWithholdFileRecord(txn_SN, filePath+fileName);
						if(!flag) {
							break;
						}
					}
					
					if(flag) {
						return RequestResultUtil.getResultSuccess("查询凭证明细（文件形式）成功，详情请参考日志！");
					}
					
				}
				
			}
			return RequestResultUtil.getResultFail("查询凭证明细（文件形式）返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询凭证明细（文件形式）异常，请检查日志！");
	}
	
	/**
	 * @Title: queryWithholdFileProcessResult
	 * @Description: 下载批量代扣文件处理结果
	 * @return 
	 */
	public Map<String, Object> downloadWithholdFileProcessResult(CcbBatchWithholdRecord record) {
		
		try {
			
			String downloadUrl = this.getCCBConfigParm(InterfaceConstant.CCB_FILE_DOWNLOAD_URL);//自CCB下载文件时的URL路径
			String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
			KeyParams keyParams = this.getKeyParams();
			
			//CCBBusiness ccb = new CCBBusiness();
//			map<String,String>
//			 * 				  如果文件下载成功,则返回相应的文件内容,(UTF-8编码的文本内容),可将此处接收到的内容写入到业务相关的文件中.
//			 * 					code=1
//			 * 					msg=文件中内容(utf-8编码)
//			 * 				  如果文件下载失败:
//			 * 					code=0
//			 * 					msg= 错误信息
			Map<String,String> resultMap = ccbBusiness.downloadFileFromCCB(downloadUrl, chanl_cust_no, record.getCcbSummaryFiel(), keyParams);
			logger.info("下载批量代扣文件处理结果文件返回内容："+resultMap);
			if (resultMap!=null) {
				String code = resultMap.get("code").toString();
				String msg = resultMap.get("msg").toString();
				
				if(code.equals("0")) {
					logger.info("下载批量代扣文件处理结果文件失败，失败原因："+msg);
					return RequestResultUtil.getResultSuccess("下载批量代扣文件处理结果文件失败，失败原因："+msg);
				}else {
					logger.info("下载批量代扣文件处理结果文件成功，返回内容："+msg);
					
					String filePath = this.downloadScsSaveDataToFile(record, msg);//保存文件到本地
					int rows = this.updateWithholdFileRecord(record.getId(), filePath);//更新数据库
					if(rows>0) {
						return RequestResultUtil.getResultSuccess("下载批量代扣文件处理结果文件成功");
					}else {
						return RequestResultUtil.getResultFail("下载批量代扣文件处理保存到本地失败");
					}
					
				}
				
			}
			return RequestResultUtil.getResultFail("下载批量代扣文件处理结果文件返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("下载批量代扣文件处理结果文件异常，请检查日志！");
	}
	
	//-----------------------------------------辅助方法------------------------------------------------------------------------------------------
	
	/**
	 * @Title: getFileListPack
	 * @Description: 获取申请处理批量代扣文件列表
	 * @param withholdFile	批量代扣本地文件名称
	 * @param ccbVchrFile
	 * @param txn_SN
	 * @return
	 * @throws Exception 
	 */
	private FileInfoList getFileListPack(String withholdFile,String ccbVchrFile, String txn_SN) throws Exception {
		
		String encoding = "GBK";//字符集
		
		//byte[] fileContent = CCBFileUtil.readToByte(withholdFile);
		//logger.info("批量代扣文件内容："+fileContent);
		//String msg_Smy = MD5withRSA.encryptionMd5(fileContent);
		String fileContent = CCBFileUtil.readToString(withholdFile, encoding);
		//logger.info("批量代扣文件内容："+fileContent);
		String msg_Smy = MD5withRSA.encryptionMd5(fileContent);
		//byte[] decryptDesKeyArr=java.util.Base64.getDecoder().decode(ccbVchrFile.getBytes("UTF-8"));		
		//String msg_Smy = new String(decryptDesKeyArr,"UTF-8");
		
		List<FileInfo> infoList = new ArrayList<>();
		FileInfo info = new FileInfo();
		info.setFILE_NAME(ccbVchrFile);
		info.setMsg_Smy(msg_Smy);
		info.setTxn_SN(txn_SN);
		infoList.add(info);
		
		FileInfoList fileList = new FileInfoList();
		fileList.setFileList(infoList);
		return fileList;
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 根据CCB处理批量代扣文件结果更新本地保存的批量处理文件相关信息
	 * @param state 
	 */
	private boolean updateWithholdFileRecord(String fileSn, String ccbSummaryFiel) {
		
		Example example = new Example(CcbBatchWithholdRecord.class);
		example.createCriteria().andEqualTo("fileSn", fileSn);
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setCcbSummaryFiel(ccbSummaryFiel);
		int rows = ccbBatchWithholdRecordService.updateByExampleSelective(record, example);
		if(rows>0) {
			logger.info("更新批量代扣记录成功："+fileSn+"---"+record);
			return true;
		}else {
			logger.info("更新批量代扣记录失败："+fileSn+"---"+record);
		}
		return false;
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 更新批量代扣记录状态
	 * @param fileSn	批量代扣记录唯一编号
	 * @param status	状态
	 * @return 
	 */
	private boolean updateWithholdFileRecord(String fileSn, EnumCcbBatchStatus status) {
		
		Example example = new Example(CcbBatchWithholdRecord.class);
		example.createCriteria().andEqualTo("fileSn", fileSn);
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setStatus(status.getValue());
		int rows = ccbBatchWithholdRecordService.updateByExampleSelective(record, example);
		if(rows>0) {
			logger.info("更新批量代扣记录状态，更新状态为："+status.getName());
			return true;
		}else {
			logger.info("更新批量代扣记录状态失败，更新状态为："+status.getName());
		}
		return false;
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 根据CCB处理批量代扣文件结果更新本地保存的批量处理文件相关信息
	 * @param state 
	 */
	private int updateWithholdFileRecord(ResponseCertState state) {
		
		Example example = new Example(CcbBatchWithholdRecord.class);
		example.createCriteria().andEqualTo("fileSn", state.getTxn_SN());
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setCcbVchrId(state.getVchID());
		record.setStatus(EnumCcbBatchStatus.PROCESS_COMPLETE.getValue());//更新状态为已处理完成
		//record.setCcbSummaryFiel(state.getFILE_NM());
		record.setSuccessTotalAmount(new BigDecimal(state.getScss_Amt()));
		record.setSuccessTotalNum(Integer.valueOf(state.getScss_Dnum()));
		record.setFailTotalAmount(new BigDecimal(state.getFail_Amt()));
		record.setFailTotalNum(Integer.valueOf(state.getFail_Dnum()));
		int rows = ccbBatchWithholdRecordService.updateByExampleSelective(record, example);
		if(rows>0) {
			logger.info("更新批量代扣记录成功："+state.getTxn_SN()+"---"+record);
		}else {
			logger.info("更新批量代扣记录失败："+state.getTxn_SN()+"---"+record);
		}
		return rows;
	}
	
	/**
	 * @Title: downloadScsSaveDataToFile
	 * @Description: 自CCB下载批量代扣处理结果文件保存的本地路径
	 * @param record
	 * @param data
	 * @return 
	 */
	private String downloadScsSaveDataToFile(CcbBatchWithholdRecord record, String data) {
		
		Date sysDate = new Date();//系统时间
		
		String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
		String filePath = this.getPath();//根据操作系统类型获取上传文件目录
		filePath = filePath + File.separator + "withhold-result-txt"+ File.separator + DateUtils.getYYYYMMDDStr() + File.separator;
		
		String fileName = "CCB-"+sn+".txt";
		
		CCBFileUtil.saveDataToFile(filePath, fileName, data);
		logger.info("自CCB下载批量代扣处理结果文件保存的本地路径："+filePath+fileName);
		return filePath+fileName;
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 更新批量代扣文件记录（更新自CCB下载后的批量代扣结果文件保存到本地后的路径）
	 * @param id
	 * @param filePath 
	 */
	private int updateWithholdFileRecord(Long id, String filePath) {
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setId(id);
		record.setSummaryFile(filePath);
		record.setStatus(EnumCcbBatchStatus.DOWNLOAD_RETURN.getValue());
		int rows = ccbBatchWithholdRecordService.updateByPrimaryKeySelective(record);
		if(rows>0) {
			logger.info("更新数据库成功，自CCB下载后的批量代扣结果文件保存到本地后的路径："+filePath);
		}
		return rows;
	}
	
	/**
	 * 自CCB中获取配置参数
	 * 
	 * @param parmKey
	 * @return
	 */
	private String getCCBConfigParm(String parmKey) {
		String parmValue = "";
		SysInterfaceConfig config = new SysInterfaceConfig();
		config.setInterfaceType(InterfaceConstant.INTERFACE_TYPE_CCB);
		config.setKey(parmKey);
		SysInterfaceConfig result = interfaceConfigService.selectOne(config);
		if (result != null) {
			parmValue = result.getValue();
		}
		return parmValue;
	}
	
	/**
	 * TODO 用于实际项目中. 测试状态:已经测试 生成通用KEY,用于交换密钥时使用
	 * 
	 * @param customerNo 客户号
	 * @return
	 */
	private String genCommonKey(String customerNo) {
		String firstPart = "";
		if (customerNo.length() >= 10) {
			firstPart = customerNo.substring(customerNo.length() - 10);
		} else {
			firstPart = String.format("%10s", customerNo);
			firstPart = firstPart.replace(" ", "0");
		}
		String dateStr = DateUtil.format(new Date(), "yyMMdd");
		return firstPart + dateStr;
	}
	
	/**
	 * @Title: getHearderParams
	 * @Description: 获取hearder参数 
	 * @return 
	 */
	private HeaderParams getHearderParams() {
		
		String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
		String txn_itt_ip_adr = this.getCCBConfigParm(InterfaceConstant.CCB_LOCAL_HOST_IP);//营业收费系统本机IP地址  (自营业收费系统发起交易时使用)
		String txn_stff_id = this.getCCBConfigParm(InterfaceConstant.CCB_STUFF_ID);//交易员编号（操作员编号）
		
		HeaderParams params = new HeaderParams();
		params.setChanl_cust_no(chanl_cust_no);
		params.setTxn_itt_ip_adr(txn_itt_ip_adr);
		params.setTxn_stff_id(txn_stff_id);
		return params;
	}
	
	/**
	 * @Title: getBodyParams
	 * @Description: 获取body参数
	 * @return 
	 */
	private BodyParams getBodyParams() {
		
		String entrst_prj_id = this.getCCBConfigParm(InterfaceConstant.CCB_ENTRUST_PROJECT_ID);//委托项目编号
		String prj_use_id = this.getCCBConfigParm(InterfaceConstant.CCB_PROJECT_USE_ID);//项目用途编号
		String etrunt_accno = this.getCCBConfigParm(InterfaceConstant.CCB_ENTRUST_UNIT_ACCOUNT_NO);//委托单位帐号
		
		BodyParams bodyParams = new BodyParams();
		bodyParams.setEntrst_prj_id(entrst_prj_id);
		bodyParams.setEtrunt_accno(etrunt_accno);
		bodyParams.setPrj_use_id(prj_use_id);
		return bodyParams;
	}
	
	/**
	 * @Title: getKeyParams
	 * @Description: 获取 KEY 参数
	 * @return 
	 */
	private KeyParams getKeyParams() {
		
		String path = this.getPath();
		
		String desKey = this.getCCBConfigParm(InterfaceConstant.CCB_DES_KEY);
		String localPrivateKeyPath = this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
		PrivateKey localPrivateKey = KeyFileUtil.loadRsaPrivateKeyFromFile(path+localPrivateKeyPath);
		String ccbPublicKeyPath = this.getCCBConfigParm(InterfaceConstant.CCB_RSA_PUBLIC_KEY_FILE_PATH_CCB);
		PublicKey ccbPublicKey = KeyFileUtil.loadRsaPublicKeyFromFile(path+ccbPublicKeyPath);
		
		KeyParams keyParams = new KeyParams();
		try {
			keyParams.setDesKey(desKey);
			logger.info("从数据库中获取到的DES KEY:"+desKey);
		} catch (UnsupportedEncodingException e) {
			logger.info("设置 DES KEY 异常，请查看日志！");
			e.printStackTrace();
		}
		keyParams.setLocalPrivateKey(localPrivateKey);
		keyParams.setCcbPublicKey(ccbPublicKey);
		return keyParams;
	}
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getUploadFolder();
		System.out.println("----------上传文件目录:"+path);
		return path;
	}
	
}
