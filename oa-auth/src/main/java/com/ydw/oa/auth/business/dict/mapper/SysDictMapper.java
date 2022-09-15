package com.ydw.oa.auth.business.dict.mapper;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ydw.oa.auth.business.dict.entity.SysDict;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-09
 */
public interface SysDictMapper extends BaseMapper<SysDict> {

	List<Map<String, Object>> dictList(@Param(Constants.WRAPPER) Wrapper<SysDict> dictWrapper);
}
