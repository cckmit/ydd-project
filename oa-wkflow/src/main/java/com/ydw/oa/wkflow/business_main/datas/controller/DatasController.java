package com.ydw.oa.wkflow.business_main.datas.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wkflow.business_main.datas.dto.DatasQuery;
import com.ydw.oa.wkflow.business_main.datas.entity.Datas;
import com.ydw.oa.wkflow.business_main.datas.mapper.DatasMapper;
import com.ydw.oa.wkflow.business_main.datas.service.IDatasService;
import com.ydw.oa.wkflow.util.SessionTool;
import com.ydw.oa.wkflow.util.TjTools;
import com.ydw.oa.wkflow.util.date.DateTools;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 冯晓东
 * @since 2020-03-10
 */
@Api(description = "我的业务管理")
@RestController
@RequestMapping("/datas/datas")
public class DatasController {

	@Autowired
	private IDatasService datasService;
	@Autowired
	private DatasMapper datasMapper;

	@ApiOperation(value = "我的业务列表")
	@PostMapping("/list")
	public Wrapper<IPage<Datas>> list(@RequestBody DatasQuery<Datas> datasQuery) {
		datasQuery.setAssigner(SessionTool.getSessionAdminId());
		datasQuery.setType("填写单");
		datasQuery.setSortz(0);
		IPage<Datas> ipage = datasService.page(datasQuery, datasQuery.makeQueryWrapper());
		return WrapMapper.ok(ipage);
	}
	
	@ApiOperation(value = "业务统计")
	@PostMapping("/statistics")
	public Wrapper<Map<String, Integer>> statistics(@RequestBody DatasQuery<Datas> datasQuery) {
		datasQuery.setType("填写单");
		datasQuery.setName("请假单");
		if(ChkUtil.isNull(datasQuery.getDate())) {
			if("年".equals(datasQuery.getFormatType())) {
				datasQuery.setDate(DateTools.getYear()+"");
			}else {
				datasQuery.setDate(DateTools.getYearMonth());
			}
		}
		String dataFormat = "";
		if("年".equals(datasQuery.getFormatType())) {
			dataFormat = "%c";
		}else {
			dataFormat = "%e";
		}
		// 获取创建时间
		List<Map<String, Object>> list = datasMapper.selectCreateTimeList(dataFormat, datasQuery.makeQueryWrapper());
		Map<String, Integer> map = TjTools.groupBy(list, "date");
		return WrapMapper.ok(map);
	}
}
