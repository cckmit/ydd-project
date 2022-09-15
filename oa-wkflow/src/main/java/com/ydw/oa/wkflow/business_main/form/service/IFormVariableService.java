package com.ydw.oa.wkflow.business_main.form.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.form.entity.FormVariable;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-19
 */
public interface IFormVariableService extends IService<FormVariable> {

	void createAndUpdateFormVariable(byte[] bytes, String modelId);

}
