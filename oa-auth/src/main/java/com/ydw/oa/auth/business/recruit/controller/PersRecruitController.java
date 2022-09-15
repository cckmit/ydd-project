package com.ydw.oa.auth.business.recruit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.recruit.dto.PersRecruitQuery;
import com.ydw.oa.auth.business.recruit.entity.PersRecruit;
import com.ydw.oa.auth.business.recruit.mapper.PersRecruitMapper;
import com.ydw.oa.auth.business.recruit.service.IPersRecruitService;

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
@Api(description = "招聘管理")
@RestController
@RequestMapping("/recruit/pers-recruit")
public class PersRecruitController {

	@Autowired
	private IPersRecruitService persRecruitService;
	@Autowired
	private PersRecruitMapper persRecruitMapper;

	@ApiOperation(value = "招聘列表")
	@PostMapping("/list")
	public Wrapper<IPage<PersRecruit>> list(@RequestBody PersRecruitQuery<PersRecruit> persRecruitQuery) {
		IPage<PersRecruit> page = persRecruitService.page(persRecruitQuery, persRecruitQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加招聘")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody PersRecruit persRecruitDto) {
		persRecruitService.save(persRecruitDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取招聘数据")
	@GetMapping("/edit_form")
	public Wrapper<PersRecruit> editForm(String objectId) {
		PersRecruit persRecruitPo = persRecruitService.getById(objectId);
		return WrapMapper.ok(persRecruitPo);
	}

	@ApiOperation(value = "修改招聘")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody PersRecruit persRecruitDto) {
		PersRecruit persRecruitPo = persRecruitService.getById(persRecruitDto.getObjectId());
		persRecruitPo.setConditionInfo(persRecruitDto.getConditionInfo());
		persRecruitPo.setGetNum(persRecruitDto.getGetNum());
		persRecruitPo.setName(persRecruitDto.getName());
		persRecruitPo.setNeedNum(persRecruitDto.getNeedNum());
		persRecruitService.saveOrUpdate(persRecruitPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除招聘数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		persRecruitService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "应聘岗位列表")
	@GetMapping("/list")
	public Wrapper<List<PersRecruit>> list() {
		List<PersRecruit> list = persRecruitMapper.selectList();
		return WrapMapper.ok(list);
	}
}
