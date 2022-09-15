package com.ydw.oa.auth.business.wage.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.wage.entity.PersWageItem;
import com.ydw.oa.auth.business.wage.service.IPersWageItemService;

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
@Api(description = "工资项")
@RestController
@RequestMapping("/cp/pers-wage-item")
public class PersWageItemController {

	@Autowired
	private IPersWageItemService persWageItemService;

	@ApiOperation(value = "工资项列表")
	@GetMapping("/list")
	public Wrapper<List<PersWageItem>> list() {
		QueryWrapper<PersWageItem> qw = new QueryWrapper<>();
		qw.orderByAsc("SORTZ");
		List<PersWageItem> list = persWageItemService.list(qw);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "添加工资项")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody PersWageItem persWageItem) {
		persWageItemService.save(persWageItem);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除工资项")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		persWageItemService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取工资项数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<PersWageItem> editForm(String objectId) {
		PersWageItem persWageItemPo = persWageItemService.getById(objectId);
		return WrapMapper.ok(persWageItemPo);
	}

	@ApiOperation(value = "编辑工资项")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody PersWageItem persWageItemDto) {
		PersWageItem persWageItemPo = persWageItemService.getById(persWageItemDto.getObjectId());
		persWageItemPo.setName(persWageItemDto.getName());
		persWageItemPo.setSortz(persWageItemDto.getSortz());
		persWageItemService.saveOrUpdate(persWageItemPo);
		return WrapMapper.ok("保存成功");
	}
}
