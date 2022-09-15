package com.ydw.oa.auth.business.msg.service.impl;

import java.util.*;

import com.ydw.oa.auth.util.activiti.ReviewTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.msg.entity.MsgInbox;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;
import com.ydw.oa.auth.business.msg.mapper.MsgOutboxMapper;
import com.ydw.oa.auth.business.msg.service.IMsgInboxService;
import com.ydw.oa.auth.business.msg.service.IMsgOutboxService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.SessionTool;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@Service
public class MsgOutboxServiceImpl extends ServiceImpl<MsgOutboxMapper, MsgOutbox> implements IMsgOutboxService {

	@Autowired
	private IMsgInboxService inboxService;
	@Autowired
	private IAuUsrService auUsrService;

	@Override
	@Transactional
	public void send(MsgOutbox outboxDto) {
		// TODO 发送邮件
		outboxDto.setSendUsrId(SessionTool.getSessionAdminId());
		outboxDto.setNoticeType(outboxDto.getNoticeType());
		outboxDto.setStartTime(new Date());
		this.save(outboxDto);
		AuUsr sendUsr = auUsrService.getById(outboxDto.getSendUsrId());
		if ("政务公开".equals(outboxDto.getNoticeType())) {
			QueryWrapper<AuUsr> qw = new QueryWrapper<AuUsr>();
			qw.select("OBJECT_ID","WX_USER_ID");
			List<AuUsr> list = auUsrService.list(qw);
			Set<String> touser = new HashSet<>();
			for (AuUsr auUsr : list) {
				if (!auUsr.getObjectId().equals(SessionTool.getSessionAdminId())) {
					String wxUserId = auUsr.getWxUserId();
					if (ChkUtil.isNotNull(wxUserId)) {
						touser.add(wxUserId);
					}
				}
			}
			ReviewTool.notice(String.join("|", touser),sendUsr.getRealName(),outboxDto,"不回复");
			return;
		}
		String usrIds = outboxDto.getReceiveUsrId();
		Set<String> touser = new HashSet<>();
		if ("集团".equals(outboxDto.getReceiveType())) {
			outboxDto.setReceiveUsrId("");
			QueryWrapper<AuUsr> qw = new QueryWrapper<AuUsr>();
			qw.select("OBJECT_ID","WX_USER_ID");
			List<AuUsr> list = auUsrService.list(qw);

			for (AuUsr auUsr : list) {
				if (!auUsr.getObjectId().equals(SessionTool.getSessionAdminId())) {
					usrIds += "," + auUsr.getObjectId();
					String wxUserId = auUsr.getWxUserId();
					if (ChkUtil.isNotNull(wxUserId)) {
						touser.add(wxUserId);
					}
				}
			}
			usrIds = ChkUtil.isNull(usrIds) ? "" : usrIds.replaceFirst(",", "");
		}

		String[] receive_user_ids = usrIds.split(",");

		for (String usrId : receive_user_ids) {
			if (ChkUtil.isNotNull(usrId)) {
				if (!"集团".equals(outboxDto.getReceiveType())) {
					AuUsr reciveUsr = auUsrService.getById(usrId);
					if (ChkUtil.isNotNull(reciveUsr.getWxUserId())) {
						touser.add(reciveUsr.getWxUserId());
					}
				}
				MsgInbox inbox = new MsgInbox();
				inbox.setUsrId(usrId);
				inbox.setIsRead("未阅读");
				inbox.setOutboxId(outboxDto.getObjectId());
				if ("1".equals(outboxDto.getNeedReply())) {
					inbox.setStatuz("未回复");
				}
				inboxService.save(inbox);
			}
		}
		ReviewTool.notice(String.join("|", touser),sendUsr.getRealName(),outboxDto,outboxDto.getNeedReply().equals("0")?"需回复":"无需回复");
	}

	@Override
	@Transactional
	public void delete(String objectId) {
		// TODO 删除公告
		this.removeById(objectId);
		QueryWrapper<MsgInbox> qw = new QueryWrapper<MsgInbox>();
		qw.eq("OUTBOX_ID", objectId);
		inboxService.remove(qw);
	}

	@Override
	@Transactional
	public void mutiDelete(String kids) {
		// TODO 批量删除公告
		List<String> list = Arrays.asList(kids.split(","));
		this.removeByIds(list);
		QueryWrapper<MsgInbox> qw = new QueryWrapper<MsgInbox>();
		qw.in("OUTBOX_ID", list);
		inboxService.remove(qw);
	}

}
