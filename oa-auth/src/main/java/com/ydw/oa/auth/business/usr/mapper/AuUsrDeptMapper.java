package com.ydw.oa.auth.business.usr.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-14
 */
public interface AuUsrDeptMapper extends BaseMapper<AuUsrDept> {

	List<Map<String, Object>> query(@Param(Constants.WRAPPER) Wrapper<AuUsrDept> auUsrWrapper);
	
	List<Map<String, Object>> deptlist(@Param(Constants.WRAPPER) Wrapper<AuUsrDept> auUsrWrapper);

	IPage<Map<String,Object>> query(Page<AuUsrDept> page, @Param(Constants.WRAPPER) Wrapper<AuUsrDept> auUsrWrapper);

	List<Map<String, Object>> queryAllNotDept();

	// 获取当前人员所属部门的部门信息  分管领导
	List<String> selectDeptInfoList(@Param(Constants.WRAPPER) Wrapper<AuUsrDept> queryWrapper);

	List<Map<String, Object>> deptUsrListForTree(@Param(Constants.WRAPPER) Wrapper<AuUsrDept> qw);

	// 获取当前人员所属部门的部门信息  部长/部门负责人
	List<String> selectDeptManaInfoList(@Param(Constants.WRAPPER) Wrapper<AuUsrDept> queryWrapper);

	// 部门下人员
	List<String> selectDeptUsrList(String dept);

	// 部门下人员
	List<Map<String,Object>> selectDeptUsrListByDeptId(String deptId);

	// 当前人员部门
	List<String> deptList(String userId);
}
