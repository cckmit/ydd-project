package com.ydw.oa.auth.business.notice.controller;

import java.util.List;
import java.util.Map;

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
import com.ydw.oa.auth.business.notice.dto.NoticeQuery;
import com.ydw.oa.auth.business.notice.entity.Notice;
import com.ydw.oa.auth.business.notice.mapper.NoticeMapper;
import com.ydw.oa.auth.business.notice.service.INoticeService;
import com.ydw.oa.auth.util.SessionTool;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @since 2020-06-15
 */
@Api(description = "通知")
@RestController
@RequestMapping("/cp/notice")
public class NoticeController {

	@Autowired
	private INoticeService noticeService;
	@Autowired
	private NoticeMapper noticeMapper;

	@ApiOperation(value = "通知列表")
	@PostMapping("/list")
	public Wrapper<IPage<Notice>> list(@RequestBody NoticeQuery<Notice> noticeQuery) {
		IPage<Notice> page = noticeService.page(noticeQuery, noticeQuery.makeQueryWrapper());
		return WrapMapper.ok(page);
	}

	@ApiOperation(value = "添加通知")
	@PostMapping("/add")
	public Wrapper<String> add(@RequestBody Notice noticeDto) {
		noticeService.save(noticeDto);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "获取通知数据")
	@GetMapping("/edit_form")
	public Wrapper<Notice> editForm(String objectId) {
		Notice noticePo = noticeService.getById(objectId);
		return WrapMapper.ok(noticePo);
	}

	@ApiOperation(value = "修改通知")
	@PostMapping("/edit")
	public Wrapper<String> edit(@RequestBody Notice noticeDto) {
		Notice noticePo = noticeService.getById(noticeDto.getObjectId());
		noticePo.setContent(noticeDto.getContent());
		noticePo.setFileId(noticeDto.getFileId());
		noticePo.setReceiveType(noticeDto.getReceiveType());
		if ("集团".equals(noticeDto.getReceiveType())) {
			noticePo.setReceiveDeptId("");
		} else {
			noticePo.setReceiveDeptId(noticeDto.getReceiveDeptId());
		}
		noticePo.setTitle(noticeDto.getTitle());
		noticePo.setIsSubmit(noticeDto.getIsSubmit());
		noticeService.saveOrUpdate(noticePo);
		return WrapMapper.ok();
	}

	@ApiOperation(value = "删除通知数据")
	@GetMapping("/del")
	public Wrapper<String> del(String objectId) {
		noticeService.removeById(objectId);
		return WrapMapper.ok("删除成功");
	}

	@ApiOperation(value = "通知列表")
	@PostMapping("/my_list")
	public Wrapper<IPage<List<Map<String, Object>>>> my_list(@RequestBody NoticeQuery<Notice> noticeQuery) {
		QueryWrapper<Notice> queryWrapper = (QueryWrapper<Notice>) noticeQuery.makeQueryWrapper();
		queryWrapper.and(wrapper -> wrapper.eq("RECEIVE_TYPE", "集团").eq("t.IS_DELETED", 0)
				.or(w -> w.eq("RECEIVE_TYPE", "部门").eq("t1.USR_ID", SessionTool.getSessionAdminId())));
		IPage<List<Map<String, Object>>> page = noticeMapper.listPage(noticeQuery, queryWrapper);
		return WrapMapper.ok(page);
	}
}
