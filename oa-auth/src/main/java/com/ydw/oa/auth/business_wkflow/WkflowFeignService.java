package com.ydw.oa.auth.business_wkflow;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tmsps.fk.common.wrapper.Wrapper;

@Component
@FeignClient(value = "${auth.wkflow}", path = "/oa-wkflow", url = "")
@RequestMapping("/api")
public interface WkflowFeignService {

	@GetMapping("/file/get")
	public Wrapper<Map<String, Object>> get(@RequestParam("fileId") String fileId);

	@GetMapping("/file/rename")
	public Wrapper<String> rename(@RequestParam("fileId") String fileId, @RequestParam("fileName") String fileName);
	
	@GetMapping("/system/get")
	public Wrapper<Map<String, Object>> get();
	
}
