package com.ydw.oa.wkflow.business_main.reject.controller;


import java.util.Map;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.datas.mapper.HiDatasMapper;
import com.ydw.oa.wkflow.util.SessionTool;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.reject.dto.RejectQuery;
import com.ydw.oa.wkflow.business_main.reject.entity.Reject;
import com.ydw.oa.wkflow.business_main.reject.mapper.RejectMapper;
import com.ydw.oa.wkflow.business_main.reject.service.IRejectService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author hxj
 * @since 2020-11-16
 */
@Api(description = "驳回管理")
@RestController
@RequestMapping("/cp/reject")
public class RejectController {

	@Autowired
	private RejectMapper rejectMapper;
	@Autowired
	private IRejectService rejectService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private HiDatasMapper hiDatasMapper;
	
	@ApiOperation(value = "驳回分页列表")
	@PostMapping("/page")
	public Wrapper<IPage<Map<String, Object>>> list(@RequestBody(required = false) RejectQuery<Reject> rejectQuery) {
		if (ChkUtil.isNull(rejectQuery.getStartUsrId())) {
			rejectQuery.setStartUsrId(SessionTool.getSessionAdminId());
		}
		IPage<Map<String, Object>> page = rejectMapper.query(rejectQuery,rejectQuery.makeQueryWrapper());

		return WrapMapper.ok(page);
	}
	@ApiOperation(value = "驳回分页列表2")
	@PostMapping("/page2")
	public Wrapper<IPage<Map<String, Object>>> list2(@RequestBody(required = false) RejectQuery<Reject> rejectQuery) {
		if (ChkUtil.isNull(rejectQuery.getStartUsrId())) {
			rejectQuery.setRejectusrid(SessionTool.getSessionAdminId());
		}
		IPage<Map<String, Object>> page2 = rejectMapper.query2(rejectQuery,rejectQuery.makeQueryWrapper2());
		return WrapMapper.ok(page2);
	}
	
	@ApiOperation(value = "撤销任务", notes = "参数传其中任意一个就可以")
	@GetMapping("/revoke")
	public Wrapper<String> revoke(String pid) {
		// 获取流程实例
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(pid).singleResult();
		if (processInstance == null) {
			return WrapMapper.error("流程已结束，无法撤销！");
		}
		rejectService.revoke(pid);
		historyService.deleteHistoricProcessInstance(pid);
//		hiDatasMapper.deleteHiDatas(pid);
		return WrapMapper.ok();
	}
}

