package com.learnbind.ai.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.TestCompareResultBean;
import com.learnbind.ai.bean.TestMeterAddrBean;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.ccb.fileutil.CCBFileUtil;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.excelutil.Common;
import com.learnbind.ai.common.util.excelutil.ReadEgMeterExcel;
import com.learnbind.ai.common.util.excelutil.ReadLocalMeterExcel;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.common.ziputil.FileTypeEnum;
import com.learnbind.ai.common.ziputil.UnPackeUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.service.addsubwater.AddSubWaterService;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.location.LocationCustomerService;
import com.learnbind.ai.service.location.LocationService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;

@Controller
@RequestMapping(value = "/test")
public class UploadTestController {

	private static final Log log = LogFactory.getLog(UploadTestController.class);
	
	@Autowired
	private UploadFileConfig uploadFileConfig;//文件配置
	@Autowired
	private CustomerAccountItemService customerAccountItemService;//账目
	@Autowired
	private CustomersService customersService;
	@Autowired
	private LocationService locationService;
	@Autowired
	private LocationCustomerService locationCustomerService;
	@Autowired
	private WechatCustomerService wechatCustomerService;
	@Autowired
	private AddSubWaterService addSubWaterService;
	@Autowired
	private PartitionWaterService partitionWaterService;
	
	/**
	 * @Title: starter
	 * @Description: 进入test页面
	 * @param request
	 * @param response
	 * @return 
	 */
	@RequestMapping(value = "/starter")
	public String starter(HttpServletRequest request, HttpServletResponse response) {
		
		//登录用户
		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
//		//----------------------------大表销账-------------------------------------------------------------------------------------
//		Customers searchCustomerObj = new Customers();
//		searchCustomerObj.setCustomerType(EnumCustomerType.CUSTOMER_UNIT.getValue());
//		searchCustomerObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		//结算如园三号楼
//		//List<Customers> customerList = customersService.getCustomersList(null,"47815-58291", null , null);
//		List<Customers> customerList = customersService.select(searchCustomerObj);
//		int count = 0;
//		for(Customers customer : customerList) {
//			count = count+1;
//			log.debug("----------结算客户 【"+customer.getCustomerName()+"】。");
//			List<CustomerAccountItem> oweBillList = customerAccountItemService.getWaterFeeOweBillList(customer.getId(), "2019-12");
//			int billCount = 0;
//			for(CustomerAccountItem oweBill : oweBillList) {
//				billCount = billCount+1;
//				log.debug("----------结算客户账单 "+oweBill.getBaseWaterFee()+"，"+oweBill.getSewageWaterFee());
////				BigDecimal oweAmount = BigDecimalUtils.subtract(oweBill.getCreditAmount(), oweBill.getDebitAmount());
////				customerAccountItemService.settleAccount(oweBill.getId(), userBean.getId(), oweAmount, 
////						 EnumAiDebitSubjectAction.PAY_WATER_FEE, EnumAiDebitSubjectPayment.PAYMENT_CASH, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
////				//获取基础水费欠费金额
//				BigDecimal baseOweAmount = customerAccountItemService.getBaseFeeOweAmount(oweBill.getDebitAssistant(), oweBill.getBaseWaterFee());
//				if(BigDecimalUtils.greaterThan(baseOweAmount, new BigDecimal(0.00))) {
//					//结算基础水费
//					customerAccountItemService.settleAccount(oweBill.getId(), userBean.getId(), baseOweAmount, 
//							 EnumAiDebitSubjectAction.PAY_BASE_FEE, EnumAiDebitSubjectPayment.PAYMENT_CASH, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
//				}
//				
//				//获取污水水费欠费金额
//				BigDecimal sewageOweAmount = customerAccountItemService.getSewageFeeOweAmount(oweBill.getDebitAssistant(), oweBill.getSewageWaterFee());
//				if(BigDecimalUtils.greaterThan(baseOweAmount, new BigDecimal(0.00))) {
//					//结算污水水费
//					customerAccountItemService.settleAccount(oweBill.getId(), userBean.getId(), sewageOweAmount, 
//							 EnumAiDebitSubjectAction.PAY_TREATMENT_FEE, EnumAiDebitSubjectPayment.PAYMENT_CASH, EnumPaymentType.PAYMENT_RECHARGE.getKey(), EnumAiTraceOperate.RECHARGE_SETTLEMENT.getKey());
//				}
//				
//			}
//			log.debug("----------共结算客户账单 "+billCount+"个。");
//		}
//		log.debug("----------共结算客户 "+count+"个。");
		
//		//----------------------------大表销账删除-------------------------------------------------------------------------------------
//		Customers searchCustomerObj = new Customers();
//		searchCustomerObj.setCustomerType(EnumCustomerType.CUSTOMER_UNIT.getValue());
//		searchCustomerObj.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
//		//结算如园三号楼
//		//List<Customers> customerList = customersService.getCustomersList(null,"47815-58291", null , null);
//		List<Customers> customerList = customersService.select(searchCustomerObj);
//		int count = 0;
//		for(Customers customer : customerList) {
//			count = count+1;
//			log.debug("----------结算客户 【"+customer.getCustomerName()+"】。");
//			
//			Example example = new Example(CustomerAccountItem.class);
//			example.createCriteria().andEqualTo("period", "2019-12").andEqualTo("customerId", customer.getId()).andCondition(" DEBIT_AMOUNT!=0");
//			List<CustomerAccountItem> billList = customerAccountItemService.selectByExample(example);//
//			int billCount = 0;
//			for(CustomerAccountItem bill : billList) {
//				billCount = billCount+1;
//				log.debug("----------删除借方辅助核算的客户账单 "+bill.getBaseWaterFee()+"，"+bill.getSewageWaterFee());
//				
//				String assistant = bill.getDebitAssistant();
//				List<AssistantBean> beanList = JSON.parseArray(assistant, AssistantBean.class);
//				if(beanList!=null && beanList.size()>0) {
//					for(AssistantBean bean : beanList) {
//						Long billId = bean.getId();
//						CustomerAccountItem updateObj = new CustomerAccountItem();
//						updateObj.setId(billId);
//						updateObj.setDeleted(EnumDeletedStatus.DELETE_YES.getValue());
//						customerAccountItemService.updateByPrimaryKeySelective(updateObj);
//					}
//				}
//				
//				
//				CustomerAccountItem updateObj = new CustomerAccountItem();
//				updateObj.setId(bill.getId());
//				updateObj.setDebitAssistant("");
//				updateObj.setDebitAmount(new BigDecimal(0.00));
//				
//				int rows = customerAccountItemService.updateByPrimaryKeySelective(updateObj);
//				if(rows>0) {
//					billCount = billCount+1;
//				}
//				
//			}
//			log.debug("----------共结算客户账单 "+billCount+"个。");
//		}
//		log.debug("----------共结算客户 "+count+"个。");
		
		//--------------------初始化客户编号--------------------------------------------------------------------------------------------------------
//		63532	建行接口测试	47815-63532	GXQ-JXJKCS		001
//		58287	红石别墅		47815-58287	GXQ-HSBS		200
//		58288	红石原著高层	47815-58288	GXQ-HSYZGC		201
//		58289	南庄新村		47815-58289	GXQ-NZXC		141
//		58290	如园小区		47815-58290	GXQ-RYXQ		210
//		58291	如园3号楼		47815-58291	GXQ-RY3HL		211
//		58292	天地荣域		47815-58292	GXQ-TDRY		220
//		63539	邵豪伟组		47815-63539	GXQ-SHWZ		011
//		63541	范云鹏组		47815-63541	GXQ-FYPZ		022
//		47816	紫林湾		47815-47816	GXQ-ZLW			110
//		47817	月珑湾		47815-47817	GXQ-YLW			120
//		47818	雍雅锦江一期	47815-47818	GXQ-YYJJYQ		130
//		47819	雍雅锦江二期	47815-47819	GXQ-YYJJEQ		131
//		47820	南庄旧楼		47815-47820	GXQ-NZJL		140
//		47821	主语城一期		47815-47821	GXQ-ZYCYQ		150
//		47822	主语城二期		47815-47822	GXQ-ZYCEQ		151
//		47823	熙湖澜岸		47815-47823	GXQ-XHLA		160
//		47824	熙湖二期		47815-47824	GXQ-XHEQ		161
//		47825	国宾壹号		47815-47825	GXQ-GBYH		170
//		47826	长久AB区		47815-47826	GXQ-ZJABQ		180
//		47827	长久C区		47815-47827	GXQ-ZJCQ		181
//		47828	北庄小区		47815-47828	GXQ-BZXQ		190	
//		63540	樊永永组		47815-63540	GXQ-FYYZ		033
//		67050	茉莉公馆		47815-67050	GXQ-MLGG		270
//		63589	刘旭组		47815-63589	GXQ-LXZ			044
//		63602	金色家园		47815-63602	GXQ-JSJY		230
//		64717	花样年华		47815-64717	GXQ-HYNH		240
//		66807	茉莉公馆10号楼	47815-66807	GXQ-MLGG10HL	271
//		65968	熙湖二期高层	47815-65968	GXQ-XHEQGC		162
//		65870	花样年华商户	47815-65870	GXQ-HYNHSH		241
//		66658	盛宏嘉苑		47815-66658	GXQ-SHJY		250
//		63603	金色家园商户	47815-63603	GXQ-JSJYSH		231
		
//		List<Location> locationList = locationService.getBlockListByPid(47815l);
//		int size = 0;
//		int totalCount = 0;
//		for(Location location : locationList) {
//			//更新地理位置code值为小区编码
////			Long id = location.getId();
////			String code = this.getCode(id);
////			Location temp = new Location();
////			temp.setId(id);
////			temp.setCode(code);
////			int rows = locationService.updateByPrimaryKeySelective(temp);
////			if(rows<=0) {
////				log.debug("地理位置ID："+id+"，更新code失败。。。");
////			}
//			
//			String code = location.getCode();
//			String traceIds = location.getTraceIds();
//			
//			
//			//locationCustomerService.get
//			List<Customers> customerList = customersService.getCustomersList(null, traceIds, null, null);
//			int count = 0;
//			for(Customers customer : customerList) {
//				Integer customerType = customer.getCustomerType();
//				if(customerType==null) {
//					continue;
//				}
//				String customerCode = "";
//				boolean isInsert = true;
//				while (isInsert) {
//					customerCode = this.getCustomerCode(customerType+code);
//					boolean flag = this.isInsert(customerCode);
//					if(flag) {
//						break;
//					}
//				}
//				
//				Customers updateObj = new Customers();
//				updateObj.setId(customer.getId());
//				updateObj.setCustomerCode(customerCode);
//				int rows = customersService.updateByPrimaryKeySelective(updateObj);
//				if(rows<=0) {
//					log.debug("更新客户编码错误，客户ID："+customer.getId());
//				}else {
//					count+=1;
//				}
//			}
//			log.debug(traceIds+"共个"+customerList.size()+"客户，更新客户编码"+count+"个。");
//			size=size+customerList.size();
//			totalCount = totalCount+count;
//		}
//		log.debug("共个"+size+"客户，更新客户编码"+totalCount+"个。");
		
		//------------------更新微信-客户关系中的客户编码---------------------------------------------------------------------------------
//		List<WechatCustomer> wcList = wechatCustomerService.selectAll();
//		for(WechatCustomer wc : wcList) {
//			Long customerId = wc.getCustomerId();
//			Customers customer = customersService.selectByPrimaryKey(customerId);
//			WechatCustomer updateObj = new WechatCustomer();
//			updateObj.setId(wc.getId());
//			updateObj.setCustomerCode(customer.getCustomerCode());
//			wechatCustomerService.updateByPrimaryKeySelective(updateObj);
//		}
		//更新追加减免水量日志表信息
//		List<AddSubWater> waterList = addSubWaterService.selectAll();
//		for(AddSubWater water : waterList) {
//			//获取分水量信息
//			Example example = new Example(PartitionWater.class);
//			example.createCriteria().andEqualTo("customerId", water.getCustomerId()).andEqualTo("recordId", water.getMeterRecordId()).andEqualTo("deleted", EnumDeletedStatus.DELETE_NO.getValue());
//			List<PartitionWater> pwaterList = partitionWaterService.selectByExample(example);
//			if(pwaterList.size() > 0) {
//				PartitionWater temp = pwaterList.get(0);
//				water.setPartitionWaterId(temp.getId());
//				addSubWaterService.updateByPrimaryKeySelective(water);
//			}
//			
//		}
		
		//--------------------更新国宾壹号高层客户编号--------------------------------------------------------------------------------------------------------
		
//		Location location = locationService.selectByPrimaryKey(id);
//
//		String code = location.getCode();
//		String traceIds = location.getTraceIds();
//		
//		
//		//locationCustomerService.get
//		List<Customers> customerList = customersService.getCustomersList(null, traceIds, null, null);
//		int count = 0;
//		for(Customers customer : customerList) {
//			Integer customerType = customer.getCustomerType();
//			if(customerType==null) {
//				continue;
//			}
//			String customerCode = "";
//			boolean isInsert = true;
//			while (isInsert) {
//				customerCode = this.getCustomerCode(customerType+code);
//				boolean flag = this.isInsert(customerCode);
//				if(flag) {
//					break;
//				}
//			}
//			
//			Customers updateObj = new Customers();
//			updateObj.setId(customer.getId());
//			updateObj.setCustomerCode(customerCode);
//			int rows = customersService.updateByPrimaryKeySelective(updateObj);
//			if(rows<=0) {
//				log.debug("更新客户编码错误，客户ID："+customer.getId());
//			}else {
//				count+=1;
//			}
//		}
//		log.debug(traceIds+"共个"+customerList.size()+"客户，更新客户编码"+count+"个。");
		
		return "test";
	}
	
	private String getCustomerCode(String code) {
		//生成5位随机数
		String random = getRandom();
		//拼接客户编码（小区编码+5位随机数）
		String customerCode = code+random;
		return customerCode;
	}
	private boolean isInsert(String customerCode) {
		
		
		//验证客户编码是否相同，不同时更新，相同时重新生成随机数
		
		Customers searchObj = new Customers();
		searchObj.setCustomerCode(customerCode);
		int count = customersService.selectCount(searchObj);
		if(count>0) {//重新生成随机数
			return false;
		}else {//更新客户编码
			return true;
		}
	}
	
	/**
	 * @Title: getRandom
	 * @Description: 获取5位随机数
	 * @return 
	 */
	private static String getRandom() {
		int random = (int)((Math.random()*9+1)*10000);
		return String.valueOf(random);
	}
	/**
	 * @Title: getCode
	 * @Description: 获取编码
	 * @param id
	 * @return 
	 */
	private String getCode(Long id) {
		String code = "";
		switch (id.toString()) {
		case "63532":
			code = "001";
			break;
		case "58287":
			code = "200";
			break;
		case "58288":
			code = "201";
			break;
		case "58289":
			code = "141";
			break;
		case "58290":
			code = "210";
			break;
		case "58291":
			code = "211";
			break;
		case "58292":
			code = "220";
			break;
		case "63539":
			code = "011";
			break;
		case "63541":
			code = "022";
			break;
		case "47816":
			code = "110";
			break;
		case "47817":
			code = "120";
			break;
		case "47818":
			code = "130";
			break;
		case "47819":
			code = "131";
			break;
		case "47820":
			code = "140";
			break;
		case "47821":
			code = "150";
			break;
		case "47822":
			code = "151";
			break;
		case "47823":
			code = "160";
			break;
		case "47824":
			code = "161";
			break;
		case "47825":
			code = "170";
			break;
		case "47826":
			code = "180";
			break;
		case "47827":
			code = "181";
			break;
		case "47828":
			code = "190";
			break;
		case "63540":
			code = "033";
			break;
		case "67050":
			code = "270";
			break;
		case "63589":
			code = "044";
			break;
		case "63602":
			code = "230";
			break;
		case "64717":
			code = "240";
			break;
		case "66807":
			code = "271";
			break;
		case "65968":
			code = "162";
			break;
		case "65870":
			code = "241";
			break;
		case "66658":
			code = "260";
			break;
		case "63603":
			code = "231";
			break;
		default:
			break;
		}
		return code;
	}
	
	/**
	 * @Title: upload
	 * @Description: 上传图片
	 * @param request
	 * @param model
	 * @return 
	 * 		返回上传文件的目录，可直接显示（http://localhost:8888/upload/img/20190524/2019052412223184678347.jpg）
	 */
	@RequestMapping(value = "/upload-img")
	@ResponseBody
	public Object uploadImg(HttpServletRequest request, Model model, String fileType, String inputName) {
		
		try {
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, uploadFileConfig.getWindowsUploadFolder(), fileType, inputName);
			String serverUrl = FileUploadUtil.getReqServerURL(request);
			for(String filePath : filePathList) {
				System.out.println(serverUrl+filePath);
			}
			Map<String, Object> respMap = RequestResultUtil.getResultUploadSuccess();
			respMap.put("filePathList", filePathList);
			return JSON.toJSONString(respMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
	}
	
	/**
	 * @Title: uploadZip
	 * @Description: 上传压缩文件
	 * @param request
	 * @param model
	 * @param fileType
	 * @param inputName 
	 */
	@RequestMapping(value = "/upload-zip")
	@ResponseBody
	public Object uploadZip(HttpServletRequest request, Model model, String fileType, String inputName) {
		
		// 创建一个通用的多部分解析器.  
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        // 设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8");  
        // 判断是否有文件上传  
        if (commonsMultipartResolver.isMultipart(request)) {//有文件上传  
        	// 转型为MultipartHttpRequest：
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    		/*Iterator<String> it = multipartRequest.getFileNames();
    		while (it.hasNext()) {
				String string = (String) it.next();
				System.out.println(string);
			}*/
    		
    		String path = uploadFileConfig.getUploadFolder();//上传文件目录
    		path = path+"zipfile"+File.separator;
    		// 获得文件：
    		List<MultipartFile> fileList = multipartRequest.getFiles(inputName);
    		for(MultipartFile file : fileList) {
    			this.unPack(file, path);
    		}
        }
		
        return JSON.toJSONString(RequestResultUtil.getResultUploadWarn());
		
	}
	
	/**
	 * @Title: unPack
	 * @Description: 解压压缩包
	 * @param zipFile
	 * @param path 
	 */
	private void unPack(MultipartFile zipFile, String path) {
		if (null == zipFile) {
            //return JSON.toJSONString(RequestResultUtil.getResultFail("没有压缩文件！"));
			log.error("没有压缩文件");
        }
        boolean isZipPack = true;
        
        String fileContentType = zipFile.getContentType();
        String originalFileName = zipFile.getOriginalFilename();
        String name = zipFile.getName();
        log.debug("originalFileName:"+originalFileName);
        log.debug("fileName:"+name);
        
        //将压缩包保存在指定路径
        String packFilePath = path + File.separator + zipFile.getName();
        if (FileTypeEnum.FILE_TYPE_ZIP.type.equals(fileContentType)) {
            //zip解压缩处理
            packFilePath += FileTypeEnum.FILE_TYPE_ZIP.fileStufix;
        } else if (FileTypeEnum.FILE_TYPE_RAR.type.equals(fileContentType)) {
            //rar解压缩处理
            packFilePath += FileTypeEnum.FILE_TYPE_RAR.fileStufix;
            isZipPack = false;
        } else {
            //return AjaxList.createFail("上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        	log.error("上传的压缩包格式不正确,仅支持rar和zip压缩文件!");
        }
        File file = new File(packFilePath);
        try {
            zipFile.transferTo(file);
        } catch (IOException e) {
            log.error("zip file save to " + packFilePath + " error", e);
            //return AjaxList.createFail("保存压缩文件到:" + packParam.getDestPath() + " 失败!");
        }
        if (isZipPack) {
            //zip压缩包
            UnPackeUtil.unPackZip(path, file, null);
        } else {
            //rar压缩包
            UnPackeUtil.unPackRar(path, file);
        }
        //return AjaxList.createSuccess("解压成功");
        log.debug("解压文件【"+packFilePath+"】成功");
	}
	
	//----------------------------表地址对比------------------------------------------------------------------------------------------------------------
	
	/**
	 * @Title: getNewSystemMeterAddrBeanList
	 * @Description: 获取新系统表地址集合
	 * @return 
	 */
	private static List<TestMeterAddrBean> getNewSystemMeterAddrBeanList(String localPath){
		//（1）读取营收系统导出的Excel文件
		//String localPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\2019-09-熙湖澜岸-20190924111136-水表数据.xls";
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put(Common.EXCEL_PATH, localPath);
		try {
			Map<String, Object> readResult = ReadLocalMeterExcel.readExcel(reqMap);
			String result_code = readResult.get("result_code").toString();
			if(result_code.equalsIgnoreCase("success")) {
				String resultDataJSON = readResult.get("result_data").toString();
				List<TestMeterAddrBean> meterList = JSON.parseArray(resultDataJSON, TestMeterAddrBean.class);
				for(TestMeterAddrBean bean : meterList) {
					String room = bean.getRoom();
					String meterAddr = bean.getMeterAddr();
					System.out.println(room+":"+meterAddr);
				}
				return meterList;
			}else {
				System.out.println("result code fail");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Title: getOldSystemMeterAddrBeanList
	 * @Description: 获取旧系统表地址集合
	 * @return 
	 */
	private static List<TestMeterAddrBean> getOldSystemMeterAddrBeanList(String egPath){
		//（1）读取营收系统导出的Excel文件
		//String egPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\EG-熙湖澜岸.xls";
		
		Map<String, Object> reqMap = new HashMap<String, Object>();
		reqMap.put(Common.EXCEL_PATH, egPath);
		try {
			Map<String, Object> readResult = ReadEgMeterExcel.readExcel(reqMap);
			String result_code = readResult.get("result_code").toString();
			if(result_code.equalsIgnoreCase("success")) {
				String resultDataJSON = readResult.get("result_data").toString();
				List<TestMeterAddrBean> meterList = JSON.parseArray(resultDataJSON, TestMeterAddrBean.class);
				for(TestMeterAddrBean bean : meterList) {
					String room = bean.getRoom();
					String meterAddr = bean.getMeterAddr();
					System.out.println(room+":"+meterAddr);
				}
				return meterList;
			}else {
				System.out.println("result code fail");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * @Title: verifyEgMeterMeterAddr
	 * @Description: 对比艺高采集器地址和表地址
	 */
	public static void verifyEgMeterMeterAddr() {
		
		List<TestCompareResultBean> resultBeanList = new ArrayList<>();
		
		String localPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\熙湖澜岸\\新系统表计数据-熙湖澜岸.xls";
		String egPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\熙湖澜岸\\旧系统表地址-熙湖澜岸.xls";
		
//		String localPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\熙湖二期\\新系统表计数据-熙湖二期.xls";
//		String egPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\熙湖二期\\旧系统表地址-熙湖二期.xls";
		
//		String localPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\北庄小区\\新系统表计数据-北庄小区.xls";
//		String egPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\北庄小区\\旧系统表地址-北庄小区.xls";
		
//		String localPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\长久AB区\\新系统表计数据-长久AB区.xls";
//		String egPath = "C:\\Users\\Administrator\\Desktop\\表地址对比EXCEL\\长久AB区\\旧系统表地址-长久AB区.xls";
		
		
		//新系统表地址
		List<TestMeterAddrBean> newSysBeanList = getNewSystemMeterAddrBeanList(localPath);
		//旧系统表地址
		List<TestMeterAddrBean> oldSysBeanList = getOldSystemMeterAddrBeanList(egPath);
		
		int counter = 0;
		
		for(TestMeterAddrBean newBean : newSysBeanList) {
			String newRoom = newBean.getRoom();
			String newMeterAddr = newBean.getMeterAddr();
			for(TestMeterAddrBean oldBean : oldSysBeanList) {
				String oldRoom = oldBean.getRoom();
				String oldMeterAddr = oldBean.getMeterAddr();
				if(newRoom.equalsIgnoreCase(oldRoom)) {
					
					//System.out.println("----------"+newRoom+":"+newMeterAddr+":"+oldMeterAddr);
					
					if(newMeterAddr.equalsIgnoreCase(oldMeterAddr)) {
						//System.out.println("----------验证通过-----"+newRoom+":"+newMeterAddr+":"+oldMeterAddr);
					}else {
						
						counter = counter+1;
						//System.out.println(counter+":----------验证未通过-----【"+newRoom+"】=【"+newMeterAddr+":"+oldMeterAddr+"】");
						System.out.println(newRoom+" "+newMeterAddr+" "+oldMeterAddr+"");
						TestCompareResultBean resultBean = new TestCompareResultBean();
						resultBean.setRoom(newRoom);
						resultBean.setNewMeterAddr(newMeterAddr);
						resultBean.setOldMeterAddr(oldMeterAddr);
						resultBeanList.add(resultBean);
					}
				}
			}
		}
		
		
		//（1）读取营收系统导出的Excel文件
		
		//（2）读取艺高系统导出的Excel文件
		//（3）对比并查找采集器地址和表地址不相同的数据
		//（4）把查找的结果导出到Excel
	}
	
	/**
	 * @Title: readTxt
	 * @Description: 读取txt文件内容
	 * @return 
	 */
	public static String readTxt() {
		
		String encoding = "GBK";//字符集
		String filePath = "D:\\test\\cusotmer_invoice_info.txt";
		
		String fileContent = CCBFileUtil.readToString(filePath, encoding);
		System.out.println("----------读取的文件内容："+fileContent);
		
		return fileContent;
	}
	
	/**
	 * @Title: getCustomerInvoiceInfo
	 * @Description: 获取客户开票信息
	 * @return 
	 */
	public static List<Map<String, String>> getCustomerInvoiceInfo() {
		
		//编码~~名称~~简码~~税号~~地址电话~~银行账号~~邮件地址~~备注~~身份证校验
		String[] title = {"code", "name", "code_1", "tax", "address", "bank_account", "email", "remark", "check_id"};
		
		String separator = CCBFileUtil.lineSeparator();//获取分割符号
		
		String invoiceInfo = readTxt();//读取txt文件内容获取客户发票信息
		
		List<Map<String, String>> mapList = new ArrayList<>();
		
		String[] rowArr = invoiceInfo.split(separator);
		for(int i=2; i<rowArr.length; i++) {
			String row = rowArr[i];
			System.out.println("----------客户开票信息："+row);
			String[] colArr = row.split("~~");
			Map<String, String> map = new HashMap<>();
			for(int n=0; n<colArr.length; n++) {
				map.put(title[n], colArr[n]);
			}
			mapList.add(map);
		}
		return mapList;
	}
	
	
	
	
	
	
	/**
	 * @Title: main
	 * @Description: main方法
	 * @param args 
	 */
	public static void main(String[] args) {
		//String path = "d://upload//img/20190524/2019052412273012427098.jpg";
		//String path = "http://localhost:8888/upload/img/20190524/2019052412223184678347.jpg";
		//System.out.println(path.substring(path.indexOf("upload")));
//		String path = "d://upload/";
//		System.out.println(path.substring(0, path.indexOf(":")+1));
//		
//		//String _s= Regex.Replace(_s, @"[\u4e00-\u9fa5]", ""); //去除汉字
//		
//		String str = "南庄新村1号楼1单元301";
//		String reg = "[\u4e00-\u9fa5]";
//		Pattern pat = Pattern.compile(reg);  
//		Matcher mat=pat.matcher(str); 
//		String repickStr = mat.replaceAll("-");
//		repickStr = repickStr.replaceFirst("\\-+", "").replaceAll("\\-+", "-");
//
//		System.out.println("去中文后:"+repickStr);
		
		//verifyEgMeterMeterAddr();//验证艺高水表表地址
		//readTxt();//读取txt文件内容
		//List<Map<String, String>> mapList = getCustomerInvoiceInfo();//获取客户开票信息
		//System.out.println("++++++++++"+mapList);
		//exportCustomersExcel(null, null);
		
		for(int i=0; i<100; i++) {
			String random = getRandom();
			System.out.println(random);
		}
		
	}
	
	
	//------------------------------	导出开票信息Excel	------------------------------
		/**
		 * @Title: exportMeterRecordExcel
		 * @Description: 导出开票信息Excel
		 * @param request
		 * @param response 
		 */
		public static void exportCustomersExcel(HttpServletRequest request, HttpServletResponse response) {
			//编码~~名称~~简码~~税号~~地址电话~~银行账号~~邮件地址~~备注~~身份证校验
			//excel标题
			String[] titles = { "编码", "名称", "简码", "税号", "地址电话", "银行账号", "邮件地址", "备注", "身份证校验"};
			//excel列名
			String[] columnNames = {"code", "name", "code_1", "tax", "address", "bank_account", "email", "remark", "check_id"};
			//sheet名
			String sheetName = "开票信息";
			List<Map<String, String>> excelDataList = getCustomerInvoiceInfo();
			//获取导出EXCEL的数据
			List<Map<String, Object>> excelList = new ArrayList<>();
			for(Map<String, String> excel : excelDataList) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("code", excel.get("code"));
				map.put("address", excel.get("address"));
				map.put("code_1", excel.get("code_1"));
				map.put("name", excel.get("name"));
				map.put("check_id", excel.get("check_id"));
				map.put("tax", excel.get("tax"));
				map.put("remark", excel.get("remark"));
				map.put("email", excel.get("email"));
				map.put("bank_account", excel.get("bank_account"));
				excelList.add(map);
			}
			//获取导出EXCEL的工作簿
			HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelList);
			//获取导出EXCEL的文件路径
			String realPath = "D:\\test\\";
			//获取导出EXCEL的文件名
			String fileName = "开票信息.xls";
			
			File file = new File(realPath+fileName);
			
			//文件输出流
		    FileOutputStream outStream = null;
			try {
				outStream = new FileOutputStream(file);
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		    try {
				wb.write(outStream);
				outStream.flush();
				outStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		    
		    System.out.println("导出文件成功！文件导出路径：--"+file);
		    
		    try {
				DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
}
