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
public class FormAccessoryQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "模板名称")
	private String name;
	@ApiModelProperty(value = "表单ID")
	private String formId;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("FILE_NAME", name);
		}
		if (ChkUtil.isNotNull(formId)) {
			queryWrapper.eq("FORM_ID", formId);
		}
		queryWrapper.orderBy(true, true, "SORTZ");
		return queryWrapper;
	}

}
