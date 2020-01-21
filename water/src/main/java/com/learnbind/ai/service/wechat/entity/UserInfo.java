package com.learnbind.ai.service.wechat.entity;

/**
 * Created by admin on 2016/10/17.
 */
public class UserInfo {
    /**
     * 微信用户唯一标识
     */
    private String openid;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private String sex;

    /**
     * 头像url
     */
    private String headimgurl;


    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

	@Override
	public String toString() {
		return "UserInfo [openid=" + openid + ", nickname=" + nickname
				+ ", sex=" + sex + ", headimgurl=" + headimgurl + "]";
	}
    
}
