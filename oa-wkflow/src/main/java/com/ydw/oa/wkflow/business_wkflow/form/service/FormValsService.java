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
 * 表单值处理
 * 
 * @author 冯晓东
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

		// 1. 获取表单参数
		String form_key = task.getFormKey();
		Form formPo = formService.getById(form_key);
		Category category = categoryService.getById(formPo.getCategoryId());
		// 2.创建表单值
		// 检测是否草稿中
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

		// 3.绑定流程
		datas.setAssigner(task.getAssignee());// 赋值操作人 : 改为当前登录用户
		datas.setActiTaskId(task.getId());
		datas.setActiProcInstId(task.getProcessInstanceId());
		datas.setResourceId(resourceId);

		// 如上,按照审批的过程,加排序
		int sortz = this.getFormValsCount(task.getProcessInstanceId());
		datas.setSortz(sortz);

		// 表单json值: 快照
		datas.setFormJson(formPo.getJson());
		// 存储表单值json值:填写的表单值
		String valsJson = formSubmitDto.getForm();
		if ("table".equals(formPo.getFormType())) {
			datas.setType("审批单");
			flag = true;
			// 生成流程关联html数据
			wkDocFileHtmlService.createWkDocFileHTML(task.getFormKey(), task.getProcessInstanceId(), formSubmitDto.getHtml());
		} else {
			datas.setType("填写单");
		}
		if (sortz > 0) {
			// 获取上一审批单的数据
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", task.getProcessInstanceId());
			queryWrapper.lt("SORTZ", sortz);
			queryWrapper.orderByDesc("SORTZ");
			List<Datas> list = datasService.list(queryWrapper);
			if (!list.isEmpty()) {
				JSONObject jsonObject = JsonUtil.jsonStrToJsonObject(valsJson);
				// 判断是否为驳回
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
			//审批记录保存
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
		// TODO 生成pdf文档 http://test.bat.77bi.vip
		String url = "http://localhost:18210/oa-wkflow/resource/table/pdfModel.html?task_id=" + task_id;

		// 获取保存路径
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

		// 保存文件
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
		logger.info("文档信息-->" + tf.getObjectId() + "---" + tf.getFileName());
	}

	/**
	 * .查询第一个表单
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
	 * .查询最后一个表单
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

	// 查询上一次表单
	public Datas getPreFormVals(String processInstanceId, String form_kid) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("TYPE", "填写单");
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

	// 查询之前填写过的表单
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

	// 流程撤销
	@Transactional
	public void cancelInstance(@Valid String processInstanceId) {
		// 删除流程
		runtimeService.deleteProcessInstance(processInstanceId, "用户撤销");
		// 删除历史审批
		historyService.deleteHistoricProcessInstance(processInstanceId);
		// 删除库数据
		// UPDATE 表名称 SET 列名称 = 新值 WHERE 列名称 = 某值
		String sql = "update t_datas t set t.IS_DELETED=-100 where t.acti_proc_inst_id=?";
		int update = jt.update(sql, processInstanceId);
		logger.info("删除-->{}<--条表单记录", update);
	}

	// 查询我填过的/未关联 的表单
	public List<Map<String, Object>> getAllMineFormList(String user_id, String form_kid) {
		String sql = "select * from t_datas t where t.IS_DELETED=0 and t.refer_kid is null and t.assigner=? and t.form_kid=? ";
		List<Map<String, Object>> list = jt.queryForList(sql, user_id, form_kid);
		return list;
	}

	// 根据task_id 获取表单
	public Datas getFormValByTaskId(String task_id) {
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("acti_task_id", task_id);
		Datas datas = datasService.getOne(queryWrapper);
		return datas;
	}

	public String taskSubmit(FormSubmitDto formSubmitDto) {
		// TODO 提交任务
		String task_id = formSubmitDto.getTask_id();
		String form = formSubmitDto.getForm();
		// 获取任务
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		if (task == null) {
			return "您已提交成功!";
		}
		String processInstanceId = task.getProcessInstanceId();
		
		logger.info("表单提交...");
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

		// *** 保存表单以及表单的值 , json格式 ***
		Datas formValsPo = this.saveFormVals(formSubmitDto, task.getTaskDefinitionKey());
		String processInstanceName = formValsPo.getFormName();
		// 生成工作流当前相关数据记录
		hiDatasService.createHistoryActivitTablesDatas(formValsPo.getObjectId(), processInstanceId);
		taskService.complete(task_id);
		
		// 动态注入 task各种变量参数
//		taskInjectService.injectDynamicData(task);

		if ("审批单".equals(formValsPo.getType())) {
			logger.info("检查重复审批人, 检测自动去重.暂时不使用.");
			// formDuplicateService.doDuplicateTask(processInstanceId);
		}
		logger.info("根据完成时间,自动设置提醒方式.");
		modelJsonService.dueNextTaskPushHandle(processInstanceId);
		// 判断流程实例是否完成，若完成则删除相关HiDatas的数据
		if (!"1".equals(formJson.getString("reviewresult"))) {
			this.checkAndDeleteHiDatas(processInstanceId, processInstanceName);
		}
		return "提交成功!";
	}

	@Transactional
	public String startTask(FormSubmitDto formSubmitDto) {
		// TODO 启动任务
		// 1. 获取表单参数
		Form formPo = formService.getById(formSubmitDto.getForm_id());
		// 启动流程
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
		// 传入参数
		JSONObject formJson = JsonUtil.jsonStrToJsonObject(formSubmitDto.getForm());
		Set<String> keySet = formJson.keySet();
		for (String key : keySet) {
			Object val = formJson.get(key);
			logger.info("{}:{}", key, val);
			taskService.setVariable(task.getId(), key, val);
		}

		formSubmitDto.setTask_id(task.getId());
		// 生成表单提交数据
		Datas formValsPo = this.saveFormVals(formSubmitDto, task.getTaskDefinitionKey());
		// 生成工作流当前相关数据记录
		hiDatasService.createHistoryActivitTablesDatas(formValsPo.getObjectId(), processInstanceId);
		
		taskService.complete(task.getId());

		// 动态注入 task各种变量参数
//		taskInjectService.injectDynamicData(task);

		// 根据完成时间,自动设置提醒方式
		modelJsonService.dueNextTaskPushHandle(processInstanceId);

		// 判断流程实例是否完成，若完成则删除相关HiDatas的数据
		if (!"1".equals(formJson.getString("reviewresult"))) {
			this.checkAndDeleteHiDatas(processInstanceId, null);
		}
		return "提交成功!";
	}

	@Transactional
	public void checkAndDeleteHiDatas(String processInstanceId, String processInstanceName) {
		// TODO 判断流程实例是否完成，若完成则删除相关HiDatas的数据
		// 获取流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		if (ChkUtil.isNull(processInstance)) {
			// 生成编码
			Datas datas = this.getFirstForm(processInstanceId);
			Form form = formService.getById(datas.getFormKid());
			Model model = repositoryService.createModelQuery().modelId(form.getModelId()).singleResult();
			JSONObject object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
			String code = object.getString("oa-text-code-0");
			if (ChkUtil.isNull(code)) {
				code = codeService.getReviewCode(model.getName());
				object.put("oa-text-code-0", "编号：" + code);// 编号
				datas.setFormValsJson(object.toJSONString());
				datasService.saveOrUpdate(datas);
				datas = this.getLastForm(processInstanceId);
				object = JsonUtil.jsonStrToJsonObject(datas.getFormValsJson());
				object.put("oa-text-code-0", code);
				datas.setFormValsJson(object.toJSONString());
				datasService.saveOrUpdate(datas);
			}
			// 删除对应的HiDatas
			hiDatasMapper.deleteHiDatas(processInstanceId);
			// 生成对应的流程文档记录和流程文档员工关联表
			wkDocFileService.createWkDocFile(processInstanceId,code);
			//给附件添加水印
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
			// 发送短信通知
			ReviewTool.finish(datas.getAssigner(), processInstanceName);

		}
	}

	public void startProcessAndSetAssignee(String deploymentId, String assigner) {
		// TODO 启动流程并为第一个任务赋代理人
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.deploymentId(deploymentId).singleResult();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("tableApplyer", assigner);
		runtimeService.startProcessInstanceById(processDefinition.getId(), map);
	}

	public JSONObject getPdfData(String task_id) {
		// TODO 获取表格数据
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
		// TODO 审批驳回任务
		Task task = taskService.createTaskQuery().taskId(task_id).singleResult();
		String pid = task.getProcessInstanceId();
		// 第一个任务提交数据
		Datas datas = this.getFirstForm(pid);
		if (ChkUtil.isNull(reason)) {
			// 通知发起人流程被驳回，需要重新发起申请
			ReviewTool.reject(datas.getAssigner(), task);
			// 保存驳回数据
			rejectService.addData(datas, task, "整体", reason,rejectUsrId);
		} else {
			// 返回上一步操作（流程回退）并填写驳回原因
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
			queryWrapper.eq("ACTI_PROC_INST_ID", pid);
			queryWrapper.orderByDesc("CREATE_TIME");
			List<Datas> list = datasService.list(queryWrapper);
			Datas preTask = list.get(0);
			// 通知上一流程执行人和发起人驳回原因，需要重新办理任务
			String usrs = datas.getAssigner();
			if (!preTask.getAssigner().equals(usrs)) {
				usrs = usrs + "," + preTask.getAssigner();
			}
			ReviewTool.reject(usrs, task);
			// 记录一条驳回操作数据
			rejectService.addData(datas, task, "局部", reason,rejectUsrId);
		}
	}

}
