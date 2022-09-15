package com.ydw.oa.auth.business.usr.entity;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

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
 * UsrDto 系统用户表
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("T_AU_USR")
@ApiModel(value = "UsrDto 系统用户表", description = "")
public class AuUsr implements Serializable {

	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value = "主键ID")
	@TableId(value = "OBJECT_ID", type = IdType.UUID)
	private String objectId;

	@ApiModelProperty(value = "身份证号")
	@TableField("CERT_NO")
	private String certNo;

	@ApiModelProperty(value = "创建时间")
	@TableField("CREATE_TIME")
	private Date createTime = new Date(System.currentTimeMillis());

	@ApiModelProperty(value = "主要部门ID")
	@TableField("DEPT_ID")
	private String deptId;

	@ApiModelProperty(value = "电子邮件")
	@TableField("EMAIL")
	private String email;

	@ApiModelProperty(value = "是否被删除(0:否;1:是)")
	@TableField("IS_DELETED")
	@TableLogic(value = "0", delval = "1")
	private int isDeleted;

	@ApiModelProperty(value = "最后更新时间")
	@TableField("LAST_UPDATE_TIME")
	private Date lastUpdateTime;

	@ApiModelProperty(value = "电话号码")
	@TableField("MOBILE")
	private String mobile;

	@ApiModelProperty(value = "是否需要重置密码(0:否;1:是)")
	@TableField("NEED_RESETPWD")
	private int needResetpwd;

	@ApiModelProperty(value = "密码")
	@TableField("PASSWD")
	private String passwd;

	@ApiModelProperty(value = "真实姓名")
	@TableField("REAL_NAME")
	private String realName;

	@ApiModelProperty(value = "状态(0:正常;1:离职;2:外出)")
	@TableField("STATUS")
	private int status;

	@ApiModelProperty(value = "用户类型(0:超级管理员;1:一般用户)")
	@TableField("TYPE")
	private int type;

	@ApiModelProperty(value = "用户编号")
	@TableField("USR_ID")
	private String usrId;

	@ApiModelProperty(value = "登录名")
	@TableField("USR_NAME")
	private String usrName;

	@ApiModelProperty(value = "扩展内容")
	@TableField("VARIABLE")
	private Clob variable;
	
	@ApiModelProperty(value = "描述")
	@TableField("NOTE")
	private String note;
	
	@ApiModelProperty(value = "微信userid")
	@TableField("WX_USER_ID")
	private String wxUserId;

	@ApiModelProperty(value = "签名图片url")
	@TableField("SIGN_URL")
	private String signUrl;

	@TableField(exist = false)
	private List<String> sign;

	@TableField(exist = false)
	private List<String> roleIds;
}
