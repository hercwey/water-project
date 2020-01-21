package com.learnbind.ai.ccb.sdk.rsa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.learnbind.ai.common.DateUtil;
import com.learnbind.ai.common.util.fileutil.DateUtils;


/**
 * 此class暂时未使用
 * 用于解释服务器端对数据的解释
 * @author lenovo
 *
 */
public class PublicKeyServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String logName="xml";
	private static final Log log = LogFactory.getLog(logName);
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
    	log.info(" ========= 交易开始 =========");
    	long tradeBeginTime=System.currentTimeMillis();
    	//this.doPost(req, resp); 
    	String sessionId=req.getSession().getId();
    	
    	// 1、获取sessionid
    	log.debug("get sessionId:"+sessionId);
    	
    	 //HttpSession session = req.getSession();
    	// 2、获取数据
    	String reciveData = req.getQueryString();
    	log.debug("接收数据:"+reciveData);
    	//获取数据,之后返回数据：
    	String returnData ="";
    	String type = req.getParameter("type");
    	log.debug("type="+type);
    	
    	String desKey = "88888888";
    	String returnCode = "000000";
    	String rsaPub="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9tZlp5zXzRC4402BZpBN6lEVs3LVml/RqO1bQZlweTCL9MtvJhp15mWNHc2GaRp1CoRX5OWZwyAY/gWIxKyuWNXWhyQ5KlMDogx87m3DmeAt0vtNbEax8MMFEg4L+qw3+v8Cs+jFh5oyzJG6l05CTCKpaK6QcxDlvQ+Oot1tCJm3MhmEQBgx73lVawgroKribMnS35WQ6iEmQsQA/AJ98vjRWafA5B7rKhwG2rklNpgfE+wjVaCIR6rFIAm5z9lontZcK0hiYjXhJcg373uuDuFC85VzAdCirZkZLDOWI+kdbLxU/AyBS8HfwRo+p3R8I5XwDZ9Wm6B3HqNGqGnhQIDAQAB";
    	String cipherPaddingMode = "DES/ECB/NoPadding";
    	
    	if("des".equals(type)){
    		returnData =returnCode+desKey;
    	}else if("pub".equals(type)){
    		byte[] bytes;
			try {
				bytes = KeyUtil.DESEncrypt(rsaPub.getBytes(), desKey.getBytes(), cipherPaddingMode);
				returnData = returnCode+StringUtil.bytes2hex(bytes);
			} catch (MyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		
    	}
    	
    	/*AsynNoticeService asy = new AsynNoticeServiceImpl();*/
    	//returnData = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAp9tZlp5zXzRC4402BZpBN6lEVs3LVml/RqO1bQZlweTCL9MtvJhp15mWNHc2GaRp1CoRX5OWZwyAY/gWIxKyuWNXWhyQ5KlMDogx87m3DmeAt0vtNbEax8MMFEg4L+qw3+v8Cs+jFh5oyzJG6l05CTCKpaK6QcxDlvQ+Oot1tCJm3MhmEQBgx73lVawgroKribMnS35WQ6iEmQsQA/AJ98vjRWafA5B7rKhwG2rklNpgfE+wjVaCIR6rFIAm5z9lontZcK0hiYjXhJcg373uuDuFC85VzAdCirZkZLDOWI+kdbLxU/AyBS8HfwRo+p3R8I5XwDZ9Wm6B3HqNGqGnhQIDAQAB";
		/*try {
			returnData = asy.doBuniess(reciveData);
		} catch (MyException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	//returnData=""+returnData;
        // 5、返回数据
      //返回json串到终端
        
     	resp.addHeader("Access-Control-Allow-Origin","*");
     	resp.setContentType("text/html");
    	PrintWriter pw=resp.getWriter();
    	
    	//String rehexString=DataChange.HexStringToHexString(jsonStr.getBytes());
    	log.debug(returnData);
    	pw.println(returnData);
    	pw.close();
		/*resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<p>src=");
		out.println("");
		out.println("</p>");

		out.println("<p>sign=");
		out.println("");
		out.println("</p>");
		
		out.println("<p>pubkey=");
		out.println("");
		out.println("</p>");
		
		out.println("<p>result=");
		out.println("");
		out.println("</p>");
		
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();*/
    	
    	long tradeEndTime = System.currentTimeMillis();
		log.info("交易执行成功，共耗时["+(tradeEndTime - tradeBeginTime)+"]毫秒");
    	log.info(" ========= 交易结束 =========");
    }
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
    		throws ServletException, IOException {
        log.info(" ========= 交易开始 =========");
    	long tradeBeginTime=System.currentTimeMillis();
    	//this.doPost(req, resp); 
    	String sessionId=req.getSession().getId();
    	
    	// 1、获取sessionid
    	log.debug("get sessionId:"+sessionId);
    	 //HttpSession session = req.getSession();
    	// 2、获取数据
    	String line="";
    	StringBuffer sb =new StringBuffer();
    	BufferedReader br = new BufferedReader(new InputStreamReader((ServletInputStream) req.getInputStream()));                        
        while ((line = br.readLine()) != null) { 
        	//log.debug(line);
            sb.append(line);  
        }
        
    	String reciveData = sb.toString();
    	
    	
    	log.debug("接收数据:"+reciveData);
    	/*Map map = new HashMap();
        Enumeration paramNames = req.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = (String) paramNames.nextElement();
            String[] paramValues = req.getParameterValues(paramName);
            if (paramValues.length == 1) {
                String paramValue = paramValues[0];
                if (paramValue.length() != 0) {
                	log.debug(paramName+"="+paramValue);
                    map.put(paramName, paramValue);
                }
            }
        }
        String type = req.getParameter("type");
    	log.debug("type="+type);*/
        //return map;
    	//获取数据,之后返回数据：
    	String returnData ="";
    	Map<String, String> map = new HashMap<String, String>();
    	String[] str = reciveData.split("\\&");
    	for (int i = 0; i < str.length; i++) {
			String string = str[i];
			String[] mid = string.split("=");
			if(mid.length ==2){
				map.put(mid[0], mid[1]);
				log.debug(mid[0]+"="+mid[1]);
			}else{
				map.put(mid[0], "");
				log.debug(mid[0]+"="+"");
			}
			
		}
    	String type=map.get("type");
    	String desHexKey = "";
    	String returnCode = "000000";
    	String rsaPub="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZo5MObBDOMY+CjDRrYfdle6FXLCVi3VYFYrbTnqTJOT04VeIe07pLUwzC3PjBd4Qp36waD+Y4TpMCXxvbCi/32gxxWG8dloAuQDllJLC3jbnpO7np+xmSuYcGrusjAwhH5nNwWeB5TnWU2/SQXhQEJ5vquEE5sIfV1YPWIb79iwIDAQAB";
    	
    	//String cipherPaddingMode = "DES/ECB/NoPadding";
    	String cipherPaddingMode = "DES/ECB/PKCS5Padding";
    	//String  CCBDF_CustNO = "973850458304234973";
    	//CCBDF_ChannelNO=HE13000009021490101
    	String CCBDF_CustNO = "9021490101";
    	//String date = DateUtil.getDateTime("yyMMdd");
    	String date=DateUtil.format(new Date(), "yyMMdd");  //获取当前日期
    	
    	
    	desHexKey=CCBDF_CustNO+date;
    	
    	log.debug("des 十六进制密钥数据[企业客户号+交换当日日期(YYMMDD 6 位)]:"+desHexKey);
    	log.debug("des 十六进制密钥长度:"+desHexKey.length());
    	log.debug("des 密钥转换为字节数组开始");
    	byte[] bytedesKey = asc2bin(desHexKey);
    	log.debug("des 密钥转换为字节数组结束byte字节长度"+bytedesKey.length);
    	
		
		log.debug("des 加密填充模式"+cipherPaddingMode);
		
		log.debug("rsa 公钥长度:"+rsaPub.length());
		byte[] nobase64 = StringUtil.Base64Decrypt(rsaPub);
		log.debug("rsa 公钥base64解码后长度:"+nobase64.length);
		byte[] bytes=null;
		byte[] ccbbytes=null;
		
		/*
		 * try { //TODO 此处进行的解密操作. bytes = KeyUtil.CCBDESEncrypt(nobase64, bytedesKey,
		 * cipherPaddingMode);
		 * 
		 * //ccbbytes = KeyUtil.DESEncrypt(nobase64, bytedesKey, cipherPaddingMode); }
		 * catch (MyException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */
		//log.debug("ccb 提供des 加密后字节数组长度:"+ccbbytes.length);
		log.debug("des 加密后字节数组长度:"+bytes.length);
		String hexResult = StringUtil.bytes2hex(bytes);
		//String base64 = StringUtil.Base64Encrypt(bytes);
		log.debug("加密后数据十六进制:"+hexResult);
		/*log.debug("加密后数据十六进制:"+hexResult);
		log.debug("加密后数据十六进制长度:"+hexResult.length());
		log.debug("加密后数据base64编码:"+base64);
		log.debug("加密后数据base64编码长度:"+base64.length());*/
		byte[] returnbyte= null;
    	if("pub".equals(type)){
    		//returnData = returnCode+hexResult;
    		//TODO commented by hz 2019/07/10
    		//returnbyte=StringUtil.compareByte(returnCode.getBytes(), bytes);
    	}
    	log.debug("返回给银行:"+new String(returnbyte));
    	log.debug("返回给银行,字节数组长度:"+returnbyte.length);
        
     	resp.addHeader("Access-Control-Allow-Origin","*");
     	resp.setContentType("text/html");
    	
    	ServletOutputStream out = resp.getOutputStream();
    	
    	//String rehexString=DataChange.HexStringToHexString(jsonStr.getBytes());
    	//log.debug("发送数据:"+returnData);
    	out.write(returnbyte);
    	out.close();
    	//pw.p
    	/*PrintWriter pw=resp.getWriter();
    	pw.println(returnData);
    	pw.close();*/
    	
    	/*resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.println("<p>src=");
		out.println("");
		out.println("</p>");

		out.println("<p>sign=");
		out.println("");
		out.println("</p>");
		
		out.println("<p>pubkey=");
		out.println("");
		out.println("</p>");
		
		out.println("<p>result=");
		out.println("");
		out.println("</p>");
		
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();*/
    	long tradeEndTime = System.currentTimeMillis();
		log.info("交易执行成功，共耗时["+(tradeEndTime - tradeBeginTime)+"]毫秒");
    	log.info(" ========= 交易结束 =========");
    }
    @Override  
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {  
        super.doDelete(req, resp);  
    }  
  
    @Override  
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)  
            throws ServletException, IOException {  
        super.doPut(req, resp);  
    }
    /**
	* java 示例，其它语言可以参考修改
	* 将 16 进制字符串转换成 16 进制数字数组
	*
	* @param hexString DES 的明文
	* @return
	*/
	public  byte[] asc2bin(String hexString) {
		byte[] hexbyte = hexString.getBytes();
		byte[] bitmap = new byte[hexbyte.length / 2];
		for (int i = 0; i < bitmap.length; i++) {
			hexbyte[i * 2] -= hexbyte[i * 2] > '9' ? 7 : 0;
			hexbyte[i * 2 + 1] -= hexbyte[i * 2 + 1] > '9' ? 7 : 0;
			bitmap[i] = (byte) ((hexbyte[i * 2] << 4 & 0xf0) | (hexbyte[i * 2 + 1] & 0x0f));
		}
	return bitmap;
	}
} 
