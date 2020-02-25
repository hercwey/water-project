package com.space.water.iot.api.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 异常处理工具类
 *
 */
public class ExceptionUtil {
	/**
	 * 换行符
	 */
	public final static String LINE_SEPARATOR = "\r\n";

	/**
	 * 获取异常的堆栈信息
	 * 
	 * @param e 异常
	 * @return
	 */
	public static String getExceptionStackTrace(Throwable e) {
		String stackTrace = "";
		StringWriter writer = null;
		PrintWriter bw = null;
		if (e == null) {
			return "";
		}

		try {
			writer = new StringWriter();
			bw = new PrintWriter(writer);
			e.printStackTrace(bw);
			stackTrace = writer.getBuffer().toString();
		} catch (Exception e1) {
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (Exception e2) {
				}
			}

			if (bw != null) {
				try {
					bw.close();
				} catch (Exception e2) {
				}
			}
		}
		return stackTrace;
	}

	/**
	 * 获取指定行数的异常堆栈信息
	 * 
	 * @param e       异常
	 * @param lineNum 要打印的堆栈行数
	 * @return
	 */
	public static String getExceptionStackTrace(Throwable e, int lineNum) {
		if (e == null) {
			return "";
		}

		StringBuffer stackTrace = new StringBuffer(e.toString());
		StackTraceElement[] astacktraceelement = e.getStackTrace();
		int size = lineNum > astacktraceelement.length ? astacktraceelement.length : lineNum;

		for (int i = 0; i < size; i++) {
			stackTrace.append(LINE_SEPARATOR).append("\tat ").append(astacktraceelement[i]);
		}

		return stackTrace.toString();
	}

	/**
	 * 获取堆栈日志
	 * 
	 * @param stackTraceElements 堆栈信息
	 * @return
	 */
	public static String getStackTraceLog(StackTraceElement[] stackTraceElements) {
		if (stackTraceElements == null) {
			return "";
		}

		StringBuffer stackTrace = new StringBuffer();
		int size = stackTraceElements.length;

		for (int i = 0; i < size; i++) {
			stackTrace.append(LINE_SEPARATOR).append("\tat ").append(stackTraceElements[i]);
		}

		return stackTrace.toString();
	}

	/**
	 * 获取简洁的异常堆栈，只打印最后五个调用堆栈
	 * 
	 * @param e
	 * @return
	 */
	public static String getBriefExceptionStackTrace(Throwable e) {
		return getExceptionStackTrace(e, 5);
	}
}
