package com.ydw.oa.auth.business.dict.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.dict.dto.DictQuery;
import com.ydw.oa.auth.business.dict.entity.SysDict;
import com.ydw.oa.auth.business.dict.service.ISysDictService;
import com.ydw.oa.auth.util.tree.TreeDictTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-09
 */
@Api(description = "字典值管理")
@RestController
@RequestMapping("/cp/sys-dict")
public class SysDictController {

	@Autowired
	private ISysDictService iSysDictService;

	@ApiOperation(value = "字典值树列表")
	@PostMapping("/list")
	public Wrapper<List<SysDict>> list(@RequestBody(required = false) DictQuery<SysDict> usrQuery) {
		List<SysDict> list = iSysDictService.list(usrQuery.makeQueryWrapper());
		return WrapMapper.ok(TreeDictTools.listToTree(list));
	}

	@ApiOperation(value = "添加字典值")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody SysDict sysDictDto) {
		iSysDictService.AddAndCreatCode(sysDictDto);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除字典值")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iSysDictService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "获取字典值数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<SysDict> editForm(String objectId) {
		SysDict sysDictPo = iSysDictService.getById(objectId);
		return WrapMapper.ok(sysDictPo);
	}

	@ApiOperation(value = "编辑字典值")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody SysDict sysDict) {
		SysDict sysDictPo = iSysDictService.getById(sysDict.getObjectId());
		sysDictPo.setParentId(sysDict.getParentId());
		sysDictPo.setValue(sysDict.getValue());
		sysDictPo.setNote(sysDict.getNote());
		sysDictPo.setDescription(sysDict.getDescription());
		iSysDictService.saveOrUpdate(sysDictPo);

		return WrapMapper.ok("保存成功");
	}
}
