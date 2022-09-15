package com.ydw.oa.auth.business.recruit.service.impl;

import com.ydw.oa.auth.business.recruit.entity.PersRecruit;
import com.ydw.oa.auth.business.recruit.entity.PersRecruitPerson;
import com.ydw.oa.auth.business.recruit.mapper.PersRecruitPersonMapper;
import com.ydw.oa.auth.business.recruit.service.IPersRecruitPersonService;
import com.ydw.oa.auth.business.recruit.service.IPersRecruitService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-07-03
 */
@Service
public class PersRecruitPersonServiceImpl extends ServiceImpl<PersRecruitPersonMapper, PersRecruitPerson>
		implements IPersRecruitPersonService {

	@Autowired
	private IPersRecruitService persRecruitService;

	@Override
	@Transactional
	public void add(PersRecruitPerson persRecruitPersonDto) {
		// TODO 新增
		this.save(persRecruitPersonDto);

		PersRecruit recruit = persRecruitService.getById(persRecruitPersonDto.getRecruitId());
		recruit.setGetNum(recruit.getGetNum() + 1);
		persRecruitService.saveOrUpdate(recruit);
	}

	@Override
	@Transactional
	public void edit(PersRecruitPerson persRecruitPersonDto) {
		// TODO 修改
		PersRecruitPerson persRecruitPersonPo = this.getById(persRecruitPersonDto.getObjectId());
		persRecruitPersonPo.setCertNo(persRecruitPersonDto.getCertNo());
		persRecruitPersonPo.setEmail(persRecruitPersonDto.getEmail());
		persRecruitPersonPo.setIsEnroll(persRecruitPersonDto.getIsEnroll());
		persRecruitPersonPo.setMobile(persRecruitPersonDto.getMobile());
		persRecruitPersonPo.setRealName(persRecruitPersonDto.getRealName());
		if (ChkUtil.isNotNull(persRecruitPersonDto.getRecruitId())) {
			if (!persRecruitPersonPo.getRecruitId().equals(persRecruitPersonDto.getRecruitId())) {
				PersRecruit recruit = persRecruitService.getById(persRecruitPersonPo.getRecruitId());
				recruit.setGetNum(recruit.getGetNum() - 1);
				persRecruitService.saveOrUpdate(recruit);

				PersRecruit recruit1 = persRecruitService.getById(persRecruitPersonDto.getRecruitId());
				recruit1.setGetNum(recruit.getGetNum() + 1);
				persRecruitService.saveOrUpdate(recruit1);
			}
		}
		persRecruitPersonPo.setRecruitId(persRecruitPersonDto.getRecruitId());
		persRecruitPersonPo.setRecruitName(persRecruitPersonDto.getRecruitName());
		this.saveOrUpdate(persRecruitPersonPo);
	}

}
