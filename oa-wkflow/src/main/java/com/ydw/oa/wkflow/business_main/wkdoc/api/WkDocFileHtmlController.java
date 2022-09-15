package com.ydw.oa.wkflow.business_main.wkdoc.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.wkdoc.dto.WkDocFileQuery;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFile;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileHtml;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileHtmlMapper;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileMapper;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileHtmlService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
@Api(description = "流程关联html数据表")
@RestController
@RequestMapping("/api/wk-doc-file-html")
public class WkDocFileHtmlController {

	@Autowired
	private IWkDocFileHtmlService wkDocFileHtmlService;

	@ApiOperation(value = "获取关联html数据")
	@GetMapping("/get")
	public Wrapper<WkDocFileHtml> get(String pid,String formId) {
		QueryWrapper<WkDocFileHtml> wkDocFileHtmlQueryWrapper = new QueryWrapper<>();
		wkDocFileHtmlQueryWrapper.eq("PROCESS_INSTANCE_ID", pid);
		wkDocFileHtmlQueryWrapper.eq("FORM_ID", formId);
		WkDocFileHtml wkDocFileHtml = wkDocFileHtmlService.getOne(wkDocFileHtmlQueryWrapper);
		return WrapMapper.ok(wkDocFileHtml);
	}
}
