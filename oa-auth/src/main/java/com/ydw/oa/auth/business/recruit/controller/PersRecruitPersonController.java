package com.ydw.oa.auth.business.recruit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.recruit.dto.PersRecruitPersonQuery;
import com.ydw.oa.auth.business.recruit.entity.PersRecruitPerson;
import com.ydw.oa.auth.business.recruit.service.IPersRecruitPersonService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-03
 */
@Api(description = "应聘人员管理")
@RestController
@RequestMapping("/recruit/pers-recruit-person")
public class PersRecruitPersonController {

	@Autowired
	private IPersRecruitPersonService persRecruitPersonService;

	@ApiOperation(value = "应聘人员列表")
	@PostMapping("/list")
	public Wrapper<IPage<PersRecruitPerson>> list(
			@RequestBody PersRecruitPersonQuery<PersRecruitPerson> persRecruitPersonQuery) {
		IPage<PersRecruitPerson> page = persRecruitPersonService.page(persRecruitPersonQuery,
				persRecruitPersonQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加应聘人员")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody PersRecruitPerson persRecruitPersonDto) {
		persRecruitPersonService.add(persRecruitPersonDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取应聘人员数据")
	@GetMapping("/edit_form")
	public Wrapper<PersRecruitPerson> editForm(String objectId) {
		PersRecruitPerson persRecruitPersonPo = persRecruitPersonService.getById(objectId);
		return WrapMapper.ok(persRecruitPersonPo);
	}

	@ApiOperation(value = "修改应聘人员")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody PersRecruitPerson persRecruitPersonDto) {
		persRecruitPersonService.edit(persRecruitPersonDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除应聘人员数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		persRecruitPersonService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}
