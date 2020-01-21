package com.learnbind.ai.util.pdf;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.DocumentException;

public class Starter {
	
	
	public static void main(String[] args) throws DocumentException, IOException {
			PDFGenerator gen = new PDFGenerator("templates/bottlelabel/bottlelabelhtml/", ".html");
			String classPathUrl = Starter.class.getResource("").toString();
			String Url =classPathUrl+"0.jpg";
			System.out.println(Url);
			System.out.println("file:/C:/Users/Administrator.WIN7-1610080938/Desktop/word2pdf/360_log.png");
			Map<String, Object> model = new HashMap<>();
			
			//create test data
			List<Map<String,Object>> dataList =new ArrayList<>();   // object list
			for(int i=0;i<10;i++) {
				Map<String,Object> onebus=new HashMap<>();  //   an object
				onebus.put("field1", "xxxx"+Integer.toString(i));
				onebus.put("field2", "yyyy"+Integer.toString(i));
				
				dataList.add(onebus);
				
			}
			
			model.put("testData", dataList);
			
			//model.put("message", "Hallo Weltfsa");
			model.put("meterNo", "123456789");
			model.put("teelSealNo", "123456789");
			model.put("place", "长久花园");
			model.put("factory", "123456789");
			model.put("readMode", "123456789");
			model.put("meterAddress", "123456789");
			//model.put("imgUrl",Url);
			//gen.generate(new File("d:/temp/word2pdf/test.pdf"), "contract", model);
			gen.generate(new File("d:/test.pdf"), "bottlelabelhtml1", model);
			System.out.println(model);
			//gen.generate(new File("d:/test.pdf"), "test", model);
		}
}
