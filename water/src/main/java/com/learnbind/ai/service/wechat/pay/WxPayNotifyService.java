package com.learnbind.ai.service.wechat.pay;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.enumclass.EnumAiDebitCreditStatus;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectAction;
import com.learnbind.ai.common.enumclass.accountitem.EnumAiDebitSubjectPayment;
import com.learnbind.ai.common.util.fileutil.DateUtils;
import com.learnbind.ai.model.CustomerAccountItem;
import com.learnbind.ai.model.WechatOrder;
import com.learnbind.ai.service.customers.CustomerAccountItemService;
import com.learnbind.ai.service.customers.CustomerAccountService;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.service.TemplateService;
import com.learnbind.ai.service.wechat.service.WechatService;
import com.learnbind.ai.service.wechatuser.WechatOrderService;
import com.learnbind.ai.util.wx.WxPayOrderUtils;

/**
 * 微信支付回调处理-服务
 * @author lenovo
 *
 */
@Service
public class WxPayNotifyService {

	private static final Log log = LogFactory.getLog(WxPayNotifyService.class);
	
	//private static final long REDIS_CACHE_EXPIRE = 3600;

	//private static final String WX_PAY_NOTIFY = "WX_PAY_NOTIFY_";

	/**
	 * @Fields wechatService：微信对象-服务
	 */
	@Autowired
	private WechatService wechatService;
	
	/**
	 * @Fields templateService：模板-服务
	 */
	@Autowired
	private TemplateService templateService;
	
	/**
	 * @Fields customerAccountItemService：客户账目-服务
	 */
	@Autowired
	private CustomerAccountItemService customerAccountItemService;
	
	/**
	 * @Fields customerAccountService：客户帐户-服务
	 */
	@Autowired
	private CustomerAccountService customerAccountService;
	
	/**
	 * @Fields wechatOrderService：微信支付订单-服务
	 */
	@Autowired
	private WechatOrderService wechatOrderService; 
	 
	/**
	 * @Title: parseXML
	 * @Description:   解析微信支付时     支付服务器通知所发送的消息   xml
	 * 					xml--->map格式
	 * 					消息内容及格式参见链接: https://pay.weixin.qq.com/wiki/doc/api/jsapi.php?chapter=9_7&index=8  
	 * @param request
	 * @return
	 * @throws IOException
	 * @throws DocumentException 
	 */
	public  Map<String, String> parseXML(HttpServletRequest request)
			throws IOException, DocumentException {

		Map<String, String> map = new HashMap<String, String>();
		SAXReader reader = new SAXReader();

		InputStream in = request.getInputStream();

		Document document = reader.read(in);
		Element root = document.getRootElement();
		List<Element> elementList = root.elements();

		for (Element e : elementList) {
			map.put(e.getName(), e.getText());
		}

		return map;

	}
	
	/**
	 * @Title: doPayNotify
	 * @Description: 微信支付完成后回调处理
	 * @param reqMap
	 * @return 
	 */
	public String doPayNotify(Map<String, String> reqMap) {
		final String RETURN_CODE="SUCCESS";
		final String RETURN_MSG="OK";
		
		String result = respXml("FAIL", "支付通知异常");

		if ("SUCCESS".equals(reqMap.get("return_code"))) {
			Wechat wechat = wechatService.getWechat();//微信信息
			if (WxPayOrderUtils.isWxNotify(reqMap, wechat.getPaykey())) {
				if ("SUCCESS".equals(reqMap.get("result_code"))) {					
					//(1)支付订单成功后处理
					int state = payOrderSuccessProcess(reqMap);					
					if (state > 0) {
						//(2)告知支付服务器通知消息接收处理完毕
						result = respXml(RETURN_CODE, RETURN_MSG);  
					}
				} else {
					log.error(reqMap.get("err_code") + ":" + reqMap.get("err_code_des"));
				}
			} else {
				log.error("微信支付通知签名异常");
			}
		} else {
			log.error(reqMap.get("return_msg"));
		}

		return result;

	}

	/**
	 * @Title: respXml
	 * @Description: 生成响应支付服务器Notify消息  xml格式  
	 * @param code 响应码
	 * @param msg  响应消息
	 * @return  生成的响应消息 
	 */
	private String respXml(String code, String msg) {
		Document document = DocumentHelper.createDocument();
		Element root = document.addElement("xml");

		root.addElement("return_code").addCDATA(code);
		root.addElement("return_msg").addCDATA(msg);

		return document.asXML();
	}
	
	/**
	 * @Title: payOrder
	 * @Description: 修改支付完成后账单的状态
	 * @param reqMap
	 * @return 
	 */
	public int payOrderSuccessProcess(Map<String, String> reqMap) {
		log.info(reqMap);
		
		long timeEnd=transDateTime2Long(reqMap.get("time_end"));  //转换为long类型
		String totalYuan=getTotalyuan(reqMap.get("cash_fee"));  //单位变换:分--->元	
		String outTradeNo=reqMap.get("out_trade_no");			//业务系统内部订单号	
		String openId=reqMap.get("openid");						//微信用户
		
		int result=busProcess(outTradeNo, timeEnd,openId,totalYuan);  //业务处理
		
		return result;
	}
	
	//支付状态常量
//	public final static int RESULT_CODE_NO_PAY=0;  		//尚未开始支付
//	public final static int RESULT_CODE_WAIT_NOTIFY=1;    //支付成功-等待通知.
//	public final static int RESULT_CODE_PAY_SUCCESS=2; 	//支付成功-接收到通知-成功
//	public final static int RESULT_CODE_PAY_FAIL=3;    	//支付失败-接收到通知-失败
	
	/**
	 * @Title: payOrder
	 * @Description: 修改支付完成后账单的状态
	 * @param outTradeNo  业务系统内订单号
	 * @param timeEnd	      支付完成时间 (long类型)
	 * @param openId	      支付用户openId	
	 * @param cashFee     现金支付金额   货币单位:元
	 * @return   result>0时处理正确,如果result<0时处理过程中发生错误
	 * 注:此处需要进行串行化处理
	 * 处理过程如下:
	 *	(1)更新支付订单
	 * 	(2)新增支付账目
	 * 	(3)对支付成功的帐目进行销账
	 * 	(4)发送微信模板消息	
	 */
	@Transactional	
	public synchronized  int busProcess(String outTradeNo, long timeEnd, String openId, String cashFee) {
		int result=0;
		
		//(1)更新支付订单为成功状态
		int row=updateWechatOrderState(outTradeNo,EnumPayResultCode.RESULT_CODE_FAIL.getValue());
		if(row>0) {
			//(2)新增充值账目,记入借方
			WechatOrder wechatOrder=getWechatOrderByBusOrderNo(outTradeNo);	//根据OrderNo查询充值订单	
			String billIds=wechatOrder.getBillIds(); //支付订单所对应的账单跟踪
			List<Map<String,Object>> accounItemIdList=addPayAccountItem(billIds);  //返回客户所增加的充值账目ID
			//(2.1)更新支付订单所关联的账目ID,采用JSON格式.结构同accountItemIdList;
			String jsonAccountItemIdTrace=JSON.toJSONString(accounItemIdList);
			updateWechatOrderRelateAccountItem(outTradeNo,jsonAccountItemIdTrace);			
			
			//(3)对需要销账的帐目进行销账处理.(记录帐目之间的引用关系)
			chargeOffsProcess(billIds,accounItemIdList);
			
			//(4)发送微信模板消息
			//TODO 稍后处理.
			//sendPayTemplayeByOrder();
			
			result=1;
		}
		else {
			result=0;
		}
		
		return result;
	}
	
	/**
	 * @Title: getWechatOrderByBusOrderNo
	 * @Description: 根据支付订单No查询支付订单
	 * @param outTradeNo 支付订单No(即  orderNo)
	 * @return 支付订单
	 */
	private WechatOrder  getWechatOrderByBusOrderNo(String outTradeNo) {
		//(1)生成查询对象
		WechatOrder searchObj=new WechatOrder();
		searchObj.setOrderNo(outTradeNo);
		//(2)查询
		WechatOrder resultObj=wechatOrderService.selectOne(searchObj);
		return resultObj;
	}
	
	/**
	 * @Title: updateWechatOrderRelateAccountItem
	 * @Description: 更新支付订单所关联的帐目ID(跟踪)
	 * @param outTradeNo  业务支付订单编号
	 * @param jsonAccountItemIdTrace   所关联的账目跟踪(JSON格式),结构如下所示:
	 * 		[
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai1	
	 * 			},
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai2
	 * 			}
	 * 			
	 * 		] 
	 * 			
	 * @return  更新时所影响的行数 
	 */
	private int updateWechatOrderRelateAccountItem(String outTradeNo,String jsonAccountItemIdTrace) {
		//(1)按业务系统支付订单号进行查询订单
		WechatOrder searchObj=new WechatOrder();
		searchObj.setOrderNo(outTradeNo);		
		WechatOrder wechatOrder=wechatOrderService.selectOne(searchObj);
		
		//如果是支付成功状态
		int row=0;
		if(wechatOrder.getResultCode()==EnumPayResultCode.RESULT_CODE_FAIL.getValue()) {
			//(2)更新
			//wechatOrder.setResultCode(resultCode);
			wechatOrder.setAccountItemId(jsonAccountItemIdTrace);
			//wechatOrder.setAccountItemId(accountItemId);  //支付订单--->帐目
			row=wechatOrderService.updateByPrimaryKeySelective(wechatOrder);
		}
		else {  //提示错误
			log.debug("更新支付订单相关联账目跟踪信息,但帐单的状态为"+searchObj.getResultCode());
		}
		return row;
	}
	
	/**
	 * @Title: addPayAccountItem
	 * @Description: 加入充值账目,记入借方
	 * @param billIds 账单记录
	 *  	[
	 * 			{
	 * 				customerId:c1,
	 * 				bills:[
	 * 						{id:xxxx,
	 * 						amount:xxxx},{} 
	 * 						]
	 * 			},
	 * 			{
	 * 				customerId:c2,
	 * 				bills:[
	 * 						{id:xxxx,
	 * 						amount:xxxx},{} 
	 * 						]
	 * 			},
	 * 		]
	 * 
	 * @return  
	 * 		[
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai1	
	 * 			},
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai2
	 * 			}
	 * 			
	 * 		] 
	 */
	private List<Map<String,Object>> addPayAccountItem(String billIds) {		
		List<Map<String,Object>> resultList=new ArrayList<Map<String,Object>>();		
		
		JSONArray jsonBillArray=JSON.parseArray(billIds);
		for(int i=0;i<jsonBillArray.size();i++) {
			Map<String,Object> resultMap=new HashMap<String,Object>();
			
			JSONObject jsonObj=jsonBillArray.getJSONObject(i);
			
			//(1)客户ID
			Long customerId=jsonObj.getLong("customerId");  //客户ID
			Long accountId=customerAccountService.getAccountIdByCustomerName(customerId);  //账户ID
			resultMap.put("customerId", accountId);
			
			//(2)特定客户帐单欠费金额合计.
			JSONArray jsonBills=(JSONArray)jsonObj.getJSONArray("bills");
			BigDecimal billSum=calcBillSum(jsonBills);   //帐单和
			//(3)增加充值账单
			long  accountItemId= createRechargeAccountItem(customerId,accountId,billSum);
			
			resultMap.put("accountItemId", accountItemId);
			
			resultList.add(resultMap);		
			
		}
		
		return resultList;
	}
	
	/**
	 * @Title: createAccountItem
	 * @Description: 向账目中增加---充值账目
	 * @param customerId	客户ID
	 * @param accountId		客户账户ID
	 * @param amount		账目金额
	 * @return  所加入账目的ID
	 */
	private Long createRechargeAccountItem(long customerId,Long accountId,BigDecimal amount) {
		final String OPERATOR_ID_SYSTEM="-1";  //系统作为操作员id
		
		//微信缴费借方科目
		String debitSubject = EnumAiDebitCreditStatus.DEBIT.getKey()+EnumAiDebitSubjectAction.PAY_WATER_FEE.getKey()+EnumAiDebitSubjectPayment.PAYMENT_WECHAT.getKey();
		
		CustomerAccountItem customerAccountItem=new CustomerAccountItem();
		customerAccountItem.setCustomerId(customerId);// 客户id
		//customerAccountItem.setPeriod(item.getPeriod());// 期间
		//Long accountId=customerAccountService.getAccountIdByCustomerName(customerId);  //账户ID
		customerAccountItem.setAccountId(accountId);// 账户id
		//customerAccountItem.setCreditAssistant(creditAssistant);//贷方辅助核算 原账单字符串
		customerAccountItem.setDebitCredit(EnumAiDebitCreditStatus.DEBIT.getKey());// 借/贷
		customerAccountItem.setDebitDigest("微信缴费");// 借方摘要-水费充值
		customerAccountItem.setDebitSubject(debitSubject);// 借方科目-充值
		customerAccountItem.setDebitAmount(amount);  //充值金额
		customerAccountItem.setAccounter(OPERATOR_ID_SYSTEM);// 记账人		
		customerAccountItem.setAccountDate(new Date());// 记账日期(系统当前日期)
		//customerAccountItem.setStartTime(date);// 账单产生时间段的开始时间
		//customerAccountItem.setEndTime(date);// 账单产生时间段的结束时间
		customerAccountItem.setRemark("微信充值水费");// 备注
		
		customerAccountItemService.insertSelective(customerAccountItem);
		
		return customerAccountItem.getId();
		
	}
	
	/**
	 * @Title: calcBillSum
	 * @Description: 计算账单的总和
	 * @param bills  账单数组 对象结构如下所示:
	 * 			[
	 * 				{
	 * 					id:xxxx,
	 * 					amount:xxxx
	 * 				},
	 * 				{
	 * 					id:xxxx,
	 * 					amount:xxxx
	 * 				} 
	 * 			]
	 * @return 
	 */
	private BigDecimal calcBillSum(JSONArray bills) {
		BigDecimal sum=new BigDecimal(0);
		for(int i=0;i<bills.size();i++) {
			JSONObject jsonObj=bills.getJSONObject(i);
			BigDecimal amount=jsonObj.getBigDecimal("amount");
			sum=sum.add(amount);
		}
		return sum;
	}
	
	/**
	 * @Title: chargeOffsProcess
	 * @Description: 销账处理
	 * @param billIds 支付订单中[账单]跟踪
	 *  	[
	 * 			{
	 * 				customerId:c1,
	 * 				bills:[
	 * 						{id:xxxx,
	 * 						amount:xxxx},{} 
	 * 						]
	 * 			},
	 * 			{
	 * 				customerId:c2,
	 * 				bills:[
	 * 						{id:xxxx,
	 * 						amount:xxxx},{} 
	 * 						]
	 * 			},
	 * 		]
	 * @param accounItemIdList 支付订单所关联的账目跟踪
	 * 		[
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai1	
	 * 			},
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai2
	 * 			}
	 * 			
	 * 		] 
	 */
	private void chargeOffsProcess(String billIds,List<Map<String,Object>> accounItemIdList) {
		//TODO 增加销账处理逻辑		
		//(1)销账处理(对)
		JSONArray jsonBillArray=JSON.parseArray(billIds);
		for(int i=0;i<jsonBillArray.size();i++) {
			//Map<String,Object> resultMap=new HashMap<String,Object>();
			
			JSONObject jsonObj=jsonBillArray.getJSONObject(i);
			
			//(1)客户ID  查询客户的充值账目ID
			long customerId=jsonObj.getLong("customerId");  //客户ID
			long payAccountItemId=getPayAccountItemId(customerId,accounItemIdList);  
			
			//(2.1)欠费账单列表平账
			//(2.2)记录账目之间的关系
			JSONArray jsonBills=(JSONArray)jsonObj.getJSONArray("bills");
			for(int j=0;j<jsonBills.size();j++) {
				JSONObject relJSONObj=jsonBills.getJSONObject(j);
				
				long billAccountItemId=relJSONObj.getLongValue("id");
				BigDecimal billAmount=relJSONObj.getBigDecimal("amount");  //账单金额
				
				//平账
				balanceAccountItem( billAccountItemId, payAccountItemId, billAmount);
			}
		}
		
		
	}
	
	/**
	 * @Title: balanceAccountItem
	 * @Description: 平账
	 * @param billAccountItemId 账单-账目ID
	 * @param payAccountItemId	  充值-账目ID	
	 * @param amount 			  金额
	 */
	
	private void balanceAccountItem(long billAccountItemId,long payAccountItemId,BigDecimal amount) {
		final String DESC="平帐";
		
		//(1)查询账单-账目
		CustomerAccountItem billAccountItem=customerAccountItemService.selectByPrimaryKey(billAccountItemId);
		//(2)账单账目-记借方金额及记账辅助
		//(2.1)记入借方金额
		billAccountItem.setDebitAmount(billAccountItem.getDebitAmount().add(amount));
		//(2.2)记借方辅助
		String debitAssistant=billAccountItem.getDebitAssistant();
		String newDebitAssistant=genAccountAssistant(payAccountItemId,amount,DESC,debitAssistant);
		billAccountItem.setDebitAssistant(newDebitAssistant);
		//(2.3)保存账单记账
		customerAccountItemService.updateByPrimaryKeySelective(billAccountItem);
		
		//(1)查询充值账目
		CustomerAccountItem payAccountItem=customerAccountItemService.selectByPrimaryKey(payAccountItemId);
		//(2)充值账目-记贷方金额及记账辅助
		//(2.1)记入贷方金额
		payAccountItem.setCreditAmount(billAccountItem.getCreditAmount().add(amount));
		String creditAssistant=payAccountItem.getCreditAssistant();
		String newCreditAssistant=genAccountAssistant(billAccountItemId,amount,DESC,creditAssistant);
		payAccountItem.setCreditAssistant(newCreditAssistant);
		//(2.3)保存账单记账
		customerAccountItemService.updateByPrimaryKeySelective(payAccountItem);
		
	}
	
	/**
	 * @Title: genAccountAssistant
	 * @Description: 生成记账辅助
	 * @param relAccountItemId  相关联的账目条目ID
	 * @param amount	相关联的金额
	 * @param desc		记账辅助描述
	 * @param accountAssistant  原有的记录辅助消息(JSON)
	 * 		所加入的记账辅助格式如下:(人,事,时间)
	 * 			id		--->相关联的账目ID
	 * 			amount	--->相关金额
	 * 			desc	--->描述
	 * 			date	--->日期
	 * 			operator--->操作员ID
	 * @return 
	 */
	private String genAccountAssistant(long relAccountItemId,BigDecimal amount,String desc,String accountAssistant) {
		JSONArray jsonArr;
		if(accountAssistant==null) {
			jsonArr=new JSONArray();			
		}
		else {
			jsonArr=JSON.parseArray(accountAssistant);
		}
		JSONObject item=new JSONObject();		
		item.put("relid", relAccountItemId);
		item.put("amount",amount);
		item.put("desc","平账");
		item.put("date",new Date());
		item.put("operator",-1);
		
		jsonArr.add(item);
		
		String jsonStr=jsonArr.toJSONString();
		
		return jsonStr;
	}
	
	/**
	 * @Title: getPayAccountItemId
	 * @Description: 自支付订单所关联的账目中查询指定的客户充值账目ID
	 * @param customerId	客户ID
	 * @param accounItemIdList  支付订单--关联-->充值账目跟踪
	 * 
	 * 		[
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai1	
	 * 			},
	 * 			{
	 * 				customerId:c1,
	 * 				accountItemId:ai2
	 * 			}
	 * 			
	 * 		] 
	 * @return  如果查询到,则返回充值账目ID,否则返回-1
	 */
	private long getPayAccountItemId(long customerId,List<Map<String,Object>> accounItemIdList) {
		long payAccountItemId=-1;   //充值账目ID
		for(int i=0;i<accounItemIdList.size();i++) {
			Map<String,Object> tempMap=accounItemIdList.get(i);
			long customerIdx=(Long)tempMap.get("customerId");
			long accountItemId=(Long)tempMap.get("accountItemId");
			if(customerIdx==customerId) {
				payAccountItemId=accountItemId;
				break;
			}
		}
		
		return payAccountItemId;
	}
	
	/**
	 * @Title: updateWechatOrderState
	 * @Description: 更新支付订单的状态
	 * @param outTradeNo
	 * @return 如果成功  
	 * 
	 * 注:此函数可以重构为采用一条SQL进行处理
	 */
	private int  updateWechatOrderState(String outTradeNo,int resultCode) {
		//(1)按业务系统支付订单号进行查询订单
		WechatOrder searchObj=new WechatOrder();
		searchObj.setOrderNo(outTradeNo);		
		WechatOrder wechatOrder=wechatOrderService.selectOne(searchObj);
		
		//如果是等待通知状态
		int row=0;
		if(wechatOrder.getResultCode()==EnumPayResultCode.RESULT_CODE_SUCCESS.getValue()) {
			//(2)更新
			wechatOrder.setResultCode(resultCode);
			//wechatOrder.setAccountItemId(accountItemId);  //支付订单--->帐目
			row=wechatOrderService.updateByPrimaryKeySelective(wechatOrder);
		}
		else {  //提示错误
			log.debug("接收到支付成功通知,但帐单的状态为"+searchObj.getResultCode());
		}
		return row;
	}
	
	/**
	 * @Title: sendPayTemplayeByOrder
	 * @Description: 发送支付完成后的模板消息
	 * @param totalYuan
	 * @param accountItem 
	 */
	private void sendPayTemplayeByOrder(String totalYuan, String openId, CustomerAccountItem accountItem){		
		//TODO 此处需要获取微信用户的openid
		//需要对此处进行重构
		templateService.sendPayTemplate(openId, totalYuan, accountItem);
	}

	/** 
	 * @Title: transDateTime2Long
	 * @Description: 将字符串时间---->long类型
	 * @param str  字符串类型时间
	 * @return   long类型时间
	 */
	private Long transDateTime2Long(String str) {
		Long time = 0L;
		try {
			time = DateUtils.formatTimeToLong(str);
		} catch (ParseException e) {
			log.error("支付通知时间转换异常", e);
			time = DateUtils.getDateTimeLong();
		}
		return time;
	}
	
	/**
	 * @Title: getTotalyuan
	 * @Description: 单位转换  分--->元
	 * @param total  以分为单位的金额
	 * @return 以元为单位的金额
	 */
	private String getTotalyuan(String total) {
		BigDecimal bd = new BigDecimal(total);
		return bd.divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP).toString();
	}
	
}
