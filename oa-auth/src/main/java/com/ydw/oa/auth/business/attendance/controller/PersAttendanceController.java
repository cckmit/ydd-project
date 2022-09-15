package com.ydw.oa.auth.business.attendance.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tmsps.fk.common.base.enums.ErrorCodeEnum;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.BusinessAssert;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.WxClockIn;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceService;
import com.ydw.oa.auth.business.dept.dto.UserQuery;
import com.ydw.oa.auth.business.usr.dto.UsrQuery;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.mapper.AuUsrDeptMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business_wkflow.WkflowFeignService;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.config.RedisUtil;
import com.ydw.oa.auth.util.DateTools;
import com.ydw.oa.auth.util.WebUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Api(description = "个人考勤/考核管理")
@RestController
@RequestMapping("/cp/attendance")
public class PersAttendanceController {

	@Autowired
	private WxFeignService wxFeignService;
	@Autowired
	private IAuUsrService usrService;
	@Autowired
	private AuUsrDeptMapper auUsrDeptMapper;
	@Autowired
	private WkflowFeignService wkflowFeignService;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private IPersAttendanceService persAttendanceService;

	@ApiOperation(value = "个人考勤数据列表")
	@PostMapping("/list")
	public Wrapper<Page<WxClockIn>> list(@RequestParam("objectId") String objectId, @RequestParam(value = "month",required = false) String month, @RequestBody Page<WxClockIn> page) {
		AuUsr user = usrService.getById(objectId);
		String wxUserId = user.getWxUserId();
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}
		if (ChkUtil.isNull(wxUserId)) {
			throw new BusinessException("账号未绑定企业微信，请联系系统管理员进行配置");
		}
		Wrapper<Page<WxClockIn>> clockinDetail = wxFeignService.getClockinDetail(wxUserId, month, page);
		return clockinDetail;
	}

	@ApiOperation(value = "某个部门人员某个月的考勤考核数据列表")
	@PostMapping("/datas")
	public Wrapper<IPage<Map<String, Object>>> datas(@RequestParam(value="month",required = false) String month,@RequestBody UserQuery<AuUsrDept> userQuery) {
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}
		IPage<Map<String, Object>> pageResult = auUsrDeptMapper.query(userQuery, userQuery.makeQueryWrapper());
		List<Map<String, Object>> records = pageResult.getRecords();
		for (Map<String, Object> user : records) {
			String userId = (String) user.get("OBJECT_ID");
			String wxUserId = (String) user.get("WX_USER_ID");
			String userKey = userId.concat("#").concat(month);
			Map<Object, Object> logDatas = redisUtil.hGetAll(userKey);
			if (MapUtils.isNotEmpty(logDatas)) {
				user.putAll(JsonUtil.jsonStrToMap(JsonUtil.toJson(logDatas)));
			}
			if (ChkUtil.isNotNull(wxUserId)) {
				String userWxKey = wxUserId.concat("#").concat(month);
				Map<Object, Object> clockinDatas = redisUtil.hGetAll(userWxKey);
				if(MapUtils.isNotEmpty(clockinDatas)){
					user.putAll(JsonUtil.jsonStrToMap(JsonUtil.toJson(clockinDatas)));
				}
			}
		}
		return WrapMapper.ok(pageResult);
	}

	@ApiOperation(value = "导出excel")
	@GetMapping("/export")
	public void export(String month,String startTime,String endTime,String pubTime) {
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}
		Wrapper<Map<String, Object>> mapWrapper = wkflowFeignService.get();
		Map<String, Object> result = mapWrapper.getResult();
		String dataPath = MapUtil.getStr(result, "value");
		persAttendanceService.export(month,dataPath,startTime,endTime,pubTime);
	}

}
