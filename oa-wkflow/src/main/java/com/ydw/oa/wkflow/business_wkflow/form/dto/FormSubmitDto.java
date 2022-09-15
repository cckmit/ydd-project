package com.ydw.oa.wkflow.business_wkflow.form.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel(value = "FormSubmitDto对象", description = "")
public class FormSubmitDto {

	@ApiModelProperty(value = "任务ID")
	private String task_id;
	@ApiModelProperty(value = "表单ID")
	private String form_id;
	@NotBlank
	@ApiModelProperty(value = "表单值json")
	private String form;
	@ApiModelProperty(value = "附件上传列表")
	private String form_files_json;
	@ApiModelProperty(value = "当前操作人员")
	private String current_usr_id;
	@ApiModelProperty(value = "html数据")
	private String html;

}
