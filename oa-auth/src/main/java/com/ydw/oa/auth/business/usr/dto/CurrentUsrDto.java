package com.ydw.oa.auth.business.usr.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value = "CurrentUsrDto 系统用户对象", description = "")
public class CurrentUsrDto {

	@ApiModelProperty(value = "主键ID")
	private String objectId;
	@ApiModelProperty(value = "身份证号")
	private String certNo;
	@ApiModelProperty(value = "电子邮件")
	private String email;
	@ApiModelProperty(value = "电话号码")
	private String mobile;
	@ApiModelProperty(value = "真实姓名")
	private String realName;
	@ApiModelProperty(value = "描述")
	private String note;
	@ApiModelProperty(value = "微信userid")
	private String wxUserId;
	@ApiModelProperty(value = "签名图片url")
	private String signUrl;

}
