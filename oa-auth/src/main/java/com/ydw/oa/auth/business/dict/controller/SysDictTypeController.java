package com.ydw.oa.auth.business.dict.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.dict.dto.DictTypeQuery;
import com.ydw.oa.auth.business.dict.entity.SysDictType;
import com.ydw.oa.auth.business.dict.service.ISysDictTypeService;
import com.ydw.oa.auth.util.CodeTools;

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
@Api(description = "字典类型管理")
@RestController
@RequestMapping("/cp/sys-dict-type")
public class SysDictTypeController {

	@Autowired
	private ISysDictTypeService iSysDictTypeService;

	@Autowired
	private CodeTools dodeTools;

	@ApiOperation(value = "字典类型列表")
	@GetMapping("/list")
	public Wrapper<List<SysDictType>> list() {
		QueryWrapper<SysDictType> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "SHOW_ORDERS");
		List<SysDictType> list = iSysDictTypeService.list(queryWrapper);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "字典类型分页列表")
	@GetMapping("/page")
	public Wrapper<IPage<SysDictType>> page(DictTypeQuery<SysDictType> dictQuery) {
		
		IPage<SysDictType> page = iSysDictTypeService.page(dictQuery,dictQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加字典类型")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody SysDictType sysDictType) {
		String code = dodeTools.getNextCode("SYS_DICT_TYPE", "CODE");
		sysDictType.setCode(code);
		iSysDictTypeService.save(sysDictType);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除字典类型")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iSysDictTypeService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "获取字典类型数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<SysDictType> editForm(String objectId) {
		SysDictType opFunc = iSysDictTypeService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑字典类型")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody SysDictType dysDictTypeDTO) {
		SysDictType sysdicttypePo = iSysDictTypeService.getById(dysDictTypeDTO.getObjectId());
		sysdicttypePo.setName(dysDictTypeDTO.getName());
		sysdicttypePo.setNote(dysDictTypeDTO.getNote());
		sysdicttypePo.setIcon(dysDictTypeDTO.getIcon());
		sysdicttypePo.setShowOrders(dysDictTypeDTO.getShowOrders());
		// deptPo.setParentId(parentId);
		iSysDictTypeService.saveOrUpdate(sysdicttypePo);
		return WrapMapper.ok("保存成功");
	}
}
