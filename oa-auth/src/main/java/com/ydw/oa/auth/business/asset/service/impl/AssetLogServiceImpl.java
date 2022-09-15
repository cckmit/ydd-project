package com.ydw.oa.auth.business.asset.service.impl;

import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import com.ydw.oa.auth.business.asset.entity.AssetLog;
import com.ydw.oa.auth.business.asset.mapper.AssetLogMapper;
import com.ydw.oa.auth.business.asset.service.IAssetLogService;
import com.ydw.oa.auth.util.SessionTool;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
@Service
public class AssetLogServiceImpl extends ServiceImpl<AssetLogMapper, AssetLog> implements IAssetLogService {

	@Autowired
	private IAuDeptService auDeptService;
	@Autowired
	private IAuUsrService auUsrService;

	@Override
	@Transactional
	public void addLog(AssetContent content, String operate) {
		// TODO 新增操作记录
		JSONObject user = SessionTool.getSessionAdmin();
		AuDept dept = auDeptService.getById(content.getCurrentUsrDeptId());
		AuUsr usr = auUsrService.getById(content.getCurrentUsrId());
		AssetLog log = new AssetLog();
		log.setContentId(content.getObjectId());
		log.setUsrId(user.getString("objectId"));
		log.setOperate(operate);
		String note = null;
		switch (operate) {
		case "新增":
			note = user.getString("realName") + "录入" + content.getName() + "【" + content.getCode() + "】";
			break;
		case "分配":
			if (content.getCurrentUsrId() != null){
			note = user.getString("realName") + "分配" + content.getName() + "【" + content.getCode() + "】到"
					+ dept.getName() + "【" + usr.getRealName() + "】";
			break;}
			if(content.getCurrentUsrId()== null || content.getCurrentUsrId()==" ") {
				note = user.getString("realName") + "分配" + content.getName() + "【" + content.getCode() + "】到"
						+ dept.getName();
				break;
			}
		case "回收":
			if(content.getCurrentUsrId().equals(null) || content.getCurrentUsrId().equals(" ")){
				note = user.getString("realName") +"从【"+dept.getName()+"】回收【"+content.getName()+"】";
				break;
			}
			else{

				note = user.getString("realName") + "从" + dept.getName() + "【" + usr.getRealName()
						+ "】回收" + content.getName() + "【" + content.getCode() + "】";
				break;
			}
		case "报废":
			note = user.getString("realName") + "报废" + content.getName() + "【" + content.getCode() + "】";
			break;
		default:
			note = user.getString("realName") + "确认" + content.getName() + "【" + content.getCode() + "】" + "报废结束" ;
			break;
		}
		log.setNote(note);
		this.save(log);
	}

}
