package com.ydw.oa.wkflow.business_main.datas.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
public interface IDatasService extends IService<Datas> {

	void deleteOverTimeTasks();

	void rollBack(Datas datas);

	JSONObject getFormData(String pid);

}
