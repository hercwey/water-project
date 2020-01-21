package com.learnbind.ai.service.wechat.entity;

/**
 * Created by Administrator on 2016/10/16.
 */
public class AccessToken {
    private String access_token;
    private int expires_in;
    private long createTime ;
    public AccessToken(){
        createTime = System.currentTimeMillis();
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    /**
	 * 验证access_token有效期
	 * 		真正有效期为7200秒，此处验证为7000秒
	 * @return
	 */
	public Boolean isValid(){
		Long currentTime = System.currentTimeMillis()/1000;
		if(createTime-currentTime>200){
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "AccessToken [access_token=" + access_token + ", expires_in=" + expires_in + ", createTime=" + createTime
				+ "]";
	}

}
