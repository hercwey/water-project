package com.learnbind.ai.common;

import java.util.HashMap;
import java.util.Map;

public class RequestResultUtil {
	
	public static final String LOGIN_SUCCESS_INFO = "登录成功";
	public static final String LOGIN_FAIL_INFO = "登录失败，用户名或密码错误！";
	public static final String VERIFY_CODE_ERROR_INFO = "登录失败，验证码错误！";
	public static final String LOGININFO_EMPTY_ERROR_INFO = "登录失败，用户名或密码为空！";
	
	public static final String RESULT_CODE = "result_code";
	public static final String RESULT_CODE_SUCCESS = "success";
	public static final String RESULT_CODE_FAIL = "fail";
	public static final String RESULT_MSG = "result_msg";
	
	public static final String RESULT_SELECT_SUCCESS = "查询成功";
	public static final String RESULT_SELECT_WARN = "查询异常";
	public static final String RESULT_SAVE_WARN = "保存异常";
	public static final String RESULT_SAVE_SUCCESS = "保存成功";
	public static final String RESULT_UPDATE_WARN = "修改异常";
	public static final String RESULT_UPDATE_SUCCESS = "修改成功";
	public static final String RESULT_DELETE_WARN = "删除异常,要删除的对象不存在或者已删除";
	public static final String RESULT_DELETE_SUCCESS = "删除成功";
	public static final String RESULT_ADD_WARN = "添加异常";
	public static final String RESULT_ADD_SUCCESS = "添加成功";
	public static final String RESULT_UPLOAD_SUCCESS = "上传成功";
	public static final String RESULT_UPLOAD_WARN = "上传异常";
	public static final String RESULT_UPLOAD_FILE_EMPTY = "上传文件不存在";
	public static final String RESULT_UPLOAD_FILE_DELETE_SUCCESS = "删除上传文件成功";
	public static final String RESULT_UPLOAD_FILE_DELETE_WARN = "删除上传文件异常，文件已删除或不存在";
	
	static Map<String, Object> respMap = new HashMap<String, Object>();
	
	public RequestResultUtil(){
		
	}
	
	/**
	 * 方法功能：获取查询成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月10日 下午1:57:50</p>
	 */
	public static Map<String, Object> getResultSelectSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_SELECT_SUCCESS);
		return respMap;
	}
	public static Map<String, Object> getResultSelectSuccess(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	/**
	 * 方法功能：获取查询异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月10日 下午1:53:03</p>
	 */
	public static Map<String, Object> getResultSelectWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_SELECT_WARN);
		return respMap;
	}
	public static Map<String, Object> getResultSelectWarn(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	/**
	 * 方法功能：获取添加成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:07:53</p>
	 */
	public static Map<String, Object> getResultAddSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_ADD_SUCCESS);
		return respMap;
	}
	public static Map<String, Object> getResultAddSuccess(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	/**
	 * 方法功能：获取添加异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:08:13</p>
	 */
	public static Map<String, Object> getResultAddWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_ADD_WARN);
		return respMap;
	}
	public static Map<String, Object> getResultAddWarn(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	/**
	 * 方法功能：获取保存成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:01:22</p>
	 */
	public static Map<String, Object> getResultSaveSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_SAVE_SUCCESS);
		return respMap;
	}
	
	public static Map<String, Object> getResultSaveSuccess(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	
	
	/**
	 * 方法功能：获取保存异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:03:51</p>
	 */
	public static Map<String, Object> getResultSaveWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_SAVE_WARN);
		return respMap;
	}
	public static Map<String, Object> getResultSaveWarn(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	/**
	 * 方法功能：获取修改成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:05:03</p>
	 */
	public static Map<String, Object> getResultUpdateSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_UPDATE_SUCCESS);
		return respMap;
	}
	public static Map<String, Object> getResultUpdateSuccess(String message){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, message);
		return respMap;
	}
	
	
	
	/**
	 * 方法功能：获取修改异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:05:54</p>
	 */
	public static Map<String, Object> getResultUpdateWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_UPDATE_WARN);
		return respMap;
	}
	
	/**
	 * 方法功能：获取修改异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:05:54</p>
	 */
	public static Map<String, Object> getResultUpdateWarn(String msg){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, msg);
		return respMap;
	}
	
	public static Map<String, Object> getResultFail(String msg){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, msg);
		return respMap;
	}
	
	public static Map<String, Object> getResultSuccess(String msg){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, msg);
		return respMap;
	}
	
	/**
	 * 方法功能：获取删除成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:09:30</p>
	 */
	public static Map<String, Object> getResultDeleteSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_DELETE_SUCCESS);
		return respMap;
	}
	
	/**
	 * 方法功能：获取删除异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:09:55</p>
	 */
	public static Map<String, Object> getResultDeleteWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_DELETE_WARN);
		return respMap;
	}
	
	/**
	 * 方法功能：获取上传成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:12:02</p>
	 */
	public static Map<String, Object> getResultUploadSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_SUCCESS);
		respMap.put(RESULT_MSG, RESULT_UPLOAD_SUCCESS);
		return respMap;
	}
	
	/**
	 * 方法功能：获取上传异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:12:15</p>
	 */
	public static Map<String, Object> getResultUploadWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_UPLOAD_WARN);
		return respMap;
	}
	
	/**
	 * 方法功能：获取上传文件为空结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午2:12:27</p>
	 */
	public static Map<String, Object> getResultUploadFileEmpty(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_UPLOAD_FILE_EMPTY);
		return respMap;
	}
	
	/**
	 * 方法功能：获取删除上传文件成功结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午4:01:44</p>
	 */
	public static Map<String, Object> getResultUploadFileDeleteSuccess(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_UPLOAD_FILE_DELETE_SUCCESS);
		return respMap;
	}
	
	/**
	 * 方法功能：获取删除上传文件异常结果集
	 * @return
	 * <hr>
	 * <b>描述：</b>
	 * <p>Description:方法功能详细说明</p> 
	 * <p>Version: 1.0</p>
	 * <p>Author: srd </p>
	 * <p>Date: 2017年1月5日 下午4:02:12</p>
	 */
	public static Map<String, Object> getResultUploadFileDeleteWarn(){
		respMap.clear();
		respMap.put(RESULT_CODE, RESULT_CODE_FAIL);
		respMap.put(RESULT_MSG, RESULT_UPLOAD_FILE_DELETE_WARN);
		return respMap;
	}
	
}
