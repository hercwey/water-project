package com.learnbind.ai.cmbc;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;

/**
 * 导出EXCEL
 * 
 * @author srd
 *
 */
public class ExportExcel {

	/**
	 * 导出Excel
	 * 
	 * @param mapList
	 */
	/**
	 * @Title: exportExcel
	 * @Description: 导出EXCEL
	 * @param titles	excel标题
	 * @param columnNames	excel列名
	 * @param sheetName	sheet名
	 * @param dataMapList	导出数据集合
	 * @return 
	 */
	public static HSSFWorkbook exportExcel(String[] titles, String[] columnNames, String sheetName, List<Map<String, Object>> dataMapList) {
		/*// excel标题
		String[] titles = { "考核年度", "人员", "角色", "周期名称", "指标比例（%）", "指标金额（元）", "利润金额（元）"};
		// excel列名
		String[] columnNames = { "year_name", "username", "role_name", "cycle_name", "target_rate", "target_amount", "profit_amount"};

		// sheet名
		String sheetName = "考核指标信息";*/

		// 创建HSSFWorkbook
		HSSFWorkbook wb = getHSSFWorkbook(sheetName, titles, columnNames, dataMapList);
		return wb;
	}

	/**
	 * 设置excel内容
	 * 
	 * @param sheetName
	 *            sheet名称
	 * @param titles
	 *            标题
	 * @param columnNames
	 *            导出对象中的列名
	 * @param mapList
	 *            导出对象集合
	 * @return
	 */
	public static HSSFWorkbook getHSSFWorkbook(String sheetName, String[] titles, String[] columnNames,
			List<Map<String, Object>> mapList) {

		// 第一步，创建一个HSSFWorkbook，对应一个Excel文件
		HSSFWorkbook wb = new HSSFWorkbook();
		//新增的四句话，设置CELL格式为文本格式  
		//HSSFCellStyle cellStyle2 = wb.createCellStyle();
		//HSSFDataFormat format = wb.createDataFormat();
		//cellStyle2.setDataFormat(format.getFormat("@"));
  	//cell.setCellStyle(cellStyle2);
		// 第二步，在workbook中添加一个sheet,对应Excel文件中的sheet
		HSSFSheet sheet = wb.createSheet(sheetName);
		
		sheet.setDefaultColumnWidth(15);
		sheet.setDefaultRowHeightInPoints(15f);
		
		// 第三步，在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制
		HSSFRow row = sheet.createRow(0);

		// 第四步，创建单元格，并设置值表头 设置表头居中
		HSSFCellStyle cellStyle = wb.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);//创建一个居中格式
		
		//设置字体
		HSSFFont font = wb.createFont();    
		font.setBold(true);//粗体显示    

		cellStyle.setFont(font);//选择需要用到的字体格式 

		HSSFCellStyle cellStyle2 = wb.createCellStyle();
		HSSFDataFormat format = wb.createDataFormat();
		cellStyle2.setDataFormat(format.getFormat("@"));
		
		// 声明列对象
		HSSFCell cell = null;

		// 创建标题
		for (int i = 0; i < titles.length; i++) {
			cell = row.createCell(i);
			//cell.setCellStyle(cellStyle2);
			cell.setCellType(CellType.STRING);
			cell.setCellValue(titles[i]);
			cell.setCellStyle(cellStyle);
			//sheet.autoSizeColumn(i,true);//宽度自适应
		}

		// 创建内容
		for (int i = 0; i < mapList.size(); i++) {
			row = sheet.createRow(i + 1);
			Map<String, Object> object = mapList.get(i);
			// 声明列对象
			cell = null;
			for (int j = 0; j < columnNames.length; j++) {
				// 将内容按顺序赋给对应的列对象
				cell = row.createCell(j);
				cell.setCellType(CellType.STRING);
				cell.setCellStyle(cellStyle2);
				System.out.println(columnNames[j]);
				cell.setCellValue(object.get(columnNames[j])==null ? "" : object.get(columnNames[j]).toString());
				//sheet.autoSizeColumn(j,true);//宽度自适应
			}
		}
		return wb;
	}
}
