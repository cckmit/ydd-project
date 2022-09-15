package com.ydw.oa.wkflow.business_wx;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.alibaba.fastjson.JSONObject;

import feign.Headers;

@Component
@FeignClient(value = "${wkfow.wx}", path="/oa-wechat", url = "")
public interface WxFeignService {

	// 发送信息
	@PostMapping("/api/message/textcard")
	@Headers("Content-Type: application/json")
	public String textcard(@RequestBody JSONObject body);
	
	@PostMapping("/api/message/text")
	@Headers("Content-Type: application/json")
	public String text(@RequestBody JSONObject body);

}
