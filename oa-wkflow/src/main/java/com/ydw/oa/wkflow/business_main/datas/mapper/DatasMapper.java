package com.ydw.oa.wkflow.business_main.datas.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
public interface DatasMapper extends BaseMapper<Datas> {

	List<Map<String, Object>> selectOverTimeUndoPids(@Param(Constants.WRAPPER) Wrapper<Datas> queryWrapper);

	List<Map<String, Object>> selectCreateTimeList(String dataFormat, @Param(Constants.WRAPPER) Wrapper<Datas> queryWrapper);

}
