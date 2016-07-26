package com.small.bdp.framework.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IExceptionProcess {

	public void doExceptionProcess(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex);

}
