package com.ydw.oa.wkflow.business_main.code.service;

import com.ydw.oa.wkflow.business_main.code.entity.Code;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-09-21
 */
public interface ICodeService extends IService<Code> {

	String getReviewCode(String type);

	String getProposalCode();
}
