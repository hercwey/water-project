package com.learnbind.ai.service.common;

import java.util.List;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

/**
 * Created by srd on 2017/02/21.
 * @ClassName AbstractBaseService
 * @Description 数据库存取公共泛型类
 * @author Administrator
 * @Date 2017年5月5日 下午3:07:36
 * @version 1.0.0
 * @param <T>
 * @param <ID>
 */
public abstract class AbstractBaseService<T,ID> implements IBaseService<T,ID> {
	
    protected Mapper<T> mapper;

    public void setMapper(Mapper<T> mapper){
        this.mapper=mapper;
    }

    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#insertSelective(java.lang.Object)
     * 添加实体
     */
    @Override
    public int insertSelective(T entity) {
       return mapper.insertSelective(entity);
    }
    
    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#selectAll()
     * 查询所有
     */
    @Override
    public List<T> selectAll() {
        return mapper.selectAll();
    }
    
    /**
     * @see com.ecp.service.IBaseService#select(java.lang.Object)
     */
    @Override
	public List<T> select(T entity) {
		return mapper.select(entity);
	}
    
    /** 
     * @Title: selectOne
     * @Description: 根据实体类查询一条数据
     * @param entity
     * @return 
     * @see com.learnbind.ai.service.common.IBaseService#selectOne(java.lang.Object)
     */
    @Override
	public T selectOne(T entity) {
		return mapper.selectOne(entity);
	}

	/**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#selectByPrimaryKey(java.lang.Object)
     * 根据主键查询实体
     */
    @Override
    public T selectByPrimaryKey(ID id) {
        return mapper.selectByPrimaryKey(id);
    }
    
    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#selectByExample(tk.mybatis.mapper.entity.Example)
     * 根据Example查询
     */
    @Override
	public List<T> selectByExample(Example example) {
		return mapper.selectByExample(example);
	}

	/**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#updateByPrimaryKeySelective(java.lang.Object)
     * 根据主键修改
     */
    @Override
    public int updateByPrimaryKeySelective(T entity) {
      return mapper.updateByPrimaryKeySelective(entity);
    }
    
    /** 
     * @Title: selectCount
     * @Description: 根据实体中的属性查询总数，查询条件使用等号
     * @param entity
     * @return 
     * @see com.learnbind.ai.service.common.IBaseService#selectCount(java.lang.Object)
     */
    @Override
    public int selectCount(T entity) {
      return mapper.selectCount(entity);
    }
    
    /** 
     * @Title: selectCountByExample
     * @Description: 根据Example条件进行查询总数
     * @param example
     * @return 
     * @see com.learnbind.ai.service.common.IBaseService#selectCountByExample(tk.mybatis.mapper.entity.Example)
     */
    @Override
    public int selectCountByExample(Example example) {
      return mapper.selectCountByExample(example);
    }
    
    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#updateByExampleSelective(java.lang.Object, tk.mybatis.mapper.entity.Example)
     * 根据条件修改
     */
    @Override
    public int updateByExampleSelective(T entity, Example example) {
      return mapper.updateByExampleSelective(entity, example);
    }

    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#deleteByPrimaryKey(java.lang.Object)
     * 根据主键删除
     */
    @Override
    public int deleteByPrimaryKey(ID id) {
        return mapper.deleteByPrimaryKey(id);
    }
    
    /**
     * @author: srd $Date: 2017年2月21日
     * @see com.ecp.service.IBaseService#delete(java.lang.Object)
     * 根据实体类删除
     */
    @Override
    public int delete(T entity) {
        return mapper.delete(entity);
    }
    
    /** 
     * (非 Javadoc)
     * 
     * @Title: deleteByExample
     * @Description: 根据条件删除
     * @param example
     * @return 
     * @see com.learnbind.ai.service.common.IBaseService#deleteByExample(java.lang.Object)
     */
    @Override
    public int deleteByExample(Object example) {
        return mapper.deleteByExample(example);
    }
    
}
