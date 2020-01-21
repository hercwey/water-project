package com.learnbind.ai.controller.ccb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.ccb.CcbBusinessUtils;
import com.learnbind.ai.ccb.CcbTestConstant;
import com.learnbind.ai.ccb.CcbUploadFileRowBean;
import com.learnbind.ai.ccb.business.BodyParams;
import com.learnbind.ai.ccb.business.CCBBusiness;
import com.learnbind.ai.ccb.business.HeaderParams;
import com.learnbind.ai.ccb.business.KeyParams;
import com.learnbind.ai.ccb.business.QueryAccountBalanceParams;
import com.learnbind.ai.ccb.business.QueryAccountDetailParams;
import com.learnbind.ai.ccb.business.TransferParams;
import com.learnbind.ai.ccb.business.TransferResultParams;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.ccb.keyfile.KeyFileUtil;
import com.learnbind.ai.ccb.md5.MD5withRSA;
import com.learnbind.ai.ccb.requestpacket.AccountForDetail;
import com.learnbind.ai.ccb.requestpacket.AccountForDetailList;
import com.learnbind.ai.ccb.requestpacket.AccountInfoForBalance;
import com.learnbind.ai.ccb.requestpacket.AccountInfoForBalanceList;
import com.learnbind.ai.ccb.requestpacket.FileInfo;
import com.learnbind.ai.ccb.requestpacket.FileInfoList;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountBalance;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountBalanceList;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountDetail;
import com.learnbind.ai.ccb.responsepacket.ResponseAccountDetailList;
import com.learnbind.ai.ccb.responsepacket.ResponseBatchWithhold;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileMode;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailFileModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailPacketMode;
import com.learnbind.ai.ccb.responsepacket.ResponseCertDetailPacketModeList;
import com.learnbind.ai.ccb.responsepacket.ResponseCertState;
import com.learnbind.ai.ccb.responsepacket.ResponseCertStateList;
import com.learnbind.ai.ccb.responsepacket.ResponseTransfer;
import com.learnbind.ai.ccb.responsepacket.ResponseTransferResult;
import com.learnbind.ai.ccb.responsepacket.ResponseTransferResultList;
import com.learnbind.ai.ccb.sdk.Des3Util;
import com.learnbind.ai.ccb.sdk.rsa.PemRSAEncrypt;
import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumCcbBatchStatus;
import com.learnbind.ai.common.enumclass.EnumCcbRsponseCertStatus;
import com.learnbind.ai.common.enumclass.EnumDeductType;
import com.learnbind.ai.common.enumclass.EnumEnabledStatus;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.InterfaceConstant;
import com.learnbind.ai.constant.RoleCodeConstant;
import com.learnbind.ai.model.CcbBatchWithholdRecord;
import com.learnbind.ai.model.CustomerBanks;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.SysInterfaceConfig;
import com.learnbind.ai.model.SysRoles;
import com.learnbind.ai.service.ccb.CcbBatchWithholdRecordService;
import com.learnbind.ai.service.customers.BankService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.interfaceconfig.InterfaceConfigService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.util.HttpClientUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.ccb
 *
 * @Title: CcbTestController.java
 * @Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author Administrator
 * @date 2019年7月18日 上午9:35:51
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/ccb-test")
public class CcbTestController {
	
	private static final Logger logger = LoggerFactory.getLogger(CcbTestController.class);
	
	//private static String YYYY_MM_DD = CcbBusinessUtils.getDateRandom();
	
	private static final String TEMPLATE_PATH = "ccb_test/";//页面目录
	private static final int PAGE_SIZE = 8; // 页大小
	
	@Autowired
	InterfaceConfigService interfaceConfigService;//接口配置
	@Autowired
	private UploadFileConfig uploadFileConfig;   //路径配置服务
	@Autowired
	private CcbBatchWithholdRecordService ccbBatchWithholdRecordService;//中国建设银行批量代扣记录服务
	@Autowired
	private LocationService locationService;//地理位置服务
	@Autowired
	private CustomerAccountItemService customerAccountItemService; // 客户账单信息
	@Autowired
	private CustomersService customersService;//客户
	@Autowired
	private BankService bankService;//客户银行信息
	
	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "ccb_test_starter";
	}
	
	/**
	 * @Title: generatorRsaKeyPairToFile
	 * @Description: 生成本地RSA密钥对
	 * @return 
	 */
	@RequestMapping(value = "/create-local-rsa", produces = "application/json")
	@ResponseBody
	public Object generatorRsaKeyPairToFile() {
		
		try {
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			
			logger.info("RSA公钥路径："+publicKeyFilePath);
			logger.info("RSA私钥路径："+privateKeyFilePath);
			
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			
			return RequestResultUtil.getResultSuccess("本地RSA密钥对已生成！");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("本地RSA密钥对生成异常，请检查日志！");
	}
	
	/**
	 * @Title: getCcbDesKeyToSave
	 * @Description: 自CCB获取DES并保存
	 * @return 
	 */
	@RequestMapping(value = "/get-ccb-des-to-save", produces = "application/json")
	@ResponseBody
	public Object getCcbDesKeyToSave() {
		
		try {
			String respMsg = this.getCcbDesKeyTransPost();
			return RequestResultUtil.getResultSuccess(respMsg);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("自CCB获取DES KEY异常，请检查日志！");
	}
	
	/**
	 * @Title: getCcbRsaPublicKeyToSave
	 * @Description: 获取CCB RSA 公钥并保存
	 * @return 
	 */
	@RequestMapping(value = "/get-ccb-rsa-public-key-to-save", produces = "application/json")
	@ResponseBody
	public Object getCcbRsaPublicKeyToSave() {
		
		try {
			String respMsg = this.getRsaPubKeyTransPost();
			return RequestResultUtil.getResultSuccess(respMsg);
		} catch (ServletException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("自CCB获取DES KEY异常，请检查日志！");
	}
	
	/**
	 * @Title: generatorWithholdFile
	 * @Description: 生成批量代扣文件
	 * @return 
	 */
	@RequestMapping(value = "/generator-withhold-file", produces = "application/json")
	@ResponseBody
	public Object generatorWithholdFile() {
		
		try {
			
			String type = CcbBusinessUtils.getType();//获取类型
			
			List<CcbBatchWithholdRecord> recordList = new ArrayList<CcbBatchWithholdRecord>();//保存创建上传文件后的记录，全部完成后保存到数据库
			
			List<CcbUploadFileRowBean> rowList = new ArrayList<>();
			CcbUploadFileRowBean row1 = new CcbUploadFileRowBean();
			row1.setCardNo(CcbTestConstant.CUSTOMER_CARD_1);
			row1.setAccountName(CcbTestConstant.CUSTOMER_CARD_NAME_1);
			row1.setTotalAmount(new BigDecimal("5.14"));
			rowList.add(row1);
			CcbUploadFileRowBean row2 = new CcbUploadFileRowBean();
			row2.setCardNo(CcbTestConstant.CUSTOMER_CARD_2);
			row2.setAccountName(CcbTestConstant.CUSTOMER_CARD_NAME_2);
			row2.setTotalAmount(new BigDecimal("4.50"));
			rowList.add(row2);
			CcbUploadFileRowBean row3 = new CcbUploadFileRowBean();
			row3.setCardNo(CcbTestConstant.CUSTOMER_CARD_3);
			row3.setAccountName(CcbTestConstant.CUSTOMER_CARD_NAME_3);
			row3.setTotalAmount(new BigDecimal("4.79"));
			rowList.add(row3);
			
			String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
			String filePath = this.saveDataToFile(type, sn, rowList);//保存文件
			CcbBatchWithholdRecord record =  this.getCcbBatchWithholdRecord(type, sn, filePath);//创建记录实体
			recordList.add(record);
			
			int rows = ccbBatchWithholdRecordService.saveList(recordList);
			if (rows > 0) {
				return RequestResultUtil.getResultSuccess("创建批量代扣文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("创建批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: uploadWithholdFile
	 * @Description: 上传批量代扣文件
	 * @return 
	 */
	@RequestMapping(value = "/upload-withhold-file", produces = "application/json")
	@ResponseBody
	public Object uploadWithholdFile() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			
			String withholdFile = record.getWithholdFile();
			String filePath = withholdFile.substring(0, withholdFile.lastIndexOf(File.separator)+1);
			String fileName = withholdFile.substring(withholdFile.lastIndexOf(File.separator)+1);
			logger.info("上传文件路径："+filePath);
			logger.info("上传文件名称："+fileName);
			
			String uploadUrl = this.getCCBConfigParm(InterfaceConstant.CCB_FILE_UPLOAD_URL);
			String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			String ccbVchrFile = ccb.uploadFileToCCB(uploadUrl, chanl_cust_no, filePath, fileName, keyParams);
			if (StringUtils.isNotBlank(ccbVchrFile)) {
				Long id = record.getId();
				record = new CcbBatchWithholdRecord();
				record.setId(id);
				record.setCcbVchrFile(ccbVchrFile);
				ccbBatchWithholdRecordService.updateByPrimaryKeySelective(record);
				return RequestResultUtil.getResultSuccess("上传批量代扣文件成功！");
			}
			return RequestResultUtil.getResultSuccess("上传批量代扣文件错误，请查看相关日志！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("上传批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: applyProcessWithholdFile
	 * @Description: 申请处理批量代扣文件
	 * @return 
	 */
	@RequestMapping(value = "/apply-process-withhold-file", produces = "application/json")
	@ResponseBody
	public Object applyProcessWithholdFile() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			String withholdFile = record.getWithholdFile();//批量代扣本地文件名称
			String ccbVchrFile = record.getCcbVchrFile();//上传后CCB返回的文件名
			String txn_SN = record.getFileSn();
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			BodyParams bodyParams = this.getBodyParams();
			KeyParams keyParams = this.getKeyParams();
			FileInfoList fileListPack = this.getFileListPack(withholdFile, ccbVchrFile, txn_SN);
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseBatchWithhold batchWithhold = ccb.requestBatchWithhold(bus_url, headerParams, bodyParams, keyParams, fileListPack);
			logger.info("申请处理批量代扣文件返回结果："+batchWithhold);
			if (batchWithhold!=null) {
				return RequestResultUtil.getResultSuccess("申请处理批量代扣文件成功！");
			}
			return RequestResultUtil.getResultSuccess("申请处理批量代扣文件错误，请查看相关日志！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("申请处理批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: queryWithholdFileProcessResult
	 * @Description: 查询批量代扣文件处理状态(查询凭证状态)
	 * @return 
	 */
	@RequestMapping(value = "/query-withhold-file-process-status", produces = "application/json")
	@ResponseBody
	public Object queryWithholdFileProcessResult() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			String txn_SN = record.getFileSn();
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			BodyParams bodyParams = this.getBodyParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseCertStateList certStateList = ccb.queryWithholdCertState(bus_url, txn_SN, headerParams, bodyParams, keyParams);
			logger.info("查询批量代扣文件处理状态返回结果："+certStateList);
			if (certStateList!=null) {
				
				List<ResponseCertState> stateList = certStateList.getCertificateList();
				if(stateList!=null && stateList.size()>0) {
					ResponseCertState state = stateList.get(0);
					String vchrSt = state.getVchr_St();
					String statusName = EnumCcbRsponseCertStatus.getName(vchrSt);
					logger.info("查询凭证状态结果："+vchrSt+"，"+statusName);
					if(vchrSt.equals(EnumCcbRsponseCertStatus.STATUS_700.getStatus())) {
						this.updateWithholdFileRecord(state);
					}
					return RequestResultUtil.getResultSuccess("凭证状态："+vchrSt+"，"+statusName);
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询凭证状态返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询凭证状态异常，请检查日志！");
	}
	
	/**
	 * @Title: queryWithholdCertDetailPacket
	 * @Description: 查询凭证明细-报文方式
	 * @return 
	 */
	@RequestMapping(value = "/query-withhold-cert-detail-packet", produces = "application/json")
	@ResponseBody
	public Object queryWithholdCertDetailPacket() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			String txn_SN = record.getFileSn();
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseCertDetailPacketModeList certDetailList = ccb.queryWithholdCertDetailPacketMode(bus_url, txn_SN, headerParams, keyParams);
			logger.info("查询凭证明细-报文方式返回结果："+certDetailList);
			if (certDetailList!=null) {
				
				List<ResponseCertDetailPacketMode> detailList = certDetailList.getCertDetailList();
				if(detailList!=null && detailList.size()>0) {
					for(ResponseCertDetailPacketMode detail : detailList) {
						logger.info("查询凭证明细-报文方式："+detail);
					}
					
					return RequestResultUtil.getResultSuccess("查询凭证明细（报文方式）成功，详情请参考日志！");
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询凭证明细（报文方式）返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询凭证明细（报文方式）异常，请检查日志！");
	}
	
	/**
	 * @Title: queryWithholdCertDetailFile
	 * @Description: 查询凭证明细-文件形式
	 * @return 
	 */
	@RequestMapping(value = "/query-withhold-cert-detail-file", produces = "application/json")
	@ResponseBody
	public Object queryWithholdCertDetailFile() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			String txn_SN = record.getFileSn();
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseCertDetailFileModeList certDetailList = ccb.queryWithholdCertDetailFileMode(bus_url, txn_SN, headerParams, keyParams);
			logger.info("查询凭证明细-文件形式返回结果："+certDetailList);
			if (certDetailList!=null) {
				
				List<ResponseCertDetailFileMode> detailList = certDetailList.getCertFileDetailList();
				if(detailList!=null && detailList.size()>0) {
					for(ResponseCertDetailFileMode detail : detailList) {
						logger.info("查询凭证明细-文件形式："+detail);
						String filePath = detail.getFILE_PATH();
						String fileName = detail.getFILE_NAME();
						
						this.updateWithholdFileRecord(txn_SN, filePath+fileName);
					}
					
					return RequestResultUtil.getResultSuccess("查询凭证明细（文件形式）成功，详情请参考日志！");
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询凭证明细（文件形式）返回信息为空！");
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
	@RequestMapping(value = "/download-withhold-file-process-result", produces = "application/json")
	@ResponseBody
	public Object downloadWithholdFileProcessResult() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			
			String downloadUrl = this.getCCBConfigParm(InterfaceConstant.CCB_FILE_DOWNLOAD_URL);//TODO 申请处理批量代扣文件URL
			String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
//			map<String,String>
//			 * 				  如果文件下载成功,则返回相应的文件内容,(UTF-8编码的文本内容),可将此处接收到的内容写入到业务相关的文件中.
//			 * 					code=1
//			 * 					msg=文件中内容(utf-8编码)
//			 * 				  如果文件下载失败:
//			 * 					code=0
//			 * 					msg= 错误信息
			Map<String,String> resultMap = ccb.downloadFileFromCCB(downloadUrl, chanl_cust_no, record.getCcbSummaryFiel(), keyParams);
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
					this.updateWithholdFileRecord(record.getId(), filePath);//更新数据库
					
					return RequestResultUtil.getResultSuccess("下载批量代扣文件处理结果文件成功");
				}
				
			}
			return RequestResultUtil.getResultSuccess("下载批量代扣文件处理结果文件返回信息为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("下载批量代扣文件处理结果文件异常，请检查日志！");
	}
	
	/**
	 * @Title: transferAccount
	 * @Description: 转账
	 * @return 
	 */
	@RequestMapping(value = "/transfer-account", produces = "application/json")
	@ResponseBody
	public Object transferAccount() {
		
		try {
			
			List<CcbBatchWithholdRecord> recordList = ccbBatchWithholdRecordService.selectAll();
			if(recordList==null || recordList.size()<=0) {
				return RequestResultUtil.getResultSuccess("批量代扣文件列表为空！");
			}
			
			CcbBatchWithholdRecord record = recordList.get(0);
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
			HeaderParams headerParams = this.getHearderParams();
			BodyParams bodyParams = this.getBodyParams();
			KeyParams keyParams = this.getKeyParams();
			TransferParams transferParams = this.getTransferParams();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseTransfer responseTransfer = ccb.transferAccount(bus_url, headerParams, transferParams, keyParams);
			logger.info("请求转账返回内容："+responseTransfer);
			if (responseTransfer!=null) {
				String resultStatus = responseTransfer.getCshMgt_Txn_Rslt_Cd();//1-交易成功2-交易失败3-处理中
				logger.info("请求转账返回状态(1-交易成功2-交易失败3-处理中):"+resultStatus);
				if(resultStatus.equals("1")) {
					return RequestResultUtil.getResultSuccess("请求转账成功，状态：交易成功");
				}else if(resultStatus.equals("2")) {
					return RequestResultUtil.getResultSuccess("请求转账成功，状态：交易失败，错误代码："+responseTransfer.getCshMgt_Err_Cd()+"，错误原因："+responseTransfer.getErr_Inf());
				}else if(resultStatus.equals("3")) {
					return RequestResultUtil.getResultSuccess("请求转账成功，状态：处理中");
				}else {
					return RequestResultUtil.getResultSuccess("请求转账返回结果异常，请查看日志！");
				}
				
			}
			return RequestResultUtil.getResultSuccess("请求转账结果返回为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("请求转账结果异常，请检查日志！");
	}
	
	/**
	 * @Title: queryTransferAccountResult
	 * @Description: 查询转账结果
	 * @return 
	 */
	@RequestMapping(value = "/query-transfer-account-result", produces = "application/json")
	@ResponseBody
	public Object queryTransferAccountResult() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			TransferResultParams transferResultParams = this.getTransferResultParams();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseTransferResultList transferResultList = ccb.queryTransferAccountResult(bus_url, headerParams, transferResultParams, keyParams);
			logger.info("查询转账结果返回内容："+transferResultList);
			if (transferResultList!=null) {
//				现金管理交易结果代码
//				DF数据格式:1!n
//				1-交易成功
//				2-交易失败
//				3-处理中
//				4-部分成功
//				a-银行已汇出
//				b-落地网点
//				c-大额关账转预约
//				d-定制交易已预约
//				e-跨行交易等待补录
//				f-结果不确定等待对账
//				DF数据格式：1!n
				List<ResponseTransferResult> resultList = transferResultList.getTranferResultList();
				
				if(resultList!=null && resultList.size()>0) {
					ResponseTransferResult result = resultList.get(0);
					String resultStatus = result.getCshMgt_Txn_Rslt_Cd();
					logger.info("查询转账结果返回状态(1-交易成功\r\n" + 
							"//				2-交易失败\r\n" + 
							"//				3-处理中\r\n" + 
							"//				4-部分成功\r\n" + 
							"//				a-银行已汇出\r\n" + 
							"//				b-落地网点\r\n" + 
							"//				c-大额关账转预约\r\n" + 
							"//				d-定制交易已预约\r\n" + 
							"//				e-跨行交易等待补录\r\n" + 
							"//				f-结果不确定等待对账):\r\n"+resultStatus);
					if(resultStatus.equals("1")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：交易成功");
					}else if(resultStatus.equals("2")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：交易失败，错误代码："+result.getCshMgt_Err_Cd()+"，错误原因："+result.getErr_Inf());
					}else if(resultStatus.equals("3")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：处理中");
					}else if(resultStatus.equals("4")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：银行已汇出");
					}else if(resultStatus.equals("a")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：银行已汇出");
					}else if(resultStatus.equals("b")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：落地网点");
					}else if(resultStatus.equals("c")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：大额关账转预约");
					}else if(resultStatus.equals("d")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：定制交易已预约");
					}else if(resultStatus.equals("e")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：跨行交易等待补录");
					}else if(resultStatus.equals("f")) {
						return RequestResultUtil.getResultSuccess("查询转账结果，状态：结果不确定等待对账");
					}else {
						return RequestResultUtil.getResultSuccess("查询转账结果返回结果异常，请查看日志！");
					}
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询转账结果返回为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询转账结果结果异常，请检查日志！");
	}
	
	/**
	 * @Title: queryAccountBalance
	 * @Description: 查询余额
	 * @return 
	 */
	@RequestMapping(value = "/query-account-balance", produces = "application/json")
	@ResponseBody
	public Object queryAccountBalance() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//与CCB正常业务交互的URL路径
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			QueryAccountBalanceParams queryAccountBalanceParams = this.getQueryAccountBalanceParams();
			AccountInfoForBalanceList accountInfoList = this.getAccountInfoList();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseAccountBalanceList balanceList = ccb.queryAccountBalance(bus_url, headerParams, queryAccountBalanceParams, accountInfoList, keyParams);
			
			logger.info("查询账户余额结果返回内容："+balanceList);
			if (balanceList!=null) {
				List<ResponseAccountBalance> resultList = balanceList.getAccountBalanceList();
				
				if(resultList!=null && resultList.size()>0) {
					for(ResponseAccountBalance balance : resultList) {
						logger.info("账户余额:"+balance.toString());
					}
					return RequestResultUtil.getResultSuccess("查询账户余额成功,详细信息请查看日志！");
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询账户余额结果返回为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询账户余额结果结果异常，请检查日志！");
	}
	
	/**
	 * @Title: queryAccountBalance
	 * @Description: 查询账户明细
	 * @return 
	 */
	@RequestMapping(value = "/query-account-detail", produces = "application/json")
	@ResponseBody
	public Object queryAccountDetail() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);//TODO 申请处理批量代扣文件URL
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			QueryAccountDetailParams queryAccountDetailParams = this.getQueryAccountDetailParams();
			AccountForDetailList accountList = this.getAccountForDetailList();
			
			CCBBusiness ccb = new CCBBusiness();
			ResponseAccountDetailList detailList = ccb.queryAccountDetail(bus_url, headerParams, queryAccountDetailParams, accountList, keyParams);
			
			logger.info("查询账户明细结果返回内容："+detailList);
			if (detailList!=null) {
				List<ResponseAccountDetail> resultList = detailList.getAccountDetailList();
				
				if(resultList!=null && resultList.size()>0) {
					for(ResponseAccountDetail detail : resultList) {
						logger.info("账户明细:"+detail.toString());
					}
					return RequestResultUtil.getResultSuccess("查询账户明细成功,详细信息请查看日志！");
				}
				
			}
			return RequestResultUtil.getResultSuccess("查询账户明细结果返回为空！");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("查询账户明细结果结果异常，请检查日志！");
	}
	
	/**
	 * @Title: login
	 * @Description: 签到
	 * @return 
	 */
	@RequestMapping(value = "/login", produces = "application/json")
	@ResponseBody
	public Object login() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			boolean flag = ccb.login(bus_url, headerParams, keyParams);
			if(flag) {
				return RequestResultUtil.getResultSuccess("签到成功");
			}
			return RequestResultUtil.getResultSuccess("签到失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("签到异常，请检查日志！");
	}
	
	/**
	 * @Title: logout
	 * @Description: 签退
	 * @return 
	 */
	@RequestMapping(value = "/logout", produces = "application/json")
	@ResponseBody
	public Object logout() {
		
		try {
			
			String bus_url = this.getCCBConfigParm(InterfaceConstant.CCB_BUSINESS_URL);
			HeaderParams headerParams = this.getHearderParams();
			KeyParams keyParams = this.getKeyParams();
			
			CCBBusiness ccb = new CCBBusiness();
			boolean flag = ccb.logout(bus_url, headerParams, keyParams);
			if(flag) {
				return RequestResultUtil.getResultSuccess("签退成功");
			}
			return RequestResultUtil.getResultSuccess("签退失败");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultFail("签退异常，请检查日志！");
	}
	
	/**
	 * @Title: getQueryAccountDetailParams
	 * @Description: 获取查询账户明细参数	TODO 待确定
	 * @return 
	 */
	private QueryAccountDetailParams getQueryAccountDetailParams() {
		
		String entrst_prj_id = this.getCCBConfigParm(InterfaceConstant.CCB_ENTRUST_PROJECT_ID);//委托项目编号
		String prj_use_id = this.getCCBConfigParm(InterfaceConstant.CCB_PROJECT_USE_ID);//项目用途编号
		String etrunt_accno = this.getCCBConfigParm(InterfaceConstant.CCB_ENTRUST_UNIT_ACCOUNT_NO);//委托单位帐号
		
		QueryAccountDetailParams params = new QueryAccountDetailParams();
		params.setCCstTr_ID(CcbTestConstant.CASH_TREE_NO);//现金客户树编号
		params.setCCstTrNdID(CcbTestConstant.CASH_TREE_NODE_NO);//现金客户树节点编号
		params.setCntrprt_Acc(CcbTestConstant.TEST_ACCOUNT_1);//对方账户,此处为水司在建设银行开立的账户-账号
		params.setCntrprt_AccNm(CcbTestConstant.TEST_ACCOUNT_1_COMPANY_NAME);//对方账户名称,此处为水司在建设银行开立的账户名称
		params.setCst_ID(CcbTestConstant.CUSTOMER_NO);//客户编号
		params.setStDt("20190601");//开始日期  DF数据格式:yyyymmdd
		params.setEdDt("20190720");//结束日期  DF数据格式:yyyymmdd
		
		return params;
	}
	/**
	 * @Title: getAccountForDetailList
	 * @Description: 获取查询账户明细的账号集合	TODO 待确定
	 * @return 
	 */
	private AccountForDetailList getAccountForDetailList() {
		
		List<AccountForDetail> accountList = new ArrayList<>();
		
		AccountForDetail account = new AccountForDetail();
		account.setAccNo(CcbTestConstant.TEST_ACCOUNT_1);//账号(需查询明细的帐号)
		accountList.add(account);
		
		AccountForDetailList detailList = new AccountForDetailList();
		detailList.setAccountList(accountList);
		return detailList;
	}
	
	/**
	 * @Title: getAccountInfoList
	 * @Description: 获取账户信息集合	TODO 未设置 
	 * @return 
	 */
	private AccountInfoForBalanceList getAccountInfoList() {
		
		AccountInfoForBalance accountInfo = new AccountInfoForBalance();
		String account_no = this.getCCBConfigParm(InterfaceConstant.CCB_ENTRUST_UNIT_ACCOUNT_NO);//水司账号
		accountInfo.setCash_Cst_AccNo(account_no);//现金客户账号,需要在项目中进行配置.
		
		List<AccountInfoForBalance> accountList = new ArrayList<>();
		accountList.add(accountInfo);
		
		AccountInfoForBalanceList accountInfoList = new AccountInfoForBalanceList();
		accountInfoList.setAccountList(accountList);
		return accountInfoList;
	}
	/**
	 * @Title: getQueryAccountBalanceParams
	 * @Description: 获取查询账户余额参数	TODO 未设置,可在接口配置中获取水司的相关信息
	 * @return 
	 */
	private QueryAccountBalanceParams getQueryAccountBalanceParams() {
		
		//String cst_ID = this.getCCBConfigParm(InterfaceConstant.CCB_COMPANY_CUSTOMER_NO);//企业客户号
		String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子合约编号
		String cash_tree_no = this.getCCBConfigParm(InterfaceConstant.CASH_TREE_NO);//现金管理树编号
		String cash_tree_node_no = this.getCCBConfigParm(InterfaceConstant.CASH_TREE_NODE_NO);//现金管理树节点编号
		QueryAccountBalanceParams params = new QueryAccountBalanceParams();
		params.setCst_ID(CcbTestConstant.CUSTOMER_NO);//客户编号  18!n,客户通过签约 唯一确认，固定
		params.setElcSubAR_ID(chanl_cust_no);//电子合约编号
		params.setCCstTr_ID(cash_tree_no);//现金客户树编号  CMN+10位顺序号，客户通过签约唯一确认，固定
		params.setCCstTrNdID(cash_tree_node_no);//现金客户树节点编号  ND+18位客户编号+5位顺序号，客户通过签约唯一确认，固定
		return params;
	}
	
	/**
	 * @Title: getTransferResultParams
	 * @Description: 获取查询转账结果参数 TODO 待确定
	 */
	private TransferResultParams getTransferResultParams() {
		
		String cash_tree_no = this.getCCBConfigParm(InterfaceConstant.CASH_TREE_NO);//现金管理树编号
		String cash_tree_node_no = this.getCCBConfigParm(InterfaceConstant.CASH_TREE_NODE_NO);//现金管理树节点编号
		
		TransferResultParams transferResultParams = new TransferResultParams();
		transferResultParams.setCCstTr_ID(cash_tree_no);//现金客户树编号
		transferResultParams.setCCstTrNdID(cash_tree_node_no);//现金客户树节点编号
		transferResultParams.setCst_ID(CcbTestConstant.CUSTOMER_NO);//客户编号
		transferResultParams.setCstPty_Py_Jrnl_No(this.getCustomNo());//客户方支付流水号 由营收系统自行提供
		transferResultParams.setEnqr_StDt("20190601");//查询起始日期  DF数据格式:yyyymmdd
		transferResultParams.setEnqr_CODt("20190823");//查询终止日期  DF数据格式:yyyymmdd
		transferResultParams.setPyr_AccNm(CcbTestConstant.TEST_ACCOUNT_2_COMPANY_NAME);//付款方帐户名称
		transferResultParams.setPyr_Cst_AccNo(CcbTestConstant.TEST_ACCOUNT_2);//付款方客户账号(水司帐号)
		transferResultParams.setRcvPrt_Cst_AccNo(CcbTestConstant.TEST_ACCOUNT_1);//收款方客户帐号
		transferResultParams.setRcvPtAc_Nm(CcbTestConstant.TEST_ACCOUNT_1_COMPANY_NAME);//收款方账户名称
		return transferResultParams;
	}
	
	/**
	 * @Title: getTransferParams
	 * @Description: 获取转账参数	TODO 待确认 
	 */
	private TransferParams getTransferParams() {
		
		TransferParams transferParams = new TransferParams();
		transferParams.setCCstTr_ID(CcbTestConstant.CASH_TREE_NO);//现金客户树编号 13!an CMN+10位顺序号，客户通过签约唯一确认，固定
		transferParams.setCCstTrNdID(CcbTestConstant.CASH_TREE_NODE_NO);//现金客户树节点编号   25!an ND+18位客户编号+5位顺序号，客户通过签约唯一确认，固定
		transferParams.setCst_ID(CcbTestConstant.CUSTOMER_NO);//客户编号  8!n,客户通过签约 唯一确认，固定
		transferParams.setCstPty_Py_Jrnl_No(this.getCustomNo());//客户方支付流水号(暂定:与客户方交易流水号相同) 
		transferParams.setCstPty_TxnSrlNo(this.getCustomNo());//客户方交易流水号  DF数据格式：an..60
		transferParams.setPyr_AccNm(CcbTestConstant.TEST_ACCOUNT_2_COMPANY_NAME);//付款方账户名称
		transferParams.setPyr_BkCgyCd("01");//付款方行别代码 （01本行02国内他行03国外他行）DF数据格式：2!n
		transferParams.setPyr_Cst_AccNo(CcbTestConstant.TEST_ACCOUNT_2);//付款方客户账号
		transferParams.setRcvPtAc_Nm(CcbTestConstant.TEST_ACCOUNT_1_COMPANY_NAME);//收款方账户名称
		transferParams.setRcvPrt_BkCgyCd("01");//收款方行别代码 （01本行 02国内他行 03国外他行）DF数据格式：2!n
		transferParams.setRcvPrt_Cst_AccNo(CcbTestConstant.TEST_ACCOUNT_1);//收款方客户账号
		transferParams.setRqs_Amt("0.12");//请求金额
		transferParams.setRmrk("测试转账功能");//备注
		return transferParams;
		
	}
	
	/**
	 * @Title: getCustomNo
	 * @Description: 获取自定义流水号
	 * @return 
	 */
	private String getCustomNo(){
		return this.getCCBConfigParm(InterfaceConstant.YYYY_MM_DD_);//自定义流水号，格式：yyyymmdd...
	}
	
	/**
	 * @Title: downloadScsSaveDataToFile
	 * @Description: 自CCB下载批量代扣处理结果文件保存的本地路径
	 * @param record
	 * @param data
	 * @return 
	 */
	private String downloadScsSaveDataToFile(CcbBatchWithholdRecord record, String data) {
		String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
		String filePath = this.getPath();//根据操作系统类型获取上传文件目录
		filePath = filePath + File.separator + "withhold-result-txt"+ File.separator + record.getFileType() + File.separator;
		
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
	private void updateWithholdFileRecord(Long id, String filePath) {
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setId(id);
		record.setSummaryFile(filePath);
		int rows = ccbBatchWithholdRecordService.updateByPrimaryKeySelective(record);
		if(rows>0) {
			logger.info("更新数据库成功，自CCB下载后的批量代扣结果文件保存到本地后的路径："+filePath);
		}
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 根据CCB处理批量代扣文件结果更新本地保存的批量处理文件相关信息
	 * @param state 
	 */
	private void updateWithholdFileRecord(ResponseCertState state) {
		
		Example example = new Example(CcbBatchWithholdRecord.class);
		example.createCriteria().andEqualTo("fileSn", state.getTxn_SN());
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setCcbVchrId(state.getVchID());
		record.setStatus(EnumCcbBatchStatus.APPLY_PROCESS.getValue());
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
	}
	
	/**
	 * @Title: updateWithholdFileRecord
	 * @Description: 根据CCB处理批量代扣文件结果更新本地保存的批量处理文件相关信息
	 * @param state 
	 */
	private void updateWithholdFileRecord(String fileSn, String ccbSummaryFiel) {
		
		Example example = new Example(CcbBatchWithholdRecord.class);
		example.createCriteria().andEqualTo("fileSn", fileSn);
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setCcbSummaryFiel(ccbSummaryFiel);
		int rows = ccbBatchWithholdRecordService.updateByExampleSelective(record, example);
		if(rows>0) {
			logger.info("更新批量代扣记录成功："+fileSn+"---"+record);
		}else {
			logger.info("更新批量代扣记录失败："+fileSn+"---"+record);
		}
	}
	
	/**
	 * @Title: getHearderParams
	 * @Description: 获取hearder参数 
	 * @return 
	 */
	private HeaderParams getHearderParams() {
		
		String chanl_cust_no = this.getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);//电子银行合约编号
		String txn_itt_ip_adr = this.getCCBConfigParm(InterfaceConstant.CCB_LOCAL_HOST_IP);
		String txn_stff_id = CcbTestConstant.OPERATOR_NO;//交易员编号（操作员编号）
		
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
	 * @Title: getFileListPack
	 * @Description: 获取申请处理批量代扣文件列表
	 * @param withholdFile	批量代扣本地文件名称
	 * @param ccbVchrFile
	 * @param txn_SN
	 * @return
	 * @throws Exception 
	 */
	private FileInfoList getFileListPack(String withholdFile,String ccbVchrFile, String txn_SN) throws Exception {
		
		//byte[] fileContent = CCBFileUtil.readToByte(withholdFile);
		//logger.info("批量代扣文件内容："+fileContent);
		//String msg_Smy = MD5withRSA.encryptionMd5(fileContent);
		String fileContent = CCBFileUtil.readToString(withholdFile, "GBK");
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
	 * 自CCB获取RSA PUB KEY数据.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	private String getRsaPubKeyTransPost()
			throws ServletException, IOException {
		// KeyFileUtil.test_savePublicKeyToFile();

		// 请求参数名称常量
		final String KEY_CHANL_CUST_NO = "chanl_cust_no";
		final String KEY_TYPE = "type";

		// 请求参数值常量
		// 密钥类型
		final String VALUE_TYPE_PUB = "pub"; // RSA公钥
		// final String VALUE_TYPE_DES="des"; //DES密钥

		final int NO_ENCRYPT_LEN = 6; // 未加密的数据长度
		final String RESPONSE_CODE_OK = "000000"; // 返回状态码-OK

		logger.info("----do post to bank get des key ----");

		// (1)自系统中获取配置参数
		// (1.1)获取配置参数:获取CCB DES密钥的url地址.
		String requestUrl = getCCBConfigParm(InterfaceConstant.CCB_KEY_URL);
		logger.info("--Read URL:" + requestUrl + "--");

		// (1.2)获取配置参数:客户的电子银行合约编号
		String chan_cust_no = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);
		logger.info("--Read chan_cust_no:" + chan_cust_no + "--");

		// (2)通过httpClient发送参数
		// (2.1)设置请求参数
		Map<String, String> parms = new HashMap<>();
		parms.put(KEY_CHANL_CUST_NO, chan_cust_no);
		parms.put(KEY_TYPE, VALUE_TYPE_PUB);

		// (2.1)发送请求
		logger.info("--send http post  request --");
		byte[] respBytes = HttpClientUtil.doPostRespBytes(requestUrl, parms);

		// 如果接收到数据
		if (respBytes != null) {

			// (3)开始对数据进行解析
			logger.info("--begin parse received data--");

			byte[] allData = respBytes;

			// 获取前面未加密部分
			byte[] codeByte = new byte[NO_ENCRYPT_LEN];
			System.arraycopy(allData, 0, codeByte, 0, NO_ENCRYPT_LEN);
			String code = new String(codeByte);

			// 获取后面加密部分
			byte[] dataByte = new byte[allData.length - NO_ENCRYPT_LEN];
			System.arraycopy(allData, NO_ENCRYPT_LEN, dataByte, 0, allData.length - NO_ENCRYPT_LEN);

			if (code.equalsIgnoreCase(RESPONSE_CODE_OK)) {
				logger.info("------Begin decrypt Data------");
				logger.info("--Read common key To decrypt--");
				// 获取配置:企业客户号
				//String customerNo = getCCBConfigParm(InterfaceConstant.CCB_COMPANY_CUSTOMER_NO);
				// String customerNo="1234567890";
				logger.info("--Read neuqpay_ccb_customkey :" + chan_cust_no + "--");
				String commonKey = genCommonKey(chan_cust_no);
				logger.info("--DES common key is :" + commonKey + "--");

				byte[] srcData = Des3Util.keyDecrypt(dataByte, commonKey); // 解决RSA PUBLIC KEY
				String keyBase64Str = Base64.encodeBase64String(srcData); // 对解密后的数据进行BASE64编码.

				if (StringUtils.isNotBlank(keyBase64Str)) {
					logger.info("--Begin Save Key Data in Base64 format--");
					logger.info("--Key Data Base64:" + keyBase64Str + "--");

					RSAPublicKey publicKey = null;

					// 根据RSA PUBLIC KEY Base64 String-----> RSA PUBLIC OBJECT对象
					try {
						publicKey = PemRSAEncrypt.getPublicKeyByBase64Str(keyBase64Str);
						logger.info(Base64.encodeBase64String(publicKey.getEncoded()));

					} catch (Exception e) {
						logger.info("Process Key Exception:", e);
					}

					// 保存CCB RSA PUBLIC KEY
					if (publicKey != null) {
						logger.info("--Save Key Data To File Start--");
						// 获取配置:自CCB获取的RSA公钥-密钥文件
						String keyFilePath = getCCBConfigParm(InterfaceConstant.CCB_RSA_PUBLIC_KEY_FILE_PATH_CCB);
						logger.info("---------key file Path:" + keyFilePath + "-----------");
						String uploadFolder = uploadFileConfig.getUploadFolder();
						String fullKeyFilePath = uploadFolder + keyFilePath;
						logger.info("---------full key file Path:" + fullKeyFilePath + "-----------");

						// save public object to file
						KeyFileUtil.savePublicKeyToFile(publicKey, fullKeyFilePath);

						logger.info("--Save rsa Key Data To File Ok--");
						return fullKeyFilePath;
					}else {
						return "RSA PUBLIC KEY IS NULL";
					}
				}else {
					return "对数据进行BASE64编码后为空";
				}
			}else {
				return "请求返回状态码："+code;
			}
			//response.getWriter().write("receive RSA key from ccb is Ok");
			//logger.info("receive RSA key from ccb is Ok");
		} else {
			logger.info("请求RSA密钥时未获取到数据");
			//response.getWriter().write("----no received data---");
			return "请求RSA密钥时未获取到数据";
		}
	}
	
	/**
	 * @Title: getCcbDesKeyTransPost
	 * @Description: 自建行获取DES密钥
	 * @return
	 * @throws ServletException
	 * @throws IOException 
	 */
	private String getCcbDesKeyTransPost() throws ServletException, IOException {
		//请求参数名称常量
		final String KEY_CHANL_CUST_NO="chanl_cust_no";
		final String KEY_TYPE="type";
		
		//请求参数值常量
		//密钥类型
		//final String VALUE_TYPE_PUB="pub";  //RSA公钥
		final String VALUE_TYPE_DES="des";  //DES密钥
		
		final int NO_ENCRYPT_LEN=6;  //未加密的数据长度
		final String RESPONSE_CODE_OK="000000";  //返回状态码-OK
		
		logger.info("----do post to bank get des key ----");
		
		//(1)自系统中获取配置参数
		//(1.1)获取配置参数:获取CCB DES密钥的url地址.
		String requestUrl = getCCBConfigParm(InterfaceConstant.CCB_KEY_URL);		
		logger.info("--Read URL:"+requestUrl+"--");
		
		//(1.2)获取配置参数:客户的电子银行合约编号
		String chan_cust_no = getCCBConfigParm(InterfaceConstant.CCB_EB_CONTRACT_NO);		
		logger.info("--Read chan_cust_no:"+chan_cust_no+"--");
		
		//(2)通过httpClient发送参数
		//(2.1)设置请求参数
		Map<String,String> parms=new HashMap<>();
		parms.put(KEY_CHANL_CUST_NO, chan_cust_no);
		parms.put(KEY_TYPE, VALUE_TYPE_DES);
		
		//(2.1)发送请求
		logger.info("--send http post  request --");
		byte[] respBytes= HttpClientUtil.doPostRespBytes(requestUrl, parms);
		
		//如果接收到数据
		if(respBytes!=null) {
		
			//(3)开始对数据进行解析
			logger.info("--begin parse received data--");
			
			byte[] allData = respBytes;
			
			//获取前面未加密部分
			byte[] codeByte = new byte[NO_ENCRYPT_LEN];
			System.arraycopy(allData, 0, codeByte, 0, NO_ENCRYPT_LEN);
			String code = new String(codeByte);
			
			//获取后面加密部分
			byte[] dataByte = new byte[allData.length-NO_ENCRYPT_LEN];
			System.arraycopy(allData, NO_ENCRYPT_LEN, dataByte, 0, allData.length-NO_ENCRYPT_LEN);

			
			if(code.equalsIgnoreCase(RESPONSE_CODE_OK)){
				logger.info("------Begin decrypt Data------");
				logger.info("--Read common key To decrypt--");
				//获取配置:企业客户号				
				//String customerNo = getCCBConfigParm(InterfaceConstant.CCB_COMPANY_CUSTOMER_NO); 
				// String customerNo="1234567890";
				logger.info("--Read neuqpay_ccb_customkey :" + chan_cust_no + "--");
				String commonKey = genCommonKey(chan_cust_no);
				logger.info("--DES common key is :" + commonKey + "--");				
				
				byte[] srcData = Des3Util.keyDecrypt(dataByte,commonKey);
				String desKeyBase64Str = Base64.encodeBase64String(srcData);
				
				if(StringUtils.isNotBlank(desKeyBase64Str)) {					
					logger.info("--Begin Save Key Data in Base64 format--");					
					saveCCBDesKey(desKeyBase64Str);					
					logger.info("--Key Data Base64:"+desKeyBase64Str+"--");	
					logger.info("--Save Key Data in Base64 format Ok--");
					return "DES KEY:"+desKeyBase64Str;
				}else {
					return "DES KEY IS NULL";
				}
			}else {
				return "请求返回状态码："+code;
			}
			//response.getWriter().write("receive des key Ok");
			//logger.info("receive des key Ok");
		}
		else {
			logger.info("请求DES密钥时未获取到数据");
			//response.getWriter().write("----no received data---");
			return "请求DES密钥时未获取到数据";
		}	
		
	}
	
	
	/**
	 * 保存CCB DES KEY到数据库
	 * @param desKey
	 */
	private int saveCCBDesKey(String desKey) {
		Example example=new Example(SysInterfaceConfig.class);
		example.createCriteria()
				.andEqualTo("key", InterfaceConstant.CCB_DES_KEY)
				.andEqualTo("interfaceType", InterfaceConstant.INTERFACE_TYPE_CCB);	
		
		SysInterfaceConfig config = new SysInterfaceConfig();		
		config.setValue(desKey);
		int rows=interfaceConfigService.updateByExampleSelective(config, example);
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
	
	
	
	
	
	//----------以下内容未用----------
	

	@RequestMapping(value = "/create-batch-withhold-file", produces = "application/json")
	@ResponseBody
	public Object testCreateBatchWithholdFile(String period, String locationCode) {
		
		try {
			
			String type = CcbBusinessUtils.getDateYYYYMMDD();//获取类型
			
			// TODO CommonKey
			// TODO DesKey：前端增加按钮获取
			// TODO Local RSA
			//linux或windows通用文件目录
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			// TODO CCB RSA public key：前端页面增加按钮获取
			
			List<CcbBatchWithholdRecord> recordList = new ArrayList<CcbBatchWithholdRecord>();//保存创建上传文件后的记录，全部完成后保存到数据库
			
			List<CcbUploadFileRowBean> rowList = new ArrayList<>();
			for(int i=0; i<100; i++) {
				CcbUploadFileRowBean row = new CcbUploadFileRowBean();
				row.setCardNo("6227000132100789567"+i);
				row.setAccountName("用户"+i);
				row.setTotalAmount(new BigDecimal(i));
				rowList.add(row);
			}
			
			String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
			String filePath = this.saveDataToFile(type, sn, rowList);//保存文件
			CcbBatchWithholdRecord record =  this.getCcbBatchWithholdRecord(type, sn, filePath);//创建记录实体
			recordList.add(record);
			
			int rows = ccbBatchWithholdRecordService.saveList(recordList);
			if (rows > 0) {
				return RequestResultUtil.getResultSuccess("创建批量代扣文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("创建批量代扣文件异常，请检查日志！");
	}
	
	/**
	 * @Title: createBatchWithholdFile
	 * @Description: 创建批量代扣文件
	 * @param period
	 * @param locationCode
	 * @return 
	 */
	//@RequestMapping(value = "/create-batch-withhold-file", produces = "application/json")
	//@ResponseBody
	public Object createBatchWithholdFile(String period, String locationCode) {
		
		try {
			
			// TODO CommonKey
			// TODO DesKey：前端增加按钮获取
			// TODO Local RSA
			//linux或windows通用文件目录
			String uploadFolder = uploadFileConfig.getUploadFolder();
			String publicKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PUBLIC_KEY_FILE_PATH);
			String privateKeyFilePath = uploadFolder+this.getCCBConfigParm(InterfaceConstant.LOCAL_RSA_PRIVATE_KEY_FILE_PATH);
			this.generatorRsaKeyPairToFile(publicKeyFilePath, privateKeyFilePath);
			// TODO CCB RSA public key：前端页面增加按钮获取
			
			List<CcbBatchWithholdRecord> recordList = new ArrayList<CcbBatchWithholdRecord>();//保存创建上传文件后的记录，全部完成后保存到数据库
			
			String type = CcbBusinessUtils.getDateYYYYMMDD();//获取类型
			
			int pageNum = 1;
			int pageSize = 2000;
			PageInfo<Map<String, Object>> pageInfo = this.getFileDataPager(pageNum, pageSize, period, locationCode);//获取第一页内容
			
			CcbBatchWithholdRecord record = this.getCcbBatchWithholdRecord(type, pageInfo.getList());//获取创建上传文件后的记录，全部完成后需要保存到数据库
			recordList.add(record);
			
			int pages = pageInfo.getPages();//总页数
			if(pages>1) {
				for(int i=2; i<=pages; i++) {
					pageInfo = this.getFileDataPager(pageNum, pageSize, period, locationCode);//获取除第一页内容外的其他页内容
					record = this.getCcbBatchWithholdRecord(type, pageInfo.getList());//获取创建上传文件后的记录，全部完成后需要保存到数据库
					recordList.add(record);
				}
			}
			
			int rows = ccbBatchWithholdRecordService.saveList(recordList);
			if (rows > 0) {
				return RequestResultUtil.getResultSuccess("创建批量代扣文件成功！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return RequestResultUtil.getResultFail("创建批量代扣文件异常，请检查日志！");
	}

	
	
	/**
	 * @Title: getCcbBatchWithholdRecord
	 * @Description: 获取CCB批量代扣记录实体类
	 * @param accountItemMapList
	 * @return 
	 */
	private CcbBatchWithholdRecord getCcbBatchWithholdRecord(String type, List<Map<String, Object>> accountItemMapList) {
		
		String sn = CcbBusinessUtils.getSn();//获取批量代扣文件SN（唯一）
		
		List<CcbUploadFileRowBean> rowList = this.getUploadFileRowList(accountItemMapList);//获取上传文件内容
		String filePath = this.saveDataToFile(type, sn, rowList);
		return this.getCcbBatchWithholdRecord(type, sn, filePath);
	}
	
	/**
	 * @Title: CcbBatchWithholdRecord
	 * @Description: 获取CCB批量代扣记录实体类
	 * @param type
	 * @param sn
	 * @param filePath
	 * @return 
	 */
	private CcbBatchWithholdRecord getCcbBatchWithholdRecord(String type, String sn, String filePath) {
		
		CcbBatchWithholdRecord record = new CcbBatchWithholdRecord();
		record.setFileType(type);
		record.setFileSn(sn);
		record.setWithholdFile(filePath);
		logger.info("保存CCB批量代扣记录："+record);
		return record;
	}
	
	/**
	 * @Title: saveDataToFile
	 * @Description: 保存到文件，并返回文件路径
	 * @param type
	 * @param rowList
	 * @return 
	 */
	private String saveDataToFile(String type, String sn, List<CcbUploadFileRowBean> rowList) {
		String filePath = this.getPath();//根据操作系统类型获取上传文件目录
		filePath = filePath + File.separator + "withhold txt" + File.separator + type + File.separator;
		
		String fileName = sn+".txt";
		
		String lineSeparator = CCBFileUtil.lineSeparator();//回车换行符号
		
		StringBuffer sb = new StringBuffer();
		sb.append(lineSeparator);

//		int size = rowList.size();
//		for(int i=0; i<size; i++) {
//			
//			CcbUploadFileRowBean row = rowList.get(i);
//			row.setSn(i+1);
//			
//			String rowContent = row.getFileRowContent();
//			
//			sb.append(rowContent);
//			
//			if(i!=(size-1)) {
//				sb.append(lineSeparator);
//			}
//		}
//		CCBFileUtil.saveDataToFile(filePath, fileName, sb.toString(), "GBK");
//		logger.info("创建上传文件的目录："+filePath+fileName);
		
		
		try {
			
			String withholdAmount = this.getCCBConfigParm(InterfaceConstant.WITHHOLD_AMOUNT);
			
			String gbkStr = lineSeparator;
			gbkStr = gbkStr + "1|4340610130352336|杨三八|"+withholdAmount+"|0|||安心付测试1|测试一分钱";
			
			//String xx=new String("sss","GBK");
			
			//String content = new String(gbkStr.getBytes(), "GBK");
			CCBFileUtil.saveDataToFile(filePath, fileName, gbkStr, "GBK");
			logger.info("创建上传文件的目录："+filePath+fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return filePath+fileName;
	}
	
	/**
	 * @Title: getFileDataPager
	 * @Description: 获取上传文件分页数据
	 * @param pageNum
	 * @param pageSize
	 * @param period
	 * @param locationCode
	 * @return 
	 */
	private PageInfo<Map<String, Object>> getFileDataPager(Integer pageNum, Integer pageSize, String period, String locationCode) {
		
		Integer FILE_PAGE_SIZE = 2000;
		// 判断当前登录用户角色，并获取登录用户ID，为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
		Long operatorId = this.getOperatorId();

		// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = FILE_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> accountItemMapList = customerAccountItemService.searchCustomerAccountItem(period, locationCode, null, null, null, operatorId, null , null);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(accountItemMapList);// (使用了拦截器或是AOP进行查询的再次处理)
		logger.info("批量代扣的总记录数："+pageInfo.getTotal()+" 共 "+pageInfo.getPages()+" 页 当前处理的是第 "+pageInfo.getPageNum()+" 页。");

		return pageInfo;
	}
	
	/**
	 * @Title: getUploadFileRowList
	 * @Description: 获取上传文件内容
	 * @param accountItemMapList
	 * @return 
	 */
	private List<CcbUploadFileRowBean> getUploadFileRowList(List<Map<String, Object>> accountItemMapList){
		
		List<CcbUploadFileRowBean> rowList = new ArrayList<>();//保存上传文件每行的内容
		
		for (Map<String, Object> accountItemMap : accountItemMapList) {
			BigDecimal zero = new BigDecimal("0");//初始化BigDecimal类型的0
			
			String customerIdStr = accountItemMap.get("CUSTOMER_ID").toString();//客户ID
			Long customerId = Long.valueOf(customerIdStr);//客户ID
			String periodTemp = accountItemMap.get("PERIOD").toString();//期间
			String accountDateStr = accountItemMap.get("ACCOUNT_DATE").toString();//记账日期
			
			//计算违约金总金额
			//BigDecimal overdueValue = customerAccountItemService.getOverdueValueSum(customerId,periodTemp);
			//计算欠费金额（账单欠费金额+违约金欠费金额）
			BigDecimal owedAmountValue = customerAccountItemService.getCurrBillOwedAmount(customerId);
			
			Customers customer = customersService.selectByPrimaryKey(customerId);//客户信息
			
			Integer deductType = customer.getDeductType();//扣费方式 1=其他；2=建行自动扣费；3=民生银行自动扣费
			
			if(deductType!=null && deductType==EnumDeductType.CCB.getValue()) {
				CustomerBanks bank = this.getCustomerBank(customerId);//获取客户-银行信息
				if(bank!=null) {
					String cardNo = bank.getCardNo();//卡号
					String accountName = bank.getAccountName();//开户名
					if(StringUtils.isNotBlank(cardNo) && StringUtils.isNotBlank(accountName)) {
						CcbUploadFileRowBean row = new CcbUploadFileRowBean();
						row.setCardNo(cardNo);
						row.setAccountName(accountName);
						row.setTotalAmount(owedAmountValue);
						rowList.add(row);
					}
				}
			}
		}
		return rowList;
	}
	
	/**
	 * @Title: getCustomerBank
	 * @Description: 获取客户-银行信息
	 * @param customerId
	 * @return 
	 */
	private CustomerBanks getCustomerBank(Long customerId) {
		Example example = new Example(CustomerBanks.class);
		example.createCriteria().andEqualTo("customerId", customerId).andEqualTo("enabled", EnumEnabledStatus.ENABLED_NO.getValue());
		example.setOrderByClause(" ID DESC");
		List<CustomerBanks> bankList = bankService.selectByExample(example);
		if(bankList!=null && bankList.size()>0) {
			CustomerBanks respBank = null;
			for(CustomerBanks tempBank : bankList) {
				String accountName = tempBank.getAccountName();//开户名
				String cardNo = tempBank.getCardNo();//卡号
				if(StringUtils.isNotBlank(accountName) && StringUtils.isNotBlank(cardNo)) {
					respBank = tempBank;
					break;
				}
			}
			return respBank;
		}
		return null;
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
	
	/**
	 * @Title: generatorRsaKeyPairToFile
	 * @Description: 自动生成RSA钥匙对并保存到对应文件
	 * @throws NoSuchAlgorithmException
	 * @throws FileNotFoundException
	 * @throws IOException 
	 */
	private void generatorRsaKeyPairToFile(String publicKeyFilePath, String privateKeyFilePath) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
		KeyPair keyPair = MD5withRSA.getKeyPair(); // 获取钥匙对
		KeyFileUtil.saveRsaKeyPairToFile(keyPair, publicKeyFilePath, privateKeyFilePath);
	}
	
	/**
	 * @Title: getOperatorId
	 * @Description: 根据角色获取当前用户ID
	 * @return 为null时是管理员角色，查询所有；不为null时是抄表员角色，只查询此抄表员生成的账单
	 */
	private Long getOperatorId() {
		UserBean userBean = (UserBean) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long operatorId = null;// 操作员ID
		if (userBean != null) {
			List<String> roleCodeList = new ArrayList<>();
			List<SysRoles> roleList = userBean.getRoleList();
			for (SysRoles role : roleList) {
				roleCodeList.add(role.getRoleCode());
			}

			if (roleCodeList.toString().indexOf(RoleCodeConstant.ROLE_CODE_METER_READER) != -1) {
				operatorId = userBean.getId();// 操作员ID
			}

		}
		return operatorId;
	}
	
	public static void main(String[] args) {
		System.out.println(UUID.randomUUID().toString().toUpperCase().replaceAll("-+", ""));
		String path = "d:\\upload\\file\\abc.txt";
		System.out.println(path);
		System.out.println(File.separator);
		System.out.println(path.substring(0, path.lastIndexOf(File.separator)+1));
		System.out.println(path.substring(path.lastIndexOf(File.separator)+1));
		String abc="sLBeniPTT4+X2YoNhXNFRrCwXp4j00+P";  
        try {
			System.out.println(new String(Base64.decodeBase64(abc), "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
