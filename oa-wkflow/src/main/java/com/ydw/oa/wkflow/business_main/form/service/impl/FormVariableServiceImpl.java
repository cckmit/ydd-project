package com.ydw.oa.wkflow.business_main.form.service.impl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_main.form.entity.FormVariable;
import com.ydw.oa.wkflow.business_main.form.mapper.FormVariableMapper;
import com.ydw.oa.wkflow.business_main.form.service.IFormVariableService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-19
 */
@Service
public class FormVariableServiceImpl extends ServiceImpl<FormVariableMapper, FormVariable>
		implements IFormVariableService {

	@Autowired
	private ObjectMapper objectMapper;

	@Transactional
	@Override
	public void createAndUpdateFormVariable(byte[] bytes, String modelId) {
		// TODO 生成/更新 模板变量
		// 根据modelId获取 FormVariable对象列表
		QueryWrapper<FormVariable> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("MODEL_ID", modelId);
		List<FormVariable> list = this.list(queryWrapper);
		for (FormVariable formVariable : list) {
			this.removeById(formVariable.getObjectId());
		}
		ObjectNode editorJsonNode;
		try {
			editorJsonNode = (ObjectNode) objectMapper.readTree(new String(bytes, "utf-8"));
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		JSONObject json = JsonUtil.jsonStrToJsonObject(editorJsonNode.toString());
		JSONArray childShapes = json.getJSONArray("childShapes");
		if (childShapes == null || childShapes.size() == 0) {
			return;
		}
		String firstTask = null;
		for (int i = 0; i < childShapes.size(); i++) {
			JSONObject node = childShapes.getJSONObject(i);
			// 获取节点类型
			JSONObject stencil = node.getJSONObject("stencil");
			if ("StartNoneEvent".equals(stencil.getString("id"))) {
				// 获取outgoing
				JSONArray outgoingArr = node.getJSONArray("outgoing");
				JSONObject resource = outgoingArr.getJSONObject(0);
				firstTask = this.getFirstUserTaskResourceId(resource.getString("resourceId"), childShapes);
			}
			if ("UserTask".equals(stencil.getString("id"))) {
				// 获取节点id
				String resourceId = node.getString("resourceId");
				// 获取属性配置
				JSONObject propJson = node.getJSONObject("properties");
				// 新建表单变量表
				FormVariable formVariable = new FormVariable();
				formVariable.setFormId(propJson.getString("formkeydefinition"));
				formVariable.setHandleTime(propJson.getString("doduedatedefinition"));
				formVariable.setHandleType(propJson.getString("handledatedefinition"));
				if (firstTask.equals(resourceId)) {
					formVariable.setIsFirst("是");
				}else {
					formVariable.setIsFirst("否");
				}
				formVariable.setModelId(modelId);
				formVariable.setResourceId(resourceId);
				this.save(formVariable);
			}

		}
	}

	private String getFirstUserTaskResourceId(String targetResourceId, JSONArray nodes) {
		// TODO 获取收个任务resourceId
		JSONObject node = this.getResource(targetResourceId, nodes);
		if ("UserTask".equals(node.getString("stencil"))) {
			return node.getString("resourceId");
		}
		JSONArray outgoingArr = node.getJSONArray("outgoing");
		JSONObject resource = outgoingArr.getJSONObject(0);
		return this.getFirstUserTaskResourceId(resource.getString("resourceId"), nodes);
	}

	private JSONObject getResource(String targetResourceId, JSONArray nodes) {
		// TODO 获取节点
		for (int i = 0; i < nodes.size(); i++) {
			JSONObject node = nodes.getJSONObject(i);
			// 获取节点id
			String resourceId = node.getString("resourceId");
			if (targetResourceId.equals(resourceId)) {
				// 获取节点类型
				JSONObject stencil = node.getJSONObject("stencil");
				// 获取outgoing
				JSONArray outgoingArr = node.getJSONArray("outgoing");
				JSONObject targetNode = new JSONObject();
				targetNode.put("stencil", stencil.getString("id"));
				targetNode.put("resourceId", resourceId);
				targetNode.put("outgoing", outgoingArr);
				return targetNode;
			}
		}
		return null;
	}

}
