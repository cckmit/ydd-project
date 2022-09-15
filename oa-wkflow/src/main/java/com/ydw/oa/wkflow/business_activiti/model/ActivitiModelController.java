package com.ydw.oa.wkflow.business_activiti.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.activiti.engine.repository.ModelQuery;
import org.activiti.engine.repository.NativeModelQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.form.service.IFormVariableService;
import com.ydw.oa.wkflow.util.CreatedTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(description = "工作流模板管理")
@RestController
public class ActivitiModelController extends BaseAction {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private IFormVariableService iFormVariableService;

	@ApiOperation(value = "新建模板")
	@ApiImplicitParam(name = "category_id", value = "类型id")
	@SuppressWarnings("deprecation")
	@GetMapping("/create")
	public void newModel(HttpServletRequest request, HttpServletResponse response, String category_id)
			throws IOException {
		// 初始化一个空模型
		Model model = repositoryService.newModel();

		// 设置一些默认信息
		String name = "";
		String description = "";
		int revision = 1;
		String key = "m_" + CreatedTools.getCreated();
		// 设置子系统
		model.setTenantId("子系统1");
		model.setCategory(category_id);

		ObjectNode modelNode = objectMapper.createObjectNode();
		modelNode.put(ModelDataJsonConstants.MODEL_NAME, name);
		modelNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
		modelNode.put(ModelDataJsonConstants.MODEL_REVISION, revision);

		model.setName(name);
		model.setKey(key);
		model.setMetaInfo(modelNode.toString());

		repositoryService.saveModel(model);
		String id = model.getId();

		// 完善ModelEditorSource
		ObjectNode editorNode = objectMapper.createObjectNode();
		editorNode.put("id", "canvas");
		editorNode.put("resourceId", "canvas");
		ObjectNode stencilSetNode = objectMapper.createObjectNode();
		stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
		editorNode.put("stencilset", stencilSetNode);
		repositoryService.addModelEditorSource(id, editorNode.toString().getBytes("utf-8"));
		response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + id);
	}

	@ApiOperation(value = "模板列表")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "category_id", value = "类型id") })
	@GetMapping("/modelList")
	public Wrapper<List<Model>> modelList(String name, String category_id) {
		String sql = "select distinct RES.* from ACT_RE_MODEL RES WHERE RES.TENANT_ID_ = '子系统1' and RES.NAME_<>'' and RES.CATEGORY_=#{category} order by RES.CREATE_TIME_ desc";
		NativeModelQuery nativeModelQuery = repositoryService.createNativeModelQuery();
		if (ChkUtil.isNull(category_id)) {
			sql = sql.replace("and RES.CATEGORY_=#{category}", "");
		}else {
			nativeModelQuery.parameter("category", category_id);
		}
		nativeModelQuery.sql(sql);
		List<Model> list = nativeModelQuery.list();
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "模板列表-分页")
	@ApiImplicitParams({ @ApiImplicitParam(name = "name", value = "名称"),
			@ApiImplicitParam(name = "category_id", value = "类型id"), @ApiImplicitParam(name = "current", value = "当前页"),
			@ApiImplicitParam(name = "size", value = "每页条数") })
	@GetMapping("/modelListPage")
	public Wrapper<Map<String, Object>> modelListPage(String name, String category_id, int current, int size) {
		if (ChkUtil.isNull(name)) {
			name = "";
		} else {
			current = 1;
		}
		ModelQuery modelQuery = repositoryService.createModelQuery();
		if (ChkUtil.isNotNull(category_id)) {
			modelQuery.modelCategory(category_id);
		}
		modelQuery.modelNameLike("%" + name + "%").modelTenantId("子系统1");
		long total = modelQuery.count();
		List<Model> list = modelQuery.orderByCreateTime().desc().listPage((current - 1) * size, size);
		Map<String, Object> map = new HashMap<>();
		map.put("records", list);
		map.put("current", current);
		map.put("size", size);
		map.put("total", total);
		return WrapMapper.ok(map);
	}

	@ApiOperation(value = "删除模板")
	@ApiImplicitParam(name = "modelId", value = "模型id")
	@GetMapping("/deleteModel")
	public Wrapper<String> deleteModel(String modelId) {
		repositoryService.deleteModel(modelId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "部署")
	@ApiImplicitParam(name = "modelId", value = "模型id")
	@GetMapping("/deploy")
	public Wrapper<String> deploy(String modelId) throws Exception {

		// 获取模型
		Model modelData = repositoryService.getModel(modelId);
		byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());

		if (bytes == null) {
			return WrapMapper.ok("模型数据为空，请先设计流程并成功保存，再进行发布。");
		}

		// 生成/更新 模板变量表
		iFormVariableService.createAndUpdateFormVariable(bytes, modelId);

		JsonNode modelNode = new ObjectMapper().readTree(bytes);
		BpmnModel model = new BpmnJsonConverter().convertToBpmnModel(modelNode);
		model.setTargetNamespace(modelData.getCategory());

		if (model.getProcesses().size() == 0) {
			return WrapMapper.ok("数据模型不符要求，请至少设计一条主线流程。");
		}
		byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);

		// 发布流程
		String processName = modelData.getName() + ".bpmn20.xml";
		Deployment deployment = repositoryService.createDeployment().name(modelData.getName())
				.category(modelData.getCategory()).addString(processName, new String(bpmnBytes, "UTF-8")).deploy();
		modelData.setDeploymentId(deployment.getId());
		repositoryService.saveModel(modelData);

		return WrapMapper.ok("部署成功");
	}

}
