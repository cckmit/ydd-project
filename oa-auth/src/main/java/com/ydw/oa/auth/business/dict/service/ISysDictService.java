package com.ydw.oa.auth.business.dict.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.dict.entity.SysDict;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-09
 */
public interface ISysDictService extends IService<SysDict> {

	public void AddAndCreatCode(SysDict dict);
}
