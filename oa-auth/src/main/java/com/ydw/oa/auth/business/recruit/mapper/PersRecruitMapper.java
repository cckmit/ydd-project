package com.ydw.oa.auth.business.recruit.mapper;

import com.ydw.oa.auth.business.recruit.entity.PersRecruit;

import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-07-03
 */
public interface PersRecruitMapper extends BaseMapper<PersRecruit> {

	List<PersRecruit> selectList();

}
