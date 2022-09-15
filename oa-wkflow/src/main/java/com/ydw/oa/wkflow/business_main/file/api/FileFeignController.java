package com.ydw.oa.wkflow.business_main.file.api;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.file.entity.File;
import com.ydw.oa.wkflow.business_main.file.service.IFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "文件feign接口")
@RestController
@RequestMapping("/api/file")
public class FileFeignController {

	@Autowired
	private IFileService fileService;

	@ApiOperation(value = "获取对象")
	@GetMapping("/get")
	public Wrapper<Map<String, Object>> get(String fileId) {
		File file = fileService.getById(fileId);
		Map<String, Object> map = JsonUtil.objToMap(file);
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "重命名")
	@GetMapping("/rename")
	public Wrapper<String> rename(String fileId, String fileName) {
		File file = fileService.getById(fileId);
		file.setFileName(fileName);
		fileService.saveOrUpdate(file);
		return WrapMapper.ok();
	}
	
}
