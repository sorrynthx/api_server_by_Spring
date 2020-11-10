package com.controllers;

import java.util.Properties;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.services.CommonService;
import com.vos.VoTest;
import com.z.utility.MyFunctions;
import com.z.utility.WebClient;

@Controller
public class MainController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private MyFunctions myFn = new MyFunctions();
	
	@Inject
	CommonService commonService;
	
	@RequestMapping(value = {"", "/", "index"}, method = RequestMethod.GET)
    public String index(HttpServletRequest HttpRequest, HttpSession session, Model model,  @ModelAttribute VoTest voTest) {
		
		// ip check
		if (!myFn.ipCheck(HttpRequest)) return "no_index";
		
		// read out, in config file (내부, 외부 config 읽기)
		Properties out_config = myFn.read_outConfig();
		Properties in_config = myFn.read_inConfig();
		
		// Vo 맵핑
		voTest.setTest("test ID");
		voTest.setValue("test VALUE");
		
		// Api 호출 준비
		Gson gson = new Gson();
		String reqVo = gson.toJson(voTest);
		WebClient web = new WebClient();
		
		// Api 호출 (List)
		String sendRtnList = web.callHttpPOST(in_config.getProperty("api.url")+"/api/testListApi", reqVo.toString());
		if (sendRtnList != null) {
			
			// Json 파싱
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(sendRtnList);
			
			if(element.getAsJsonObject().get("data") != null){
				JsonArray testList = element.getAsJsonObject().get("data").getAsJsonArray();
				
				logger.info("JsonArray testList size : " + testList.size());
				logger.info("JsonArray testList : " 	 + testList);
				
				if(testList != null){
					model.addAttribute("testList", testList);
				}
			}
		}
		
		// Api 호출 (Object)
		String sendRtnObject = web.callHttpPOST(in_config.getProperty("api.url")+"/api/testObjectApi", reqVo.toString());
		if (sendRtnObject != null) {
			
			// Json 파싱
			JsonParser parser = new JsonParser();
			JsonElement element = parser.parse(sendRtnObject);
			
			if(element.getAsJsonObject().get("data") != null){
				JsonObject testObject = element.getAsJsonObject().get("data").getAsJsonObject();
				
				logger.info("JsonObject testObject : " 	 + testObject);
				
				if(testObject != null){
					model.addAttribute("testObject", testObject);
				}
			}
		}
		
		return "index";
    }
	
	
	
}
