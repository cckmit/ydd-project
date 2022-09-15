package com.ydw.oa.auth.business.usr.service;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.usr.dto.CurrentUsrDto;
import com.ydw.oa.auth.business.usr.dto.UsrDto;
import com.ydw.oa.auth.business.usr.entity.AuUsr;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
public interface IAuUsrService extends IService<AuUsr> {

	AuUsr login(String usrName, String password);

	void saveUser(@Valid UsrDto usrDto);

	void delete(String objectId);

	void updateUser(@Valid UsrDto usrDto);

	void updateUserInfo(@Valid CurrentUsrDto usrDto);

	void export();
}
