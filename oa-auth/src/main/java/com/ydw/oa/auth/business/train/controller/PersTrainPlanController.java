package com.ydw.oa.auth.business.train.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.train.dto.PersTrainPlanQuery;
import com.ydw.oa.auth.business.train.entity.PersTrainPlan;
import com.ydw.oa.auth.business.train.service.IPersTrainPlanService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-03
 */
@Api(description = "培训管理")
@RestController
@RequestMapping("/cp/train")
public class PersTrainPlanController {

	@Autowired
	private IPersTrainPlanService persTrainPlanService;

	@ApiOperation(value = "培训列表")
	@PostMapping("/list")
	public Wrapper<IPage<PersTrainPlan>> list(@RequestBody PersTrainPlanQuery<PersTrainPlan> persTrainPlanQuery) {
		IPage<PersTrainPlan> page = persTrainPlanService.page(persTrainPlanQuery, persTrainPlanQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加培训")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody PersTrainPlan persTrainPlanDto) {
		persTrainPlanService.save(persTrainPlanDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取培训数据")
	@GetMapping("/edit_form")
	public Wrapper<PersTrainPlan> editForm(String objectId) {
		PersTrainPlan persTrainPlanPo = persTrainPlanService.getById(objectId);
		return WrapMapper.ok(persTrainPlanPo);
	}

	@ApiOperation(value = "修改培训")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody PersTrainPlan persTrainPlanDto) {
		PersTrainPlan persTrainPlanPo = persTrainPlanService.getById(persTrainPlanDto.getObjectId());
		persTrainPlanPo.setContent(persTrainPlanDto.getContent());
		persTrainPlanPo.setFileId(persTrainPlanDto.getFileId());
		persTrainPlanPo.setFlow(persTrainPlanDto.getFlow());
		persTrainPlanPo.setReview(persTrainPlanDto.getReview());
		persTrainPlanPo.setStartTime(persTrainPlanDto.getStartTime());
		persTrainPlanPo.setTitle(persTrainPlanDto.getTitle());
		persTrainPlanService.saveOrUpdate(persTrainPlanPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除培训数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		persTrainPlanService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
}

