package com.ydw.oa.auth.business.doc.service.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.doc.entity.DocFile;
import com.ydw.oa.auth.business.doc.entity.DocFileDownload;
import com.ydw.oa.auth.business.doc.mapper.DocFileDownloadMapper;
import com.ydw.oa.auth.business.doc.service.IDocFileDownloadService;
import com.ydw.oa.auth.business.doc.service.IDocFileService;
import com.ydw.oa.auth.business_wkflow.WkflowFeignService;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-11-03
 */
@Service
public class DocFileDownloadServiceImpl extends ServiceImpl<DocFileDownloadMapper, DocFileDownload>
		implements IDocFileDownloadService {


	@Override
	public void download(String objectId) {
		// TODO 下载文件
		DocFileDownload docFileDownload = this.getById(objectId);
		if ("待下载".equals(docFileDownload.getStatuz())) {
			docFileDownload.setStatuz("已下载");
			docFileDownload.setDownloadTime(new Date());
			this.saveOrUpdate(docFileDownload);
		}
	}

}
