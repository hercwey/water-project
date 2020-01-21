package com.learnbind.ai.tax.processor;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.github.pagehelper.util.StringUtil;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.tax.packet.common.PublicPart;
import com.learnbind.ai.tax.packet.common.RootPart;
import com.learnbind.ai.tax.packet.request.BatchQueryCond;
import com.learnbind.ai.tax.packet.request.Invoice;
import com.learnbind.ai.tax.packet.request.InvoiceDetail;
import com.learnbind.ai.tax.packet.request.InvoiceEmpty;
import com.learnbind.ai.tax.packet.request.InvoiceHead;
import com.learnbind.ai.tax.packet.request.InvoiceInvalid;
import com.learnbind.ai.tax.packet.request.InvoiceListPrint;
import com.learnbind.ai.tax.packet.request.InvoicePrint;
import com.learnbind.ai.tax.packet.request.InvoiceRepair;
import com.learnbind.ai.tax.packet.request.InvoiceSingle;
import com.learnbind.ai.tax.packet.request.InvoiceUpload;
import com.learnbind.ai.tax.packet.request.IssueControlRequest;
import com.learnbind.ai.tax.packet.request.PrintParam;
import com.learnbind.ai.tax.packet.request.RedInfo;
import com.learnbind.ai.tax.packet.request.RedInfoDetail;
import com.learnbind.ai.tax.packet.request.RedInfoDownload;
import com.learnbind.ai.tax.packet.request.RedInfoHead;
import com.learnbind.ai.tax.packet.request.TaxSIDCode;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.processor
 *
 * @Title: TaxDownPacketProcessor.java
 * @Description: 航天税务开票下行报文生成器.
 *
 * @author lenovo
 * @date 2019年10月18日 下午1:16:53
 * @version V1.0 
 *
 */
public class TaxDownPacketProcessor {
	/**
	 * @Title: genDocument
	 * @Description: 生成文档对象
	 * @param commentList 文档注释列表(用于生成报文的注释)
	 * @return 返回所生成的文档对象
	 */
	private static Document genDocument(List<String> commentList) {
		Document doc = DocumentHelper.createDocument();
		if (commentList != null) {
			for (String comment : commentList) {
				doc.addComment(comment); // 向xml文件中添加注释
			}
		}

		return doc;
	}

	/**
	 * @Title: genDocRoot
	 * @Description: 根据RootPart来生成root node
	 * @param doc  文档对象
	 * @return	root node 
	 */
	private static Element genDocRoot(Document doc) {
		Element root = null;

		RootPart rootPart = new RootPart(); // root对象
		List<Map<String, Object>> list = EntityUtils.entityToListMap(rootPart);
		if (list != null) {
			Map<String, Object> tempMap = list.get(0);
			for (String key : tempMap.keySet()) {
				// 得到消息长度元素引用.
				root = doc.addElement(key);
				break;
			}
		}
		return root;
	}

	/**
	 * @Title: packPublicPart
	 * @Description: 打包公共部分
	 * @param root  root node
	 * @param sid 参见TaxSIDCode
	 */
	private static void packPublicPart(Element root, String sid) {
		PublicPart publicPart = new PublicPart();
		publicPart.setSid(sid);
		List<Map<String, Object>> list = EntityUtils.entityToListMap(publicPart);
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				Element elem = root.addElement(key);
				// elem.addCDATA((String) map.get(key));
				elem.addText((String) map.get(key));
			}
		}
	}
	
	/**
	 * @Title: genDataElement
	 * @Description: 生成数据节点
	 * @param root  根结点
	 * @param count data节点下record元素个数
	 * @return 
	 * 		data node
	 */
	private static Element genDataElement(Element root,int count) {
		Element dataElement=root.addElement("data");
		if(count>0) {
			dataElement.addAttribute("count", Integer.toString(count));
		}
		return dataElement;		
	}	
	
	/**
	 * @param doc Document对象(DOM4J)
	 * @return
	 * 		生成XML字符串 (utf-8编码)
	 */
	private static String createXMLStr(Document doc) {
		OutputFormat format = OutputFormat.createPrettyPrint();
		format.setEncoding("utf-8"); //设置xml文档的编码为utf-8
		StringWriter out; // 输出流对象
		String resultXML = "";
		try {

			out = new StringWriter(); // 创建一个输出流对象
			XMLWriter writer = new XMLWriter(out, format); // 创建一个dom4j创建xml的对象
			// 调用write方法将doc文档写到输出流中
			writer.write(doc);
			writer.close();

			resultXML = out.toString();
			out.close();

			//System.out.println("结果XML1:" + resultXML);
			//System.out.println("报文长度1:" + resultXML.length());

		} catch (IOException e) {
			System.out.print("生成XML失败");
			e.printStackTrace();
		}

		return resultXML;
	}
	
	
	
	/**
	 * @Title: addObj2Elem
	 * @Description: 向指定节点下增加指定的对象(迭代POJO各属性并加入到结点下)
	 * @param elem  node
	 * @param obj 需要增加的对象
	 */
	private static void addObj2Elem(Element elem,Object obj) {
		List<Map<String, Object>> mapList=EntityUtils.entityToListMap(obj);
		for (Map<String, Object> map : mapList) {
			for (String key : map.keySet()) {
				Element subElem = elem.addElement(key);
				//subElem.addCDATA((String) map.get(key));
				String value= (String)map.get(key);
				if(StringUtil.isEmpty(value)) {
					value="";
				}
				subElem.addText(value);
			}
		}		
	}
	
	//-----------------获取库存----------------
	
	/**
	 * @Title: packBusinessData
	 * @Description: 打包业务数据
	 * @param root  root node
	 * @param invoiceTypeList  发票类型列表,参见 invoiceType常量
	 * 		record
	 * 			FPZL
	 */
	private static void packBusinessData_0(Element root,List<String> invoiceTypeList) {
		Element dataElement=genDataElement(root,invoiceTypeList.size());
		for(String invoiceType:invoiceTypeList) {
			Element recordElement=dataElement.addElement("record");  //增加record node
			Element fpxlElem=recordElement.addElement("FPZL");
			fpxlElem.addText(invoiceType);
		}
	}

	/**
	 * @Title: genPackage_0
	 * @Description: 生成获取库存信息请求报文
	 * @param sid  服务功能号,参见TaxSIDCode
	 * @param invoiceTypeList  发票类型编码列表
	 * @return 
	 * 		[获取库存信息]请求报文
	 */
	public static String genPackage_0(List<String> invoiceTypeList) {
		//(0)创建一个xml文档
		Document doc = genDocument(null);
		//(1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		//(2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_0);
		//(3)打包业务数据部分.
		packBusinessData_0(root,invoiceTypeList);
		//(4)生成XML
		String tempXML = createXMLStr(doc);	
		
		return tempXML;
	}
	
	//----------------发票开具---------------
	private static void packBusinessData_1(Element root,List<Invoice> invoiceList) {
		Element dataElem=genDataElement(root,invoiceList.size());
		for(Invoice invoice:invoiceList) {
			Element recordElem=dataElem.addElement("record");
			//(1)加入发票头
			Element fpElem=recordElem.addElement("fp");
			addObj2Elem(fpElem,invoice.getInvoiceHead());  
			//(2)加入发票明细
			Element groupElem=recordElem.addElement("group");
			for(InvoiceDetail detail:invoice.getInvoiceDetailList()) {
				Element fpmxElem=groupElem.addElement("fpmx");
				addObj2Elem(fpmxElem,detail);  
			}
		}
	}
	
	
	
	/**
	 * @Title: genPackage_1
	 * @Description: 生成:发票开具报文
	 * @param sid 
	 * @return 
	 * 		发票开具报文
	 */
	public static String genPackage_1(List<Invoice> invoiceList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_1);
		//(3)打包业务数据
		packBusinessData_1(root,invoiceList);
		//(4)生成XML
		String tempXML = createXMLStr(doc);	
				
		return tempXML;		
	}
	
	//------------------发票打印--------------------
	private static void packBusinessData_2(Element root,List<InvoicePrint> busDataList) {
		Element dataElem=genDataElement(root,busDataList.size());
		for(InvoicePrint invoicePrint:busDataList) {
			Element recordElem=dataElem.addElement("record");			
			addObj2Elem(recordElem,invoicePrint);  
		}
	}
	
	/**
	 * @Title: genPackage_2
	 * @Description: 生成:打印发票报文
	 * @param sid 
	 * @return 
	 * 		打印发票报文
	 */
	public static String genPackage_2(List<InvoicePrint> invoicePrintList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_2);
		//(3)打包业务数据
		packBusinessData_2(root,invoicePrintList);
		//(4)生成XML
		String tempXML = createXMLStr(doc);	
				
		return tempXML;		
	}
	
	//-------------------发票作废--------------------
	private static void packBusinessData_4(Element root,List<InvoiceInvalid> busDataList) {
		final String RECORD_NODE_NAME="record";
		Element dataElem=genDataElement(root,busDataList.size());
		for(InvoiceInvalid invoiceInvalid:busDataList) {
			Element recordElem=dataElem.addElement(RECORD_NODE_NAME);			
			addObj2Elem(recordElem,invoiceInvalid);  
		}
	}
	
	/**
	 * @Title: genPackage_4
	 * @Description: 生成:发票报废报文
	 * @param sid   SID_4
	 * @return 
	 * 		发票报废报文
	 */
	public static String genPackage_4(List<InvoiceInvalid> invoiceInvalidList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_4);
		//(3)打包业务数据
		packBusinessData_4(root,invoiceInvalidList);
		//(4)生成XML并返回
		String tempXML = createXMLStr(doc);	
		
		return tempXML;		
	}
	
	//-------------------清单打印--------------------	
	private static void packBusinessData_3(Element root,List<InvoiceListPrint> busDataList) {
		final String RECORD_NODE_NAME="record";
		Element dataElem=genDataElement(root,busDataList.size());
		for(InvoiceListPrint invoiceListPrint:busDataList) {
			Element recordElem=dataElem.addElement(RECORD_NODE_NAME);			
			addObj2Elem(recordElem,invoiceListPrint);  
		}
	}
	
		
	/**
	 * @Title: genPackage_3
	 * @Description: 生成:清单打印报文
	 * @param invoiceListPrintList  打印清单列表
	 * @return
	 * 		清单打印报文 
	 */
	public static String genPackage_3(List<InvoiceListPrint> invoiceListPrintList) {
		//(0)创建一个xml文档
		Document doc = genDocument(null);
		//(1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		//(2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_4);
		//(3)打包业务数据
		packBusinessData_3(root,invoiceListPrintList);
		//(4)生成XML并返回
		String tempXML = createXMLStr(doc);
		
		return tempXML;		
	}
	
	
	//--------------发票上传15---------------	
	private static void packBusinessData_15(Element root, List<InvoiceUpload> busDataList) {
		final String RECORD_NODE_NAME = "record";
		Element dataElem = genDataElement(root, busDataList.size());
		for (InvoiceUpload invoiceListPrint : busDataList) {
			Element recordElem = dataElem.addElement(RECORD_NODE_NAME);
			addObj2Elem(recordElem, invoiceListPrint);
		}
	}

	/**
	 * @Title: genPackage_15
	 * @Description: 生成发票上传报文
	 * @param invoiceUploadList 上传发票列表
	 * @return 发票上传报文
	 */
	public static String genPackage_15(List<InvoiceUpload> invoiceUploadList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_15);
		// (3)打包业务数据
		packBusinessData_15(root, invoiceUploadList);
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
		
	//--------------发票状态更新-SID:16---------------	
	/**
	 * @Title: genPackage_16
	 * @Description: 生成发票状态更新报文	 
	 * @return 发票状态更新报文
	 * 	备注:发票状态更新报文只有公共节点，详见公共节点说明章节1.3.2。 
	 */
	public static String genPackage_16() {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_16);
		// (3)打包业务数据
		//发票状态更新报文
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
	
	
	//--------------空白发票作废------SID:KBZF---------	
	private static void packBusinessData_KBZF(Element root, List<InvoiceEmpty> busDataList) {
		final String RECORD_NODE_NAME = "record";
		Element dataElem = genDataElement(root, busDataList.size());
		for (InvoiceEmpty invoiceEmpty : busDataList) {
			Element recordElem = dataElem.addElement(RECORD_NODE_NAME);
			addObj2Elem(recordElem, invoiceEmpty);
		}
	}
	
	/**
	 * @Title: genPackage_KBZF
	 * @Description: 生成空白发票作废报文
	 * @param invoiceEmptyList  空白发票列表
	 * @return 
	 * 		空白发票作废报文	
	 */
	public static String genPackage_KBZF(List<InvoiceEmpty> invoiceEmptyList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_KBZF);
		// (3)打包业务数据
		packBusinessData_KBZF(root, invoiceEmptyList);
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
	
	//-------------金税盘状态查询-----------
	public static String genPackage_JSPXX() {
		//(0)创建一个xml文档
		Document doc = genDocument(null);
		//(1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		//(2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_JSPXX);
		//(3)打包业务数据
		//没有业务数据
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
	
	//--------------打印参数设置SID为“DYCS”，查询金税盘状态。 printParams---------	
	private static void packBusinessData_DYCS(Element root, PrintParam printParam) {		
		Element dataElem = genDataElement(root, 0);
		addObj2Elem(dataElem, printParam);
	}

	/**
	 * @Title: genPackage_DYCS
	 * @Description: 生成设置打印参数报文
	 * @param printParam 打印参数
	 * @return 
	 * 		打印参数报文
	 */
	public static String genPackage_DYCS(PrintParam printParam) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_DYCS);
		// (3)打包业务数据
		packBusinessData_DYCS(root, printParam);
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
	
	//--------------红字信息上传------SID:HZXXSC---------	
		
		/**
		 * @Title: packBusinessData_HZXXSC
		 * @Description: 红字信息表上传-业务主体报文生成
		 * @param root	报文root node
		 * @param busDataList 红字信息表
		 */
		private static void packBusinessData_HZXXSC(Element root, List<RedInfo> busDataList) {
			Element dataElem=genDataElement(root,busDataList.size());
			for(RedInfo redInfo:busDataList) {
				Element recordElem=dataElem.addElement("record");
				//(1)加入发票头
				Element fpElem=recordElem.addElement("fp");
				addObj2Elem(fpElem,redInfo.getRedInfoHead());  
				//(2)加入发票明细
				Element groupElem=recordElem.addElement("group");
				for(RedInfoDetail detail:redInfo.getRedInfoDetailList()) {
					Element fpmxElem=groupElem.addElement("fpmx");
					addObj2Elem(fpmxElem,detail);  
				}
			}
		}
		
		/**
		 * @Title: genPackage_HZXXSC
		 * @Description: 生成红字信息表上传报文
		 * @param redInfoList  红字信息列表
		 * @return 
		 * 		红字信息表上传报文
		 */
	public static String genPackage_HZXXSC(List<RedInfo> redInfoList) {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_HZXXSC);
		// (3)打包业务数据
		packBusinessData_HZXXSC(root, redInfoList);
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
		
		
	//--------------------汇总抄报-------------------
	public static String genPackage_HZCB() {
		// (0)创建一个xml文档
		Document doc = genDocument(null);
		// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
		Element root = genDocRoot(doc);
		// (2)打包公共部分
		packPublicPart(root, TaxSIDCode.SID_HZCB);
		// (3)打包业务数据
		//针对汇总上报,无业务主体信息
		// (4)生成XML并返回
		String tempXML = createXMLStr(doc);
		return tempXML;
	}
	
	//--------------------远程清卡-------------------
		/**
		 * @Title: genPackage_YCQK
		 * @Description: 远程清卡	SID为“YCQK”，用于远程清卡。   clearCard
		 * @return 
		 * 	远程清卡请求报文
		 */
		public static String genPackage_YCQK() {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_YCQK);
			// (3)打包业务数据
			//针对汇总上报,无业务主体信息
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		//--------------//红字信息表下载	SID为“HXXXXZ”，下载红字信息表。  downloadRedInfo---------	
		/**
		 * @Title: packBusinessData_HZXXXZ
		 * @Description: 打包红字信息下载业务-主体
		 * @param root  红字信息下载请求报文-root node
		 * @param redInfoDownload  红字信息下载请求报文-业务实体
		 */
		private static void packBusinessData_HZXXXZ(Element root, RedInfoDownload redInfoDownload) {		
			Element dataElem = genDataElement(root, 0);
			addObj2Elem(dataElem, redInfoDownload);
		}

		
		/**
		 * @Title: genPackage_HZXXXZ
		 * @Description: 生成红字信息下载请求报文
		 * @param redInfoDownload  红字信息下载业务主体
		 * @return 
		 * 	红字信息下载请求报文
		 */
		public static String genPackage_HZXXXZ(RedInfoDownload redInfoDownload) {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_HZXXXZ);
			// (3)打包业务数据
			packBusinessData_HZXXXZ(root, redInfoDownload);
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		
		//---------------------//发票单张查询	SID为“FPCX”，发票查询   queryInvoiceInfo-------------------------
		/**
		 * @Title: packBusinessData_FPCX
		 * @Description: 发票单张查询
		 * @param root  请求报文root node
		 * @param busDataList 发票列表
		 */
		private static void packBusinessData_FPCX(Element root, List<InvoiceSingle> busDataList) {
			final String RECORD_NODE_NAME = "record";
			Element dataElem = genDataElement(root, busDataList.size());
			for (InvoiceSingle invoiceSingle : busDataList) {
				Element recordElem = dataElem.addElement(RECORD_NODE_NAME);
				addObj2Elem(recordElem, invoiceSingle);
			}
		}
		
		
		/**
		 * @Title: genPackage_FPCX
		 * @Description: 生成:发票单张查询请求报文
		 * @param invoiceSingleList  发票列表
		 * @return 
		 * 		发票单张查询-请求报文
		 */
		public static String genPackage_FPCX(List<InvoiceSingle> invoiceSingleList) {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_FPCX);
			// (3)打包业务数据
			packBusinessData_FPCX(root, invoiceSingleList);
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		
		//-----------------关闭控制台服务	SID 为“GBKZT”，关闭控制台  closeInvoiceService-------------------
		/**
		 * @Title: genPackage_GBKZT
		 * @Description: 生成-关闭控制台服务请求报文
		 * @return 
		 * 		关闭控制台服务请求报文
		 */
		public static String genPackage_GBKZT() {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_GBKZT);
			// (3)打包业务数据			
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		
		//------------------单张发票修复 SID为“FPXF”，单张发票修复  repairSingleInvoice----------------
		/**
		 * @Title: packBusinessData_FPXF
		 * @Description: 打包发票修复请求业务主体
		 * @param root  请求报文root node
		 * @param busDataList  请求报文业务主体 
		 * 			
		 */
		private static void packBusinessData_FPXF(Element root, List<InvoiceRepair> busDataList) {
			final String RECORD_NODE_NAME = "record";
			Element dataElem = genDataElement(root, busDataList.size());
			for (InvoiceRepair invoiceRepair : busDataList) {
				Element recordElem = dataElem.addElement(RECORD_NODE_NAME);
				addObj2Elem(recordElem, invoiceRepair);
			}
		}
		
		/**
		 * @Title: genPackage_FPXF
		 * @Description: 生成发票修复请求报文
		 * @param InvoiceSingleList
		 * @return 
		 * 		发票修复请求报文
		 */
		public static String genPackage_FPXF(List<InvoiceRepair> invoiceRepairList) {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_FPXF);
			// (3)打包业务数据
			packBusinessData_FPXF(root, invoiceRepairList);
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		//-------------------开票控制	SID为“KPKZ”，开票控制   issueControl-----------------
		private static void packBusinessData_kpkz(Element root,String kpkz) {
			IssueControlRequest issueControl=new IssueControlRequest();
			addObj2Elem(root, issueControl);
		}
		
		/**
		 * @Title: genPackage_KPKZ
		 * @Description: 生成开票控制数据包
		 * @param kpkz  开票控制   固定值：0：允许该客户端开票1：不允许该客户端开票
		 * @return 开票控制请求数据包
		 */
		public static String genPackage_KPKZ(String kpkz) {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_KPKZ);
			// (3)打包业务数据			
			packBusinessData_kpkz(root,kpkz);
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		
		//--------------------发票批量查询--------------------
		private static void packBusinessData_5(Element root, BatchQueryCond batchQueryCond) {		
			Element dataElem = genDataElement(root, 0);
			addObj2Elem(dataElem, batchQueryCond);
		}
		
		
		/**
		 * @Title: genPackage_5
		 * @Description: 生成发票批量查询报文
		 * @param batchQueryCond  批量查询条件 
		 * @return 
		 * 		批量查询报文
		 */
		public static String genPackage_5(BatchQueryCond batchQueryCond) {
			// (0)创建一个xml文档
			Document doc = genDocument(null);
			// (1)创建一个名为service的节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
			Element root = genDocRoot(doc);
			// (2)打包公共部分
			packPublicPart(root, TaxSIDCode.SID_5);
			// (3)打包业务数据
			packBusinessData_5(root, batchQueryCond);
			// (4)生成XML并返回
			String tempXML = createXMLStr(doc);
			return tempXML;
		}
		 
	
	//=======================test function===================
		/**
		 * @Title: testGenPackage_5
		 * @Description: 测试发票批量查询 
		 */
		private static void testGenPackage_5() {
			BatchQueryCond batchQueryCond=new BatchQueryCond();
			String xml = genPackage_5(batchQueryCond);
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
		
		/**
		 * @Title: testGenPackage_KPKZ
		 * @Description: 测试开票控制 
		 */
		private static void testGenPackage_KPKZ() {
			String xml = genPackage_KPKZ("1");
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
		
		
		/**
		 * @Title: testGenPackage_FPXF
		 * @Description: 测试生成发票修复报文 
		 */
		private static void testGenPackage_FPXF() {
			List<InvoiceRepair> invoiceRepairList = new ArrayList<InvoiceRepair>();
			for (int i = 0; i < 2; i++) {
				InvoiceRepair invoiceRepair = new InvoiceRepair();
				invoiceRepairList.add(invoiceRepair);
			}

			String xml = genPackage_FPXF(invoiceRepairList);
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
		
		
		//关闭控制台服务-test
		private static void testGenPackage_GBKZT() {
			String xml = genPackage_GBKZT();
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
		
		//单张发票查询
		private static void testGenPackage_FPCX() {
			List<InvoiceSingle> invoiceSinglList = new ArrayList<InvoiceSingle>();
			for (int i = 0; i < 2; i++) {
				InvoiceSingle invoiceSingle = new InvoiceSingle();
				invoiceSinglList.add(invoiceSingle);
			}

			String xml = genPackage_FPCX(invoiceSinglList);
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
		/**
		 * @Title: testGenPackage_HZXXXZ
		 * @Description: 红字信息下载-test 
		 */
		private static void testGenPackage_HZXXXZ() {
			RedInfoDownload redInfoDownload=new RedInfoDownload();
			String xml = genPackage_HZXXXZ(redInfoDownload);
			String xmlGBK = EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:" + xml);
			System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
		}
		
	
		/**
		 * @Title: testGenPackage_YCQK
		 * @Description: 远程清卡测试 
		 */
		private static void testGenPackage_YCQK() {
			String xml=genPackage_YCQK();
			String xmlGBK=EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:"+xml);
			System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
		}
		
	
	/**
	 * @Title: testGenPackage_HZCB
	 * @Description: 汇总抄报-生成请求报文测试 
	 */
	private static void testGenPackage_HZCB() {		
		String xml=genPackage_HZCB();
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
	}
	
	
	
		/**
		 * @Title: testGenPackage_HZXXSC
		 * @Description: TEST-红字信息上传 
		 */
		private static void testGenPackage_HZXXSC() {
			List<RedInfo> invoiceList=new ArrayList<RedInfo>();  //发票列表
			
			for (int i = 0; i < 2; i++) {
				// (1)生成一张发票
				RedInfo invoice1 = new RedInfo();
				// (1.1)加入发票头
				RedInfoHead RedInfoHead1 = new RedInfoHead();
				invoice1.setRedInfoHead(RedInfoHead1);
				// (1.2)加入发票明细
				for (int j = 0; j < 2; j++) {
					RedInfoDetail detail1 = new RedInfoDetail();
					invoice1.addDetail(detail1);
				}
				// (2)将发票加入到发票列表中
				invoiceList.add(invoice1);
			}
			
			String xml=genPackage_HZXXSC(invoiceList);
			String xmlGBK=EncodeUtils.utf2gbk(xml);
			System.out.println("生成的请求数据包(UTF-8)为:"+xml);
			System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
			
		}
	
	/**
	 * @Title: testGenPackage_DYCS
	 * @Description: 设置打印参数 
	 */
	private static void testGenPackage_DYCS() {
		PrintParam printParam=new PrintParam();
		String xml = genPackage_DYCS(printParam);
		String xmlGBK = EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:" + xml);
		System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
	}
	
	
	
	/**
	 * @Title: testGenPackage_JSPXX
	 * @Description:  测试:金税盘状态查询
	 */
	private static void testGenPackage_JSPXX() {
		String xml = genPackage_JSPXX();
		String xmlGBK = EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:" + xml);
		System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
	}
	
	
	/**
	 * @Title: testGenPackage_KBZF
	 * @Description: 空白作废 
	 */
	private static void testGenPackage_KBZF() {
		List<InvoiceEmpty> invoiceEmptyList = new ArrayList<InvoiceEmpty>();
		for (int i = 0; i < 2; i++) {
			InvoiceEmpty invoiceEmpty = new InvoiceEmpty();
			invoiceEmptyList.add(invoiceEmpty);
		}

		String xml = genPackage_KBZF(invoiceEmptyList);
		String xmlGBK = EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:" + xml);
		System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_16
	 * @Description: 发票状态更新 
	 */
	private static void testGenPackage_16() {
		String xml = genPackage_16();
		String xmlGBK = EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:" + xml);
		System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_15
	 * @Description: 发票上传 
	 */
	private static void testGenPackage_15() {
		List<InvoiceUpload> invoiceListPrintList = new ArrayList<InvoiceUpload>();
		for (int i = 0; i < 2; i++) {
			InvoiceUpload invoiceUpload = new InvoiceUpload();
			invoiceListPrintList.add(invoiceUpload);
		}

		String xml = genPackage_15(invoiceListPrintList);
		String xmlGBK = EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:" + xml);
		System.out.println("生成的请求数据包(GBK)为:" + xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_3
	 * @Description: test清单打印 
	 */
	private static void testGenPackage_3() {
		List<InvoiceListPrint> invoiceListPrintList=new ArrayList<InvoiceListPrint>();
		for(int i=0;i<2;i++) {
			InvoiceListPrint invoiceListPrint=new InvoiceListPrint();
			invoiceListPrintList.add(invoiceListPrint);
		}
		
		String xml=genPackage_3(invoiceListPrintList);
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_4
	 * @Description:  测试-发票作废
	 */
	private static void testGenPackage_4() {
		List<InvoiceInvalid> invoiceInvlaidList=new ArrayList<InvoiceInvalid>();
		for(int i=0;i<2;i++) {
			InvoiceInvalid invoiceInvalid=new InvoiceInvalid();
			invoiceInvlaidList.add(invoiceInvalid);
		}
		
		String xml=genPackage_4(invoiceInvlaidList);
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_2
	 * @Description:  测试-发票打印
	 */
	private static void testGenPackage_2() {
		List<InvoicePrint> invoicePrintList=new ArrayList<InvoicePrint>();
		for(int i=0;i<2;i++) {
			InvoicePrint invoicePrint=new InvoicePrint();
			invoicePrintList.add(invoicePrint);
		}
		
		String xml=genPackage_2(invoicePrintList);
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
		
	}
	
	/**
	 * @Title: testGenPackage_1
	 * @Description: 开据发票 测试,模拟了两张发票
	 */
	private static void testGenPackage_1() {
		
		List<Invoice> invoiceList=new ArrayList<Invoice>();  //发票列表
		
		for (int i = 0; i < 2; i++) {
			// (1)生成一张发票
			Invoice invoice1 = new Invoice();
			// (1.1)加入发票头
			InvoiceHead invoiceHead1 = new InvoiceHead();
			invoice1.setInvoiceHead(invoiceHead1);
			// (1.2)加入发票明细
			for (int j = 0; j < 2; j++) {
				InvoiceDetail detail1 = new InvoiceDetail();
				invoice1.addDetail(detail1);
			}
			// (2)将发票加入到发票列表中
			invoiceList.add(invoice1);
		}
		
		String xml=genPackage_1(invoiceList);
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
	}
	
	/**
	 * @Title: testGenPackage_0
	 * @Description: 查询发票库存 
	 */
	private static void testGenPackage_0() {
		List<String> invoiceTypeList=new ArrayList<>();
		invoiceTypeList.add("0");
		invoiceTypeList.add("1");		
		String xml=genPackage_0(invoiceTypeList);
		String xmlGBK=EncodeUtils.utf2gbk(xml);
		System.out.println("生成的请求数据包(UTF-8)为:"+xml);
		System.out.println("生成的请求数据包(GBK)为:"+xmlGBK);
	}
	
	public static void main(String[] args) {
		//testGenPackage_0();   //查询发票库存
		//testGenPackage_1();   //开据发票
		//testGenPackage_2();   //发票打印
		//testGenPackage_4();   //发票作废
		//testGenPackage_3();   //清单打印
		//testGenPackage_15();  //发票上传
		//testGenPackage_16();    //发票状态更新
		//testGenPackage_KBZF();	//空白发票作废
		//testGenPackage_JSPXX();  	//金税盘状态查询	SID为“JSPXX”，设置打印参数。  queryJSPInfo
		//testGenPackage_DYCS();  //打印参数
		//testGenPackage_HZXXSC();  	//红字信息上传
		//testGenPackage_HZCB();  	//汇总抄报
		//testGenPackage_YCQK();  //生成远程清卡报文
		//testGenPackage_HZXXXZ();  //红字信息下载
		//testGenPackage_GBKZT();  //关闭控制台服务
		//testGenPackage_FPXF();   //单张发票修复
		//testGenPackage_KPKZ();   //开票控制
		testGenPackage_5();  //发票批量查询
		
	}
}
