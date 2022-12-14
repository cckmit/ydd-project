package com.ydw.oa.wkflow.business_wkflow.form.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import com.alibaba.fastjson.JSONArray;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.business_main.review_records.entity.ReviewRecords;
import com.ydw.oa.wkflow.business_main.review_records.service.IReviewRecordsService;
import com.ydw.oa.wkflow.util.IPUtil;
import com.ydw.oa.wkflow.util.WebUtil;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.category.entity.Category;
import com.ydw.oa.wkflow.business_main.category.service.ICategoryService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.mapper.HiDatasMapper;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.datas.service.IHiDatasService;
import com.ydw.oa.wkflow.business_main.file.entity.File;
import com.ydw.oa.wkflow.business_main.file.service.IFileService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_main.reject.service.IRejectService;
import com.ydw.oa.wkflow.business_main.system.entity.System;
import com.ydw.oa.wkflow.business_main.system.service.ISystemService;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileHtmlService;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileService;
import com.ydw.oa.wkflow.business_wkflow.form.dto.FormSubmitDto;
import com.ydw.oa.wkflow.business_wkflow.task.service.TaskInjectService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.activiti.ReviewTool;
import com.ydw.oa.wkflow.util.date.DateTools;
import com.ydw.oa.wkflow.util.file.Html2PdfUtil;
import org.springframework.web.util.WebUtils;

/**
 * ???????????????
 * 
 * @author ?????????
 *
 */
@Service
public class FormValsService {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ISystemService systemService;
	@Autowired
	private IFileService fileService;
	@Autowired
	private JdbcTemplate jt;

	@Autowired
	private IFormService formService;
	@Autowired
	private ICategoryService categoryService;
	@Autowired
	private IDatasService datasService;
	@Autowired
	private IHiDatasService hiDatasService;
	@Autowired
	private IRejectService rejectService;
	@Autowired
	private IWkDocFileService wkDocFileService;
	@Autowired
	private IWkDocFileHtmlService wkDocFileHtmlService;

	@Autowired
	private ModelJsonService modelJsonService;

	@Autowired
	private TaskInjectService taskInjectService;

	@Autowired
	private HiDatasMapper hiDatasMapper;

	@Autowired
	private ICodeService codeService;

	@Autowired
	private IReviewRecordsService reviewRecordsService;
	@Autowired
	private AuthFeignService authFeignService;

	@Transactional
	public Datas saveFormVals(@Valid FormSubmitDto formSubmitDto, String resourceId) {
		boolean flag = false;
		String task_id = formSubmitDto.getTask_id();
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();

		// 1. ??????????????????
		String form_key = task.getFormKey();
		Form formPo = formService.getById(form_key);
		Category category = categoryService.getById(formPo.getCategoryId());
		// 2.???????????????
		// ?????????????????????
		Datas datas = this.getFormValByTaskId(task_id);
		if (datas == null) {
			datas = new Datas();
		}
		datas.setCategoryId(category.getObjectId());
		datas.setCategoryName(category.getType());
		datas.setFormKid(formPo.getObjectId());
		datas.setShowKey(formPo.getShowKey());
		datas.setFormName(formPo.getName());
		datas.setFormFilesJson(formSubmitDto.getForm_files_json());
		datas.setHtml(formPo.getHtml());
		datas.setHtmlReadonly(formPo.getHtmlReadonly());
		datas.setRunType(formPo.getRunType());

		// 3.????????????
		datas.setAssigner(task.getAssignee());// ??????????????? : ????????????????????????
		datas.setActiTaskId(task.getId());
		datas.setActiProcInstId(task.getProcessInstanceId());
		datas.setResourceId(resourceId);

		// ??????,?????????????????????,?????????
		int sortz = this.getFormValsCount(task.getProcessInstanceId());
		datas.setSortz(sortz);

		// ??????json???: ??????
		datas.setFormJson(formPo.getJson());
		// ???????????????json???:??????????????????
		String valsJson = formSubmitDto.getForm();
		if ("table".equals(formPo.getFormType())) {
			datas.setType("?????????");
			flag = true;
			// ??????????????????html??????
			wkDocFileHtmlService.createWkDocFileHTML(task.getFormKey(), task.getProcessInstanceId(), formSubmitDto.getHtml());
		} else {
			datas.setType("?????????");
		}
		if (sortz > 0) {
			// ??????????????????????????????
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", task.getProcessInstanceId());
			queryWrapper.lt("SORTZ", sortz);
			queryWrapper.orderByDesc("SORTZ");
			List<Datas> list = datasService.list(queryWrapper);
			if (!list.isEmpty()) {
				JSONObject jsonObject = JsonUtil.jsonStrToJsonObject(valsJson);
				// ?????????????????????
				if("1".equals(jsonObject.getString("reviewresult"))) {
					String rejectUsrId = formSubmitDto.getCurrent_usr_id();
					if (ChkUtil.isNull(rejectUsrId)) {
						rejectUsrId = SessionTool.getSessionAdminId();
					}
					this.taskRejectSubmit(task_id, jsonObject.getString("reviewreason"), rejectUsrId);
				}
				jsonObject.put("reviewresult", "");
				JSONObject anotherJsonObject = JsonUtil.jsonStrToJsonObject(list.get(0).getFormValsJson());
				anotherJsonObject.putAll(jsonObject);
				valsJson = anotherJsonObject.toJSONString();
			}
		}
		datas.setFormValsJson(valsJson);
		datas.setStatuz(0);
		datasService.saveOrUpdate(datas);

		if (flag) {
			//??????????????????
			ReviewRecords reviewRecords = new ReviewRecords();
			String assigner = datas.getAssigner();
			reviewRecords.setReviewId(assigner);
			reviewRecords.setReviewIP(IPUtil.getIpAddress(WebUtil.getRequest()));
			Map<String, Object> result = authFeignService.getOne(assigner).getResult();
			if (ChkUtil.isNotNull(result) && !result.isEmpty()) {
				reviewRecords.setReviewName((String)result.get("REAL_NAME"));
			}
			reviewRecordsService.saveOrUpdate(reviewRecords);
		}

		return datas;
	}

	@Transactional
	public void createPdf(String task_id) {
		// TODO ??????pdf?????? http://test.bat.77bi.vip
		String url = "http://localhost:18210/oa-wkflow/resource/table/pdfModel.html?task_id=" + task_id;

		// ??????????????????
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();
		int year = DateTools.getYear();
		int month = DateTools.getMonth();
		String folder = year + java.io.File.separator + month;
		String folder_url = year + "/" + month;
		String filename = DateTools.getCreated() + ".pdf";
		String destPath = DATA_PATH + java.io.File.separator + folder + java.io.File.separator + filename;
		logger.info("path--->" + destPath);
		Html2PdfUtil.html2pdf(url, destPath);

		// ????????????
		File tf = new File();
		tf.setFileName(filename);
		tf.setNewFileName(filename);
		tf.setFolder(folder);
		tf.setFolderUrl(folder_url);
		tf.setContentType("application/pdf");
		java.io.File newFile = new java.io.File(destPath);
		if (!newFile.getParentFile().exists()) {
			newFile.getParentFile().mkdirs();
		}
		tf.setSize(newFile.length());
		fileService.save(tf);
		logger.info("????????????-->" + tf.getObjectId() + "---" + tf.getFileName());
	}

	/**
	 * .?????????????????????
	 * 
	 * @param processInstanceId
	 * @return
	 */
	public Datas getFirstForm(String processInstanceId) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("sortz", 0);
		queryWrapper.eq("acti_proc_inst_id", processInstanceId);
		Datas datas = datasService.getOne(queryWrapper);
		return datas;
	}

	/**
	 * .????????????????????????
	 *
	 * @param processInstanceId
	 * @return
	 */
	public Datas getLastForm(String processInstanceId) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("acti_proc_inst_id", processInstanceId).orderByDesc("SORTZ");;

		List<Datas> list = datasService.list(queryWrapper);
		if(ChkUtil.isNotNull(list) && list.size()>0){
			return list.get(0);
		}
		return null;
	}

	public List<Map<String, Object>> listFormVals(String processInstanceId) {
		String sql = "select t.form_vals_json from t_datas t where t.IS_DELETED=0 and t.acti_proc_inst_id=?";
		List<Map<String, Object>> list = jt.queryForList(sql, processInstanceId);
		return list;
	}

	public int getFormValsCount(String processInstanceId) {
		String sql = "select count(*) cnt from t_datas t where t.IS_DELETED=0 and t.acti_proc_inst_id=?";
		Number cnt = (Number) jt.queryForMap(sql, processInstanceId).get("cnt");
		return cnt.intValue();
	}

	// ?????????????????????
	public Datas getPreFormVals(String processInstanceId, String form_kid) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("TYPE", "?????????");
		queryWrapper.eq("ACTI_PROC_INST_ID", processInstanceId);
		queryWrapper.eq("FORM_KID", form_kid);
		List<Datas> list = datasService.list(queryWrapper);
		if (list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	private String getShowKeySql(String show_key) {
		String sql = "";
		if (ChkUtil.isNull(show_key)) {
			return sql;
		}
		sql = " and (show_key is null or show_key='@show_key')".replace("@show_key", show_key);

		return sql;
	}

	// ??????????????????????????????
	public List<Map<String, Object>> getAllBeforForms(String processInstanceId, int sortz, String show_key) {
		String sql = "select t.*,t1.FORM_TYPE from t_datas t left join t_form t1 on t.FORM_KID=t1.OBJECT_ID where t.IS_DELETED=0 and t.acti_proc_inst_id=? and t.sortz<? @showKeySql order by t.sortz asc";
		sql = sql.replace("@showKeySql", getShowKeySql(show_key));
		List<Map<String, Object>> list = jt.queryForList(sql, processInstanceId, sortz);
		return list;
	}

	public List<Map<String, Object>> getAllBeforForms(String processInstanceId, String task_id, String show_key) {
		String sql = "select t.*,t1.FORM_TYPE from t_datas t left join t_form t1 on t.FORM_KID=t1.OBJECT_ID where t.IS_DELETED=0 and t.acti_proc_inst_id=? and (t.acti_task_id!=? or t.acti_task_id is null) @showKeySql order by t.sortz asc";
		sql = sql.replace("@showKeySql", getShowKeySql(show_key));
		List<Map<String, Object>> list = jt.queryForList(sql, processInstanceId, task_id);
		return list;
	}

	// ????????????
	@Transactional
	public void cancelInstance(@Valid String processInstanceId) {
		// ????????????
		runtimeService.deleteProcessInstance(processInstanceId, "????????????");
		// ??????????????????
		historyService.deleteHistoricProcessInstance(processInstanceId);
		// ???????????????
		// UPDATE ????????? SET ????????? = ?????? WHERE ????????? = ??????
		String sql = "update t_datas t set t.IS_DELETED=-100 where t.acti_proc_inst_id=?";
		int update = jt.update(sql, processInstanceId);
		logger.info("??????-->{}<--???????????????", update);
	}

	// ??????????????????/????????? ?????????
	public List<Map<String, Object>> getAllMineFormList(String user_id, String form_kid) {
		String sql = "select * from t_datas t where t.IS_DELETED=0 and t.refer_kid is null and t.assigner=? and t.form_kid=? ";
		List<Map<String, Object>> list = jt.queryForList(sql, user_id, form_kid);
		return list;
	}

	// ??????task_id ????????????
	public Datas getFormValByTaskId(String task_id) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("acti_task_id", task_id);
		Datas datas = datasService.getOne(queryWrapper);
		return datas;
	}

	public String taskSubmit(FormSubmitDto formSubmitDto) {
		// TODO ????????????
		String task_id = formSubmitDto.getTask_id();
		String form = formSubmitDto.getForm();
		// ????????????
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		if (task == null) {
			return "??????????????????!";
		}
		String processInstanceId = task.getProcessInstanceId();
		
		logger.info("????????????...");
		logger.info("getCurrent_usr_id-->:" + formSubmitDto.getCurrent_usr_id());
		if (ChkUtil.isNotNull(formSubmitDto.getCurrent_usr_id())) {
			task.setAssignee(formSubmitDto.getCurrent_usr_id());
		} else {
			task.setAssignee(SessionTool.getSessionAdminId());
		}
		taskService.saveTask(task);

		JSONObject formJson = JsonUtil.jsonStrToJsonObject(form);
		Set<String> keySet = formJson.keySet();
		for (String key : keySet) {
			Object val = formJson.get(key);
			logger.info("{}:{}", key, val);
			taskService.setVariable(task.getId(), key, val);
		}

		// *** ?????????????????????????????? , json?????? ***
		Datas formValsPo = this.saveFormVals(formSubmitDto, task.getTaskDefinitionKey());
		String processInstanceName = formValsPo.getFormName();
		// ???????????????????????????????????????
		hiDatasService.createHistoryActivitTablesDatas(formValsPo.getObjectId(), processInstanceId);
		taskService.complete(task_id);
		
		// ???????????? task??????????????????
//		taskInjectService.injectDynamicData(task);

		if ("?????????".equals(formValsPo.getType())) {
			logger.info("?????????????????????, ??????????????????.???????????????.");
			// formDuplicateService.doDuplicateTask(processInstanceId);
		}
		logger.info("??????????????????,????????????????????????.");
		modelJsonService.dueNextTaskPushHandle(processInstanceId);
		// ?????????????????????????????????????????????????????????HiDatas?????????
		if (!"1".equals(formJson.getString("reviewresult"))) {
			this.checkAndDeleteHiDatas(processInstanceId, processInstanceName);
		}
		return "????????????!";
	}

	@Transactional
	public String startTask(FormSubmitDto formSubmitDto) {
		// TODO ????????????
		// 1. ??????????????????
		Form formPo = formService.getById(formSubmitDto.getForm_id());
		// ????????????
		Model model = repositoryService.createModelQuery().modelId(formPo.getModelId()).singleResult();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(model.getDeploymentId()).singleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableApplyer", SessionTool.getSessionAdminId());
		ProcessInstance process = runtimeService.startProcessInstanceById(processDefinition.getId(), map);

		Task task = taskService.createTaskQuery().processInstanceId(process.getId()).singleResult();
		
		String processInstanceId = task.getProcessInstanceId();
		task.setAssignee(SessionTool.getSessionAdminId());
		taskService.saveTask(task);
		// ????????????
		JSONObject formJson = JsonUtil.jsonStrToJsonObject(formSubmitDto.getForm());
		Set<String> keySet = formJson.keySet();
		for (String key : keySet) {
			Object val = formJson.get(key);
			logger.info("{}:{}", key, val);
			taskService.setVariable(task.getId(), key, val);
		}

		formSubmitDto.setTask_id(task.getId());
		// ????????????????????????
		Datas formValsPo = this.saveFormVals(formSubmitDto, task.getTaskDefinitionKey());
		// ???????????????????????????????????????
		hiDatasService.createHistoryActivitTablesDatas(formValsPo.getObjectId(), processInstanceId);
		
		taskService.complete(task.getId());

		// ???????????? task??????????????????
//		taskInjectService.injectDynamicData(task);

		// ??????????????????,????????????????????????
		modelJsonService.dueNextTaskPushHandle(processInstanceId);

		// ?????????????????????????????????????????????????????????HiDatas?????????
		if (!"1".equals(formJson.getString("reviewresult"))) {
			this.checkAndDeleteHiDatas(processInstanceId, null);
		}
		return "????????????!";
	}

	@Transactional
	public void checkAndDeleteHiDatas(String processInstanceId, String processInstanceName) {
		// TODO ?????????????????????????????????????????????????????????HiDatas?????????
		// ??????????????????
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (ChkUtil.isNull(processInstance)) {
			// ????????????
			Datas datas = this.getFirstForm(processInstanceId);
			Form form = formService.getById(datas.getFormKid());
			Model model = repositoryService.createModelQuery().modelId(form.getModelId()).singleResult();
			JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
			String code = object.getString("oa-text-code-0");
			if (ChkUtil.isNull(code)) {
				code = codeService.getReviewCode(model.getName());
				object.put("oa-text-code-0", "?????????" + code);// ??????
				datas.setFormValsJson(object.toJSONString());
				datasService.saveOrUpdate(datas);
				datas = this.getLastForm(processInstanceId);
				object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
				object.put("oa-text-code-0", code);
				datas.setFormValsJson(object.toJSONString());
				datasService.saveOrUpdate(datas);
			}
			// ???????????????HiDatas
			hiDatasMapper.deleteHiDatas(processInstanceId);
			// ???????????????????????????????????????????????????????????????
			wkDocFileService.createWkDocFile(processInstanceId,code);
			//?????????????????????
			JSONArray files = object.getJSONArray("files");
			if (ChkUtil.isNotNull(files) && files.size() > 0) {
				for (int i = 0; i < files.size(); i++) {
					JSONObject jsonObject = files.getJSONObject(i);
					String src = jsonObject.getString("value");
					if (ChkUtil.isNotNull(src)) {
						String[] split = src.split("\\?");
						if (split.length>1 && ChkUtil.isNotNull(split[1])) {
							String[] split1 = split[1].split("=");
							if (split1.length>1 && ChkUtil.isNotNull(split1[1])){
								String fileId = split1[1];
								fileService.addWatermark(fileId,code);
							}
						}
					}
				}
			}
			// ??????????????????
			ReviewTool.finish(datas.getAssigner(), processInstanceName);

		}
	}

	public void startProcessAndSetAssignee(String deploymentId, String assigner) {
		// TODO ?????????????????????????????????????????????
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId).singleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableApplyer", assigner);
		runtimeService.startProcessInstanceById(processDefinition.getId(), map);
	}

	public JSONObject getPdfData(String task_id) {
		// TODO ??????????????????
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_TASK_ID", task_id);
		Datas datas = datasService.getOne(queryWrapper);

		Form form = formService.getById(datas.getFormKid());

		JSONObject result = new JSONObject();
		result.put("form", form);
		result.put("json", datas.getFormValsJson());
		return result;
	}

	public void taskRejectSubmit(String task_id, String reason, String rejectUsrId) {
		// TODO ??????????????????
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		String pid = task.getProcessInstanceId();
		// ???????????????????????????
		Datas datas = this.getFirstForm(pid);
		if (ChkUtil.isNull(reason)) {
			// ?????????????????????????????????????????????????????????
			ReviewTool.reject(datas.getAssigner(), task);
			// ??????????????????
			rejectService.addData(datas, task, "??????", reason,rejectUsrId);
		} else {
			// ????????????????????????????????????????????????????????????
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", pid);
			queryWrapper.orderByDesc("CREATE_TIME");
			List<Datas> list = datasService.list(queryWrapper);
			Datas preTask = list.get(0);
			// ??????????????????????????????????????????????????????????????????????????????
			String usrs = datas.getAssigner();
			if (!preTask.getAssigner().equals(usrs)) {
				usrs = usrs + "," + preTask.getAssigner();
			}
			ReviewTool.reject(usrs, task);
			// ??????????????????????????????
			rejectService.addData(datas, task, "??????", reason,rejectUsrId);
		}
	}

}
