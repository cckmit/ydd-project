package com.ydw.oa.auth.business.usr.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface AuUsrRoleMapper extends BaseMapper<AuUsrRole> {

	// 获取某种角色下的人员
	List<String> selectRoleUserInfoList(@Param(Constants.WRAPPER) Wrapper<AuUsrRole> queryWrapper);

	List<String> roleList(String userId);

}
