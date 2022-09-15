package com.ydw.oa.auth.business.train.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;

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
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "PersTrainPlan对象", description = "")
public class PersTrainPlan implements Serializable {

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

	@ApiModelProperty(value = "培训计划标题")
	@TableField("TITLE")
	private String title;

	@ApiModelProperty(value = "培训时间")
	@TableField("START_TIME")
	private Date startTime;

	@ApiModelProperty(value = "培训内容")
	@TableField("CONTENT")
	private String content;

	@ApiModelProperty(value = "培训流程")
	@TableField("FLOW")
	private String flow;

	@ApiModelProperty(value = "培训审批结果")
	@TableField("REVIEW")
	private String review;

	@ApiModelProperty(value = "培训记录文件id")
	@TableField("FILE_ID")
	private String fileId;

}
