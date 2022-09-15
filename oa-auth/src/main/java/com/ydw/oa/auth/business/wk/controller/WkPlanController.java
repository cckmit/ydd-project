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
import com.ydw.oa.auth.business.wk.entity.WkPlan;
import com.ydw.oa.auth.business.wk.service.IWkPlanService;
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
@Api(description = "工作计划管理")
@RestController
@RequestMapping("/cp/wk-plan")
public class WkPlanController {

	@Autowired
	private IWkPlanService planService;
	
	@ApiOperation(value = "工作计划管理")
	@PostMapping("/list")
	public Wrapper<IPage<WkPlan>> list(@RequestBody WkQuery<WkPlan> query) {

		IPage<WkPlan> page = planService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加工作计划")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody WkPlan wkPlan) {
		wkPlan.setUsrId(SessionTool.getSessionAdminId());
		planService.save(wkPlan);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除工作计划")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		planService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "编辑获取工作计划数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<WkPlan> editForm(String objectId) {
		WkPlan wkPlanPo = planService.getById(objectId);
		return WrapMapper.ok(wkPlanPo);
	}

	@ApiOperation(value = "编辑工作计划")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody WkPlan wkPlanDto) {
		WkPlan wkPlanPo = planService.getById(wkPlanDto.getObjectId());
		wkPlanPo.setContent(wkPlanDto.getContent());
		wkPlanPo.setType(wkPlanDto.getType());
		planService.saveOrUpdate(wkPlanPo);
		return WrapMapper.ok("保存成功");
	}

}

