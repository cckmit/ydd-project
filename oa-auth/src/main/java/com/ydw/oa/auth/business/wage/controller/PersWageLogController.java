package com.ydw.oa.auth.business.wage.controller;

import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.wage.dto.PersWageLogQuery;
import com.ydw.oa.auth.business.wage.entity.PersWageLog;
import com.ydw.oa.auth.business.wage.service.IPersWageLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Api(description = "工资管理")
@RestController
@RequestMapping("/cp/pers-wage-log")
public class PersWageLogController {

	@Autowired
	private IPersWageLogService persWageLogService;

	@ApiOperation(value = "工资列表")
	@PostMapping("/list")
	public Wrapper<IPage<PersWageLog>> list(@RequestBody PersWageLogQuery<PersWageLog> query) {

		IPage<PersWageLog> page = persWageLogService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加工资")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody PersWageLog persWageLog) {
		if (ChkUtil.isNull(persWageLog.getSendTime())) {
			persWageLog.setSendTime(new Date());
		}
		persWageLogService.save(persWageLog);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除工资")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		persWageLogService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取工资数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<PersWageLog> editForm(String objectId) {
		PersWageLog persWageLogPo = persWageLogService.getById(objectId);
		return WrapMapper.ok(persWageLogPo);
	}

	@ApiOperation(value = "编辑工资")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody PersWageLog persWageLogDto) {
		PersWageLog persWageLogPo = persWageLogService.getById(persWageLogDto.getObjectId());
		persWageLogPo.setBaseWage(persWageLogDto.getBaseWage());
		persWageLogPo.setRealName(persWageLogDto.getRealName());
		persWageLogPo.setRealWage(persWageLogDto.getRealWage());
		persWageLogPo.setSendTime(persWageLogDto.getSendTime());
		persWageLogPo.setUsrId(persWageLogDto.getUsrId());
		persWageLogPo.setWageMonth(persWageLogDto.getWageMonth());
		persWageLogService.saveOrUpdate(persWageLogPo);
		return WrapMapper.ok("保存成功");
	}
	
	@ApiOperation(value = "导出工资模板")
	@GetMapping("/export_model")
	public void export_model() throws IOException {
		persWageLogService.export_model();
	}
	
	@ApiOperation(value = "导入工资数据")
	@GetMapping("/import_wage")
	public Wrapper<String> import_wage(String fileId) throws Exception {
		persWageLogService.import_wage(fileId);
		return WrapMapper.ok("导入成功");
	}
}
