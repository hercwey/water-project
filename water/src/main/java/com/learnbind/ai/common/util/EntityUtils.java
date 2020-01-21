package com.learnbind.ai.common.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Copyright (c) 2018 by SRD
 * 
 * @Package com.learnbind.ai.common.util
 *
 * @Title: EntityUtils.java
 * @Description: 实体对象与Map之间的转换工具类
 *
 * @author Administrator
 * @date 2019年5月22日 下午12:35:52
 * @version V1.0 
 *
 */
public class EntityUtils {

	/**
	 * @Title: entityToMap
	 * @Description: 实体类转Map
	 * @param object
	 * @return 
	 */
	public static Map<String, Object> entityToMap(Object object) {
		Map<String, Object> map = new HashMap<>();
		for (Field field : object.getClass().getDeclaredFields()) {
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				map.put(field.getName(), o);
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return map;
	}	
	
	
	public static List<Map<String, Object>> entityToListMap(Object object) {
		List<Map<String, Object>> list = new ArrayList<>();
		Field[] fieldArr=object.getClass().getDeclaredFields();
		for (Field field : fieldArr) {
			try {
				boolean flag = field.isAccessible();
				field.setAccessible(true);
				Object o = field.get(object);
				Map<String,Object> map=new HashMap<>();
				map.put(field.getName(), o);
				list.add(map);
				field.setAccessible(flag);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	

	/**
	 * @Title: mapToEntity
	 * @Description: Map转实体类	TODO 此函数需要调整
	 * @param <T>
	 * @param map
	 * @param entity
	 * @return 
	 */
	public static <T> T mapToEntity(Map<String, Object> map, Class<T> entity) {
		T t = null;
		try {
			t = entity.newInstance();
			for (Field field : entity.getDeclaredFields()) {
				if (map.containsKey(field.getName())) {
					boolean flag = field.isAccessible();
					field.setAccessible(true);
					Object object = map.get(field.getName());
					if (object != null && field.getType().isAssignableFrom(object.getClass())) {
						field.set(t, object);
					}
//					if(field.getType().toString().contains("Integer"))//判断属性类型 进行转换,map中存放的是Object对象需要转换 实体类中有多少类型就加多少类型,实体类属性用包装类;
//                        if(field.getName().equals(mapKey)){
//                        	field.set(field,Integer.valueOf(map.get(mapKey).toString()));
//                            break;
//                        }
//                        if(field.getType().toString().contains("String") )//判断属性类型 进行转换;
//                            if(field.getName().equals(mapKey)){
//                            	field.set(t,map.get(mapKey));
//                    
//                                break;
//                            }
//                    }
					
//					if (String.class.isAssignableFrom(type)) {
//                        field.set(o, defVal);
//                    } else if (Number.class.isAssignableFrom(type)) {
//                        if (Byte.class.isAssignableFrom(type)) {
//                            field.set(o, Byte.valueOf(defVal));
//                        } else if (Float.class.isAssignableFrom(type)) {
//                            field.set(o, Float.valueOf(defVal));
//                        } else if (Short.class.isAssignableFrom(type)) {
//                            field.set(o, Short.valueOf(defVal));
//                        } else if (Integer.class.isAssignableFrom(type)) {
//                            field.set(o, Integer.valueOf(defVal));
//                        } else if (Double.class.isAssignableFrom(type)) {
//                            field.set(o, Double.valueOf(defVal));
//                        } else if (Long.class.isAssignableFrom(type)) {
//                            field.set(o, Long.valueOf(defVal));
//                        }
//                    }
					
					field.setAccessible(flag);
				}
			}
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return t;
	}
}
