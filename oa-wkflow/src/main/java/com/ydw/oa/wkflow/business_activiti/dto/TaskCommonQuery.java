package com.ydw.oa.wkflow.business_activiti.dto;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class TaskCommonQuery {

	@ApiModelProperty(value = "当前页")
	private int current;
	@ApiModelProperty(value = "数量")
	private int size;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "开始时间从")
	private String startTime;
	@ApiModelProperty(value = "到")
	private String endTime;
	@ApiModelProperty(value = "结束时间从")
	private String finishStartTime;
	@ApiModelProperty(value = "到")
	private String finishEndTime;
	@ApiModelProperty(value = "流程分类")
	private String category_id;
	@ApiModelProperty(value = "当前用户")
	private String userId;


}
