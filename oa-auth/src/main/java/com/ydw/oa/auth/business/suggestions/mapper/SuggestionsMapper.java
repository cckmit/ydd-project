package com.ydw.oa.auth.business.suggestions.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.suggestions.entity.Suggestions;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-20
 */
public interface SuggestionsMapper extends BaseMapper<Suggestions> {

	IPage<List<Map<String,Object>>> suggestionsquery(Page<Suggestions> page, @Param(Constants.WRAPPER) Wrapper<Suggestions> auggestionsWrapper);
}
