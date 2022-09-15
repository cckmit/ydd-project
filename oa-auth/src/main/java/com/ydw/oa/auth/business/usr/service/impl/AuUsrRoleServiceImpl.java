package com.ydw.oa.auth.business.usr.service.impl;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.mapper.AuUsrRoleMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Service
public class AuUsrRoleServiceImpl extends ServiceImpl<AuUsrRoleMapper, AuUsrRole> implements IAuUsrRoleService {

	@Override
	@Transactional
	public void saveUsrRoles(String usrId, String roles) {
		if (ChkUtil.isNull(roles)) {
			return;
		}
		String[] roleIds = roles.split(",");
		for (String roleId : roleIds) {
			if (ChkUtil.isNull(roleId)) {
				continue;
			}
			AuUsrRole usrRole = new AuUsrRole();
			usrRole.setRoleId(roleId);
			usrRole.setUsrId(usrId);
			this.save(usrRole);
		}

	}

	@Override
	@Transactional
	public void deleteAllByUsrId(String usrId) {
		QueryWrapper<AuUsrRole> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("USR_ID", usrId);
			this.remove(queryWrapper);
		}
	}

}
