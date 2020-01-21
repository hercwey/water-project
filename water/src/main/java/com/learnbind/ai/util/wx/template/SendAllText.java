package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllText extends SendAllBase {

	private Map<String, String> text;
	
	public SendAllText(String content) {
		super();
		setMsgtype("text");
		Map<String, String> map = new HashMap<String, String>();
		map.put("content", content);
		this.text = map;
	}

	public Map<String, String> getText() {
		return text;
	}

	public void setText(Map<String, String> text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "SendAllText [text=" + text + ", toString()=" + super.toString()
				+ "]";
	}
	
}
