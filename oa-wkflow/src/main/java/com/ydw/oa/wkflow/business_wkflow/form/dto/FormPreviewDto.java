package com.ydw.oa.wkflow.business_wkflow.form.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@ApiModel(value = "FormPreviewDto表单预览对象", description = "")
@Data
public class FormPreviewDto {

	@ApiModelProperty(value = "表单预览的html")
	private String html;
	
	@ApiModelProperty(value = "表单id")
	private String formId;
	
	@ApiModelProperty(value = "任务id")
	private String taskId;

}
