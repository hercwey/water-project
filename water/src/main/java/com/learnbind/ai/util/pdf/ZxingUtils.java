package com.learnbind.ai.util.pdf;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     ZxingUtils.java
*	@Description   用于生成条码(1维,2维) 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月20日 下午3:11:24 
*/
public class ZxingUtils {
	/**
	 * 生成二维码
	 *
	 * @param contents  二维码所表示的内容
	 * @param width		二维码的宽度
	 * @param height	二维码的高度
	 * @param imgPath	文件路径
	 */
	public void encodeQRCode(String contents, int width, int height, String imgPath) {
		
		Map<EncodeHintType, Object> hints = new Hashtable<>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		// 指定编码格式
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		
		try {
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.QR_CODE, width, height, hints);

			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 解析二维码
	 *
	 * @param imgPath	二维码文件路径
	 * @return			二维码所表示的内容
	 */
	public String decodeQRCode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

			Map<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

			result = new MultiFormatReader().decode(bitmap, hints);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成条形码
	 *
	 * @param contents  条形码所表示的内容
	 * @param width		条形码的宽度
	 * @param height	条形码的高度
	 * @param imgPath	条形码文件路径
	 */
	// int width = 105, height = 50; 长度很容易报错:NotFoundException
	public void encodeBarCode(String contents, int width, int height, String imgPath) {
		int codeWidth = 3 + 	// start guard
				(7 * 6) + 		// left bars
				5 + 			// middle guard
				(7 * 6) + 		// right bars
				3; 				// end guard
		
		codeWidth = Math.max(codeWidth, width);
		try {
			
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0);
			
			//BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.EAN_13, codeWidth, height,	hints);
			BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.CODE_128, codeWidth, height,	hints);
			MatrixToImageWriter.writeToStream(bitMatrix, "png", new FileOutputStream(imgPath));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description		解析条形码
	 * 
	 * @param imgPath	条形码文件路径
	 * 
	 * @return			解析条码所表示的内容
	 */
	public String decodeBarCode(String imgPath) {
		BufferedImage image = null;
		Result result = null;
		try {
			image = ImageIO.read(new File(imgPath));
			if (image == null) {
				System.out.println("the decode image may be not exit.");
			}
			LuminanceSource source = new BufferedImageLuminanceSource(image);
			BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
			/*            Map<DecodeHintType, Object> hints = new Hashtable<>();
			hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
			hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
			result = new MultiFormatReader().decode(bitmap, hints);*/
			result = new MultiFormatReader().decode(bitmap, null);
			return result.getText();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/** 
	 *	@Title testQRcode 
	 *	@Description 代码测试
	 *	@Return void    返回类型 
	 *	@Date 2018年12月20日 下午3:54:44
	*/
	private static void testQRcode() {
		String imgPathQRCode = "D:\\testQR.png";
		String impPathBarcode="D:\\testBarcode.png";
		// 益达无糖口香糖的条形码
		String contents = "6923450657713";
		int width = 105, height = 50;
		
		ZxingUtils handler = new ZxingUtils();
		
		//生成条码(一维)并进行解码
		handler.encodeBarCode(contents, width, height, impPathBarcode);		
		String barcode = handler.decodeBarCode(impPathBarcode);
		System.out.println(barcode);
		
		//生成条码(二维)并进行解码
		handler.encodeQRCode("abc123中文@#\\", 200, 200, imgPathQRCode);
		String qrcode = handler.decodeQRCode(imgPathQRCode);
		System.out.println(qrcode);
	}
	
	public static void main(String[] args) throws Exception {
		testQRcode();
	}
}
