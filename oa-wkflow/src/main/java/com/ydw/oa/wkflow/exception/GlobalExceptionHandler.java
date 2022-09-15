package com.ydw.oa.wkflow.exception;

import com.tmsps.fk.common.base.enums.ErrorCodeEnum;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.JsonUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alibaba.fastjson.JSON;

/**
 * 全局的的异常拦截器
 *
 * @author 冯晓东 398479251@qq.com
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(getClass());

	/**
	 * 参数非法异常.
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseBody
	public Wrapper<String> illegalArgumentException(IllegalArgumentException e) {
		log.error("参数非法异常={}", e.getMessage());
		return WrapMapper.wrap(ErrorCodeEnum.GL99990100.code(), e.getMessage());
	}

	/**
	 * 参数非法异常.
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(BindException.class)
	@ResponseBody
	public Wrapper<String> bindException(BindException e) {

		final StringBuilder errorMsg = new StringBuilder();
		e.getAllErrors().forEach(action -> {
			final String fmt = "[%s]%s;";
			String code = JSON.parseObject(JsonUtil.toJson(action.getArguments()[0])).getString("code");
			String msg = String.format(fmt, code, action.getDefaultMessage());
			errorMsg.append(msg);
		});
		// log.error("参数非法异常={}", e.getMessage(), e);
		return WrapMapper.wrap(ErrorCodeEnum.GL99990100.code(), errorMsg.toString());
	}

	/**
	 * 参数非法异常.
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public Wrapper<String> methodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.error("参数非法异常={}", e.getMessage(), e);
		return WrapMapper.wrap(ErrorCodeEnum.GL99990100.code(), e.getMessage());
	}

	/**
	 * 业务异常.
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(BusinessException.class)
	@ResponseBody
	public Wrapper<String> businessException(BusinessException e) {
		log.error("业务异常={}", e.getMessage());
		return WrapMapper.wrap(e.getCode() == 0 ? Wrapper.ERROR_CODE : e.getCode(), e.getMessage());
	}

	/**
	 * 全局异常.
	 *
	 * @return the wrapper
	 */
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public Wrapper<String> exception(Exception e) {
		e.printStackTrace();
		log.error("保存全局异常信息 ex={}", e.getMessage());
		return WrapMapper.error(e.getMessage());
	}
}
