package com.ydw.oa.auth.business.usr.controller;

import java.util.List;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;

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
 * 用户下的角色管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "用户下的角色管理")
@RestController
@RequestMapping("/cp/usr/au-usr-role")
public class AuUsrRoleController {

	@Autowired
	private IAuUsrRoleService usrRoleService;

	@ApiOperation(value = "用户下的部门管理")
	@ApiImplicitParam(name = "usrId", value = "用户id")
	@GetMapping("/list")
	public Wrapper<List<AuUsrRole>> list(String usrId) {

		QueryWrapper<AuUsrRole> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("USR_ID", usrId);
		}
		List<AuUsrRole> list = usrRoleService.list(queryWrapper);

		return WrapMapper.ok(list);
	}
}
