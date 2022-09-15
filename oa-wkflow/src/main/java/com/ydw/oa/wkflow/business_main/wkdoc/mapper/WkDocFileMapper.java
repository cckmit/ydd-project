package com.ydw.oa.wkflow.business_main.wkdoc.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFile;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
public interface WkDocFileMapper extends BaseMapper<WkDocFile> {

	IPage<Map<String, Object>> query(Page<WkDocFile> page, @Param(Constants.WRAPPER) Wrapper<WkDocFile> wrapper);

	IPage<Map<String, Object>> queryusr(Page<WkDocFile> page, @Param(Constants.WRAPPER) Wrapper<WkDocFile> wrapper);

}
