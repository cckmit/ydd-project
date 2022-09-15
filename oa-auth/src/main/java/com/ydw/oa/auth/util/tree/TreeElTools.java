package com.ydw.oa.auth.util.tree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeElTools {

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> listMapToTree(List<Map<String, Object>> oldList, String labelColumn,
			String parentColumn) {// antd tree
		Map<String, Object> newMap = new HashMap<>();
		List<Map<String, Object>> newList = new ArrayList<>();
		for (Map<String, Object> treeDto : oldList) {
			treeDto.put("key", treeDto.get("OBJECT_ID"));
			treeDto.put("label", treeDto.get(labelColumn));

			newMap.put(treeDto.get("OBJECT_ID") + "", treeDto);
		}
		for (Map<String, Object> treeDto : oldList) {
			Map<String, Object> parent = (Map<String, Object>) newMap.get(treeDto.get(parentColumn) + "");
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
