package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.iot.TestCommandBean;
import com.learnbind.ai.model.iot.WmCommand;

import tk.mybatis.mapper.common.Mapper;

public interface WmCommandMapper extends Mapper<WmCommand> {
	
	int deleteById(Long id);
    int save(TestCommandBean commandBean);
    TestCommandBean selectById(Long id);
    int modify(TestCommandBean commandBean);
    int updateByDeviceCommand(TestCommandBean commandBean);

    TestCommandBean selectByCommandId(TestCommandBean commandBean);
    
    public List<Map<String, Object>> searchList(@Param("searchCommandType") Integer searchCommandType, @Param("searchCond") String searchCond);
    public List<Map<String, Object>> searchByDeviceId(String deviceId);
}