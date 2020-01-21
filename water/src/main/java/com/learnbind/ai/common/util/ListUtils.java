package com.learnbind.ai.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

/**
 * Copyright (c) 2019 by SRD
 * 
 * @Package com.learnbind.ai.common.util
 *
 * @Title: ListUtils.java
 * @Description: List工具类
 *
 * @author Administrator
 * @date 2019年11月24日 下午12:39:43
 * @version V1.0
 *
 */
public class ListUtils {

	/**
	 * @Title: deepCopy
	 * @Description: List深度复制
	 * @param <T>
	 * @param src
	 * @return
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static <T> List<T> deepCopy(List<T> src) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ObjectOutputStream out = new ObjectOutputStream(byteOut);
		out.writeObject(src);

		ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
		ObjectInputStream in = new ObjectInputStream(byteIn);
		@SuppressWarnings("unchecked")
		List<T> dest = (List<T>) in.readObject();
		return dest;
	}

}
