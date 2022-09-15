package com.ydw.oa.auth.business.suggestions.controller;


import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.suggestions.dto.SuggestionsQuery;
import com.ydw.oa.auth.business.suggestions.entity.Suggestions;
import com.ydw.oa.auth.business.suggestions.mapper.SuggestionsMapper;
import com.ydw.oa.auth.business.suggestions.service.ISuggestionsService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-20
 */
@RestController
@RequestMapping("/cp/suggestions")
public class SuggestionsController {

	@Autowired
	private ISuggestionsService iSuggestionsService;
	
	@Autowired
	private SuggestionsMapper suggestionsMapper;
	
	@ApiOperation(value = "反馈列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String,Object>>>> list(@RequestBody(required = false) SuggestionsQuery<Suggestions> suggestionsQuery) {
		IPage<List<Map<String,Object>>> list = suggestionsMapper.suggestionsquery(suggestionsQuery, suggestionsQuery.makeQueryWrapper());
		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "添加反馈")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody Suggestions suggestions) {
		suggestions.setUserid(SessionTool.getSessionAdminId());
		iSuggestionsService.save(suggestions);
		return WrapMapper.ok("反馈提交成功！我们会耐心审核并采取您的意见，谢谢！");
	}

	@ApiOperation(value = "删除")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iSuggestionsService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}

