package com.ydw.oa.wkflow.business_main.form.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.form.dto.FormQuery;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.mapper.FormMapper;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "表单管理")
@RestController
@RequestMapping("/form/form")
public class FormController {

	@Autowired
	private IFormService formService;
	@Autowired
	private FormMapper formMapper;

	@ApiOperation(value = "表单管理")
	@PostMapping("/list")
	public Wrapper<Map<String, Object>> list(@RequestBody FormQuery<Form> formQuery) {

		List<Form> list = formMapper.selectListPage(formQuery, formQuery.makeQueryWrapper());

		Map<String, Object> map = new HashMap<>();
		map.put("records", list);
		map.put("current", formQuery.getCurrent());
		map.put("size", formQuery.getSize());
		map.put("total", formQuery.getTotal());
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "添加表单")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody Form form) {
		formService.saveForm(form);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除表单")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		formService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取表单数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<Form> editForm(String objectId) {
		Form form = formService.getById(objectId);
		return WrapMapper.ok(form);
	}

	@ApiOperation(value = "编辑表单")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody Form form) {
		formService.updateForm(form);
		return WrapMapper.ok("保存成功");
	}
	
	@ApiOperation(value = "更改表单模式")
	@ApiImplicitParams({ @ApiImplicitParam(name = "objectId", value = "主键id"),
		@ApiImplicitParam(name = "runType", value = "表单模式: html or json") })
	@GetMapping("/change_runType")
	public Wrapper<String> change_runType(String objectId, String runType) {
		Form formDb = formService.getById(objectId);
		formDb.setRunType(runType);
		formService.saveOrUpdate(formDb);
		return WrapMapper.ok("模式操作成功");
	}
	
	@ApiOperation(value = "更改表单类型")
	@ApiImplicitParams({ @ApiImplicitParam(name = "objectId", value = "主键id"),
		@ApiImplicitParam(name = "runType", value = "表单类型: form or table") })
	@GetMapping("/change_formType")
	public Wrapper<String> change_formType(String objectId, String formType) {
		Form formDb = formService.getById(objectId);
		formDb.setFormType(formType);
		formService.saveOrUpdate(formDb);
		return WrapMapper.ok("类型操作成功");
	}
}
