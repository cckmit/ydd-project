package com.ydw.oa.auth.business.msg.entity;

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
 * 回复
 * </p>
 *
 * @author hxj
 * @since 2020-09-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MsgReply对象", description="")
public class MsgReply implements Serializable {

    private static final long serialVersionUID=1L;

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

    @ApiModelProperty(value = "发件id")
    @TableField("INBOX_ID")
    private String inboxId;

    @ApiModelProperty(value = "回复内容")
    @TableField("CONTENT")
    private String content;

    @ApiModelProperty(value = "附件")
    @TableField("FILE_ID")
    private String fileId;

    @ApiModelProperty(value = "状态：通过 or 驳回")
    @TableField("STATUZ")
    private String statuz;

    @ApiModelProperty(value = "驳回原因")
    @TableField("REASON")
    private String reason;


}
