package com.ydw.oa.auth.business.wk.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.wk.dto.WkQuery;
import com.ydw.oa.auth.business.wk.entity.WkResult;
import com.ydw.oa.auth.business.wk.service.IWkResultService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-28
 */
@Api(description = "工作总结管理")
@RestController
@RequestMapping("/cp/wk-result")
public class WkResultController {

	@Autowired
	private IWkResultService resultService;
	
	@ApiOperation(value = "工作总结管理")
	@PostMapping("/list")
	public Wrapper<IPage<WkResult>> list(@RequestBody WkQuery<WkResult> query) {

		IPage<WkResult> page = resultService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加工作总结")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody WkResult wkResult) {
		wkResult.setUsrId(SessionTool.getSessionAdminId());
		resultService.save(wkResult);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除工作总结")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		resultService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "编辑获取工作总结数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<WkResult> editForm(String objectId) {
		WkResult wkResultPo = resultService.getById(objectId);
		return WrapMapper.ok(wkResultPo);
	}

	@ApiOperation(value = "编辑工作总结")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody WkResult wkResultDto) {
		WkResult wkResultPo = resultService.getById(wkResultDto.getObjectId());
		wkResultPo.setContent(wkResultDto.getContent());
		wkResultPo.setType(wkResultDto.getType());
		resultService.saveOrUpdate(wkResultPo);
		return WrapMapper.ok("保存成功");
	}
}

