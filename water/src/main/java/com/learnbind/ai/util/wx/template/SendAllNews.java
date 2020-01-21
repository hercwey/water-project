package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllNews extends SendAllBase {

	private Map<String, String> mpnews;
	
	public SendAllNews(String mediaId) {
		super();
		setMsgtype("mpnews");
		Map<String, String> map = new HashMap<String, String>();
		map.put("media_id", mediaId);
		this.mpnews = map;
	}

	public Map<String, String> getMpnews() {
		return mpnews;
	}

	public void setMpnews(Map<String, String> mpnews) {
		this.mpnews = mpnews;
	}

	@Override
	public String toString() {
		return "SendAllNews [mpnews=" + mpnews + ", toString()="
				+ super.toString() + "]";
	}

	
}
