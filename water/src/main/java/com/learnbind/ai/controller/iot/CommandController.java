package com.learnbind.ai.controller.iot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.learnbind.ai.iot.protocol.PacketCodec;
import com.learnbind.ai.iot.protocol.PacketFrame;
import com.learnbind.ai.iot.protocol.bean.MeterBase;
import com.learnbind.ai.iot.protocol.bean.MeterConfig;
import com.learnbind.ai.iot.protocol.bean.MeterConfigReadCmd;
import com.learnbind.ai.iot.protocol.bean.MeterConfigWriteCmd;
import com.learnbind.ai.iot.protocol.util.HexStringUtils;
import com.learnbind.ai.iot.util.StringUtil;
import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.CommandCallbackBean;
import com.learnbind.ai.model.iot.CommandResultBean;
import com.learnbind.ai.model.iot.DeviceBean;
import com.learnbind.ai.model.iot.JsonResult;
import com.learnbind.ai.model.iot.MeterConfigBean;
import com.learnbind.ai.service.iot.ICommandService;
import com.learnbind.ai.service.iot.IDeviceService;

@Controller
@RequestMapping("/cmd")
public class CommandController {

    @Autowired
    ICommandService commandService;
    @Autowired
    IDeviceService deviceService;

    @RequestMapping(value = "/send", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> send(@RequestBody String data) {
        //TODO 下发指令
        CommandBean commandBean = CommandBean.parseJson(data);

        //TODO 将指令写入数据库，status设置为0（指令已创建，未下发成功）
        commandBean.setDatabaseStatus(0);
        commandBean.setCreateTime(new Date());

        commandService.save(commandBean);

        return ResponseEntity.ok(JsonResult.success(JsonResult.SUCCESS, JsonResult.STATUS_SUCCESS).toString());
    }

    @RequestMapping(value = "/callback", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> callback(@RequestBody String data) {
        //TODO 指令执行结果
        System.out.println("控制指令执行结果：" + data);

        //TODO 更改status为-1（设备执行失败）或2（设备执行成功）
        CommandResultBean commandResultBean = CommandResultBean.parseJson(data);

        CommandBean commandBean = new CommandBean();
        commandBean.setDeviceId(commandResultBean.getDeviceId());
        commandBean.setCommandId(commandResultBean.getCommandId());
        
        //FIXME G11 判断指令执行结果，逻辑待优化
        if (data.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_DELIVERED) || data.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_SENT) || data.contains(CommandCallbackBean.COMMAND_CALLBACK_STATUS_SUCCESS)) {
        	commandBean.setDatabaseStatus(2);
		} else {
            commandBean.setDatabaseStatus(-1);
		}
        
        commandBean.setDesc(commandResultBean.getReason());
        if (commandBean.getDatabaseStatus() == 2) {
			//指令执行成功后，解析数据，并保存
        	CommandBean temp = commandService.getCommandBeanByCommandId(commandBean);
        	String commandStr = JSON.parseObject(temp.getMethodParams()).getString("value");
        	
        	//判断是否为设置配置操作
        	PacketFrame packetFrame = PacketCodec.decodeFrame(HexStringUtils.hexStringToBytes(commandStr));
        	MeterBase meterBase = PacketCodec.decodeData(packetFrame);
            
            //FIXME G11 针对数据上报的其他类型数据，进行解析（后续根据需求对数据进行分类，优化处理逻辑）
            if (meterBase instanceof MeterConfigWriteCmd) {
            	MeterConfig meterConfig = (MeterConfig)PacketCodec.decodeData(packetFrame);
                MeterConfigBean configBean = MeterConfigBean.fromMeterConfig(meterConfig);
            	DeviceBean deviceBean = new DeviceBean();
            	deviceBean.setDeviceId(commandBean.getDeviceId());
            	deviceBean = DeviceBean.fromWmDevice(deviceService.getDeviceByDeviceId(deviceBean));
            	
            	deviceBean.setMeterConfig(MeterConfigBean.toJsonString(configBean));
            	deviceService.modifyDevice(deviceBean);
			} else if (meterBase instanceof MeterConfigReadCmd) {
				
			}
		}
        commandService.updateByDeviceCommand(commandBean);

        return ResponseEntity.ok(JsonResult.success(JsonResult.SUCCESS,data).toString());
    }
    
    //----------------加载发送指令记录主页面部分----------------------------------------------------------------------------------
  	@RequestMapping(value = "/load-main")
  	public String loadMain(Model model) {
  		return "iot/wm_command/command_main";
  	}
  	
  	//------------------查询发送指令记录----------------------------------------------------------------------------------------------
    @RequestMapping(value = "/search-send-cmd-records")
    public String searchSendCmdRecords(Model model, Integer pageNum, Integer pageSize, Integer searchCommandType, String searchCond) {
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	// 判定页码有效性
		if (pageNum == null || pageNum == 0) {
			pageNum = 1;
			pageSize = 5;//PagerConstant.DEFAULT_PAGE_SIZE;
		}

		// 查询并分页
		PageHelper.startPage(pageNum, pageSize); // PageHelper
		List<Map<String, Object>> commandMapList = commandService.searchList(searchCommandType, searchCond);
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(commandMapList);// (使用了拦截器或是AOP进行查询的再次处理)

		for(Map<String, Object> commandMap : commandMapList) {
			Object createTime = commandMap.get("CREATE_TIME");
			Object platformIssuedTime = commandMap.get("PLATFORM_ISSUED_TIME");
			
			String createTimeStr = "";
			if(createTime!=null) {
				createTimeStr = sdf.format((Date)createTime);
			}
			String platformIssuedTimeSdf = "";
			if(platformIssuedTime!=null) {
				Date platformIssuedTimeD = StringUtil.timeZoneTrans((String)platformIssuedTime);
				platformIssuedTimeSdf = sdf.format(platformIssuedTimeD);
			}
			commandMap.put("CREATE_TIME_STR", createTimeStr);
			commandMap.put("PLATFORM_ISSUED_TIME_STR", platformIssuedTimeSdf);
		}
		// 传递如下数据至前台页面
		model.addAttribute("commandMapList", commandMapList); // 列表数据
		
		//分页数据
		model.addAttribute("pageInfo", pageInfo);
		model.addAttribute("pageNum", pageNum);
		model.addAttribute("pageSize", pageSize);
		
		return "iot/wm_command/command_table";
    	
    }

}
