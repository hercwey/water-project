package com.learnbind.ai.controller.moduleproductno;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.bean.UserBean;
import com.learnbind.ai.cmbc.ExportExcel;
import com.learnbind.ai.common.RequestResultUtil;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.common.util.fileutil.DownLoadFileUtil;
import com.learnbind.ai.config.upload.UploadFileConfig;
import com.learnbind.ai.model.ModuleProductNo;
import com.learnbind.ai.service.moduleproductno.ModuleProductNoService;

/**
 * Copyright (c) 2020 by SRD
 * 
 * @Package com.learnbind.ai.controller.moduleproductno
 *
 * @Title: ModuleProdutNoSearchController.java
 * @Description: 产品查询
 *
 * @author SRD
 * @date 2020年2月27日 下午7:15:34
 * @version V1.0 
 *
 */
@Controller
@RequestMapping(value = "/module-product-search")
public class ModuleProdutNoSearchController {

	private static Log log = LogFactory.getLog(ModuleProdutNoSearchController.class);

	private static final String TEMPLATE_PATH = "moduleproductno/search/";// 页面目录
	private static final int PAGE_SIZE = 10;// 页大小

	@Autowired
	private UploadFileConfig uploadFileConfig;//文件上传配置信息
	@Autowired
	private ModuleProductNoService moduleProductNoService;// 模组号-出厂编号对照表

	/**
	 * @Title: starter
	 * @Description: 开始页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/starter")
	public String starter(Model model) {
		return TEMPLATE_PATH + "starter";
	}

	/**
	 * @Title: main
	 * @Description: 主页面
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/main")
	public String main(Model model) {
		return TEMPLATE_PATH + "main";
	}

	/**
	 * @Title: table
	 * @Description: 列表页面
	 * @param model
	 * @param pageNum
	 * @param pageSize
	 * @param searchCond
	 * @return
	 */
	@RequestMapping(value = "/table")
	public String table(Model model, Integer pageNum, Integer pageSize, String operatorName, String operatorDate, String moduleNo, String productNo) {

		UserBean userBean = (UserBean)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		// 判定页码有效性
		if (pageNum == null || pageNum == 0 || pageSize==null || pageSize==0) {
			pageNum = 1;
			pageSize = PAGE_SIZE;//PagerConstant.DEFAULT_PAGE_SIZE;
		}

		Date operatorDateD = null;
		if(StringUtils.isNotBlank(operatorDate)) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//日期格式化
			try {
				operatorDateD = sdf.parse(operatorDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<ModuleProductNo> mpNoList = moduleProductNoService.searchList(operatorName, operatorDateD, moduleNo, productNo);
		PageInfo<ModuleProductNo> pageInfo = new PageInfo<>(mpNoList);// (使用了拦截器或是AOP进行查询的再次处理)

		// 传递如下数据至前台页面
		model.addAttribute("moduleProductNoList", mpNoList); // 列表数据

		// 分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);

		// 查询条件回传
		model.addAttribute("searchCond", null);

		return TEMPLATE_PATH + "table";
	}

	/**
	 * @Title: delete
	 * @Description: 删除
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete", produces = "application/json")
	@ResponseBody
	public Object delete(@RequestBody ArrayList<Long> ids) throws Exception {
		try {
			for (Long id : ids) {
				moduleProductNoService.deleteByPrimaryKey(id);
			}
		} catch (Exception e) {
			return RequestResultUtil.getResultDeleteWarn();
		}

		return RequestResultUtil.getResultDeleteSuccess();

	}

	/**
	 * @Title: loadModiDialog
	 * @Description: 加载编辑对话框
	 * @param itemId
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/load-edit-dialog")
	public String loadModiDialog(Long itemId, Model model) {

		// 读取需要编辑的条目
		ModuleProductNo currItem = moduleProductNoService.selectByPrimaryKey(itemId);
		model.addAttribute("currItem", currItem);

		return TEMPLATE_PATH + "edit_dialog";
	}

	/**
	 * @Title: update
	 * @Description: 修改出厂编号
	 * @param id
	 * @param productNo
	 * @return
	 */
	@RequestMapping(value = "/update", produces = "application/json")
	@ResponseBody
	public Object update(Long id, String productNo) {

		try {
			ModuleProductNo mpNo = new ModuleProductNo();
			mpNo.setId(id);
			mpNo.setProductNo(productNo);
			int rows = moduleProductNoService.updateByPrimaryKeySelective(mpNo);
			if (rows > 0) {
				return RequestResultUtil.getResultUpdateSuccess();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return RequestResultUtil.getResultUpdateWarn();
	}
	
	//------------------------------	导出Excel	------------------------------
	/**
	 * @Title: exportExcel
	 * @Description: 导出Excel
	 * @param request
	 * @param response
	 * @param operatorName
	 * @param operatorDate
	 * @param moduleNo
	 * @param productNo 
	 */
	@RequestMapping("/export-excel")
	public void exportExcel(HttpServletRequest request, HttpServletResponse response, String operatorName, String operatorDate, String moduleNo, String productNo) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");//使用了默认的格式创建了一个日期格式化对象。
		Date operatorDateD = null;
		if(StringUtils.isNotBlank(operatorDate)) {
			try {
				operatorDateD = sdf.parse(operatorDate);
			} catch (ParseException e) {
				e.printStackTrace();
				operatorDateD = null;
			}
		}
		
		//excel标题
		String[] titles = { "序号", "模组号", "出厂编号", "操作员姓名", "操作日期"};
		//excel列名
		String[] columnNames = { "countor", "moduleNo", "productNo", "operatorName", "operatorDateStr"};
		//sheet名
		String sheetName = "模组号与出厂编号对照表";
		
		//获取导出EXCEL的数据
		List<Map<String, Object>> excelDataList = this.getExportExcelData(operatorName, operatorDateD, moduleNo, productNo);
		//获取导出EXCEL的工作簿
		HSSFWorkbook wb = ExportExcel.exportExcel(titles, columnNames, sheetName, excelDataList);
		//获取导出EXCEL的文件路径
		String realPath = this.getRealPath(request);
		//获取导出EXCEL的文件名
		String fileName = this.getFileName(operatorDateD);
		
		File file = new File(realPath+fileName);
		
		//文件输出流
	    FileOutputStream outStream = null;
		try {
			outStream = new FileOutputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
	    try {
			wb.write(outStream);
			outStream.flush();
			outStream.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    System.out.println("导出文件成功！文件导出路径：--"+file);
	    
	    try {
			DownLoadFileUtil.downLoad(fileName, "xls", realPath+fileName, response);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	/**
	 * @Title: getExportExcelData
	 * @Description: 获取导出excel
	 * @return 
	 */
	private List<Map<String, Object>> getExportExcelData(String operatorName, Date operatorDate, String moduleNo, String productNo){
		
		List<ModuleProductNo> mpNoList = moduleProductNoService.searchList(operatorName, operatorDate, moduleNo, productNo);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//使用了默认的格式创建了一个日期格式化对象。
		List<Map<String, Object>> mpNoMapList = new ArrayList<>();
		for(int i=0; i<mpNoList.size(); i++) {
			
			ModuleProductNo mpNo = mpNoList.get(i);
			
			Map<String, Object> mpNoMap = EntityUtils.entityToMap(mpNo);
			String operatorDateStr = sdf.format(mpNo.getOperatorDate());
			mpNoMap.put("operatorDateStr", operatorDateStr);
			mpNoMap.put("countor", (i+1));
			mpNoMapList.add(mpNoMap);
		}
		
		return mpNoMapList;
	}
	
	/**
	 * 获取文件路径
	 * @param request
	 * @return
	 */
	private String getRealPath(HttpServletRequest request){
		String realPath = this.getPath();
		realPath = realPath+File.separator+"export excel"+File.separator;
		
		File temp = new File(realPath);
		//如果文件路径不存在，则创建
		if(!temp.exists()){
			temp.mkdirs();
		}
		return realPath;
	}
	/**
	 * 获取导出EXCEL文件名
	 * @return
	 */
	private String getFileName(Date operatorDate){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String times = sdf.format(new Date());
		if(operatorDate!=null) {
			times = sdf.format(operatorDate);
		}
		
		// excel文件名
		String fileName = times+"-模组号与出厂编号对照表"+".xls";
		return fileName;
	}
	
	//------------------------------	上传文件通用方法	------------------------------
	/**
	 * @Title: getPath
	 * @Description: 根据操作系统类型获取上传文件目录
	 * @return 
	 */
	private String getPath() {
		String path = uploadFileConfig.getLinuxUploadFolder();
		if(this.isWindowsOS()) {
			path = uploadFileConfig.getWindowsUploadFolder();
		}
		System.out.println("----------"+path);
		return path;
	}
	
	/**
	 * @Title: isWindowsOS
	 * @Description: 判断操作系统是否是Windows
	 * @return 
	 * 		true=windows;false=其他
	 */
	private boolean isWindowsOS() {
		String os = System.getProperty("os.name");//获取操作系统是Linux还是Windows  
    	if(os.toUpperCase().startsWith("WIN")){  
    		System.out.println("==============================操作系统："+os);
    		return true;
    	}else {
    		System.out.println("==============================操作系统："+os);
    		return false;
    	}
	}
	
	public static void main(String[] args) {
		String path = "d:/upload/img/20190701/abc.jpg";
		//String path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("windows:"+path);
		path = "/home/upload/img/20190701/abc.jpg";
		path = path.substring(path.indexOf("upload"));
		System.out.println("linux:"+path);
		
		String[] ids = new String[3];
		ids[0]="1";
		ids[1]="2";
		ids[2]="3";
		System.out.println("数组toString:"+ids.toString());
	}
	

}