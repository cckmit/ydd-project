package com.ydw.oa.wkflow.business_main.form.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/api/form")
public class FormApiController {

	@Autowired
	private IFormService formService;

	@ApiOperation(value = "表单菜单管理")
	@GetMapping("/list")
	public Wrapper<List<Form>> list() {

		QueryWrapper<Form> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("IS_MENU", "是");
		queryWrapper.eq("IS_SET", "是");
		List<Form> list = formService.list(queryWrapper);

		return WrapMapper.ok(list);
	}

}
