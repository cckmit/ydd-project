package com.ydw.oa.auth.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.auth.business.func.entity.AuFunc;
import com.ydw.oa.auth.business.usr.dto.MenuVo;
import com.ydw.oa.auth.business.usr.dto.Meta;

public class TreeFuncTools {

	/**
	 * 树转为tree
	 * 
	 * @param oldList
	 * @return
	 */
	public static List<AuFunc> listToTree(List<AuFunc> oldList) {
		Map<String, Object> newMap = new HashMap<>();
		List<AuFunc> newList = new ArrayList<>();
		for (AuFunc treeDto : oldList) {
			newMap.put(treeDto.getObjectId(), treeDto);
		}
		for (AuFunc treeDto : oldList) {
			AuFunc parent = (AuFunc) newMap.get(treeDto.getParentId());
			if (parent != null) {
				if (parent.getChildren() == null) {
					List<AuFunc> ch = new ArrayList<>();
					ch.add(treeDto);
					parent.setChildren(ch);
				} else {
					List<AuFunc> ch = parent.getChildren();
					ch.add(treeDto);
					parent.setChildren(ch);
				}
			} else {
				newList.add(treeDto);
			}
		}
		return newList;
	}

	public static List<MenuVo> toMenuList(List<AuFunc> oldList) {

		List<MenuVo> list = new ArrayList<>();
		for (AuFunc afFunc : oldList) {
			MenuVo menuVo = new MenuVo();
			menuVo.setComponent(afFunc.getFuncUrls());
			menuVo.setLabel(afFunc.getFuncName());
			menuVo.setIcon(afFunc.getFuncIcon());
			menuVo.setObjectId(afFunc.getObjectId());
			menuVo.setParentId(afFunc.getParentId());
			menuVo.setStatus(afFunc.getStatus());
			menuVo.setRoles(afFunc.getRole());
			menuVo.setSort(afFunc.getShowOrders());
			Meta meta = new Meta();
			//TODD 从afFunc中获取KEEP_ALIVE的值设置到meta.keepAlive中;
			meta.setKeepAlive(afFunc.isKeepAlive());

			menuVo.setMeta(meta);
			//vue路由
			if(ChkUtil.isNotNull(afFunc.getFuncUrls())&&afFunc.getFuncUrls().startsWith("http")) {
				menuVo.setPath(afFunc.getFuncUrls());
			}else {
				menuVo.setPath("/"+afFunc.getObjectId());
			}
			list.add(menuVo);
		}
		return list;
	}

	public static List<MenuVo> menuListToTree(List<AuFunc> aucList) {
		
		List<MenuVo> oldList = toMenuList(aucList);
		Map<String, Object> newMap = new HashMap<>();
		List<MenuVo> newList = new ArrayList<>();
		for (MenuVo treeDto : oldList) {
			newMap.put(treeDto.getObjectId(), treeDto);
		}
		for (MenuVo treeDto : oldList) {
			MenuVo parent = (MenuVo) newMap.get(treeDto.getParentId());
			if (parent != null) {
				if (parent.getChildren() == null) {
					List<MenuVo> ch = new ArrayList<>();
					ch.add(treeDto);
					parent.setChildren(ch);
				} else {
					List<MenuVo> ch = parent.getChildren();
					ch.add(treeDto);
					parent.setChildren(ch);
				}
			} else {
				newList.add(treeDto);
			}
		}
		return newList;
	}

	public static void main1(String[] args) {
		List<AuFunc> list = new ArrayList<>();

		AuFunc f11 = new AuFunc();
		f11.setObjectId("11");
		list.add(f11);

		AuFunc f12 = new AuFunc();
		f12.setObjectId("f12");
		list.add(f12);

		AuFunc f111 = new AuFunc();
		f111.setObjectId("f111");
		f111.setParentId("11");
		list.add(f111);

		List<AuFunc> listToTree = TreeFuncTools.listToTree(list);
		System.err.println(JsonUtil.toJson(listToTree));

	}
}
