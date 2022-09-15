package com.ydw.oa.wkflow.listener;

import java.io.Serializable;

import org.activiti.engine.HistoryService;
import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.ydw.oa.wkflow.util.SpringContextUtil;

@Component
public class GlobalActivitiEventListener implements ActivitiEventListener, Serializable {

	private static final long serialVersionUID = 1L;

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	// 事件及事件的处理器
	@Override
	public void onEvent(ActivitiEvent event) {
		switch (event.getType()) {
		case ACTIVITY_STARTED:
			break;
		case TASK_CREATED:
			// 节点开始的时候,如果和上个执行人是同一个人,则自动关闭节点
			sendSms(event);
			break;
		case TASK_ASSIGNED:
			break;
		case TASK_COMPLETED:
			break;
		case ACTIVITY_COMPLETED:
			break;
		default:
			break;
		}
	}

	// 拦截发送短信
	private void sendSms(ActivitiEvent event) {
		HistoryService historyService = SpringContextUtil.getBean(HistoryService.class);
	}

	@Override
	public boolean isFailOnException() {
		return false;
	}
}
