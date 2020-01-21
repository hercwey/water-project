package com.learnbind.ai.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 获取Spring注入的类，工具类
 * @author srd
 *
 */
@Component
public class SpringUtil implements ApplicationContextAware {

	private final Log log = LogFactory.getLog(getClass());//日志
	
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if(SpringUtil.applicationContext == null) {
            SpringUtil.applicationContext = applicationContext;
        }
        log.info("----------------------------------------------------------------------");
        log.info("----------	com.learnbind.pivas.common.util.SpringUtil	----------");
        log.info("----------	ApplicationContext配置成功	----------");
        log.info("----------	在普通类可以通过调用SpringUtils.getAppContext()获取ApplicationContext对象	----------");
        log.info("----------	ApplicationContext="+SpringUtil.applicationContext+"	----------");
        log.info("----------------------------------------------------------------------");
    }

    //获取applicationContext
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    //通过name获取 Bean.
    public static Object getBean(String name){
        return getApplicationContext().getBean(name);
    }

    //通过class获取Bean.
    public static <T> T getBean(Class<T> clazz){
        return getApplicationContext().getBean(clazz);
    }

    //通过name,以及Clazz返回指定的Bean
    public static <T> T getBean(String name,Class<T> clazz){
        return getApplicationContext().getBean(name, clazz);
    }

}
