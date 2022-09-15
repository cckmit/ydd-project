package com.ydw.oa.auth.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.auth.business.dict.entity.SysDict;

public class TreeDictTools {

	/**
	 * 树转为tree
	 * 
	 * @param oldList
	 * @return
	 */
	public static List<SysDict> listToTree(List<SysDict> oldList) {
		Map<String, Object> newMap = new HashMap<>();
		List<SysDict> newList = new ArrayList<>();
		for (SysDict treeDto : oldList) {
			newMap.put(treeDto.getObjectId(), treeDto);
		}
		for (SysDict treeDto : oldList) {
			SysDict parent = (SysDict) newMap.get(treeDto.getParentId());
			if (parent != null) {
				if (parent.getChildren() == null) {
					List<SysDict> ch = new ArrayList<>();
					ch.add(treeDto);
					parent.setChildren(ch);
				} else {
					List<SysDict> ch = parent.getChildren();
					ch.add(treeDto);
					parent.setChildren(ch);
				}
			} else {
				newList.add(treeDto);
			}
		}
		return newList;
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> listMapToTree(List<Map<String, Object>> oldList) {
		Map<String, Object> newMap = new HashMap<>();
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> treeDto : oldList) {
			newMap.put(treeDto.get("OBJECT_ID").toString(), treeDto);
		}
		for (Map<String, Object> treeDto : oldList) {
			Map<String, Object> parent = (Map<String, Object>) newMap.get(treeDto.get("PARENT_ID"));
			if (parent != null) {
				if (ChkUtil.isNull(parent.get("children"))) {
					List<Map<String, Object>> ch = new ArrayList<>();
					ch.add(treeDto);
					parent.put("children", ch);
				} else {
					List<Map<String, Object>> ch = (List<Map<String, Object>>) parent.get("children");
					ch.add(treeDto);
					parent.put("children", ch);
				}
			} else {
				newList.add(treeDto);
			}
		}
		return newList;
	}

	public static void main1(String[] args) {
		List<SysDict> list = new ArrayList<>();

		SysDict f11 = new SysDict();
		f11.setObjectId("11");
		list.add(f11);

		SysDict f12 = new SysDict();
		f12.setObjectId("f12");
		list.add(f12);

		SysDict f111 = new SysDict();
		f111.setObjectId("f111");
		f111.setParentId("11");
		list.add(f111);

		List<SysDict> listToTree = TreeDictTools.listToTree(list);
		System.err.println(JsonUtil.toJson(listToTree));

	}
}
