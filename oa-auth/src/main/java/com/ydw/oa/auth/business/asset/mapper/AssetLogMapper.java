package com.ydw.oa.auth.business.asset.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.asset.entity.AssetLog;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
public interface AssetLogMapper extends BaseMapper<AssetLog> {

	IPage<Map<String,Object>> query(Page<AssetLog> query, @Param(Constants.WRAPPER) Wrapper<AssetLog> wrapper);

}
