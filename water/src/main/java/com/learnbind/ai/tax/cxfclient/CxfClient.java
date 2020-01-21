package com.learnbind.ai.tax.cxfclient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.xml.namespace.QName;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.apache.cxf.service.model.BindingInfo;
import org.apache.cxf.service.model.BindingOperationInfo;

import com.learnbind.ai.controller.tax.TaxController;

//import com.learnbind.ai.util.HttpClientUtil;

public class CxfClient {
	private static Log log = LogFactory.getLog(CxfClient.class);
	public static void main(String[] args) throws Exception {
		String url = "http://192.168.55.1:8080/TaxKPServer/services/KpWebserviceC?wsdl";
		String method = "queryInventory";
		String parms="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<sid>0</sid>\r\n" + 
				"	<ip>192.168.52.171</ip>\r\n" + 
				"	<port>30001</port>\r\n" + 
				"	<data count=\"2\">\r\n" +
				"		<record>\r\n" + 
				"			<FPZL>0</FPZL>\r\n" + 
				"		</record>\r\n" +
				"		<record>\r\n" + 
				"			<FPZL>2</FPZL>\r\n" + 
				"		</record>\r\n" +
				"	</data>\r\n" + 
				"</service>\r\n"; 
		String parmsGBK="";
		
		byte[] bytes = parms.getBytes("utf-8"); 		  
		byte[] bytes2 = new String(bytes, "utf-8").getBytes("gbk");		
		String plain=new String(bytes2, "gbk");
		
		try {
			parmsGBK = URLEncoder.encode(parms, "GBK");
			System.out.println(parmsGBK);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
				
		Object[] parameters = new Object[]{plain};
		System.out.println(invokeRemoteMethod(url, method,parameters)[0]);
    }	
	
	public static Object[] invokeRemoteMethod(String url, String operation, Object[] parameters){        
        Object[] res = null;
        if (!url.endsWith("wsdl")) {
            url += "?wsdl";
        }
		try {
			JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();			
			org.apache.cxf.endpoint.Client client = dcf.createClient(url);
			// 处理webService接口和实现类namespace不同的情况，CXF动态客户端在处理此问题时，会报No operation was found
			// with the name的异常
			Endpoint endpoint = client.getEndpoint();
			QName opName = new QName(endpoint.getService().getName().getNamespaceURI(), operation);
			BindingInfo bindingInfo = endpoint.getEndpointInfo().getBinding();
			if (bindingInfo.getOperation(opName) == null) {
				for (BindingOperationInfo operationInfo : bindingInfo.getOperations()) {
					if (operation.equals(operationInfo.getName().getLocalPart())) {
						opName = operationInfo.getName();
						break;
					}
				}
			}

			res = client.invoke(opName, parameters);
			System.out.println("return data is" + res);
		} catch (Exception e) {
			//e.printStackTrace();
			log.debug("call web service error in cxf client!");
			log.debug(e.getMessage());
		}
		return res;
    }	


}
