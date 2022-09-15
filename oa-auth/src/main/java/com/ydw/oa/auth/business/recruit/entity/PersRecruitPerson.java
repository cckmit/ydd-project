package com.ydw.oa.auth.business.recruit.entity;

import java.io.Serializable;
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
 * @since 2020-07-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("pers_recruit_person")
@ApiModel(value = "PersRecruitPerson对象", description = "")
public class PersRecruitPerson implements Serializable {

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

	@ApiModelProperty(value = "应聘人姓名")
	@TableField("REAL_NAME")
	private String realName;

	@ApiModelProperty(value = "身份证号")
	@TableField("CERT_NO")
	private String certNo;

	@ApiModelProperty(value = "电子邮件")
	@TableField("EMAIL")
	private String email;

	@ApiModelProperty(value = "电话号码")
	@TableField("MOBILE")
	private String mobile;

	@ApiModelProperty(value = "应聘岗位id")
	@TableField("RECRUIT_ID")
	private String recruitId;
	
	@ApiModelProperty(value = "应聘岗位")
	@TableField("RECRUIT_NAME")
	private String recruitName;

	@ApiModelProperty(value = "是否录取  是 or 否")
	@TableField("IS_ENROLL")
	private String isEnroll;

}
