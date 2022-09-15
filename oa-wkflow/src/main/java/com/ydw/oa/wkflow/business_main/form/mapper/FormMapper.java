package com.ydw.oa.wkflow.business_main.form.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.ydw.oa.wkflow.business_main.form.entity.Form;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
public interface FormMapper extends BaseMapper<Form> {

	List<Form> selectListPage(IPage<Form> formQuery,  @Param(Constants.WRAPPER) Wrapper<Form> makeQueryWrapper);

}
