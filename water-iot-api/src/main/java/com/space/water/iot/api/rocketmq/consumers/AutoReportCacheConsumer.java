package com.space.water.iot.api.rocketmq.consumers;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.space.water.iot.api.model.report.BaseReportData;
import com.space.water.iot.api.rocketmq.MQConstant;
import com.space.water.iot.api.service.IReportService;
import com.space.water.iot.api.util.LogUtil;

@Component
public class AutoReportCacheConsumer extends BaseConsumer {

	@Autowired
	IReportService reportService;
	
	public AutoReportCacheConsumer() throws MQClientException {
		super();
	}
	
	@Override
	void processMessage(Message msg) {
		LogUtil.debug("--------------------------");
		LogUtil.debug("| 处理上报数据 ：" + new String(msg.getBody()));
		LogUtil.debug("--------------------------");
		// TODO 数据解析
		BaseReportData meterBean = BaseReportData.fromUploadDataJson(new String(msg.getBody()));
		reportService.process(meterBean);
	}

	@Override
	void initParams() {
		group = MQConstant.G_AUTO_REPORT_CACHE;
		tag = topicConfig.getTagAutoReportCache();
	}

}
