package com.ydw.oa.auth.business.msg.service;

import com.ydw.oa.auth.business.msg.entity.MsgReply;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hxj
 * @since 2020-09-14
 */
public interface IMsgReplyService extends IService<MsgReply> {

	void reply(MsgReply msgReply);

	void review(MsgReply msgReplyDto);

}
