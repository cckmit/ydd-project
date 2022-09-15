package com.ydw.oa.wkflow.business_activiti.instance;

import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ydw.oa.wkflow.business_activiti.instance.service.InstanceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "流程实例图")
@RestController
@RequestMapping("/instance")
public class InstanceImageController {

	@Autowired
	private InstanceService instanceService;

	@ApiOperation(value = "获取图片")
	@ApiImplicitParam(name = "processInstanceId", value = "流程实例id")
	@GetMapping("/getImg")
	public void getImg(String processInstanceId, HttpServletResponse response) throws Exception {
		response.setContentType("image/jpg"); // 设置返回的文件类型

		byte[] bytes = instanceService.getProcessImage(processInstanceId);

		OutputStream os = response.getOutputStream();
		os.write(bytes);
		os.flush();
		os.close();
	}
}
