package com.ydw.oa.auth.business.asset.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author hxj
 * @since 2020-06-29
 */
public interface IAssetContentService extends IService<AssetContent> {

	void add(AssetContent assetContent);

	String delete(String objectId);

	void distribution(String objectId, String deptId, String usrId);

	void recovery(String objectId);

	void scrap(String objectId);

	void scrapAgain(String objectId);

}
