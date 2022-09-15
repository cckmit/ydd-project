package com.ydw.oa.wkflow.business_main.wkdoc.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.wkdoc.dto.WkDocFileCategoryQuery;
import com.ydw.oa.wkflow.business_main.wkdoc.dto.WkDocFileQuery;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFile;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileMapper;
import com.ydw.oa.wkflow.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
@Api(description = "流程文档管理")
@RestController
@RequestMapping("/cp/wk-doc-file")
public class WkDocFileController {

	@Autowired
	private WkDocFileMapper wkDocFileMapper;
	@Autowired
	private AuthFeignService feignService;

	@ApiOperation(value = "我的文档列表(参与流程)")
	@PostMapping("/page")
	public Wrapper<IPage<Map<String, Object>>> list(
			@RequestBody(required = false) WkDocFileQuery<WkDocFile> wkDocFileQuery) {
		IPage<Map<String, Object>> page = wkDocFileMapper.query(wkDocFileQuery, wkDocFileQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}
	
	@ApiOperation(value = "按照文档分类显示列表")
	@PostMapping("/classify_page")
	public Wrapper<IPage<Map<String, Object>>> classify_page(
			@RequestBody(required = false) WkDocFileCategoryQuery<WkDocFile> wkDocFileQuery) {
		Map<String, Object> user = feignService.getOne(SessionTool.getSessionAdminId()).getResult();
		wkDocFileQuery.setCategory((String) user.get("dept"));
		IPage<Map<String, Object>> page = wkDocFileMapper.query(wkDocFileQuery, wkDocFileQuery.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "按照管理部门显示列表")
	@PostMapping("/dept_page")
	public Wrapper<IPage<Map<String, Object>>> dept_page(
			@RequestBody(required = false) WkDocFileCategoryQuery<WkDocFile> wkDocFileQuery) {
		Map<String, Object> user = feignService.getOne(SessionTool.getSessionAdminId()).getResult();
		wkDocFileQuery.setDeptId((String) user.get("deptId"));
		IPage<Map<String, Object>> page = wkDocFileMapper.query(wkDocFileQuery, wkDocFileQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}
}
