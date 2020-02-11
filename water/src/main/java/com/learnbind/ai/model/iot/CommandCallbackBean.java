package com.learnbind.ai.model.iot;

public class CommandCallbackBean {

	/**
	 * 当命令状态变化时(执行失败，执行成功，超时，发送，已送达)会通知NA， 平台会以POST方式发送HTTP消息给应用，请求Body为json字符串，
	 * 格式形如：{"deviceId":"deviceId","commandId":"commandId","result":{"status":"SUCCESS","result":{…}}}
	 */
	public static final String COMMAND_CALLBACK_STATUS_SENT = "SENT";
	public static final String COMMAND_CALLBACK_STATUS_PENDING = "PENDING";
	public static final String COMMAND_CALLBACK_STATUS_DELIVERED = "DELIVER";
	public static final String COMMAND_CALLBACK_STATUS_FAILED = "FAIL";
	public static final String COMMAND_CALLBACK_STATUS_SUCCESS = "SUCCESS";
	public static final String COMMAND_CALLBACK_STATUS_TIMEOUT = "TIMEOUT";

	public static final int COMMAND_STATUS_SENT = 1;
	public static final int COMMAND_STATUS_PENDING = 2;
	public static final int COMMAND_STATUS_DELIVERED = 3;
	public static final int COMMAND_STATUS_FAILED = -1;
	public static final int COMMAND_STATUS_SUCCESS = 4;
	public static final int COMMAND_STATUS_TIMEOUT = 5;

	/**
	 * 已取消，已发送，已送达，过期，超时，等待，成功，失败
	 */
	public static final String COMMAND_RESP_CANCEL = "CANCEL";
	public static final String COMMAND_RESP_SENT = "SENT";
	public static final String COMMAND_RESP_DELIVERED = "DELIVER";
	public static final String COMMAND_RESP_EXPIRED = "EXPIRED";
	public static final String COMMAND_RESP_TIMEOUT = "TIMEOUT";
	public static final String COMMAND_RESP_PENDING = "PENDING";
	public static final String COMMAND_RESP_SUCCESS = "SUCCESS";
	public static final String COMMAND_RESP_FAIL = "FAIL";
}
