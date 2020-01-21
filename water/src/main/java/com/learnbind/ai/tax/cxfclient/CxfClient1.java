package com.learnbind.ai.tax.cxfclient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.endpoint.ClientImpl;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingMessageInfo;
import org.apache.cxf.service.model.BindingOperationInfo;
import org.apache.cxf.service.model.MessagePartInfo;
import org.apache.cxf.service.model.ServiceInfo;

import com.alibaba.fastjson.JSON;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.test
 *
 * @Title: CxfClient1.java
 * @Description: 暂时先不考虑此CLASS.待所有的业务处理完毕后再加强性能.
 *
 * @author lenovo
 * @date 2019年10月15日 下午6:38:31
 * @version V1.0 
 *
 */
public class CxfClient1 {
	private static final  Map<String,Endpoint>  factoryMap=new HashMap<>();
	private static final Map<String,Client> clientMap=new HashMap<>();
	
	/**
	 *
	 * @param wsdlUrl  wsdl的地址：http://localhost:8001/demo/HelloServiceDemoUrl?wsdl
	 * @param methodName 调用的方法名称 selectOrderInfo			
	 * @param targetNamespace 命名空间 http://service.limp.com/  ???
	 * @param name  name HelloServiceDemo
	 * @param paramList 参数集合
	 * @throws Exception
	 */
	public  static String dynamicCallWebServiceByCXF(String wsdlUrl,String methodName,
													String targetNamespace,String name,
													List<Object> paramList)throws Exception{
		//临时增加缓存，增加创建速度
		if(!factoryMap.containsKey(methodName)){
			// 创建动态客户端
			JaxWsDynamicClientFactory factory = JaxWsDynamicClientFactory.newInstance();
			// 创建客户端连接
			Client client = factory.createClient(wsdlUrl);
			ClientImpl clientImpl = (ClientImpl) client;
			Endpoint endpoint = clientImpl.getEndpoint();
			factoryMap.put(methodName,endpoint);
			clientMap.put(methodName,client);
			System.out.println("初始化");
		}
		//从缓存中换取 endpoint、client
		Endpoint endpoint=factoryMap.get(methodName);
		Client client=clientMap.get(methodName);
		// Make use of CXF service model to introspect the existing WSDL
		ServiceInfo serviceInfo = endpoint.getService().getServiceInfos().get(0);
		// 创建QName来指定NameSpace和要调用的service
		String localPart=name+"SoapBinding";
		QName bindingName = new QName(targetNamespace, localPart);
		BindingInfo binding = serviceInfo.getBinding(bindingName);

		//创建QName来指定NameSpace和要调用的方法绑定方法
		QName opName = new QName(targetNamespace, methodName);//selectOrderInfo

		BindingOperationInfo boi = binding.getOperation(opName);
//		BindingMessageInfo inputMessageInfo = boi.getInput();
		BindingMessageInfo inputMessageInfo = null;
		if (!boi.isUnwrapped()) {
			//OrderProcess uses document literal wrapped style.
			inputMessageInfo = boi.getWrappedOperation().getInput();
		} else {
			inputMessageInfo = boi.getUnwrappedOperation().getInput();
		}

		List<MessagePartInfo> parts = inputMessageInfo.getMessageParts();

		/***********************以下是初始化参数，组装参数；处理返回结果的过程******************************************/
		Object[] parameters = new Object[parts.size()];
		for(int m=0;m<parts.size();m++){
			MessagePartInfo  part=parts.get(m);
			// 取得对象实例
			Class<?> partClass = part.getTypeClass();//OrderInfo.class;
			System.out.println(partClass.getCanonicalName()); // GetAgentDetails
			//实例化对象
			Object initDomain=null;
			//普通参数的形参，不需要fastJson转换直接赋值即可
			if("java.lang.String".equalsIgnoreCase(partClass.getCanonicalName())
					||"int".equalsIgnoreCase(partClass.getCanonicalName())){
				initDomain=paramList.get(m).toString();
			}
			//如果是数组
			else if(partClass.getCanonicalName().indexOf("[]")>-1){
				//转换数组
				initDomain=JSON.parseArray(paramList.get(m).toString(),partClass.getComponentType());
			}else{
				initDomain=JSON.parseObject(paramList.get(m).toString(),partClass);
			}
			parameters[m]=initDomain;

		}
		//定义返回结果集
		Object[] result=null;
	 	//普通参数情况 || 对象参数情况  1个参数 ||ArryList集合
			try {
				result = client.invoke(opName,parameters);
			}catch (Exception ex){
				ex.printStackTrace();
				return "参数异常"+ex.getMessage();
			}
		//返回调用结果
		if(result.length>0){
			return  JSON.toJSON(result[0]).toString();
		}
		return  "invoke success, but is void ";
	}
	
	//
	/*
	 * System.out.println(dynamicCallWebServiceByCXF(
	 * 	wsdlUrl,
	 * "sayHello2",
	 * "http://service.limp.com/",
	 * "HelloServiceDemo",
	 * listParam1)
	 * );
	 */
	
	public static final void  main(String[] args) {
		String url = "http://192.168.55.1:8080/TaxKPServer/services/KpWebserviceC?wsdl";
		String method = "queryInventory";
		String parms="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<sid>0</sid>\r\n" + 
				"	<ip>192.168.52.117</ip>\r\n" + 
				"	<port>30001</port>\r\n" + 
				"	<data count=\"1\">\r\n" +				
				"		<record>\r\n" + 
				"			<FPZL>2</FPZL>\r\n" + 
				"		</record>\r\n" +
				"	</data>\r\n" + 
				"</service>\r\n"; 
		
		List<Object> listParam=new ArrayList<>();
		listParam.add(parms);
		try {
			String response=CxfClient1.dynamicCallWebServiceByCXF(url,method,
						"http://taxkpserver.webservice.heb.aisino.com/",
						"KpWebserviceCImplPort",listParam);
			
			System.out.println("web service response is:"+response);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	

}
