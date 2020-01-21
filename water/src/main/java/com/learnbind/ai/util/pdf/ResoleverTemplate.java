package com.learnbind.ai.util.pdf;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

public class ResoleverTemplate {
	/** 
	 *	@Title resoleverTemplate 
	 *	@Description 采用thymeleaf模块解析器生成HTML
	 *				 HTML+DATA
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月19日 下午3:13:37
	*/
	/** 
	 *	@Title resoleverTemplate 
	 *	@Description 采用thymeleaf模块解析器生成HTML
	 * 
	 *	@param templateFileName		瓶签模板文件名称
	 *	@param contextData			业务数据(上下文数据).
	 *	@param htmlFileName			生成的HTML文件名称
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月20日 下午3:16:07
	*/
	public void resoleverTemplate(String templateFileName,Map<String,Object> contextData,  String htmlFileName) {
		//获取生成PDF文件路径
		final String FILE_DIR="templates/bottlelabel/labeltemplate/";
		//获取文件名称
		final String SUFFIX=".html";
		
		//(1)构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        //模板所在目录，相对于当前classloader的classpath。
        resolver.setPrefix(FILE_DIR);
        //模板文件后缀
        resolver.setSuffix(SUFFIX);
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //(2)构造上下文(Model)
        Context context = buildContext(contextData);        

        //(3)渲染模板
        FileWriter write;
		try {
			//write = new FileWriter("src/main/resources/templates/labeltemplate/result.html");
			//write = new FileWriter("d:/temp/testpdf/result.html");
			write = new FileWriter(htmlFileName);
			templateEngine.process(templateFileName, context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
	/** 
	 *	@Title setContext 
	 *	@Description 设置上下文数据
	 * 
	 *	@param contextData	业务数据(HashMap<String,Object>)
	 *
	 *	@return			   	上下文对象     
	 *	@Return Context    	返回类型 
	 *
	 *	@Date 2018年12月20日 下午3:22:41
	*/
	private Context buildContext(Map<String,Object> contextData) {
		Context context = new Context();
		
		//数据的传递: 示例-以下内容需要进一步重构 
        //context.setVariable("name", "蔬菜列表");
        //context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});
		context.setVariable("context", contextData);
        return context;
	}
	
	public void resoleverTemplate1(String templateFileName,Map<String,Object> contextData,  String htmlFileName) {
		final String FILE_DIR="templates/bottlelabel/labeltemplate/";
		final String SUFFIX=".html";
		
		//(1)构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
		//SpringResourceTemplateResolver resolver=new SpringResourceTemplateResolver();
		//ServletContextTemplateResolver resolver=new ServletContextTemplateResolver();
        
        resolver.setPrefix(FILE_DIR);  //模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(SUFFIX);  //模板文件后缀
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //(2)构造上下文(Model)
        Context context = buildContext(contextData);        

        //(3)渲染模板
        FileWriter write;
		try {
			//采用绝对路径或是src/main......路径.
			write = new FileWriter("src/main/resources/templates/labeltemplate/labelTemplate.html");
			//write = new FileWriter("d:/temp/testpdf/result.html");
			//write = new FileWriter(htmlFileName);
			templateEngine.process(templateFileName, context, write);
		} catch (IOException e) {
			e.printStackTrace();
		}        
	}
	
}
