package com.ydw.oa.auth.business.msg.service.impl;

import com.ydw.oa.auth.business.msg.entity.MsgInbox;
import com.ydw.oa.auth.business.msg.entity.MsgReply;
import com.ydw.oa.auth.business.msg.mapper.MsgReplyMapper;
import com.ydw.oa.auth.business.msg.service.IMsgInboxService;
import com.ydw.oa.auth.business.msg.service.IMsgReplyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-09-14
 */
@Service
public class MsgReplyServiceImpl extends ServiceImpl<MsgReplyMapper, MsgReply> implements IMsgReplyService {

	@Autowired
	private IMsgInboxService inboxService;
	
	@Override
	@Transactional
	public void reply(MsgReply msgReply) {
		// TODO 收件人回复
		this.save(msgReply);
		
		MsgInbox inbox = inboxService.getById(msgReply.getInboxId());
		inbox.setStatuz("已回复");
		inboxService.saveOrUpdate(inbox);
	}

	@Override
	@Transactional
	public void review(MsgReply msgReplyDto) {
		// TODO 发件人审批回复
		MsgReply msgReplyPo = this.getById(msgReplyDto.getObjectId());
		msgReplyPo.setStatuz(msgReplyDto.getStatuz());
		msgReplyPo.setReason(msgReplyDto.getReason());
		this.saveOrUpdate(msgReplyPo);
		
		MsgInbox inbox = inboxService.getById(msgReplyPo.getInboxId());
		if("通过".equals(msgReplyDto.getStatuz())) {
			inbox.setStatuz("已完成");
		}else {
			inbox.setStatuz("已驳回");
		}
		inboxService.saveOrUpdate(inbox);
	}
}
