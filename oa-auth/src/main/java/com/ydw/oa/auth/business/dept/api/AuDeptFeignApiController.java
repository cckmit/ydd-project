package com.ydw.oa.auth.business.dept.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "部门管理Api")
@RestController
@RequestMapping("/api/depart")
public class AuDeptFeignApiController {

	@Autowired
	private IAuDeptService deptService;

	@ApiOperation(value = "部长/部门负责人")
	@GetMapping("/departManager")
	public Wrapper<String> getDepartManager(String deptId) {
		AuDept dept = deptService.getById(deptId);
		return WrapMapper.ok(dept.getDeptManaUsrId());
	}

	@ApiOperation(value = "部门分管领导")
	@GetMapping("/departLeader")
	public Wrapper<String> getDepartLeader(String deptId) {
		AuDept dept = deptService.getById(deptId);
		return WrapMapper.ok(dept.getUsrId());
	}
	
	@ApiOperation(value = "部门负责人")
	@GetMapping("/getDepartManagerByDeptName")
	public Wrapper<String> getDepartManagerByDeptName(String deptName) {
		QueryWrapper<AuDept> qw = new QueryWrapper<AuDept>();
		qw.eq("NAME", deptName);
		AuDept dept = deptService.getOne(qw);
		return WrapMapper.ok(dept.getDeptManaUsrId());
	}
	
	@ApiOperation(value = "部门分管领导")
	@GetMapping("/getDepartLeaderByDeptName")
	public Wrapper<String> getDepartLeaderByDeptName(String deptName) {
		QueryWrapper<AuDept> qw = new QueryWrapper<AuDept>();
		qw.eq("NAME", deptName);
		AuDept dept = deptService.getOne(qw);
		return WrapMapper.ok(dept.getUsrId());
	}
}
