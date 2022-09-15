package com.ydw.oa.wkflow.business_main.form.controller;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.form.dto.FormAccessoryQuery;
import com.ydw.oa.wkflow.business_main.form.entity.FormAccessory;
import com.ydw.oa.wkflow.business_main.form.service.IFormAccessoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-13
 */
@Api(description = "表单模板管理")
@RestController
@RequestMapping("/file/form-accessory")
public class FormAccessoryController {

	@Autowired
	private IFormAccessoryService formAccessoryService;

	@ApiOperation(value = "表单模板列表")
	@PostMapping("/list")
	public Wrapper<List<FormAccessory>> list(@RequestBody FormAccessoryQuery<FormAccessory> formAccessoryQuery) {
		
		List<FormAccessory> list = formAccessoryService.list(formAccessoryQuery.makeQueryWrapper());

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "添加表单模板")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody FormAccessory formAccessory) {
		formAccessoryService.save(formAccessory);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除表单模板")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		formAccessoryService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取表单模板数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<FormAccessory> editForm(String objectId) {
		FormAccessory opFunc = formAccessoryService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑表单模板")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody FormAccessory formAccessory) {
		FormAccessory formAccessoryDb = formAccessoryService.getById(formAccessory.getObjectId());
		formAccessoryDb.setFileId(formAccessory.getFileId());
		formAccessoryDb.setFileName(formAccessory.getFileName());
		formAccessoryDb.setIsRequired(formAccessory.getIsRequired());
		formAccessoryDb.setSortz(formAccessory.getSortz());
		formAccessoryService.saveOrUpdate(formAccessoryDb);

		return WrapMapper.ok("保存成功");
	}
}

