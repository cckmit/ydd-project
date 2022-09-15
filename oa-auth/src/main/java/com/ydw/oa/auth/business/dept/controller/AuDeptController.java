package com.ydw.oa.auth.business.dept.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

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
import com.ydw.oa.auth.business.dept.dto.DeptQuery;
import com.ydw.oa.auth.business.dept.dto.UserQuery;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.util.tree.TreeDeptTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 部门管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "部门管理")
@RestController
@RequestMapping("/cp/dept/au-dept")
public class AuDeptController {

	@Autowired
	private IAuDeptService deptService;

	@Autowired
	private AuUsrDeptMapper auUsrDeptMapper;

	@Autowired
	private IAuUsrDeptService iAuUsrDeptService;

	@ApiOperation(value = "部门Tree")
	@GetMapping("/tree")
	public Wrapper<List<AuDept>> tree() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<AuDept> list = deptService.list(queryWrapper);
		List<AuDept> tree = TreeDeptTools.listToTree(list);

		return WrapMapper.ok(tree);
	}

	@ApiOperation(value = "部门列表")
	@PostMapping("/list")
	public Wrapper<IPage<AuDept>> list(@RequestBody DeptQuery<AuDept> deptQuery) {
		IPage<AuDept> ipage = deptService.page(deptQuery, deptQuery.makeQueryWrapper());

		return WrapMapper.ok(ipage);
	}

	@ApiOperation(value = "添加部门")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody AuDept deptDto) {
		deptService.save(deptDto);

		if (ChkUtil.isNotNull(deptDto.getUsrId())) {
			iAuUsrDeptService.saveUsrDepts(deptDto.getUsrId(), deptDto.getObjectId());
		}

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除部门")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		deptService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取部门数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<AuDept> editForm(String objectId) {
		AuDept opFunc = deptService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑部门")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody AuDept deptDto) {
		AuDept deptPo = deptService.getById(deptDto.getObjectId());
		deptPo.setDeptId(deptDto.getDeptId());
		deptPo.setName(deptDto.getName());
		deptPo.setDescription(deptDto.getDescription());
		deptPo.setVariable(deptDto.getVariable());
		deptPo.setUsrId(deptDto.getUsrId());
		deptPo.setDeptManaUsrId(deptDto.getDeptManaUsrId());
		deptPo.setXzjc(deptDto.getXzjc());
		deptPo.setShowOrders(deptDto.getShowOrders());
		deptPo.setDwjc(deptDto.getDwjc());
		deptService.saveOrUpdate(deptPo);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "部门下用户列表")
	@PostMapping("/user_list")
	public Wrapper<List<Map<String, Object>>> user_list(@RequestBody UserQuery<AuUsrDept> usrQuery) {
		List<Map<String, Object>> list = auUsrDeptMapper.query(usrQuery.makeQueryWrapper());

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "没有部门的用户列表")
	@PostMapping("/all_user_list")
	public Wrapper<List<Map<String, Object>>> all_user_list() {
		List<Map<String, Object>> list = auUsrDeptMapper.queryAllNotDept();

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "用户列表")
	@PostMapping("/deptuserlist")
	public Wrapper<IPage<Map<String, Object>>> deptuserlist(@RequestBody UserQuery<AuUsrDept> usrQuery) {

		IPage<Map<String, Object>> list = auUsrDeptMapper.query(usrQuery, usrQuery.makeQueryWrapper());

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "部门")
	@GetMapping("/dept_list")
	public List<Map<String, Object>> list() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
//		queryWrapper.select("OBJECT_ID value","NAME label");
		queryWrapper.select("NAME value","NAME label");
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		return list;
	}
}
