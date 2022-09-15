package com.ydw.oa.wkflow.business_main.delegate.controller;

import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.delegate.dto.DelegateQuery;
import com.ydw.oa.wkflow.business_main.delegate.entity.Delegate;
import com.ydw.oa.wkflow.business_main.delegate.service.IDelegateService;
import com.ydw.oa.wkflow.business_main.delegate_business.entity.DelegateBusiness;
import com.ydw.oa.wkflow.business_main.delegate_business.service.IDelegateBusinessService;

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
@Api(description = "委托管理")
@RestController
@RequestMapping("/delegate/delegate")
public class DelegateController {

	@Autowired
	private IDelegateService iDelegateService;

	@Autowired
	private IDelegateBusinessService iDelegateBusinessService;

	@ApiOperation(value = "委托管理列表")
	@PostMapping("/list")
	public Wrapper<IPage<Delegate>> list(@RequestBody DelegateQuery<Delegate> delegateQuery) {
		IPage<Delegate> list = iDelegateService.page(delegateQuery, delegateQuery.makeQueryWrapper());

		return WrapMapper.ok(list);
	}

	@ApiOperation(value = "添加委托")
	@PostMapping("/add")
	public Wrapper<String> add(@ApiParam @Valid @RequestBody Delegate delegate) {
		iDelegateService.save(delegate);

		JSONArray array = JSON.parseArray(delegate.getBusiness());
		for (int i = 0; i < array.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) array.get(i);
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			DelegateBusiness db = new DelegateBusiness();
			db.setBusinessKey(id);
			db.setBusinessName(name);
			db.setDelegateId(delegate.getObjectId());
			iDelegateBusinessService.save(db);
		}

		return WrapMapper.ok("保存成功");
	}

	@ApiOperation(value = "删除委托")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/delete")
	public Wrapper<String> delete(String objectId) {
		iDelegateService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "编辑获取委托")
	@ApiImplicitParam(name = "objectId", value = "主键id")
	@GetMapping("/edit_form")
	public Wrapper<Delegate> editForm(String objectId) {
		Delegate opFunc = iDelegateService.getById(objectId);
		return WrapMapper.ok(opFunc);
	}

	@ApiOperation(value = "编辑委托")
	@PostMapping("/edit")
	public Wrapper<String> edit(@Valid @RequestBody Delegate delegate) {
		Delegate delegateDb = iDelegateService.getById(delegate.getObjectId());
		delegateDb.setBusiness(delegate.getBusiness());
		delegateDb.setEnd(delegate.getEnd());
		delegateDb.setStart(delegate.getStart());
		delegateDb.setReason(delegate.getReason());
		delegateDb.setUserdName(delegate.getUserdName());
		delegateDb.setUserName(delegate.getUserName());
		delegateDb.setStatuz(delegate.getStatuz());
		delegateDb.setTitle(delegate.getTitle());
		delegateDb.setUser(delegate.getUser());
		delegateDb.setUserd(delegate.getUserd());
		iDelegateService.saveOrUpdate(delegateDb);

		QueryWrapper<DelegateBusiness> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("DELEGATE_ID", delegate.getObjectId());
		iDelegateBusinessService.remove(queryWrapper);
		JSONArray array = JSON.parseArray(delegate.getBusiness());
		for (int i = 0; i < array.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, Object> map = (Map<String, Object>) array.get(i);
			String id = map.get("id").toString();
			String name = map.get("name").toString();
			DelegateBusiness db = new DelegateBusiness();
			db.setBusinessKey(id);
			db.setBusinessName(name);
			db.setDelegateId(delegate.getObjectId());
			iDelegateBusinessService.save(db);
		}

		return WrapMapper.ok("保存成功");
	}

}
