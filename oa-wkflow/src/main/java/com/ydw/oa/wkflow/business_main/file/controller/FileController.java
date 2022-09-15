package com.ydw.oa.wkflow.business_main.file.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.file.entity.File;
import com.ydw.oa.wkflow.business_main.file.service.IFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "文件管理")
@RestController
@RequestMapping("/file/file")
public class FileController {

	@Autowired
	private IFileService fileService;

	@ApiOperation(value = "上传文件")
	@ApiImplicitParam(name = "file", value = "上传文件")
	@PostMapping("/upload")
	public Wrapper<File> upload(@RequestParam MultipartFile file) throws Exception {
		File save_file = fileService.saveFile(file);
		save_file.setDownUrl("/oa-wkflow/file/file/download?file_id=" + save_file.getObjectId());
		return WrapMapper.ok(save_file);
	}

	@ApiOperation(value = "下载文件")
	@ApiImplicitParams({ @ApiImplicitParam(name = "file_id", value = "文件id"),
			@ApiImplicitParam(name = "mw", value = "宽"), @ApiImplicitParam(name = "mh", value = "高") })
	@GetMapping("/download")
	public void download(String file_id, String mw, String mh) throws Exception {
		fileService.download(file_id);

	}
    @ApiOperation(value = "批量下载")
	@GetMapping({"/downloadfile"})
	public void downloadfile(String objectId,HttpServletResponse response,HttpServletRequest request){
		fileService.downloadPlanFile(objectId,request,response);
	}

	@ApiOperation(value = "查看图片")
	@ApiImplicitParam(name = "file_id", value = "文件id")
	@GetMapping("/img")
	public void showImg(String file_id, String mw, String mh) throws Exception {
		fileService.showImg(file_id, mw, mh);
	}

	@ApiOperation(value = "通过文件id集合获取文件列表")
	@GetMapping("/files")
	public Wrapper<List<File>> names(String fileIds) {
		List<File> list = (List<File>) fileService.listByIds(Arrays.asList(fileIds.split(",")));
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "上传base64文件")
	@PostMapping("/base64")
	public Wrapper<Map<String, Object>> base64(String fileData) {
		Map<String, Object> result = fileService.uploadByBase64(fileData);
		return WrapMapper.ok(result);
	}
}
