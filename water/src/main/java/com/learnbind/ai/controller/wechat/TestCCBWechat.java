package com.learnbind.ai.controller.wechat;

import java.math.BigDecimal;
import java.util.Map;

import com.learnbind.ai.ccbwechat.entity.PayParams;
import com.learnbind.ai.ccbwechat.order.CCBWechatOrderUtil;
import com.learnbind.ai.common.util.EntityUtils;

//测试建行微信
public class TestCCBWechat {

	public static final void main(String[] args) {
		PayParams payParams = CCBWechatOrderUtil.postOrder2CCB("1234", new BigDecimal("0.01"), "wxd4f0915c71c3b84c", "oTZW-5l5E6seMT42SbDkNNX5zSHQ");
		Map<String, Object> payMap = null;
		if (payParams.getERRCODE().equalsIgnoreCase("000000")) {
			payMap = EntityUtils.entityToMap( payParams);
			System.out.print("建行接口调用成功!");
		} else {
			// log.debug("建行接口调用失败!");
			System.out.print("建行接口调用失败!");

		}

	}
}
