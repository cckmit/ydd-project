package com.ydw.oa.auth.business.fk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.tmsps.fk.common.util.ChkUtil;

@Service
public class CheckUniqueService {

	@Autowired
	protected JdbcTemplate jt;

	public boolean selectTableFindValue(String table, String field, String value) {
		if (ChkUtil.isNull(table) || table.contains(" ")) {
			return false;
		}

		String sql = "select count(*) cnt from @table t where t.IS_DELETED!=1 and t.@field=?";
		sql = sql.replace("@table", table);
		sql = sql.replace("@field", field);
		Number l = (Number) jt.queryForMap(sql, value).get("cnt");

		return l.intValue() > 0 ? true : false;
	}

	public boolean selectTableFindValueNotme(String table, String field, String value, String object_id) {
		if (ChkUtil.isNull(table) || table.contains(" ")) {
			return false;
		}
		System.out.println(table+field+value+object_id);
		String sql = "select count(*) cnt from @table t where t.IS_DELETED!=1 and t.@field=? and t.OBJECT_ID!=?";
		sql = sql.replace("@table", table);
		sql = sql.replace("@field", field);
		Number l = (Number) jt.queryForMap(sql, value, object_id).get("cnt");
		return l.intValue() > 0 ? true : false;
	}

}
