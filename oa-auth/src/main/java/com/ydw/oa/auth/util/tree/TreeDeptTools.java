package com.ydw.oa.auth.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.auth.business.dept.entity.AuDept;

public class TreeDeptTools {

	/**
	 * 树转为tree
	 * 
	 * @param oldList
	 * @return
	 */
	public static List<AuDept> listToTree(List<AuDept> oldList) {
		Map<String, Object> newMap = new HashMap<>();
		List<AuDept> newList = new ArrayList<>();
		for (AuDept treeDto : oldList) {
			newMap.put(treeDto.getObjectId(), treeDto);
		}
		for (AuDept treeDto : oldList) {
			AuDept parent = (AuDept) newMap.get(treeDto.getParentId());
			if (parent != null) {
				if (parent.getChildren() == null) {
					List<AuDept> ch = new ArrayList<>();
					ch.add(treeDto);
					parent.setChildren(ch);
				} else {
					List<AuDept> ch = parent.getChildren();
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
		List<AuDept> list = new ArrayList<>();

		AuDept f11 = new AuDept();
		f11.setObjectId("11");
		list.add(f11);

		AuDept f12 = new AuDept();
		f12.setObjectId("f12");
		list.add(f12);

		AuDept f111 = new AuDept();
		f111.setObjectId("f111");
		f111.setParentId("11");
		list.add(f111);

		List<AuDept> listToTree = TreeDeptTools.listToTree(list);
		System.err.println(JsonUtil.toJson(listToTree));

	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> listMapToTree(List<Map<String, Object>> oldList) {// antd tree
		Map<String, Object> newMap = new HashMap<>();
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> treeDto : oldList) {
			treeDto.put("id", treeDto.get("OBJECT_ID"));
			treeDto.put("name", treeDto.get("NAME"));
			treeDto.put("label", treeDto.get("NAME"));
			treeDto.put("key", treeDto.get("OBJECT_ID"));
			treeDto.put("value", treeDto.get("OBJECT_ID"));
			treeDto.put("title", treeDto.get("NAME"));
			
			newMap.put(treeDto.get("OBJECT_ID")+"", treeDto);
		}
		for (Map<String, Object> treeDto : oldList) {
			Map<String, Object> parent = (Map<String, Object>) newMap.get(treeDto.get("PARENT_ID")+"");
			if (parent != null) {
				if (parent.get("children") == null) {
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
}
