package com.ydw.oa.auth.business.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface IAuUsrRoleService extends IService<AuUsrRole> {

	void saveUsrRoles(String usrId, String roles);

	void deleteAllByUsrId(String usrId);

}
