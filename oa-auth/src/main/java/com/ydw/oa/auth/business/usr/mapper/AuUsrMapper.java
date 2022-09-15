package com.ydw.oa.auth.business.usr.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydw.oa.auth.business.usr.entity.AuUsr;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface AuUsrMapper extends BaseMapper<AuUsr> {

	List<Map<String, Object>> userList();

	String getReceiveUsrNames(String usrIds);

	// 同部门下人员
	List<Map<String, Object>> deptUserList(String usrId);

	List<String> getWxUsrIds(String userIds);

	List<Map<String, Object>> driverList();
}
