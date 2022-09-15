package com.ydw.oa.auth.business.usr.service.impl;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;

import java.util.List;

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
 * @since 2020-02-14
 */
@Service
public class AuUsrDeptServiceImpl extends ServiceImpl<AuUsrDeptMapper, AuUsrDept> implements IAuUsrDeptService {

	@Override
	public void saveUsrDepts(String usrId, String depts) {

		if (ChkUtil.isNull(depts)) {
			return;
		}
		String[] deptIds = depts.split(",");
		for (String deptId : deptIds) {
			if (ChkUtil.isNull(deptId)) {
				continue;
			}
			AuUsrDept usrDept = new AuUsrDept();
			usrDept.setDeptId(deptId);
			usrDept.setUsrId(usrId);
			this.save(usrDept);
		}

	}

	@Override
	@Transactional
	public void deleteAllByUsrId(String usrId) {
		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("USR_ID", usrId);
			this.remove(queryWrapper);
		}
	}

	@Override
	public String getUsrDept(String usrId) {
		// TODO 获取用户部门
		QueryWrapper<AuUsrDept> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_ID", usrId);
		List<AuUsrDept> list = this.list(queryWrapper);
		String deptIds = "";
		for (AuUsrDept auUsrDept : list) {
			deptIds += "," + auUsrDept.getDeptId();
		}
		if(ChkUtil.isNotNull(deptIds)) {
			deptIds = deptIds.replaceFirst(",", "");
		}
		return deptIds;
	}
}
