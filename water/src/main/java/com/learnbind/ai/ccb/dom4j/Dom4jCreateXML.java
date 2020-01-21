package com.learnbind.ai.ccb.dom4j;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 
 * DOM4J打包XML测试
 * @author lenovo
 * test DOM4j
 *  
 */
public class Dom4jCreateXML {
	
	/**
	 * 生成一个XML,并写入文件
	 */
	public void testCreateXmlToFile() {
        //创建一个xml文档
        Document doc = DocumentHelper.createDocument();
        //向xml文件中添加注释
        doc.addComment("这里是注释");
        //创建一个名为students的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
        Element root = doc.addElement("students");
        //在root节点下创建一个名为student的节点
        Element stuEle = root.addElement("student");
        //给student节点添加属性
        stuEle.addAttribute("id", "101");
        //给student节点添加一个子节点
        Element nameEle = stuEle.addElement("name");
        //设置子节点的文本
        nameEle.setText("张三");
        //用于格式化xml内容和设置头部标签
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置xml文档的编码为utf-8
        format.setEncoding("utf-8");
        
        //String xml=doc.toString();
        //System.out.println("生成的XML内容为:"+xml);
        
        Writer out;
        try {
            //创建一个输出流对象
            out = new FileWriter("f://xml//new.xml");            
            //创建一个dom4j创建xml的对象
            XMLWriter writer = new XMLWriter(out, format);            
            //调用write方法将doc文档写到指定路径
            writer.write(doc);
            writer.close();
            System.out.print("生成XML文件成功");
        } catch (IOException e) {
            System.out.print("生成XML文件失败");
            e.printStackTrace();
        }
    }
	
	
	/**
	 * 生成一个XML并写入 StringWriter
	 * 可以通过此函数返回XML字符串.
	 */
	public void testCreateXmlToStream() {
        //创建一个xml文档
        Document doc = DocumentHelper.createDocument();
        //向xml文件中添加注释
        doc.addComment("这里是注释");
        //创建一个名为students的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
        Element root = doc.addElement("students");
        //在root节点下创建一个名为student的节点
        Element stuEle = root.addElement("student");
        //给student节点添加属性
        stuEle.addAttribute("id", "101");
        //给student节点添加一个子节点
        Element nameEle = stuEle.addElement("name");
        //设置子节点的文本
        nameEle.setText("张三");
        //用于格式化xml内容和设置头部标签
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置xml文档的编码为utf-8
        format.setEncoding("utf-8");
        
        //String xml=doc.toString();
        //System.out.println("生成的XML内容为:"+xml);
        
        StringWriter out;
        
        try {
            //创建一个输出流对象
            //out = new FileWriter("f://xml//new.xml");
        	out=new StringWriter();
            //创建一个dom4j创建xml的对象
            XMLWriter writer = new XMLWriter(out, format);            
            //调用write方法将doc文档写到指定路径
            writer.write(doc);
            writer.close();
            //System.out.print("生成XML文件成功");
            
            String resultXML=out.toString();
            System.out.println("结果XML:"+resultXML);            
            
            
        } catch (IOException e) {
            System.out.print("生成XML文件失败");
            e.printStackTrace();
        }
    }
    
	
	/**
	 * 生成一个XML并写入 StringWriter
	 * 可以通过此函数返回XML字符串.
	 * 测试生成一个请求签到的数据包(请求数据包)
	 * 测试通过.
	 */
	public void testCreateLoginXmlToStream() {
        //创建一个xml文档
        Document doc = DocumentHelper.createDocument();
        
        //向xml文件中添加注释
        doc.addComment("这里是注释");
        
        //创建一个名为students的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
        Element root = doc.addElement("Transaction");
        
        //在root节点下创建两个子节点,分别是 Transaction_Header  Transaction_Body
        Element header = root.addElement("Transaction_Header");        
        Element txCode=header.addElement("SYS_TX_CODE");
        Element msgLen=header.addElement("SYS_MSG_LEN");
        //add cdata
        txCode.addCDATA("P1OPME001");
        msgLen.addCDATA("0000001004");        
        
        //在body子节点下加入request子节点
        Element body = root.addElement("Transaction_Body");
        body.addElement("request");        
        
        OutputFormat format = OutputFormat.createPrettyPrint();        
        format.setEncoding("utf-8");  //设置xml文档的编码为utf-8   
        
        StringWriter out;  //输出流对象        
        try {
                        
        	out=new StringWriter(); //创建一个输出流对象            
            XMLWriter writer = new XMLWriter(out, format); //创建一个dom4j创建xml的对象            
            //调用write方法将doc文档写到输出流中
            writer.write(doc);
            writer.close();            
            
            String resultXML=out.toString();
            System.out.println("结果XML:"+resultXML);            
            
            
        } catch (IOException e) {
            System.out.print("生成XML文件失败");
            e.printStackTrace();
        }        
    }
	
	/**
	 * 对XML进行解析.
	 */
	private void parseDomTest() {
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?>" + 
				"<!--这里是注释-->" + 
				"<Transaction>" + 
				"  <Transaction_Header>\r\n" + 
				"    <SYS_TX_CODE><![CDATA[P1OPME001]]></SYS_TX_CODE>" + 
				"    <SYS_MSG_LEN><![CDATA[0000001004]]></SYS_MSG_LEN>" + 
				"  </Transaction_Header>" + 
				"  <Transaction_Body>" + 
				"    <request/>" + 
				"  </Transaction_Body>" + 
				"</Transaction>";
		try {

			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element root = doc.getRootElement(); // 获取根节点

			Element header = root.element("Transaction_Header");
			Element txCode = header.element("SYS_TX_CODE");
			String strx = txCode.getTextTrim();  //直接读取即可.
			System.out.println("cdata is:"+strx);

		} catch (DocumentException e) {
			System.out.println(e.getMessage());
		}		
		
	}
	
	
    public static void main(String[] args){
        Dom4jCreateXML xml = new Dom4jCreateXML();
        //xml.testCreateXmlToFile();
        //xml.testCreateLoginXmlToStream();
        xml.parseDomTest();
    }
	
}
