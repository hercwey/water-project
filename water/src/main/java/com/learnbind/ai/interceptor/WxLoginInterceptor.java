package com.learnbind.ai.interceptor;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.learnbind.ai.common.util.fileutil.FileUploadUtil;
import com.learnbind.ai.constant.SessionConstant;
import com.learnbind.ai.model.WechatUser;
import com.learnbind.ai.service.wechat.cache.CacheManager;
import com.learnbind.ai.service.wechat.entity.Wechat;
import com.learnbind.ai.service.wechat.service.WechatService;
import com.learnbind.ai.util.wx.WxOauth2Utils;

/**
 * Class: loginInterceptor
 *   微信登录拦截器  
 * @author srd 
 * @version 1.0 $Date: 2016年12月22日 下午2:12:57
 */
@Component
public class WxLoginInterceptor implements HandlerInterceptor {  
  
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private WechatService wechatService;
	
    private List<String> allowUrls;// 还没发现可以直接配置不拦截的资源，所以在代码里面来排除  
  
    public void setAllowUrls(List<String> allowUrls) {  
        this.allowUrls = allowUrls;  
    }

	/**
	 * @author: srd $Date: 2016年12月22日
	 * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, java.lang.Exception)
	 */
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @author: srd $Date: 2016年12月22日
	 * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.web.servlet.ModelAndView)
	 */
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	/**
	 * @author: srd $Date: 2016年12月22日
	 * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
		String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");  
		log.debug("拦截URL: "+request.getRequestURL());
		
		if(allowPass(requestUrl)) {
			log.debug("放行的url is:"+requestUrl);
			return true;  //判定放行的url
		}
        
        log.debug("into interceptor");
        HttpSession session = request.getSession();
        
        //自Session获取微信用户对象
        //test: 待测试成功后,打开即可.        
		WechatUser user = (WechatUser)session.getAttribute(SessionConstant.USER);
		log.debug("interceptor中session用户："+user);
       
        //待测试完成后 屏蔽以下代码.
        //--------------test start------------------
//		 WechatUser user=new WechatUser();
//		 user.setOpenid("oTZW-5l5E6seMT42SbDkNNX5zSHQ");//Hz		 
		 //user.setOpenid("oTZW-5u_rE1n-2BqnDAOXiQjKmMI");//srd		 
//		 session.setAttribute(SessionConstant.USER, user);
//		 System.out.println("set session ok!!!!!--------------------");
		 //--------------test  end--------------------
		
        
		//如未获取到
        if (user == null) {  
        	log.debug("用户为空");    		
    		Wechat wechat=getWechatFromCache();  //自cache中获取微信公众号对象
    		if(wechat==null) return false;
    		try {
    			//"login"替换为原始请求.  requestUrl    			
    			// modified  at  2019/08/05
    			String url = WxOauth2Utils.getAuthorizeUrl(wechat.getAppid(), 
    													   FileUploadUtil.getReqServerURL(request)+"/login", 
    													   "snsapi_userinfo",    													   
    													   requestUrl);
    			log.debug("微信授权URL："+url);
    			response.sendRedirect(url);
    			return false;
    		} catch (UnsupportedEncodingException e) {
    			e.printStackTrace();
    			log.error("获取微信授权url异常，UnsupportedEncodingException");
    		} catch (Exception e){
    			e.printStackTrace();
    			log.error("获取微信授权url异常，Exception");
    		}
        }
        return true; // 返回true，则这个方面调用后会接着调用postHandle(), afterCompletion()  
	}
	
	/**
	 *   判定是否请允许通过
	 * @param requestUrl 请求的URL
	 * @return  如果请允许通过则返回true,否则返回false
	 */
	private boolean allowPass(String requestUrl) {
		boolean allow=false;
		if (null != allowUrls && allowUrls.size() >= 1) {  
	        for (String url : allowUrls) {  
	            if (requestUrl.contains(url)) {  
	                allow= true;  
	                break;
	            }  
	        }
	    }
		return allow;
	}
	
	/**
	 * 自Cache中获取微信公众号对象(Cache中存放的是JSON格式的公众号对象)
	 * @return 如果获取成功则返回对象,否则返回false;
	 */
	private Wechat getWechatFromCache() {
		JSONObject wechatJson = CacheManager.get(CacheManager.KEY_WECHAT);
		if(wechatJson==null){
			Wechat wechat = wechatService.getWechat();
			if(wechat!=null){
				wechatJson = JSON.parseObject(JSON.toJSONString(wechat));
				CacheManager.set(CacheManager.KEY_WECHAT, wechatJson);  
			}else{
				log.error("微信公众号信息为空");
				return null;
			}
		}
		Wechat wechat = JSON.toJavaObject(wechatJson, Wechat.class);
		return wechat;
	}
  
}  
