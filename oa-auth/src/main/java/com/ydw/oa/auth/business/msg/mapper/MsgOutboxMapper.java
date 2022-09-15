package com.ydw.oa.auth.business.msg.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.msg.entity.MsgOutbox;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-06-08
 */
public interface MsgOutboxMapper extends BaseMapper<MsgOutbox> {
	IPage<List<Map<String, Object>>> query(Page<MsgOutbox> page, @Param(Constants.WRAPPER) Wrapper<MsgOutbox> wrapper);
}
