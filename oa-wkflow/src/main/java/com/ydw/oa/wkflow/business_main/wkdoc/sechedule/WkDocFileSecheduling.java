package com.ydw.oa.wkflow.business_main.wkdoc.sechedule;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileService;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Slf4j
@RestController
@Component
public class WkDocFileSecheduling {

	@Autowired
	private IWkDocFileService iWkDocFileService;

	// 定时查询（2分钟）流程文档记录, 生成并上传pdf到文件
	@Scheduled(cron = "0 */2 * * * ?")
	@RequestMapping("/sync/wk_doc_file/schedule")
	public void calcScore() throws IOException {
		iWkDocFileService.createPdfAndUpdateWkDocFile();
		log.info("---> search wk_doc_file per 2 mins, then create pdf");
	}

}
