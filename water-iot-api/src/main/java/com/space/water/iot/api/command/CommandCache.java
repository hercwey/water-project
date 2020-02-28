package com.space.water.iot.api.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.space.water.iot.api.util.LogUtil;

public class CommandCache {

	Map<String, ArrayList<String>> commandMap;
	private static CommandCache instance = new CommandCache();

	// 让构造函数为 private，这样该类就不会被实例化
	private CommandCache() {
		initCommandMap();
	}

	// 获取唯一可用的对象
	public static CommandCache getInstance() {
		return instance;
	}
	
	private void initCommandMap() {
		commandMap = new HashMap<String, ArrayList<String>>();
	}
	
	public ArrayList<String> getCommandList(String deviceId) {
		return commandMap.get(deviceId);
	}
	public void setCommandList(String deviceId,ArrayList<String> commandList) {
		commandMap.put(deviceId, commandList);
	}
	public void addCommand(String deviceId,String command) {
		ArrayList<String> commandList = commandMap.get(deviceId);
		if (commandList == null) {
			commandList = new ArrayList<>();
		}
		commandList.add(command);
		
		commandMap.put(deviceId, commandList);
		
		LogUtil.debug("-------------------------------------");
		LogUtil.debug("| 新增命令");
		LogUtil.debug("| DeviceID：" + deviceId);
		LogUtil.debug("| Command ：" + command);
		LogUtil.debug("-------------------------------------");
		LogUtil.debug("-------------------------------------");
		LogUtil.debug("| 设备（" + deviceId + "）指令总计：" + commandList.size() + " 条");
		LogUtil.debug("-------------------------------------");
	}
}
