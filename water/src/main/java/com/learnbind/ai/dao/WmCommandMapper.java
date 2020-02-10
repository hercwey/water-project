package com.learnbind.ai.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.WmCommand;

import tk.mybatis.mapper.common.Mapper;

public interface WmCommandMapper extends Mapper<WmCommand> {
	
	int deleteById(Long id);
    int save(CommandBean commandBean);
    CommandBean selectById(Long id);
    int modify(CommandBean commandBean);
    int updateByDeviceCommand(CommandBean commandBean);

    CommandBean selectByCommandId(CommandBean commandBean);
    
    public List<Map<String, Object>> searchList(@Param("searchCommandType") Integer searchCommandType, @Param("searchCond") String searchCond);
    
}