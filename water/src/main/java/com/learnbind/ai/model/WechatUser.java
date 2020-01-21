package com.learnbind.ai.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "WECHAT_USER")
public class WechatUser {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY, generator="SELECT WECHAT_USER_SEQ_GENERATOR.CURRVAL FROM DUAL")//前提是先创建了oracle序列
    private Long id;

    @Column(name = "SUBSCRIBE")
    private Integer subscribe;

    @Column(name = "OPENID")
    private String openid;

    @Column(name = "NICKNAME")
    private String nickname;

    @Column(name = "SEX")
    private Integer sex;

    @Column(name = "PROVINCE")
    private String province;

    @Column(name = "CITY")
    private String city;

    @Column(name = "COUNTRY")
    private String country;

    @Column(name = "HEADIMGURL")
    private String headimgurl;

    @Column(name = "PRIVILEGE")
    private String privilege;

    @Column(name = "UNIONID")
    private String unionid;

    @Column(name = "SUBSCRIBE_TIME")
    private Date subscribeTime;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "GROUPID")
    private String groupid;

    @Column(name = "TAGID_LIST")
    private String tagidList;

    @Column(name = "SUBSCRIBE_SCENE")
    private String subscribeScene;

    @Column(name = "QR_SCENE")
    private String qrScene;

    @Column(name = "QR_SCENE_STR")
    private String qrSceneStr;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return SUBSCRIBE
     */
    public Integer getSubscribe() {
        return subscribe;
    }

    /**
     * @param subscribe
     */
    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    /**
     * @return OPENID
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * @return NICKNAME
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * @return SEX
     */
    public Integer getSex() {
        return sex;
    }

    /**
     * @param sex
     */
    public void setSex(Integer sex) {
        this.sex = sex;
    }

    /**
     * @return PROVINCE
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * @return CITY
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * @return COUNTRY
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * @return HEADIMGURL
     */
    public String getHeadimgurl() {
        return headimgurl;
    }

    /**
     * @param headimgurl
     */
    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl == null ? null : headimgurl.trim();
    }

    /**
     * @return PRIVILEGE
     */
    public String getPrivilege() {
        return privilege;
    }

    /**
     * @param privilege
     */
    public void setPrivilege(String privilege) {
        this.privilege = privilege == null ? null : privilege.trim();
    }

    /**
     * @return UNIONID
     */
    public String getUnionid() {
        return unionid;
    }

    /**
     * @param unionid
     */
    public void setUnionid(String unionid) {
        this.unionid = unionid == null ? null : unionid.trim();
    }

    /**
     * @return SUBSCRIBE_TIME
     */
    public Date getSubscribeTime() {
        return subscribeTime;
    }

    /**
     * @param subscribeTime
     */
    public void setSubscribeTime(Date subscribeTime) {
        this.subscribeTime = subscribeTime;
    }

    /**
     * @return REMARK
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * @return GROUPID
     */
    public String getGroupid() {
        return groupid;
    }

    /**
     * @param groupid
     */
    public void setGroupid(String groupid) {
        this.groupid = groupid == null ? null : groupid.trim();
    }

    /**
     * @return TAGID_LIST
     */
    public String getTagidList() {
        return tagidList;
    }

    /**
     * @param tagidList
     */
    public void setTagidList(String tagidList) {
        this.tagidList = tagidList == null ? null : tagidList.trim();
    }

    /**
     * @return SUBSCRIBE_SCENE
     */
    public String getSubscribeScene() {
        return subscribeScene;
    }

    /**
     * @param subscribeScene
     */
    public void setSubscribeScene(String subscribeScene) {
        this.subscribeScene = subscribeScene == null ? null : subscribeScene.trim();
    }

    /**
     * @return QR_SCENE
     */
    public String getQrScene() {
        return qrScene;
    }

    /**
     * @param qrScene
     */
    public void setQrScene(String qrScene) {
        this.qrScene = qrScene == null ? null : qrScene.trim();
    }

    /**
     * @return QR_SCENE_STR
     */
    public String getQrSceneStr() {
        return qrSceneStr;
    }

    /**
     * @param qrSceneStr
     */
    public void setQrSceneStr(String qrSceneStr) {
        this.qrSceneStr = qrSceneStr == null ? null : qrSceneStr.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", subscribe=").append(subscribe);
        sb.append(", openid=").append(openid);
        sb.append(", nickname=").append(nickname);
        sb.append(", sex=").append(sex);
        sb.append(", province=").append(province);
        sb.append(", city=").append(city);
        sb.append(", country=").append(country);
        sb.append(", headimgurl=").append(headimgurl);
        sb.append(", privilege=").append(privilege);
        sb.append(", unionid=").append(unionid);
        sb.append(", subscribeTime=").append(subscribeTime);
        sb.append(", remark=").append(remark);
        sb.append(", groupid=").append(groupid);
        sb.append(", tagidList=").append(tagidList);
        sb.append(", subscribeScene=").append(subscribeScene);
        sb.append(", qrScene=").append(qrScene);
        sb.append(", qrSceneStr=").append(qrSceneStr);
        sb.append("]");
        return sb.toString();
    }
}