package com.ydw.oa.auth.business.attendance.service;

import com.ydw.oa.auth.business.attendance.entity.PersAttendanceRule;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-10-18
 */
public interface IPersAttendanceRuleService extends IService<PersAttendanceRule> {

	BigDecimal getScoreByRule(String string);

}
