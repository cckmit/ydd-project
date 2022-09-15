package com.ydw.oa.wkflow.business_main.file.service.impl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ydw.oa.wkflow.util.watermark.excel.ExcelWatermark;
import com.ydw.oa.wkflow.util.watermark.pdf.PdfWatermark;
import com.ydw.oa.wkflow.util.watermark.word.WordTextWatermark;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.image.ImgTools;
import com.ydw.oa.wkflow.business_main.file.entity.File;
import com.ydw.oa.wkflow.business_main.file.mapper.FileMapper;
import com.ydw.oa.wkflow.business_main.file.service.IFileService;
import com.ydw.oa.wkflow.business_main.system.entity.System;
import com.ydw.oa.wkflow.business_main.system.service.ISystemService;
import com.ydw.oa.wkflow.util.WebUtil;
import com.ydw.oa.wkflow.util.date.DateTools;
import com.ydw.oa.wkflow.util.file.FileTools;
import com.ydw.oa.wkflow.util.file.ImgBase64Tools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */



@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements IFileService {
	Logger logger = Logger.getLogger(FileServiceImpl.class.getName());

	@Autowired
	private ISystemService systemService;

	@Autowired
	private FileMapper mapper;


	@Override
	public File saveFile(MultipartFile file) throws IllegalStateException, IOException {
		// TODO 保存文件
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String path = system.getValue();
		String suffix = FileTools.getSuffix(file.getOriginalFilename());

		String file_name = file.getOriginalFilename();

		String new_file_name = DateTools.getCreated() + "." + suffix;
		int year = DateTools.getYear();
		int month = DateTools.getMonth();
		String folder = year + java.io.File.separator + month;
		String folder_url = year + "/" + month;
		// 保存文件
		File tf = new File();
		if (ChkUtil.isNotNull(file_name)) {
			tf.setFileName(file_name);
		} else {
			if (ChkUtil.isNotNull(suffix)) {
				tf.setFileName(file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."))
						+ "." + suffix);
			} else {
				tf.setFileName(file.getOriginalFilename());
			}
		}
		tf.setNewFileName(new_file_name);
		tf.setFolder(folder);
		tf.setFolderUrl(folder_url);
		tf.setContentType(file.getContentType());
		tf.setSize(file.getSize());
		// 获取保存路径
		java.io.File newFile = new java.io.File(
				path + java.io.File.separator + folder + java.io.File.separator + tf.getNewFileName());
		if (!newFile.getParentFile().exists()) {
			newFile.getParentFile().mkdirs();
		}
		FileUtils.copyInputStreamToFile(file.getInputStream(), newFile);
//		file.transferTo(newFile);
		this.save(tf);
		return tf;
	}

	@Override
	public void download(String file_id) throws IOException {
		// TODO 下载文件
		File file = null;

		if (ChkUtil.isNotNull(file_id)) {
			file = this.getById(file_id);
		}


		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();

		ServletOutputStream os = WebUtil.getReponse().getOutputStream();
		FileInputStream fis = null; //文件输入流
		java.io.File img = null; //图片地址
		try {

			if (file != null) {
				String folderName = DATA_PATH + java.io.File.separator + file.getFolder() + java.io.File.separator;
				img = new java.io.File(folderName, file.getNewFileName());//新文件名字
			}
			if (img == null || !img.exists()) {//图片地址为空或不存在
				String folderName = DATA_PATH + java.io.File.separator + "model" + java.io.File.separator;
				img = new java.io.File(folderName, "default.png");
			}
			// 设置页面不缓存
			WebUtil.getReponse().setHeader("Pragma", "No-cache");
			WebUtil.getReponse().setHeader("Cache-Control", "no-cache");
			WebUtil.getReponse().setDateHeader("Expires", 0);
			WebUtil.getReponse().setHeader("content-disposition",
					"attachment;filename=" + URLEncoder.encode(file.getFileName(), "UTF-8"));

			fis = new FileInputStream(img);
			byte[] b = new byte[10240];
			int len = -1;
			while ((len = fis.read(b)) != -1) {
				os.write(b, 0, len);
			}

		} catch (Exception e) {
			log.debug("文件找不到:" + e);
			e.printStackTrace();
		} finally {
			os.close();
			if (fis != null) {
				fis.close();
			}
		}
	}


	@Override
	public void showImg(String file_id, String mw, String mh) throws IOException {
		// TODO 查看图片
		File file = null;
		if (ChkUtil.isNotNull(file_id)) {
			file = this.getById(file_id);
		}

		ServletOutputStream os = WebUtil.getReponse().getOutputStream();
		// 获取保存路径
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();
		java.io.File img = null;
		try {
			if (file != null) {
				String folderName = DATA_PATH + java.io.File.separator + file.getFolderUrl() + java.io.File.separator;
				img = new java.io.File(folderName, file.getNewFileName());
			}
			if (img == null || !img.exists()) {
				String folderName = DATA_PATH + java.io.File.separator + "model" + java.io.File.separator;
				img = new java.io.File(folderName, "default.png");
			}

			int height = ChkUtil.getInteger(mh);
			int width = ChkUtil.getInteger(mw);
			height = height < 0 ? 0 : height;
			width = width < 0 ? 0 : width;

			// 设置页面不缓存
			WebUtil.getReponse().setHeader("Pragma", "No-cache");
			WebUtil.getReponse().setHeader("Cache-Control", "no-cache");
			WebUtil.getReponse().setDateHeader("Expires", 0);

			ImgTools.thumbnail_w_h(img, width, height, os);

		} catch (Exception e) {
			log.debug("图片找不到:" + e);
			e.printStackTrace();
		} finally {
			os.close();
		}
	}

	@Override
	public Map<String, Object> uploadByBase64(String fileData) {
		// TODO 上传base64文件
//		resp.setCharacterEncoding("utf-8");

		String filename = DateTools.getCreated() + ".pdf";
		int year = DateTools.getYear();
		int month = DateTools.getMonth();
		String folder = year + java.io.File.separator + month;
		String folder_url = year + "/" + month;
		// 保存文件
		File tf = new File();
		tf.setFileName("base64_img");
		tf.setNewFileName(filename);
		tf.setFolder(folder);
		tf.setFolderUrl(folder_url);
		tf.setContentType("application/pdf");
		// 获取保存路径
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();

		java.io.File newFile = new java.io.File(
				DATA_PATH + java.io.File.separator + folder + java.io.File.separator + tf.getNewFileName());
		if (!newFile.getParentFile().exists()) {
			newFile.getParentFile().mkdirs();
		}
		ImgBase64Tools.generateImage(fileData, newFile.getAbsolutePath());
		tf.setSize(newFile.length());
		this.save(tf);

		Map<String, Object> end = new HashMap<String, Object>();
		// 发送保存结果
		end.put("file_id", tf.getObjectId());
		end.put("file_name", tf.getFileName());
		end.put("content_type", tf.getContentType());
		return end;
	}

	@Override
	public void addWatermark(String fileId, String code) {
		if (ChkUtil.isNull(fileId)) {
			log.error("fileId为空！！");
			return;
		}

		File file = this.getById(fileId);
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();
		String folderName = DATA_PATH + java.io.File.separator + file.getFolder() + java.io.File.separator;
		String newFileName = file.getNewFileName();
		String filePath = folderName.concat(newFileName);
		String[] suffixs = newFileName.split("\\.");
		String suffix = "";
		if (suffixs.length > 1) {
			suffix = suffixs[1];
		}
		if (ChkUtil.isNull(suffix)) {
			log.error("文件后缀为空，无法识别！");
			return;
		}
		if (suffix.equals("pdf")) {
			PdfWatermark.addWaterMark(filePath, code);
		} else if (suffix.equals("xls") || suffix.equals("xlsx")) {
			ExcelWatermark.addWaterMark(filePath, code, suffix);
		} else if (suffix.equals("doc") || suffix.equals("docx")) {
			WordTextWatermark.addWaterMark(filePath, code);
		} else {
			log.warn("不能添加水印的文件后缀:->" + suffix);
			return;

		}
	}


	@Override
	public void downloadPlanFile(String file_id, HttpServletRequest request, HttpServletResponse response) {
		// TODO 批量下载

		String[] id = file_id.split(",");

		File file = null;
		String folderName = "";
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");

		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();
		String urls1 = "";
		String url = "";
		String fileN ="";
		String fileNs ="";
		//String [] urls = new String[]{};
		for (int i = 0; i < id.length; i++) {
			if (ChkUtil.isNotNull(file_id)) {
				file = this.getById(id[i]);
				if (file != null) {
					folderName = DATA_PATH + java.io.File.separator + file.getFolder() + java.io.File.separator;
					//img = new java.io.File(folderName, file.getNewFileName());//新文件名字
					url = folderName + file.getNewFileName();
					fileN = file.getNewFileName();
					logger.info("==============url================" + url);
					if (StringUtils.isEmpty(urls1)) {
						urls1 = url;

					} else {
						urls1 = urls1 + "," + url;
					}


					if(StringUtils.isEmpty(fileNs)){
						fileNs = fileN;
					}else{
						fileNs = fileNs + "," + fileN;
					}
				}

			}

		}
		String[] urls = urls1.split(",");
		String [] fileNs1 = fileNs.split(",");
 		// 响应头的设置
		//response.reset();
		response.setCharacterEncoding("utf-8");
		response.setContentType("multipart/form-data");

		// 设置压缩包的名字
		// 解决不同浏览器压缩包名字含有中文时乱码的问题
		String downloadName = "MyDown"+".zip";
		String agent = request.getHeader("USER-AGENT");
		try {
			if (agent.contains("MSIE") || agent.contains("Trident")) {
				downloadName = java.net.URLEncoder.encode(downloadName, "UTF-8");
			} else {
				downloadName = new String(downloadName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		response.setHeader("Content-Disposition", "attachment;fileName=\"" + downloadName + "\"");

		// 设置压缩流：直接写入response，实现边压缩边下载
		ZipOutputStream zipos = null;
		try {
			zipos = new ZipOutputStream(new BufferedOutputStream(response.getOutputStream()));
			zipos.setMethod(ZipOutputStream.DEFLATED); // 设置压缩方法
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 循环将文件写入压缩流
		DataOutputStream os = null;
		for (int i = 0; i < urls.length; i++) {
			String fileName = fileNs1[i];
			java.io.File file1 = new java.io.File(urls[i]);
			try {
				// 添加ZipEntry，并ZipEntry中写入文件流
				// 这里，加上i是防止要下载的文件有重名的导致下载失败
				zipos.putNextEntry(new ZipEntry(fileName));
				os = new DataOutputStream(zipos);
				InputStream is = new FileInputStream(file1);
				byte[] b = new byte[100];
				int length = 0;
				while ((length = is.read(b)) != -1) {
					os.write(b, 0, length);
				}
				is.close();
				zipos.closeEntry();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 关闭流
		try {
			os.flush();
			os.close();
			zipos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}




	}

}
