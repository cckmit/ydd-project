package com.ydw.oa.auth.business_wx;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.WxClockIn;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;

import feign.Headers;

import java.util.List;

@Component
@FeignClient(value = "${auth.wx}", path="/oa-wechat", url = "")
public interface WxFeignService {

	// 发送信息
	@PostMapping("/api/message/text")
	@Headers("Content-Type: application/json")
	String text(@RequestBody JSONObject body);

	// 发送卡片信息
	@PostMapping("/api/message/textcard")
	@Headers("Content-Type: application/json")
	public String textcard(@RequestBody JSONObject body);
	// 发送卡片信息
	@PostMapping("/api/message/textcard_notice")
	@Headers("Content-Type: application/json")
	public String textcard_notice(@RequestBody JSONObject body);

	// 获取所有用户当天的考勤数据
	@RequestMapping(value = "/api/oa/clockin/{yearMonth}/{day}",method = RequestMethod.GET)
	Wrapper<List<WxClockIn>> getClockinsByYearMonthAndDay(@PathVariable String yearMonth, @PathVariable String day);

	// 获取考勤数据
	@RequestMapping(value = "/api/oa/clockin/list",method = RequestMethod.POST)
	Wrapper<Page<WxClockIn>> getClockinDetail(@RequestParam String userid, @RequestParam String yearMonth, @RequestBody Page<WxClockIn> page);

}
