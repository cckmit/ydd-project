package com.ydw.oa.wkflow.util.token;

import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ydw.oa.wkflow.util.WebUtil;

@RestController
public class TokenController {

	/**
	 * 
	 * @param key
	 * @return
	 */
	@GetMapping("/getToken")
	public String getToken(String key) {
		if (key == null || "".equals(key.trim())) {
			throw new RuntimeException("500:Parameter <key> can not be null.");
		}

		// 设置token值
		String token = key + "@@" + UUID.randomUUID();
		WebUtil.getSession().setAttribute("token@@" + key, token);
		return token;
	}

}
