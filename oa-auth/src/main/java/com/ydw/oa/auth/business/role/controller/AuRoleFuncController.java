package com.ydw.oa.auth.business.role.controller;

import java.util.List;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.role.entity.AuRoleFunc;
import com.ydw.oa.auth.business.role.service.IAuRoleFuncService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 角色下的菜单管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "角色下的菜单管理")
@RestController
@RequestMapping("/cp/role/au-role-func")
public class AuRoleFuncController {

	@Autowired
	private IAuRoleFuncService roleFuncService;

	@ApiOperation(value = "角色下的菜单管理")
	@ApiImplicitParam(name = "roleId", value = "角色id")
	@GetMapping("/list")
	public Wrapper<List<AuRoleFunc>> list(String roleId) {

		QueryWrapper<AuRoleFunc> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(roleId)) {
			queryWrapper.eq("ROLE_ID", roleId);
		}
		List<AuRoleFunc> list = roleFuncService.list(queryWrapper);

		return WrapMapper.ok(list);
	}

}
