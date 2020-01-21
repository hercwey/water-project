package com.learnbind.ai.bean;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 *  added by jch 2019/06/11
 */
public class FastJsonUtils {
    public static <T> T getObject(String jsonString, Class<T> cls) {
        T t = null;
        try {
            t = JSON.parseObject(jsonString, cls);
        } catch (Exception e) {

        }
        return t;
    }

    public static<T> List<T> getArray(String jsonString, Class<T> cls) {
        List<T> list = new ArrayList<>();
        try {
            list = JSON.parseArray(jsonString, cls);
        } catch (Exception e) {
        }
        return list;
    }

}
