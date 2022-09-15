package com.ydw.oa.auth.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.role.dto.RoleDto;
import com.ydw.oa.auth.business.role.entity.AuRole;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface IAuRoleService extends IService<AuRole> {

	void save(RoleDto roleDto);

	void delete(String objectId);

	void update(RoleDto roleDto);

}
