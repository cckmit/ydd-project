package com.ydw.oa.auth.business.attendance.service.impl;

import java.math.BigDecimal;

import com.tmsps.fk.common.util.ChkUtil;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.base.enums.ErrorCodeEnum;
import com.tmsps.fk.common.util.BusinessAssert;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceRule;
import com.ydw.oa.auth.business.attendance.mapper.PersAttendanceRuleMapper;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceRuleService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-10-18
 */
@Service
public class PersAttendanceRuleServiceImpl extends ServiceImpl<PersAttendanceRuleMapper, PersAttendanceRule> implements IPersAttendanceRuleService {

	@Override
	public BigDecimal getScoreByRule(String type) {
		BigDecimal systemScore = new BigDecimal(0);
		// TODO 根据考勤规则，计算系统分数
		QueryWrapper<PersAttendanceRule> qw = new QueryWrapper<PersAttendanceRule>();
		qw.eq("SIGN", type);
		PersAttendanceRule rule = this.getOne(qw);
		if (ChkUtil.isNotNull(rule)) {
			if("加分".equals(rule.getType())) {
				systemScore = systemScore.add(rule.getScore());
			}else {
				systemScore = systemScore.subtract(rule.getScore());
			}
		}
		return systemScore;
	}

}
