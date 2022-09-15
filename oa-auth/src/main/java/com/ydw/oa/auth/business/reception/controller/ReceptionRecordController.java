package com.ydw.oa.auth.business.reception.controller;

import cn.hutool.core.map.MapUtil;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business_wkflow.WkflowFeignService;
import com.ydw.oa.auth.util.DateTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.reception.dto.ReceptionRecordQuery;
import com.ydw.oa.auth.business.reception.entity.ReceptionRecord;
import com.ydw.oa.auth.business.reception.service.IReceptionRecordService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2020-06-15
 */
@Api(description = "财务公司业务接待情况公示表")
@RestController
@RequestMapping("/cp/receptionRecord")
public class ReceptionRecordController {

	@Autowired
	private IReceptionRecordService receptionRecordService;
	@Autowired
	private WkflowFeignService wkflowFeignService;

	@ApiOperation(value = "财务公司业务接待情况公示表")
	@PostMapping("/list")
	public Wrapper<IPage<ReceptionRecord>> list(@RequestBody ReceptionRecordQuery<ReceptionRecord> receptionRecordQuery) {
		IPage<ReceptionRecord> page = receptionRecordService.page(receptionRecordQuery, receptionRecordQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "导出excel")
	@GetMapping("/export")
	public void export(String month) {
		if (ChkUtil.isNull(month)) {
			month = DateTools.getToday("yyyyMM");
		}
		Wrapper<Map<String, Object>> mapWrapper = wkflowFeignService.get();
		Map<String, Object> result = mapWrapper.getResult();
		String dataPath = MapUtil.getStr(result, "value");
		receptionRecordService.export(month,dataPath);
	}

}
