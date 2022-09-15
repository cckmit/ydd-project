package com.ydw.oa.auth.business.role.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.role.entity.AuRoleFunc;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface IAuRoleFuncService extends IService<AuRoleFunc> {

	void save(String roleId, String menus);

	void deleteAllByRoleId(String objectId);

}
