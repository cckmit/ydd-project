package com.ydw.oa.wkflow.business_main.reject.service.impl;

import java.util.Date;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.mapper.HiDatasMapper;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;
import com.ydw.oa.wkflow.business_main.reject.mapper.RejectMapper;
import com.ydw.oa.wkflow.business_main.reject.service.IRejectService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.SessionTool;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-11-16
 */
@Service
public class RejectServiceImpl extends ServiceImpl<RejectMapper, Reject> implements IRejectService {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private FormValsService formValsService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private IDatasService datasService;

	@Autowired
	private HiDatasMapper hiDatasMapper;

	@Override
	public void addData(Datas firstData, Task currentTask, String type, String reason,String rejectUserId) {
		// TODO 生成驳回列表
		Reject reject = new Reject();
		reject.setPid(currentTask.getProcessInstanceId());
		reject.setReason(reason);
		reject.setRejectTime(new Date());
		reject.setRejectType(type);
		reject.setRejectUsrId(rejectUserId);
		reject.setStartTime(firstData.getCreateTime());
		reject.setStartUsrId(firstData.getAssigner());
		reject.setTaskId(currentTask.getId());
		this.save(reject);

	}

	@Override
	@Transactional
	public void revoke(String pid) {
		// TODO 撤销任务
		Datas datas = formValsService.getFirstForm(pid);
		// 将整个流程终止，无需填写驳回原因
		runtimeService.deleteProcessInstance(pid, "手动撤销");
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		datasService.remove(queryWrapper);
		// 保存驳回数据
		Reject reject = new Reject();
		reject.setPid(pid);
		reject.setRejectTime(new Date());
		reject.setRejectType("撤销");
		reject.setRejectUsrId(SessionTool.getSessionAdminId());
		reject.setReason("用户手动撤销");
		reject.setStartTime(datas.getCreateTime());
		reject.setStartUsrId(datas.getAssigner());
		this.save(reject);

	}

	@Override
	public boolean checkReject(String pid) {
		// TODO 判断流程是否驳回
		QueryWrapper<Reject> qw = new QueryWrapper<Reject>();
		qw.eq("PID", pid);
		int count = this.count(qw);
		if (count > 0) {
			return true;
		}
		return false;
	}

}
