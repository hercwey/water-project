package com.learnbind.ai.util.pdf;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     PrintFile.java
*	@Description   将指定的文件发送到打印机打印. 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月21日 上午11:08:24 
*/
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.MediaSizeName;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;
import org.apache.pdfbox.printing.PDFPrintable;
import org.apache.pdfbox.printing.Scaling;
import org.springframework.stereotype.Component;

@Component
public class PrintFile {

	/** 
	 *	@Title print 
	 *	@Description 将文件发送到打印机
	 * 
	 *	@param fileName		文件名称
	 *	@param docFlavor	文件格式
	 *	@param copies		打印份数
	 *	@param printerName	打印机名称
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月21日 上午11:28:10
	*/
	private void print(String fileName,DocFlavor docFlavor,int copies,String printerName) {
		//(1)打开需要打印的文件
		FileInputStream psStream = null;
		try {
			psStream = new FileInputStream(fileName);
		} catch (FileNotFoundException ffne) {
			ffne.printStackTrace();
		}
		if (psStream == null) {
			return;
		}
		
		//(2)设置打印数据的格式，此处为图片gif格式  
		//DocFlavor psInFormat = DocFlavor.INPUT_STREAM.GIF;
		DocFlavor psInFormat = DocFlavor.INPUT_STREAM.PDF;
		//DocFlavor psInFormat = docFlavor;
		
		//(3)创建打印数据  
		DocAttributeSet docAttr = new HashDocAttributeSet();//设置文档属性  
		Doc myDoc = new SimpleDoc(psStream, psInFormat, docAttr);
		//Doc myDoc = new SimpleDoc(psStream, psInFormat, null);


		//(4)设置打印属性  
		PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
		aset.add(new Copies(copies));//打印份数 已经参数化.
		
		
		//当前默认打印机
		/*PrintService myPrinter = PrintServiceLookup.lookupDefaultPrintService();
		myPrinter.getName();*/
		

		//查找所有打印服务  
		PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

		// this step is necessary because I have several printers configured  
		//将所有查找出来的打印机与自己想要的打印机进行匹配，找出自己想要的打印机  
		PrintService myPrinter = null;
		for (int i = 0; i < services.length; i++) {
			System.out.println("service found: " + services[i]);
			String svcName = services[i].toString();
			if (svcName.contains(printerName)) {
				myPrinter = services[i];
				System.out.println("my printer found: " + svcName);
				System.out.println("my printer found: " + myPrinter);
				break;
			}
		}

		//可以输出打印机的各项属性  
		AttributeSet att = myPrinter.getAttributes();

		for (Attribute a : att.toArray()) {

			String attributeName;
			String attributeValue;

			attributeName = a.getName();
			attributeValue = att.get(a.getClass()).toString();

			System.out.println(attributeName + " : " + attributeValue);
		}

		if (myPrinter != null) {
			DocPrintJob job = myPrinter.createPrintJob();//创建文档打印作业  
			try {
				job.print(myDoc, aset);//打印文档  

			} catch (Exception pe) {
				pe.printStackTrace();
			}
		} else {
			System.out.println("no printer services found");
		}
	}  //end of function
	
	
	public void printPDF(String fileName,String printerName) throws IOException, PrinterException {
		// 使用打印机的名称
		String printName = "";
		//String pdfPath = "D:\test.pdf";
		String pdfPath=fileName;
        File file = new File(pdfPath);
		// 读取pdf文件
        PDDocument document = PDDocument.load(file);
		// 创建打印任务
        PrinterJob job = PrinterJob.getPrinterJob();
        // 遍历所有打印机的名称
        for (PrintService ps : PrinterJob.lookupPrintServices()) {
            String psName = ps.toString();
			// 选用指定打印机
            //if (psName.equals(printerName)) {
            if (StringUtils.equals(psName, printName)) {
                Boolean isChoose = true;
                job.setPrintService(ps);
                break;
            }
        }
 
        job.setPageable(new PDFPageable(document));
 
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(MediaSizeName.ISO_A4); // A5横向打印
		job.print(pras);


	}
	
	public void printCustomerPDF(String fileName,String printerName) throws IOException, PrinterException {
		// 使用打印机的名称
		String printName = "";
		// String pdfPath = "D:\test.pdf";
		String pdfPath = fileName;
		File file = new File(pdfPath);
		// 读取pdf文件
		PDDocument document = PDDocument.load(file);
		// 创建打印任务
		PrinterJob job = PrinterJob.getPrinterJob();
		// 遍历所有打印机的名称
		for (PrintService ps : PrinterJob.lookupPrintServices()) {
			String psName = ps.toString();
			// 选用指定打印机
			// if (psName.equals(printerName)) {
			if (StringUtils.equals(psName, printName)) {
				Boolean isChoose = true;
				job.setPrintService(ps);
				break;
			}
		}

		job.setPageable(new PDFPageable(document));
		
		//DocFlavor flavor = DocFlavor.INPUT_STREAM.JPEG;
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		//DocAttributeSet das = new HashDocAttributeSet();// 打印属性设置
		pras.add(MediaSizeName.ISO_A5); // A5横向打印
		//das.add(OrientationRequested.LANDSCAPE);// （文件的）横向打印格式

		// URL url = new
		// URL("http://www.apress.com/ApressCorporate/supplement/1/421/bcm.gif ");
		// Doc doc = new SimpleDoc(url,flavor,das);
		job.print(pras);

//        Paper paper = new Paper();
//		// 设置打印纸张大小
//        paper.setSize(525,370); // 1/72 inch ,A5 525, 370 
//        
//		// 设置打印位置 坐标
//        paper.setImageableArea(0, 15, paper.getWidth(), paper.getHeight()); // no margins
//        // custom page format
//        PageFormat pageFormat = new PageFormat();
//        pageFormat.setPaper(paper);
//        // override the page format
//        Book book = new Book();
//        // append all pages 设置一些属性 是否缩放 打印张数等
//        book.append(new PDFPrintable(document, Scaling.ACTUAL_SIZE), pageFormat, 1);
//        job.setPageable(book);
//        
//		// 开始打印
//        job.print();

	}
	
	public void sendPDFA4ToPrinter(String fileName,String printerName) throws IOException, PrinterException {
		// 使用打印机的名称
		String printName = "";
		String pdfPath = fileName;
		File file = new File(pdfPath);
		// 读取pdf文件
		PDDocument document = PDDocument.load(file);
		// 创建打印任务
		PrinterJob job = PrinterJob.getPrinterJob();
		// 遍历所有打印机的名称
		for (PrintService ps : PrinterJob.lookupPrintServices()) {
			String psName = ps.toString();
			// 选用指定打印机
			if (StringUtils.equals(psName, printName)) {
				Boolean isChoose = true;
				job.setPrintService(ps);
				break;
			}
		}

		job.setPageable(new PDFPageable(document));
		
		PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
		pras.add(MediaSizeName.ISO_A4); // A4横向打印
		job.print(pras);


	}
	
	
	/** 
	 *	@Title testPrintFile 
	 *	@Description 打印图片文件测试
	 *	@Return void    返回类型 
	 *	@Date 2018年12月21日 下午3:47:54
	*/
	private static void testPrintFile() {
		PrintFile printFile=new PrintFile();
		String fileName="src/main/resources/templates/bottlelabel/bottlelabelpdf/bottlelabelpdf1.pdf";
		//String fileName="src/main/resources/templates/bottlelabel/barcode/barcode1.png";
		//String fileName="d://testBarcode.png";
		DocFlavor fileFormat=DocFlavor.INPUT_STREAM.PNG;
		int copies=1;
		String printerName="Canon";
		
		printFile.print(fileName, fileFormat, copies, printerName);
		
	}
	
	/** 
	 *	@Title testPrintPDF 
	 *	@Description 打印PDF测试
	 *	@Return void    返回类型 
	 *	@Date 2018年12月21日 下午3:48:17
	*/
	private static void testPrintPDF() {
		PrintFile printFile=new PrintFile();
		//String fileName="src/main/resources/templates/bottlelabel/bottlelabelpdf/bottlelabelpdf1.pdf";
		String fileName="d:/temp/word2pdf/sign.pdf";
		String printerName="Canon";
		
		try {
			printFile.printPDF(fileName, printerName);
		} catch (IOException | PrinterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		//testPrintFile();
		testPrintPDF();
	}
	
		
}
