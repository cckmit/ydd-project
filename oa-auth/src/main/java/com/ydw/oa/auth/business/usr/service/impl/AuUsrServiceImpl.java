package com.ydw.oa.auth.business.usr.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.util.Md5Util;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.role.entity.AuRole;
import com.ydw.oa.auth.business.role.service.IAuRoleService;
import com.ydw.oa.auth.business.usr.dto.CurrentUsrDto;
import com.ydw.oa.auth.business.usr.dto.UsrDto;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.entity.AuUsrDept;
import com.ydw.oa.auth.business.usr.entity.AuUsrRole;
import com.ydw.oa.auth.business.usr.mapper.AuUsrMapper;
import com.ydw.oa.auth.business.usr.service.IAuUsrDeptService;
import com.ydw.oa.auth.business.usr.service.IAuUsrRoleService;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business.usr.service.IAuUsrSignService;
import com.ydw.oa.auth.util.PinYinUtil;
import com.ydw.oa.auth.util.SessionTool;

import com.ydw.oa.auth.util.excel.ExcelReadTools;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-02-04
 */
@Service
public class AuUsrServiceImpl extends ServiceImpl<AuUsrMapper, AuUsr> implements IAuUsrService {

	@Autowired
	private IAuUsrRoleService usrRoleService;
	@Autowired
	private IAuUsrDeptService usrDeptService;
	@Autowired
	private IAuUsrSignService usrSignService;
	@Autowired
	private IAuDeptService deptService;
	@Autowired
	private IAuRoleService roleService;

	@Override
	public AuUsr login(String usrName, String password) {
		QueryWrapper<AuUsr> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("USR_NAME", usrName);
		queryWrapper.eq("PASSWD", Md5Util.md5(password));
		AuUsr usr = this.getOne(queryWrapper);
		return usr;
	}

	@Override
	@Transactional
	public void saveUser(@Valid UsrDto usrDto) {
		// 1. 保存用户
		AuUsr usrPo = new AuUsr();
		BeanUtils.copyProperties(usrDto, usrPo);
		usrPo.setPasswd(Md5Util.md5(usrDto.getPasswd()));// md5 密码加密
		usrPo.setCreateTime(new Date(System.currentTimeMillis()));
		if(ChkUtil.isNull(usrDto.getRealName())) {
			usrPo.setRealName(usrDto.getUsrName());
		}
		this.save(usrPo);
		// 2.保存角色列表
		usrRoleService.saveUsrRoles(usrPo.getObjectId(), usrDto.getRoles());
		// 3.保存部门列表
		usrDeptService.saveUsrDepts(usrPo.getObjectId(), usrDto.getDepts());
		// 保存人员标签
		usrSignService.saveUsrSigns(usrPo.getObjectId(), usrDto.getSigns());
	}

	@Override
	@Transactional
	public void delete(String usrId) {
		// 1.删除自身
		this.removeById(usrId);
		// 2.删除关联 角色
		usrRoleService.deleteAllByUsrId(usrId);
		// 2.删除关联 部门
		usrDeptService.deleteAllByUsrId(usrId);
		// 删除标签
		usrSignService.deleteAllByUsrId(usrId);
	}

	@Override
	public void updateUser(@Valid UsrDto usrDto) {
		// 1. 保存自身
		AuUsr usrPo = this.getById(usrDto.getObjectId());
		usrPo.setUsrId(usrDto.getUsrId());
		usrPo.setDeptId(usrDto.getDeptId());
		usrPo.setType(usrDto.getType());
		usrPo.setMobile(usrDto.getMobile());
		usrPo.setEmail(usrDto.getEmail());
		usrPo.setRealName(usrDto.getRealName());
		usrPo.setNote(usrDto.getNote());
		usrPo.setCertNo(usrDto.getCertNo());
		usrPo.setVariable(usrDto.getVariable());
		usrPo.setLastUpdateTime(new Date(System.currentTimeMillis()));
		usrPo.setWxUserId(usrDto.getWxUserId());
		usrPo.setSignUrl(usrDto.getSignUrl());
		this.saveOrUpdate(usrPo);

		String usrId = usrPo.getObjectId();
		// 2.删除并保存 角色
		usrRoleService.deleteAllByUsrId(usrPo.getObjectId());
		usrRoleService.saveUsrRoles(usrId, usrDto.getRoles());

		// 3.删除并保存 部门
		usrDeptService.deleteAllByUsrId(usrId);
		usrDeptService.saveUsrDepts(usrId, usrDto.getDepts());
		
		// 删除并保存人员标签
		usrSignService.deleteAllByUsrId(usrId);
		usrSignService.saveUsrSigns(usrId, usrDto.getSigns());
	}

	@Override
	public void updateUserInfo(@Valid CurrentUsrDto usrDto) {
		// TODO 修改当前用户信息
		AuUsr usrPo = this.getById(SessionTool.getSessionAdminId());
		usrPo.setMobile(usrDto.getMobile());
		usrPo.setEmail(usrDto.getEmail());
		usrPo.setRealName(usrDto.getRealName());
		usrPo.setNote(usrDto.getNote());
		usrPo.setCertNo(usrDto.getCertNo());
		usrPo.setLastUpdateTime(new Date(System.currentTimeMillis()));
		usrPo.setNote(usrDto.getNote());
		usrPo.setWxUserId(usrDto.getWxUserId());
		usrPo.setSignUrl(usrDto.getSignUrl());
		this.saveOrUpdate(usrPo);
	}

	@Override
	public void export() {
		String path = "D:/user.xlsx";
		InputStream is = null;
		try {
			is = new FileInputStream(path);
			ExcelReadTools excelReader = new ExcelReadTools();
			List<Map<String, Object>> list = excelReader.readExcel2ListMap(is,
					new String[] { "index", "realName", "mobile", "deptName", "roleName",
							"wxUserId", "note"},
					1);
			for (Map<String, Object> map : list) {
				QueryWrapper<AuDept> dw = new QueryWrapper<AuDept>();
				dw.eq("NAME", map.get("deptName"));
				AuDept dept = deptService.getOne(dw);
				if(ChkUtil.isNull(dept)) {
					break;
				}
				QueryWrapper<AuRole> rw = new QueryWrapper<AuRole>();
				rw.eq("NAME", map.get("roleName"));
				AuRole role = roleService.getOne(rw);
				if(ChkUtil.isNull(role)) {
					break;
				}
				String name = map.get("realName").toString().replace(" ", "");
				AuUsr usr = new AuUsr();
				usr.setWxUserId(map.get("wxUserId").toString());
				usr.setRealName(name);
				usr.setUsrName(name);
				usr.setNote(map.get("note").toString());
				usr.setMobile(map.get("mobile").toString());
				usr.setPasswd(Md5Util.md5("123456"));
				usr.setSignUrl(PinYinUtil.getCamelPinYin(name) + ".png");
				this.save(usr);
				AuUsrRole usrRole = new AuUsrRole();
				usrRole.setRoleId(role.getObjectId());
				usrRole.setUsrId(usr.getObjectId());
				usrRoleService.save(usrRole);
				AuUsrDept usrDept = new AuUsrDept();
				usrDept.setDeptId(dept.getObjectId());
				usrDept.setUsrId(usr.getObjectId());
				usrDeptService.save(usrDept);
			}
		} catch (FileNotFoundException e) {
			System.out.println("未找到指定路径的文件!");
			e.printStackTrace();
		} finally {
			try {
				if (is != null) {
					is.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
