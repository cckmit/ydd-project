package com.ydw.oa.auth.business.wage.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.wage.entity.PersWageAlgorithmic;
import com.ydw.oa.auth.business.wage.service.IPersWageAlgorithmicService;

import io.swagger.annotations.Api;
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
@Api(description = "工资计算公式")
@RestController
@RequestMapping("/cp/wage-algorithmic")
public class PersWageAlgorithmicController {

	@Autowired
	private IPersWageAlgorithmicService persWageAlgorithmicService;

	@ApiOperation(value = "获取工资计算公式")
	@GetMapping("/get")
	public Wrapper<PersWageAlgorithmic> editForm() {
		PersWageAlgorithmic persWageAlgorithmicPo = persWageAlgorithmicService.getOne(new QueryWrapper<>());
		return WrapMapper.ok(persWageAlgorithmicPo);
	}

	@ApiOperation(value = "保存工资计算公式")
	@PostMapping("/and_and_edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody PersWageAlgorithmic persWageAlgorithmicDto) {
		PersWageAlgorithmic persWageAlgorithmicPo = persWageAlgorithmicService.getOne(new QueryWrapper<>());
		if (ChkUtil.isNull(persWageAlgorithmicPo)) {
			persWageAlgorithmicService.save(persWageAlgorithmicDto);
		} else {
			persWageAlgorithmicPo.setTitle(persWageAlgorithmicDto.getTitle());
			persWageAlgorithmicService.saveOrUpdate(persWageAlgorithmicPo);
		}
		return WrapMapper.ok("保存成功");
	}

}
