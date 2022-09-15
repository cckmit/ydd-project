package com.ydw.oa.wkflow.business_main.wkdoc.service;

import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileUsr;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
public interface IWkDocFileUsrService extends IService<WkDocFileUsr> {

	void createWkDocFileUsrs(String processInstanceId, String wkDocFileId);

}
