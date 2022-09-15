package com.ydw.oa.auth.business.recruit.service;

import com.ydw.oa.auth.business.recruit.entity.PersRecruitPerson;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-07-03
 */
public interface IPersRecruitPersonService extends IService<PersRecruitPerson> {

	void add(PersRecruitPerson persRecruitPersonDto);

	void edit(PersRecruitPerson persRecruitPersonDto);

}
