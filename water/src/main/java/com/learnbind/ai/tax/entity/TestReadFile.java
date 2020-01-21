package com.learnbind.ai.tax.entity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;

public class TestReadFile {
	/**
	 * 	判断文件的编码格式
	 * @param fileName :file
	 * @return 文件编码格式
	 * @throws Exception
	 */
	public static String codeString(File fileName) throws Exception{
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		
		switch (p) {
			case 0xefbb:
				code = "UTF-8";
				break;
			case 0xfffe:
				code = "Unicode";
				break;
			case 0xfeff:
				code = "UTF-16BE";
				break;
			default:
				code = "GBK";
		}
		IOUtils.closeQuietly(bin);
		return code;
	}
	
	public static void main(String[] args) {
		String filePath = "D:\\2019121609483761043050.txt";
		
		String encoding = "UTF-8";
		try {
			InputStream inputStream = new FileInputStream(filePath);
			byte[] head = new byte[3];
			
				inputStream.read(head);
			String code = "GBK"; // 或GBK
			if (head[0] == -1 && head[1] == -2)
				code = "UTF-16";
			else if (head[0] == -2 && head[1] == -1)
				code = "Unicode";
			else if (head[0] == -17 && head[1] == -69 && head[2] == -65)
				code = "UTF-8";
			inputStream.close();
			encoding = code;
			System.out.println("原来的文件编码是:"+encoding);
			
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		File filexx=new File(filePath);
		try {
			String encoding1=codeString(filexx);
			System.out.println("新程序测试的文件编码是:"+encoding1);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
        File file = new File(filePath);
		if (!file.exists()) {
			// throw new RuntimeException("要读取的文件不存在");
			System.out.println("要读取的文件不存在");
		}
        Long filelength = file.length();  
        //byte[] filecontent = new byte[filelength.intValue()];
        char[] filecontent = new char[filelength.intValue()];
        StringBuffer sb = new StringBuffer();
        try {  
            FileInputStream in = new FileInputStream(file);  
            //in.read(filecontent);
        	//in.close();
        	InputStreamReader isr=new InputStreamReader(in,encoding);
        	int len=isr.read(filecontent);
        	sb.append(new String(filecontent,0,len));
            isr.close();
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        try {  
            //String result = new String(filecontent, encoding);
        	String result = sb.toString();
            System.out.println("=================================================================================");
            System.out.println(result);
            System.out.println("=================================================================================");
        } catch (Exception e) {  
            System.err.println("The OS does not support " + encoding);  
            e.printStackTrace();  
        } 
	}
	
}
