package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllVideo extends SendAllBase {
	
	private Map<String, String> mpvideo;

	public SendAllVideo(String mediaId) {
		super();
		setMsgtype("mpvideo");
		Map<String, String> map = new HashMap<String, String>();
		map.put("media_id", mediaId);
		this.mpvideo = map;
	}
	
	public Map<String, String> getMpvideo() {
		return mpvideo;
	}

	public void setMpvideo(Map<String, String> mpvideo) {
		this.mpvideo = mpvideo;
	}

	@Override
	public String toString() {
		return "SendAllVideo [mpvideo=" + mpvideo + ", toString()="
				+ super.toString() + "]";
	}
	
}
