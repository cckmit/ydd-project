package com.ydw.oa.wkflow.business_main.form.entity;

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
@TableName("t_form")
@ApiModel(value = "Form对象", description = "")
public class Form {

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

	@ApiModelProperty(value = "表单名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "表单的同类标识", example = "同时下发到 人事+财务，人事与财务的审批单互相不可见，通过show_key做标识区分")
	@TableField("SHOW_KEY")
	private String showKey;

	@ApiModelProperty(value = "编码  排序用")
	@TableField("CODE")
	private String code;

	@ApiModelProperty(value = "所属模型id")
	@TableField("MODEL_ID")
	private String modelId;

	@ApiModelProperty(value = "所属类型id")
	@TableField("CATEGORY_ID")
	private String categoryId;

	@ApiModelProperty(value = "运行类型： html or json")
	@TableField("RUN_TYPE")
	private String runType;

	@ApiModelProperty(value = "原版网页json")
	@TableField("WIDGET_JSON")
	private String widgetJson;

	@ApiModelProperty(value = "生成表单json")
	@TableField("JSON")
	private String json;

	@ApiModelProperty(value = "动态注入json 预留")
	@TableField("JSON_EVAL")
	private String jsonEval;
	
	@ApiModelProperty(value = "同步生成html代码")
	@TableField("HTML")
	private String html;
	
	@ApiModelProperty(value = "同步生成只读 html代码")
	@TableField("HTML_READONLY")
	private String htmlReadonly;

	@ApiModelProperty(value = "流程名称")
	@TableField(exist = false)
	private String modelName;
	
	@ApiModelProperty(value = "运行类型： form or table")
	@TableField("FORM_TYPE")
	private String formType;

}
