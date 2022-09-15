package com.ydw.oa.auth.business.msg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
public interface IMsgOutboxService extends IService<MsgOutbox> {

	/**
	 * 发送邮件
	 * @param outboxDto
	 */
	void send(MsgOutbox outboxDto);

	// 删除公告
	void delete(String objectId);

	// 批量删除公告
	void mutiDelete(String kids);

}
