/**
 *  @author xiaoliu
 */

package com.ccq.framework.lang;

import java.io.Serializable;

public class JsonResult implements Serializable{

	private static final long serialVersionUID = 8324599203636896151L;

	private boolean success;
	private String message;
	private String code;
	private Object object;
	private String serviceCode;
	
	
	public JsonResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 *
	 * @param success
	 * @param message
	 */
	public JsonResult(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	
	/**
	 *
	 * @param success
	 * @param message
	 * @param code
	 */
	public JsonResult(boolean success, String message, String code) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
	}

	/**
	 *
	 * @param success
	 * @param message
	 * @param code
	 * @param object
	 * @param serviceCode
	 */
	public JsonResult(boolean success, String message, String code, Object object, String serviceCode) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
		this.object = object;
		this.serviceCode = serviceCode;
	}
	
	/**
	 *
	 * @param message
	 */
	public void success(String message) {
		setSuccess(true);
		setMessage(message);
	}
	
	/**
	 *
	 * @param message
	 */
	public void success(String message,String code) {
		setSuccess(true);
		setMessage(message);
		setCode(code);
	}
	
	/**
	 *
	 * @param message
	 */
	public void success(String message,String code,Object obj) {
		setSuccess(true);
		setMessage(message);
		setCode(code);
		setObject(obj);
	}
	
	/**
	 * @param message
	 */
	public void error(String message) {
		setSuccess(false);
		setMessage(message);
	}
	
	public void error(String message,String code) {
		setSuccess(false);
		setMessage(message);
		setCode(code);
	}
	
	public void error(String message,String code,Object obj) {
		setSuccess(false);
		setCode(code);
		setObject(obj);
	}

	public static JsonResult getErrorResult(String message) {
		return new JsonResult(false,message);
	}

	public static JsonResult getSuccessResult(String message) {
		return new JsonResult(true,message);
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
}
