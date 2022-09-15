package com.ydw.oa.wkflow.business_main.datas.entity;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_datas")
@ApiModel(value = "Datas对象", description = "")
public class Datas {

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableLogic(value = "0", delval = "1")
	@TableField("IS_DELETED")
	private int isDeleted;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "流程实例ID")
	@TableField("ACTI_PROC_INST_ID")
	private String actiProcInstId;

	@ApiModelProperty(value = "排序")
	@TableField("SORTZ")
	private int sortz;

	@ApiModelProperty(value = "任务ID")
	@TableField("ACTI_TASK_ID")
	private String actiTaskId;

	@ApiModelProperty(value = "表单提交json")
	@TableField("FORM_JSON")
	private String formJson;

	@ApiModelProperty(value = "表单ID")
	@TableField("FORM_KID")
	private String formKid;

	@ApiModelProperty(value = "表单名称")
	@TableField("FORM_NAME")
	private String formName;

	@ApiModelProperty(value = "表单的同类标识,从t_form表拷贝而来", example = "同时下发到 人事+财务，人事与财务的审批单互相不可见，通过show_key做标识区分")
	@TableField("SHOW_KEY")
	private String showKey;

	@ApiModelProperty(value = "类型ID")
	@TableField("CATEGORY_ID")
	private String categoryId;

	@ApiModelProperty(value = "类型名称")
	@TableField("CATEGORY_NAME")
	private String categoryName;

	@ApiModelProperty(value = "表单填写Json名称")
	@TableField("FORM_VALS_JSON")
	private String formValsJson;

	@ApiModelProperty(value = "表单附件json名称")
	@TableField("FORM_FILES_JSON")
	private String formFilesJson;

	@TableField("REFER_KID")
	private String referKid;

	@ApiModelProperty(value = "重置状态：知道同一类表单,表单重置:statuz为 -100")
	@TableField("STATUZ")
	private int statuz;

	@ApiModelProperty(value = "类型：填写单 or 审批单")
	@TableField("TYPE")
	private String type;

	@ApiModelProperty(value = "操作人员")
	@TableField("ASSIGNER")
	private String assigner;

	@ApiModelProperty(value = "表单提交html")
	@TableField("HTML")
	private String html;

	@ApiModelProperty(value = "表单提交只读 html代码")
	@TableField("HTML_READONLY")
	private String htmlReadonly;

	@ApiModelProperty(value = "运行类型： html or json")
	@TableField("RUN_TYPE")
	private String runType;

	@ApiModelProperty(value = "工作流节点id")
	@TableField("RESOURCE_ID")
	private String resourceId;
}
