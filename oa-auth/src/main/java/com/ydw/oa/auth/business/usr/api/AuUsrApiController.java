package com.ydw.oa.auth.business.usr.api;

import java.util.List;
import java.util.Map;

import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.usr.dto.UsrDto;
import com.ydw.oa.auth.business.usr.dto.UsrQuery;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.mapper.AuUsrMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/cp/api")
public class AuUsrApiController {

	@Autowired
	private IAuUsrService usrService;
	@Autowired
	private AuUsrMapper auUsrMapper;
	@Autowired
	private AuUsrDeptMapper usrDeptMapper;


	@ApiOperation(value = "用户管理")
	@PostMapping("/list")
	public Wrapper<List<AuUsr>> list(@RequestBody(required = false) UsrQuery<AuUsr> usrQuery) {
		QueryWrapper<AuUsr> qw = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrQuery)) {
			qw = (QueryWrapper<AuUsr>) usrQuery.makeQueryWrapper();
		}
		List<AuUsr> page2 = usrService.list(qw);

		return WrapMapper.ok(page2);
	}

	@ApiOperation(value = "获取session用户数据")
	@GetMapping("/getUserInfo")
	public Wrapper<AuUsr> getUserInfo() {
		String object_id = SessionTool.getSessionAdminId();
		AuUsr opFunc = usrService.getById(object_id);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "更新用户信息")
	@ApiImplicitParam(name = "userDto", value = "用户对象")
	@PostMapping("/updateUserInfo")
	public Wrapper<String> updateUserInfo(@RequestBody UsrDto userDto) {
		AuUsr usrPo = usrService.getById(userDto.getObjectId());
		usrPo.setRealName(userDto.getRealName());
		usrPo.setEmail(userDto.getEmail());
		usrPo.setNote(userDto.getNote());
		usrService.saveOrUpdate(usrPo);
		return WrapMapper.ok("修改资料成功");
	}

	@ApiOperation(value = "表单使用用户列表 label:用户名称 value:用户id")
	@GetMapping("/usr_list")
	public List<Map<String, Object>> usr_list() {
		List<Map<String, Object>> list = auUsrMapper.userList();
		return list;
	}
	
	@ApiOperation(value = "部门用户列表")
	@GetMapping("/dept_usr_list")
	public List<Map<String, Object>> dept_usr_list() {
		String id = SessionTool.getSessionAdminId();
		List<Map<String, Object>> list = auUsrMapper.deptUserList(id);
		return list;
	}

	@ApiOperation(value = "获取集合人员列表")
	@GetMapping("/names")
	public Wrapper<String> names(String usrIds) {
		String names = auUsrMapper.getReceiveUsrNames(usrIds);
		return WrapMapper.ok(names);
	}
	
	@ApiOperation(value = "司机列表")
	@GetMapping("/driver_list")
	public List<Map<String, Object>> driver_list() {
		List<Map<String, Object>> list = auUsrMapper.driverList();
		return list;
	}


}
