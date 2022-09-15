package com.ydw.oa.auth.business.doc.service;

import com.ydw.oa.auth.business.doc.entity.DocFileDownload;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-11-03
 */
public interface IDocFileDownloadService extends IService<DocFileDownload> {

	void download(String objectId) throws IOException;

}
