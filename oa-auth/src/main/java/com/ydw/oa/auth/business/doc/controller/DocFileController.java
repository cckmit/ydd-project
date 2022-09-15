package com.ydw.oa.auth.business.doc.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ydw.oa.auth.util.SessionTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.doc.dto.DocFileDownloadQuery;
import com.ydw.oa.auth.business.doc.dto.DocFileQuery;
import com.ydw.oa.auth.business.doc.entity.DocFile;
import com.ydw.oa.auth.business.doc.entity.DocFileDownload;
import com.ydw.oa.auth.business.doc.mapper.DocFileDownloadMapper;
import com.ydw.oa.auth.business.doc.mapper.DocFileMapper;
import com.ydw.oa.auth.business.doc.service.IDocFileDownloadService;
import com.ydw.oa.auth.business.doc.service.IDocFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2020-06-15
 */
@Api(description = "文件、文件夹管理")
@RestController
@RequestMapping("/cp/doc-file")
public class DocFileController {

	@Autowired
	private IDocFileService docFileService;
	@Autowired
	private DocFileMapper docFileMapper;
	@Autowired
	private DocFileDownloadMapper docFileDownloadMapper;
	@Autowired
	private IDocFileDownloadService docFileDownloadService;
	@Autowired
	private DocFileDownloadMapper downloadMapper;

	@ApiOperation(value = "文档列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String, Object>>>> list(@RequestBody DocFileQuery<DocFile> query) {
		query.setCreateUserId(SessionTool.getSessionAdminId());
		IPage<List<Map<String, Object>>> list = docFileMapper.query(query, query.makeQueryWrapper());
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "文档上传")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody DocFile docFileDTO) {
		docFileService.add(docFileDTO);
		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "获取当前文档权限")
	@GetMapping("/getAuth")
	public Wrapper<JSONObject> getAuth(String objectId) {
		DocFile docFile = docFileService.getById(objectId);
		List<Map<String, Object>> list = docFileDownloadMapper.queryUserByDocFileId(objectId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("docFile", docFile);
		jsonObject.put("usrs",list);
		return WrapMapper.ok(jsonObject);
	}

	@ApiOperation(value = "修改权限")
	@PostMapping("/change")
	public Wrapper<String> change(@RequestBody DocFile docFileDTO) {
		docFileService.change(docFileDTO);
		return WrapMapper.ok("重新分配权限成功");
	}

	@ApiOperation(value = "删除")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		docFileService.delFile(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "下载详情")
	@PostMapping("/download_detail")
	public Wrapper<IPage<List<Map<String, Object>>>> download_detail(@RequestBody DocFileDownloadQuery<DocFile> query) {
		IPage<List<Map<String, Object>>> list = downloadMapper.query(query, query.makeQueryWrapper());
		return WrapMapper.ok(list);
	}
}
