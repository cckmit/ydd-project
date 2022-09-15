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
import com.ydw.oa.auth.business.doc.entity.DocFileDownload;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-11-03
 */
public interface DocFileDownloadMapper extends BaseMapper<DocFileDownload> {

	IPage<List<Map<String, Object>>> query(Page<DocFile> page, @Param(Constants.WRAPPER) Wrapper<DocFile> wrapper);

	List<Map<String,Object>> queryUserByDocFileId(String docFileId);

}
