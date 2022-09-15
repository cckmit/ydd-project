package com.ydw.oa.auth.business.usr.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Joiner;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.func.entity.AuFunc;
import com.ydw.oa.auth.business.func.service.IAuFuncService;
import com.ydw.oa.auth.business.role.entity.AuRoleFunc;
import com.ydw.oa.auth.business.role.service.IAuRoleFuncService;
import com.ydw.oa.auth.business.usr.dto.MenuVo;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.SessionTool;
import com.ydw.oa.auth.util.tree.TreeFuncTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 当前登录用户菜单接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "当前登录用户菜单接口")
@RestController
@RequestMapping("/cp/usr/func")
public class UsrFuncController {

	@Autowired
	private IAuUsrService usrService;
	@Autowired
	private IAuUsrRoleService usrRoleService;
	@Autowired
	private IAuRoleFuncService roleFuncService;
	@Autowired
	private IAuFuncService funcService;

	@ApiOperation(value = "获取用户的所有菜单，好像没有")
	@GetMapping("/left_menu")
	public Wrapper<List<AuFunc>> left_menu() {
		String adminId = SessionTool.getSessionAdminId();
		AuUsr usr = usrService.getById(adminId);

		// 超级管理员,获取所有菜单
		if (usr.getType() == 0) {
			QueryWrapper<AuFunc> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("FUNC_TYPE", 0);
			queryWrapper.orderBy(true, true, "SHOW_ORDERS");
			List<AuFunc> list = funcService.list(queryWrapper);
			for (AuFunc auFunc : list) {
				// 获取按钮权限
				QueryWrapper<AuFunc> query = new QueryWrapper<>();
				query.in("PARENT_ID", auFunc.getObjectId());
				query.eq("FUNC_TYPE", 1);
				List<AuFunc> menus = funcService.list(query);
				List<String> roles = new ArrayList<String>();
				menus.forEach(item->{
					roles.add(item.getFuncName());
				});
				auFunc.setRole(Joiner.on(",").join(roles));
			}
			List<AuFunc> tree = TreeFuncTools.listToTree(list);
			return WrapMapper.ok(tree);
		}

		// 普通管理员,先找角色,角色再找菜单
		List<AuRoleFunc> allFuncList = new ArrayList<>();
		QueryWrapper<AuUsrRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_ID", adminId);
		List<AuUsrRole> list = usrRoleService.list(queryWrapper);
		for (AuUsrRole usrRole : list) {
			QueryWrapper<AuRoleFunc> funcQuery = new QueryWrapper<>();
			funcQuery.eq("ROLE_ID", usrRole.getRoleId());
			List<AuRoleFunc> list2 = roleFuncService.list(funcQuery);
			allFuncList.addAll(list2);
		}

		// 汇总list为funcIdsList
		List<String> funcIds = new ArrayList<>();
		for (AuRoleFunc roleFunc : allFuncList) {
			funcIds.add(roleFunc.getFuncId());
		}

		List<AuFunc> tree = new ArrayList<>();
		// 查询func列表
		if (ChkUtil.isNotNull(funcIds)) {
			QueryWrapper<AuFunc> funcQuery = new QueryWrapper<>();
			funcQuery.in("OBJECT_ID", funcIds);
			funcQuery.eq("FUNC_TYPE", 0);
			queryWrapper.orderBy(true, true, "SHOW_ORDERS");
			List<AuFunc> funcList = funcService.list(funcQuery);
			for (AuFunc auFunc : funcList) {
				// 获取按钮权限
				QueryWrapper<AuFunc> query = new QueryWrapper<>();
				query.in("OBJECT_ID", funcIds);
				query.in("PARENT_ID", auFunc.getObjectId());
				query.eq("FUNC_TYPE", 1);
				List<AuFunc> menus = funcService.list(query);
				List<String> roles = new ArrayList<String>();
				menus.forEach(item->{
					roles.add(item.getFuncName());
				});
				auFunc.setRole(Joiner.on(",").join(roles));
			}
			tree = TreeFuncTools.listToTree(funcList);
		}
		return WrapMapper.ok(tree);
	}
	
	@ApiOperation(value = "获取用户的所有菜单")
	@GetMapping("/new_left_menu")
	public Wrapper<List<MenuVo>> new_left_menu() {
		String adminId = SessionTool.getSessionAdminId();
		AuUsr usr = usrService.getById(adminId);

		// 超级管理员,获取所有菜单
		if (usr.getType() == 0) {
			QueryWrapper<AuFunc> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("FUNC_TYPE", 0);
			queryWrapper.orderBy(true, true, "SHOW_ORDERS");
			List<AuFunc> list = funcService.list(queryWrapper);
			for (AuFunc auFunc : list) {
				// 获取按钮权限
				QueryWrapper<AuFunc> query = new QueryWrapper<>();
				query.eq("PARENT_ID", auFunc.getObjectId());
				query.eq("FUNC_TYPE", 1);
				List<AuFunc> menus = funcService.list(query);
				List<String> roles = new ArrayList<String>();
				menus.forEach(item->{
					roles.add(item.getFuncName());
				});
				auFunc.setRole(Joiner.on(",").join(roles));
			}
			List<MenuVo> tree = TreeFuncTools.menuListToTree(list);
			return WrapMapper.ok(tree);
		}

		// 普通管理员,先找角色,角色再找菜单
		List<AuRoleFunc> allFuncList = new ArrayList<>();
		QueryWrapper<AuUsrRole> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_ID", adminId);
		List<AuUsrRole> list = usrRoleService.list(queryWrapper);
		for (AuUsrRole usrRole : list) {
			QueryWrapper<AuRoleFunc> funcQuery = new QueryWrapper<>();
			funcQuery.eq("ROLE_ID", usrRole.getRoleId());
			List<AuRoleFunc> list2 = roleFuncService.list(funcQuery);
			allFuncList.addAll(list2);
		}

		// 汇总list为funcIdsList
		List<String> funcIds = new ArrayList<>();
		for (AuRoleFunc roleFunc : allFuncList) {
			funcIds.add(roleFunc.getFuncId());
		}

		List<MenuVo> tree = new ArrayList<>();
		// 查询func列表
		QueryWrapper<AuFunc> funcQuery = new QueryWrapper<>();
		if (ChkUtil.isNotNull(funcIds)) {
			funcQuery.in("OBJECT_ID", funcIds);
			funcQuery.eq("FUNC_TYPE", 0);
			queryWrapper.orderBy(true, true, "SHOW_ORDERS");
			List<AuFunc> funcList = funcService.list(funcQuery);
			for (AuFunc auFunc : funcList) {
				// 获取按钮权限
				QueryWrapper<AuFunc> query = new QueryWrapper<>();
				query.in("OBJECT_ID", funcIds);
				query.eq("PARENT_ID", auFunc.getObjectId());
				query.eq("FUNC_TYPE", 1);
				List<AuFunc> menus = funcService.list(query);
				List<String> roles = new ArrayList<String>();
				menus.forEach(item->{
					roles.add(item.getFuncName());
				});
				auFunc.setRole(Joiner.on(",").join(roles));
			}
			tree = TreeFuncTools.menuListToTree(funcList);
			sortMenu(tree);
		}
		return WrapMapper.ok(tree);
	}

	private void sortMenu(List<MenuVo> tree) {
		tree.sort(new Comparator<MenuVo>() {
			@Override
			public int compare(MenuVo o1, MenuVo o2) {
				return o1.getSort() - o2.getSort();
			}
		});
		for (MenuVo menuVo : tree) {
			List<MenuVo> children = menuVo.getChildren();
			if (children != null && !children.isEmpty()) {
				sortMenu(children);
			}
		}

	}

}
