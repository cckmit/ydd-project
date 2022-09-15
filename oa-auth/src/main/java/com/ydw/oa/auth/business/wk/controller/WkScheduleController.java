package com.ydw.oa.auth.business.wk.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.wk.dto.WkQuery;
import com.ydw.oa.auth.business.wk.entity.WkSchedule;
import com.ydw.oa.auth.business.wk.service.IWkScheduleService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-28
 */
@Api(description = "工作日程管理")
@RestController
@RequestMapping("/cp/wk-schedule")
public class WkScheduleController {

	@Autowired
	private IWkScheduleService scheduleService;
	
	@ApiOperation(value = "工作日程管理")
	@PostMapping("/list")
	public Wrapper<IPage<WkSchedule>> list(@RequestBody WkQuery<WkSchedule> query) {

		IPage<WkSchedule> page = scheduleService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加工作日程")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody WkSchedule wkSchedule) {
		wkSchedule.setUsrId(SessionTool.getSessionAdminId());
		scheduleService.save(wkSchedule);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除工作日程")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		scheduleService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "编辑获取工作日程数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<WkSchedule> editForm(String objectId) {
		WkSchedule wkSchedulePo = scheduleService.getById(objectId);
		return WrapMapper.ok(wkSchedulePo);
	}

	@ApiOperation(value = "编辑工作日程")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody WkSchedule wkScheduleDto) {
		WkSchedule wkSchedulePo = scheduleService.getById(wkScheduleDto.getObjectId());
		wkSchedulePo.setContent(wkScheduleDto.getContent());
		wkSchedulePo.setEndTime(wkScheduleDto.getEndTime());
		wkSchedulePo.setStartTime(wkScheduleDto.getStartTime());
		wkSchedulePo.setStatuz(wkScheduleDto.getStatuz());
		scheduleService.saveOrUpdate(wkSchedulePo);
		return WrapMapper.ok("保存成功");
	}

}

