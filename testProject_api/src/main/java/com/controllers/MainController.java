package com.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.services.CommonService;
import com.vos.ReturnVo;
import com.vos.VoTest;
import com.z.utility.MyFunctions;

@Controller
public class MainController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MyFunctions myFn = new MyFunctions();
	
	@Inject
	CommonService commonService;
	
	@RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index(HttpServletRequest HttpRequest, HttpSession session, Locale locale, Model model) {
		
		// ip check
		if (!myFn.ipCheck(HttpRequest)) return "no_index";
		
		// read out, in config file (내부, 외부 config 읽기)
		Properties out_config = myFn.read_outConfig();
		Properties in_config = myFn.read_inConfig();
		
				
		return "index";
    }
	
	// Api 리스트
	@RequestMapping(value = "/api/testListApi", method = RequestMethod.POST, produces={"application/json", "application/json; charset=UTF-8"})
    public @ResponseBody ReturnVo testListApi(@RequestBody String reqVo, HttpServletRequest HttpRequest, HttpSession session,
    		Locale locale, Model model) throws Exception {

		logger.info("/api/testListApi");
		
		Gson gson = new Gson();	logger.info("reqVo : =>" + reqVo);
		VoTest voTest = gson.fromJson(reqVo, VoTest.class);			// DB 매개변수로 이용
		
		// DB
		List<VoTest> testList = new ArrayList<VoTest>();
		voTest.setMessage("api message for List");
		testList.add(voTest);
		
		return new ReturnVo("YES", "Test for List Success", testList);
    }
	
	// Api 오브젝트
	@RequestMapping(value = "/api/testObjectApi", method = RequestMethod.POST, produces={"application/json", "application/json; charset=UTF-8"})
    public @ResponseBody ReturnVo testObjectApi(@RequestBody String reqVo, HttpServletRequest HttpRequest, HttpSession session,
    		Locale locale, Model model) throws Exception {

		logger.info("/api/testObjectApi");
		
		Gson gson = new Gson();	logger.info("reqVo : =>" + reqVo);
		VoTest voTest = gson.fromJson(reqVo, VoTest.class);			// DB 매개변수로 이용
		
		// DB
		VoTest testObject = new VoTest();
		testObject.setTest(voTest.getTest());
		testObject.setValue(voTest.getValue());
		testObject.setMessage("api message for Object");
		
		
		return new ReturnVo("YES", "Test for Object Success", testObject);
    }
	
}
