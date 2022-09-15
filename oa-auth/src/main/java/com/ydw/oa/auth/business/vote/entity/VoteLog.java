package com.ydw.oa.auth.business.vote.entity;

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
 * @since 2020-07-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "VoteLog对象", description = "")
public class VoteLog implements Serializable {

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

	@ApiModelProperty(value = "投票名称")
	@TableField("NAME")
	private String name;

	@ApiModelProperty(value = "投票备注")
	@TableField("NOTE")
	private String note;

	@ApiModelProperty(value = "投票表单id")
	@TableField("FORM_ID")
	private String formId;

	@ApiModelProperty(value = "投票人员 多选")
	@TableField("SEND_USR_ID")
	private String sendUsrId;

	@ApiModelProperty(value = "投票编号")
	@TableField("VOTE_NO")
	private String voteNo;
	
	@ApiModelProperty(value = "是否发送投票任务")
	@TableField("IS_SEND")
	private String isSend;

}
