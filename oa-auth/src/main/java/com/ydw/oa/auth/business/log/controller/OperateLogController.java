package com.ydw.oa.auth.business.log.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.log.entity.OperateLog;
import com.ydw.oa.auth.business.log.service.IOperateLogService;
import com.ydw.oa.auth.business.log.dto.OperateLogQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "日志管理")
@RestController
@RequestMapping("/cp/operate-log")
public class OperateLogController {

	@Autowired
	private IOperateLogService operateLogService;

	@ApiOperation(value = "链接列表")
	@PostMapping("/list")
	public Wrapper<IPage<OperateLog>> list(@RequestBody OperateLogQuery<OperateLog> query) {
		IPage<OperateLog> page = operateLogService.page(query, query.makeQueryWrapper());
		return WrapMapper.ok(page);
	}
}

