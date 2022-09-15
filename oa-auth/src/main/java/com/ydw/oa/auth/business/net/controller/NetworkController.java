package com.ydw.oa.auth.business.net.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.net.dto.NetworkQuery;
import com.ydw.oa.auth.business.net.entity.Network;
import com.ydw.oa.auth.business.net.service.INetworkService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "链接管理")
@RestController
@RequestMapping("/cp/network")
public class NetworkController {

	@Autowired
	private INetworkService networkService;

	@ApiOperation(value = "链接列表")
	@PostMapping("/list")
	public Wrapper<IPage<Network>> list(@RequestBody NetworkQuery<Network> networkQuery) {
		IPage<Network> page = networkService.page(networkQuery, networkQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加链接")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody Network networkDto) {
		networkService.save(networkDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取链接数据")
	@GetMapping("/edit_form")
	public Wrapper<Network> editForm(String objectId) {
		Network networkPo = networkService.getById(objectId);
		return WrapMapper.ok(networkPo);
	}

	@ApiOperation(value = "修改链接")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody Network networkDto) {
		Network networkPo = networkService.getById(networkDto.getObjectId());
		networkPo.setIcon(networkDto.getIcon());
		networkPo.setName(networkDto.getName());
		networkPo.setUrl(networkDto.getUrl());
		networkService.saveOrUpdate(networkPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除链接数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		networkService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}

