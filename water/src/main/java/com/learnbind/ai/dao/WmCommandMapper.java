package com.learnbind.ai.dao;

import com.learnbind.ai.model.iot.CommandBean;
import com.learnbind.ai.model.iot.WmCommand;
import tk.mybatis.mapper.common.Mapper;

public interface WmCommandMapper extends Mapper<WmCommand> {
	
	int deleteById(Long id);
    int save(CommandBean commandBean);
    CommandBean selectById(Long id);
    int modify(CommandBean commandBean);
    int updateByDeviceCommand(CommandBean commandBean);
}