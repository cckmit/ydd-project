package com.ydw.oa.auth.business.msg.controller;


import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.msg.dto.MsgInboxQuery;
import com.ydw.oa.auth.business.msg.entity.MsgInbox;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;
import com.ydw.oa.auth.business.msg.mapper.MsgInboxMapper;
import com.ydw.oa.auth.business.msg.service.IMsgInboxService;
import com.ydw.oa.auth.business.msg.service.IMsgOutboxService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Api(description = "接收公告")
@RestController
@RequestMapping("/cp/inbox")
public class MsgInboxController {

	@Autowired
	private MsgInboxMapper inboxMapper;
	@Autowired
	private IMsgInboxService inboxService;
	@Autowired
	private IMsgOutboxService outboxService;
	
	@ApiOperation(value = "我的收件箱列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String, Object>>>> list(@RequestBody MsgInboxQuery<MsgInbox> inboxQuery) {
		IPage<List<Map<String, Object>>> list = inboxMapper.query(inboxQuery, inboxQuery.makeQueryWrapper());
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "查看收件箱数据")
	@GetMapping("/edit_form")
	public Wrapper<JSONObject> editForm(String objectId) {
		MsgInbox inbox = inboxService.getById(objectId);
		if(!"已阅读".equals(inbox.getIsRead())){
			inbox.setIsRead("已阅读");
			inbox.setReadTime(new Date());
			inboxService.saveOrUpdate(inbox);
		}

		MsgOutbox outbox = outboxService.getById(inbox.getOutboxId());
		
		JSONObject result = new JSONObject();
		result.put("outbox", outbox);
		result.put("inbox", inbox);
		return WrapMapper.ok(result);
	}
	
	@ApiOperation(value = "我的收件箱列表_未回复")
	@GetMapping("/list_unreply")
	public Wrapper<List<Map<String, Object>>> list_unreply() {
		QueryWrapper<MsgInbox> qw = new QueryWrapper<MsgInbox>();
		qw.eq("t.IS_DELETED", 0);
		qw.eq("t.USR_ID", SessionTool.getSessionAdminId());
		qw.eq("t.STATUZ", "未回复");
		qw.orderByAsc("t.CREATE_TIME");
		List<Map<String, Object>> list = inboxMapper.selectListByStatuz(qw);
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "我的收件箱列表_已驳回")
	@GetMapping("/list_reject")
	public Wrapper<List<Map<String, Object>>> list_reject() {
		QueryWrapper<MsgInbox> qw = new QueryWrapper<MsgInbox>();
		qw.eq("t.IS_DELETED", 0);
		qw.eq("t.USR_ID", SessionTool.getSessionAdminId());
		qw.eq("t.STATUZ", "已驳回");
		qw.orderByAsc("t.CREATE_TIME");
		List<Map<String, Object>> list = inboxMapper.selectListByStatuz(qw);
		return WrapMapper.ok(list);
	}


}

