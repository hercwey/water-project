package com.learnbind.ai.util.wx.errcode;



public abstract class ErrcodeHandle {
	
	protected ErrcodeHandle nextErrcodeHandle;

	public ErrcodeHandle getNextErrcodeHandle() {
		return nextErrcodeHandle;
	}

	public void setNextErrcodeHandle(ErrcodeHandle nextErrcodeHandle) {
		this.nextErrcodeHandle = nextErrcodeHandle;
	}

	public abstract String processErrcode(String accessToken, String errcode);

}
