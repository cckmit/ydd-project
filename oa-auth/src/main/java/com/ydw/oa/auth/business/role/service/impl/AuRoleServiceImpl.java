package com.ydw.oa.auth.business.role.service.impl;

import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.role.dto.RoleDto;
import com.ydw.oa.auth.business.role.entity.AuRole;
import com.ydw.oa.auth.business.role.mapper.AuRoleMapper;
import com.ydw.oa.auth.business.role.service.IAuRoleFuncService;
import com.ydw.oa.auth.business.role.service.IAuRoleService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Service
public class AuRoleServiceImpl extends ServiceImpl<AuRoleMapper, AuRole> implements IAuRoleService {

	@Autowired
	private IAuRoleFuncService roleFuncService;

	@Override
	@Transactional
	public void save(@Valid RoleDto roleDto) {
		AuRole role = new AuRole();
		BeanUtils.copyProperties(roleDto, role);
		role.setCreateTime(new Date());
		this.save(role);

		roleFuncService.save(role.getObjectId(), roleDto.getMenus());

	}

	@Override
	@Transactional
	public void delete(String objectId) {
		this.removeById(objectId);
		roleFuncService.deleteAllByRoleId(objectId);
	}

	@Override
	@Transactional
	public void update(RoleDto roleDto) {
		AuRole rolePo = this.getById(roleDto.getObjectId());

		// 1. 删掉旧数据
		roleFuncService.deleteAllByRoleId(roleDto.getObjectId());

		// 2. 更改字段
		rolePo.setName(roleDto.getName());
		rolePo.setDescription(roleDto.getDescription());
		rolePo.setStatus(roleDto.getStatus());
		rolePo.setShowOrders(roleDto.getShowOrders());
		rolePo.setVariable(roleDto.getVariable());
		rolePo.setLastUpdateTime(new Date(System.currentTimeMillis()));
		this.saveOrUpdate(rolePo);

		// 3. 保存新的级联数据
		roleFuncService.save(rolePo.getObjectId(), roleDto.getMenus());
	}

}
