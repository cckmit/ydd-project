package com.ydw.oa.auth.business.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import com.ydw.oa.auth.business.asset.entity.AssetLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
public interface IAssetLogService extends IService<AssetLog> {

	void addLog(AssetContent assetContent, String operate);

}
