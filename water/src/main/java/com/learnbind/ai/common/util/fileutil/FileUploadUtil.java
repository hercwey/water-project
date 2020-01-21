package com.learnbind.ai.common.util.fileutil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.ai.upload.utils
 *
 * @Title: FileUploadUtil.java
 * @Description: 文件上传工具类
 *
 * @author Administrator
 * @date 2019年5月23日 下午6:44:45
 * @version V1.0 
 *
 */
public class FileUploadUtil {

	private static final Log log = LogFactory.getLog(FileUploadUtil.class);
	
	/**
	 * @Title: uploadFile
	 * @Description: 上传文件保存到固定目录
	 * @param request
	 * @param file
	 * @param realPath
	 * @param typeDir
	 * @return
	 * @throws IOException 
	 */
	public static String uploadFile(HttpServletRequest request, MultipartFile file, String realPath, String typeDir)
			throws IOException {

		String savePath = "";
		try {
			savePath = getSavePath(typeDir);
			realPath = realPath + savePath;
			if(StringUtils.isBlank(realPath)){
				return null;
			}
		} catch (Exception e) {
			log.error("获取保存路径异常", e);
		}
			
		File filePath =new File(realPath);    
		//如果文件夹不存在则创建    
		if (!filePath.exists() && !filePath.isDirectory()) {
			boolean flag = filePath.mkdirs();
			log.debug("目录不存在   创建目录: "+filePath+" 结果："+flag);
		} else {
			log.debug("目录存在 : "+filePath);
		}
		
		String fileName = getRandomFileName(file);//获取由时间和5位随机数字组成的文件名 2015112512345.jpg
		
		fileName = uploadFile(file, fileName, realPath);
		if(StringUtils.isBlank(fileName)){
			return null;
		}
		
		String reqPath = subPath(realPath + File.separator + fileName);
		return reqPath;
	}
	
	public static String uploadFileAbc(MultipartFile file, String realPath, String typeDir) throws IOException {

		String savePath = "";
		try {
			savePath = getSavePath(typeDir);
			realPath = realPath + savePath;
			if(StringUtils.isBlank(realPath)){
				return null;
			}
		} catch (Exception e) {
			log.error("获取保存路径异常", e);
		}
			
		File filePath =new File(realPath);    
		//如果文件夹不存在则创建    
		if (!filePath.exists() && !filePath.isDirectory()) {
			boolean flag = filePath.mkdirs();
			log.debug("目录不存在   创建目录: "+filePath+" 结果："+flag);
		} else {
			log.debug("目录存在 : "+filePath);
		}
		
		String fileName = getRandomFileName(file);//获取由时间和5位随机数字组成的文件名 2015112512345.jpg
		
		fileName = uploadFile(file, fileName, realPath);
		if(StringUtils.isBlank(fileName)){
			return null;
		}
		
		String reqPath = subPath(realPath + File.separator + fileName);
		return reqPath;
	}
	
	/**
	 * @Title: subPath
	 * @Description: 替换目录间隔符号，并截取返回
	 * @param realPath
	 * 		如：d://upload/\img\20190527\2019052715473645939515.jpg
	 * @return 
	 * 		如：/upload/img/20190524/2019052412273012427098.jpg
	 */
	public static String subPath(String realPath) {
		realPath = StringUtils.replace(realPath, File.separator, "/");
		realPath = StringUtils.replace(realPath, "//", "/");
		//realPath = realPath.substring(realPath.indexOf(":")+1);
		return realPath;
	}

	/**
	 * @Title: uploadFile
	 * @Description: 转存文件
	 * @param file
	 * @param realPath
	 * @return
	 * @throws IOException 
	 */
	public static String uploadFile(MultipartFile file, String realPath)
			throws IOException {
		try {
			String fileName = getRandomFileName(file);
			if(StringUtils.isBlank(fileName)){
				return null;
			}
			//FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
			file.transferTo(new File(realPath, fileName));

			return realPath+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Title: uploadFile
	 * @Description: 上传文件
	 * @param file
	 * @param fileName
	 * @param realPath
	 * @return
	 * @throws IOException 
	 */
	public static String uploadFile(MultipartFile file, String fileName, String realPath) throws IOException {
		FileUtils.copyInputStreamToFile(file.getInputStream(), new File(realPath, fileName));
		return fileName;
	}

	/**
	 * @Title: getRealPath
	 * @Description: TODO(这里用一句话描述这个方法的作用)
	 * @param uploadFolder	
	 * 		d:/upload/
	 * @param savePath	
	 * 		http://localhost:8888/upload/img/20190524/2019052412223184678347.jpg
	 * 		/img/20190524/2019052412273012427098.jpg
	 * @return 
	 */
	public static String getRealPath(String uploadFolder, String savePath) {
		try {
			//uploadFolder = uploadFolder.substring(0, uploadFolder.lastIndexOf(File.separator));
			savePath = savePath.substring(0, savePath.indexOf("upload"));
			String realPath = uploadFolder + savePath;
			return realPath;
		} catch (Exception e) {
			log.error("获取文件存储的真实文件夹路径异常", e);
		}
		return null;
	}

	/**
	 * @Title: getRealPath
	 * @Description: 获取文件存储的真实文件夹路径(不包括项目名)
	 * @param request
	 * @return 
	 * 		D:/Java/apache-tomcat-8.0.27/webapps/
	 */
	public static String getRealPath(HttpServletRequest request) {
		try {
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String temp = realPath.substring(0, realPath.lastIndexOf(File.separator));
			temp = temp.substring(0, temp.lastIndexOf(File.separator));
			return temp;
		} catch (Exception e) {
			log.error("获取文件存储的真实文件夹路径异常", e);
		}
		return null;
	}

	/**
	 * @Title: getRandomFileName
	 * @Description: 获取由时间和5位随机数字组成的文件名 2015112512345.jpg
	 * @param file
	 * @return 
	 */
	public static String getRandomFileName(MultipartFile file) {

		try {
			String fileName = "";

			String oldFilename = file.getOriginalFilename();
			if(StringUtils.isBlank(oldFilename)){
				return null;
			}
			String bufix = oldFilename.substring(oldFilename.indexOf("."));
			String random = Integer.toString(new Random().nextInt(89999) + 10000);
			fileName = DateUtils.getDateRandom() + random + bufix;
			return fileName;// 当前时间
		} catch (Exception e) {
			log.error("获取由时间和5位随机数字组成的文件名异常", e);
		}
		return null;
	}

	/**
	 * @Title: getSavePath
	 * @Description: 获取业务中某个字段存储的文件所属文件夹路径，便于前台显示/upload/(typeDir)/20151125
	 * @return
	 * @throws Exception 
	 */
	public static String getSavePath() throws Exception {
		return getSavePath(null);
	}

	/**
	 * @Title: getSavePath
	 * @Description: 获取业务中某个字段存储的文件所属文件夹路径，便于前台显示 /upload/(typeDir)/20151125
	 * @param typeDir
	 * @return
	 * 		文件夹名称
	 * @throws Exception 
	 */
	public static String getSavePath(String typeDir) throws Exception{

		String savePath = "";
		String subFolderName = DateUtils.getDate("yyyyMMdd");

		if (StringUtils.isNotBlank(typeDir)) {
			savePath = File.separator + typeDir + File.separator + subFolderName;
		} else {
			savePath = File.separator + subFolderName;
		}

		return savePath;
	}
	
	/**
	 * @Title: subImgPath
	 * @Description: 截取上传图片路径
	 * @param path
	 * 		如：windows=d:/upload/img/20190701/abc.jpg
	 * 		如：linux=/home/upload/img/20190701/abc.jpg
	 * @return 
	 * 		如：upload/img/20190701/abc.jpg
	 */
	public static String subImgPath(String path) {
		path = path.substring(path.indexOf("upload"));
		path = "/"+path;
		return path;
	}

	/**
	 * @Title: deleteFile
	 * @Description: 删除单个文件
	 * @param realPath
	 * @param filePath
	 * 		业务中某个字段存储的logo文件路径 /upload/(typeDir)/20151125/logo.jpg
	 * @return 
	 * 		单个文件删除成功返回true，否则返回false
	 */
	public static boolean deleteFile(String realPath, String filePath){
		boolean flag = false;
		try {
			String reqPath = StringUtils.replace(filePath, "/", File.separator);
			String path = getRealPath(realPath, reqPath);
			File file = new File(path);
			// 路径为文件且不为空则进行删除
			if (file.isFile() && file.exists()) {
				boolean isDel = file.delete();
				if(!isDel){
					log.error("删除上传文件异常，文件已删除或文件不存在  文件路径："+filePath);
				}
				flag = true;
			}
		} catch (Exception e) {
			log.error("删除上传文件异常"+filePath, e);
		}
		return flag;
	}
	
	/**
	 * @Title: fileExists
	 * @Description: 判断文件是否存在
	 * @param request
	 * @param filePath
	 * 		业务中某个字段存储的logo文件路径 /upload/(typeDir)/20151125/logo.jpg
	 * @return 
	 * 		存在返回true，否则返回false
	 */
	public static boolean fileExists(HttpServletRequest request, String filePath) {
		boolean flag = false;
		try {
			String reqPath = StringUtils.replace(filePath, "/", File.separator);
			String path = null;//getRealPath(request, reqPath);
			File file = new File(path);
			// 路径为文件且存在
			if (file.isFile() && file.exists()) {
				flag = true;
				return flag;
			}
		} catch (Exception e) {
			log.error("上传文件不存在  文件路径："+filePath, e);
		}
		return flag;
	}

	/**
	 * @Title: getReqServerURL
	 * @Description: 获取访问域名
	 * @param request
	 * @return
	 * 		"http://主机名：端口/(contextPath)"
	 * @throws Exception 
	 */
	public static String getReqServerURL(HttpServletRequest request) throws Exception {

		StringBuffer url = new StringBuffer();
		String scheme = request.getScheme();
		int port = request.getServerPort();
		if (port < 0) {
			port = 80;
		}

		url.append(scheme);
		url.append("://");
		url.append(request.getServerName());
		//url.append("zl.cnwell.com");
		if ((scheme.equals("http") && (port != 80)) || (scheme.equals("https") && (port != 443))) {
			url.append(':');
			url.append(port);
		}
		url.append(request.getContextPath());
		return url.toString();
	}
	
	/**
	 * @Title: getFile2Upload
	 * @Description: 根据fileName获取上传文件并上传
	 * @param request
	 * @param typeDir
	 * 		类型目录
	 * @param fileName
	 * 		上传文件input标签的name值
	 * @return
	 * @throws IOException 
	 */
	public static String getFile2Upload(HttpServletRequest request, String uploadFolder, String typeDir, String fileName) throws IOException{
		// 创建一个通用的多部分解析器.  
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
        // 设置编码  
        commonsMultipartResolver.setDefaultEncoding("utf-8");  
        // 判断是否有文件上传  
        if (commonsMultipartResolver.isMultipart(request)) {//有文件上传  
        	// 转型为MultipartHttpRequest：
    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
    		// 获得文件：
    		MultipartFile file = multipartRequest.getFile(fileName);
    		if (file!=null) {
				String uploadPath = uploadFile(multipartRequest, file, uploadFolder, typeDir);
				log.debug("上传文件成功   文件保存路径 : "+uploadPath);
    			return uploadPath;
    		}
        }
		return null;
    }
	
	/**
	 * @Title: getFiles2UploadPath
	 * @Description: 根据fileName获取多个上传文件并上传
	 * @param request
	 * @param fileType
	 * 		类型目录
	 * @param inputName
	 * 		上传文件input标签的name值
	 * @return 
	 */
	public static List<String> getFiles2UploadPath(HttpServletRequest request, String uploadFolder, String fileType, String inputName){
		try {
			// 创建一个通用的多部分解析器.  
	        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
	        // 设置编码  
	        commonsMultipartResolver.setDefaultEncoding("utf-8");  
	        // 判断是否有文件上传  
	        if (commonsMultipartResolver.isMultipart(request)) {//有文件上传  
	        	// 转型为MultipartHttpRequest：
	    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    		/*Iterator<String> it = multipartRequest.getFileNames();
	    		while (it.hasNext()) {
					String string = (String) it.next();
					System.out.println(string);
				}*/
	    		// 获得文件：
	    		List<MultipartFile> fileList = multipartRequest.getFiles(inputName);
	    		List<String> filePathList = new ArrayList<String>();
	    		for(MultipartFile file : fileList){
	    			if (file!=null && StringUtils.isNotBlank(file.getOriginalFilename())) {
	    				String uploadPath = uploadFile(multipartRequest, file, uploadFolder, fileType);
	    				if(StringUtils.isNotBlank(uploadPath)){
	    					filePathList.add(uploadPath);
	    					log.debug("上传文件成功   文件保存路径 : "+uploadPath);
	    				}
	        		}
	    		}
	    		if(!filePathList.isEmpty() && filePathList.size()>0){
	    			return filePathList;
	    		}
	        }
		} catch (IOException e) {
			e.printStackTrace();
			log.info("上传文件IO异常，返回null");
		} catch (NullPointerException e) {
			e.printStackTrace();
			log.info("上传文件为空，返回null");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("上传文件异常，返回null");
		}
		return null;
    }
	
	//==================================上传压缩文件===================================================================================================
	
	public static List<String> getZipFilesUploadPath(HttpServletRequest request, String uploadFolder, String fileType, String inputName){
		try {
			// 创建一个通用的多部分解析器.  
	        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());  
	        // 设置编码  
	        commonsMultipartResolver.setDefaultEncoding("utf-8");  
	        // 判断是否有文件上传  
	        if (commonsMultipartResolver.isMultipart(request)) {//有文件上传  
	        	// 转型为MultipartHttpRequest：
	    		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
	    		/*Iterator<String> it = multipartRequest.getFileNames();
	    		while (it.hasNext()) {
					String string = (String) it.next();
					System.out.println(string);
				}*/
	    		// 获得文件：
	    		List<MultipartFile> fileList = multipartRequest.getFiles(inputName);
	    		List<String> filePathList = new ArrayList<String>();
	    		for(MultipartFile file : fileList){
	    			if (file!=null && StringUtils.isNotBlank(file.getOriginalFilename())) {
	    				String uploadPath = uploadFile(multipartRequest, file, uploadFolder, fileType);
	    				if(StringUtils.isNotBlank(uploadPath)){
	    					filePathList.add(uploadPath);
	    					log.debug("上传文件成功   文件保存路径 : "+uploadPath);
	    				}
	        		}
	    		}
	    		if(!filePathList.isEmpty() && filePathList.size()>0){
	    			return filePathList;
	    		}
	        }
		} catch (IOException e) {
			e.printStackTrace();
			log.info("上传文件IO异常，返回null");
		} catch (NullPointerException e) {
			e.printStackTrace();
			log.info("上传文件为空，返回null");
		} catch (Exception e) {
			e.printStackTrace();
			log.info("上传文件异常，返回null");
		}
		return null;
    }
	
}
