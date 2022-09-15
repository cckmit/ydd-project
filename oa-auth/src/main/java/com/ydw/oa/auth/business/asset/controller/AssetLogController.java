package com.ydw.oa.auth.business.asset.controller;

import java.util.Map;

import com.ydw.oa.auth.business.asset.dto.AssetLogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.asset.entity.AssetLog;
import com.ydw.oa.auth.business.asset.mapper.AssetLogMapper;
import com.ydw.oa.auth.business.wk.dto.WkQuery;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
@Api(description = "资产管理记录管理")
@RestController
@RequestMapping("/cp/asset-log")
public class AssetLogController {

	@Autowired
	private AssetLogMapper assetLogMapper;

	@ApiOperation(value = "资产管理")
	@PostMapping("/list")
	public Wrapper<IPage<Map<String, Object>>> list(@RequestBody AssetLogQuery<AssetLog> query) {
		IPage<Map<String, Object>> page = assetLogMapper.query(query, query.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

}
