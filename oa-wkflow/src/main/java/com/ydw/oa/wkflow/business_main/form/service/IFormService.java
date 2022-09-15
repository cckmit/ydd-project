package com.ydw.oa.wkflow.business_main.form.service;

import javax.validation.Valid;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
public interface IFormService extends IService<Form> {

	void saveForm(@Valid Form form);

	void updateForm(@Valid Form form);

}
