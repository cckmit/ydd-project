package com.ydw.oa.auth.business.doc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.doc.entity.DocFile;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @since 2020-06-15
 */
public interface IDocFileService extends IService<DocFile> {

	void add(DocFile docFileDTO);

	void change(DocFile docFileDTO);

	void delFile(String objectId);

}
