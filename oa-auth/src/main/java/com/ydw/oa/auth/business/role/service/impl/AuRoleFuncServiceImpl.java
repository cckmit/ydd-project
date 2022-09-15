package com.ydw.oa.auth.business.role.service.impl;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.role.entity.AuRoleFunc;
import com.ydw.oa.auth.business.role.mapper.AuRoleFuncMapper;
import com.ydw.oa.auth.business.role.service.IAuRoleFuncService;

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
public class AuRoleFuncServiceImpl extends ServiceImpl<AuRoleFuncMapper, AuRoleFunc> implements IAuRoleFuncService {

	@Override
	@Transactional
	public void save(String roleId, String menus) {
		if (ChkUtil.isNull(menus)) {
			return;
		}

		// 逗号分隔保存
		String[] menuArr = menus.split(",");
		for (String funcId : menuArr) {
			AuRoleFunc roleFunc = new AuRoleFunc();
			roleFunc.setFuncId(funcId);
			roleFunc.setRoleId(roleId);
			this.save(roleFunc);
		}
	}

	@Override
	@Transactional
	public void deleteAllByRoleId(String roleId) {
		QueryWrapper<AuRoleFunc> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(roleId)) {
			queryWrapper.eq("ROLE_ID", roleId);
			this.remove(queryWrapper);
		}
	}

}
