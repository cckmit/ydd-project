package com.ydw.oa.wkflow.business_main.form.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FormQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "表单名称")
	private String name;
	@ApiModelProperty(value = "流程名称")
	private String modelName;
	@ApiModelProperty(value = "流程类型id")
	private String categoryId;
	@ApiModelProperty(value = "所属模型id")
	private String modelId;
	@ApiModelProperty(value = "流程id")
	private String procdefId;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("NAME", name);
		}
		if (ChkUtil.isNotNull(modelName)) {
			queryWrapper.like("model.NAME_", modelName);
		}
		if (ChkUtil.isNotNull(categoryId)) {
			queryWrapper.eq("CATEGORY_ID", categoryId);
		}
		if (ChkUtil.isNotNull(modelId)) {
			queryWrapper.eq("MODEL_ID", modelId);
		}
		if (ChkUtil.isNotNull(procdefId)) {
			queryWrapper.eq("proc.ID_", procdefId);
		}
		queryWrapper.eq("IS_DELETED", 0);
		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}

}
