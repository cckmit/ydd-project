package com.ydw.oa.wkflow.business_main.datas.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydw.oa.wkflow.business_main.datas.entity.HiDatas;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-23
 */
public interface HiDatasMapper extends BaseMapper<HiDatas> {

	void deleteHiDatas(String processInstanceId);

}
