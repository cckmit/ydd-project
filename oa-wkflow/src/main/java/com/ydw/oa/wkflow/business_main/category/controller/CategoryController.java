package com.ydw.oa.wkflow.business_main.category.controller;

import java.util.List;

import javax.validation.Valid;

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
import com.ydw.oa.wkflow.business_main.category.dto.CategoryQuery;
import com.ydw.oa.wkflow.business_main.category.entity.Category;
import com.ydw.oa.wkflow.business_main.category.service.ICategoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "流程分类管理")
@RestController
@RequestMapping("/category/category")
public class CategoryController {

	@Autowired
	private ICategoryService iCategoryService;

	@ApiOperation(value = "流程分类列表")
	@GetMapping("/list")
	public Wrapper<List<Category>> list() {
		QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderBy(true, true, "CODE");
		List<Category> list = iCategoryService.list(queryWrapper);
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "流程分类分页列表")
	@PostMapping("/page")
	public Wrapper<IPage<Category>> list(@RequestBody(required = false) CategoryQuery<Category> categoryQuery) {
		
		IPage<Category> page = iCategoryService.page(categoryQuery,categoryQuery.makeQueryWrapper());

		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加流程分类")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody Category category) {
		iCategoryService.save(category);

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除流程分类")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iCategoryService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取流程分类数据")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<Category> editForm(String objectId) {
		Category opFunc = iCategoryService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑流程分类")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody Category category) {
		Category categoryDb = iCategoryService.getById(category.getObjectId());
		categoryDb.setType(category.getType());
		categoryDb.setCode(category.getCode());
		iCategoryService.saveOrUpdate(categoryDb);

		return WrapMapper.ok("保存成功");
	}
}
