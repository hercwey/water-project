package com.learnbind.ai.listener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.listener
 *
 * @Title: ImplDisposableBean.java
 * @Description: 监听应用关系事件
 *
 * @author Administrator
 * @date 2019年8月11日 上午11:40:45
 * @version V1.0 
 *
 */
@Component
public class ImplDisposableBean implements DisposableBean, ExitCodeGenerator {

	private final Log log = LogFactory.getLog(getClass());//日志
	
    @Override
    public int getExitCode() {
        return 5;
    }

    @Override
    public void destroy() throws Exception {
        log.info("<<<<<<<<<<<	我被销毁了......	>>>>>>>>>>>>>>>");
        log.info("<<<<<<<<<<<	我被销毁了......	>>>>>>>>>>>>>>>");
    }

}
