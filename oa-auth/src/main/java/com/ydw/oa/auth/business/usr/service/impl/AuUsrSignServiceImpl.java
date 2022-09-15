package com.ydw.oa.auth.business.usr.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.usr.entity.AuUsrSign;
import com.ydw.oa.auth.business.usr.mapper.AuUsrSignMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrSignService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-09-20
 */
@Service
public class AuUsrSignServiceImpl extends ServiceImpl<AuUsrSignMapper, AuUsrSign> implements IAuUsrSignService {

	@Override
	public void saveUsrSigns(String usrId, String signs) {
		// TODO 保存人员的标签
		AuUsrSign usrSign = new AuUsrSign();
		usrSign.setUsrId(usrId);
		usrSign.setSign(signs);
		this.save(usrSign);
	}

	@Override
	public void deleteAllByUsrId(String usrId) {
		// TODO 删除标签
		QueryWrapper<AuUsrSign> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(usrId)) {
			queryWrapper.eq("USR_ID", usrId);
			this.remove(queryWrapper);
		}
	}

}
