package com.ydw.oa.auth.business.attendance.controller;


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
import com.ydw.oa.auth.business.attendance.dto.PersAttendanceRuleQuery;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceRule;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceRuleService;

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
 * @since 2020-10-18
 */
@Api(description = "考勤规则")
@RestController
@RequestMapping("/cp/pers-attendance-rule")
public class PersAttendanceRuleController {

	@Autowired
	private IPersAttendanceRuleService persAttendanceRuleService;

	@ApiOperation(value = "考勤规则管理")
	@PostMapping("/list")
	public Wrapper<IPage<PersAttendanceRule>> list(@RequestBody PersAttendanceRuleQuery<PersAttendanceRule> query) {

		IPage<PersAttendanceRule> page = persAttendanceRuleService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加考勤规则")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody PersAttendanceRule persAttendanceRule) {
		persAttendanceRuleService.save(persAttendanceRule);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除考勤规则")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		persAttendanceRuleService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取考勤规则数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<PersAttendanceRule> editForm(String objectId) {
		PersAttendanceRule persAttendanceRulePo = persAttendanceRuleService.getById(objectId);
		return WrapMapper.ok(persAttendanceRulePo);
	}

	@ApiOperation(value = "编辑考勤规则")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody PersAttendanceRule persAttendanceRuleDto) {
		PersAttendanceRule persAttendanceRulePo = persAttendanceRuleService.getById(persAttendanceRuleDto.getObjectId());
		persAttendanceRulePo.setName(persAttendanceRuleDto.getName());
		persAttendanceRulePo.setType(persAttendanceRuleDto.getType());
		persAttendanceRulePo.setNote(persAttendanceRuleDto.getNote());
		persAttendanceRulePo.setScore(persAttendanceRuleDto.getScore());
		persAttendanceRulePo.setSign(persAttendanceRuleDto.getSign());
		persAttendanceRuleService.saveOrUpdate(persAttendanceRulePo);
		return WrapMapper.ok("保存成功");
	}
	
}

