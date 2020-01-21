package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllCard extends SendAllBase {

	private Map<String, String> wxcard;

	public SendAllCard(String cardId) {
		super();
		setMsgtype("wxcard");
		Map<String, String> map = new HashMap<String, String>();
		map.put("card_id", cardId);
		this.wxcard = map;
	}
	
	public Map<String, String> getWxcard() {
		return wxcard;
	}

	public void setWxcard(Map<String, String> wxcard) {
		this.wxcard = wxcard;
	}

	@Override
	public String toString() {
		return "SendAllCard [wxcard=" + wxcard + ", toString()="
				+ super.toString() + "]";
	}
	
}
