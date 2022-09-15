package com.ydw.oa.auth.util.tree;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @author 冯晓东 398479251@qq.com
 *
 */
public class AuthTreeTools {

	/**
	 * 预处理树节点
	 * 
	 * @param menuList
	 * @param isChecked
	 * @return
	 */
	private static JSONArray handleTree(JSONArray menuList, boolean isChecked) {
		for (int i = 0; i < menuList.size(); i++) {
			JSONObject map = menuList.getJSONObject(i);
			map.put("key", map.getString("code"));
			map.put("value", map.getString("code"));
			map.put("title", map.getString("name"));
			if (isChecked) {
				map.put("checked", false);
			}
		}
		return menuList;
	}

	public static List<Map<String, Object>> turnListToTree(JSONArray menuList) {
		// 转换List为树形结构
		return turnListToTree(menuList, false);
	}

	@SuppressWarnings("unchecked")
	public static List<Map<String, Object>> turnListToTree(JSONArray menuList, boolean isChecked) {
		// 转换List为树形结构
		menuList = handleTree(menuList, isChecked);

		List<Map<String, Object>> nodeList = new ArrayList<Map<String, Object>>();

		for (int i = 0; i < menuList.size(); i++) {
			JSONObject node1 = menuList.getJSONObject(i);
			String node1_code = (String) node1.get("code");
			String node1_parent_code = node1_code.substring(0, node1_code.length() - 3);

			boolean mark = false;
			for (int j = 0; j < menuList.size(); j++) {
				Map<String, Object> node2 = menuList.getJSONObject(j);
				String node2_code = (String) node2.get("code");

				if (node1_parent_code != null && node1_parent_code.equals(node2_code)) {
					mark = true;
					if (node2.get("children") == null) {
						node2.put("children", new ArrayList<Map<String, Object>>());
					}
					((List<Map<String, Object>>) node2.get("children")).add(node1);
					node2.put("leaf", false);
					if (!isChecked) {
						node2.put("expanded", false);
					}
					break;
				}
			}
			if (!mark) {
				nodeList.add(node1);
			}
		}
		return nodeList;
	}

}
