package com.ydw.oa.wkflow.business_activiti.model;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Api(description = "流程图片管理")
@RestController
@RequestMapping("/model")
public class ActivitiModelImageController {

	@Autowired
	private RepositoryService repositoryService;

	@ApiOperation(value = "获取图片-base64")
	@ApiImplicitParam(name = "modelId", value = "模型id")
	@GetMapping("/getImgBase64")
	public Wrapper<String> getImgBase64(String modelId) {
		// 模型模块
		byte[] bytes = repositoryService.getModelEditorSourceExtra(modelId);
		String image = Base64.getEncoder().encodeToString(bytes);
		return WrapMapper.ok("data:image/png;base64," + image);

	}          

	@ApiOperation(value = "获取图片")
	@ApiImplicitParam(name = "modelId", value = "模型id")
	@GetMapping("/getImg")
	public void getImg(String modelId, HttpServletResponse response) throws IOException {
		response.setContentType("image/jpg"); // 设置返回的文件类型

		// 模型模块
		byte[] bytes = repositoryService.getModelEditorSourceExtra(modelId);

		OutputStream os = response.getOutputStream();
		if(ChkUtil.isNotNull(bytes)) {
			os.write(bytes);
			os.flush();
			os.close();
		}
	}

	@ApiOperation(value = "获取图片-通过deploymentId")
	@ApiImplicitParam(name = "deploymentId", value = "流程deploymentId")
	@GetMapping("/getImgByDeploymentId")
	public void getImgByDeploymentId(String deploymentId, HttpServletResponse response) throws IOException {
		Model model = repositoryService.createModelQuery().deploymentId(deploymentId).singleResult();

		String modelId = model.getId();
		getImg(modelId, response);
	}

}
