package com.learnbind.ai.common.util.excelutil;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.alibaba.fastjson.JSON;
import com.learnbind.ai.bean.TestMeterAddrBean;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.util.excelutil
 *
 * @Title: ReadLocalMeterExcel.java
 * @Description: 读取营收系统Excel
 *
 * @author Administrator
 * @date 2019年9月24日 下午12:01:52
 * @version V1.0 
 *
 */
public class ReadLocalMeterExcel {
    
	private static final Log log = LogFactory.getLog(ReadLocalMeterExcel.class);
	
    /**
     * @Title: readExcel
     * @Description: 读取Excel
     * @param reqMap
     * 		{"excelPath":path}
     * @return
     * @throws IOException 
     */
    public static Map<String, Object> readExcel(Map<String, Object> reqMap) throws IOException {
    	Map<String, Object> respMap = new HashMap<String, Object>();
    	String path = reqMap.get(Common.EXCEL_PATH).toString();
    	if (path == null || Common.EMPTY.equals(path)) {
        	respMap.put("result_code", "fail");
			respMap.put("result_msg", "上传excel文件URL为空");
			return respMap;
        } else {
            String postfix = Util.getPostfix(path);
            if (!Common.EMPTY.equals(postfix)) {
                if (Common.OFFICE_EXCEL_2003_POSTFIX.equals(postfix)) {
                    return readXls(reqMap);
                } else if (Common.OFFICE_EXCEL_2010_POSTFIX.equals(postfix)) {
                    return readXlsx(reqMap);
                }
            } else {
                log.error(path + Common.NOT_EXCEL_FILE);
            }
        }
        respMap.put("result_code", "fail");
		respMap.put("result_msg", "解析excel文件异常");
        return respMap;
    }

    /**
     * @Title: readXlsx
     * @Description: Read the Excel 2010
     * @param reqMap
     * 		{"excelPath":path}
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("resource")
	public static Map<String, Object> readXlsx(Map<String, Object> reqMap) throws IOException {
        log.info(Common.PROCESSING + reqMap.get(Common.EXCEL_PATH).toString());
        InputStream is = new FileInputStream(reqMap.get(Common.EXCEL_PATH).toString());
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(is);
        try {
        	return parseExcelInfoXlsx(xssfWorkbook);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解析excel异常", e);
		}
        	
    	Map<String, Object> respMap = new HashMap<String, Object>();
        respMap.put("result_code", "fail");
		respMap.put("result_msg", "解析excel异常");
		return respMap;
        
    }
    
    /**
     * @Title: parseExcelInfoXlsx
     * @Description: 解析扩展名为xlsx的文件信息excel文件
     * @param xssfWorkbook
     * @return 
     */
    private static Map<String, Object> parseExcelInfoXlsx(XSSFWorkbook xssfWorkbook){
    	Map<String, Object> respMap = new HashMap<String, Object>();
        
    	List<TestMeterAddrBean> meterMapList = new ArrayList<>();//表计
    	
        int sheetSize = xssfWorkbook.getNumberOfSheets();
        log.info("Excel sheet num : "+sheetSize);
        // Read the Sheet
        for (int numSheet = 0; numSheet < sheetSize; numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            int rowSize = xssfSheet.getLastRowNum();
            log.info("Excel sheet row num : "+rowSize);
            // Read the Row
            for (int rowNum = 1; rowNum <= rowSize; rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                	
                	try {
                		
                		TestMeterAddrBean meterMap = getMeterParm(xssfRow);
                		meterMapList.add(meterMap);
                    	
					} catch (Exception e) {
						e.printStackTrace();
						log.error("解析excel异常，请求检查 "+xssfWorkbook.getSheetName(numSheet)+" 工作表，第 "+(rowNum+1)+" 行数据。", e);
						respMap.put("result_code", "fail");
						respMap.put("result_msg", "解析excel异常，请求检查 "+xssfWorkbook.getSheetName(numSheet)+" 工作表，第 "+(rowNum+1)+" 行数据。");
						return respMap;
					}
                }
            }
        }
        respMap.put("result_code", "success");
        respMap.put("result_msg", "解析入住信息excel文件成功");
        respMap.put("result_data", JSON.toJSONString(meterMapList));
        return respMap;
    }
    
    /**
     * @Title: getMeterParm
     * @Description: 获取表计对象
     * @param xssfRow
     * @return 
     */
    private static TestMeterAddrBean getMeterParm(XSSFRow xssfRow) {
    	String locationName = getValue(xssfRow.getCell(0));//地理位置名称（小区-楼号-单元-房间号）
    	
    	String collectorAddr = getValue(xssfRow.getCell(13));//采集器地址
    	String meterAddress = getValue(xssfRow.getCell(14));//表地址
    	
    	String room = locationName.substring((locationName.indexOf("-")+1));
    	String meterAddr = collectorAddr+meterAddress;
    	
    	TestMeterAddrBean bean = new TestMeterAddrBean();
    	bean.setRoom(room);
    	bean.setMeterAddr(meterAddr);
    	
    	return bean;
    }
    
    /**
     * @Title: readXls
     * @Description: Read the Excel 2003-2007
     * @param reqMap
     * 		{"excelPath":path}
     * @return
     * @throws IOException 
     */
    @SuppressWarnings("resource")
	public static Map<String, Object> readXls(Map<String, Object> reqMap) throws IOException {
        log.info(Common.PROCESSING + reqMap.get(Common.EXCEL_PATH).toString());
        InputStream is = new FileInputStream(reqMap.get(Common.EXCEL_PATH).toString());
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(is);
        try {
        	return parseExcelInfoXls(hssfWorkbook);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("解析excel异常", e);
		}
        	
        Map<String, Object> respMap = new HashMap<String, Object>();
        respMap.put("result_code", "fail");
		respMap.put("result_msg", "解析excel异常");
		return respMap;
        
    }

	/**
	 * @Title: parseExcelInfoXls
	 * @Description: 解析扩展名为xls的入住信息excel文件
	 * @param hssfWorkbook
	 * @return 
	 */
	private static Map<String, Object> parseExcelInfoXls(HSSFWorkbook hssfWorkbook){
    	//用于返回execl的读取状态.
    	Map<String, Object> respMap = new HashMap<String, Object>();
    	
    	List<TestMeterAddrBean> meterMapList = new ArrayList<>();//表计集合 
        
        int sheetSize = hssfWorkbook.getNumberOfSheets();
        log.info("Excel sheet num : "+sheetSize);
        // Read the Sheet
        for (int numSheet = 0; numSheet < sheetSize; numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            int rowSize = hssfSheet.getLastRowNum();
            log.info("Excel sheet row num : "+rowSize);
            // Read the Row
            for (int rowNum = 1; rowNum <= rowSize; rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");                	
                	try {
                		
                		TestMeterAddrBean meterMap = getMeterParm(hssfRow);//获取表计参数
                		meterMapList.add(meterMap);
                    	
					} catch (Exception e) {
						e.printStackTrace();
						log.error("解析excel异常，请求检查 "+hssfSheet.getSheetName()+" 工作表，第 "+(rowNum+1)+" 行数据。", e);
						respMap.put("result_code", "fail");
						respMap.put("result_msg", "解析excel异常，请求检查 "+hssfSheet.getSheetName()+" 工作表，第 "+(rowNum+1)+" 行数据。");
						return respMap;
					}
                }
            }
        }
        respMap.put("result_code", "success");
        respMap.put("result_msg", "解析excel文件成功");
        respMap.put("result_data", JSON.toJSONString(meterMapList));
        return respMap;
    }
	
	/**
     * @Title: getMeterParm
     * @Description: 获取表计对象
     * @param hssfRow
     * @return 
     */
    private static TestMeterAddrBean getMeterParm(HSSFRow hssfRow) {
    	
    	String locationName = getValue(hssfRow.getCell(0));//地理位置名称（小区-楼号-单元-房间号）
    	
    	String collectorAddr = getValue(hssfRow.getCell(13));//采集器地址
    	String meterAddress = getValue(hssfRow.getCell(14));//表地址
    	
    	String room = locationName.substring((locationName.indexOf("-")+1));
    	String meterAddr = collectorAddr+meterAddress;
    	
    	TestMeterAddrBean bean = new TestMeterAddrBean();
    	bean.setRoom(room);
    	bean.setMeterAddr(meterAddr);
    	return bean;
    }
    
    /**
     * @Title: getValue
     * @Description: 判断并获取EXCEL单元格内容
     * @param xssfCell
     * @return 
     */
    @SuppressWarnings({ "static-access", "deprecation" })
    private static String getValue(XSSFCell xssfCell) {
    	try {
    		
    		if(xssfCell==null) {
    			return null;
    		}
    		
    		DecimalFormat df = new DecimalFormat("#");
            if (xssfCell.getCellType() == CellType.BOOLEAN) {//hssfCell.CELL_TYPE_BOOLEAN
                return df.format(xssfCell.getBooleanCellValue());
            } else if (xssfCell.getCellType() == CellType.NUMERIC) {//xssfRow.CELL_TYPE_NUMERIC
            	if(DateUtil.isCellInternalDateFormatted(xssfCell)){
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            		return String.valueOf(sdf.format(xssfCell.getDateCellValue()));
            	}
            	return new BigDecimal(xssfCell.getNumericCellValue()).toPlainString();
            } else {
                return String.valueOf(xssfCell.getStringCellValue());
            }
		} catch (Exception e) {
			log.info("判断并获取EXCEL单元格内容出错 或单元格内容为空", e);
		}
    	
    	return null;
    	
    }

    /**
     * @Title: getValue
     * @Description: 判断并获取EXCEL单元格内容
     * @param hssfCell
     * @return 
     */
    @SuppressWarnings("static-access")
    private static String getValue(HSSFCell hssfCell) {
    	try {
    		
    		if(hssfCell==null) {
    			return null;
    		}
    		
    		DecimalFormat df = new DecimalFormat("#");
            if (hssfCell.getCellType() == CellType.BOOLEAN) {//hssfCell.CELL_TYPE_BOOLEAN
                return df.format(hssfCell.getBooleanCellValue());
            } else if (hssfCell.getCellType() == CellType.NUMERIC) {//hssfCell.CELL_TYPE_NUMERIC
            	if(DateUtil.isCellInternalDateFormatted(hssfCell)){
            		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            		return String.valueOf(sdf.format(hssfCell.getDateCellValue()));
            	}
            	return new BigDecimal(hssfCell.getNumericCellValue()).toPlainString();
            } else {
                return String.valueOf(hssfCell.getStringCellValue());
            }
		} catch (Exception e) {
			log.info("判断并获取EXCEL单元格内容出错", e);
		}
    	return null;
    	
    }
    
    /**
     * main方法
     * @param args
     */
    public static void main(String[] args) {
    	//System.out.println(HSSFCell.CELL_TYPE_STRING);
    	//System.out.println(Double.valueOf("8.0").intValue());
    	String s1 = "10.0";
    	String s2 = "房间1";
    	
    	String s3 = null;
    	try {
    		s3 = String.valueOf(Double.valueOf(s1).intValue());
    		System.out.println("try");
		} catch (Exception e) {
			s3 = String.valueOf(Double.valueOf(s1).toString());
			System.out.println("catch");
		}
    	
    	System.out.println(s3);
    	
    	DecimalFormat df = new DecimalFormat("0");  
    	  
    	String whatYourWant = df.format(Double.valueOf("10.0")); 
    	System.out.println(whatYourWant);
    	
    	/*SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currdate = null;
		try {
			currdate = format.parse("2016-10-09 00:00:00");
		} catch (ParseException e) {
			e.printStackTrace();
		}
        System.out.println("现在的日期是：" + format.format(currdate));
        Calendar ca = Calendar.getInstance();
        ca.setTime(currdate);
        ca.add(Calendar.MONTH, 2);// num为增加的天数，可以改变的
        currdate = ca.getTime();
        String enddate = format.format(currdate);
        System.out.println("增加天数以后的日期：" + enddate);*/
	}
}
