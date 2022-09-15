package com.ydw.oa.auth.business.asset.controller;

import java.util.Map;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONObject;
import com.ydw.oa.auth.business.asset.entity.AssetType;
import com.ydw.oa.auth.business.asset.service.IAssetTypeService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.asset.dto.AssetContentQuery;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import com.ydw.oa.auth.business.asset.mapper.AssetContentMapper;
import com.ydw.oa.auth.business.asset.service.IAssetContentService;

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
 * @since 2020-06-29
 */
@Api(description = "资产管理")
@RestController
@RequestMapping("/cp/asset-content")
public class AssetContentController {

	@Autowired
	private IAssetContentService assetContentService;
	@Autowired
	private AssetContentMapper assetContentMapper;
	@Autowired
	private IAssetTypeService assetTypeService;
	@Autowired
	private IAuUsrService auUsrService;
	@Autowired
	private IAuUsrDeptService auUsrDeptService;

	@ApiOperation(value = "资产管理")
	@PostMapping("/list")
	public Wrapper<IPage<Map<String, Object>>> list(@RequestBody AssetContentQuery<AssetContent> query) {

		IPage<Map<String, Object>> page = assetContentMapper.query(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "新增固定资产")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody AssetContent assetContent) {
		assetContentService.add(assetContent);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除资产")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		String result = assetContentService.delete(objectId);
		return WrapMapper.ok(result);
	}

	@ApiOperation(value = "编辑获取资产数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<JSONObject> editForm(String objectId) {
		JSONObject jsonObject = new JSONObject();
		AssetContent assetContent = assetContentService.getById(objectId);
		AssetType assetType = assetTypeService.getById(assetContent.getTypeId());

//		AuUsr auUsr = auUsrService.getById(assetContent.getBuyUsr());
//		AuUsrDept auUsrDept = auUsrDeptService.getById(assetContent.getCurrentUsrDeptId());

		jsonObject.put("assetContent",assetContent);
		jsonObject.put("assetType",assetType);
//		jsonObject.put("auUsr",auUsr);
//		jsonObject.put("currentUsrDept",auUsrDept);
		return WrapMapper.ok(jsonObject);
	}

	@ApiOperation(value = "编辑资产")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody AssetContent assetContentDto) {
		AssetContent assetContentPo = assetContentService.getById(assetContentDto.getObjectId());
		assetContentPo.setCode(assetContentDto.getCode());
		assetContentPo.setName(assetContentDto.getName());
		assetContentPo.setTypeId(assetContentDto.getTypeId());
		assetContentPo.setBuyTime(assetContentDto.getBuyTime());
//		assetContentPo.setBuyUsr(assetContentDto.getBuyUsr());
		assetContentPo.setNumber(assetContentDto.getNumber());
		assetContentPo.setNote(assetContentDto.getNote());
		assetContentService.saveOrUpdate(assetContentPo);
		return WrapMapper.ok("修改成功");
	}

	@ApiOperation(value = "资产分配")
	@GetMapping("/distribution")
	public Wrapper<String> distribution(String objectId, String deptId, @RequestParam(value = "usrId",required = false) String usrId) {
		assetContentService.distribution(objectId, deptId, usrId);
		return WrapMapper.ok("分配成功");
	}

	@ApiOperation(value = "资产回收")
	@GetMapping("/recovery")
	public Wrapper<String> recovery(String objectId) {
		assetContentService.recovery(objectId);
		return WrapMapper.ok("回收成功");
	}

	@ApiOperation(value = "资产报废")
	@GetMapping("/scrap")
	public Wrapper<String> scrap(String objectId) {
		assetContentService.scrap(objectId);
		return WrapMapper.ok("报废成功");
	}

	@ApiOperation(value = "资产再报废")
	@GetMapping("/scrap_again")
	public Wrapper<String> scrap_again(String objectId) {
		assetContentService.scrapAgain(objectId);
		return WrapMapper.ok("报废结束");
	}
}
