package com.ydw.oa.wkflow.business_main.file.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import com.aliyuncs.http.HttpResponse;
import org.springframework.http.HttpRequest;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wkflow.business_main.file.entity.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
public interface IFileService extends IService<File> {

	File saveFile(MultipartFile file) throws IllegalStateException, IOException;

	void download(String file_id) throws IOException;

	void showImg(String file_id, String mw, String mh) throws IOException;

	Map<String, Object> uploadByBase64(String fileData);

	void addWatermark(String fileId,String code);

	void downloadPlanFile(String objectId,HttpServletRequest request, HttpServletResponse response);



}
