package com.learnbind.ai.ccb.dom4j;

//先加入dom4j.jar包 
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**   
* @Title: TestDom4j.java
* @Package 
* @Description: 解析xml字符串
* @author 无处不在
* @date 2012-11-20 下午05:14:05
* @version V1.0   
* 用测试DOM解析
*/
public class TestDom4j {

  public void readStringXml(String xml) {
      Document doc = null;
      try {

          // 读取并解析XML文档
          // SAXReader就是一个管道，用一个流的方式，把xml文件读出来
          // 
          // SAXReader reader = new SAXReader(); //User.hbm.xml表示你要解析的xml文档
          // Document document = reader.read(new File("User.hbm.xml"));
    	  
          // 下面的是通过解析xml字符串的
          doc = DocumentHelper.parseText(xml); //将字符串转为XML

          Element rootElt = doc.getRootElement(); // 获取根节点
          System.out.println("根节点：" + rootElt.getName()); // 拿到根节点的名称

          Iterator iter = rootElt.elementIterator("head"); // 获取根节点下的子节点head

          // 遍历head节点
          while (iter.hasNext()) {

              Element recordEle = (Element) iter.next();
              String title = recordEle.elementTextTrim("title"); // 拿到head节点下的子节点title值
              System.out.println("title:" + title);

              Iterator iters = recordEle.elementIterator("script"); // 获取子节点head下的子节点script

              // 遍历Header节点下的Response节点
              while (iters.hasNext()) {

                  Element itemEle = (Element) iters.next();

                  String username = itemEle.elementTextTrim("username"); // 拿到head下的子节点script下的字节点username的值
                  String password = itemEle.elementTextTrim("password");

                  System.out.println("username:" + username);
                  System.out.println("password:" + password);
              }
          }
          Iterator iterss = rootElt.elementIterator("body"); ///获取根节点下的子节点body
          // 遍历body节点
          while (iterss.hasNext()) {

              Element recordEless = (Element) iterss.next();
              String result = recordEless.elementTextTrim("result"); // 拿到body节点下的子节点result值
              System.out.println("result:" + result);

              Iterator itersElIterator = recordEless.elementIterator("form"); // 获取子节点body下的子节点form
              // 遍历Header节点下的Response节点
              while (itersElIterator.hasNext()) {

                  Element itemEle = (Element) itersElIterator.next();

                  String banlce = itemEle.elementTextTrim("banlce"); // 拿到body下的子节点form下的字节点banlce的值
                  String subID = itemEle.elementTextTrim("subID");

                  System.out.println("banlce:" + banlce);
                  System.out.println("subID:" + subID);
              }
          }
      } catch (DocumentException e) {
          e.printStackTrace();

      } catch (Exception e) {
          e.printStackTrace();

      }
  }

  /**
   * @description 将xml字符串转换成map
   * @param xml
   * @return Map
   */
  public static Map readStringXmlOut(String xml) {
      Map map = new HashMap();
      Document doc = null;
      try {
          // 将字符串转为XML
          doc = DocumentHelper.parseText(xml); 
          // 获取根节点
          Element rootElt = doc.getRootElement(); 
          // 拿到根节点的名称
          System.out.println("根节点：" + rootElt.getName()); 

          // 获取根节点下的子节点head
          Iterator iter = rootElt.elementIterator("head"); 
          // 遍历head节点
          while (iter.hasNext()) {

              Element recordEle = (Element) iter.next();
              // 拿到head节点下的子节点title值
              String title = recordEle.elementTextTrim("title"); 
              System.out.println("title:" + title);
              map.put("title", title);
              // 获取子节点head下的子节点script
              Iterator iters = recordEle.elementIterator("script"); 
              // 遍历Header节点下的Response节点
              while (iters.hasNext()) {
                  Element itemEle = (Element) iters.next();
                  // 拿到head下的子节点script下的字节点username的值
                  String username = itemEle.elementTextTrim("username");
                  String password = itemEle.elementTextTrim("password");

                  System.out.println("username:" + username);
                  System.out.println("password:" + password);
                  map.put("username", username);
                  map.put("password", password);
              }
          }

          //获取根节点下的子节点body
          Iterator iterss = rootElt.elementIterator("body"); 
          // 遍历body节点
          while (iterss.hasNext()) {
              Element recordEless = (Element) iterss.next();
              // 拿到body节点下的子节点result值
              String result = recordEless.elementTextTrim("result"); 
              System.out.println("result:" + result);
              // 获取子节点body下的子节点form
              Iterator itersElIterator = recordEless.elementIterator("form"); 
              // 遍历Header节点下的Response节点
              while (itersElIterator.hasNext()) {
                  Element itemEle = (Element) itersElIterator.next();
                  // 拿到body下的子节点form下的字节点banlce的值
                  String banlce = itemEle.elementTextTrim("banlce"); 
                  String subID = itemEle.elementTextTrim("subID");

                  System.out.println("banlce:" + banlce);
                  System.out.println("subID:" + subID);
                  map.put("result", result);
                  map.put("banlce", banlce);
                  map.put("subID", subID);
              }
          }
      } catch (DocumentException e) {
          e.printStackTrace();
      } catch (Exception e) {
          e.printStackTrace();
      }
      return map;
  }
  
  
  private static void testParse() {
	  // 下面是需要解析的xml字符串例子
      String xmlString = "<html>" +
    		  				"<head>" + 
    		  					"<title>dom4j解析一个例子</title>"+
    		  				    "<script>" + 
    		  						"<username>yangrong</username>" + 
    		  						"<password>123456</password>" + 
    		  					"</script>"	+ 
    		  				"</head>" + 
    		  				"<body>" + 
    		  					"<result>0</result>" + 
    		  					"<form>" + 
    		  						"<banlce>1000</banlce>" + 
    		  						"<subID>36242519880716</subID>" + 
    		  					"</form>" + 
    		  				"</body>" + 
    		  			"</html>";

      /*
       * Test2 test = new Test2(); test.readStringXml(xmlString);
       */
      Map map = readStringXmlOut(xmlString);
      Iterator iters = map.keySet().iterator();
      while (iters.hasNext()) {
          String key = iters.next().toString(); // 拿到键
          String val = map.get(key).toString(); // 拿到值
          System.out.println(key + "=" + val);
      }
  }
  
  /**
 * 
 * 测试数据包长度
 */
private static void testDomLen() {
	  String str="<?xml version='1.0' encoding='utf-8'?>\r\n" + 
	  		"<Transaction>\r\n" + 
	  		"    <Transaction_Header>\r\n" + 
	  		"        <SYS_TX_CODE><![CDATA[P1OPME001]]></SYS_TX_CODE>\r\n" + 
	  		"        <SYS_MSG_LEN><![CDATA[0000001004]]></SYS_MSG_LEN>\r\n" + 
	  		"        <SYS_REQ_TIME><![CDATA[20181009114504215]]></SYS_REQ_TIME>\r\n" + 
	  		"        <SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
	  		"        <TXN_DT><![CDATA[20181009]]></TXN_DT>\r\n" + 
	  		"        <TXN_TM><![CDATA[114504]]></TXN_TM>\r\n" + 
	  		"        <TXN_STFF_ID><![CDATA[333333]]></TXN_STFF_ID>\r\n" + 
	  		"        <MULTI_TENANCY_ID><![CDATA[CN000]]></MULTI_TENANCY_ID>\r\n" + 
	  		"        <LNG_ID><![CDATA[zh-cn]]></LNG_ID>\r\n" + 
	  		"        <REC_IN_PAGE><![CDATA[]]></REC_IN_PAGE>\r\n" + 
	  		"        <PAGE_JUMP><![CDATA[]]></PAGE_JUMP>\r\n" + 
	  		"        <STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
	  		"        <CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
	  		"        <IttParty_Jrnl_No><![CDATA[]]></IttParty_Jrnl_No>\r\n" + 
	  		"        <Txn_Itt_IP_Adr><![CDATA[121.28.4.58]]></Txn_Itt_IP_Adr>\r\n" + 
	  		"    </Transaction_Header>\r\n" + 
	  		"    <Transaction_Body>\r\n" + 
	  		"        <request/>\r\n" + 
	  		"    </Transaction_Body>\r\n" + 
	  		"</Transaction>";
	  System.out.println(str.length());
  }

  private static void testDomLen1() {
	  String str="<?xml version='1.0' encoding='utf-8'?>\r\n" + 
	  		"<Transaction>\r\n" + 
	  		"  <Transaction_Header>\r\n" + 
	  		"    <SYS_TX_CODE><![CDATA[P1OPME001]]></SYS_TX_CODE>\r\n" + 
	  		"    <SYS_MSG_LEN><![CDATA[0000000932]]></SYS_MSG_LEN>\r\n" + 
	  		"    <SYS_REQ_TIME><![CDATA[20190706020257402]]></SYS_REQ_TIME>\r\n" + 
	  		"    <SYS_TX_VRSN><![CDATA[01]]></SYS_TX_VRSN>\r\n" + 
	  		"    <TXN_DT><![CDATA[20190706]]></TXN_DT>\r\n" + 
	  		"    <TXN_TM><![CDATA[020257]]></TXN_TM>\r\n" + 
	  		"    <TXN_STFF_ID><![CDATA[333333]]></TXN_STFF_ID>\r\n" + 
	  		"    <MULTI_TENANCY_ID><![CDATA[CN000]]></MULTI_TENANCY_ID>\r\n" + 
	  		"    <LNG_ID><![CDATA[zh-cn]]></LNG_ID>\r\n" + 
	  		"    <REC_IN_PAGE><![CDATA[]]></REC_IN_PAGE>\r\n" + 
	  		"    <PAGE_JUMP><![CDATA[]]></PAGE_JUMP>\r\n" + 
	  		"    <STS_TRACE_ID><![CDATA[]]></STS_TRACE_ID>\r\n" + 
	  		"    <CHNL_CUST_NO><![CDATA[HE13000009021440001]]></CHNL_CUST_NO>\r\n" + 
	  		"    <IttParty_Jrnl_No><![CDATA[]]></IttParty_Jrnl_No>\r\n" + 
	  		"    <Txn_Itt_IP_Adr><![CDATA[192.168.1.123]]></Txn_Itt_IP_Adr>\r\n" + 
	  		"  </Transaction_Header>\r\n" + 
	  		"  <Transaction_Body>\r\n" + 
	  		"    <request/>\r\n" + 
	  		"  </Transaction_Body>\r\n" + 
	  		"</Transaction>";
	  System.out.println(str.length());
  }

  public static void main(String[] args) {
	  testParse();
  }

}
