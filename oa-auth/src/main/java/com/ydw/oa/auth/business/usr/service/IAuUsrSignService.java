package com.ydw.oa.auth.business.usr.service;

import com.ydw.oa.auth.business.usr.entity.AuUsrSign;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-09-20
 */
public interface IAuUsrSignService extends IService<AuUsrSign> {

	void saveUsrSigns(String usrId, String signs);

	void deleteAllByUsrId(String usrId);

}
