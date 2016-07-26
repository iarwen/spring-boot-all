package com.small.bdp.framework.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.small.bdp.framework.dto.ResultDto;
import com.small.bdp.util.StringUtils;

public class BDPFreamworkExceptionHandler implements HandlerExceptionResolver {

	private static Logger logger = Logger.getLogger(BDPFreamworkExceptionHandler.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		if (!(ex instanceof BizException)) {
			ex.printStackTrace();
		}

		// 添加异常处理类， TODO 暂定默认一下邮件异常处理器
		try {
			String defaultClass = "com.ishop.cloud.exception.MailExceptionProcess";
			IExceptionProcess process = (IExceptionProcess) Class.forName(defaultClass).newInstance();
			if (process != null) {
				process.doExceptionProcess(request, response, handler, ex);
			}
		} catch (Exception eee) {
			logger.error(eee);
		}

		HandlerMethod mathod = (HandlerMethod) handler;
		ResponseBody body = mathod.getMethodAnnotation(ResponseBody.class);
		// 判断有没有@ResponseBody的注解没有的话调用父方法
		if (body == null) {
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("ex", ex);
			return new ModelAndView("bdp_freamwork/error", model);
		} else {
			ResultDto errorDto = new ResultDto(false, StringUtils.isEmptyString(ex.getMessage()) ? ex.toString() : ex.getMessage());
			if (ex instanceof BaseException) {
				errorDto.setErrorCode(((BaseException) ex).getErrorCode());
			}

			Map<String, Object> map = new HashMap<String, Object>();

			ObjectMapper om = new ObjectMapper();
			String exceptionMsg = "系统发生错误!";
			try {
				exceptionMsg = om.writeValueAsString(errorDto);
				@SuppressWarnings("unchecked")
				Map<String, Object> tempMap = JSONObject.parseObject(exceptionMsg, Map.class);
				map.putAll(tempMap);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}

			// 把异常按Json格式正确返回，同时避免服务器响应状态异常的问题
			ModelAndView mav = new ModelAndView();
			MappingJackson2JsonView view = new MappingJackson2JsonView();
			view.setAttributesMap(map);
			mav.setView(view);
			return mav;

		}
	}

}
