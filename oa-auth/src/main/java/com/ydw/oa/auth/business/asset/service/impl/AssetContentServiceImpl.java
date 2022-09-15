package com.ydw.oa.auth.business.asset.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import com.ydw.oa.auth.business.asset.entity.AssetLog;
import com.ydw.oa.auth.business.asset.mapper.AssetContentMapper;
import com.ydw.oa.auth.business.asset.service.IAssetContentService;
import com.ydw.oa.auth.business.asset.service.IAssetLogService;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
@Service
public class AssetContentServiceImpl extends ServiceImpl<AssetContentMapper, AssetContent>
		implements IAssetContentService {

	@Autowired
	private IAssetLogService assetLogService;


	@Override
	@Transactional
	public void add(AssetContent assetContent) {
		// TODO 新增固定资产
		this.save(assetContent);
		
		assetLogService.addLog(assetContent, "新增");
	}

	@Override
	@Transactional
	public String delete(String objectId) {
		// TODO 删除
		AssetContent content = this.getById(objectId);
		if ("使用中".equals(content.getStatuz())) {
			return "该资产不可删除";
		}
		this.removeById(objectId);

		QueryWrapper<AssetLog> qw = new QueryWrapper<AssetLog>();
		qw.eq("CONTENT_ID", objectId);
		assetLogService.remove(qw);
		return "删除成功";
	}

	@Override
	@Transactional
	public void distribution(String objectId, String deptId, String usrId) {
		// TODO 资产分配
		AssetContent content = this.getById(objectId);//通过ID获取对象
		content.setCurrentUsrDeptId(deptId);
		content.setCurrentUsrId(usrId);
		assetLogService.addLog(content, "分配");
		content.setStatuz("使用中");
		this.saveOrUpdate(content);
	}

	@Override
	@Transactional
	public void recovery(String objectId) {
		// TODO 资产回收
		AssetContent content = this.getById(objectId);
		assetLogService.addLog(content, "回收");
		content.setCurrentUsrDeptId(" ");
		content.setCurrentUsrId(" ");
		content.setStatuz("在库");
		this.saveOrUpdate(content);


	}

	@Override
	@Transactional
	public void scrap(String objectId) {
		// TODO 资产报废
		AssetContent content = this.getById(objectId);
		content.setStatuz("待报废");
        content.setCurrentUsrDeptId(" ");
		content.setCurrentUsrId(" ");
		this.saveOrUpdate(content);

		assetLogService.addLog(content, "报废");
	}

	@Override
	@Transactional
	public void scrapAgain(String objectId) {
		// TODO 资产再报废
		AssetContent content = this.getById(objectId);
		content.setStatuz("报废结束");
        content.setCurrentUsrDeptId(" ");
		content.setCurrentUsrId(" ");
		this.saveOrUpdate(content);

		assetLogService.addLog(content, "报废结束");
	}

}
