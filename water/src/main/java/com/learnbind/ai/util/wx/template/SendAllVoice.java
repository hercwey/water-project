package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllVoice extends SendAllBase {

	private Map<String, String> voice;
	
	public SendAllVoice(String mediaId) {
		super();
		setMsgtype("voice");
		Map<String, String> map = new HashMap<String, String>();
		map.put("media_id", mediaId);
		this.voice = map;
	}

	public Map<String, String> getVoice() {
		return voice;
	}

	public void setVoice(Map<String, String> voice) {
		this.voice = voice;
	}

	@Override
	public String toString() {
		return "SendAllVoice [voice=" + voice + ", toString()="
				+ super.toString() + "]";
	}
}
