package com.ydw.oa.auth.business.usr.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-14
 */
public interface IAuUsrDeptService extends IService<AuUsrDept> {

	void saveUsrDepts(String usrId, String depts);

	void deleteAllByUsrId(String usrId);

	String getUsrDept(String usrId);

}
