package com.ydw.oa.auth.business.fk.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.fk.service.CheckUniqueService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 唯一性校验
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
@Api(description = "唯一性校验")
@RestController
@RequestMapping("/cp")
public class CheckUniqueAction extends BaseAction {

	@Autowired
	private CheckUniqueService checkUniqueService;

	@ApiOperation(value = "唯一性校验")
	@ApiImplicitParams({ @ApiImplicitParam(name = "table", value = "表面"),
		@ApiImplicitParam(name = "field", value = "字段"),
		@ApiImplicitParam(name = "value", value = "值") })
	@GetMapping("/check_unique")
	public Wrapper<Boolean> check_unique(String table, String field, String value) throws Exception {
		System.out.println(table+field+value);
		boolean b = checkUniqueService.selectTableFindValue(table, field, value);
		return WrapMapper.ok(b);
	}

	@ApiOperation(value = "唯一性校验")
	@ApiImplicitParams({ @ApiImplicitParam(name = "table", value = "表面"),
		@ApiImplicitParam(name = "field", value = "字段"),
		@ApiImplicitParam(name = "value", value = "值"),
		@ApiImplicitParam(name = "object_id", value = "主键id") })
	@GetMapping("/check_unique_notme")
	public Wrapper<Boolean> check_unique_notme(String table, String field, String value, String object_id) throws Exception {
		boolean b = checkUniqueService.selectTableFindValueNotme(table, field, value, object_id);
		return WrapMapper.ok(b);
	}

}
