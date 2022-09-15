package com.ydw.oa.auth.business.msg.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
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
import com.ydw.oa.auth.business.msg.dto.MsgInboxDto;
import com.ydw.oa.auth.business.msg.dto.MsgOutboxQuery;
import com.ydw.oa.auth.business.msg.entity.MsgInbox;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;
import com.ydw.oa.auth.business.msg.mapper.MsgInboxMapper;
import com.ydw.oa.auth.business.msg.mapper.MsgOutboxMapper;
import com.ydw.oa.auth.business.msg.service.IMsgOutboxService;
import com.ydw.oa.auth.business.msg.service.IMsgInboxService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Api(description = "发送公告")
@RestController
@RequestMapping("/cp/outbox")
public class MsgOutboxController {

	@Autowired
	private IMsgOutboxService outboxService;
	@Autowired
	private IMsgInboxService inboxService;
	@Autowired
	private MsgOutboxMapper outboxMapper;
	@Autowired
	private MsgInboxMapper inboxMapper;

	@ApiOperation(value = "我的发件箱列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String, Object>>>> list(@RequestBody MsgOutboxQuery<MsgOutbox> outboxQuery) {
		String adminId = SessionTool.getSessionAdminId();
		outboxQuery.setSendUsrId(adminId);
		IPage<List<Map<String, Object>>> list = outboxMapper.query(outboxQuery, outboxQuery.makeQueryWrapper());
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "查看发件箱数据")
	@GetMapping("/edit_form")
	public Wrapper<MsgOutbox> editForm(String objectId) {
		MsgOutbox outbox = outboxService.getById(objectId);
		return WrapMapper.ok(outbox);
	}

	@ApiOperation(value = "修改公告标题和内容以及附件")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody MsgOutbox outboxDto) {
		MsgOutbox outbox = outboxService.getById(outboxDto.getObjectId());
		outbox.setContent(outboxDto.getContent());
		outbox.setTitle(outboxDto.getTitle());
		outbox.setFileId(outboxDto.getFileId());
//		outbox.setStartTime(outboxDto.getStartTime());
		outbox.setEndTime(outboxDto.getEndTime());

		outboxService.saveOrUpdate(outbox);

		MsgInbox inbox = new MsgInbox();
       	inbox = inboxService.getById(inbox.getObjectId());
       	try{
        if("已阅读".equals(inbox.getIsRead())){
            inbox.setIsRead("未阅读");
            inbox.setReadTime(new Date());
            inboxService.saveOrUpdate(inbox);
        }else{

		}
       	}catch (Exception e){

		}
		return WrapMapper.ok("发送成功");
	}
	
	@ApiOperation(value = "邮件的收件人列表")
	@PostMapping("/list_inbox")
	public Wrapper<IPage<List<Map<String, Object>>>> list_inbox(@RequestBody MsgInboxDto<MsgInbox> msgIboxDto) {
		IPage<List<Map<String, Object>>> list = inboxMapper.listInboxs(msgIboxDto,msgIboxDto.makeQueryWrapper());
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "删除发件箱数据")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		outboxService.delete(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "批量删除")
	@GetMapping("/batchdelete")
	public Wrapper<String> batchdelete(String kids) {
		outboxService.mutiDelete(kids);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "新建公告，普通or政务公开")
	@PostMapping("/send")
	public Wrapper<String> send(@RequestBody MsgOutbox outboxDto) {
		outboxService.send(outboxDto);
		return WrapMapper.ok("发送成功");
	}
	
	@ApiOperation(value = "政务公开栏列表")
	@PostMapping("/notice_list")
	public Wrapper<IPage<List<Map<String, Object>>>> notice_list(@RequestBody MsgOutboxQuery<MsgOutbox> outboxQuery) {
		outboxQuery.setNoticeType("政务公开");
		if (ChkUtil.isNull(outboxQuery.getAffairsEndTime())) {
			outboxQuery.setAffairsEndTime(new Date());
		}
		if (ChkUtil.isNull(outboxQuery.getAffairsStartTime())) {
			outboxQuery.setAffairsStartTime(new Date());
		}
		IPage<List<Map<String, Object>>> list = outboxMapper.query(outboxQuery, outboxQuery.makeQueryWrapper());
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "政务公开栏列表-最新的3个")
	@GetMapping("/newest_notice_list")
	public Wrapper<List<Map<String, Object>>> newest_notice_list() {
		QueryWrapper<MsgOutbox> qw = new QueryWrapper<MsgOutbox>();
		qw.eq("NOTICE_TYPE", "政务公开");
		qw.le("START_TIME", new Date());
		qw.ge("END_TIME", new Date());
		qw.orderByDesc("CREATE_TIME");
		qw.last("limit 3");
		List<Map<String, Object>> list = outboxService.listMaps(qw);
		return WrapMapper.ok(list);
	}
}
