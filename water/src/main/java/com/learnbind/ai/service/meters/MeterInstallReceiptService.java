package com.learnbind.ai.service.meters;

import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

import com.itextpdf.text.DocumentException;
import com.learnbind.ai.model.CheckResultErecord;
import com.learnbind.ai.model.EngineeringDoc;
import com.learnbind.ai.model.MeterInstallRecepit;
import com.learnbind.ai.service.common.IBaseService;


/**
 * Copyright (c) 2019 by ZYC
 * 
 * @Package com.learnbind.ai.service.meters
 *
 * @Title: MeterInstallReceiptService.java
 * @Description: 水表安装领用单
 *
 * @author Thinkpad
 * @date 2019年10月26日 下午12:00:10
 * @version V1.0 
 *
 */
public interface MeterInstallReceiptService extends IBaseService<MeterInstallRecepit, Long> {
	/**
     * 批量删除
     * @param meterIds
     * @return
     */
    public int deleteInstallReceipt(List<Long> receiptIds);
    
    public List<MeterInstallRecepit> searchCond(String searchCond, Long meterId);
    
    
    public int printInstallReceipt(String FILE_DIR, String TEMPLATE_PREFIX, Long itemId, String TEMPLATE_FILE_NAME, Long printerId) throws IOException, PrinterException, DocumentException;
}
