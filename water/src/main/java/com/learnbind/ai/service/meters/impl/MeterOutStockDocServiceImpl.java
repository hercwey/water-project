package com.learnbind.ai.service.meters.impl;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.common.util.EntityUtils;
import com.learnbind.ai.dao.MeterOutStockDocMapper;
import com.learnbind.ai.model.MeterInstallRecepit;
import com.learnbind.ai.model.MeterOutStockDoc;
import com.learnbind.ai.model.PrinterConfig;
import com.learnbind.ai.service.common.AbstractBaseService;
import com.learnbind.ai.service.meters.MeterOutStockDocService;
import com.learnbind.ai.service.notify.GenerateNotify;
import com.learnbind.ai.service.printer.PrinterService;
import com.learnbind.ai.util.pdf.PrintFile;



/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters.impl
 *
 * @Title: MeterInstallReceiptServiceImpl.java
 * @Description:水表安装领用单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午12:01:50
 * @version V1.0 
 *
 */
@Service
public class MeterOutStockDocServiceImpl extends AbstractBaseService<MeterOutStockDoc, Long> implements MeterOutStockDocService {
	
	@Autowired
	public MeterOutStockDocMapper meterOutStockDocMapper;
	@Autowired
	public GenerateNotify generateNotify;
	@Autowired
	private PrinterService printerService;// 打印机配置
	@Autowired
	private PrintFile printFile;


		
	/** 
		* <p>Title:采用构造函数注入 </p> 
		* <p>Description: </p> 
		* @param mapper 
	*/
	public MeterOutStockDocServiceImpl(MeterOutStockDocMapper mapper) {
		this.meterOutStockDocMapper=mapper;
		this.setMapper(mapper);
	}
	
	@Override
	@Transactional
	public int deleteOutStockDoc(List<Long> receiptIds) {
		try {
			int rows = 0;
			for(Long receiptId : receiptIds) {
				rows = this.deleteByPrimaryKey(receiptId);
				if(rows<=0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					break;
				}
			}
			return rows;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		return 0;
	}

	@Override
	public List<MeterOutStockDoc> searchCond(String searchCond, Long meterId) {
		return meterOutStockDocMapper.searchCond(searchCond, meterId);
	}

	@Override
	public int printReturnReceipt(String FILE_DIR, String TEMPLATE_PREFIX, Long itemId, String TEMPLATE_FILE_NAME,
			Long printerId) throws IOException, PrinterException, DocumentException {
		List<String> pdfFileList = new ArrayList<>();
		MeterOutStockDoc receipt = new MeterOutStockDoc();
		if(itemId != null) {
			receipt= this.selectByPrimaryKey(itemId);
		}
		Map<String, Object> meterMap = EntityUtils.entityToMap(receipt);
		meterMap.put("outStockDateStr", receipt.getOutStockDateStr());
		String pdfFileName = generateNotify.generateNotifyPDF(FILE_DIR, TEMPLATE_PREFIX, TEMPLATE_FILE_NAME,
				meterMap, receipt.getMeterNo(), receipt.getCaliber());
		pdfFileList.add(pdfFileName);
		// 查询打印小区编码
		sendPDFToPrinter(pdfFileList, printerId);
		if(pdfFileList.size() > 0) {
			return 1;
		}
		return 0;
	}
	
	/**
	 * @Title: sendPDFToPrinter
	 * @Description: 将文件发送给打印机
	 * @param pdfFileList
	 * @param printerId
	 * @throws IOException
	 * @throws PrinterException 
	 */
	private void sendPDFToPrinter(List<String> pdfFileList, Long printerId)
			throws IOException, PrinterException {
		PrinterConfig printerConfig = printerService.selectByPrimaryKey(printerId);
		// 打印PDF文件
		for (int i = 0; i < pdfFileList.size(); i++) {
			System.out.println(pdfFileList.get(i));
			printFile.printPDF(pdfFileList.get(i), printerConfig.getPrinterName());
		}
	}

}
