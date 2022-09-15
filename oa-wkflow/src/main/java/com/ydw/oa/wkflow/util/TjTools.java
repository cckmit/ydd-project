package com.ydw.oa.wkflow.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tmsps.fk.common.util.ChkUtil;


public class TjTools {

	// 获取统计数据
	public static Map<String, Integer> groupBy(List<Map<String, Object>> list, String key) {
		Map<String, Integer> result = new HashMap<>();
		if (ChkUtil.isNull(list)) {
			return result;
		}

		for (Map<String, Object> map : list) {
			String val = (String) map.get(key);
			if (val == null) {
				continue;
			}

			Integer cnt = result.get(val);

			if (cnt == null) {
				cnt = 1;
			} else {
				cnt++;
			}

			result.put(val, cnt);
		}
		return result;

	}

	// 分组保存每年12个月的数据
	public static List<Map<String, Object>> groupByAddTimes(List<Map<String, Object>> list,
			List<Map<String, Object>> type) {
		if (list.size() == 0 || type.size() == 0) {
			return null;
		}
		List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();
		// 遍历type中的数据
		type.forEach(t -> {
			// 创建map用来保存数据
			Map<String, Object> map = new HashMap<String, Object>();
			// 创建int[] 用来保存12个月的数据
			int[] data = new int[12];
			// 遍历list中的数据
			if (t.get("type") != null) {
				list.forEach(l -> {
					long num = (long) l.get("num");
					String times = (String) l.get("times");
					if (l.get("type") != null && l.get("type").equals(t.get("type"))) {
						data[ChkUtil.getInteger(times.substring(times.indexOf("-") + 1)) - 1] = (int) num;
					}
				});
				map.put("name", t.get("type"));
				map.put("data", data);
				list2.add(map);
			}
		});
		return list2;
	}

	// 保存每年12个月的数据
	public static Map<String, Object> addTimes(Map<String, Integer> map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		int[] a = new int[12];
		Set<String> timeKey = map.keySet();
		for (String s : timeKey) {
			a[ChkUtil.getInteger(s.substring(s.indexOf("-") + 1)) - 1] = map.get(s);
		}
		map1.put("time", a);
		return map1;
	}

	// 保存每年每天的数据
	public static Map<String, Object> addYearTimes(List<Map<String, Object>> list, int date) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, date);
		cal.set(Calendar.MONTH, 1);
		cal.set(Calendar.DAY_OF_YEAR, 1);
		Calendar cal1 = Calendar.getInstance();
		cal1.set(Calendar.YEAR, date + 1);
		cal1.set(Calendar.MONTH, 1);
		cal1.set(Calendar.DAY_OF_YEAR, 1);
		cal1.set(Calendar.HOUR_OF_DAY, 0);
		cal1.set(Calendar.MINUTE, 0);
		cal1.set(Calendar.SECOND, 0);
		while (cal.compareTo(cal1) < 0) {
			map.put(sdf.format(cal.getTime()), 0);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		}
		list.forEach(l -> {
			map.put((String) l.get("times"), l.get("num"));
		});
		return map;

	}

	public static void main(String[] args) {

		addYearTimes(null, 2017);

	}

}
