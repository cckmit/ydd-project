package com.ydw.oa.wkflow.business_main.wkdoc.service;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
public interface IWkDocFileService extends IService<WkDocFile> {

	void createWkDocFile(String processInstanceId,String code);

	void createPdfAndUpdateWkDocFile() throws IOException;

}
