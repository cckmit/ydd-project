package com.ydw.oa.auth.business.reception.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * @author hxj
 * @since 2020-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_reception_record")
@ApiModel(value = "ReceptionRecord对象", description = "")
public class ReceptionRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
	private int isDeleted;

	@ApiModelProperty(value = "接待时间")
	@TableField("RECEPTION_DATE")
	private Date receptionDate;

	@ApiModelProperty(value = "接待地点")
	@TableField("ADDRESS")
	private String address;

	@ApiModelProperty(value = "接待方式（住宿 or 就餐）")
	@TableField("STYLE")
	private String style;

	@ApiModelProperty(value = "接待部门")
	@TableField("DEPT_NAME")
	private String deptName;

	@ApiModelProperty(value = "来访事由")
	@TableField("REASON")
	private String reason;

	@ApiModelProperty(value = "合计人数（需计算）")
	@TableField("TOTAL_NUM")
	private int totalNum;

	@ApiModelProperty(value = "合计金额（需计算）")
	@TableField("TOTAL_MONEY")
	private BigDecimal totalMoney;

	@ApiModelProperty(value = "分管领导（根据部门去查）")
	@TableField("DEPT_LEADER")
	private String deptLeader;

	@ApiModelProperty(value = "流程实例id")
	@TableField("PID")
	private String pid;
	
	@ApiModelProperty(value = "审批编号")
	@TableField("REVIEW_CODE")
	private String reviewCode;
	
	@ApiModelProperty(value = "是否会签： 待会签 or 会签中 or 已会签")
	@TableField("MUTI_REVIEW")
	private String mutiReview;
	
	@ApiModelProperty(value = "会签时间")
	@TableField("SIGN_DATE")
	private Date signDate;
	
	@ApiModelProperty(value = "表单Id")
	@TableField("FORM_ID")
	private String formId;

	@ApiModelProperty(value = "接待月份")
	@TableField("MONTH")
	private String month;
}
