package com.learnbind.ai.controller.wx;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.wechat.cache.CacheManager;
import com.learnbind.ai.service.wechat.entity.OAuthAccessToken;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechatuser.WechatUserService;
import com.learnbind.ai.util.wx.WxOauth2Utils;

/**
 * Class: LoginController
 * 		Controller类
 * @author srd 
 * @version 1.0 $Date: 2017年5月8日 下午10:16:02
 */
@Controller
public class WxLoginController {

	private static final Logger log = LoggerFactory.getLogger(WxLoginController.class);
	
	@Autowired	
	private WechatUserService wechatUserService;  //微信用户实体-服务  
	
	
	/**
	 * 获取微信授权url
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/goLogin")
	public String goLogin(HttpServletRequest request, HttpServletResponse response){
//		User user = sysUsers.selectByPrimaryKey(1l);
//		if(user!=null){
//			UserBean userBean = copyUserToBean(user);
//			request.getSession().setAttribute(SessionConstants.USER, userBean);
//			return "redirect:user/goIndex";
//		}
		return null;
	}
	@RequestMapping("/login")
	public ModelAndView login(HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		
		log.debug("into user controller");
		
		//存放回调函数中所传送的  原始请求之URI.
		String orginUrl=request.getParameter("state");	 //回调函数传递过来的参数(在拦截器设置)	
		String code = request.getParameter("code");      //回调函数传递过来的参数
		
		if(code==null){
			log.error("code为空");
			mav.setViewName("/500.jsp");
			return mav;
		}else{
			
			//(1)get token
			OAuthAccessToken token=getToken(code);  //get token
			
			//(2)search user from db,
			//	if exist, set it into session.
			//	if not exist,  send request to wechat server. save it into db.
			//根据state及用户登录,绑定关系确定  redirect url地址.
			WechatUser tempUser=searchWechatUser(token.getOpenid());			
			if(tempUser==null){
				//(3.1) 自微信服务器获取用户信息		
				tempUser=getMemberInfoFromWechatServer(token);
				if(tempUser!=null) {
					saveWechatUser(tempUser);  //(3.2) 添加用户--->DB
				}
				else {
					log.error("获取微信用户信息异常");
					mav.setViewName("/500.jsp");
					return mav;
				}
				
			}
			setUserIntoSession(request,tempUser);
			
			//根据state及用户登录,绑定关系确定  redirect url地址.
			mav.setViewName("redirect:"+orginUrl);
		}
		
		return mav;
	}
	
	/**
	 * 将微信用户加入到session中
	 * @param request  
	 * @param wechatUser 微信用户对象
	 */
	private void setUserIntoSession(HttpServletRequest request,WechatUser wechatUser) {
		HttpSession session = request.getSession();
		session.setAttribute(SessionConstant.USER, wechatUser);
		session.setMaxInactiveInterval(30*60);
		WechatUser user = (WechatUser)request.getSession().getAttribute(SessionConstant.USER);
		log.debug("wechat session用户："+user);
	}
	
	/**
	 * 根据openId查询微信用户(在数据库中)
	 * @param openId  微信用户的openId
	 * @return  如果查询到则返回相应的对象,否则返回null;
	 */
	private WechatUser searchWechatUser(String openId) {
		//(1)生成查询对象
		WechatUser searchObj=new WechatUser();
		searchObj.setOpenid(openId);
		
		//(2)根本查询对象的值进行查询
		WechatUser resultObj=wechatUserService.selectOne(searchObj);
		if(resultObj!=null)
			log.debug("根据openid（"+openId+"）查询微信用户信息 ： "+ resultObj.getNickname());
		else {
			log.debug("根据openid（"+openId+"）查询微信用户信息 ： 未查询到此用户" );
		}
		
		//(3)返回查询结果
		return resultObj;
	}
	
	/**
	 * 自微信服务器获取微信用户信息
	 * @param token 
	 * @return  微信用户
	 */
	private WechatUser getMemberInfoFromWechatServer(OAuthAccessToken token) {
		JSONObject json = WxOauth2Utils.getMemberInfo(token.getAccess_token(), token.getOpenid());
		log.debug("微信用户："+json);
		return JSON.toJavaObject(json, WechatUser.class);
	}
	
	/**
	 * 保存微信客户信息--->DB
	 * @param wechatUser  微信客户实体
	 * @return  行数
	 */
	private int saveWechatUser(WechatUser wechatUser) {
		wechatUser.setNickname(filterEmoji(wechatUser.getNickname()));
		int rows= wechatUserService.insertSelective(wechatUser);
		if(rows>0){
			log.debug("保存微信用户信息成功");
		}else{
			log.error("保存微信用户信息异常");
		}
		return rows;
	}
	
	
	/**
	 * 获取 token 
	 * @param code
	 * @return
	 */
	private OAuthAccessToken getToken(String code) {
		Wechat wechat = JSON.toJavaObject(CacheManager.get(CacheManager.KEY_WECHAT), Wechat.class);
		log.debug("Wechat : "+wechat);
		OAuthAccessToken token = JSON.toJavaObject(WxOauth2Utils.getAccessToken(wechat.getAppid(), wechat.getSecret(), code), OAuthAccessToken.class);
		log.debug("OAuthAccessToken : "+token);
		return token;
	}
	
	
	
	/**
	 * 过滤emoji符号
	 * @param source
	 * @return
	 */
	public String filterEmoji(String source) { 
        if(source != null){
            Pattern emoji = Pattern.compile ("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",Pattern.UNICODE_CASE | Pattern . CASE_INSENSITIVE ) ;
            Matcher emojiMatcher = emoji.matcher(source);
            if ( emojiMatcher.find()){
                source = emojiMatcher.replaceAll("*");
                return source ;
            }
            return source;
        }
        return source; 
    }
	
	/**
	 * 拷贝User到UserBean
	 * @param user
	 * @return
	 */
//	private UserBean copyUserToBean(User user){
//		//return new UserBean(user.getUserId(), user.getOpenid(), user.getNickname(), user.getSex(), user.getProvince(), user.getCity(), user.getCountry(), user.getHeadimgurl(), user.getPrivilege(), user.getUnionid(), user.getScore());
//		return null;
//	}
	
}
