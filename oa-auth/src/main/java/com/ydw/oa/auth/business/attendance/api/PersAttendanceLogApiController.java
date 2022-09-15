package com.ydw.oa.auth.business.attendance.api;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;
import com.ydw.oa.auth.business.car.mapper.CarMapper;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "考核管理")
@RestController
@RequestMapping("/api/attendance")
public class PersAttendanceLogApiController {

	@Autowired
	private IPersAttendanceLogService persAttendanceLogService;
	@Autowired
	private IAuUsrService auUsrService;
	@Autowired
	private IAuUsrDeptService auUsrDeptService;
	@Autowired
	private IAuDeptService auDeptService;

	@ApiOperation(value = "查询考核数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/get")
	public Wrapper<JSONObject> get(String objectId) {
		JSONObject jsonObject = new JSONObject();
		PersAttendanceLog persAttendanceLog = persAttendanceLogService.getById(objectId);
		jsonObject.putAll(JsonUtil.objToMap(persAttendanceLog));
		String usrId = persAttendanceLog.getUsrId();
		AuUsr auUsr = auUsrService.getById(usrId);
		jsonObject.put("usrName", auUsr.getRealName());
		QueryWrapper<AuUsrDept> deptQueryWrapper = new QueryWrapper<>();
		deptQueryWrapper.eq("USR_ID", auUsr.getObjectId());
		AuUsrDept auUsrDept = auUsrDeptService.getOne(deptQueryWrapper);
		AuDept auDept = auDeptService.getById(auUsrDept.getDeptId());
		jsonObject.put("deptName",auDept.getName());
		return WrapMapper.ok(jsonObject);
	}

	@ApiOperation(value = "待审核考核数据列表(按月份和部门)")
	@GetMapping("/dept_approve_list")
	public Wrapper<JSONObject> dept_approve_list(String month,String deptId) {
		JSONObject jsonObject = new JSONObject();
		AuDept auDept = auDeptService.getById(deptId);
		String year = month.substring(0,4);
		String monthStr = month.substring(4);
		jsonObject.put("oa-text-title",auDept.getName().concat(year).concat("年").concat(monthStr).concat("月").concat("考核情况明细表"));
		List<PersAttendanceLog> list = persAttendanceLogService.listAttendanceLogMonthByStatuzAndDeptId(month, "已上报", deptId);
		JSONArray depts = new JSONArray();

		List<List<PersAttendanceLog>> persAttendanceLogss = CollUtil.groupByField(list, "usrId");
		for (List<PersAttendanceLog> persAttendanceLogs : persAttendanceLogss) {
			JSONObject jsonObject1 = new JSONObject(new LinkedHashMap());
			ArrayList<String> scoreList = new ArrayList<>();
			ArrayList<String> noteList = new ArrayList<>();
			BigDecimal total = new BigDecimal(100);
			for (PersAttendanceLog persAttendanceLog : persAttendanceLogs) {
				if(ObjectUtil.isNull(jsonObject1.getString("oa-list-0-col-1"))){
					AuUsr auUsr = auUsrService.getById(persAttendanceLog.getUsrId());
					jsonObject1.put("oa-list-0-col-1", auUsr.getRealName());
				}
				scoreList.add(persAttendanceLog.getScore().toString());
				noteList.add(persAttendanceLog.getNote());
				total = total.add(persAttendanceLog.getScore());
			}
			jsonObject1.put("oa-list-0-col-2","100");
			jsonObject1.put("oa-list-0-col-3",scoreList);
			jsonObject1.put("oa-list-0-col-4", noteList);
			jsonObject1.put("oa-list-0-col-5", total.toString());
			depts.add(jsonObject1);
		}
		jsonObject.put("oa-list-0",depts);

		return WrapMapper.ok(jsonObject);
	}

	@ApiOperation(value = "审核通过考核数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/agree")
	public Wrapper<String> agree(String month,String deptId,String userId) {
		List<PersAttendanceLog> list = persAttendanceLogService.listAttendanceLogMonthByStatuzAndDeptId(month, "已上报", deptId);
		boolean flag = false;
		for (PersAttendanceLog persAttendanceLog : list) {
			persAttendanceLog.setStatuz("已确认");
			persAttendanceLog.setApproveUsrId(userId);
			persAttendanceLogService.updateById(persAttendanceLog);
			flag = true;
		}
		if (flag) {
			persAttendanceLogService.countAttendanceLogDataGroupByYearMonth(month);
		}

		return WrapMapper.ok("审核成功");
	}

	@ApiOperation(value = "审核拒绝考核数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/disagree")
	public Wrapper<String> disagree(String month,String deptId,String userId) {
		List<PersAttendanceLog> list = persAttendanceLogService.listAttendanceLogMonthByStatuzAndDeptId(month, "已上报", deptId);
		for (PersAttendanceLog persAttendanceLog : list) {
			persAttendanceLog.setStatuz("已拒绝");
			persAttendanceLog.setApproveUsrId(userId);
			persAttendanceLogService.updateById(persAttendanceLog);
		}
		return WrapMapper.ok("审核完成");
	}
}
