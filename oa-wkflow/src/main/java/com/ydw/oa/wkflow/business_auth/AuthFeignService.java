package com.ydw.oa.wkflow.business_auth;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSONObject;
import com.tmsps.fk.common.wrapper.Wrapper;

@Component
@FeignClient(value = "${wkfow.auth}", path = "/oa-auth", url = "http://localhost:18201")
public interface AuthFeignService {

	// 部长/部门负责人
	@GetMapping("/api/usr/departManager")
	public Wrapper<List<String>> getDepartManager(@RequestParam("currentUsrId") String currentUsrId);

	// 角色下人员
	@GetMapping("/api/usr/roleUsers")
	public Wrapper<List<String>> getRoleUsers(@RequestParam("role") String role);
	
	// 部门下人员
	@GetMapping("/api/usr/deptUsers")
	public Wrapper<List<String>> getDeptUsers(@RequestParam("dept") String dept);

	// 部门分管领导
	@GetMapping("/api/usr/departLeader")
	public Wrapper<List<String>> getDepartLeader(@RequestParam("currentUsrId") String currentUsrId);
	
	// 获取用户微信id
	@GetMapping("/api/usr/getWxUsrIds")
	public Wrapper<List<String>> getWxUsrIds(@RequestParam("userIds") String userIds);
	
	// 获取用户对象
	@GetMapping("/api/usr/getOne")
	public Wrapper<Map<String, Object>> getOne(@RequestParam("userId") String userId);
	
	@PostMapping("/api/car_use/save")
	public Wrapper<String> save(@RequestBody JSONObject carInfo);

	// 保存接待记录
	@PostMapping("/api/reception/save")
	public void saveReceptionRecords(@RequestBody JSONObject result);
	
	// 所选接待记录,更改状态
	@GetMapping("/api/reception/choseRecord")
	public Wrapper<String> choseRecord(@RequestParam("pids") String pids);

	// 接待会签结束，变更状态
	@GetMapping("/api/reception/endRecordSign")
	public Wrapper<String> endRecordSign(@RequestParam("pids") String pids, @RequestParam("isReject") boolean isReject);
	
	// 部长/部门负责人
	@GetMapping("/api/depart/departManager")
	public Wrapper<String> getDepartManagerById(@RequestParam("deptId") String deptId);

	// 部门分管领导
	@GetMapping("/api/depart/departLeader")
	public Wrapper<String> getDepartLeaderById(@RequestParam("deptId") String deptId);
	
	// 获取用户对象
	@GetMapping("/api/usr/roles")
	public Wrapper<List<String>> getRoles(@RequestParam("userId") String userId);
	
	// 获取人员部门
	@GetMapping("/api/usr/depts")
	public Wrapper<List<String>> getDepts(@RequestParam("userId") String userId);

	// 通过部门名称获取部门负责人
	@GetMapping("/api/depart/getDepartManagerByDeptName")
	public Wrapper<String> getDepartManagerByDeptName(@RequestParam("deptName") String deptName);
	
	// 通过部门名称获取部门分管领导
	@GetMapping("/api/depart/getDepartLeaderByDeptName")
	public Wrapper<String> getDepartLeaderByDeptName(@RequestParam("deptName") String deptName);

	// 获取所有部门负责人
	@GetMapping("/api/depart/formDeptList")
	public List<Map<String, Object>> getDeptManagers();

}
