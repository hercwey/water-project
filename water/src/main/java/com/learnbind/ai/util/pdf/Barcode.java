package com.learnbind.ai.util.pdf;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.learnbind.ai.common.DefineStringUtil;

/**
*	@Copyright (c) 2018 by Hz
*	@ClassName     Barcode.java
*	@Description   二维三生成工具类,暂时未用 
* 
*	@author        lenovo
*	@version       V1.0  
*	@Date          2018年12月20日 上午10:51:41 
*/
public class Barcode {
	/** 
    *	@Fields BARCODE_TYPE_1D  条形码 
    */
	public static final int BARCODE_TYPE_1D = 1;

	/** 
	*	@Fields BARCODE_TYPE_2D  二维码 
	*/
	public static final int BARCODE_TYPE_2D = 2;
	
	/** 
	*	@Fields BARCODE_IMAGE_FORMAT_PNG  条码图像格式 
	*/
	public static final String BARCODE_IMAGE_FORMAT_PNG="png";

	/** 
	*	@Fields barcodeWidth  条形码默认宽度 
	*/
	public static final int BARCODE_1D_DEFAULT_WIDTH = 110;
	/** 
	*	@Fields barcodeHeight  条形码默认高度 
	*/
	public static final int BARCODE_1D_DEFAULT_HEIGHT = 35;
	
	/** 
	*	@Fields BARCODE_2D_DEFAULT_WIDTH  二维码默认宽度
	*/
	public static final int BARCODE_2D_DEFAULT_WIDTH=120;
	/** 
	*	@Fields BARCODE_2D_DEFAULT_HEIGHT  二维码默认高度 
	*/
	public static final int BARCODE_2D_DEFAULT_HEIGHT=120;
	
	/** 
	 *	@Title createBarcode2 
	 *	@Description 生成条码
	 * 
	 *	@param barcodeType 	条码类型.1:1D; 2:2D
	 *	@param content		条码所表示的内容
	 *	@param imgFormat	条码图形格式     	
	 *	@param width		条码宽度
	 *	@param height		条码高度
	 *	@param margin		条码margin(边界)值,与HTML中的margin类似
	 **	@param fileName		条码所在文件
	 *
	 *	@return				1:创建成功;2:文件已经存在;3:错误;	
	 *
	 *	@Return int    返回类型
	 *
	 * @throws Exception
	 *
	 *	@Date 2018年12月20日 上午11:28:22
	*/
	private int createBarcode2(int barcodeType,String content,String imgFormat, int width,int height,int margin,String fileName) throws Exception {
		final int CREATE_SUCCESS=1;			//创建成功
		final int CREATE_FILE_EXIST=2;		//文件已经存在
		final int CREATE_ERROR=3;			//创建错误.
		
		
		int createFlag=CREATE_ERROR;  //创建标志
		
		String format = imgFormat; // 图像类型
		File file = new File(fileName);
		if (!file.exists()) {
			
			//确定条码类型
			BarcodeFormat barcode = BarcodeFormat.CODE_128;//条码-1D
			switch(barcodeType) {
			case Barcode.BARCODE_TYPE_1D:
				barcode = BarcodeFormat.CODE_128;//条码-1D
				break;
			case Barcode.BARCODE_TYPE_2D:
				barcode = BarcodeFormat.QR_CODE; //条码-2D
				break;
			default:
				barcode=BarcodeFormat.CODE_128;//条码-1D
			}
			
			
			Map<EncodeHintType, Object> hints = new HashMap<>();
			hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
			hints.put(EncodeHintType.MARGIN, 0);	

			// 生成矩阵
			BitMatrix bitMatrix = new MultiFormatWriter().encode(content, barcode, width, height, hints);

			//重新调整条形码或是二维码的边界
			bitMatrix = updateBit(bitMatrix, margin);
			// 输出图像
			MatrixToImageWriter.writeToFile(bitMatrix, format, file);
			
			createFlag=CREATE_SUCCESS;
		}

		return createFlag;
	}
	
	/** 
     *	@Title updateBit 
     *	@Description 调整条码的边界
     *	@param matrix	条码矩阵
     *	@param margin	条码边界
     *	@return			调整边界后的条码矩阵
     *	@Return BitMatrix    返回类型 
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
	
	
}
