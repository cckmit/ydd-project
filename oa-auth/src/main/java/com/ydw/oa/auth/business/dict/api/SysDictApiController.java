package com.ydw.oa.auth.business.dict.api;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.oa.auth.business.dict.entity.SysDict;
import com.ydw.oa.auth.business.dict.mapper.SysDictMapper;
import com.ydw.oa.auth.util.tree.TreeDictTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-09
 */
@Api(description = "字典值管理")
@RestController
@RequestMapping("/api/dict")
public class SysDictApiController {

	@Autowired
	private SysDictMapper sysDictMapper;

	@ApiOperation(value = "字典值树列表")
	@GetMapping("/list")
	public List<Map<String, Object>> list(String type_code) {

		QueryWrapper<SysDict> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("TYPE_CODE", type_code);
		queryWrapper.eq("IS_DELETE", 0);
		List<Map<String, Object>> list = sysDictMapper.dictList(queryWrapper);
		return TreeDictTools.listMapToTree(list);
	}

}
