package com.ydw.oa.auth.business.dept.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.util.tree.TreeDeptTools;

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
public class AuDeptApiController {

	@Autowired
	private IAuDeptService deptService;
	@Autowired
	private AuUsrDeptMapper auUsrDeptMapper;

	@ApiOperation(value = "部门Tree")
	@GetMapping("/list_tree")
	public Wrapper<List<Map<String, Object>>> tree() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		List<Map<String, Object>> tree = TreeDeptTools.listMapToTree(list);
		return WrapMapper.ok(tree);
	}

	@ApiOperation(value = "部门人员Tree结构")
	@GetMapping("/dept_tree")
	public List<Map<String, Object>> dept_tree() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("OBJECT_ID","NAME","PARENT_ID");
		queryWrapper.eq("IS_DELETED", 0);
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		for (Map<String, Object> map : list) {
			QueryWrapper<AuUsrDept> qw = new QueryWrapper<>();
			qw.eq("a.DEPT_ID", map.get("OBJECT_ID"));
			List<Map<String,Object>> l = auUsrDeptMapper.deptUsrListForTree(qw);
			map.put("children", l);
		}
		List<Map<String, Object>> tree = TreeDeptTools.listMapToTree(list);
		return tree;
	}
	
	@ApiOperation(value = "表单使用——选择部门，label:部门名称  value:部门负责人")
	@GetMapping("/formDeptList")
	public List<Map<String, Object>> formDeptList() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.select("DEPT_MANA_USR_ID value", "NAME label");
		queryWrapper.ne("NAME", "经理部");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		return list;
	}
	
	@ApiOperation(value = "部门")
	@GetMapping("/list")
	public List<Map<String, Object>> list() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
//		queryWrapper.select("OBJECT_ID value","NAME label");
		queryWrapper.select("NAME value","NAME label");
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		return list;
	}

	@ApiOperation(value = "部门")
	@GetMapping("/list_id")
	public List<Map<String, Object>> list_id() {
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<>();
//		queryWrapper.select("OBJECT_ID value","NAME label");
		queryWrapper.select("OBJECT_ID value","NAME label");
		queryWrapper.orderByAsc("SHOW_ORDERS");
		List<Map<String, Object>> list = deptService.listMaps(queryWrapper);
		return list;
	}
}
