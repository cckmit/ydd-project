package com.ydw.oa.wkflow.business_main.datas.dto;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.base.dto.BaseVueQuery;
import com.tmsps.fk.common.util.ChkUtil;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "DatasQuery对象", description = "")
public class DatasQuery<T> extends BaseVueQuery<T> {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "表单名称")
	private String name;
	@ApiModelProperty(value = "操作人员")
	private String assigner;
	@ApiModelProperty(value = "类型：填写单 or 审批单")
	private String type;
	@ApiModelProperty(value = "流程分类")
	private String category_id;
	@ApiModelProperty(value = "表单id")
	private String formKid;
	@ApiModelProperty(value = "统计时间", example = "年-月  |  年")
	private String date;
	@ApiModelProperty(value = "统计时间类型: 月  or 年")
	private String formatType;
	@ApiModelProperty(value = "排序")
	private int sortz;
	@ApiModelProperty(value = "开始时间从")
	private String startTime;
	@ApiModelProperty(value = "到")
	private String endTime;

	public Wrapper<T> makeQueryWrapper() {
		QueryWrapper<T> queryWrapper = new QueryWrapper<>();
		if (ChkUtil.isNotNull(name)) {
			queryWrapper.like("FORM_NAME", name);
		}
		if (ChkUtil.isNotNull(assigner)) {
			queryWrapper.eq("ASSIGNER", assigner);
		}
		if (ChkUtil.isNotNull(type)) {
			queryWrapper.eq("TYPE", type);
		}
		if (ChkUtil.isNotNull(sortz)) {
			queryWrapper.eq("SORTZ", sortz);
		}
		if (ChkUtil.isNotNull(category_id)) {
			queryWrapper.eq("CATEGORY_ID", category_id);
		}
		if(ChkUtil.isNotNull(formKid)) {
			queryWrapper.eq("FORM_KID", formKid);
		}
		if(ChkUtil.isNotNull(date)) {
			if("月".equals(formatType)) {
				queryWrapper.like("DATE_FORMAT(CREATE_TIME,'%Y-%m')", date);
			}else {
				queryWrapper.like("DATE_FORMAT(CREATE_TIME,'%Y')", date);
			}
		}

		if (ChkUtil.isNotNull(startTime)) {
			queryWrapper.ge("CREATE_TIME", startTime);
		}
		if (ChkUtil.isNotNull(endTime)) {
			queryWrapper.le("CREATE_TIME", endTime);
		}

		queryWrapper.orderByDesc("CREATE_TIME");
		return queryWrapper;
	}

}
