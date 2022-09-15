package com.ydw.oa.auth.business.login;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.login.dto.LoginDto;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business.wxapi.QyWeixinTool;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 用户登录
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Api(description = "用户登录")
@RestController
public class LoginController {

	@Autowired
	private IAuUsrService usrService;

	@ApiOperation(value = "用户登录")
	@PostMapping("/login")
	public Wrapper<AuUsr> list(@ApiParam @Valid @RequestBody LoginDto loginDto) {

		AuUsr usr = usrService.login(loginDto.getUsrName(), loginDto.getPassword());

		if (usr == null) {
			return WrapMapper.error("登录失败,账号密码错误");
		}

		SessionTool.setSessionAdminLoginKey(JsonUtil.toJson(usr));

		return WrapMapper.ok(usr);
	}

	@ApiOperation(value = "企业微信用户登录")
	@GetMapping("/login")
	public Wrapper<AuUsr> list(String code) {

		String userId = QyWeixinTool.getUserId(code);
		QueryWrapper<AuUsr> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("WX_USER_ID", userId);
		AuUsr usr = usrService.getOne(queryWrapper);
		if (usr == null) {
			return WrapMapper.error("登录失败,用户不存在");
		}
		SessionTool.setSessionAdminLoginKey(JsonUtil.toJson(usr));

		return WrapMapper.ok(usr);
	}

}
