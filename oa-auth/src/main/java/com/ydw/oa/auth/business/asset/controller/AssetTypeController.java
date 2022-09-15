package com.ydw.oa.auth.business.asset.controller;


import java.util.List;
import java.util.Map;

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
import com.ydw.oa.auth.business.asset.entity.AssetType;
import com.ydw.oa.auth.business.asset.service.IAssetTypeService;
import com.ydw.oa.auth.util.tree.TreeElTools;

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
 * @since 2020-06-29
 */
@Api(description = "资产分类管理")
@RestController
@RequestMapping("/cp/asset-type")
public class AssetTypeController {

	@Autowired
	private IAssetTypeService assetTypeService;
	
	@ApiOperation(value = "资产分类Tree")
	@GetMapping("/tree")
	public Wrapper<List<Map<String, Object>>> tree() {
		QueryWrapper<AssetType> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("CREATE_TIME","PARENT_ID");
		List<Map<String, Object>> list = assetTypeService.listMaps(queryWrapper);
		return WrapMapper.ok(TreeElTools.listMapToTree(list, "NAME", "PARENT_ID"));
	}

	@ApiOperation(value = "添加资产分类")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody AssetType assetType) {
		assetTypeService.save(assetType);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除资产分类")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		assetTypeService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
	
	@ApiOperation(value = "编辑获取资产分类数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<AssetType> editForm(String objectId) {
		AssetType assetTypePo = assetTypeService.getById(objectId);
		return WrapMapper.ok(assetTypePo);
	}

	@ApiOperation(value = "编辑资产分类")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody AssetType assetTypeDto) {
		AssetType assetTypePo = assetTypeService.getById(assetTypeDto.getObjectId());
		assetTypePo.setName(assetTypeDto.getName());
		assetTypePo.setNote(assetTypeDto.getNote());
		assetTypePo.setParentId(assetTypeDto.getParentId());
		assetTypePo.setCode(assetTypeDto.getCode());
		assetTypeService.saveOrUpdate(assetTypePo);
		return WrapMapper.ok("保存成功");
	}
	
	@ApiOperation(value = "资产分类")
	@GetMapping("/list")
	public Wrapper<List<Map<String, Object>>> list() {
		QueryWrapper<AssetType> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("NAME","CREATE_TIME");
		List<Map<String, Object>> list = assetTypeService.listMaps(queryWrapper);
		return WrapMapper.ok(list);
	}
}

