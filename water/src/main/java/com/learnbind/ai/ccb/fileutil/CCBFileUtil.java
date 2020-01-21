
package com.learnbind.ai.ccb.fileutil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.learnbind.ai.ccb.business.CCBBusiness;

/**
 * 文件操作工具类
 * 
 * @author lenovo
 *
 */
public class CCBFileUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(CCBFileUtil.class);

	/**
	 * 可用于项目中
	 * 
	 * @param fileName 文件名称
	 * @param data     
	 */
	/**
	 * 保存数据到文件
	 * @param filePath 
	 * @param fileName
	 * @param data
	 */
	public static void saveDataToFile(String filePath,String fileName, String data) {
		BufferedWriter writer = null;
		
		//如果不存在此目录则创建
		String destDirName = filePath;
		File dir = new File(destDirName);
		if (dir.exists()) {
			//System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			//System.out.println("创建目录" + destDirName + "成功！");
			logger.info("创建目录" + destDirName + "成功！");
		} else {
			//System.out.println("创建目录" + destDirName + "失败！");
			logger.info("创建目录" + destDirName + "失败！");
		}
		
		
		File file = new File(destDirName + fileName);
		//System.out.println(file + "file");
		// 如果文件不存在，则新建一个
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName + "文件不存在");
		}
		// 写入
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), "UTF8"));
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("文件写入成功！");
		logger.info("文件写入成功！");
	}
	
	/**
	 * @Title: saveDataToFile
	 * @Description: 保存数据到文件
	 * @param filePath
	 * @param fileName
	 * @param data	UTF-8
	 * @param charSet GBK
	 * 		写入GBK文件时，字符串编码为UTF-8，只在写入文件时设置编码为GBK
	 */
	public static void saveDataToFile(String filePath,String fileName, String data, String charSet) {
		BufferedWriter writer = null;
		
		//如果不存在此目录则创建
		String destDirName = filePath;
		File dir = new File(destDirName);
		if (dir.exists()) {
			//System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
		}
		if (!destDirName.endsWith(File.separator)) {
			destDirName = destDirName + File.separator;
		}
		// 创建目录
		if (dir.mkdirs()) {
			//System.out.println("创建目录" + destDirName + "成功！");
			logger.info("创建目录" + destDirName + "成功！");
		} else {
			//System.out.println("创建目录" + destDirName + "失败！");
			logger.info("创建目录" + destDirName + "失败！");
		}
		
		
		File file = new File(destDirName + fileName);
		//System.out.println(file + "file");
		// 如果文件不存在，则新建一个
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(fileName + "文件不存在");
		}
		// 写入
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), charSet));
			writer.write(data);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//System.out.println("文件写入成功！");
		logger.info("文件写入成功！");
	}

	
	/**
	 * 可用于项目中.
	 * 自文件中读取数据
	 * @param filePath  路径
	 * @param fileName  文件名称
	 * @return  文件内容
	 */
	public static String readDataFromFile(String filePath,String fileName) {

		// 为了确保文件一定在之前是存在的，将字符串路径封装成File对象
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}
		File file = new File(filePath+fileName);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			logger.info("要读取的文件不存在");
			return null;
		}
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("文件读取成功");
		return laststr;
	}
	
	/**
	 * 可用于项目中.
	 * 自文件中读取数据
	 * @param filePath  路径
	 * @param fileName  文件名称
	 * @return  文件内容
	 */
	public static String readDataFromFile(String filePath) {

		File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			logger.info("要读取的文件不存在");
			return null;
		}
		BufferedReader reader = null;
		String laststr = "";
		try {
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8");
			reader = new BufferedReader(inputStreamReader);
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				laststr += tempString;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		//System.out.println("文件读取成功");
		return laststr;
	}
	
	/**
	 * 自文本文件中读取数据到字符串
	 * @param filePath  文件路径
	 * @param fileName  文件名称
	 * @return			UTF-8编码的字符串
	 */
	public static byte[] readToByte(String filePath,String fileName) {
		
		if (!filePath.endsWith(File.separator)) {
			filePath = filePath + File.separator;
		}		
		
        //String encoding = "UTF-8";  
        File file = new File(filePath+fileName);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			logger.info("要读取的文件不存在");
			return null;
		}
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return filecontent;
//        try {  
//            //return new String(filecontent, encoding);
//        	return filecontent;
//        } catch (UnsupportedEncodingException e) {  
//            //System.err.println("The OS does not support " + encoding);  
//            e.printStackTrace();  
//            return null;  
//        }  
    }

	/**
	 * 自文本文件中读取数据到字符串
	 * @param filePath  文件路径
	 * @param fileName  文件名称
	 * @return			UTF-8编码的字符串
	 */
	public static String readToString(String filePath, String encoding) {
		
        //String encoding = "UTF-8";  
        File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			logger.info("要读取的文件不存在");
			return null;
		}
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            return new String(filecontent, encoding);  
        } catch (UnsupportedEncodingException e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
            return null;  
        }  
    }
	
	/**
	 * 自文本文件中读取数据到字符串
	 * @param filePath  文件路径
	 * @param fileName  文件名称
	 * @return			UTF-8编码的字符串
	 */
	public static byte[] readToByte(String filePath) {
		
        String encoding = "GBK";  
        File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			logger.info("要读取的文件不存在");
			return null;
		}
        Long filelength = file.length();  
        byte[] filecontent = new byte[filelength.intValue()];  
        try {  
            FileInputStream in = new FileInputStream(file);  
            in.read(filecontent);  
            in.close();  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return filecontent;
//        try {  
//            //return new String(filecontent, encoding);
//        	return filecontent;
//        } catch (UnsupportedEncodingException e) {  
//            System.err.println("The OS does not support " + encoding);  
//            e.printStackTrace();  
//            return null;  
//        }  
    }
	
	/**
	 * 获取当前系统的换行符
	 */
	public static String lineSeparator() {
		// 注意在将流写入文件时，换行应根据操作系统的不同来决定。
		// 在程序我们应尽量使用System.getProperty("line.separator")来获取当前系统的换
		// 行符，而不是写/r/n或/n。
		// 这样写程序不够灵活
		// 当我们在java控制台输出的时候，/r和/n都能达到换行的效果。
//		String lineSeparatorx = "abcde";
//		if (System.getProperty("line.separator").equals("\r\n")) {
//			//System.out.println("\\r\\n is for windows");
//			lineSeparatorx = "\r\n";
//		} else if (System.getProperty("line.separator").equals("/r")) {
//			System.out.println("//r is for Mac");
//			lineSeparatorx = "/r";
//		} else if (System.getProperty("line.separator").equals("\n")) {
//			System.out.println("//n is for Unix/Linux");
//			lineSeparatorx = "\n";
//		}
//		return lineSeparatorx;
		return System.getProperty("line.separator");
	}

	public static void main(String[] args) {
		CCBFileUtil fileUtil = new CCBFileUtil();
		String data = "1212121212" + lineSeparator() + "12345" +lineSeparator();
		fileUtil.saveDataToFile("f:/test","test.txt", data);
		
		//String readData=fileUtil.readDataFromFile("f:/test", "test.txt");
		//String readData=fileUtil.readToString("f:/test", "test.txt");
		//logger.info(readData);
		//System.out.println(readData);
	}

}
