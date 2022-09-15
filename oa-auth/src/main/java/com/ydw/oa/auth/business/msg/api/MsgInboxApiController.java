package com.ydw.oa.auth.business.msg.api;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.msg.entity.MsgInbox;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;
import com.ydw.oa.auth.business.msg.service.IMsgInboxService;
import com.ydw.oa.auth.business.msg.service.IMsgOutboxService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.DateTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-06-30
 */
@Api(description = "公告管理api")
@RestController
@RequestMapping("/api/notice")
public class MsgInboxApiController {
	@Autowired
	private IMsgInboxService inboxService;
	@Autowired
	private IMsgOutboxService outboxService;

	@ApiOperation(value = "查询公告数据")
	@ApiImplicitParam(name = "msgOutboxId", value = "主键id")
	@GetMapping("/get")
	public Wrapper<JSONObject> get(String msgOutboxId,String usrId) {
		JSONObject jsonObject = new JSONObject();
		if(ChkUtil.isNotNull(usrId)){
			QueryWrapper<MsgInbox> msgInboxQueryWrapper = new QueryWrapper<>();
			msgInboxQueryWrapper.eq("USR_ID", usrId);
			msgInboxQueryWrapper.eq("OUTBOX_ID", msgOutboxId);
			MsgInbox inbox = inboxService.getOne(msgInboxQueryWrapper);
			if(ChkUtil.isNotNull(inbox) && !"已阅读".equals(inbox.getIsRead())){
				inbox.setIsRead("已阅读");
				inbox.setReadTime(new Date());
				inboxService.saveOrUpdate(inbox);
			}
		}
		MsgOutbox outbox = outboxService.getById(msgOutboxId);

		Date endTime = outbox.getEndTime();
		Date now = new Date();
		boolean isTimeout = false;
		if (ChkUtil.isNotNull(endTime) && now.getTime() > endTime.getTime()) {
			isTimeout = true;
		}
		jsonObject.put("outbox",outbox);
		jsonObject.put("isTimeout",isTimeout);
		return WrapMapper.ok(jsonObject);
	}

}
