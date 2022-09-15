package com.ydw.oa.wkflow.business_main.wkdoc.service.impl;

import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileHtml;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileHtmlMapper;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileHtmlService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-11-24
 */
@Service
public class WkDocFileHtmlServiceImpl extends ServiceImpl<WkDocFileHtmlMapper, WkDocFileHtml> implements IWkDocFileHtmlService {

	@Override
	@Transactional
	public void createWkDocFileHTML(String formKey, String processInstanceId, String html) {
		// TODO 保存流程关联html数据
		QueryWrapper<WkDocFileHtml> qw = new QueryWrapper<WkDocFileHtml>();
		qw.eq("PROCESS_INSTANCE_ID", processInstanceId);
		qw.eq("FORM_ID", formKey);
		WkDocFileHtml fileHtml = this.getOne(qw);
		if(ChkUtil.isNull(fileHtml)) {
			fileHtml = new WkDocFileHtml();
			fileHtml.setFormId(formKey);
			fileHtml.setProcessInstanceId(processInstanceId);
		}
		fileHtml.setHtmlContent(html);
		this.saveOrUpdate(fileHtml);
		
	}

}
