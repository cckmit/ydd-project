package com.ydw.oa.auth.business.wage.service;

import java.io.IOException;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.wage.entity.PersWageLog;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
public interface IPersWageLogService extends IService<PersWageLog> {

	void export_model() throws IOException;

	void import_wage(String fileId) throws Exception;

}
