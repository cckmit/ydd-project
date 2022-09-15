package com.ydw.oa.wkflow.business_main.code.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.wkflow.business_main.code.entity.Code;
import com.ydw.oa.wkflow.business_main.code.mapper.CodeMapper;
import com.ydw.oa.wkflow.business_main.code.service.ICodeService;
import com.ydw.oa.wkflow.util.date.DateTools;

import net.sourceforge.pinyin4j.PinyinHelper;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-09-21
 */
@Service
public class CodeServiceImpl extends ServiceImpl<CodeMapper, Code> implements ICodeService {

	@Override
	@Transactional
	public synchronized String getReviewCode(String type) {
		// TODO 获取编码
		String header = this.getPinYinHeadChar(type).toUpperCase();
		String year = String.valueOf(DateTools.getYear());
		QueryWrapper<Code> qw = new QueryWrapper<Code>();
		qw.eq("REVIEW_TYPE", header);
		qw.eq("DATE_INFO", year);
		Code code = this.getOne(qw);
		if (ChkUtil.isNull(code)) {
			code = new Code();
			code.setReviewType(header);
			code.setDateInfo(year);
			this.save(code);
		}
		int num = code.getNum() + 1;
		code.setNum(num);
		this.saveOrUpdate(code);
		String numMsg = this.getNum(5,String.valueOf(num));
		return header + "-" + year + "-" + numMsg;
	}

	@Override
	public synchronized String getProposalCode() {
		String header = "PROPOSAL";
		String year = String.valueOf(DateTools.getYear());
		QueryWrapper<Code> qw = new QueryWrapper<Code>();
		qw.eq("REVIEW_TYPE", header);
		qw.eq("DATE_INFO", year);
		Code code = this.getOne(qw);
		if (ChkUtil.isNull(code)) {
			code = new Code();
			code.setReviewType(header);
			code.setDateInfo(year);
			this.save(code);
		}
		int num = code.getNum() + 1;
		code.setNum(num);
		this.saveOrUpdate(code);
		String numMsg = this.getNum(3,String.valueOf(num));
		return year + "-" + numMsg;
	}

	private String getNum(int count,String num) {

		// TODO 获取后五位编码
		String result = "";
		for (int i = 0; i < count - num.length(); i++) {
			result += "0";
		}
		result += num;
		return result;
	}

	private String getPinYinHeadChar(String str) {
		// TODO 获取首字母缩写
		String convert = "";
		for (int j = 0; j < str.length(); j++) {
			char word = str.charAt(j);
			String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(word);
			if (pinyinArray != null) {
				convert += pinyinArray[0].charAt(0);
			} else {
				convert += word;
			}
		}
		return convert;
	}

}
