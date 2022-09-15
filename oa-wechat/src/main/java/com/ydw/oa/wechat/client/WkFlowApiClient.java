package com.ydw.oa.wechat.client;

import com.tmsps.fk.common.wrapper.Wrapper;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "${wkfow.wkflow}",path = "/oa-wkflow"/*, url = "${token.url}"*/)
public interface WkFlowApiClient {

    // 根据taskId获取工作流信息
    @GetMapping("/api/task/data")
    Wrapper<Map<String,Object>> getWkFlowInfo(@RequestParam("task_id") String taskId, @RequestParam("pid") String pid);

}
