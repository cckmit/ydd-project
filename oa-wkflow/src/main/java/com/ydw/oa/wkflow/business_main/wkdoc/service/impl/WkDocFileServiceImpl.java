package com.ydw.oa.wkflow.business_main.wkdoc.service.impl;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import com.ydw.oa.wkflow.util.HtmlToPDF;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.JsonUtil;
import com.ydw.oa.wkflow.business_auth.AuthFeignService;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.file.entity.File;
import com.ydw.oa.wkflow.business_main.file.service.IFileService;
import com.ydw.oa.wkflow.business_main.form.entity.Form;
import com.ydw.oa.wkflow.business_main.form.service.IFormService;
import com.ydw.oa.wkflow.business_main.system.entity.System;
import com.ydw.oa.wkflow.business_main.system.service.ISystemService;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFile;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileHtml;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileMapper;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileHtmlService;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileService;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileUsrService;
import com.ydw.oa.wkflow.business_wkflow.form.service.FormValsService;
import com.ydw.oa.wkflow.util.date.DateTools;
import com.ydw.oa.wkflow.util.file.FileTools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
@Service
@CommonsLog
public class WkDocFileServiceImpl extends ServiceImpl<WkDocFileMapper, WkDocFile> implements IWkDocFileService {

	@Autowired
	private IWkDocFileHtmlService wkDocFileHtmlService;
	@Autowired
	private IWkDocFileUsrService wkDocFileUsrService;
	@Autowired
	private IDatasService datasService;
	@Autowired
	private FormValsService formValsService;
	@Autowired
	private IFormService formService;
	@Autowired
	private ISystemService systemService;
	@Autowired
	private IFileService fileService;
	@Autowired
	private AuthFeignService authFeignService;

	@Override
	@Transactional
	public void createWkDocFile(String processInstanceId,String code) {
		// TODO 生成对应的流程文档记录和流程文档员工关联表
		QueryWrapper<WkDocFileHtml> qw = new QueryWrapper<WkDocFileHtml>();
		qw.eq("PROCESS_INSTANCE_ID", processInstanceId);
		List<WkDocFileHtml> list = wkDocFileHtmlService.list(qw);

		Datas firstTaskData = formValsService.getFirstForm(processInstanceId);

		for (WkDocFileHtml wkDocFileHtml : list) {
			QueryWrapper<Datas> queryWrapper = new QueryWrapper<Datas>();
			queryWrapper.eq("ACTI_PROC_INST_ID", processInstanceId);
			queryWrapper.eq("FORM_KID", wkDocFileHtml.getFormId());
			queryWrapper.orderByDesc("SORTZ");
			List<Datas> datas = datasService.list(queryWrapper);
			Datas data = datas.get(0);

			Form form = formService.getById(wkDocFileHtml.getFormId());
			WkDocFile file = new WkDocFile();
			file.setCategoryId(data.getCategoryId());
			JSONObject jsonObject = JsonUtil.jsonStrToJsonObject(data.getFormValsJson());
			jsonObject.put("oa-text-code-0", code);
			data.setFormValsJson(jsonObject.toJSONString());
			datasService.saveOrUpdate(data);
//			String code = jsonObject.getString("oa-text-code-0");
//			if (ChkUtil.isNull(code)) {
//				code = jsonObject.getString("oa-text-code-1");
//			}
			file.setCode(code);
			file.setUsrId(firstTaskData.getAssigner());
			List<String> depts = authFeignService.getDepts(firstTaskData.getAssigner()).getResult();
			file.setDetpId(Joiner.on(",").join(depts));
			file.setFormId(data.getFormKid());
			file.setName(form.getName());
			file.setPid(processInstanceId);
			file.setStartTime(firstTaskData.getCreateTime());
			this.save(file);

			wkDocFileUsrService.createWkDocFileUsrs(processInstanceId, file.getObjectId());
		}

	}

	@Override
	@Transactional
	public void createPdfAndUpdateWkDocFile() throws IOException {
		// TODO 查询流程文档记录, 生成并上传pdf到文件
		QueryWrapper<WkDocFile> queryWrapper = new QueryWrapper<WkDocFile>();
		queryWrapper.eq("IS_CREATE_PDF", 0);
		List<WkDocFile> list = this.list(queryWrapper);
		for (WkDocFile wkDocFile : list) {
			QueryWrapper<WkDocFileHtml> qw = new QueryWrapper<WkDocFileHtml>();
			qw.eq("PROCESS_INSTANCE_ID", wkDocFile.getPid());
			qw.eq("FORM_ID", wkDocFile.getFormId());
			WkDocFileHtml docFileHtml = wkDocFileHtmlService.getOne(qw);

			String file_id = this.writeTempFileAndCreatePdf(wkDocFile, docFileHtml);
			if (ChkUtil.isNotNull(file_id)) {
				wkDocFile.setIsCreatePdf(1);
				wkDocFile.setFileId(file_id);
				this.saveOrUpdate(wkDocFile);
			}
		}
	}

	private String writeTempFileAndCreatePdf(WkDocFile wkDocFile, WkDocFileHtml docFileHtml) throws IOException {
		// 生成pdf
		QueryWrapper<System> queryWrapper = new QueryWrapper<System>();
		queryWrapper.eq("NAME", "DATA-PATH");
		System system = systemService.getOne(queryWrapper);
		String DATA_PATH = system.getValue();
		int year = DateTools.getYear();
		int month = DateTools.getMonth();
		String folder = year + java.io.File.separator + month;
		String folder_url = year + "/" + month;
		String filename = DateTools.getCreated() + ".pdf";
		String destPath = DATA_PATH + java.io.File.separator + folder + java.io.File.separator + filename;

		String osName = java.lang.System.getProperties().getProperty("os.name");
		java.io.File pdfDest = new java.io.File(destPath);
		if (!pdfDest.getParentFile().exists()) {
			pdfDest.getParentFile().mkdirs();
		}
		if (!osName.toLowerCase().contains("linux")) {
			HtmlToPDF.convert(destPath, wkDocFile.getPid(), wkDocFile.getFormId());
		} else {
			// TODO 生成临时html，生成pdf，保存
			String html = docFileHtml.getHtmlContent();
			if (ChkUtil.isNull(html)) {
				return null;
			}
			// 获取模板路径
			QueryWrapper<System> qw = new QueryWrapper<System>();
			qw.eq("NAME", "MODEL-PATH");
			System model = systemService.getOne(qw);
			String MODEL_PATH = model.getValue();
			String doc = FileTools.readFileToString(MODEL_PATH.concat(java.io.File.separator).concat("pdfModel.html"));
			doc = doc.replace("@content", html);
//			String tempPath = MODEL_PATH + java.io.File.separator + wkDocFile.getPid() + ".html";
//			FileTools.writeFile(doc, tempPath);

//			java.io.File htmlSource = new java.io.File(tempPath);

			ConverterProperties converterProperties = new ConverterProperties();
			HtmlConverter.convertToPdf(doc, new FileOutputStream(pdfDest), converterProperties);

		}

		// 保存文件
		File tf = new File();
		tf.setFileName(filename);
		tf.setNewFileName(filename);
		tf.setFolder(folder);
		tf.setFolderUrl(folder_url);
		tf.setContentType("application/pdf");
		tf.setSize(pdfDest.length());
		fileService.save(tf);
		return tf.getObjectId();

		// 删除临时html文件
//		new java.io.File(tempPath).delete();
	}

}
