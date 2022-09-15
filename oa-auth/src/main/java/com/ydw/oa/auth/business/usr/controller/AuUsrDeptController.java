package com.ydw.oa.auth.business.usr.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 用户下的部门管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-14
 */
@Api(description = "用户下的部门管理")
@RestController
@RequestMapping("/cp/usr/au-usr-dept")
public class AuUsrDeptController {

	@Autowired
	private IAuUsrDeptService usrDeptService;

	@Autowired
	private AuUsrDeptMapper auUsrDeptMapper;
	
	@ApiOperation(value = "用户下的部门管理")
	@ApiImplicitParam(name = "usrId", value = "用户id")
	@GetMapping("/list")
	public Wrapper<List<AuUsrDept>> list(String usrId) {

		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("USR_ID", usrId);
		}
		List<AuUsrDept> list = usrDeptService.list(queryWrapper);

		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "用户部门名称列表")
	@ApiImplicitParam(name = "usrId", value = "用户id")
	@GetMapping("/deptlist")
	public Wrapper<List<Map<String,Object>>> deptlist(String usrId) {

		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("a.USR_ID", usrId);
		}
		List<Map<String,Object>> list = auUsrDeptMapper.deptlist(queryWrapper);

		return WrapMapper.ok(list);
	}
}
