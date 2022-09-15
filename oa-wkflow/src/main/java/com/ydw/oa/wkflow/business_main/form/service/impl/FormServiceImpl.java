package com.ydw.oa.wkflow.business_main.form.service.impl;

import javax.validation.Valid;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.mapper.FormMapper;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Service
public class FormServiceImpl extends ServiceImpl<FormMapper, Form> implements IFormService {

	@Autowired
	private RepositoryService repositoryService;

	@Override
	public void saveForm(@Valid Form form) {
		// TODO 保存表单
		Model model = repositoryService.createModelQuery().modelId(form.getModelId()).singleResult();
		form.setCategoryId(model.getCategory());
		if(ChkUtil.isNull(form.getWidgetJson())) {
			form.setWidgetJson(form.getJson());
		}
		this.save(form);
	}

	@Override
	public void updateForm(@Valid Form form) {
		// TODO 编辑表单
		Model model = repositoryService.createModelQuery().modelId(form.getModelId()).singleResult();
		
		Form formDb = this.getById(form.getObjectId());
		formDb.setName(form.getName());
		formDb.setCode(form.getCode());
		formDb.setModelId(form.getModelId());
		formDb.setCategoryId(model.getCategory());
		formDb.setRunType(form.getRunType());
		formDb.setFormType(form.getFormType());
		this.saveOrUpdate(formDb);
	}

}
