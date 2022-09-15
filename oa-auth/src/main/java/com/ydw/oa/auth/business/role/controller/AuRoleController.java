package com.ydw.oa.auth.business.role.controller;

import java.util.List;

import javax.validation.Valid;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.role.dto.RoleDto;
import com.ydw.oa.auth.business.role.dto.RoleQuery;
import com.ydw.oa.auth.business.role.entity.AuRole;
import com.ydw.oa.auth.business.role.service.IAuRoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 角色管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "角色管理")
@RestController
@RequestMapping("/cp/role/au-role")
public class AuRoleController {

	@Autowired
	private IAuRoleService roleService;

	@ApiOperation(value = "角色管理")
	@PostMapping("/list")
	public Wrapper<IPage<AuRole>> list(@RequestBody RoleQuery<AuRole> roleQuery) {

		IPage<AuRole> ipage = roleService.page(roleQuery, roleQuery.makeQueryWrapper());

		return WrapMapper.ok(ipage);
	}

	@ApiOperation(value = "添加角色")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody RoleDto roleDto) {
		roleService.save(roleDto);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除角色")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		roleService.delete(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取角色数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<AuRole> editForm(String objectId) {
		AuRole opFunc = roleService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑角色")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody RoleDto roleDto) {
		roleService.update(roleDto);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "角色管理")
	@PostMapping("/select_list")
	public Wrapper<List<AuRole>> select_list() {

		List<AuRole> list = roleService.list();

		return WrapMapper.ok(list);
	}
}
