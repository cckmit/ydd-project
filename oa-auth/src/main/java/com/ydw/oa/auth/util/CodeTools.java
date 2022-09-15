package com.ydw.oa.auth.util;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import com.tmsps.fk.common.util.ChkUtil;

@Component
public class CodeTools {

	@Autowired
	protected JdbcTemplate jt;

	// 获取下一位编号
	public String getNextCode(String table_name, String field) {
		String sql = "select max(" + field + ") " + field + " from " + table_name + " t where t.status=0 ";
		List<Map<String, Object>> list = jt.queryForList(sql);
		String code = list.get(0).get(field) + "";
		System.out.println(code);
		return (ChkUtil.isNotNull(code) && !"null".equals(code)) ? String.valueOf((Integer.parseInt(code) + 1)) : "001";
	}

	// 生成编号
	public static String code(String code) {
		String newcode = "";
		if (ChkUtil.isNotNull(code) && code.length() >= 3) {
			String pre = code.substring(0, code.length() - 3);
			String suf = code.substring(code.length() - 3);
			int num = Integer.parseInt(suf);
			num += 1;
			String string = String.valueOf(num);
			for (int i = 0; i < 3 - string.length(); i++) {
				newcode += "0";
			}
			newcode = pre + newcode + string;
		} else {
			int num = Integer.parseInt(code);
			num += 1;
			String string = String.valueOf(num);
			for (int i = 0; i < 3 - string.length(); i++) {
				newcode += "0";
			}
			newcode = newcode + string;

		}
		return newcode;
	}

	public String getLength3Code(String type_code) {
		String sql = "select MAX(CODE) CODE from SYS_DICT t where t.IS_DELETE=0 and (t.TYPE_CODE=?) and LENGTH(CODE)=3";
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[] { type_code });

		return list.get(0).get("CODE") + "";
	}

	public String getGreaterLengthCode(String type_code, String code) {
		String sql = "select max(t.CODE) CODE from SYS_DICT t where LEFT(t.CODE," + code.length()
				+ ")=? and t.TYPE_CODE=? and LENGTH(t.code)=" + (code.length() + 3);
		List<Map<String, Object>> list = jt.queryForList(sql, new Object[] { code, type_code });
		return list.get(0).get("CODE") + "";
	}
}
