package com.ydw.oa.wkflow.business_main.wkdoc.service;

import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileHtml;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
public interface IWkDocFileHtmlService extends IService<WkDocFileHtml> {

	void createWkDocFileHTML(String formKey, String processInstanceId, String html);

}
