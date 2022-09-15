package com.ydw.oa.auth.business.doc.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.doc.entity.DocFile;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @since 2020-06-15
 */
public interface DocFileMapper extends BaseMapper<DocFile> {

	IPage<List<Map<String, Object>>> query(Page<DocFile> page, @Param(Constants.WRAPPER) Wrapper<DocFile> wrapper);

}
