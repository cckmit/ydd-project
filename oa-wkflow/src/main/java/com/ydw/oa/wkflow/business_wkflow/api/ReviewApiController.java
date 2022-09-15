package com.ydw.oa.wkflow.business_wkflow.api;

import java.util.Map;

import com.ydw.oa.wkflow.util.date.DateTools;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.util.sms.SmsAliTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "审批时获取短信验证码")
@RestController
@RequestMapping("/api/review")
public class ReviewApiController {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthFeignService authFeignService;

	@ApiOperation(value = "获取短信验证码")
	@GetMapping("/get_code")
	public Wrapper<JSONObject> getCode(String mobile) {
		JSONObject result = new JSONObject();
		String code = RandomStringUtils.randomNumeric(6);
		SmsAliTools.sendCode(mobile, code);

		logger.info("短信验证码-------》" + code);
		
		result.put("code", code);
		result.put("date", DateTools.getToday("yyyy年MM月dd日"));
		result.put("result", "success");
		result.put("msg", "验证码发送成功!");
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "获取当天日期")
	@GetMapping("/get_date")
	public Wrapper<JSONObject> getDate() {
		JSONObject result = new JSONObject();
		result.put("date", DateTools.getToday("yyyy年MM月dd日"));
		result.put("result", "success");
		result.put("msg", "获取时间成功!");
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "获取用户信息")
	@GetMapping("/get_user")
	public Wrapper<Map<String, Object>> getUser(String userId) {
		Map<String, Object> user = authFeignService.getOne(userId).getResult();
		return WrapMapper.ok(user);

	}
}
