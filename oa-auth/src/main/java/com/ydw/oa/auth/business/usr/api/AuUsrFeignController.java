package com.ydw.oa.auth.business.usr.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.ydw.oa.auth.business.usr.entity.AuUsrSign;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;
import com.ydw.oa.auth.business.usr.service.IAuUsrSignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.business.usr.mapper.AuUsrMapper;
import com.ydw.oa.auth.business.usr.mapper.AuUsrRoleMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(description = "用户管理feign接口")
@RestController
@RequestMapping("/api/usr")
public class AuUsrFeignController {

	@Autowired
	private AuUsrRoleMapper usrRoleMapper;
	@Autowired
	private AuUsrDeptMapper usrDeptMapper;
	@Autowired
	private IAuUsrService auUsrService;
	@Autowired
	private AuUsrMapper auUsrMapper;
	@Autowired
	private IAuUsrSignService usrSignService;
	@Autowired
	private IAuUsrRoleService iAuUsrRoleService;

	
	@ApiOperation(value = "部门人员列表")
	@GetMapping("/user_list")
	public Wrapper<List<Map<String, Object>>> user_list(String depart_id) {
		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(depart_id)) {
			queryWrapper.eq("a.DEPT_ID", depart_id);
		}
		List<Map<String, Object>> list = usrDeptMapper.query(queryWrapper);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "部长/部门负责人")
	@GetMapping("/departManager")
	public Wrapper<List<String>> getDepartManager(String currentUsrId) {
		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(currentUsrId)) {
			queryWrapper.eq("a.USR_ID", currentUsrId);
		}
		List<String> list = usrDeptMapper.selectDeptManaInfoList(queryWrapper);
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "部门分管领导")
	@GetMapping("/departLeader")
	public Wrapper<List<String>> getDepartLeader(String currentUsrId) {
		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(currentUsrId)) {
			queryWrapper.eq("a.USR_ID", currentUsrId);
		}
		List<String> list = usrDeptMapper.selectDeptInfoList(queryWrapper);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "角色下人员")
	@GetMapping("/roleUsers")
	public Wrapper<List<String>> getRoleUsers(String role){
		QueryWrapper<AuUsrRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("NAME", role);
		List<String> list = usrRoleMapper.selectRoleUserInfoList(queryWrapper);
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "部门下人员")
	@GetMapping("/deptUsers")
	public Wrapper<List<String>> getDeptUsers(String dept){
		List<String> list = usrDeptMapper.selectDeptUsrList(dept);
		return WrapMapper.ok(list);
	}


	
	@ApiOperation(value = "获取人员信息")
	@GetMapping("/getUsr")
	public Wrapper<Map<String, Object>> getUsr(String wxUserId){
		QueryWrapper<AuUsr> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("WX_USER_ID", wxUserId);
		Map<String, Object> map = auUsrService.getMap(queryWrapper);
		return WrapMapper.ok(map);
	}
	
	@ApiOperation(value = "获取用户微信id")
	@GetMapping("/getWxUsrIds")
	public Wrapper<List<String>> getWxUsrIds(String userIds){
		List<String> list = auUsrMapper.getWxUsrIds(userIds);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "获取所有用户")
	@GetMapping("/listAll")
	public Wrapper<List<Map<String,Object>>> getListAll(){
		List<Map<String,Object>> list = auUsrService.listMaps();
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "获取用户")
	@GetMapping("/getOne")
	public Wrapper<Map<String, Object>> getOne(String userId){
		QueryWrapper<AuUsr> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("OBJECT_ID", userId);
		Map<String, Object> map = auUsrService.getMap(queryWrapper);
		QueryWrapper<AuUsrDept> qw = new QueryWrapper<>();
		if (ChkUtil.isNotNull(userId)) {
			qw.eq("a.USR_ID", userId);
		}
		List<Map<String,Object>> list = usrDeptMapper.deptlist(qw);
		if(list.size()>0) {
			map.put("dept",list.get(0).get("NAME"));
			map.put("deptId", list.get(0).get("OBJECT_ID"));
		}
		QueryWrapper<AuUsrSign> queryWrapperSign = new QueryWrapper<>();
		queryWrapperSign.eq("USR_ID", userId);
		AuUsrSign sign = usrSignService.getOne(queryWrapperSign);
		if(ChkUtil.isNotNull(sign)) {
			map.put("sign",Arrays.asList(sign.getSign().split(",")));
		}

		QueryWrapper<AuUsrRole> roleQueryWrapper = new QueryWrapper<>();
		roleQueryWrapper.eq("USR_ID", userId).select("ROLE_ID");
		List<AuUsrRole> roleList = iAuUsrRoleService.list(roleQueryWrapper);
		if (roleList.size() > 0) {
			List<String> roleIds = new ArrayList<>();
			for (AuUsrRole auUsrRole : roleList) {
				roleIds.add(auUsrRole.getRoleId());
			}
			map.put("roleIds",roleIds);
		}
		return WrapMapper.ok(map);
	}
	
	@ApiOperation(value = "获取人员角色")
	@GetMapping("/roles")
	public Wrapper<List<String>> getRoles(String userId){
		List<String> list = usrRoleMapper.roleList(userId);
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "获取人员部门")
	@GetMapping("/depts")
	public Wrapper<List<String>> getDepts(String userId){
		List<String> list = usrDeptMapper.deptList(userId);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "获取部门人员列表-远端数据")
	@GetMapping("/dept_users")
	public List<Map<String, Object>> getDeptUsersById(String deptId){
		return usrDeptMapper.selectDeptUsrListByDeptId(deptId);
	}

	@ApiOperation(value = "根据excel用户导入")
	@GetMapping("/export_excel")
	public Wrapper<String> export_excel() {
		auUsrService.export();
		return WrapMapper.ok();
	}

	@ApiOperation(value = "司机列表")
	@GetMapping("/driver_list")
	public List<Map<String, Object>> driver_list() {
		List<Map<String, Object>> list = auUsrMapper.driverList();
		return list;
	}
}
