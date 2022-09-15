package com.ydw.oa.wkflow.business_main.reject.service;

import org.activiti.engine.task.Task;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-11-16
 */
public interface IRejectService extends IService<Reject> {

	void addData(Datas firstData, Task currentTask, String type, String reason,String rejectUserId);

	void revoke(String pid);

	boolean checkReject(String pid);

}
