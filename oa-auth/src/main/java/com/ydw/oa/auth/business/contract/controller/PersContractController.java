package com.ydw.oa.auth.business.contract.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.contract.dto.PersContractQuery;
import com.ydw.oa.auth.business.contract.entity.PersContract;
import com.ydw.oa.auth.business.contract.service.IPersContractService;

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
@Api(description = "人事档案管理及劳动合同管理管理")
@RestController
@RequestMapping("/cp/contract")
public class PersContractController {

	@Autowired
	private IPersContractService persContractService;

	@ApiOperation(value = "合同管理")
	@PostMapping("/list")
	public Wrapper<IPage<PersContract>> list(@RequestBody PersContractQuery<PersContract> query) {

		IPage<PersContract> page = persContractService.page(query, query.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加合同")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody PersContract persContract) {
		persContractService.save(persContract);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除合同")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		persContractService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取合同数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<PersContract> editForm(String objectId) {
		PersContract persContractPo = persContractService.getById(objectId);
		return WrapMapper.ok(persContractPo);
	}

	@ApiOperation(value = "编辑合同")
	@PostMapping("/edit")
	public Wrapper<String> edit(@ApiParam @Valid @RequestBody PersContract persContractDto) {
		PersContract persContractPo = persContractService.getById(persContractDto.getObjectId());
		persContractPo.setEndTime(persContractDto.getEndTime());
		persContractPo.setFileId(persContractDto.getFileId());
		persContractPo.setIsRemind(persContractDto.getIsRemind());
		persContractPo.setName(persContractDto.getName());
		persContractPo.setType(persContractDto.getType());
		persContractService.saveOrUpdate(persContractPo);
		return WrapMapper.ok("保存成功");
	}
}
