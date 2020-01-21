package com.learnbind.ai.service.wechat.entity;

/**
 * 微信公众号对象
 * @author lenovo
 *
 */
public class Wechat {
    /**
     * 自增ID
     */
    private Long wechatId;

    /**
     * 公众号的唯一标识
     */
    private String appid;

    /**
     * 公众号的appsecret
     */
    private String secret;
    
    /**
     * @Fields paykey：支付Key
     */
    private String paykey;

    /**
     * @Fields mchid：商户ID
     */
    private String mchid;

    /**
     * 返回类型，请填写code
     */
    private String responseType;

    /**
     * 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     */
    private String scope;

    /**
     * 不是必须，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    private String state;

    /**
     * 授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
     */
    private String redirectUri;

    /**
     * 从微信请求中获取，并做缓存
     */
    private String token;
    
    /**
     * @Fields payNotifyUrl:支付成功后Tencent支付服务器发送通知时调用的URL.
     */
    private String payNotifyUrl;
    
    public String getPayNotifyUrl() {
		return payNotifyUrl;
	}

	public void setPayNotifyUrl(String payNotifyUrl) {
		this.payNotifyUrl = payNotifyUrl;
	}

	/**
     * 获取自增ID
     *
     * @return wechat_id - 自增ID
     */
    public Long getWechatId() {
        return wechatId;
    }

    /**
     * 设置自增ID
     *
     * @param wechatId 自增ID
     */
    public void setWechatId(Long wechatId) {
        this.wechatId = wechatId;
    }

    /**
     * 获取公众号的唯一标识
     *
     * @return appid - 公众号的唯一标识
     */
    public String getAppid() {
        return appid;
    }

    /**
     * 设置公众号的唯一标识
     *
     * @param appid 公众号的唯一标识
     */
    public void setAppid(String appid) {
        this.appid = appid == null ? null : appid.trim();
    }

    /**
     * 获取公众号的appsecret
     *
     * @return secret - 公众号的appsecret
     */
    public String getSecret() {
        return secret;
    }

    /**
     * 设置公众号的appsecret
     *
     * @param secret 公众号的appsecret
     */
    public void setSecret(String secret) {
        this.secret = secret == null ? null : secret.trim();
    }

    /**
     * 获取返回类型，请填写code
     *
     * @return response_type - 返回类型，请填写code
     */
    public String getResponseType() {
        return responseType;
    }

    /**
     * 设置返回类型，请填写code
     *
     * @param responseType 返回类型，请填写code
     */
    public void setResponseType(String responseType) {
        this.responseType = responseType == null ? null : responseType.trim();
    }

    /**
     * 获取应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     *
     * @return scope - 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     */
    public String getScope() {
        return scope;
    }

    /**
     * 设置应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     *
     * @param scope 应用授权作用域，snsapi_base （不弹出授权页面，直接跳转，只能获取用户openid），snsapi_userinfo （弹出授权页面，可通过openid拿到昵称、性别、所在地。并且，即使在未关注的情况下，只要用户授权，也能获取其信息）
     */
    public void setScope(String scope) {
        this.scope = scope == null ? null : scope.trim();
    }

    /**
     * 获取不是必须，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     *
     * @return state - 不是必须，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    public String getState() {
        return state;
    }

    /**
     * 设置不是必须，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     *
     * @param state 不是必须，重定向后会带上state参数，开发者可以填写a-zA-Z0-9的参数值，最多128字节
     */
    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    /**
     * 获取授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
     *
     * @return redirect_uri - 授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
     */
    public String getRedirectUri() {
        return redirectUri;
    }

    /**
     * 设置授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
     *
     * @param redirectUri 授权后重定向的回调链接地址，请使用urlEncode对链接进行处理
     */
    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri == null ? null : redirectUri.trim();
    }

    /**
     * 获取从微信请求中获取，并做缓存
     *
     * @return token - 从微信请求中获取，并做缓存
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置从微信请求中获取，并做缓存
     *
     * @param token 从微信请求中获取，并做缓存
     */
    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }

    public String getPaykey() {
		return paykey;
	}

	public void setPaykey(String paykey) {
		this.paykey = paykey;
	}

	public String getMchid() {
		return mchid;
	}

	public void setMchid(String mchid) {
		this.mchid = mchid;
	}

	@Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", wechatId=").append(wechatId);
        sb.append(", appid=").append(appid);
        sb.append(", secret=").append(secret);
        sb.append(", responseType=").append(responseType);
        sb.append(", scope=").append(scope);
        sb.append(", state=").append(state);
        sb.append(", redirectUri=").append(redirectUri);
        sb.append(", token=").append(token);
        sb.append(", paykey=").append(paykey);
        sb.append(", mchid=").append(mchid);
        sb.append("]");
        return sb.toString();
    }
}