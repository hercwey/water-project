package com.learnbind.ai.controller.wechat;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.enumclass.EnumDeletedStatus;
import com.learnbind.ai.common.enumclass.EnumNotifyMode;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.CustomerAgreement;
import com.learnbind.ai.model.Customers;
import com.learnbind.ai.model.KnowLibrary;
import com.learnbind.ai.model.PartitionWater;
import com.learnbind.ai.model.WechatCustomer;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomersService;
import com.learnbind.ai.service.knowlibrary.KnowLibraryService;
import com.learnbind.ai.service.meterrecord.PartitionWaterService;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.service.WechatService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;
import com.learnbind.ai.service.wechatuser.WechatUserService;
import com.learnbind.ai.util.wx.SignUtil;
import com.learnbind.ai.util.wx.entity.Signature;

import tk.mybatis.mapper.util.StringUtil;

@Controller
@RequestMapping(value = "/wechat")
public class WeChatController {
	private static Log log = LogFactory.getLog(WeChatController.class);
	
	private static final String TEMPLATE_PATH = "wechat"; // 页面目录
	//private static final int PAGE_SIZE = 8; // 页大小
	
	/**
	 * 营收系统客户实体-服务
	 */
	@Autowired
	CustomersService customersService;
	/**
	 * 微信客户实体-服务
	 */
	@Autowired
	WechatUserService wechatUserService;
	/**
	 *  微信-客户关系  服务
	 */	
	 @Autowired 
	 WechatCustomerService wechatCustomerService;  
	/**
	 * 内容管理-服务
	 */
	@Autowired 
	KnowLibraryService  knowLibraryService;
	/**
	 * 客户分水量-服务
	 */
	@Autowired
	PartitionWaterService partitionWaterService;
	
	/**
	 * 微信对象-服务
	 */
	@Autowired
	private WechatService wechatService;
	
	/**
	 * @Fields uploadFileConfig：//文件上传配置信息
	 */
	@Autowired
	private UploadFileConfig uploadFileConfig;
	
	
	
	//----------------test this的使用-------------------------
	@RequestMapping(value = "/test")
	public String test(HttpServletRequest request, HttpServletResponse response) {
		return TEMPLATE_PATH + "/test/test.html"; 
	}
	@RequestMapping(value = "/loadpage")
	public String loadpage(HttpServletRequest request, HttpServletResponse response) {
		return TEMPLATE_PATH + "/test/loadpage.html"; 
	}
	//---------------------------微信验证------------------------	
	/**
	 * @Title: valid
	 * @Description: 微信验证
	 * @param request
	 * @param response 
	 */
	@RequestMapping(value = "/valid")
	public void valid(HttpServletRequest request, HttpServletResponse response) {
		
		final String token = "wsd";
		
		// 微信加密签名
        String signature = request.getParameter("signature");
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        // 随机数
        String nonce = request.getParameter("nonce");
        // 随机字符串
        String echostr = request.getParameter("echostr");

		try {
			PrintWriter out = response.getWriter();
			
			Signature sign = new Signature();
	        sign.setSignature(signature);
	        sign.setTimestamp(timestamp);
	        sign.setNonce(nonce);
	        sign.setEchostr(echostr);
	        
	        //通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
	        if (SignUtil.checkSignature(token, sign)) {
	            System.out.print("echostr=" + echostr);
	            out.print(echostr);
	        }
	        out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	//---------------------------用户绑定------------------------
	/**
	 * 用户关联主界面(绑定,查询,解除绑定)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/customer")
	public String customer(Model model) {
		return TEMPLATE_PATH + "/customer/customer";
	}
	
	/**
	 * 加载用户绑定页面
	 * 以下参数为自用户编号查询界面获取到的数据
	 * @param telNo	用户电话
	 * @param customerName 用户姓名
	 * @param customerCode	客户编号  (customer_code)
	 * @param model
	 * @return  用户绑定页面
	 */
	@RequestMapping(value = "/bindcustomer")
	public String bindCustomer(String telNo,String customerName,String customerCode, Model model) {
		//return TEMPLATE_PATH + "/bind_customer/bind_customer";
		//System.out.println("bindcustomer-----customerCode:"+customerCode);
		
		model.addAttribute("telNo", telNo);
		model.addAttribute("customerName", customerName);
		model.addAttribute("customerCode", customerCode);
		
		return TEMPLATE_PATH + "/bind_customer/bind_customer_weui";
	}
	
	/**
	 * 绑定用户
	 * @param customerNo 用户编号
	 * @param telNo		  电话号码
	 * @param customerName 用户姓名
	 * @param model
	 * @return   返回绑定结果.
	 */
	@RequestMapping(value = "/bind")
	@ResponseBody
	public Object bind(String customerCode,String telNo,String customerName,  Model model,HttpServletRequest request) {
		//request parameters		
		/*
		 * System.out.println("customerCode:"+customerCode);
		 * System.out.println("telNo:"+telNo);
		 * System.out.println("customerName:"+customerName);
		 */
		//TODO add code here, bind customer
		//add code here, bind customer
		
		//(1)自Session获取微信用户
		WechatUser  wechatUser=(WechatUser)request.getSession().getAttribute(SessionConstant.USER);
		String openId=wechatUser.getOpenid();
		
		//(2)根据custuomerCode查询
		//业务约束:一套房只允许一个微信公众号进行绑定.但一个微信公众号可以绑定多套房子
		List<WechatCustomer> bindRelation =searchBindRelationByCustomerCode(customerCode);
		if(bindRelation.size()>0)
		{
			//return RequestResultUtil.getResultSuccess("已经绑定");
			return RequestResultUtil.getResultFail("已经绑定,无法再次绑定!");
		}
		else {
			//(3)开始绑定
			//(3.1)根据openId查询微信用户信息
			WechatUser wechatUserBind=searchWechatUser(openId);
			//(3.2)根据customerCode查询运营系统端客户信息
			Customers customerBind=searchCustomer(customerCode);
			if(customerBind!=null) {
				//(3.3)建立微信用户与营业系统客户关系.
				bindWechatUserAndCustomer(wechatUserBind,customerBind,telNo);
				//(3.4)返回状态
				return RequestResultUtil.getResultSuccess("绑定成功");
			}
			else
			{
				return RequestResultUtil.getResultFail("无此用户信息,无法绑定");
			}
			
		}
		
		
//		
//		List<WechatCustomer> bindRelation =searchBindRelation(openId);
//		if(bindRelation.size()>0)
//		{
//			//return RequestResultUtil.getResultSuccess("已经绑定");
//			return RequestResultUtil.getResultFail("已经绑定");
//		}
//		else {
//			//(3)开始绑定
//			//(3.1)根据openId查询微信用户信息
//			WechatUser wechatUserBind=searchWechatUser(openId);
//			//(3.2)根据customerCode查询运营系统端客户信息
//			Customers customerBind=searchCustomer(customerCode);
//			if(customerBind!=null) {
//				//(3.3)建立微信用户与营业系统客户关系.
//				bindWechatUserAndCustomer(wechatUserBind,customerBind,telNo);
//				//(3.4)返回状态
//				return RequestResultUtil.getResultSuccess("绑定成功");
//			}
//			else
//			{
//				return RequestResultUtil.getResultFail("无此用户");
//			}
//			
//			
//		}
	}
	
	//按客户CODE查询绑定关系.
	private List<WechatCustomer> searchBindRelationByCustomerCode(String customerCode){
		WechatCustomer searchObj=new  WechatCustomer();
		searchObj.setCustomerCode(customerCode);
		return wechatCustomerService.select(searchObj);
	}
	
	/**
	 * 绑定[微信用户]及[营业系统客户]
	 * @param wechatUser	微信用户
	 * @param customer		用水客户
	 * @param telNo			绑定时使用的电话号码
	 */
	private void bindWechatUserAndCustomer(WechatUser wechatUser,Customers customer,String telNo) {
		WechatCustomer insertObj=new WechatCustomer();
		insertObj.setCustomerCode(customer.getCustomerCode());
		insertObj.setBindDate(new Date());		
		insertObj.setCustomerId(customer.getId());
		insertObj.setMobile(telNo);
		insertObj.setOpenid(wechatUser.getOpenid());
		insertObj.setWechatId(wechatUser.getId());
		insertObj.setIdCardNo("");  //暂时未设置
		
		wechatCustomerService.insertSelective(insertObj);
	}
	
	/**
	 *   根据客户的openId查询绑定关系.
	 * @param openId  微信用户openId
	 * @return
	 */
	private List<WechatCustomer> searchBindRelation(String openId) {
		WechatCustomer searchObj=new WechatCustomer();
		searchObj.setOpenid(openId);
		return wechatCustomerService.select(searchObj);
	}
	
	
	
	/**
	 *  根据用户编号查询营业客户对象
	 * @param customerCode  客户编号
	 * @return  营业系统中客户对象
	 */
	private Customers searchCustomer(String customerCode) {
		Customers searchObj=new Customers();
		searchObj.setCustomerCode(customerCode);
		return customersService.selectOne(searchObj);
	}
	
	/**
	 * 根据openId查询微信用户对象
	 * @param openId
	 * @return
	 */
	private WechatUser searchWechatUser(String openId) {
		WechatUser searchObj=new WechatUser();
		searchObj.setOpenid(openId);
		return wechatUserService.selectOne(searchObj);
	}
	
	/**
	 * 绑定成功
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/bindsuccess")
	public String bindSuccess(Model model) {
		return TEMPLATE_PATH + "/bind_customer/bind_customer_success";
	}
	
	/**
	 * 绑定失败
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/binderror")
	public String bindError(Model model) {
		return TEMPLATE_PATH + "/bind_customer/bind_customer_error";
	}
	
	//---------------解除绑定-----------------
	
	/**
	 *  加载解除绑定页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/unbindcustomer")
	public String unbindCustomer(HttpServletRequest request,Model model) {
		//(1) 查询绑定
		//(1.1)自Session获取微信用户
		WechatUser  wechatUser=(WechatUser)request.getSession().getAttribute(SessionConstant.USER);
		String openId=wechatUser.getOpenid();
		//(1.2)查询绑定关系
		List<Map<String,Object>> bindedCustomerList=searchBindedCustomerList(openId);
		
		model.addAttribute("bindedCustomerList", bindedCustomerList);			
		
		return TEMPLATE_PATH + "/unbind_customer/unbind_customer";
	}
	
	/**
	 *  根据微信号查询己绑定的信息列表
	 * @param openId  微信号
	 * @return  绑定的信息列表,其中的元素结构如下:
	  			 customerCode,
				 address,
				 room,
				 relationId
	 */
	private List<Map<String,Object>> searchBindedCustomerList(String openId) {
		
		List<Map<String,Object>> resultList=new ArrayList<>();
		
		List<WechatCustomer> relationList=this.searchBindRelation(openId);	
		System.out.println("relationList size():"+ relationList.size());
		for(WechatCustomer wechatCustomer:relationList) {
			
			//生成列表对象
			Map<String,Object> tempMap=new HashMap<>();  
			  
			//Long wechatId=wechatCustomer.getWechatId();
			Long relationId=wechatCustomer.getId();
			Long customerId=wechatCustomer.getCustomerId();
			
			Customers customer=customersService.selectByPrimaryKey(customerId);
			
			//列表对象赋值
			tempMap.put("customerCode",customer.getCustomerCode());
			tempMap.put("address", customer.getAddress());
			tempMap.put("room", customer.getRoom());
			tempMap.put("relationId", relationId);
			
			resultList.add(tempMap);
		}
		
		return resultList;
	}
	
	/**
	 * 解绑
	 * @param relationId  绑定关系ID
	 * @param model
	 * @return  
	 */
	@RequestMapping(value = "/unbind")
	@ResponseBody
	public Object unbind(Long relationId,Model model) {
		System.out.println("解除绑定的关系ID:"+relationId);
		unbindWechatCustomer(relationId);
		return RequestResultUtil.getResultSuccess("解绑成功");		
	}
	
	/**
	 * 解绑
	 * @param relationId
	 */
	private int unbindWechatCustomer(Long relationId) {
		return wechatCustomerService.deleteByPrimaryKey(relationId);
	}
	
	/**
	 * 加载解绑对话框
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/loadunbindconfirmdialog")
	public String loadUnbindConfirmDialog(Model model) {
		return TEMPLATE_PATH + "/unbind_customer/unbind_confirm_dialog";
	}
	
	
	//-------------------用户编码(编号)查询-----------------------
	
	/**
	 * 加载查询用户编号界面
	 * 查询用户编号(此编号用于绑定用户)
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/searchcustomerno")
	public String searchCustomerNo(Model model) {
		//return TEMPLATE_PATH + "/search_customer_no/search_customer_no";
		return TEMPLATE_PATH + "/search_customer_no/search_customer_no_weui";
	}
	
	/**
	 * 根据电话号码及用户名查询用户编号
	 * @param telNo  电话号码
	 * @param customerName  用户编号
	 * @param model
	 * @return  用户编号列表
	 */
	@RequestMapping(value = "/customernolist")
	public String customerNoList(String telNo,String customerName,Model model) {
		//return TEMPLATE_PATH + "/search_customer_no/customer_no_list";
		//search customer list in db by telno and customerName
		//add code here.
		//request parameters
		System.out.println("telNo:"+telNo);
		System.out.println("customerName:"+customerName);
		
		List<Customers> customerList=searchCustomer(telNo,customerName);
		model.addAttribute("customerList",customerList);		
		
		return TEMPLATE_PATH + "/search_customer_no/customer_no_list_weui";
	}
	
	/**
	 * 根据客户手机号码及姓名查询用户编号
	 * @param telNo   手机号码
	 * @param customerName  用户姓名
	 * @return  如果查询到则返回列表且个数大于等于1,否则返回长度为0的列表
	 */
	private List<Customers> searchCustomer(String telNo,String customerName){
		//final int DELETED_NO=0;
		//final int DELETED_YES=1;
		
		return customersService.searchCustomerNo(telNo, customerName);
		
	}
	
	/**
	 * 未找到用户编号时的页面.  此页面已经不再使用
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/notfoundcustomerno")
	public String notFoundCustomerNo(Model model) {
		return TEMPLATE_PATH + "/search_customer_no/not_found";
	}
	
	//---------------------通知方式------------------	
	/**
	 * 加载通知方式页面
	 * @param model
	 * @return  通知方式页面
	 * @throws IOException 
	 */
	@RequestMapping(value = "/notifymode")
	public String notifyMode(Model model ,HttpServletRequest request,HttpServletResponse response) throws IOException {		
		//(1)根据用户的openId
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		
		//如果微信用户尚未绑定,则重定向到用户绑定页面
		if(bindRelation.size()<=0) {
			response.sendRedirect("/wechat/customer");
		}
		else {
			//查询客户的绑定方式,返回前台
			long customerId=bindRelation.get(0).getCustomerId();  //取其中一个客户的通知方式
			int notifyMode=getCustomerNotifyMode(customerId);
			model.addAttribute("notifyMode", notifyMode);
		}
		
		return TEMPLATE_PATH + "/notifymode/notify_mode_weui";
	}
	
	/**
	 * 取得客户的通知方式
	 * @param customerId  客户ID
	 * @return  通知方式
	 */
	private int getCustomerNotifyMode(long customerId) {
		Customers customer=customersService.selectByPrimaryKey(customerId);		
		return customer.getNotifyMode();
	}
	
	 
	
	/**
	 * 设置通知方式
	 * @param weChatMode  	微信通知方式  1:打开  0:关闭
	 * @param smsMode		短信通知方式  1:打开  0:关闭
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/setnotifymode")
	@ResponseBody
	public Object setNotifyMode(Integer weChatMode,Integer smsMode, Model model,HttpServletRequest request) {
		//request parameters.
		/*
		 * System.out.println("weChatMode:"+weChatMode);
		 * System.out.println("smsMode:"+smsMode);
		 */
		
		//modify notify mode of customer
		//(1)根据用户设置的状态判定通知状态值
		int notifyMode=getNotifyModeValue(weChatMode,smsMode);
		//(2)更新用户通知模式.(如果一个微信号绑定了多个客户时,同时修改)
		//(2.1)根据微信号的openId查询已经绑定的客户关系列表.		
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		//(2.3)根据绑定关系中的客户所对应的通知方式
		modifiyNotifyMode(notifyMode,bindRelation);
		//(2.4)返回更新状态
		return RequestResultUtil.getResultSuccess("设置成功!");
	}
	
	/**
	 * 修改绑定关系中客户的通知方式
	 * @param notifyMode
	 * @param bindRelation
	 */
	private void modifiyNotifyMode(int notifyMode,List<WechatCustomer> bindRelation) {
		//迭代更新通知模式
		for(WechatCustomer wechatCustomer:bindRelation) {
			Long customerId=wechatCustomer.getCustomerId();
			
			Customers customer=new Customers();
			customer.setId(customerId);
			customer.setNotifyMode(notifyMode);
			
			customersService.updateByPrimaryKeySelective(customer);
		}		
	}
	
	
	
	/**
	 * 获取session用户的openId
	 * @param request
	 * @return
	 */
	private String getOpenIdFromSession(HttpServletRequest request) {
		WechatUser  wechatUser=(WechatUser)request.getSession().getAttribute(SessionConstant.USER);
		String openId=wechatUser.getOpenid();
		return openId;
	}
	
	/**
	 * 根据用户所设置的通知状态值--->通知模式值
	 * @param weChatMode 微信通知方式(0,1)
	 * @param smsMode	   短信通知方式(0,1)
	 * @return
	 */
	private int getNotifyModeValue(int weChatMode,int smsMode) {
		final int OPEN=1;
		final int CLOSE=0;
		
		int notifyMode=0;
		
		switch(weChatMode) {
		case OPEN:
			if(smsMode==OPEN)  
				notifyMode=EnumNotifyMode.NOTIFY_ALL.getValue();  //全部方式
			else  
				notifyMode=EnumNotifyMode.NOTIFY_WECHAT.getValue();  //微信方式
			break;
		case CLOSE:
			if(smsMode==OPEN) 
				notifyMode=EnumNotifyMode.NOTIFY_SMS.getValue();  //短信方式
			else
				notifyMode=EnumNotifyMode.NOTIFY_NO.getValue();  //不通知
			break;
		}
		
		return notifyMode;
	}
	
	//----------------文章列表-----------------
	/**
	 * 文章列表页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/articlelist")
	public String articleList(String articleType,Model model) {
		//request parameters
		//System.out.println("articleType:"+articleType);
		log.debug("articleType:"+articleType);
		
		List<KnowLibrary> articleList=getArticleListByType(articleType);
		model.addAttribute("articleList",articleList);
		
		//回传文章类型参数,用于前台显示不同的title
		model.addAttribute("articleType", articleType);
		
		return TEMPLATE_PATH + "/workflow/article_list_weui";
	}
	
	/**
	 * 根据文章类型获取文章列表
	 * @param articleType  文件类型
	 * @return  文章列表
	 */
	private List<KnowLibrary>  getArticleListByType(String articleType){
		return knowLibraryService.searchArticlebyType(articleType);
	}
	
	/**
	 * 文章详情页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/article")
	public String article(Long articleId,Model model) {
		//request parameters
		//System.out.println("articleId:"+articleId);
		log.debug("文章ID---articleId:"+articleId);
		
		
		KnowLibrary article=getArticle(articleId);
		model.addAttribute("article", article);
		
		return TEMPLATE_PATH + "/workflow/article_weui";
	}
	
	/**
	 * 根据文章ID读取文章
	 * @param articleId  文章ID
	 * @return  文章对象
	 */
	private KnowLibrary getArticle(Long articleId) {
		return knowLibraryService.selectByPrimaryKey(articleId);
	}
	
	//-----------------用水分析-------------------
	/**
	 * 用水分析页面(获取用户水量消费情况信息)
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/analysis")
	public String analysis(Model model,HttpServletRequest request,HttpServletResponse response) throws IOException {
		//(1)根据用户的openId
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		
		//如果微信用户尚未绑定,则重定向到用户绑定页面
		if(bindRelation.size()<=0) {
			response.sendRedirect("/wechat/customer");
		}
		else {
			//查询客户的用水量消费情况,返回前台			
			List<Map<String,Object>> waterConsumptionList= getWaterConsumption(bindRelation);
			model.addAttribute("waterConsumptionList",waterConsumptionList);
		}	
		
		return TEMPLATE_PATH + "/analysis/analysis_weui";
	}
	
	/**
	 * 查询客户的用水量
	 * @param bindRelation  微信与客户关系列表
	 * @return  客户的用水量消费列表.
	 * 
	 * 对象结构如下:
	 * 		[
	 * 			{
	 * 				location:"xxxx"
	 * 				partList:[{
	 * 							genre:xxxx
	 * 							sold:yyyy
	 * 						  }]
	 * 			}
	 * 		]
	 */
	private List<Map<String,Object>> getWaterConsumption(List<WechatCustomer> bindRelation){
		
		List<Map<String,Object>> consumptionList=new ArrayList<>();
		
		//查询客户的用水量(可能多个客户),而后传送到前台
		for(WechatCustomer relation:bindRelation) {
			Map<String,Object> custMap=new HashMap<>();
			
			//查询客户的用水量.最近6个月的用水量信息
			Long customerId=relation.getCustomerId();  //客户ID			
			//TODO 需要改进			
			List<Map<String,Object>> partList=  partitionWaterService.getRecentlySixMonthAmount(customerId);
			//TODO 需要改进.
			List<Map<String,Object>> amountList=processChartData(partList);  //最近6个月的水表数据			
			
			//查询客户以获取地理位置信息
			Customers customer=customersService.selectByPrimaryKey(customerId);
			
			custMap.put("location", customer.getAddress()+customer.getRoom());
			custMap.put("partList", amountList);
			
			consumptionList.add(custMap);
		}
		
		return consumptionList;
	}
	
	/**
	 * @Title: processChartData
	 * @Description: 将某个用户的数据处理为前台需要的格式
	 * @param partList  用水量数据(按期间,来自于分水量表)
	 * @return 前台需要的格式
	 */
	private List<Map<String,Object>> processChartData(List<Map<String,Object>> partList){
		List<Map<String,Object>> amountList=new ArrayList<>();
		for(int i=partList.size()-1;i>=0;i--) {
			Map<String,Object> part=partList.get(i);
			Map<String,Object> waterMap=new HashMap<>();
			waterMap.put("genre", part.get("PERIOD"));				//期间
			waterMap.put("sold", part.get("WATER_AMOUNT"));			//用水量
			amountList.add(waterMap);
		}
		return amountList;
	}
	
	static final String REDIRECT_URL="/wechat/customer";
	
	//----------------水费帐单----------------
	/**
	 *  加载水费帐单主页(主要为客户信息)
	 * @param model
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping(value = "/bill")
	public String bill(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		
		//(1)根据用户的openId
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		
		//如果微信用户尚未绑定,则重定向到用户绑定页面
		if(bindRelation.size()<=0) {
			response.sendRedirect(REDIRECT_URL);
		}
		else {
			//TODO ADD CODE HERE
			List<Map<String,Object>> customerList=getCustomersInfo(bindRelation);
			model.addAttribute("customerList", customerList);
			
		}
		return TEMPLATE_PATH + "/bill/bill_weui";
	}
	
	/**
	 *   加载水费帐单
	 * @param customerIds  客户ID列表
	 * @param request
	 * @param response
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/getbill")
	                       
	public String loadbill(@RequestParam(value = "customerIds[]", required = false,defaultValue="") List<Long> customerIds,							
							Model model) {
		
		log.debug("list customersIds size is:"+customerIds.size());
		List<Map<String,Object>>  billList= searchCustomerBill(customerIds);
		model.addAttribute("billList", billList);
		return TEMPLATE_PATH + "/bill/customer_bill";
	}
	
	
	
	/**
	 * 客户账目-服务
	 */
	@Autowired
	CustomerAccountItemService customerAccountItemService;
	
	/**
	 * 获取客户信息
	 * @param bindRelation  微信与客户绑定关系列表  (微信---N:1---客户)
	 * @return 客户信息列表 
	 * 	客户信息列表结构:
	 * 			[
	 * 				{
	 * 					location:xxxxx,
	 * 					customerCode:xxxx,
	 * 					customerId:xxxx
	 * 				},
	 * 				{}
	 * 			]
	 */
	private List<Map<String,Object>> getCustomersInfo(List<WechatCustomer> bindRelation){
		final RoundingMode roundMode=RoundingMode.HALF_UP;
		final int precision=2;  //
		
		List<Map<String,Object>> customerList=new ArrayList<>();		
		for(WechatCustomer wechatCustomer:bindRelation) {
			Map<String,Object> tempMap=new HashMap<String,Object>();
			
			//(1)查询客户
			Long customerId=wechatCustomer.getCustomerId();
			System.out.println("customerId is :"+customerId);
			Customers customer=customersService.selectByPrimaryKey(customerId);
			//(2)查询客户余额
			BigDecimal customerBalance=customerAccountItemService.getCustomerBalance(customerId);
			
			customerBalance=customerBalance.setScale(precision, roundMode);  //精度限定为2,采用4舍5入.
			
			tempMap.put("location", customer.getAddress()+customer.getRoom());  //客户位置
			tempMap.put("customerCode", customer.getCustomerCode());			//客户编号
			tempMap.put("customerId", customer.getId());						//客户ID
			tempMap.put("balance", customerBalance);							//客户余额
			
			customerList.add(tempMap);
			
		}	
		
		return customerList;
	}
	
	
	/**
	 * 查询用户帐单
	 * @param bindRelation
	 * @return  用户帐单列表  结构如下:
	 * 	[
	 * 		{
	 * 			location:address+room
	 * 			customerCode:123456789
	 * 			customerId:xxxxx
	 * 			bills:[
	 * 					{
	 * 						上期抄见:
	 * 						上期抄表时间:
	 * 						本期抄见:
	 * 						本期抄表时间:
	 * 						水量:
	 * 						水价:
	 * 						金额(CREDIT_AMOUNT):
	 * 						违约金:
	 * 						合计:
	 * 					}	
	 * 				]	
	 * 		}
	 *  ]
	 * 
	 */
	private List<Map<String,Object>> searchCustomerBill(List<Long> customerIds){
		List<Map<String,Object>> billList=new ArrayList<>();		
		for(Long customerId:customerIds) {
			Map<String,Object> tempMap=new HashMap<String,Object>();
			//Long customerId=wechatCustomer.getCustomerId();
			System.out.println("customerId is :"+customerId);
			//Long customerId=Long.parseLong(customerIdStr);
			Customers customer=customersService.selectByPrimaryKey(customerId);
			
			tempMap.put("location", customer.getAddress()+customer.getRoom());  //客户位置
			tempMap.put("customerCode", customer.getCustomerCode());			//客户编号
			tempMap.put("customerId", customer.getId());						//客户ID
			
			List<Map<String,Object>> customerBillList=customerAccountItemService.getCustomerBillList(customerId);  //账单
			
			tempMap.put("bills", customerBillList);
			
			billList.add(tempMap);
			
		}	
		
		return billList;
	}
	
	
	
	/**
	 * 支付页
	 * @param model
	 * @return
	 */
//	@RequestMapping(value = "/pay")
//	public String pay(Model model) {
//		return TEMPLATE_PATH + "/bill/pay_weui";
//	}
	
	//---------------查询缴费记录--------------
	
	/**
	 * 加载查询缴费记录主页
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/fee")
	public String feeRecord(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		//(1)根据用户的openId
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		
		List<Map<String,Object>>  customerList=new ArrayList<>();
		//如果微信用户尚未绑定,则重定向到用户绑定页面
		if(bindRelation.size()<=0) {
			response.sendRedirect("/wechat/customer");
		}
		else {
			customerList=getCustomersInfo(bindRelation);
		}
		model.addAttribute("customerList", customerList);
		return TEMPLATE_PATH + "/fee/fee_record_main_weui";		
	}
	
	/**
	 * 查询缴费记录
	 * @param model
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/searchfee")
	public String searchFeeList(@RequestParam  Map<String,Object> parms,Model model) throws IOException {
		log.debug("parms's endDate is:"+parms.get("endDate"));
		//log.debug("idArr is:"+parms.get("idArr").toString());
		
		//JSONArray jsonArr=JSON.parseArray((String)parms.get("idArr"));
		List<Long> idList= JSON.parseArray((String)parms.get("idArr"), Long.class);
		String startDate=(String)parms.get("startDate");
		String endDate=(String)parms.get("endDate");
		
		List<Map<String,Object>> feeList =new ArrayList<>();
		if(idList!=null && idList.size()>0) {		
			feeList=searchCustomerFee(idList,startDate,endDate);
		}
		model.addAttribute("feeList", feeList);
		return TEMPLATE_PATH + "/fee/fee_list";
	}
	
	
	
	
	/**
	 * 查询客户缴费记录
	 * @param customerIds
	 * @return
	 */
	private List<Map<String,Object>> searchCustomerFee(List<Long> customerIds,String startDate,String endDate){
		List<Map<String,Object>> resultList=new ArrayList<>();		
		for(Long customerId:customerIds) {
			Map<String,Object> tempMap=new HashMap<String,Object>();
			
			log.debug("customerId is :"+customerId);
			Customers customer=customersService.selectByPrimaryKey(customerId);
			
			tempMap.put("location", customer.getAddress()+customer.getRoom());  //客户位置
			tempMap.put("customerCode", customer.getCustomerCode());			//客户编号
			tempMap.put("customerId", customer.getId());						//客户ID
			
			List<Map<String,Object>> customerBillList=customerAccountItemService.getCustomerFeeList(customerId,startDate,endDate);  //账单
			
			tempMap.put("fees", customerBillList);
			
			resultList.add(tempMap);
			
		}	
		
		return resultList;
	}
	
	//------------------------水表报修-----------------------
	/**
	 * @Title: loadWaterRepairMain
	 * @Description: 加载水表报修主页
	 * @param model
	 * @return 水表报修主页
	 */
	@RequestMapping(value = "/meterrepair")
	public String loadWaterRepairMain(HttpServletRequest request,Model model){
		WechatUser wechatUser = (WechatUser) request.getSession().getAttribute(SessionConstant.USER);
		Wechat wechat = wechatService.getWechat();//微信信息
		
		//String url = request.getRequestURL().toString();
		
		String url="";
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url = request.getRequestURL().toString() + "?" + request.getQueryString();
		}
		else {
			url = request.getRequestURL().toString();
		}
		
		//Map<String, String> jsMap = wechatService.getJsConfig(wechat, url);	
		
		//model.addAttribute("jsMap",jsMap);  //向前台传送参数.jsconfig参数
		
		return TEMPLATE_PATH + "/meterrepair/meter_repair_main";
	}
	
	
	
	/**
	 * @Title: loadWaterRepairBusinessPage
	 * @Description: 加载水表报修业务页面
	 * @param model
	 * @return 
	 */
//	@RequestMapping(value = "/meterrepairbusinesspage")
//	public String loadWaterRepairBusinessPage(HttpServletRequest request ,Model model){
//		
//		WechatUser wechatUser = (WechatUser) request.getSession().getAttribute(SessionConstant.USER);
//		Wechat wechat = wechatService.getWechat();//微信信息
//		
//		//String url = request.getRequestURL().toString();
//		
//		String url="";
//		if(StringUtils.isNotBlank(request.getQueryString())) {
//			url = request.getRequestURL().toString() + "?" + request.getQueryString();
//		}
//		else {
//			url = request.getRequestURL().toString();
//		}
//		
//		
//		//String url="http:///wechat/meterrepairbusinesspage";
//		Map<String, String> jsMap = wechatService.getJsConfig(wechat, url);	
//		
//		model.addAttribute("jsMap",jsMap);  //向前台传送参数.
//		
//		return TEMPLATE_PATH + "/meterrepair/meter_repair";
//	}
	
	
	
	/**
	 * @Title: commitmeterrepair
	 * @Description: 提交水表报修
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/commitmeterrepair")
	@ResponseBody
	public Object commitmeterrepair(HttpServletRequest request, Model model,@RequestParam Map<String,String> parms){
		try {
			String name=parms.get("name");
			//String telNo=parms.get("telno");
			log.debug("参数名称name的值为:"+name);
			
			String telNo=parms.get("telno");
			String address=parms.get("address");
			String longlat=parms.get("longlat");
			String description=parms.get("description"); 			
			
			String fileType=parms.get(("fileType"));
			String inputName=parms.get("inputName");		 
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();			
			//上传协议文件,如果前台有多选,此处只上传了第一个文件.
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("上传图片路径："+filePath);
				filePath = FileUploadUtil.subImgPath(filePathList.get(0));
				System.out.println("截取后上传图片路径："+filePath);
				//agreement.setPath(filePath);
			}
			
			//TODO 增加实体
			
//			String serverUrl = FileUploadUtil.getReqServerURL(request);
//			for(String filePath : filePathList) {
//				System.out.println(serverUrl+filePath);
//			}
			
			//保存客户-协议到数据库
			/*
			 * agreement.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			 * //agreement.setPath(filePathList.toString()); int rows =
			 * customerAgreementService.insertSelective(agreement); if(rows>0) { return
			 * JSON.toJSONString(RequestResultUtil.getResultUploadSuccess()); }
			 */
			
			//Map<String, Object> respMap = RequestResultUtil.getResultUploadSuccess();
			//respMap.put("filePathList", filePathList);
			//return JSON.toJSONString(respMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultSuccess("提交成功"));
	}
	
	
	
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	
	//------------------争议申请------------------
	/**
	 * @Title: dissentRequest
	 * @Description: 加载争议申请主页
	 * @param model
	 * @return 争议申请主页
	 */
	@RequestMapping(value = "/dissentrequest")
	public String dissentRequest(HttpServletRequest request,Model model){
		WechatUser wechatUser = (WechatUser) request.getSession().getAttribute(SessionConstant.USER);
		Wechat wechat = wechatService.getWechat();//微信信息
		
		//String url = request.getRequestURL().toString();
		
		String url="";
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url = request.getRequestURL().toString() + "?" + request.getQueryString();
		}
		else {
			url = request.getRequestURL().toString();
		}
		
		//Map<String, String> jsMap = wechatService.getJsConfig(wechat, url);	
		
		//model.addAttribute("jsMap",jsMap);  //向前台传送参数.jsconfig参数
		
		return TEMPLATE_PATH + "/dissentrequest/dissent_request_main";
	}
	
	/**
	 * @Title: commitDissentRequest
	 * @Description: 提交水表报修
	 * @param model
	 * @return 
	 */
	@RequestMapping(value = "/commitdissentrequest")
	@ResponseBody
	public Object commitDissentRequest(HttpServletRequest request, Model model,@RequestParam Map<String,String> parms){
		try {
			String name=parms.get("name");
			//String telNo=parms.get("telno");
			log.debug("参数名称name的值为:"+name);
			
			String telNo=parms.get("telno");
			String address=parms.get("address");
			String longlat=parms.get("longlat");
			String description=parms.get("description"); 			
			
			String fileType=parms.get(("fileType"));
			String inputName=parms.get("inputName");		 
			
			//根据操作系统类型获取上传文件目录
			String path = this.getPath();			
			//上传协议文件,如果前台有多选,此处只上传了第一个文件.
			List<String> filePathList = FileUploadUtil.getFiles2UploadPath(request, path, fileType, inputName);
			if(filePathList!=null && filePathList.size()>0) {
				String filePath = filePathList.get(0);
				System.out.println("上传图片路径："+filePath);
				filePath = FileUploadUtil.subImgPath(filePathList.get(0));
				System.out.println("截取后上传图片路径："+filePath);
				//agreement.setPath(filePath);
			}
			
			//TODO 增加实体
			
//			String serverUrl = FileUploadUtil.getReqServerURL(request);
//			for(String filePath : filePathList) {
//				System.out.println(serverUrl+filePath);
//			}
			
			//保存客户-协议到数据库
			/*
			 * agreement.setDeleted(EnumDeletedStatus.DELETE_NO.getValue());
			 * //agreement.setPath(filePathList.toString()); int rows =
			 * customerAgreementService.insertSelective(agreement); if(rows>0) { return
			 * JSON.toJSONString(RequestResultUtil.getResultUploadSuccess()); }
			 */
			
			//Map<String, Object> respMap = RequestResultUtil.getResultUploadSuccess();
			//respMap.put("filePathList", filePathList);
			//return JSON.toJSONString(respMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return JSON.toJSONString(RequestResultUtil.getResultSuccess("提交成功"));
	}
	
	//---------------预支付-------------------
	//预支付订单主页
	@RequestMapping("/pay/prepay")	
	public String prepay(HttpServletRequest request,HttpServletResponse response,Model model) throws IOException {
		//(1)根据用户的openId
		String openId=getOpenIdFromSession(request);		
		//(2.2)根据openId 查询此用户绑定关系
		List<WechatCustomer> bindRelation =searchBindRelation(openId);
		
		//如果微信用户尚未绑定,则重定向到用户绑定页面
		if(bindRelation.size()<=0) {
			response.sendRedirect(REDIRECT_URL);
		}
		else {
			//可能有多个客户(一个微信号对应多个客户)
			List<Map<String,Object>> customerList=getCustomersInfo(bindRelation);
			model.addAttribute("customerList", customerList);
			
		}
		return TEMPLATE_PATH+"/prepay/prepay_main";   //返回确认支付页面
	}
	
}
