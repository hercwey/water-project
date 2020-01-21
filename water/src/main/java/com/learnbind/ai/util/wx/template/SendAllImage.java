package com.learnbind.ai.util.wx.template;

import java.util.HashMap;
import java.util.Map;

public class SendAllImage extends SendAllBase {
	
	private Map<String, String> image;

	public SendAllImage(String mediaId) {
		super();
		setMsgtype("image");
		Map<String, String> map = new HashMap<String, String>();
		map.put("media_id", mediaId);
		this.image = map;
	}
	
	public Map<String, String> getImage() {
		return image;
	}

	public void setImage(Map<String, String> image) {
		this.image = image;
	}

	@Override
	public String toString() {
		return "SendAllImage [image=" + image + ", toString()="
				+ super.toString() + "]";
	}
	
}
