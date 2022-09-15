package com.ydw.oa.wkflow.business_activiti.service;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.entity.HiDatas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.datas.service.IHiDatasService;

/**
 * TODO 任务回退
 * 
 * @author Administrator
 *
 */
@Service
public class TaskRollBackService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private HistoryService historyService;
	@Autowired
	private IDatasService datasService;
	@Autowired
	private IHiDatasService hiDatasService;

	@Transactional
	public String rollBack(String taskId, String backTaskId) {
		// TODO 流程回退
		logger.info("任务流程回退{}-->{}", taskId, backTaskId);
		HistoricTaskInstance preTask = historyService.createHistoricTaskInstanceQuery().taskId(backTaskId)
				.singleResult();
		if (ChkUtil.isNull(preTask)) {
			return "任务不存在";
		}
		// 获取任务提交信息
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", preTask.getProcessInstanceId());
		queryWrapper.eq("ACTI_TASK_ID", preTask.getId());
		Datas datas = datasService.getOne(queryWrapper);
		QueryWrapper<HiDatas> hiQueryWrapper = new QueryWrapper<>();
		hiQueryWrapper.eq("DATAS_ID", datas.getObjectId());
		HiDatas hiDatas = hiDatasService.getOne(hiQueryWrapper);
		// 删除工作流历史记录数据
		hiDatasService.rollBack(hiDatas, preTask.getProcessInstanceId(), taskId);
		// 删除任务记录数据
		datasService.rollBack(datas);
		return "回退成功";
	}

}
