package com.ydw.oa.auth.business.login.dto;

import javax.validation.constraints.NotBlank;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "PasswordDto 修改密码 DTO", description = "")
@Data
@EqualsAndHashCode(callSuper = false)
public class PasswordDto {

	@NotBlank
	@ApiModelProperty(value = "原有密码")
	private String oldPassword;
	@NotBlank
	@ApiModelProperty(value = "新密码")
	private String newPassword1;
	@NotBlank
	@ApiModelProperty(value = "新密码")
	private String newPassword2;

}
