package com.learnbind.ai.jsengine;

import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.stereotype.Service;

/**
 * 
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.jsengine
 *
 * @Title: JSEngineUtils.java
 * @Description: 执行JS SCRIPT脚本工具类
 *
 * @author lenovo
 * @date 2019年9月27日 下午2:46:11
 * @version V1.0 
 *
 */
@Service
public class JSEngineUtils {
	private ScriptEngineManager manager = null;  //脚本引擎管理器
    private ScriptEngine engine;  //javascript脚本引擎
    private String fileName;  //脚本文件
    
    /**
     * 
     * @Title: JSEngineUtils
     * @Description: 默认构造函数
     * 		(1)初始脚本引擎管理器,
     * 		(2)初始化javascript脚本引擎
     */
    public  JSEngineUtils() {    	
    	manager = new ScriptEngineManager();   
    	engine = manager.getEngineByName("JavaScript");
    }
    
    /**
     * 
     * 	创建一个新的实例 JSEngineUtils.
     * @param fileName  脚本文件名称,在此文件中可以写入需要执行的脚本
     */
    public JSEngineUtils(String fileName){
    	manager = new ScriptEngineManager();   
    	engine = manager.getEngineByName("JavaScript");
        this.fileName = fileName;
    }
    
    /**
     * 
     * @Title: setVar
     * @Description: 设置变量
     * @param varName
     * @param obj
     */
    public void setVar(String varName, Object obj){
        engine.put(varName, obj);
    }
    
    /**
     * 
     * @Title: start
     * @Description: 执行指定的脚本
     * @param script  脚本
     * @return 返回内容Object 
     * @throws ScriptException
     */
    public Object start(String script) throws ScriptException{
        return engine.eval(script);
    }
    
    /**
     * 
     * @Title: start
     * @Description: 执行指定的脚本文件(构造函数设置的脚本文件)
     * @return
     * @throws FileNotFoundException
     * @throws ScriptException
     */
    public Object startFile() throws FileNotFoundException, ScriptException{
        return engine.eval(new FileReader(fileName));
    }
    
    /**
     * 
     * @Title: main
     * @Description: Test
     * @param args
     */
    static public void main(String[] args) {
    	JSEngineUtils jsEngine=new JSEngineUtils();
    	String script="1+2+3*4";
    	try {
			Integer result=(Integer)jsEngine.start(script);
			System.out.println("execute result is:"+result);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
}
