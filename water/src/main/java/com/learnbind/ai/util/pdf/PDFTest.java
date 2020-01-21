package com.learnbind.ai.util.pdf;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.ElementList;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.learnbind.ai.common.DefineStringUtil;
import com.learnbind.ai.common.Propertiesconfiguration;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     PDFTest.java
*	@Description   TODO(用一句话描述该文件做什么) 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          HTML2PDF测试 
*/
public class PDFTest {
	
	/** 
	    *	@Fields BARCODE_TYPE_1D  条形码 
	    */
	    private static final String BARCODE_TYPE_1D = "1";

	    /** 
	    *	@Fields BARCODE_TYPE_2D  二维码 
	    */
	    private static final String BARCODE_TYPE_2D = "2";
	    
	    /** 
	    *	@Fields barcodeWidth  条形码的宽度 
	    */
	    private static int barcodeWidth = 110;
	    /** 
	    *	@Fields barcodeHeight  条形码的高度 
	    */
	    private static int barcodeHeight = 35;
	
	
	
	/** 
	 *	@Title createPDF 
	 *	@Description 1:生成一个PDF
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月17日 上午11:43:34
	*/
	public void createPDF()  {
		final String FILE_DIR="d://temp//testpdf//";
		try {
			//Step 1—Create a Document.  
			Document document = new Document();  
			//Step 2—Get a PdfWriter instance.  
			PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR + "createSamplePDF.pdf"));  
			//Step 3—Open the Document.  
			document.open();  
			//Step 4—Add content.  
			document.add(new Paragraph("Hello World"));  
			//Step 5—Close the Document.  
			document.close();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 *	@Title html2pdf 
	 *	@Description 37 HTML to PDF 根据HTML生成PDF 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月17日 上午11:44:43
	*/
	public void  html2pdf() {
		try {
			final String FILE_DIR="d://temp//testpdf//";
			
			Document document = new Document(PageSize.LETTER);  
			PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR+"testpdf1.pdf"));  
			document.open();  
			
			HTMLWorker htmlWorker = new HTMLWorker(document);  
			htmlWorker.parse(new StringReader("<h1>This is a test!</h1>"));  
			
			document.close();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	
	/** 
	 *	@Title testHtml2pdf 
	 *	@Description 可运行,但未显示中文.
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午6:26:13
	*/
	public void  testHtml2pdf() {
		try {
			final String FILE_DIR="d://temp//testpdf//";
			String htmlFile = FILE_DIR+"test.html";
			
			Document document = new Document(PageSize.LETTER);
			PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR+"testpdf2.pdf"));
			
			document.open();
			  
			
			HTMLWorker htmlWorker = new HTMLWorker(document);
			
			// PdfUtils.parseHTML2PDFFile(pdfFile, new FileInputStream(htmlFile));
			String ss = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				// System.out.println(t);
				ss += t;
			}
			
			
			htmlWorker.parse(new StringReader(ss));  
			
			document.close();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
	}
	
	
	/** 
	 *	@Title createBarCode 
	 *	@Description 生成条形码与二维码
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 上午11:27:25
	*/
	public void createBarCode() {
		try {
			String myString = "http://www.google.com";  
			final String FILE_DIR="d://temp//testpdf//";
			
			Document document = new Document(PageSize.LETTER);			
			PdfWriter writer=PdfWriter.getInstance(document, new FileOutputStream(FILE_DIR+"barcode_pdf.pdf"));
			document.open();
			
			PdfContentByte cb = writer.getDirectContent();
			
			
			
			//条形码
			Barcode128 code128 = new Barcode128();  
			code128.setCode(myString.trim());  
			code128.setCodeType(Barcode128.CODE128);  
			Image code128Image = code128.createImageWithBarcode(cb, null, null);  
			code128Image.setAbsolutePosition(10,700);  
			code128Image.scalePercent(125);  
			document.add(code128Image);  
			  
			//二维码
			BarcodeQRCode qrcode = new BarcodeQRCode(myString.trim(), 1, 1, null);  
			Image qrcodeImage = qrcode.getImage();  
			qrcodeImage.setAbsolutePosition(10,600);  
			qrcodeImage.scalePercent(200);  
			document.add(qrcodeImage); 
			
			document.close();
			
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	/** 
	 *	@Title pageSize 
	 *	@Description 页面大小,页面背景色,页边空白,Title,Author,Subject,Keywords 
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 上午11:27:01
	*/
	public void pageSize() {		
		final String FILE_DIR="d://testpdf//";	
		
		//页面大小  
		Rectangle rect = new Rectangle(PageSize.B5.rotate());
		//Rectangle rect=new Rectangle(PageSize.)
		
		//页面背景色  
		rect.setBackgroundColor(BaseColor.ORANGE);  
		  
		Document doc = new Document(rect);  
		try {
			PdfWriter writer;
			writer = PdfWriter.getInstance(doc, new FileOutputStream(FILE_DIR+"pageSize.pdf"));
			
			//PDF版本(默认1.4)  
			writer.setPdfVersion(PdfWriter.PDF_VERSION_1_2);
			
			  
			//文档属性  
			doc.addTitle("Title@sample");  
			doc.addAuthor("Author@rensanning");  
			doc.addSubject("Subject@iText sample");  
			doc.addKeywords("Keywords@iText");  
			doc.addCreator("Creator@iText");  
			  
			//页边空白  
			doc.setMargins(10, 20, 30, 40);  
			  
			doc.open();  
			doc.add(new Paragraph("Hello World")); 
			
			doc.close();
		} catch (FileNotFoundException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** 
	 *	@Title setPassword 
	 *	@Description 设置文档密码  测试未通过
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 上午11:42:51
	*/
	public void setPassword1() {
		final String USER_PASS = "Hello123";
		final String OWNER_PASS = "Owner123";
		final String FILE_DIR = "d://temp//testpdf//";

		try {

			FileOutputStream file = new FileOutputStream(new File(FILE_DIR + "setPassword.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);

			writer.setEncryption(USER_PASS.getBytes(), OWNER_PASS.getBytes(), PdfWriter.ALLOW_PRINTING,
					PdfWriter.ENCRYPTION_AES_128);

			document.open();
			document.add(new Paragraph("Hello World, iText"));
			document.add(new Paragraph(new Date().toString()));

			document.close();
			file.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

	}
	
	/** 
	 *	@Title addPage 
	 *	@Description 增加页(可以显示中文)
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午1:52:01
	*/
	public void addPage() {
		final String FILE_DIR = "d://testpdf//";

		try {
			FileOutputStream file = new FileOutputStream(new File(FILE_DIR + "newPage.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			
			//中文字体
			BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			Font font = new Font(bfChinese, 12,Font.NORMAL);
				
			document.open();  
			  
			document.newPage();  
			writer.setPageEmpty(false);
			document.add(new Paragraph("New page"));
			document.add(new Paragraph("第一页",font));
			  
			document.newPage();  
			document.add(new Paragraph("New page"));
			document.add(new Paragraph("第二页",font));
			
			document.newPage();  
			document.add(new Paragraph("new page"));
			document.add(new Paragraph("第三页",font));
			
			document.close();
			
		} catch (IOException | DocumentException  e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/** 
	 *	@Title addWatermark 
	 *	@Description 添加水印
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午3:31:54
	*/
	public void setWatermark() {
		final String FILE_DIR = "d://temp//testpdf//";
		
		  
		try {
			PdfReader reader = new PdfReader(FILE_DIR + "setWatermark.pdf");  
			PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(FILE_DIR + "setWatermark2.pdf")); 
			
			//中文字体
			BaseFont bfChinese = BaseFont.createFont( "STSongStd-Light" ,"UniGB-UCS2-H",BaseFont.NOT_EMBEDDED);
			Font font = new Font(bfChinese, 12,Font.NORMAL);
			
			//PdfPCell cell = new PdfPCell(new Paragraph("测试",font.BOLDITALIC));

			//图片水印
			Image img = Image.getInstance("src/main/resources/static/images/engine_diagram.jpg");  
			img.setAbsolutePosition(200, 400);  
			PdfContentByte under = stamp.getUnderContent(1);  
			under.addImage(img);  
			  
			//文字水印   是否可以显示中文水印?
			PdfContentByte over = stamp.getOverContent(2);  
			over.beginText();  
			BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.WINANSI, BaseFont.EMBEDDED);  
			over.setFontAndSize(bf, 18);  
			over.setTextMatrix(30, 30);  
			over.showTextAligned(Element.ALIGN_LEFT, "NO COPY", 230, 430, 45);  
			over.endText();  
			  
			//背景图  
			Image img2 = Image.getInstance("src/main/resources/static/images/radar_diagram.jpg");  
			img2.setAbsolutePosition(0, 0);  
			PdfContentByte under2 = stamp.getUnderContent(3);  
			under2.addImage(img2);  
			  
			stamp.close();  
			reader.close();
			
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	}
	
	/** 
	 *	@Title parseHTML2PDFFile2 
	 *	@Description html2pdf2
	 * 
	 *	@param pdfFile
	 *	@param html
	 *	@throws DocumentException
	 *	@throws IOException
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午4:32:50
	*/
	public void parseHTML2PDFFile2(String pdfFile, String html) throws DocumentException, IOException {
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pdfFile));
		document.open();
		XMLWorkerHelper.getInstance().parseXHtml(writer, document, 
												new ByteArrayInputStream(html.getBytes("Utf-8")),
												Charset.forName("UTF-8"));
		document.close();
	}

	//测试后发现不可以显示中文
	public void testHTML2PDFFile2()  {
		final String FILE_DIR = "d://temp//testpdf//";
		
		String htmlFile = FILE_DIR+"test.html";
		 
		String pdfFile = FILE_DIR+"html2PDF2.pdf";
		// PdfUtils.parseHTML2PDFFile(pdfFile, new FileInputStream(htmlFile));
		String ss = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				// System.out.println(t);
				ss += t;
			}
			parseHTML2PDFFile2(pdfFile, ss);
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/** 
	 *	@Title html2pdf3 
	 *	@Description 可以正确有显示中文
	 * 
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午6:15:13
	*/
	public void html2pdf3() {
		final String FILE_DIR = "d://temp//testpdf//";

		try {
			FileOutputStream file = new FileOutputStream(new File(FILE_DIR + "html2pdf3.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			
			document.open();
			
			String html = "<div style='color:green;font-size:20px;'>你好世界！hello world !</div>";
			Paragraph context = new Paragraph();
			ElementList elementList =MyXMLWorkerHelper.parseToElementList(html, null);
			for (Element element : elementList) {
			    context.add(element);
			}
			document.add(context);
			
			document.close();
			
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/** 
	 *	@Title html2pdf4 
	 *	@Description 读取html文件并生成pdf  test
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月18日 下午6:17:37
	*/
	public void html2pdf4() {
		final String FILE_DIR = "d://temp//testpdf//";

		try {
			FileOutputStream file = new FileOutputStream(new File(FILE_DIR + "html2pdf4.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			
			document.open();
			
			//String html = "<div style='color:green;font-size:20px;'>你好世界！hello world !</div>";
			
			String htmlFile = FILE_DIR+"labelTemplate.html";
			String htmlStr = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				// System.out.println(t);
				htmlStr += t;
			}
			
			
			Paragraph context = new Paragraph();
			ElementList elementList =MyXMLWorkerHelper.parseToElementList(htmlStr, null);
			for (Element element : elementList) {
			    context.add(element);
			}
			document.add(context);
			
			document.close();
			
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	
	
	
    /** 
     *	@Title createBarcode 
     *	@Description 生成条形码或是二维码.
     * 
     *	@param content	   条形码/二维码所表示的内容
     *	@param savePath	   条形码/二维码所对应的文件路径.
     *	@param bottleNum  瓶签编号,用于生成文件条形码(二维码)文件名称
     *	@return
     *	@throws Exception
     *     
     *	@Return String    返回类型 
     *
     *	@Date 2018年12月19日 下午4:17:41
    */
    private String createBarcode1(String content, String savePath, String bottleNum)  throws Exception {
    	//瓶签类型  #瓶签打印是1维码还是2维码(1,2)
    	//print.label.code.type=2
    	//在application.properties中定义.
       // String codeType = Propertiesconfiguration.getStringProperty("print.label.code.type");
    	
    	String codeType="1";
        
        if (DefineStringUtil.isEmpty(codeType)) {
            codeType = "";
        }
        String format = "png";// 图像类型
        String fileName = "zxing" + codeType + "_" + bottleNum + "." + format;

        File file = new File(savePath + fileName);
        if (!file.exists()) {
            barcodeWidth = 110; // 图像宽度
            barcodeHeight = 35; // 图像高度

            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints.put(EncodeHintType.MARGIN, 0);

            //BarcodeFormat.QR_CODE 二维码
            BarcodeFormat barcode = BarcodeFormat.CODE_128;//条形码

            //二维码为60*60	
            if (BARCODE_TYPE_2D.equals(codeType)) {
                barcodeWidth = 150;
                barcodeHeight = 150;
                barcode = BarcodeFormat.QR_CODE;
            }

            // 生成矩阵
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barcode, barcodeWidth, barcodeHeight, hints);

            //重新调整条形码或是二维码的边界
            bitMatrix = updateBit(bitMatrix,2);
            // 输出图像
            MatrixToImageWriter.writeToFile(bitMatrix, format, file);
        }

        return fileName;
    }
    
    public void html2pdf5() {
		final String DEST_FILE_DIR = "d://";					//生成的PDF路径
		final String HTML_FILE_DIR = "D:\\software\\github\\git\\wsd-project\\wsd\\src\\main\\resources\\templates\\bottlelabel\\labeltemplate\\";		//模板文件所在路径

		try {
			FileOutputStream file = new FileOutputStream(new File(DEST_FILE_DIR + "html2pdf5.pdf"));
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, file);
			
			document.open();
			
			//String html = "<div style='color:green;font-size:20px;'>你好世界！hello world !</div>";
			
			String htmlFile = HTML_FILE_DIR+"labelTemplate.html";
			String htmlStr = "";
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(htmlFile), "UTF-8"));
			String t = "";
			while ((t = br.readLine()) != null) {
				// System.out.println(t);
				htmlStr += t;
			}
			
			
			Paragraph context = new Paragraph();
			ElementList elementList =MyXMLWorkerHelper.parseToElementList(htmlStr, null);
			for (Element element : elementList) {
			    context.add(element);
			}
			document.add(context);
			
			document.close();
			
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    
    /** 
     *	@Title updateBit 
     *	@Description 调整条形码或是二维码的边界
     * 
     *	@param matrix
     *	@param margin
     *	@return
     *     
     *	@Return BitMatrix    返回类型 
     *
     *	@Date 2018年12月19日 下午5:38:49
    */
    private BitMatrix updateBit(BitMatrix matrix, int margin) {
        int tempM = margin * 2;
        int[] rec = matrix.getEnclosingRectangle(); //获取二维码图案的属性
        int resWidth = rec[2] + tempM;
        int resHeight = rec[3] + tempM;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // 按照自定义边框生成新的BitMatrix
        resMatrix.clear();
        for (int i = margin; i < resWidth - margin; i++) { //循环，将二维码图案绘制到新的bitMatrix中
            for (int j = margin; j < resHeight - margin; j++) {
                if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }
	
	/** 
	 *	@Title resoleverTemplate 
	 *	@Description 采用thymeleaf模块解析器生成HTML
	 *				 HTML+DATA
	 *     
	 *	@Return void    返回类型 
	 *
	 *	@Date 2018年12月19日 下午3:13:37
	*/
	public void resoleverTemplate() {
		//构造模板引擎
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setPrefix("templates/example/");//模板所在目录，相对于当前classloader的classpath。
        resolver.setSuffix(".html");//模板文件后缀
        
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

        //构造上下文(Model)
        Context context = new Context();
        context.setVariable("name", "蔬菜列表");
        context.setVariable("array", new String[]{"土豆", "番茄", "白菜", "芹菜"});
        

        //渲染模板
        FileWriter write;
		try {
			write = new FileWriter("src/main/resources/templates/labeltemplate/result.html");
			//write = new FileWriter("d:/temp/testpdf/result.html");
			templateEngine.process("example", context, write);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
	}
	
	//生成二维码图片文件
	private void testCreateBarcode1() {
		String content="1234567890";
		String savePath="src/main/resources/static/images/";
		String bottleNum="123456789_1";
		
		try {
			createBarcode1(content, savePath, bottleNum);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	public static void main(String[] args)  {
		PDFTest pdfTest=new PDFTest();
		//pdfTest.createPDF();
		//pdfTest.html2pdf();
		//pdfTest.createBarCode();
		//pdfTest.pageSize();
		//pdfTest.setPassword1();
		pdfTest.addPage();
		//pdfTest.setWatermark();
		//pdfTest.testHTML2PDFFile2();
		//pdfTest.testHtml2pdf();
		//pdfTest.html2pdf3();
		//pdfTest.html2pdf4();
		//pdfTest.resoleverTemplate();
		
		//pdfTest.testCreateBarcode1();
		//pdfTest.html2pdf5();
	}
	
	
}
