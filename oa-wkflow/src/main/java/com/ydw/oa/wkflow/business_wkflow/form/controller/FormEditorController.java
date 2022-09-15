package com.ydw.oa.wkflow.business_wkflow.form.controller;

import javax.annotation.Resource;

import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.base.action.BaseAction;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_wkflow.form.dto.FormPreviewDto;
import com.ydw.oa.wkflow.util.WebUtil;
import com.ydw.oa.wkflow.util.form.FormReadTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "表单同步、调试、保存")
@RestController
@RequestMapping("/form/editor")
public class FormEditorController extends BaseAction {

	@Autowired
	private IFormService formService;
	@Resource
	private TaskService taskService;

	@ApiOperation(value = "将json 数据同步并生成 html")
	@GetMapping("/sync")
	public Wrapper<String> sync(@ApiParam("要同步的表单ID") String form_id) {
		Form form = formService.getById(form_id);
		// 替换表单提交文件
		form.setHtml(FormReadTools.replaceSubmitModel(form.getJson()));
		// 替换表单只读文件
		form.setHtmlReadonly(FormReadTools.replaceReadModel(form.getJson()));
		formService.saveOrUpdate(form);

		return WrapMapper.ok("同步成功!");
	}

	@ApiOperation(value = "将html设置到session中")
	@PostMapping("/setToSession")
	public Wrapper<String> setToSession(FormPreviewDto formPreviewDto) {
		WebUtil.getSession().setAttribute("html", formPreviewDto.getHtml());
		return WrapMapper.ok();
	}

	@ApiOperation(value = "预览接口")
	@GetMapping("/preview")
	public String preview() {
		String html = (String) WebUtil.getSession().getAttribute("html");
		WebUtil.getSession().setAttribute("html", null);
		return html;
	}

	@ApiOperation(value = "更新html的值")
	@PostMapping("/updateHtml")
	public Wrapper<String> updateHtml(FormPreviewDto formPreviewDto) {
		String formId = formPreviewDto.getFormId();
		if (ChkUtil.isNotNull(formPreviewDto.getTaskId()) && !"null".equals(formPreviewDto.getTaskId())) {
			Task task = taskService.createTaskQuery().taskId(formPreviewDto.getTaskId()).singleResult();
			formId = task.getFormKey();
		}
		Form form = formService.getById(formId);
		form.setHtml(formPreviewDto.getHtml());
		formService.saveOrUpdate(form);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "更新只读html的值")
	@PostMapping("/updateHtmlReadonly")
	public Wrapper<String> updateHtmlReadonly(FormPreviewDto formPreviewDto) {
		String formId = formPreviewDto.getFormId();
		if (ChkUtil.isNotNull(formPreviewDto.getTaskId()) && !"null".equals(formPreviewDto.getTaskId())) {
			Task task = taskService.createTaskQuery().taskId(formPreviewDto.getTaskId()).singleResult();
			formId = task.getFormKey();
		}
		Form form = formService.getById(formId);
		form.setHtmlReadonly(formPreviewDto.getHtml());
		formService.saveOrUpdate(form);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取表单html")
	@GetMapping("/getFormHtml")
	public String getFormHtml(FormPreviewDto formPreviewDto) {
		String formId = formPreviewDto.getFormId();
		if (ChkUtil.isNotNull(formPreviewDto.getTaskId()) && !"null".equals(formPreviewDto.getTaskId())) {
			Task task = taskService.createTaskQuery().taskId(formPreviewDto.getTaskId()).singleResult();
			formId = task.getFormKey();
		}
		Form form = formService.getById(formId);
		if (ChkUtil.isNull(form.getHtml())) {
			return "您还没有同步表单，请先同步表单";
		}
		return form.getHtml();
	}

	@ApiOperation(value = "获取表单只读html")
	@GetMapping("/getFormHtmlReadonly")
	public String getFormHtmlReadonly(FormPreviewDto formPreviewDto) {
		String formId = formPreviewDto.getFormId();
		if (ChkUtil.isNotNull(formPreviewDto.getTaskId()) && !"null".equals(formPreviewDto.getTaskId())) {
			Task task = taskService.createTaskQuery().taskId(formPreviewDto.getTaskId()).singleResult();
			formId = task.getFormKey();
		}
		Form form = formService.getById(formId);
		if (ChkUtil.isNull(form.getHtml())) {
			return "您还没有同步表单，请先同步表单";
		}
		return form.getHtmlReadonly();
	}
}
