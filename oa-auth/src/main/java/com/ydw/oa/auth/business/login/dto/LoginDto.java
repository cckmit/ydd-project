package com.ydw.oa.auth.business.login.dto;

import javax.validation.constraints.NotBlank;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@ApiModel(value = "LoginDto 用户登录模型", description = "")
@Data
@EqualsAndHashCode(callSuper = false)
public class LoginDto {

	@NotBlank
	@ApiModelProperty(value = "用户名")
	private String usrName;
	@NotBlank
	@ApiModelProperty(value = "密码")
	private String password;

}
