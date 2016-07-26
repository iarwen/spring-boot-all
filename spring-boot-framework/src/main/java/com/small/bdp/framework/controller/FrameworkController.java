package com.small.bdp.framework.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.small.bdp.framework.dto.ResultDto;

@Controller
public class FrameworkController {

	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView index() {
		ModelAndView mv = new ModelAndView("index");

		return mv;
	}

	@RequestMapping(value = "/login", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login() {
		ModelAndView mv = new ModelAndView("login");

		return mv;
	}

	@RequestMapping(value = "/main", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main() {
		ModelAndView mv = new ModelAndView("main");

		return mv;
	}

	@RequestMapping(value = "/bdp/serverinfos", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView serverinfos() {
		ModelAndView mv = new ModelAndView("bdp_freamwork/serverinfos");

		return mv;
	}

	@RequestMapping(value = "/bdp/active", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody ResultDto active() {
		ResultDto rs = new ResultDto(true);
		return rs;
	}

}
