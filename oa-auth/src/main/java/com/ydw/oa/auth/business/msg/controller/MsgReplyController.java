package com.ydw.oa.auth.business.msg.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.msg.entity.MsgReply;
import com.ydw.oa.auth.business.msg.service.IMsgReplyService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-09-14
 */
@Api(description = "回复公告")
@RestController
@RequestMapping("/cp/msg-reply")
public class MsgReplyController {

	@Autowired
	private IMsgReplyService msgReplyService;

	@ApiOperation(value = "收件人回复列表")
	@GetMapping("/list_reply")
	public Wrapper<List<MsgReply>> list_reply(String inboxId) {
		QueryWrapper<MsgReply> qw = new QueryWrapper<>();
		qw.eq("INBOX_ID", inboxId);
		qw.orderByAsc("CREATE_TIME");
		List<MsgReply> list = msgReplyService.list(qw);
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "收件人回复")
	@PostMapping("/reply")
	public Wrapper<String> reply(@RequestBody MsgReply msgReply) {
		msgReplyService.reply(msgReply);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "发件人审批回复")
	@PostMapping("/review")
	public Wrapper<String> review(@RequestBody MsgReply msgReplyDto) {
		msgReplyService.review(msgReplyDto);
		return WrapMapper.ok();
	}
}
