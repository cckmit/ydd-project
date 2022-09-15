package com.ydw.oa.wkflow.business_main.datas.service.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.mapper.DatasMapper;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.date.DateTools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Service
public class DatasServiceImpl extends ServiceImpl<DatasMapper, Datas> implements IDatasService {

	@Autowired
	private DatasMapper datasMapper;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private FormValsService formValsService;
	@Autowired
	private AuthFeignService authFeignService;

	@Transactional
	@Override
	public void deleteOverTimeTasks() {
		// TODO 删除超过24小时的未做任务
		// 查找超时未处理的流程实例
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		Timestamp now = new Timestamp(System.currentTimeMillis());
		queryWrapper.isNull("ahp.END_TIME_");
		queryWrapper.between("ahp.START_TIME_", DateTools.addDay(now, -2), DateTools.addDay(now, -1));
		queryWrapper.isNull("td.OBJECT_ID");
		List<Map<String, Object>> list = datasMapper.selectOverTimeUndoPids(queryWrapper);
		for (Map<String, Object> map : list) {
			String processInstanceId = map.get("processInstanceId") + "";
			// 删除流程
			runtimeService.deleteProcessInstance(processInstanceId, "用户撤销");
			// 删除历史审批
			historyService.deleteHistoricProcessInstance(processInstanceId);
		}

	}

	@Transactional
	@Override
	public void rollBack(Datas datas) {
		// TODO 删除任务记录数据
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", datas.getActiProcInstId());
		queryWrapper.ge("CREATE_TIME", datas.getCreateTime());
		this.remove(queryWrapper);
	}

	@Override
	public JSONObject getFormData(String pid) {
		// TODO 获取表单数据
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", pid);
		JSONObject result = new JSONObject();
		List<Map<String, Object>> allBeforFormsList = this.listMaps(queryWrapper);
		for (Map<String, Object> map : allBeforFormsList) {
			JSONObject object = JsonUtil.jsonStrToJsonObject(map.get("FORM_VALS_JSON") + "");
			result.putAll(object);
		}
		Datas firstFormVals = formValsService.getFirstForm(pid);
		if(ChkUtil.isNull(firstFormVals)) {
			return null;
		}
		Map<String, Object> user = authFeignService.getOne(firstFormVals.getAssigner()).getResult();
		result.put("pid", pid);
		result.put("assigner", user.get("REAL_NAME"));
		result.put("createTime", DateTools.formatDateCn(firstFormVals.getCreateTime()));
		return result;
	}

}
