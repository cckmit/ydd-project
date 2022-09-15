package com.ydw.oa.wkflow.business_wkflow.form.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "FormJsonDto对象", description = "")
@Data
public class FormJsonDto {

	@NotBlank
	@ApiModelProperty(value = "表单ID")
	private String kid;
	@ApiModelProperty(value = "表单设计的json值")
	private String widget_json;
	@ApiModelProperty(value = "表单显示的json值")
	private String json;

}
