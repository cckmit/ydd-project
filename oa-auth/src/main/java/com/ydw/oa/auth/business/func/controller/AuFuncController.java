package com.ydw.oa.auth.business.func.controller;

import java.util.Date;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.func.dto.FuncQuery;
import com.ydw.oa.auth.business.func.entity.AuFunc;
import com.ydw.oa.auth.business.func.service.IAuFuncService;
import com.ydw.oa.auth.util.tree.TreeFuncTools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 系统功能
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "系统功能")
@RestController
@RequestMapping("/cp/func/au-func")
public class AuFuncController {

	@Autowired
	private IAuFuncService funcService;

	@ApiOperation(value = "系统功能管理Tree")
	@GetMapping("/tree")
	public Wrapper<List<AuFunc>> tree() {
		QueryWrapper<AuFunc> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "SHOW_ORDERS");
		List<AuFunc> list = funcService.list(queryWrapper);
		List<AuFunc> tree = TreeFuncTools.listToTree(list);

		return WrapMapper.ok(tree);
	}

	@ApiOperation(value = "系统功能管理")
	@PostMapping("/list")
	public Wrapper<IPage<AuFunc>> list(@RequestBody FuncQuery<AuFunc> funcQuery) {
		IPage<AuFunc> ipage = funcService.page(funcQuery, funcQuery.makeQueryWrapper());

		return WrapMapper.ok(ipage);
	}

	@ApiOperation(value = "添加系统功能")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody AuFunc funcDto) {
		funcService.save(funcDto);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除 系统功能")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		funcService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "批量删除 系统功能")
	@ApiImplicitParam(name = "objectIds", value = "主键ids")
	@GetMapping("/delete_ids")
	public Wrapper<String> delete_ids(String objectIds) {
		String[] ids = objectIds.split(",");
		boolean b = funcService.removeByIds(Arrays.asList(ids));
		return WrapMapper.ok(b ? "删除成功" : "删除失败");
	}

	@ApiOperation(value = "编辑获取系统功能数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<AuFunc> editForm(String objectId) {
		AuFunc opFuncPo = funcService.getById(objectId);
		return WrapMapper.ok(opFuncPo);
	}

	@ApiOperation(value = "编辑系统功能")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody AuFunc funcDto) {
		AuFunc funcPo = funcService.getById(funcDto.getObjectId());
		funcPo.setFuncName(funcDto.getFuncName());
		funcPo.setDescription(funcDto.getDescription());
		funcPo.setFuncType(funcDto.getFuncType());
		funcPo.setFuncUrls(funcDto.getFuncUrls());
		funcPo.setFuncIcon(funcDto.getFuncIcon());
		funcPo.setShowOrders(funcDto.getShowOrders());
		funcPo.setStatus(funcDto.getStatus());
		funcPo.setVariable(funcDto.getVariable());
		funcPo.setKeepAlive(funcDto.isKeepAlive());
		funcPo.setLastUpdateTime(new Date(System.currentTimeMillis()));
		funcService.saveOrUpdate(funcPo);

		return WrapMapper.ok("保存成功");
	}

}
