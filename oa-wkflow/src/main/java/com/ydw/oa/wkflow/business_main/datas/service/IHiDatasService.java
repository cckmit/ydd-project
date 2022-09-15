package com.ydw.oa.wkflow.business_main.datas.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.datas.entity.HiDatas;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-23
 */
public interface IHiDatasService extends IService<HiDatas> {

	HiDatas createHistoryActivitTablesDatas(String objectId, String pId);

	void rollBack(HiDatas hiDatas, String pId, String currentTaskId);

}
