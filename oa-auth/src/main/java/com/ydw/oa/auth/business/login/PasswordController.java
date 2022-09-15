package com.ydw.oa.auth.business.login;

import javax.validation.Valid;

import com.tmsps.fk.common.util.Md5Util;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.login.dto.PasswordDto;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.SessionTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 密码管理
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "用户密码管理")
@RestController
@RequestMapping("/cp")
public class PasswordController {

	@Autowired
	private IAuUsrService usrService;

	@ApiOperation(value = "修改密码")
	@PostMapping("/changePwd")
	public Wrapper<String> changePwd(@ApiParam @Valid @RequestBody PasswordDto passwordDto) {

		if (!passwordDto.getNewPassword1().equals(passwordDto.getNewPassword2())) {
			return WrapMapper.error("修改失败,两次输入的密码不一致.");
		}

		String oldPwd = Md5Util.md5(passwordDto.getOldPassword());
		String adminId = SessionTool.getSessionAdminId();
		AuUsr usr = usrService.getById(adminId);
		if (!usr.getPasswd().equals(oldPwd)) {
			return WrapMapper.error("修改失败,密码输入错误.");
		}
		usr.setPasswd(Md5Util.md5(passwordDto.getNewPassword1()));
		usrService.saveOrUpdate(usr);

		return WrapMapper.ok("修改成功");
	}

	@ApiOperation(value = "重置当前登录用户密码为 ： 123456")
	@GetMapping("/resetPwd")
	public Wrapper<String> resetPwd() {

		String adminId = SessionTool.getSessionAdminId();
		AuUsr usr = usrService.getById(adminId);
		usr.setPasswd(Md5Util.md5("123456"));
		usrService.saveOrUpdate(usr);

		return WrapMapper.ok("重置成功,新密码为: 123456");
	}

	@ApiOperation(value = "重置用户密码为 ： 123456")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/resetUserPwd")
	public Wrapper<String> resetUserPwd(String objectId) {

		AuUsr usr = usrService.getById(objectId);
		usr.setPasswd(Md5Util.md5("123456"));
		usrService.saveOrUpdate(usr);

		return WrapMapper.ok("重置成功,新密码为: 123456");
	}

}
