package com.small.bdp.framework.dto;

/**
 * 请调用结果返回
 * 
 * @author xiaojiao_li
 *
 */
public class ResultDto {

	private boolean success = true;

	private String msg;

	private int errorCode = -1;

	private Object userObject;

	public ResultDto() {
	}

	public ResultDto(boolean success) {
		this.success = success;
	}

	public ResultDto(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getUserObject() {
		return userObject;
	}

	public void setUserObject(Object userObject) {
		this.userObject = userObject;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
