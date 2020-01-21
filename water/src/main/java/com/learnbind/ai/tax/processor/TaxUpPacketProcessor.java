package com.learnbind.ai.tax.processor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.tax.packet.response.BatchQueryInvoice;
import com.learnbind.ai.tax.packet.response.BatchQueryInvoiceDetail;
import com.learnbind.ai.tax.packet.response.BatchQueryInvoiceHead;
import com.learnbind.ai.tax.packet.response.BatchQueryResponseResult;
import com.learnbind.ai.tax.packet.response.DownloadRedInfo;
import com.learnbind.ai.tax.packet.response.DownloadRedInfoDetail;
import com.learnbind.ai.tax.packet.response.DownloadRedInfoHead;
import com.learnbind.ai.tax.packet.response.ElecInvoice;
import com.learnbind.ai.tax.packet.response.FailInvoice;
import com.learnbind.ai.tax.packet.response.InvalidateEmptyInvoice;
import com.learnbind.ai.tax.packet.response.InvalidateInvoice;
import com.learnbind.ai.tax.packet.response.InvoiceInventory;
import com.learnbind.ai.tax.packet.response.MotorVehicleSale;
import com.learnbind.ai.tax.packet.response.OpenInvoice;
import com.learnbind.ai.tax.packet.response.PrintInvoice;
import com.learnbind.ai.tax.packet.response.PrintList;
import com.learnbind.ai.tax.packet.response.QuerySingleInvoice;
import com.learnbind.ai.tax.packet.response.RepairSingleInvoice;
import com.learnbind.ai.tax.packet.response.ResponseResult;
import com.learnbind.ai.tax.packet.response.SingleInvoice;
import com.learnbind.ai.tax.packet.response.SingleInvoiceDetail;
import com.learnbind.ai.tax.packet.response.SingleInvoiceHead;
import com.learnbind.ai.tax.packet.response.SuccessInvoice;
import com.learnbind.ai.tax.packet.response.TaxEquipInfo;
import com.learnbind.ai.tax.packet.response.TransportVatSpecial;
import com.learnbind.ai.tax.packet.response.UploadInvoice;
import com.learnbind.ai.tax.packet.response.UploadRedInfo;
import com.learnbind.ai.tax.packet.response.VatSpecialAndNormal;

/**
 * Copyright (c) 2019 by Hz
 * 
 * @Package com.learnbind.ai.tax.processor
 *
 * @Title: TaxUpPacketProcessor.java
 * @Description: 上行报文处理器.
 *
 * @author lenovo
 * @date 2019年10月18日 下午1:22:12
 * @version V1.0 
 *
 */
public class TaxUpPacketProcessor {
	
	/**
	 * @Title: parseResponse
	 * @Description: 泛型函数-解析响应报文
	 * @param <T> 泛型方法
	 * @param tClass  泛型实例
	 * @param xml  响应报文
	 * @Param bodyNodeName: business data node's name
	 * @return 
	 * 		业务主体列表
	 * 		注:泛型后函数
	 */
	private static <T>  List<T> parseResponse(Class<T> tClass,String xml,String bodyNodeName) {
		// 需要返回库存信息列表
		List<T> busObjList = new ArrayList<T>();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node

			// 获取BODY结点
			Element bodyElem = rootElem.element(bodyNodeName); // business node 

			// Element responseElem = bodyElem.element("refp"); //refp node
			Iterator<Element> iter = bodyElem.elementIterator("refp");
			while (iter.hasNext()) {
				Element refpNode = (Element) iter.next(); // refp node
				T busObj=IterateNode2Obj(tClass,refpNode);  //迭代refp node下的子节点				
				busObjList.add(busObj);
			}
		} catch (Exception e) {
			busObjList = null;
		}
		
		return busObjList;
	}
	
	/**
	 * @Title: parseInvoiceStatus
	 * @Description: 解析发票状态更新-响应报文中-成功/失败的发票列表
	 * @param <T>  泛型
	 * @param tClass  泛型实例
	 * @param rootElem  响应报文root node(service node)
	 * @param status  	"succ"---更新成功的发票列表
	 * 					"fail"---更新失败的发票列表
	 * @return 
	 * 		发票列表.
	 */
	private static <T> List<T> parseInvoiceStatus(Class<T> tClass, Element rootElem,String status){
		List<T> invoiceList=new ArrayList<T>();  //状态更新列表
		Element statusElem=rootElem.element("info").element(status);
		Iterator<Element> succInvoiceItor = statusElem.elementIterator("fp");
		while(succInvoiceItor.hasNext()) {
			Element fpElem=(Element)succInvoiceItor.next();
			T successInvoice=IterateNode2Obj(tClass,fpElem);
			invoiceList.add(successInvoice);
		}
		return invoiceList;
	}
	
	
	/**
	 * @Title: IterateNode2Obj
	 * @Description: 将指定node的数据--transfer to-->Object
	 * 				   迭代如下形式的结点,其中nodex为指定结点.
	 * 				  返回的对象字段要与nodex下子结点一致(只迭代直接child)
	 * 					nodex
	 * 						nodex1
	 * 						nodex2
	 * 						nodex3
	 * @param <T>  泛型声明
	 * @param tClass  泛型
	 * @param element  指定的报文节点
	 * @return 泛型类型对 象
	 * 			如果指定节点下有子node
	 * 		
	 */
	private static <T> T IterateNode2Obj(Class<T> tClass,Element element) {
		T retObj=null;
		
		if(element.elements().size()>0) {
			try {
				//(1)object--->map
				retObj=tClass.newInstance();
				Map<String,Object> tempMap=EntityUtils.entityToMap(retObj);
				Iterator<Element> iter = element.elementIterator();
				while(iter.hasNext()) {
					Element fieldElem=(Element)iter.next();
					String name= fieldElem.getName();					
					String value=fieldElem.getTextTrim();
					tempMap.put(name, value);
				}
				//(2)map--->object
				retObj=EntityUtils.mapToEntity(tempMap, tClass);				
				
			} catch (InstantiationException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return retObj;
	}
	
	/**
	 * @Title: parseInvoiceInventory
	 * @Description: 解析库存信息返回报文
	 * @param xml  库存信息返回报文
	 * @return	发票库存信息列表 		
	 */	
	public static List<InvoiceInventory> parseInvoiceInventory(String xml) {		
		return parseResponse(InvoiceInventory.class,xml,"err");
	}		
	
	/**
	 * @Title: parseOpenInvoice
	 * @Description: 解析开据发票响应报文
	 * @param xml	开据发票响应报文
	 * @return  开据发票结果列表 	
	 */
	public static List<OpenInvoice> parseOpenInvoice(String xml) {
		return parseResponse(OpenInvoice.class,xml,"err");		
	}
	
	/**
	 * @Title: parsePrintInvoice
	 * @Description: 解析打印发票-响应报文
	 * @param xml  响应报文
	 * @return 打印发票结果列表
	 */
	public static List<PrintInvoice> parsePrintInvoice(String xml) {
		return parseResponse(PrintInvoice.class,xml,"err");		
	}
	
	
	/**
	 * @Title: parseInvalidateInvoice
	 * @Description: 解析发票作废-返回报文
	 * @param xml 响应报文
	 * @return 作废发票返回结果列表
	 */
	public static List<InvalidateInvoice> parseInvalidateInvoice(String xml) {
		return parseResponse(InvalidateInvoice.class,xml,"err");		
	}
	
	
	/**
	 * @Title: parsePrintList
	 * @Description: 解析打印清单响应报文
	 * @param xml	打印清单响应报文
	 * @return 清单打印结果列表
	 */
	public static List<PrintList> parsePrintList(String xml) {
		return parseResponse(PrintList.class,xml,"err");		
	}
	
	/**
	 * @Title: parseUploadInvoice
	 * @Description: 解析发票上传返回报文
	 * @param xml  响应报文
	 * @return 发票上传结果列表
	 */
	public static List<UploadInvoice> parseUploadInvoice(String xml) {
		return parseResponse(UploadInvoice.class,xml,"err");		
	}
	
	/**
	 * @Title: parseUpdateInvoiceStatus
	 * @Description: 解析更新发票状态响应报文
	 * @param xml  响应报文
	 * @return 
	 * 		发票更新状态
	 * 			"result"--->ResponseResult Object
	 * 			"successList"--->List<SuccessInvoice>
	 * 			"failList"--->List<FailInvoice>
	 */
	public static Map<String, Object> parseUpdateInvoiceStatus(String xml) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node

			// (1)处理响应结果结点
			Element responseResultElem = rootElem.element("err"); // err node //获取响应结果结点
			ResponseResult responseResult = IterateNode2Obj(ResponseResult.class, responseResultElem); // 对响应结果进行解析
			resultMap.put("result", responseResult); // 置于函数返回结果map

			// (2)处理更新成功的发票
			List<SuccessInvoice> successInvoiceList = parseInvoiceStatus(SuccessInvoice.class, rootElem, "succ");
			resultMap.put("successList", successInvoiceList); // 置于函数返回结果map

			// (3)处理更新失败的发票
			List<FailInvoice> failInvoiceList = parseInvoiceStatus(FailInvoice.class, rootElem, "fail");
			; // 状态更新失败列表
			resultMap.put("failList", failInvoiceList); // 置于函数返回结果map

		} catch (Exception e) {
			//do nothing
		}

		return resultMap;
	}
	
	/**
	 * @Title: parseInvalidateEmptyInvoice
	 * @Description: 解析空白发票作废返回报文
	 * @param xml  空白发票作废响应报文
	 * @return 
	 * 		空白发票作废结果列表
	 */
	public static List<InvalidateEmptyInvoice> parseInvalidateEmptyInvoice(String xml) {
		return parseResponse(InvalidateEmptyInvoice.class,xml,"err");		
	}
	
	/**
	 * @Title: parseQueryTaxEquipInfo
	 * @Description: 解析:查询金税盘设备信息-响应报文
	 * @param xml  响应报文
	 * @return
	 * 		map对象: 
	 * 		(1)"result"--->ResponseResult Object		//请求响应状态对象
	 * 		(2)"taxEquipInfo"--->TaxEquipInfo Object   //设备信息对明
	 * 		(3)"vatSpecialAndNormal"--->VatSpecialAndNormal Object  //增值税专用发票及增值税普通发票
	 * 		(4)"transportVatSpecial"--->TransportVatSpecial Object //货物运输业增值税专用发票
	 * 		(5)"motorVehicleSale"--->MotorVehicleSale Object  //机动车销售统一发票
	 * 		(6)"elecInvoice"--->ElecInvoice Object  //电子发票
	 * 
	 * comment:
	 * 		需要判定结点是否存在.
	 */
	public static Map<String, Object> parseQueryTaxEquipStatus(String xml) {
		Map<String, Object> resultMap = new HashMap<String, Object>();

		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node

			// (1)处理响应结果结点
			Element responseResultElem = rootElem.element("err"); // err node //获取响应结果结点
			ResponseResult responseResult = IterateNode2Obj(ResponseResult.class, responseResultElem); // 对响应结果进行解析
			resultMap.put("result", responseResult); // 置于函数返回结果map

			// (2)处理金税设备状态查询
			Element equipInfoElem=rootElem.element("Jssbztcx").element("Jssbxx");
			TaxEquipInfo taxEquipInfo = IterateNode2Obj(TaxEquipInfo.class,equipInfoElem ); // 对响应结果进行解析;
			resultMap.put("taxEquipInfo", taxEquipInfo); // 置于函数返回结果map

			// (3)处理增值税专用发票及增值税普通发票			
			Element vatSpecialAndNormalElem=rootElem.element("Jssbztcx").element("Zzszyptfp");
			if(vatSpecialAndNormalElem!=null) {
				VatSpecialAndNormal vatSpecialAndNormal = IterateNode2Obj(VatSpecialAndNormal.class,vatSpecialAndNormalElem ); // 对响应结果进行解析;
				resultMap.put("vatSpecialAndNormal", vatSpecialAndNormal); // 置于函数返回结果map
			}
			else {
				resultMap.put("vatSpecialAndNormal", null); // 置于函数返回结果map
			}
			
			
			//(4)货物运输业
			//TransportVatSpecial
			Element transportVatSpecialElem=rootElem.element("Jssbztcx").element("Hwysyzzszyfp");
			if(transportVatSpecialElem!=null) {
				TransportVatSpecial transportVatSpecial = IterateNode2Obj(TransportVatSpecial.class,transportVatSpecialElem ); // 对响应结果进行解析;
				resultMap.put("transportVatSpecial", transportVatSpecial); // 置于函数返回结果map
			}
			else {
				resultMap.put("transportVatSpecial", null); // 置于函数返回结果map
			}
			
			
			//(5)机动车销售统一发票
			//MotorVehicleSale
			Element motorVehicleSaleElem=rootElem.element("Jssbztcx").element("Jdcxstyfp");
			if(motorVehicleSaleElem!=null) {
				MotorVehicleSale motorVehicleSale = IterateNode2Obj(MotorVehicleSale.class,motorVehicleSaleElem ); // 对响应结果进行解析;
				resultMap.put("motorVehicleSale", motorVehicleSale); // 置于函数返回结果map
			}
			else {
				resultMap.put("motorVehicleSale", null); // 置于函数返回结果map
			}
			
			
			//(6)电子发票
			//ElecInvoice
			Element elecInvoiceElem=rootElem.element("Jssbztcx").element("Dzfp");
			if(elecInvoiceElem!=null) {
				ElecInvoice elecInvoice = IterateNode2Obj(ElecInvoice.class,elecInvoiceElem ); // 对响应结果进行解析;
				resultMap.put("elecInvoice", elecInvoice); // 置于函数返回结果map
			}
			else {
				resultMap.put("elecInvoice", null); // 置于函数返回结果map
			}			

		} catch (Exception e) {
			System.out.println("解析时发生错误:"+e.getMessage());
		}

		return resultMap;
	}
	
	/**
	 * @Title: parseConfigPrintParam
	 * @Description: 解析设置打印机参数-响应报文
	 * @param xml  响应报文
	 * @return 
	 * 		设置结果对象(ResponseResult)
	 * 
	 */
	public static ResponseResult parseConfigPrintParam(String xml) {
		ResponseResult responseResult=null;
		responseResult=parseReponseResult(xml);
		return responseResult;
	}
	
	/**
	 * @Title: parseReponseResult
	 * @Description: 解析报文响应状态    sub nodes of err node
	 * @param xml  响应报文
	 * @return 
	 * 			如果有err node,则返回ResponseResult对象
	 * 			否则返回null;
	 * 			
	 */
	private static ResponseResult parseReponseResult(String xml) {
		ResponseResult responseResult=null;
		try {
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node

			// (1)处理响应结果结点
			Element responseResultElem = rootElem.element("err"); // err node //获取响应结果结点
			responseResult = IterateNode2Obj(ResponseResult.class, responseResultElem); // 对响应结果进行解析
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return responseResult;
	}
	
	/**
	 * @Title: parseUploadRedInfo
	 * @Description: 解析红字信息表上传返回报文
	 * @param xml  红字信息表上传响应报文
	 * @return 
	 * 		红字信息表上传结果列表
	 */
	public static List<UploadRedInfo> parseUploadRedInfo(String xml) {
		return parseResponse(UploadRedInfo.class,xml,"redata");		
	}
	
	/**
	 * @Title: parseSummaryReport
	 * @Description: 解析汇总抄表响应报文-响应状态
	 * @param xml  向应报文
	 * @return 响应状态对象
	 */
	public static ResponseResult parseSummaryReport(String xml) {
		ResponseResult responseResult=parseReponseResult(xml);
		return responseResult;
	}	
	
	
	/**
	 * @Title: parseRemoteClearCard
	 * @Description: 解析远程清卡响应报文-响应状态
	 * @param xml  响应报文
	 * @return 响应状态对象
	 */
	public static ResponseResult parseRemoteClearCard(String xml) {
		ResponseResult responseResult=parseReponseResult(xml);
		return responseResult;
	}	
	
	
	
	
	
	/**
	 * @Title: parseControlService
	 * @Description: 关闭控制台服务-响应报文
	 * @param xml 响应报文
	 * @return 响应状态对象
	 */
	public static ResponseResult parseCloseControlService(String xml) {
		ResponseResult responseResult=parseReponseResult(xml);
		return responseResult;
	}
	
	/**
	 * @Title: parseQuerySingleInvoice
	 * @Description: 发票单张查询-响应报文
	 * @param xml 响应报文
	 * @return 单张发票列表
	 */
	public static List<QuerySingleInvoice> parseQuerySingleInvoice(String xml) {
		return parseResponse(QuerySingleInvoice.class,xml,"err");		
	}
	
	/**
	 * @Title: parseControlService
	 * @Description: 开票控制-响应报文
	 * @param xml 响应报文
	 * @return 响应状态对象
	 */
	public static ResponseResult parseOpenInvoiceControl(String xml) {
		ResponseResult responseResult=parseReponseResult(xml);
		return responseResult;
	}
	
	/**
	 * @Title: parseRepairSingleInvoice
	 * @Description: 解析-修复单张发票
	 * @param xml	修复单张发票报文
	 * @return 修复单张发票结果列表
	 */
	public static List<RepairSingleInvoice> parseRepairSingleInvoice(String xml) {
		return parseResponse(RepairSingleInvoice.class,xml,"err");		
	}
	
	
	/**
	 * @Title: IterateNode2ObjExt
	 * @Description: 解析一张红字信息表
	 * @param element  "refp" node
	 * @return 
	 * 		红字信息表对象
	 */
	private static DownloadRedInfo IterateNode2ObjExt(Element element) {
		DownloadRedInfo retObj=null;
		
		if(element.elements().size()>0) {
			try {
				
				retObj=new DownloadRedInfo();
				
				//(1)object--->map
				Map<String,Object> headMap=EntityUtils.entityToMap(retObj.getRedInfoHead());
				Iterator<Element> iter = element.elementIterator();
				while(iter.hasNext()) {
					//表头字段部分
					Element fieldElem=(Element)iter.next();
					String name= fieldElem.getName();					
					String value=fieldElem.getTextTrim();
					
					//明细部分
					if(name.equals("refpMx")) {
						Iterator<Element> itor = fieldElem.elementIterator(); // mx node's iterator
						while(itor.hasNext()) {
							Element mxElem=itor.next();
							DownloadRedInfoDetail redInfoDetail=IterateNode2Obj(DownloadRedInfoDetail.class,mxElem);
							retObj.addRedInfoDetail(redInfoDetail);  //详情加入到列表中
						}						
					}
					else {
						headMap.put(name, value);
					}					
				}
				
				//(2)map--->object
				DownloadRedInfoHead redInfoHead=EntityUtils.mapToEntity(headMap, DownloadRedInfoHead.class);
				retObj.setRedInfoHead(redInfoHead);  //设置红字信息表头								
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return retObj;
	}
	
	/**
	 * @Title: parseDownloadRedInfo
	 * @Description: 解析-红字信息表下载
	 * @param xml	红字信息表下载-响应报文
	 * @return 
	 * 		"err"--->ResponseResult	响应结果
	 * 		"redInfoList"--->List<DownloadRedInfo>		红字信息列表
	 */
	public static Map<String, Object> parseDownloadRedInfo(String xml) {
		Map<String, Object> resultMap = new HashMap<>(); // 解析结果map

		try {
			
			//(1)报文响应部分
			ResponseResult responseResult = parseReponseResult(xml);
			//报文响应结果--->map
			resultMap.put("err", responseResult); 			
			
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node

			// 获取BODY结点
			Element bodyElem = rootElem.element("redata"); // business node
			Iterator<Element> iter = bodyElem.elementIterator();  //refp nodes' iterator
			//(2)红字信息表(List部分)
			List<DownloadRedInfo> redInfoList=new ArrayList<DownloadRedInfo>();  
			while (iter.hasNext()) {
				Element refpNode = (Element) iter.next(); // refp node
				
				DownloadRedInfo  busObj=IterateNode2ObjExt(refpNode);  //迭代refp node下的子节点				
				redInfoList.add(busObj);
			}
			//红字信息表 redInfoList--->map
			resultMap.put("redInfoList", redInfoList); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}
	
	
	/**
	 * @Title: IterateNode2ObjExtBatchQuery
	 * @Description: 解析批量发票查询-业务主体(发票头+发票详情)
	 * @param element 
	 * @return 
	 */
	private static BatchQueryInvoice iterateNode2ObjExtBatchQuery(Element element) {
		BatchQueryInvoice retObj=null;
		
		if(element.elements().size()>0) {
			try {
				
				retObj=new BatchQueryInvoice();
				
				//(1)object--->map
				Map<String,Object> headMap=EntityUtils.entityToMap(retObj.getInvoiceHead());
				Iterator<Element> iter = element.elementIterator();
				while(iter.hasNext()) {
					//表头字段部分
					Element fieldElem=(Element)iter.next();
					String name= fieldElem.getName();					
					String value=fieldElem.getTextTrim();
					
					//明细部分
					if(name.equals("refpMx")) {
						Iterator<Element> itor = fieldElem.elementIterator(); // mx node's iterator
						while(itor.hasNext()) {
							Element mxElem=itor.next();
							BatchQueryInvoiceDetail invoiceDetail=IterateNode2Obj(BatchQueryInvoiceDetail.class,mxElem);
							retObj.addDetail(invoiceDetail);  //详情加入到列表中
						}						
					}
					else {
						headMap.put(name, value);
					}					
				}
				
				//(2)map--->object
				BatchQueryInvoiceHead invoiceHead=EntityUtils.mapToEntity(headMap, BatchQueryInvoiceHead.class);
				retObj.setInvoiceHead(invoiceHead);  //设置发票表头								
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return retObj;
	}
	
	/**
	 * @Title: parseBatchQueryInvoice
	 * @Description: 批量发票查询-响应报文
	 * @param xml
	 * @return 
	 *  		"err"			--->BatchQueryResponseResult	响应结果
	 * 			"invoiceList"	--->List<BatchQueryInvoice>		发票列表
	 */
	public static Map<String, Object> parseBatchQueryInvoice(String xml) {
		Map<String, Object> resultMap = new HashMap<>(); // 解析结果map

		try {
			
			Document doc = DocumentHelper.parseText(xml); // 将字符串转为XML
			Element rootElem = doc.getRootElement(); // 获取根节点 service node
			
			//(1)报文响应部分
			Element resultElem=rootElem.element("err");
			BatchQueryResponseResult responseResult=IterateNode2Obj(BatchQueryResponseResult.class,resultElem);
			resultMap.put("err", responseResult);  //报文响应结果--->map

			// 获取BODY结点
			Element bodyElem = rootElem.element("redata"); // business node
			Iterator<Element> iter = bodyElem.elementIterator();  //refp nodes' iterator
			//(2)红字信息表(List部分)
			List<BatchQueryInvoice> invoiceList=new ArrayList<BatchQueryInvoice>();  
			while (iter.hasNext()) {
				Element refpNode = (Element) iter.next(); // refp node
										  
				BatchQueryInvoice  busObj=iterateNode2ObjExtBatchQuery(refpNode);  //迭代refp node下的子节点				
				invoiceList.add(busObj);
			}
			//红字信息表 redInfoList--->map
			resultMap.put("invoiceList", invoiceList); 
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return resultMap;
	}
	
	
	/**
	 * @Title: iterateNode2ObjExtSingleInvoice
	 * @Description: 解析单张发票   INFOXML
	 * @param element  INFOXML(XML格式内容)
	 * @return SingleInvoice 单张发票对象
	 */
	private static SingleInvoice iterateNode2ObjExtSingleInvoice(Element element) {
		SingleInvoice retObj=null;
		
		if(element.elements().size()>0) {
			try {
				
				retObj=new SingleInvoice();
				
				//(1)object--->map
				Map<String,Object> headMap=EntityUtils.entityToMap(retObj.getInvoiceHead());
				Iterator<Element> iter = element.elementIterator();
				while(iter.hasNext()) {
					//表头字段部分
					Element fieldElem=(Element)iter.next();
					String name= fieldElem.getName();					
					String value=fieldElem.getTextTrim();
					
					//明细部分
					if(name.equals("MXS")) {
						Iterator<Element> itor = fieldElem.elementIterator(); // mx node's iterator
						while(itor.hasNext()) {
							Element mxElem=itor.next();
							SingleInvoiceDetail invoiceDetail=IterateNode2Obj(SingleInvoiceDetail.class,mxElem);
							retObj.addDetail(invoiceDetail);  //详情加入到列表中
						}						
					}
					else {
						headMap.put(name, value);
					}					
				}
				
				//(2)map--->object
				SingleInvoiceHead invoiceHead=EntityUtils.mapToEntity(headMap, SingleInvoiceHead.class);
				retObj.setInvoiceHead(invoiceHead);  //设置发票表头								
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}		
		return retObj;
	}
	
	
	/**
	 * @Title: parseSingleInvoice
	 * @Description: 解析单张发票(发票单张查询)
	 * @param xml  INFOXML中的内容(XML格式)
	 * @return  单张发票(SingleInvoice) 		
	 */
	public static SingleInvoice parseSingleInvoice(String xml) {
		SingleInvoice invoice=null;

		try {
			
			Document doc = DocumentHelper.parseText(xml); 	// 将字符串转为XML
			Element rootElem = doc.getRootElement(); 		// 获取根节点 info node

			//解析单张发票																  
			invoice=iterateNode2ObjExtSingleInvoice(rootElem);  //迭代refp node下的子节点
		} catch (Exception e) {
			e.printStackTrace();
		}

		return invoice;		
	}
	
	
	
	//-----------------test function---------------
	
	/**
	 * @Title: testParseSingleInvoice
	 * @Description: 解析单张发票 
	 */
	public static void testParseSingleInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"GBK\"?>\r\n" +
				"<Info>\r\n" + 
					"<GFMC/>\r\n" + 
					"<GFSH/>\r\n" + 
					"<GFDZDH/>\r\n" + 
					"<GFYHZH/>\r\n" +				 
					"<XFMC/>\r\n" +				 
					"<XFSH/>\r\n" + 
					"<XFDZDH/>\r\n" + 
					"<XFYHZH/>\r\n" + 
					"<BZ/>\r\n" + 
					"<KPR/>\r\n" + 
					"<SKR/>\r\n" + 
					"<FHR/>\r\n" + 
					"<KPSXH/>\r\n" + 
					"<DZSYH/>\r\n" +
					"<QDBZ/>\r\n" + 
					"<BMBBBH/>\r\n" + 
					"<MXS>\r\n" + 
						"<MX>\r\n" + 
							"<SPMC/>\r\n" + 
							"<GGXH/>\r\n" + 
							"<JLDW/>\r\n" + 
							"<SL/>\r\n" + 
							"<DJ/>\r\n" + 
							"<HSJBZ/>\r\n" + 
							"<FPHXZ/>\r\n" + 
							"<JE/>\r\n" + 
							"<SLV/>\r\n" + 
							"<SE/>\r\n" + 
							"<FLBM/>\r\n" + 
							"<LSLVBS/>\r\n" + 
							"<XSYH/>\r\n" + 
							"<YHSM/>	\r\n" + 
						"</MX>\r\n" +
						"<MX>\r\n" + 
							"<SPMC/>\r\n" + 
							"<GGXH/>\r\n" + 
							"<JLDW/>\r\n" + 
							"<SL/>\r\n" + 
							"<DJ/>\r\n" + 
							"<HSJBZ/>\r\n" + 
							"<FPHXZ/>\r\n" + 
							"<JE/>\r\n" + 
							"<SLV/>\r\n" + 
							"<SE/>\r\n" + 
							"<FLBM/>\r\n" + 
							"<LSLVBS/>\r\n" + 
							"<XSYH/>\r\n" + 
							"<YHSM/>	\r\n" + 
						"</MX>\r\n" +							 
					"</MXS>\r\n" + 
				"</Info>\r\n" + 
				"";
		SingleInvoice singleInvoice=parseSingleInvoice(xml);
		System.out.println("single invoice mx list size is:"+singleInvoice.getDetailNum());   //预期结果:2
		
	}
	
	/**
	 * @Title: testParseBatchQueryInvoice
	 * @Description: test 发票批量查询 
	 */
	public static void testParseBatchQueryInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"		<UPLOADMODE></UPLOADMODE>\r\n" + 
				"	</err>\r\n" + 
				"	<redata count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<KPJH></KPJH>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<JYM></JYM>\r\n" + 
				"			<XFMC></XFMC>\r\n" + 
				"			<XFSH></XFSH>\r\n" + 
				"			<XFDZDH></XFDZDH>\r\n" + 
				"			<XFYHZH></XFYHZH>\r\n" + 
				"			<GFMC></GFMC>\r\n" + 
				"			<GFSH></GFSH>\r\n" + 
				"			<GFDZDH></GFDZDH>\r\n" + 
				"			<GFYHZH></GFYHZH>\r\n" + 
				"			<BZ></BZ>\r\n" + 
				"			<SKR></SKR>\r\n" + 
				"			<FHR></FHR>\r\n" + 
				"			<KPR></KPR>\r\n" + 
				"			<HJJE></HJJE>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<KPRQ></KPRQ>\r\n" + 
				"			<XSDJBH></XSDJBH>\r\n" + 
				"			<QDBZ></QDBZ>\r\n" + 
				"			<DYBZ></DYBZ>\r\n" + 
				"			<ZFBZ></ZFBZ>\r\n" + 
				"			<ZFRQ></ZFRQ>\r\n" + 
				"			<BSBZ></BSBZ>\r\n" + 
				"			<BMBBH></BMBBH>\r\n" + 
				"			<refpMx>\r\n" + 
				"				<mx>\r\n" + 
				"					<FPMXXH></FPMXXH>\r\n" + 
				"					<FPHXZ></FPHXZ>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"					<SSFLBM></SSFLBM>\r\n" + 
				"					<YHZC></YHZC>\r\n" + 
				"					<YHZCNR></YHZCNR>\r\n" + 
				"					<LSLBS></LSLBS>\r\n" + 
				"					<QYZBM></QYZBM>\r\n" + 
				"				</mx>\r\n" + 
				"				<mx>\r\n" + 
				"					<FPMXXH></FPMXXH>\r\n" + 
				"					<FPHXZ></FPHXZ>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"					<SSFLBM></SSFLBM>\r\n" + 
				"					<YHZC></YHZC>\r\n" + 
				"					<YHZCNR></YHZCNR>\r\n" + 
				"					<LSLBS></LSLBS>\r\n" + 
				"					<QYZBM></QYZBM>\r\n" + 
				"				</mx>\r\n" +
				"			</refpMx> \r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<KPJH></KPJH>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<JYM></JYM>\r\n" + 
				"			<XFMC></XFMC>\r\n" + 
				"			<XFSH></XFSH>\r\n" + 
				"			<XFDZDH></XFDZDH>\r\n" + 
				"			<XFYHZH></XFYHZH>\r\n" + 
				"			<GFMC></GFMC>\r\n" + 
				"			<GFSH></GFSH>\r\n" + 
				"			<GFDZDH></GFDZDH>\r\n" + 
				"			<GFYHZH></GFYHZH>\r\n" + 
				"			<BZ></BZ>\r\n" + 
				"			<SKR></SKR>\r\n" + 
				"			<FHR></FHR>\r\n" + 
				"			<KPR></KPR>\r\n" + 
				"			<HJJE></HJJE>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<KPRQ></KPRQ>\r\n" + 
				"			<XSDJBH></XSDJBH>\r\n" + 
				"			<QDBZ></QDBZ>\r\n" + 
				"			<DYBZ></DYBZ>\r\n" + 
				"			<ZFBZ></ZFBZ>\r\n" + 
				"			<ZFRQ></ZFRQ>\r\n" + 
				"			<BSBZ></BSBZ>\r\n" + 
				"			<BMBBH></BMBBH>\r\n" + 
				"			<refpMx>\r\n" + 
				"				<mx>\r\n" + 
				"					<FPMXXH></FPMXXH>\r\n" + 
				"					<FPHXZ></FPHXZ>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"					<SSFLBM></SSFLBM>\r\n" + 
				"					<YHZC></YHZC>\r\n" + 
				"					<YHZCNR></YHZCNR>\r\n" + 
				"					<LSLBS></LSLBS>\r\n" + 
				"					<QYZBM></QYZBM>\r\n" + 
				"				</mx>\r\n" + 
				"				<mx>\r\n" + 
				"					<FPMXXH></FPMXXH>\r\n" + 
				"					<FPHXZ></FPHXZ>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"					<SSFLBM></SSFLBM>\r\n" + 
				"					<YHZC></YHZC>\r\n" + 
				"					<YHZCNR></YHZCNR>\r\n" + 
				"					<LSLBS></LSLBS>\r\n" + 
				"					<QYZBM></QYZBM>\r\n" + 
				"				</mx>\r\n" + 
				"			</refpMx>\r\n" + 
				"		</refp>\r\n" +  
				"	</redata>\r\n" + 
				"</service>\r\n" + 
				"";
		
		Map<String,Object> resultMap=parseBatchQueryInvoice(xml);
		BatchQueryResponseResult reponseResult=(BatchQueryResponseResult)resultMap.get("err");		
		List<BatchQueryInvoice> invoiceList=(ArrayList<BatchQueryInvoice>)resultMap.get("invoiceList");
		
		System.out.println("batch query invoicelist size is:"+invoiceList.size());
		
		BatchQueryInvoice invoice1=invoiceList.get(0);		
		System.out.println("invoice's detail list size is:"+invoice1.getDetailNum());
	}
	
	
	/**
	 * @Title: testParseDownloadRedInfo
	 * @Description: 红字信息表下载-响应报文 
	 */
	@SuppressWarnings("unchecked")
	public static void testParseDownloadRedInfo() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"	<redata count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<XXBLSH></XXBLSH>\r\n" + 
				"			<XXBBH></XXBBH>\r\n" + 
				"			<XXBZTDM></XXBZTDM>\r\n" + 
				"			<XXBZTMS></XXBZTMS>\r\n" + 
				"			<XXBLX></XXBLX>\r\n" + 
				"			<LPDM></LPDM>\r\n" + 
				"			<LPHM></LPHM>\r\n" + 
				"			<SZLB></SZLB>\r\n" + 
				"			<DSLBZ></DSLBZ>\r\n" + 
				"			<TKRQ></TKRQ>\r\n" + 
				"			<GFMC></GFMC>\r\n" + 
				"			<GFSH></GFSH>\r\n" + 
				"			<XFMC></XFMC>\r\n" + 
				"			<XFSH></XFSH>\r\n" + 
				"			<HJJE></HJJE>\r\n" + 
				"			<SLV></SLV>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<SQSM></SQSM>\r\n" + 
				"			<refpMx>\r\n" + 
				"				<mx>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"				</mx>\r\n" +
				"				<mx>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"				</mx>\r\n" +
				"			</refpMx> 	       \r\n" + 
				"		</refp>\r\n" +
				"		<refp>\r\n" + 
				"			<XXBLSH></XXBLSH>\r\n" + 
				"			<XXBBH></XXBBH>\r\n" + 
				"			<XXBZTDM></XXBZTDM>\r\n" + 
				"			<XXBZTMS></XXBZTMS>\r\n" + 
				"			<XXBLX></XXBLX>\r\n" + 
				"			<LPDM></LPDM>\r\n" + 
				"			<LPHM></LPHM>\r\n" + 
				"			<SZLB></SZLB>\r\n" + 
				"			<DSLBZ></DSLBZ>\r\n" + 
				"			<TKRQ></TKRQ>\r\n" + 
				"			<GFMC></GFMC>\r\n" + 
				"			<GFSH></GFSH>\r\n" + 
				"			<XFMC></XFMC>\r\n" + 
				"			<XFSH></XFSH>\r\n" + 
				"			<HJJE></HJJE>\r\n" + 
				"			<SLV></SLV>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<SQSM></SQSM>\r\n" + 
				"			<refpMx>\r\n" + 
				"				<mx>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"				</mx>\r\n" + 
				"				<mx>\r\n" + 
				"					<SPMC></SPMC>\r\n" + 
				"					<JLDW></JLDW>\r\n" + 
				"					<GGXH></GGXH>\r\n" + 
				"					<DJ></DJ>\r\n" + 
				"					<SL></SL>\r\n" + 
				"					<JE></JE>\r\n" + 
				"					<SLV></SLV>\r\n" + 
				"					<SE></SE>\r\n" + 
				"					<HSBZ></HSBZ>\r\n" + 
				"				</mx>\r\n" + 
				"			</refpMx> 	       \r\n" + 
				"		</refp>\r\n" +				 
				"	</redata>\r\n" + 
				"</service>\r\n" + 
				"";
		
		Map<String,Object> resultMap=parseDownloadRedInfo(xml);
		ResponseResult reponseResult=(ResponseResult)resultMap.get("err");		
		List<DownloadRedInfo> redInfoList=(ArrayList<DownloadRedInfo>)resultMap.get("redInfoList");
		
		System.out.println("download red info list size is:"+redInfoList.size());
		
		DownloadRedInfo readInfo1=redInfoList.get(0);		
		System.out.println("readInfo's detail list size is:"+readInfo1.getDetailNum());
	}
	
	/**
	 * @Title: testParseRepairSingleInvoice
	 * @Description: 测试:解析修复单张发票 
	 */
	public static void testParseRepairSingleInvoice() {
		
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"		</refp>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<RepairSingleInvoice> resultList=parseRepairSingleInvoice(xml);
		System.out.println("result list is:"+resultList.size());
	}
	
	
	/**
	 * @Title: testParseOpenInvoiceControl
	 * @Description: 解析:开票控制响应报文 
	 */
	public static void testParseOpenInvoiceControl() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		ResponseResult responseResult=parseSummaryReport(xml);
		System.out.println("return result is:"+responseResult.getRETCODE());
	}
		
	/**
	 * @Title: testParseCloseControlService
	 * @Description: TEST:解析关闭控制台服务	  
	 */
	public static void testParseCloseControlService() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		ResponseResult responseResult=parseSummaryReport(xml);
		System.out.println("return result is:"+responseResult.getRETCODE());
	}
	
	/**
	 * @Title: testParseQuerySingleInvoice
	 * @Description: TEST:发票单张查询 
	 */
	public static void testParseQuerySingleInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<XSDJBH></XSDJBH>\r\n" + 
				"			<BJBHSJE></BJBHSJE>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<KPRQ></KPRQ>\r\n" + 
				"			<DYBZ></DYBZ>\r\n" + 
				"			<FPBSZT></FPBSZT>\r\n" + 
				"			<ZFBZ></ZFBZ>\r\n" + 
				"			<INFOXML></INFOXML>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<XSDJBH></XSDJBH>\r\n" + 
				"			<BJBHSJE></BJBHSJE>\r\n" + 
				"			<HJSE></HJSE>\r\n" + 
				"			<KPRQ></KPRQ>\r\n" + 
				"			<DYBZ></DYBZ>\r\n" + 
				"			<FPBSZT></FPBSZT>\r\n" + 
				"			<ZFBZ></ZFBZ>\r\n" + 
				"			<INFOXML></INFOXML>\r\n" + 
				"		</refp>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<QuerySingleInvoice> resultList= parseQuerySingleInvoice(xml);
		System.out.println("result list size is:"+resultList.size());
		
	}
	
	
	
	
	/**
	 * @Title: testparseRemoteClearCard
	 * @Description: TEST:远程清卡响应报文 
	 */
	public static void testParseRemoteClearCard() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		ResponseResult responseResult=parseSummaryReport(xml);
		System.out.println("return result is:"+responseResult.getRETCODE());
	}
	
	
	/**
	 * @Title: testparseSummaryReport
	 * @Description: 测试: 解析汇总抄表响应报文
	 */
	public static void testparseSummaryReport() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		ResponseResult responseResult=parseSummaryReport(xml);
		System.out.println("return result is:"+responseResult.getRETCODE());
	}
	
	
	/**
	 * @Title: testParseUploadRedInfo
	 * @Description: test-红字信息表上传响应报文  
	 */
	public static void testParseUploadRedInfo() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>	\r\n" + 
				"	</err>\r\n" + 
				"	<redata count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<XXBLSH></XXBLSH>\r\n" + 
				"			<XXBBH></XXBBH>\r\n" + 
				"			<XXBZTDM></XXBZTDM>\r\n" + 
				"			<XXBZTMS></XXBZTMS>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<XXBLSH></XXBLSH>\r\n" + 
				"			<XXBBH></XXBBH>\r\n" + 
				"			<XXBZTDM></XXBZTDM>\r\n" + 
				"			<XXBZTMS></XXBZTMS>\r\n" + 
				"		</refp>\r\n" + 
				"	</redata>\r\n" + 
				"</service>\r\n" + 
				"";
		List<UploadRedInfo> resultList=parseUploadRedInfo(xml);
		System.out.println("upload Invoice result list size is:"+resultList.size());  //预测结果为2
		
	}
	
	/**
	 * @Title: testParseConfigPrintParam
	 * @Description: test-设置打印机参数-响应报文 
	 */
	public static void testParseConfigPrintParam() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		ResponseResult result=parseConfigPrintParam(xml);
	}
	
	
	public static void testParseQueryTaxEquipStatus() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"	<Jssbztcx>\r\n" + 
				"		<Jssbxx>\r\n" + 
				"			<Nsrsbh></Nsrsbh>\r\n" + 
				"			<Bkpjlx></Bkpjlx>\r\n" + 
				"			<Fkpjsm></Fkpjsm>\r\n" + 
				"			<Kpdh></Kpdh>\r\n" + 
				"			<Jssbdqrq></Jssbdqrq>\r\n" + 
				"			<Fpsfyw></Fpsfyw>\r\n" + 
				"			<Scjzrq></Scjzrq>\r\n" + 
				"			<Lxsx></Lxsx>\r\n" + 
				"			<Qdcxbbh></Qdcxbbh>\r\n" + 
				"			<Dccxbbh></Dccxbbh>\r\n" + 
				"			<Jspbh></Jspbh>\r\n" + 
				"			<Bspbh></Bspbh>\r\n" + 
				"			<Bspdcbbh></Bspdcbbh>\r\n" + 
				"			<Hzfwsq></Hzfwsq>\r\n" + 
				"			<Fxsqxx></Fxsqxx>\r\n" + 
				"			<Bsprl></Bsprl>\r\n" + 
				"		</Jssbxx>\r\n" + 
				"		<Zzszyptfp>\r\n" + 
				"			<Sfdcsq></Sfdcsq>\r\n" + 
				"			<Sfdssq></Sfdssq>\r\n" + 
				"			<Zyfplxxe></Zyfplxxe>\r\n" + 
				"			<Ptfplxxe></Ptfplxxe>\r\n" + 
				"			<Zyfplxsyje></Zyfplxsyje>\r\n" + 
				"			<Ptfplxsyje></Ptfplxsyje>\r\n" + 
				"			<Zyfpkpxe></Zyfpkpxe>\r\n" + 
				"			<Ptfpkpxe></Ptfpkpxe>\r\n" + 
				"			<Ssrq></Ssrq>\r\n" + 
				"			<Sccbrq></Sccbrq>\r\n" + 
				"			<Csqsrq></Csqsrq>\r\n" + 
				"			<Bszl></Bszl>\r\n" + 
				"			<Bscgbz></Bscgbz>\r\n" + 
				"			<Grfpxx></Grfpxx>\r\n" + 
				"			<Thfpxx></Thfpxx>\r\n" + 
				"		</Zzszyptfp>\r\n" + 
				"		<Hwysyzzszyfp>\r\n" + 
				"			<Sfdcsq></Sfdcsq>\r\n" + 
				"			<Sfdssq></Sfdssq>\r\n" + 
				"			<Lxxe></Lxxe>\r\n" + 
				"			<Lxsyje></Lxsyje>\r\n" + 
				"			<Dzkpxe></Dzkpxe>\r\n" + 
				"			<Ssrq></Ssrq>\r\n" + 
				"			<Sccbrq></Sccbrq>\r\n" + 
				"			<Csqsrq></Csqsrq>\r\n" + 
				"			<Bszl></Bszl>\r\n" + 
				"			<Bscgbz></Bscgbz>\r\n" + 
				"			<Grfpxx></Grfpxx>\r\n" + 
				"			<Thfpxx></Thfpxx>\r\n" + 
				"		</Hwysyzzszyfp>\r\n" + 
				"		<Jdcxstyfp>\r\n" + 
				"			<Sfdcsq></Sfdcsq>\r\n" + 
				"			<Sfdssq></Sfdssq>\r\n" + 
				"			<Lxxe></Lxxe>\r\n" + 
				"			<Lxsyje></Lxsyje>\r\n" + 
				"			<Dzkpxe></Dzkpxe>\r\n" + 
				"			<Yljkpxx></Yljkpxx>\r\n" + 
				"			<Yljtpxx></Yljtpxx>\r\n" + 
				"			<Byykfpje></Byykfpje>\r\n" + 
				"			<Byyktpje></Byyktpje>\r\n" + 
				"			<Ssrq></Ssrq>\r\n" + 
				"			<Sccbrq></Sccbrq>\r\n" + 
				"			<Csqsrq></Csqsrq>\r\n" + 
				"			<Bszl></Bszl>\r\n" + 
				"			<Bscgbz></Bscgbz>\r\n" + 
				"			<Grfpxx></Grfpxx>\r\n" + 
				"			<Thfpxx></Thfpxx>\r\n" + 
				"		</Jdcxstyfp>\r\n" + 
				"		<Dzfp>\r\n" + 
				"			<Sfdcsq></Sfdcsq>\r\n" + 
				"			<Sfdssq></Sfdssq>\r\n" + 
				"			<Lxxe></Lxxe>\r\n" + 
				"			<Lxsyje></Lxsyje>\r\n" + 
				"			<Dzkpxe></Dzkpxe>\r\n" + 
				"			<Ssrq></Ssrq>\r\n" + 
				"			<Sccbrq></Sccbrq>\r\n" + 
				"			<Csqsrq></Csqsrq>\r\n" + 
				"			<Bszl></Bszl>\r\n" + 
				"			<Bscgbz></Bscgbz>\r\n" + 
				"			<Grfpxx></Grfpxx>\r\n" + 
				"			<Thfpxx></Thfpxx>\r\n" + 
				"		</Dzfp>\r\n" + 
				"	</Jssbztcx>\r\n" + 
				"</service>\r\n" + 
				"";
		Map<String,Object> resultList=parseQueryTaxEquipStatus(xml);		
	}
		
	
	/**
	 * @Title: testInvalidateEmptyInvoice
	 * @Description: TEST-空白发票作废返回报文 
	 */
	public static void testInvalidateEmptyInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"		</refp>\r\n" +
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<InvalidateEmptyInvoice> resultList=parseInvalidateEmptyInvoice(xml);
		System.out.println("upload Invoice result list size is:"+resultList.size());  //预测结果为2
	}
	
	
	/**
	 * @Title: testParseUpdateInvoiceStatus
	 * @Description: 测试发票状态更新响应报文 
	 */
	public static void testParseUpdateInvoiceStatus() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err>\r\n" + 
				"		<RETCODE></RETCODE>\r\n" + 
				"		<RETMSG></RETMSG>\r\n" + 
				"	</err>\r\n" + 
				"	<info>\r\n" + 
				"		<succ>\r\n" + 
				"			<fp>\r\n" + 
				"				<FPDM></FPDM>\r\n" + 
				"				<FPHM></FPHM>\r\n" + 
				"				<XLH></XLH>\r\n" + 
				"			</fp>\r\n" + 
				"			<fp>\r\n" + 
				"				<FPDM></FPDM>\r\n" + 
				"				<FPHM></FPHM>\r\n" + 
				"				<XLH></XLH>\r\n" + 
				"			</fp>\r\n" +
				"		</succ>\r\n" + 
				"		<fail>\r\n" + 
				"			<fp>\r\n" + 
				"				<FPDM></FPDM>\r\n" + 
				"				<FPHM></FPHM>\r\n" + 
				"				<XLH></XLH>\r\n" + 
				"				<CODE></CODE>\r\n" + 
				"				<MESS></MESS>				\r\n" + 
				"			</fp>\r\n" + 
				"			<fp>\r\n" + 
				"				<FPDM></FPDM>\r\n" + 
				"				<FPHM></FPHM>\r\n" + 
				"				<XLH></XLH>\r\n" + 
				"				<CODE></CODE>\r\n" + 
				"				<MESS></MESS>				\r\n" + 
				"			</fp>\r\n" +
				"		</fail>\r\n" + 
				"	</info>\r\n" + 
				"</service>\r\n" + 
				"";
		Map<String,Object> resultMap=parseUpdateInvoiceStatus(xml);
		
	}
	
	
	/**
	 * @Title: testParseUploadInvoice
	 * @Description: 测试-发票上传返回报文 
	 */
	public static void testParseUploadInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" +
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<PrintList> resultList=parsePrintList(xml);
		System.out.println("upload Invoice result list size is:"+resultList.size());  //预测结果为2
	}
	
	/**
	 * @Title: testParsePrintList
	 * @Description: test打印清单响应报文 
	 */
	public static void testParsePrintList() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" + 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<PrintList> resultList=parsePrintList(xml);
		System.out.println("record count is:"+resultList.size());  //预测结果为2
	}
	
	/**
	 * @Title: testParseInvalidateInvoice
	 * @Description: TEST- 发票作废-返回报文
	 */
	public static void testParseInvalidateInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" +
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<InvalidateInvoice> invalidateInvoiceList=parseInvalidateInvoice(xml);
		System.out.println("record count is:"+invalidateInvoiceList.size());  //预测结果为2
	}
	
	
	
	/**
	 * @Title: testParseOpenInvoice
	 * @Description: TEST:打印发票响应报文解析 
	 */
	public static void testParsePrintInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" +
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPZL></FPZL>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"		</refp>\r\n" +
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<PrintInvoice> printInvoiceList=parsePrintInvoice(xml);
		System.out.println("record count is:"+printInvoiceList.size());  //预测结果为2	
	}
	
	
	
	/**
	 * @Title: testParseOpenInvoice
	 * @Description: 测试开据发票 
	 */
	public static void testParseOpenInvoice() {
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<JE></JE>\r\n" + 
				"			<SE></SE>\r\n" + 
				"			<KPRQ></KPRQ>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<JYM></JYM>\r\n" + 
				"			<MW></MW>\r\n" + 
				"			<SIGN></SIGN>\r\n" + 
				"			<SYZFPDM></SYZFPDM>\r\n" + 
				"			<SYZFPHM></SYZFPHM>\r\n" + 
				"			<SYZFPZL></SYZFPZL>\r\n" + 
				"		</refp>\r\n" +				 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		List<OpenInvoice> openInvoiceList=parseOpenInvoice(xml);
		System.out.println("record count is:"+openInvoiceList.size());				
	}
	
	/**
	 * @Title: testParseInvoiceInventory
	 * @Description: TEST-查询库存-响应报文 
	 */
	public static void testParseInvoiceInventory() {
		/*
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"<service>\r\n" + 
				"	<err count=\"2\">\r\n" + 
				"		<refp>\r\n" + 
				"			<RETCODE></RETCODE>\r\n" + 
				"			<RETMSG></RETMSG>\r\n" + 
				"			<FPHM></FPHM>\r\n" + 
				"			<FPDM></FPDM>\r\n" + 
				"			<KCFPSL></KCFPSL>\r\n" + 
				"			<JSSBRQ></JSSBRQ>\r\n" + 
				"			<XFSH></XFSH>\r\n" + 
				"			<SCFS></SCFS>\r\n" + 
				"			<KPDH></KPDH>\r\n" + 
				"			<CSQBZ></CSQBZ>\r\n" + 
				"			<SSQBZ></SSQBZ>\r\n" + 
				"			<KPFS></KPFS>\r\n" + 
				"			<KPFWQH></KPFWQH>\r\n" + 
				"			<JSPH></JSPH>\r\n" + 
				"			<XFMC></XFMC>\r\n" + 
				"		</refp>\r\n" +				 
				"	</err>\r\n" + 
				"</service>\r\n" + 
				"";
		*/
		
		String xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" + 
				"	<service>\r\n" + 
				"		<err count=\"2\">\r\n" + 
				"			<refp><RETCODE>3011</RETCODE>\r\n" + 
				"				<RETMSG>3011-金税盘状态信息读取成功 [TCD_0_15,]</RETMSG>\r\n" + 
				"				<FPHM>14907748</FPHM>\r\n" + 
				"				<FPDM>1300141140</FPDM>\r\n" + 
				"				<KCFPSL>1000</KCFPSL>\r\n" + 
				"				<JSSBRQ>2019-10-21 11:55:23</JSSBRQ>\r\n" + 
				"				<XFSH>130100999999220</XFSH>\r\n" + 
				"				<SCFS>1</SCFS>\r\n" + 
				"				<KPDH>1</KPDH>\r\n" + 
				"				<CSQBZ>0</CSQBZ>\r\n" + 
				"				<SSQBZ>0</SSQBZ>\r\n" + 
				"				<KPFS>0</KPFS>\r\n" + 
				"				<KPFWQH></KPFWQH>\r\n" + 
				"				<JSPH>661555297643</JSPH>\r\n" + 
				"				<XFMC>航信培训企业</XFMC>\r\n" + 
				"			</refp>\r\n" + 
				"			<refp>\r\n" + 
				"				<RETCODE>3011</RETCODE>\r\n" + 
				"				<RETMSG>3011-金税盘状态信息读取成功 [TCD_0_15,]</RETMSG>\r\n" + 
				"				<FPHM>4959119</FPHM>\r\n" + 
				"				<FPDM>1300124620</FPDM>\r\n" + 
				"				<KCFPSL>1000</KCFPSL>\r\n" + 
				"				<JSSBRQ>2019-10-21 11:55:23</JSSBRQ>\r\n" + 
				"				<XFSH>130100999999220</XFSH>\r\n" + 
				"				<SCFS>1</SCFS>\r\n" + 
				"				<KPDH>1</KPDH>\r\n" + 
				"				<CSQBZ>0</CSQBZ>\r\n" + 
				"				<SSQBZ>0</SSQBZ>\r\n" + 
				"				<KPFS>0</KPFS>\r\n" + 
				"				<KPFWQH></KPFWQH>\r\n" + 
				"				<JSPH>661555297643</JSPH>\r\n" + 
				"				<XFMC>航信培训企业</XFMC>\r\n" + 
				"			</refp>\r\n" + 
				"		</err>\r\n" + 
				"	</service>\r\n" + 
				"";
		
		
		List<InvoiceInventory> invoiceInventoryList=parseInvoiceInventory(xml);
		System.out.println("record count is:"+invoiceInventoryList.size());				
	}
	
	//--------------test function---------------	
	public static void main(String[] args) {
		testParseInvoiceInventory();  	//发票库存查询
		//testParseOpenInvoice();  			//测试开据发票.
		//testParsePrintInvoice();  		//测试打印发票.
		//testParseInvalidateInvoice();  	//测试作废发票
		//testParsePrintList();   			//清单打印返回报文
		//testParseUploadInvoice();  		//发票上传返回报文
		//testParseUpdateInvoiceStatus();  	//发票状态更新返回报文
		//testInvalidateEmptyInvoice();  	//空白发票作废返回报文
		//testParseQueryTaxEquipStatus();  	//测试金税盘状态查询返回报文
		//testParseConfigPrintParam();  	//测试设置打印参数响应报文
		//testParseUploadRedInfo();  		//红字信息表上传响应报文
		//testparseSummaryReport();  		//汇总抄表响应报文
		//testParseRemoteClearCard();  		//远程清卡返回报文
		//testParseDownloadRedInfo();		//红字信息表下载		
		//testParseQuerySingleInvoice();  	//发票单张查询		
		//testParseCloseControlService(); 	//关闭控制台服务
		//testParseRepairSingleInvoice();	//单张发票修复
		//testParseOpenInvoiceControl();	//发票控制
		//testParseBatchQueryInvoice();       //发票批量查询
		//testParseSingleInvoice();  //test:解析单张发票 
		
	}
	
	
	
}
