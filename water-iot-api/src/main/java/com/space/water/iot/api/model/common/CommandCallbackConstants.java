package com.space.water.iot.api.model.common;

public class CommandCallbackConstants {

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

	/**
	 * 等于0，为未向电信平台发送
	 * 大于0，已下发电信平台
	 * 小于0，指令执行失败
	 */
	public static final int COMMAND_STATUS_UNSENT = 0;//未发送
	public static final int COMMAND_STATUS_SENT = 1;//已发送
	public static final int COMMAND_STATUS_PENDING = 2;//等待
	public static final int COMMAND_STATUS_DELIVERED = 3;//已送达
	public static final int COMMAND_STATUS_SUCCESS = 4;//执行成功
	public static final int COMMAND_STATUS_FAILED = -1;//执行失败
	public static final int COMMAND_STATUS_TIMEOUT = -2;//超时

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
