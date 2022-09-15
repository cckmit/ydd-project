package com.ydw.oa.auth.business.vote.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.vote.dto.VoteLogQuery;
import com.ydw.oa.auth.business.vote.entity.VoteLog;
import com.ydw.oa.auth.business.vote.service.IVoteLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-07-01
 */
@Api(description = "投票管理")
@RestController
@RequestMapping("/cp/vote-log")
public class VoteLogController {

	@Autowired
	private IVoteLogService voteLogService;

	@ApiOperation(value = "投票列表")
	@PostMapping("/list")
	public Wrapper<IPage<VoteLog>> list(@RequestBody VoteLogQuery<VoteLog> voteLogQuery) {
		IPage<VoteLog> page = voteLogService.page(voteLogQuery, voteLogQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加投票")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody VoteLog voteLogDto) {
		voteLogService.save(voteLogDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取投票数据")
	@GetMapping("/edit_form")
	public Wrapper<VoteLog> editForm(String objectId) {
		VoteLog voteLogPo = voteLogService.getById(objectId);
		return WrapMapper.ok(voteLogPo);
	}

	@ApiOperation(value = "修改投票")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody VoteLog voteLogDto) {
		VoteLog voteLogPo = voteLogService.getById(voteLogDto.getObjectId());
		voteLogPo.setFormId(voteLogDto.getFormId());
		voteLogPo.setName(voteLogDto.getName());
		voteLogPo.setNote(voteLogDto.getNote());
		voteLogPo.setSendUsrId(voteLogDto.getSendUsrId());
		voteLogPo.setVoteNo(voteLogDto.getVoteNo());
		voteLogService.saveOrUpdate(voteLogPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除投票数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		voteLogService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "发送投票任务")
	@GetMapping("/send_task")
	public Wrapper<String> send_task(String objectId) {
		VoteLog voteLogPo = voteLogService.getById(objectId);
		voteLogPo.setIsSend("是");
		voteLogService.saveOrUpdate(voteLogPo);
		return WrapMapper.ok("删除成功");
	}
}
