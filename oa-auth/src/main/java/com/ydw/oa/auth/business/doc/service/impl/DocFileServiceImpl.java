package com.ydw.oa.auth.business.doc.service.impl;

import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.doc.entity.DocFile;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.doc.entity.DocFileDownload;
import com.ydw.oa.auth.business.doc.mapper.DocFileMapper;
import com.ydw.oa.auth.business.doc.service.IDocFileDownloadService;
import com.ydw.oa.auth.business.doc.service.IDocFileService;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.util.SessionTool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @since 2020-06-15
 */
@Service
public class DocFileServiceImpl extends ServiceImpl<DocFileMapper, DocFile> implements IDocFileService {

	@Autowired
	private IDocFileDownloadService docFileDownloadService;
	@Autowired
	private IAuUsrDeptService usrDeptService;
	@Autowired
	private IAuUsrService usrService;

	@Override
	@Transactional
	public void add(DocFile docFileDTO) {
		// TODO 上传
		docFileDTO.setCreateUsrId(SessionTool.getSessionAdminId());
		String deptId = usrDeptService.getUsrDept(SessionTool.getSessionAdminId());
		docFileDTO.setDeptId(deptId);
		this.save(docFileDTO);
		//判断分配的权限范围
		int scope = docFileDTO.getScope();
		List<String> usrIds = new ArrayList<>();
		if(scope == 1){
			List<AuUsr> list = usrService.list();
			for (AuUsr usr : list) {
				usrIds.add(usr.getObjectId());
			}
		}else if(scope == 2){
			QueryWrapper<AuUsrDept> auUsrQueryWrapper = new QueryWrapper<>();
			auUsrQueryWrapper.eq("DEPT_ID",deptId);
			List<AuUsrDept> list = usrDeptService.list(auUsrQueryWrapper);
			for (AuUsrDept usrDept : list) {
				usrIds.add(usrDept.getUsrId());
			}
		}else{
			usrIds = docFileDTO.getUsrIds();
		}
		for (String usrId : usrIds) {
			DocFileDownload entity = new DocFileDownload();
			entity.setDocFileId(docFileDTO.getObjectId());
			entity.setUsrId(usrId);
			entity.setStatuz("待下载");
			docFileDownloadService.save(entity);
		}

	}

	@Override
	@Transactional
	public void change(DocFile docFileDTO) {
		// TODO 修改权限
		// 删除原有权限
		QueryWrapper<DocFileDownload> queryWrapper = new QueryWrapper<DocFileDownload>();
		queryWrapper.eq("DOC_FILE_ID", docFileDTO.getObjectId());
		String deptId = usrDeptService.getUsrDept(SessionTool.getSessionAdminId());

		//判断分配的权限范围
		int scope = docFileDTO.getScope();
		DocFile docFile = this.getById(docFileDTO.getObjectId());
		docFile.setScope(scope);
		this.updateById(docFile);
		List<String> usrIds = new ArrayList<>();
		if(scope == 1){
			List<AuUsr> list = usrService.list();
			for (AuUsr usr : list) {
				usrIds.add(usr.getObjectId());
			}
		}else if(scope == 2){
			QueryWrapper<AuUsrDept> auUsrQueryWrapper = new QueryWrapper<>();
			auUsrQueryWrapper.eq("DEPT_ID",deptId);
			List<AuUsrDept> list = usrDeptService.list(auUsrQueryWrapper);
			for (AuUsrDept usrDept : list) {
				usrIds.add(usrDept.getUsrId());
			}
		}else{
			usrIds = docFileDTO.getUsrIds();
		}



//		docFileDownloadService.remove(queryWrapper);
		List<DocFileDownload> docFileDownloads = docFileDownloadService.list(queryWrapper);
		List<String> removeObjectIds = new ArrayList<>();
		List<String> repeatUsrIds = new ArrayList<>();
		for (DocFileDownload docFileDownload : docFileDownloads) {
			String usrId = docFileDownload.getUsrId();
			String objectId = docFileDownload.getObjectId();
			if (!usrIds.contains(usrId)) {
				removeObjectIds.add(objectId);
			}else{
				repeatUsrIds.add(usrId);
			}
		}
		usrIds.removeAll(repeatUsrIds);
		//删除取消权限的数据
		if(removeObjectIds.size()>0){
			docFileDownloadService.removeByIds(removeObjectIds);
		}
		//增加新分配权限的数据
		for (String usrId : usrIds) {
			DocFileDownload entity = new DocFileDownload();
			entity.setDocFileId(docFileDTO.getObjectId());
			entity.setUsrId(usrId);
			entity.setStatuz("待下载");
			docFileDownloadService.save(entity);
		}
	}


	@Override
	@Transactional
	public void delFile(String objectId) {
		if(this.removeById(objectId)){
			// TODO 删除文档
			QueryWrapper<DocFileDownload> queryWrapper = new QueryWrapper<DocFileDownload>();
			queryWrapper.eq("DOC_FILE_ID", objectId);
			docFileDownloadService.remove(queryWrapper);
		}
	}

}
