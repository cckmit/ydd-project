package com.ydw.oa.auth.business.attendance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceRule;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.log.entity.OperateLog;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.DateTools;
import com.ydw.oa.auth.util.SessionTool;
import com.ydw.oa.auth.util.activiti.ReviewTool;
import io.netty.handler.codec.json.JsonObjectDecoder;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiParam;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.dto.PersAttendanceLogQuery;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.validation.Valid;

/**
 * <p>
 * ???????????????
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Api(description = "????????????")
@RestController
@RequestMapping("/cp/attendance-log")
public class PersAttendanceLogController {

	@Autowired
	private IPersAttendanceLogService persAttendanceLogService;
	@Autowired
	private IAuUsrService auUsrService;
	@Autowired
	private IAuUsrDeptService auUsrDeptService;
	@Autowired
	private IAuDeptService auDeptService;

	@ApiOperation(value = "????????????")
	@PostMapping("/list")
	public Wrapper<IPage<PersAttendanceLog>> list(@RequestBody PersAttendanceLogQuery<PersAttendanceLog> query) {
		String month = query.getMonth();
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
			query.setMonth(month);
		}
		IPage<PersAttendanceLog> page = persAttendanceLogService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "???????????????????????????")
	@PostMapping("/approve_list")
	public Wrapper<IPage<Map<String,Object>>> approve_list(@RequestBody PersAttendanceLogQuery<PersAttendanceLog> query) {
		IPage<Map<String, Object>> persAttendanceLogIPage = persAttendanceLogService.pageAttendanceLogByStatuzAndUserId(query, SessionTool.getSessionAdminId());
		return WrapMapper.ok(persAttendanceLogIPage);
	}




	@ApiOperation(value = "??????????????????")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody PersAttendanceLog persAttendanceLog) {
		String month = DateTools.getToday("yyyyMM");
		String day = DateTools.getToday("dd");
		persAttendanceLog.setMonth(month);
		persAttendanceLog.setDay(day);
		String operationType = persAttendanceLog.getOperationType();
		BigDecimal score = persAttendanceLog.getScore();
		if("??????".equals(operationType)) {
			persAttendanceLog.setScore(score.negate());
		}
		String type = persAttendanceLog.getType();
		String statuz = "?????????";
		if ("????????????".equals(type)) {
			statuz = "?????????";
		}
		persAttendanceLog.setStatuz(statuz);
		if(persAttendanceLogService.save(persAttendanceLog)){
			//??????????????????
			if (!"????????????".equals(type)) {
				persAttendanceLogService.countAttendanceLogDataGroupByUsrIdAndYearMonth(persAttendanceLog.getUsrId(), month);
			}
//			if (!"????????????".equals(type)) {
//			}else{
////				JSONObject usrId = SessionTool.getSessionAdmin();
////				AuUsr auUsr = auUsrService.getById(usrId);
////				QueryWrapper<AuUsrDept> deptQueryWrapper = new QueryWrapper<>();
////				deptQueryWrapper.eq("USR_ID", auUsr.getObjectId());
////				AuUsrDept auUsrDept = auUsrDeptService.getOne(deptQueryWrapper);
////				AuDept auDept = auDeptService.getById(auUsrDept.getDeptId());
//				//???????????????????????????????????????
////				ReviewTool.attendanceApprove(auDept.getUsrId(),persAttendanceLog.getObjectId());
//
//			}
		}
		return WrapMapper.ok("????????????");
	}



	@ApiOperation(value = "????????????????????????")
	@ApiImplicitParam(name = "objectId", value = "??????id")
	@GetMapping("/disagree")
	public Wrapper<String> disagree(String objectId) {
		PersAttendanceLog persAttendanceLog = persAttendanceLogService.getById(objectId);
		persAttendanceLog.setStatuz("?????????");
		persAttendanceLogService.updateById(persAttendanceLog);
		return WrapMapper.ok("????????????");
	}

	@ApiOperation(value = "??????????????????")
	@ApiImplicitParam(name = "objectId", value = "??????id")
	@GetMapping("/confirm")
	public Wrapper<String> confirm(String objectId) {
		PersAttendanceLog persAttendanceLog = persAttendanceLogService.getById(objectId);
		String usrId = persAttendanceLog.getUsrId();
		persAttendanceLog.setStatuz("?????????");
		if(persAttendanceLogService.updateById(persAttendanceLog)){
			//??????????????????
			persAttendanceLogService.countAttendanceLogDataGroupByUsrIdAndYearMonth(usrId, persAttendanceLog.getMonth());
		}
		return WrapMapper.ok("????????????");
	}

	@ApiOperation(value = "??????????????????")
	@ApiImplicitParam(name = "month", value = "??????id")
	@GetMapping("/report")
	public Wrapper<String> report(String month) {
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}
		String adminId = SessionTool.getSessionAdminId();
		QueryWrapper<AuUsrDept> deptQueryWrapper = new QueryWrapper<>();
		deptQueryWrapper.eq("USR_ID",adminId);
		AuUsrDept auUsrDept = auUsrDeptService.getOne(deptQueryWrapper);
		List<PersAttendanceLog> list = persAttendanceLogService.listAttendanceLogMonthByStatuzAndDeptId(month, "?????????", auUsrDept.getDeptId());
		if(list.size()<=0){
			return WrapMapper.ok("?????????????????????????????????????????????????????????");
		}
		for (PersAttendanceLog persAttendanceLog : list) {
			persAttendanceLog.setStatuz("?????????");
			persAttendanceLogService.updateById(persAttendanceLog);
		}
		AuDept auDept = auDeptService.getById(auUsrDept.getDeptId());

		ReviewTool.attendanceApprove(auDept.getUsrId(),month,auDept.getObjectId(),auDept.getName());

		return WrapMapper.ok("????????????");
	}

	@ApiOperation(value = "??????????????????")
	@ApiImplicitParam(name = "objectId", value = "??????id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		PersAttendanceLog persAttendanceLog = persAttendanceLogService.getById(objectId);
		String usrId = persAttendanceLog.getUsrId();
		if(persAttendanceLogService.removeById(objectId)){
			//??????????????????
			persAttendanceLogService.countAttendanceLogDataGroupByUsrIdAndYearMonth(usrId, persAttendanceLog.getMonth());
		}
		return WrapMapper.ok("????????????");
	}
}
