package com.learnbind.ai.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.learnbind.ai.mq.north.service.AutoReportDataProcessService;


@SpringBootTest(classes=AutoReportDataProcessService.class)
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration  //表示是一个web程序
public class AutoReportTest {

	@Test
	public void test() {

	}
	
}
