package com.ydw.oa.auth.business.doc.controller;


import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.doc.dto.DocFileDownloadQuery;
import com.ydw.oa.auth.business.doc.entity.DocFile;
import com.ydw.oa.auth.business.doc.mapper.DocFileDownloadMapper;
import com.ydw.oa.auth.business.doc.service.IDocFileDownloadService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-11-03
 */
@RestController
@RequestMapping("/cp/doc-file-download")
public class DocFileDownloadController {

	@Autowired
	private IDocFileDownloadService docFileDownloadService;
	@Autowired
	private DocFileDownloadMapper downloadMapper;
	
	@ApiOperation(value = "下载文件")
	@GetMapping("/download/{objectId}")
	public Wrapper<String> download(@PathVariable String objectId) throws IOException  {
		docFileDownloadService.download(objectId);
		return WrapMapper.ok("下载成功");
	}
	
	@ApiOperation(value = "我的文档列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String, Object>>>> list(@RequestBody DocFileDownloadQuery<DocFile> query) {
		query.setUsrId(SessionTool.getSessionAdminId());
		IPage<List<Map<String, Object>>> list = downloadMapper.query(query, query.makeQueryWrapper());
		return WrapMapper.ok(list);
	}
}

