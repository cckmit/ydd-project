package com.ydw.oa.auth.business.msg.controller;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.msg.dto.MsgDraftboxQuery;
import com.ydw.oa.auth.business.msg.entity.MsgDraftbox;
import com.ydw.oa.auth.business.msg.mapper.MsgDraftboxMapper;
import com.ydw.oa.auth.business.msg.service.IMsgDraftboxService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
@ApiIgnore
@Api(description = "草稿箱")
@RestController
@RequestMapping("/cp/draftbox")
@Deprecated
public class MsgDraftboxController {

	@Autowired
	private MsgDraftboxMapper draftboxMapper;
	@Autowired
	private IMsgDraftboxService msgDraftboxService;
	
	@ApiOperation(value = "我的草稿箱列表")
	@PostMapping("/list")
	public Wrapper<IPage<List<Map<String, Object>>>> list(@RequestBody MsgDraftboxQuery<MsgDraftbox> draftBoxQuery) {
		IPage<List<Map<String, Object>>> list = draftboxMapper.query(draftBoxQuery, draftBoxQuery.makeQueryWrapper());
		return WrapMapper.ok(list);
	}
	
	@ApiOperation(value = "添加草稿箱")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody MsgDraftbox msgDraftboxDto) {
		msgDraftboxDto.setUsrId(SessionTool.getSessionAdminId());
		if("全部".equals(msgDraftboxDto.getReceiveType())) {
			msgDraftboxDto.setReceiveUsrId("");
		}
		msgDraftboxService.save(msgDraftboxDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取草稿箱数据")
	@GetMapping("/edit_form")
	public Wrapper<MsgDraftbox> editForm(String objectId) {
		MsgDraftbox msgPo = msgDraftboxService.getById(objectId);
		return WrapMapper.ok(msgPo);
	}

	@ApiOperation(value = "修改草稿箱")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody MsgDraftbox msgDraftboxDto) {
		MsgDraftbox msgPo = msgDraftboxService.getById(msgDraftboxDto.getObjectId());
		msgPo.setContent(msgDraftboxDto.getContent());
		msgPo.setTitle(msgDraftboxDto.getTitle());
		msgPo.setReceiveUsrId(msgDraftboxDto.getReceiveUsrId());
		msgPo.setCopyUsrId(msgDraftboxDto.getCopyUsrId());
		msgPo.setFileId(msgDraftboxDto.getFileId());
		msgPo.setNeedReply(msgDraftboxDto.getNeedReply());
		msgPo.setReceiveType(msgDraftboxDto.getReceiveType());
		if("全部".equals(msgDraftboxDto.getReceiveType())) {
			msgPo.setReceiveUsrId("");
		}
		msgDraftboxService.saveOrUpdate(msgPo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除草稿箱数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		msgDraftboxService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}
}

