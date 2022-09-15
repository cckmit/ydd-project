package com.ydw.oa.auth.business.attendance.sechedule;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import io.netty.util.internal.ObjectUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceRuleService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.config.RedisUtil;
import com.ydw.oa.auth.util.DateTools;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;

import javax.sql.rowset.serial.SerialArray;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Slf4j
@RestController
@Component
public class PersAttendanceLogSecheduling {

	@Autowired
	private IPersAttendanceLogService iPersAttendanceLogService;

	/**
	 * 自动计算考核分数并缓存统计数据
	 */
	//每天凌晨1点30执行一次（在微信统计完考勤数据后执行）
	@Scheduled(cron = "0 30 1 * * ?")
	@RequestMapping("/sync/attendance_log/schedule")
	public void calcScore() {
		Date date = new Date();
		long time = date.getTime();
		long yesterday = time - 86400*1000L;
		String month = DateTools.format(new Date(yesterday), "yyyyMM");
		String day = DateTools.format(new Date(yesterday), "dd");
		QueryWrapper<PersAttendanceLog> persAttendanceLogQueryWrapper = new QueryWrapper<>();
		persAttendanceLogQueryWrapper.eq("MONTH",month).eq("DAY",day).eq("TYPE","系统考核");
		iPersAttendanceLogService.remove(persAttendanceLogQueryWrapper);
		//自动计算所有员工当日的考核分数
		iPersAttendanceLogService.autoSaveAttendanceLog(month,day);
		//统计所有员工当月的考核分数
		iPersAttendanceLogService.countAttendanceLogDataGroupByYearMonth(month);
	}

	@RequestMapping("/sync/attendance_log/data")
	private void calcScore(String month, String day){
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}

		if (ChkUtil.isNull(day)) {
			day = DateTools.getToday("dd");
		}
		QueryWrapper<PersAttendanceLog> persAttendanceLogQueryWrapper = new QueryWrapper<>();
		persAttendanceLogQueryWrapper.eq("MONTH",month).eq("DAY",day).eq("TYPE","系统考核");
		iPersAttendanceLogService.remove(persAttendanceLogQueryWrapper);
		//自动计算所有员工某天的考核分数
		iPersAttendanceLogService.autoSaveAttendanceLog(month,day);
		//统计所有员工某天的考核分数
		iPersAttendanceLogService.countAttendanceLogDataGroupByYearMonth(month);
	}


}
