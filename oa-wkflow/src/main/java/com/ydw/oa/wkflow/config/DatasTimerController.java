package com.ydw.oa.wkflow.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;

import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;

/**
 * <p>
 * 定时判断流程执行情况，超过24小时，删除该流程
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@RestController
public class DatasTimerController {

	@Autowired
	private IDatasService datasService;

	@Scheduled(cron = "0 0 0 * * ?")
	public void list() {
		// TODO 删除超时的流程
		datasService.deleteOverTimeTasks();
	}
}
