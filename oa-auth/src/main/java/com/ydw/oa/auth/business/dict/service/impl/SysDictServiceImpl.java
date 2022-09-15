package com.ydw.oa.auth.business.dict.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.dict.entity.SysDict;
import com.ydw.oa.auth.business.dict.mapper.SysDictMapper;
import com.ydw.oa.auth.business.dict.service.ISysDictService;
import com.ydw.oa.auth.util.CodeTools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-09
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

	@Autowired
	private CodeTools codeTools;

	// 生成排好序的code
	@Override
	public void AddAndCreatCode(SysDict dictDto) {
		SysDict dict = super.getById(dictDto.getParentId());
		String type_code = dictDto.getTypeCode();
		if(ChkUtil.isNotNull(dict)) {
			type_code = dict.getTypeCode();
			dictDto.setTypeCode(type_code);
		}
		String code = dictDto.getCode();
		if (ChkUtil.isNotNull(code)) {
			String str = codeTools.getGreaterLengthCode(type_code, code);
			if (ChkUtil.isNotNull(str) && !"null".equals(str)) {
				String newcode = CodeTools.code(str);
				dictDto.setCode(newcode);
				super.save(dictDto);
			} else {
				code += "001";
				dictDto.setCode(code);
				super.save(dictDto);
			}
		} else {
			String str = codeTools.getLength3Code(type_code);
			if (ChkUtil.isNotNull(str) && !"null".equals(str)) {
				String newcode = CodeTools.code(str);
				dictDto.setCode(newcode);
			} else {
				dictDto.setCode("001");
			}
			super.save(dictDto);
		}

	}

}
