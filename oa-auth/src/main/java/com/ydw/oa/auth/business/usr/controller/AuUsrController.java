package com.ydw.oa.auth.business.usr.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;

import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.usr.dto.CurrentUsrDto;
import com.ydw.oa.auth.business.usr.dto.UsrDto;
import com.ydw.oa.auth.business.usr.dto.UsrQuery;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrSign;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business.usr.service.IAuUsrSignService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 用户管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "用户管理")
@RestController
@RequestMapping("/cp/usr/au-usr")
public class AuUsrController {

	@Autowired
	private IAuUsrService usrService;
	@Autowired
	private IAuUsrSignService usrSignService;
	@Autowired
	private IAuUsrDeptService iAuUsrDeptService;
	@Autowired
	private IAuUsrRoleService iAuUsrRoleService;

	@ApiOperation(value = "用户管理")
	@PostMapping("/list")
	public Wrapper<IPage<AuUsr>> list(@RequestBody UsrQuery<AuUsr> usrQuery) {

		IPage<AuUsr> page2 = usrService.page(usrQuery, usrQuery.makeQueryWrapper());

		return WrapMapper.ok(page2);
	}

	@ApiOperation(value = "添加用户")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody UsrDto usrDto) {
		usrService.saveUser(usrDto);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除用户")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		usrService.delete(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "编辑获取用户数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<AuUsr> editForm(String objectId) {
		AuUsr opFunc = usrService.getById(objectId);
		QueryWrapper<AuUsrSign> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_ID", objectId);
		AuUsrSign sign = usrSignService.getOne(queryWrapper);
		if(ChkUtil.isNotNull(sign)) {
			opFunc.setSign(Arrays.asList(sign.getSign().split(",")));
		}
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑用户")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody UsrDto usrDto) {
		usrService.updateUser(usrDto);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "停用启用")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/change_status")
	public Wrapper<String> delete(String objectId,int status) {
		AuUsr opFunc = usrService.getById(objectId);
		opFunc.setStatus(status);
		usrService.saveOrUpdate(opFunc);
		if(status==0) {
			return WrapMapper.ok("账号启用成功");
		}else {
			return WrapMapper.ok("账号停用成功");
		}
	}
	
	@ApiOperation(value = "获取当前用户")
	@GetMapping("/current")
	public Wrapper<AuUsr> current() {
		AuUsr opFunc = usrService.getById(SessionTool.getSessionAdminId());
		QueryWrapper<AuUsrSign> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_ID", opFunc.getObjectId());
		AuUsrSign sign = usrSignService.getOne(queryWrapper);
		if(ChkUtil.isNotNull(sign)) {
			opFunc.setSign(Arrays.asList(sign.getSign().split(",")));
		}
		QueryWrapper<AuUsrDept> deptQueryWrapper = new QueryWrapper<>();
		deptQueryWrapper.eq("USR_ID", opFunc.getObjectId());
		AuUsrDept auUsrDept = iAuUsrDeptService.getOne(deptQueryWrapper);
		if(ChkUtil.isNotNull(auUsrDept)) {
			opFunc.setDeptId(auUsrDept.getDeptId());
		}
		QueryWrapper<AuUsrRole> roleQueryWrapper = new QueryWrapper<>();
		roleQueryWrapper.eq("USR_ID", opFunc.getObjectId()).select("ROLE_ID");
		List<AuUsrRole> list = iAuUsrRoleService.list(roleQueryWrapper);
		if (list.size() > 0) {
			List<String> roleIds = new ArrayList<String>();
			for (AuUsrRole auUsrRole : list) {
				roleIds.add(auUsrRole.getRoleId());
			}
			opFunc.setRoleIds(roleIds);
		}
		return WrapMapper.ok(opFunc);
	}
	
	@ApiOperation(value = "修改用户信息")
	@PostMapping("/edit_current")
	public Wrapper<String> edit_current(@ApiParam @Valid @RequestBody CurrentUsrDto usrDto) {
		usrService.updateUserInfo(usrDto);
		return WrapMapper.ok("保存成功");
	}
	
	@ApiOperation(value = "所有用户")
	@GetMapping("/usr_list")
	public Wrapper<List<AuUsr>> usr_list() {
		List<AuUsr> list = usrService.list();
		return WrapMapper.ok(list);
	}
}
