package com.ydw.oa.wkflow.business_main.wkdoc.service.impl;

import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.business_main.wkdoc.entity.WkDocFileUsr;
import com.ydw.oa.wkflow.business_main.wkdoc.mapper.WkDocFileUsrMapper;
import com.ydw.oa.wkflow.business_main.wkdoc.service.IWkDocFileUsrService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
public class WkDocFileUsrServiceImpl extends ServiceImpl<WkDocFileUsrMapper, WkDocFileUsr> implements IWkDocFileUsrService {

	@Autowired
	private IDatasService datasService;
	
	@Override
	@Transactional
	public void createWkDocFileUsrs(String processInstanceId, String wkDocFileId) {
		// TODO 生成流程文档员工关联
		QueryWrapper<Datas> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("ACTI_PROC_INST_ID", processInstanceId);
		List<Datas> list = datasService.list(queryWrapper);
		
		for (Datas datas : list) {
			WkDocFileUsr usr = new WkDocFileUsr();
			usr.setWkDocFileId(wkDocFileId);
			usr.setUsrId(datas.getAssigner());
			this.save(usr);
		}
	}

}
