package com.ydw.oa.auth.business.msg.mapper;

import com.ydw.oa.auth.business.msg.entity.MsgInbox;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
public interface MsgInboxMapper extends BaseMapper<MsgInbox> {
	IPage<List<Map<String, Object>>> query(Page<MsgInbox> page, @Param(Constants.WRAPPER) Wrapper<MsgInbox> wrapper);

	IPage<List<Map<String, Object>>> listInboxs(Page<MsgInbox> page, @Param(Constants.WRAPPER) Wrapper<MsgInbox> wrapper);

	List<Map<String, Object>> selectListByStatuz(@Param(Constants.WRAPPER) QueryWrapper<MsgInbox> wrapper);
}
