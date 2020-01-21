package com.learnbind.ai.util.wx.pay;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.util.wx.pay
 *
 * @Title: UnifiedOrder.java
 * @Description: 统一下单
 *
 * @author Administrator
 * @date 2019年8月5日 下午6:43:48
 * @version V1.0 
 *
 */
public class UnifiedOrder {
	
	private String appid;//微信分配的公众账号ID
	
	private String mch_id;//微信支付分配的商户号
	
	private String device_info;//否 终端设备号(门店号或收银设备ID)，注意：PC网页或公众号内支付请传"WEB"
	
	private String nonce_str;//随机字符串，不长于32位。
	
	private String sign;//签名
	
	private String body;//商品或支付单简要描述
	
	private String detail;//否 商品名称明细列表
	
	private String attach;//否 附加数据，在查询API和支付通知中原样返回，该字段主要用于商户携带订单的自定义数据
	
	private String out_trade_no;//商户系统内部的订单号,32个字符内、可包含字母
	
	private String fee_type;//符合ISO 4217标准的三位字母代码，默认人民币：CNY
	
	private int total_fee;//订单总金额，单位为分
	
	private String spbill_create_ip;//APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
	
	private String time_start;//否 订单生成时间，格式为yyyyMMddHHmmss，如2009年12月25日9点10分10秒表示为20091225091010。
	
	private String time_expire;//否 订单失效时间，格式为yyyyMMddHHmmss，如2009年12月27日9点10分10秒表示为20091227091010。
	
	private String goods_tag;//否 商品标记，代金券或立减优惠功能的参数
	
	private String notify_url;//接收微信支付异步通知回调地址，通知url必须为直接可访问的url，不能携带参数。
	
	private String trade_type;//交易类型取值如下：JSAPI，NATIVE，APP
	
	private String product_id;//否 trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义。
	
	private String limit_pay;//否 no_credit--指定不能使用信用卡支付
	
	private String openid;//否 trade_type=JSAPI，此参数必传，用户在商户appid下的唯一标识。

	public String getAppid() {
		return appid;
	}

	/**
	 * 公众账号ID
	 * @param appid 必填
	 */
	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getMch_id() {
		return mch_id;
	}

	/**
	 * 商户号
	 * @param mch_id 必填
	 */
	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getDevice_info() {
		return device_info;
	}

	/**
	 * 设备号
	 * @param device_info 非必填
	 */
	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getNonce_str() {
		return nonce_str;
	}

	/**
	 * 随机字符串
	 * @param nonce_str 必填
	 */
	public void setNonce_str(String nonce_str) {
		this.nonce_str = nonce_str;
	}

	public String getSign() {
		return sign;
	}
	
	/**
	 * 签名
	 * @param sign 必填
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}
	
	/**
	 * 商品描述
	 * @param body 必填
	 */
	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}
	
	/**
	 * 商品详情
	 * @param detail 非必填
	 */
	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAttach() {
		return attach;
	}
	
	/**
	 * 附加数据
	 * @param attach 非必填
	 */
	public void setAttach(String attach) {
		this.attach = attach;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}
	
	/**
	 * 商户订单号
	 * @param out_trade_no 必填
	 */
	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getFee_type() {
		return fee_type;
	}
	
	/**
	 * 货币类型
	 * @param fee_type 非必填 默认人民币：CNY
	 */
	public void setFee_type(String fee_type) {
		this.fee_type = fee_type;
	}
	
	public int getTotal_fee() {
		return total_fee;
	}
	
	/**
	 * 总金额 
	 * @param total_fee 必填 单位为分
	 */
	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public String getSpbill_create_ip() {
		return spbill_create_ip;
	}
	
	/**
	 * 终端IP
	 * @param spbill_create_ip 必填
	 */
	public void setSpbill_create_ip(String spbill_create_ip) {
		this.spbill_create_ip = spbill_create_ip;
	}

	public String getTime_start() {
		return time_start;
	}
	
	/**
	 * 交易起始时间
	 * @param time_start 非必填 格式为yyyyMMddHHmmss
	 */
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}

	public String getTime_expire() {
		return time_expire;
	}
	
	/**
	 * 交易结束时间
	 * @param time_expire 非必填 格式为yyyyMMddHHmmss
	 */
	public void setTime_expire(String time_expire) {
		this.time_expire = time_expire;
	}

	public String getGoods_tag() {
		return goods_tag;
	}
	
	/**
	 * 商品标记
	 * @param goods_tag 非必填
	 */
	public void setGoods_tag(String goods_tag) {
		this.goods_tag = goods_tag;
	}

	public String getNotify_url() {
		return notify_url;
	}
	
	/**
	 * 通知地址
	 * @param notify_url 必填
	 */
	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getTrade_type() {
		return trade_type;
	}
	
	/**
	 * 交易类型
	 * @param trade_type 必填 取值如下：JSAPI，NATIVE，APP
	 */
	public void setTrade_type(String trade_type) {
		this.trade_type = trade_type;
	}

	public String getProduct_id() {
		return product_id;
	}
	
	/**
	 * 商品ID
	 * @param product_id trade_type=NATIVE，此参数必传。
	 */
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getLimit_pay() {
		return limit_pay;
	}
	
	/**
	 * 指定支付方式
	 * @param limit_pay 非必填
	 */
	public void setLimit_pay(String limit_pay) {
		this.limit_pay = limit_pay;
	}

	public String getOpenid() {
		return openid;
	}
	
	/**
	 * 用户标识
	 * @param openid trade_type=JSAPI，此参数必传
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}

	@Override
	public String toString() {
		return "UnifiedOrder [appid=" + appid + ", mch_id=" + mch_id
				+ ", device_info=" + device_info + ", nonce_str=" + nonce_str
				+ ", sign=" + sign + ", body=" + body + ", detail=" + detail
				+ ", attach=" + attach + ", out_trade_no=" + out_trade_no
				+ ", fee_type=" + fee_type + ", total_fee=" + total_fee
				+ ", spbill_create_ip=" + spbill_create_ip + ", time_start="
				+ time_start + ", time_expire=" + time_expire + ", goods_tag="
				+ goods_tag + ", notify_url=" + notify_url + ", trade_type="
				+ trade_type + ", product_id=" + product_id + ", limit_pay="
				+ limit_pay + ", openid=" + openid + "]";
	}

	
}
