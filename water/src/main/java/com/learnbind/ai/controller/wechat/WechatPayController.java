package com.learnbind.ai.controller.wechat;

import java.math.BigDecimal;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.ccbwechat.entity.CCBCallbackParam;
import com.learnbind.ai.ccbwechat.entity.PayParams;
import com.learnbind.ai.ccbwechat.order.CCBWechatOrderUtil;
import com.learnbind.ai.common.enumclass.EnumWeChatOrderType;
import com.learnbind.ai.common.util.BigDecimalUtils;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.WechatCustomer;
import com.learnbind.ai.model.WechatOrder;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.pay.EnumPayResultCode;
import com.learnbind.ai.service.wechat.pay.WechatOrderUtil;
import com.learnbind.ai.service.wechat.pay.WxPayConfigService;
import com.learnbind.ai.service.wechat.pay.WxPayNotifyService;
import com.learnbind.ai.service.wechat.service.WechatService;
import com.learnbind.ai.service.wechatuser.WechatCustomerService;
import com.learnbind.ai.service.wechatuser.WechatOrderService;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.controller.wechat
 *
 * @Title: WxPayController.java
 * @Description: 微信支付
 *
 * @author Administrator
 * @date 2019年8月6日 上午12:37:00
 * @version V1.0 
 *
 */
@Controller
@RequestMapping("/wechat/pay")
public class WechatPayController {

	private final Log log = LogFactory.getLog(WechatPayController.class);
	
	private static final String TEMPLATE_PATH = "wechat"; // 页面目录
	private static final String BILL_CREATE_IP="192.168.0.123";

	/**
	 * 微信对象-服务
	 */
	@Autowired
	private WechatService wechatService;
	/**
	 * 
	 */
	@Autowired
	private WxPayConfigService wxPayConfigService;
	/**
	 * 客户账目-服务
	 */
	@Autowired
	private CustomerAccountItemService customerAccountItemService;
	
	/**
	 * 微信支付通知-服务
	 */
	@Autowired
	private WxPayNotifyService wxPayNotifyService; 
	
	/**
	 * @Fields wechatOrderService：支付订单-服务
	 */
	@Autowired 
	WechatOrderService wechatOrderService;
	
	/**
	 * @Fields wechatCustomerService：微信-客户  service
	 */
	@Autowired WechatCustomerService wechatCustomerService;

	/**
	 * [支付]确定
	 * @param billIds  帐单ID列表  JSON数组
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/gotopay")	                      
	public String gotopay(String billIds,  
						  HttpServletRequest request, 
						  Model model) {
		
		WechatUser wechatUser = (WechatUser) request.getSession().getAttribute(SessionConstant.USER);
		Wechat wechat = wechatService.getWechat();//微信信息		
		
		//(1)查询客户账单
		List<Long> billIdList=JSON.parseArray(billIds, Long.class);
		List<CustomerAccountItem> accountItemList =getCustomerAccountItem(billIdList);
		
		model.addAttribute("accountItemList", accountItemList);
		
		//(2)计算账单总和
		BigDecimal orderAmount=calcAccountItemFeeSum(accountItemList);
		model.addAttribute("billSum", orderAmount);
		
		//(3)将账单金额转换成  分
		//int orderAmountCent=Integer.parseInt(BigDecimalUtils.multiply(orderAmount, new BigDecimal(100), 0).toPlainString());
		
		//(3)生成支付订单
		String jsonBills=getAccountItemIdAmountStr(accountItemList);  //生成 id:amount,id:amcount  字符串
		//保存支付定单到数据库中  2019/12/27
		//Map<String,Object> tempMap = addBusinessPayOrder(wechat,wechatUser,jsonBills,orderAmount);		
		Map<String,Object> tempMap = addBusinessPayOrder(EnumWeChatOrderType.TYPE_PAY_WATER_FEE.getValue(),
														null,
														wechat.getAppid(),
														wechatUser.getOpenid(),
														jsonBills,orderAmount);
		String busOrderNo=(String)tempMap.get("busOrderNo");  //订单号
		long busOrderId=(Long)tempMap.get("busOrderId");   //此订单在数据库的ID

		String url="";
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url = request.getRequestURL().toString() + "?" + request.getQueryString();
		}
		else {
			url = request.getRequestURL().toString();
		}		
		Map<String, String> jsMap = wechatService.getJsConfig(wechat.getAppid(), url);
		
		//(4.1)调用建行下单接口  2019/12/28
		PayParams payParams=CCBWechatOrderUtil.postOrder2CCB(busOrderNo,orderAmount,wechat.getAppid(),wechatUser.getOpenid());
		Map<String,Object> payMap=null;
		if (payParams.getERRCODE().equalsIgnoreCase("000000")){
			log.debug("建行接口调用成功-发起微信端支付!");
			payMap=EntityUtils.entityToMap(payParams);
		}
		else {
			log.debug("建行接口调用-第二次请求后返回非0结果码.!");
		}
		
		//Map<String, String> payMap = wxPayConfigService.getWxPayConfig(wechat, wechatUser,busOrderNo,orderAmountCent,BILL_CREATE_IP);//获取微信支付页面所需要的参数
		
		//		if(payMap==null) {
		//			log.debug("获取微信支付页面所需要的参数----发生错误!");
		//		}
		
		//String prepayId= payMap.get("prepayId");
		//updateBusOrderPrepayId(busOrderId,prepayId);
		model.addAttribute("payParams", payParams);  //added by hz  2010/01/09
		
		model.addAttribute("busOrderId",busOrderId);  //预支付订单ID
		
		//微信支付参数配置
		model.addAttribute("jsMap",jsMap);
		model.addAttribute("payMap",payMap);
				
		return TEMPLATE_PATH+"/bill/pay_weui";   //返回确认支付页面
	}
	
	/**
	 *   更新指定支付订单的 预付ID
	 * @param busOrderId  业务端支付订单ID(key)
	 * @param prepayId  预支付D 
	 * @return 影响的行数
	 */
	private int updateBusOrderPrepayId(long busOrderId,String prepayId) {
		WechatOrder orderx=new WechatOrder();
		orderx.setId(busOrderId);
		orderx.setPrepayId(prepayId);
		return wechatOrderService.updateByPrimaryKeySelective(orderx);
	}
	
	/**
	 * @Title: addBusinessPayOrder
	 * @Description: 增加支付订单
	 * @param orderType 订单类型 参见常量类 EnumWeChatOrderType
	 * @param appId  微信公众号ID
	 * @param openId 此公众号关注客户的ID
	 * @param billIds		账单ID列表(JSON格式)
	 * @param orderAmount  订单总金额
	 * @return    
	 * 				busOrderNo--->支付订单的编号(唯一)
	 * 			   	busOrderId--->支付订单的ID	(自增主键)
	 */
	private Map<String,Object> addBusinessPayOrder(int orderType,Long customerId,String appId,String openId, String idAmountPair,BigDecimal orderAmount) {
//		final int RESULT_CODE_NO_PAY=0;  		//尚未支付
//		final int RESULT_CODE_WAIT_NOTIFY=1;    //支付成功等待通知.
//		final int RESULT_CODE_PAY_SUCCESS=2; 	//支付成功-接到成功通知
//		final int RESULT_CODE_PAY_FAIL=3;    	//支付失败-接到失败通知
		
		WechatOrder wechatOrder=new WechatOrder();
		
		//生成营收系统内的支付订单号
		String orderNo=WechatOrderUtil.genOrderIdBySnowflakeCCB();  		
		
		//微信相关参数
		wechatOrder.setAppId(appId);
		wechatOrder.setOpenId(openId);	
		wechatOrder.setCustomerId(customerId);
		
		
		//订单相关参数
		wechatOrder.setOrderNo(orderNo);		  			//营收系统内部订单号
		wechatOrder.setOrderDate(new Date());	  			//订单日期
		wechatOrder.setOrderAmount(orderAmount);  			//订单金额
		
		//订单中-账单消息
		wechatOrder.setBillIds(idAmountPair);				//订单相关accountItemId
		wechatOrder.setResultCode(EnumPayResultCode.RESULT_CODE_NO_PAY.getValue());      //支付订单的初始状态(尚未支付)
		
		wechatOrder.setOrderType(orderType);  //加入订单类型  added by hz  2020/01/10
		
		wechatOrderService.insertSelective(wechatOrder);
		
		Map<String,Object> map=new HashMap<>();
		map.put("busOrderNo", orderNo);
		map.put("busOrderId",wechatOrder.getId());
		
		return map;
	}
	
	/**
	 * @Title: getAccountItemIdAmountStr
	 * @Description: 生成账单的id:amount对象  可多个  以逗号进行分隔
	 * @param accountItemList  账单列表
	 * @return  返回格式示例:1:22,2:33
	 */
	/**
	 * @Title: getAccountItemIdAmountStr
	 * @Description: 生成帐单跟踪历史
	 * @param accountItemList
	 * @return  结构如下:
	 * 		[
	 * 			{
	 * 				customerId:c1,
	 * 				bills:[
	 * 						id:xxxx,
	 * 						amount:xxxx 
	 * 						]
	 * 			},
	 * 			{
	 * 				customerId:c2,
	 * 				bills:[
	 * 						id:xxxx,
	 * 						amount:xxxx 
	 * 						]
	 * 			},
	 * 		] 
	 * 
	 */
	private String getAccountItemIdAmountStr(List<CustomerAccountItem> accountItemList) {
		final String KEY_CUSTOMER_ID="customerId";
		final String KEY_BILLS="bills";
		final String KEY_ID="id";
		final String KEY_AMOUNT="amount";
		
		List<Map<String,Object>> customerBillList=new ArrayList<>();		
		
		//收集账单中的客户信息,并置于一个map中.
		Map<String,Object> customerMap=new HashMap<>();
		for(CustomerAccountItem accountItem:accountItemList) {
			customerMap.put(accountItem.getCustomerId().toString(), null);
		}
		
		//根据客户信息生成所需要的数据结构
		/* 		[
		  		 * 			{
		  		 * 				customerId:c1,
		  		 * 				bills:[
		  		 * 						 
		  		 * 					  ]
		  		 * 			},
		  		 * 			{
		  		 * 				customerId:c2,
		  		 * 				bills:[
		  		 *  						 
		  		 * 					  ]
		  		 * 			},
		  		 * 		] 
		 */ 				
		for(String key:customerMap.keySet()) {
			Map<String,Object> tempMap=new HashMap<>();
			
			//客户ID
			tempMap.put(KEY_CUSTOMER_ID, key);  
			
			//客户下账单列表
			List<Map<String,Object>> bills=  new ArrayList<>();
			tempMap.put(KEY_BILLS, bills);
			
			customerBillList.add(tempMap);
		}
		
		//向此结构中增加数据
		for(CustomerAccountItem accountItem:accountItemList) {
			//在列表中查询客户
			String customerId=accountItem.getCustomerId().toString();			
			for(Map<String,Object> tempMap:customerBillList) {
				if(tempMap.get("customerId")!=null) {  //查询到客户
					@SuppressWarnings("unchecked")
					List<Map<String,Object>> bills=(ArrayList<Map<String,Object>>)tempMap.get("bills");  //取得客户的帐单对象
					
					BigDecimal feex =accountItem.getCreditAmount().subtract(accountItem.getDebitAmount());  //账单费用  贷方金额-借方金额
					String accountItemId=accountItem.getId().toString();
					
					//向账单列表中加入数据
					Map<String,Object> billMap=new HashMap<>();
					billMap.put("id", accountItemId);
					billMap.put("amount", feex.toPlainString());
					bills.add(billMap);
					
				}
			}
		}
		
		String jsonStr =JSON.toJSONString(customerBillList);
		return jsonStr;
	}
	
	
	/**
	 * 根据账单ID列表读取账单
	 * @param accountItemIdList  账单ID列表
	 * @return  账单列表
	 */
	private List<CustomerAccountItem> getCustomerAccountItem(List<Long> accountItemIdList){
		List<CustomerAccountItem> itemList=new ArrayList<>();
		for(Long accountItemId:accountItemIdList) {
			CustomerAccountItem accountItem=customerAccountItemService.selectByPrimaryKey(accountItemId);
			itemList.add(accountItem);
		}
		return itemList;
	}
	
	/**
	 * 计算账单的总和
	 * @param accountItemList
	 * @return
	 */
	private BigDecimal calcAccountItemFeeSum(List<CustomerAccountItem> accountItemList) {
		BigDecimal sum=new BigDecimal(0);
		for(CustomerAccountItem accountItem:accountItemList) {
			BigDecimal feex =accountItem.getCreditAmount().subtract(accountItem.getDebitAmount());  //账单费用   贷方金额-借方金额
			sum=sum.add(feex);
		}
		
		return sum;
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
	 * 获取session用户的openId
	 * @param request
	 * @return
	 */
	private String getOpenIdFromSession(HttpServletRequest request) {
		WechatUser  wechatUser=(WechatUser)request.getSession().getAttribute(SessionConstant.USER);
		String openId=wechatUser.getOpenid();
		return openId;
	}
	
	//---------------------微信公众号前端HTML代码中JS调用(微信支付)--------------------------------
	
	/**
	 * 
	 * @Title: paySuccess
	 * @Description: 业务系统支付页面-支付 成功后可以由前台调用此函数
	 * 				  置预支付订单的状态为:    EnumPayResultCode.RESULT_CODE_WAIT_NOTIFY(等待支付服务器Notify消息.)
	 * 
	 * 				 页面端调用(成功支付后由页面端进行调用  2020/01/08
	 * 				  
	 * @param busOrderId  预支付订单ID
	 * @param request
	 * @param model
	 * @return   
	 */
	@RequestMapping("/success")
	@ResponseBody
	public int paySuccess(long busOrderId,HttpServletRequest request, Model model) {
		log.debug("用户支付成功(微信公众号客户端已经确认支付,预支付订单ID:"+busOrderId);
		WechatOrder updateObj=new WechatOrder();
		updateObj.setId(busOrderId);
		updateObj.setResultCode(EnumPayResultCode.RESULT_CODE_SUCCESS.getValue());
		int row=wechatOrderService.updateByPrimaryKeySelective(updateObj);
		return row;
	}
	
	/**
	 * @Title: payFail
	 * @Description:由前端页面调用(支付失败时)
	 * @param busOrderId  预支付订单ID(不是预支付订单的NO)
	 * @param request
	 * @param model
	 * @return 
	 */
	@RequestMapping("/fail")
	@ResponseBody
	public int payFail(long busOrderId,HttpServletRequest request, Model model) {
		log.debug("用户支付失败,预支付订单ID:"+busOrderId);
		WechatOrder updateObj=new WechatOrder();
		updateObj.setId(busOrderId);
		updateObj.setResultCode(EnumPayResultCode.RESULT_CODE_FAIL.getValue());  //支付失败状态
		int row=wechatOrderService.updateByPrimaryKeySelective(updateObj);
		return row;
	}
	
	//-------------------CCB微信聚合支付--- 回调通知接收及处理-------------------------
	
	/**
	 * 微信支付服务器回调此URL
	 * @param request
	 * @return
	 */
	@RequestMapping("/notify")
	@ResponseBody
	public String doPayNotify(HttpServletRequest request) {
		String result = "success";		
		log.debug("接收到异步通知消息...........");	
		
//		  try { Map<String, String> reqMap = wxPayNotifyService.parseXML(request);
//		  log.info("支付通知：" + reqMap); result = wxPayNotifyService.doPayNotify(reqMap);
//		  } catch (IOException | DocumentException e) {
//		  log.error("支付结果通知request转换map异常", e); }
		
		//(1)解析自CCB回调时所加入的参数
		CCBCallbackParam callbackParam=getCCBCallbackParams(request);
		log.debug("所收到的异步消息内容-所有字段:"+callbackParam.toString());
		//(2)根据回调情况置订单的状态
		processCCBCallbackNotify(callbackParam);
		
		return result;
	}
	
	/*
	 *  获取回调请求中的参数信息(CCB-----call---->营收系统)
	 *  返回数据示例(实际测试数据):
	 *  	
			POSID=041692036, 
			BRANCHID=130000000, 
			ORDERID=202001081402664468869741543424,   //营收系统生成的订单ID
			PAYMENT=0.01, 
			CURCODE=01, 
			REMARK1=, REMARK2=, 
			ACC_TYPE=WX, 
			SUCCESS=Y, 
			ACCDATE=20200108
		
	 *  
	 *  CCB方采用POST方式发送请求.
	 *  其中的参数采用形如:  http://www.xxx.com/?parms=123  方式进行传递
	 */
	private CCBCallbackParam getCCBCallbackParams(HttpServletRequest request) {
		CCBCallbackParam param=new CCBCallbackParam();
		
		param.setPOSID(request.getParameter("POSID"));  //柜台号
		param.setBRANCHID(request.getParameter("BRANCHID"));  //分行代码
		param.setORDERID(request.getParameter("ORDERID"));  //订单号(营收系统端定义的订单编号.)
		param.setPAYMENT(request.getParameter("PAYMENT"));  //支付金额
		
		param.setCURCODE(request.getParameter("CURCODE"));  //币种代码
		param.setREMARK1(request.getParameter("REMARK1"));  //备注1
		param.setREMARK2(request.getParameter("REMARK2"));  //备注2
		param.setACC_TYPE(request.getParameter("ACC_TYPE"));  //账户类型  此处返回WX
		param.setSUCCESS(request.getParameter("SUCCESS"));  //成功状态
		param.setACCDATE(request.getParameter("ACCDATE"));  //
		
		return param;
	}
	
	/**
	 * @Title: processCCBCallbackNotify
	 * @Description: 处理CCB微信聚合支付回调通知
	 * @param callbackParam  CCB回调时传递的参数对象
	 * @param busOrderId 预支付订单ID
	 */
	private void processCCBCallbackNotify(CCBCallbackParam callbackParam) {
		//参数SUCCESS返回值域如下所示:
		final String PAY_STATE_SUCCESS="Y";  
		final String PAY_STATE_FAIL="N";
		
		//如果支付成功,确定回调支付状态
		int resultCode=-1;
		if(StringUtils.equalsIgnoreCase(callbackParam.getSUCCESS(), PAY_STATE_SUCCESS)) {
			resultCode=EnumPayResultCode.RESULT_CODE_SUCCESS .getValue();
		}
		else if(StringUtils.equalsIgnoreCase(callbackParam.getSUCCESS(), PAY_STATE_FAIL)) {  //支付失败
			resultCode=EnumPayResultCode.RESULT_CODE_FAIL  .getValue();
		}
		
		//接收到回调,保存状态到数据库中.
		if(resultCode>=0) {
			//根据订单号查询订单
			WechatOrder searchObj=new WechatOrder();
			searchObj.setOrderNo(callbackParam.getORDERID());
			List<WechatOrder> orderList=   wechatOrderService.select(searchObj);

			//如果查询到预支付订单时,更新订单的状态及金额.
			if(orderList.size()>0) {
				
				WechatOrder currOrder=orderList.get(0);
				
				//防止多次回调
				if(currOrder.getResultCodeCallback()<=0) {
					
					//STEP1:更新预支付订单的状态
//					WechatOrder updateObj=new WechatOrder();
//					updateObj.setId(currOrder.getId());	
//					updateObj.setPayAmount(new BigDecimal(callbackParam.getPAYMENT()));  //金额
//					updateObj.setResultCodeCallback(resultCode);  //设置回调的状态
//					int row=wechatOrderService.updateByPrimaryKeySelective(updateObj);
					
					updatePrepayorderByNotify(new BigDecimal(callbackParam.getPAYMENT()),currOrder.getId(),resultCode);
					
					//STEP2:业务处理
					int orderType=currOrder.getOrderType();
					if(orderType==EnumWeChatOrderType.TYPE_PAY_WATER_FEE.getValue()) {   //支付水费
						//STEP2.1:平账接口调用.传递账单ID列表
						List<Long> idList=getAccountItemIdListFromPayOrder(currOrder.getBillIds());
						customerAccountItemService.wechatSettlement(idList);
					}
					else if(orderType==EnumWeChatOrderType.TYPE_PREPAYMENT.getValue()){  //预支付水费
						//STEP2.2:预支付水费订单处理.
						//需要的参数为customer_id与amount即可.  customer_id可以自微信与客户的关系表中取得,   金额可以自预支付订单表中orderAmount取得. 
						//Long customerId=getCustomerIdByOpenId(currOrder.getOpenId());
						Long customerId=currOrder.getCustomerId();
						Long accountItemId= customerAccountItemService.wechatPrepayment(customerId,new BigDecimal(callbackParam.getPAYMENT()));
						//更新预支付订单
						int rowx=createRelationPrepayOrderAndAccountItem(accountItemId,currOrder.getId());
						
					}
				}
			}
		}
	}
	
	//根据回调信息更新预支付订单
	private int updatePrepayorderByNotify(BigDecimal payment,Long prepayOrderId,int resultCodeCallBack) {
		WechatOrder updateObj=new WechatOrder();
		updateObj.setId(prepayOrderId);	
		updateObj.setPayAmount(payment);  //金额
		updateObj.setResultCodeCallback(resultCodeCallBack);  //设置回调的状态
		
		int row=wechatOrderService.updateByPrimaryKeySelective(updateObj);
		
		return row;
	}
	
	/**
	 * @Title: createPrepayOrderAndAccountItem
	 * @Description: 建立账单与预支付订单之间的关系.
	 * @param accountItemId  账单条目ID
	 * @Param 
	 * @return   行数
	 */
	private int createRelationPrepayOrderAndAccountItem(Long accountItemId,Long wechatOrderId) {
		WechatOrder updateObj=new WechatOrder();
		updateObj.setId(wechatOrderId);
		updateObj.setAccountItemId(accountItemId.toString());
		
		int row=wechatOrderService.updateByPrimaryKeySelective(updateObj);
		
		return row;
	}
	
	
	//根据微信openId获取用户的ID
	private Long getCustomerIdByOpenId(String openId) {
		WechatCustomer searchObj=new WechatCustomer();
		searchObj.setOpenid(openId);
		
		List<WechatCustomer> relList= wechatCustomerService.select(searchObj);
		
		Long customerId=null;
		if(relList.size()>0) {
			customerId=relList.get(0).getCustomerId();
		}
		
		return customerId;
	}

	/*
		获取支付订单中相关的账单ID列表.
			解析JSON对象:
			[{"customerId":"56702","bills":[{"amount":"0.01","id":"15103"}]}]
	*/
	private  List<Long> getAccountItemIdListFromPayOrder(String desc){
		List<Long> idList=new ArrayList<>();
		JSONArray accountList=JSON.parseArray(desc);
		for(int i=0;i<accountList.size();i++) {
			JSONObject account=accountList.getJSONObject(i); 
			String billsStr=account.getString("bills");
			JSONArray billList=JSON.parseArray(billsStr);
			for(int j=0;j<billList.size();j++){
				JSONObject bill=billList.getJSONObject(j);
				idList.add(bill.getLong("id"));
			}
			
		}
		return idList;
	}
	
	//----------------------预支付水费部分-----------------------
	
	
	/**
	 * 预支付水费[支付]确定
	 * @param amount 金额
	 * @param CustomerId 金额
	 * @param customerLocation 客户编号及地址
	 * @param request
	 * @param model
	 * @return
	 */
	@RequestMapping("/gotoprepay")	                      
	public String gotoPrepay(String amount,  
							Long customerId,
							String customerLocation,
						  HttpServletRequest request, 
						  Model model) {
		
		
		
		WechatUser wechatUser = (WechatUser) request.getSession().getAttribute(SessionConstant.USER);
		Wechat wechat = wechatService.getWechat();//微信信息		
		
		//(1)预支付金额
		BigDecimal orderAmount=new BigDecimal(amount);
		model.addAttribute("prepayAmount", orderAmount);

		//(3)生成支付订单
		//保存支付定单到数据库中  2019/12/27
		//Map<String,Object> tempMap = addBusinessPayOrder(wechat,wechatUser,jsonBills,orderAmount);		
		Map<String,Object> tempMap = addBusinessPayOrder(EnumWeChatOrderType.TYPE_PREPAYMENT.getValue(),
														customerId,
														wechat.getAppid(),
														wechatUser.getOpenid(),
														"",
														orderAmount);
		String busOrderNo=(String)tempMap.get("busOrderNo");  //订单号
		long busOrderId=(Long)tempMap.get("busOrderId");   //此订单在数据库的ID

		String url="";
		if(StringUtils.isNotBlank(request.getQueryString())) {
			url = request.getRequestURL().toString() + "?" + request.getQueryString();
		}
		else {
			url = request.getRequestURL().toString();
		}		
		Map<String, String> jsMap = wechatService.getJsConfig(wechat.getAppid(), url);
		
		//(4.1)调用建行下单接口  2019/12/28
		PayParams payParams=CCBWechatOrderUtil.postOrder2CCB(busOrderNo,orderAmount,wechat.getAppid(),wechatUser.getOpenid());  //向建设银行发送请求后的结果
		Map<String,Object> payMap=null;
		if (payParams.getERRCODE().equalsIgnoreCase("000000")){
			log.debug("建行接口调用成功-发起微信端支付!");
			payMap=EntityUtils.entityToMap(payParams);
		}
		else {
			log.debug("建行接口调用-第二次请求后返回非0结果码.!");
		}
		
		model.addAttribute("payParams", payParams);  //added by hz  2010/01/09
		model.addAttribute("busOrderId",busOrderId);  //预支付订单ID
		
		model.addAttribute("customerId",customerId);  //客户ID
		model.addAttribute("customerLocation",customerLocation);  //客户编号及地址
		
		//微信支付参数配置
		model.addAttribute("jsMap",jsMap);
		model.addAttribute("payMap",payMap);
				
		return TEMPLATE_PATH+"/prepay/prepay_pay";   //返回确认支付页面
	}
	
	
	
	//------------------TEST--------------
	public static final void main(String[] args) {
		String desc="[{\"customerId\":\"56702\",\"bills\":[{\"amount\":\"0.01\",\"id\":\"15103\"}]}]";
//		List<Long> idList=WechatPayController.getAccountItemIdListFromPayOrder(desc);		
//		for(int i=0;i<idList.size();i++) {
//			System.out.println("id index:"+i+"  value is:"+idList.get(i));
//		}
		
	}
	
	
	
	//自流中读入返回的字符串
//	private String getStrFromStream(InputStream inputStream) throws IOException {
//		StringBuilder sb = new StringBuilder();
//		String line;
//		BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
//		while ((line = br.readLine()) != null) {
//			sb.append(line);
//		}
//		String str = sb.toString();
//		return str;
//	}
	
	


}
