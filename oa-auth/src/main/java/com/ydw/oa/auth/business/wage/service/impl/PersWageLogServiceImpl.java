package com.ydw.oa.auth.business.wage.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.base.exception.BusinessException;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.wage.entity.PersWageAlgorithmic;
import com.ydw.oa.auth.business.wage.entity.PersWageItem;
import com.ydw.oa.auth.business.wage.entity.PersWageLog;
import com.ydw.oa.auth.business.wage.mapper.PersWageLogMapper;
import com.ydw.oa.auth.business.wage.service.IPersWageAlgorithmicService;
import com.ydw.oa.auth.business.wage.service.IPersWageItemService;
import com.ydw.oa.auth.business.wage.service.IPersWageLogService;
import com.ydw.oa.auth.business_wkflow.WkflowFeignService;
import com.ydw.oa.auth.util.DateTools;
import com.ydw.oa.auth.util.WebUtil;
import com.ydw.oa.auth.util.excel.ExcelExport;
import com.ydw.oa.auth.util.excel.ExcelReadTools;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Service
public class PersWageLogServiceImpl extends ServiceImpl<PersWageLogMapper, PersWageLog> implements IPersWageLogService {

	@Autowired
	private IPersWageItemService persWageItemService;
	@Autowired
	private WkflowFeignService feignService;
	@Autowired
	private IPersWageAlgorithmicService persWageAlgorithmicService;

	@Override
	public void export_model() throws IOException {
		// TODO 导出模板
		String[] head_cells = this.getHeadCells();
		ExcelExport exEport = new ExcelExport();
		WebUtil.getReponse().setHeader("content-disposition", "attachment;filename="+ URLEncoder.encode("工资模板.xls", "utf-8"));
		exEport.exportExcelL("工资模板", "基础工资模板", head_cells, WebUtil.getReponse().getOutputStream());
	}

	private String[] getHeadCells() {
		// TODO 工资excel表头
		QueryWrapper<PersWageItem> qw = new QueryWrapper<>();
		qw.orderByAsc("SORTZ");
		List<PersWageItem> list = persWageItemService.list(qw);
		if (ChkUtil.isNull(list)) {
			throw new BusinessException(505, "工资项为空，请先添加工资项");
		}
		String[] head_cells = new String[list.size() + 3];
		head_cells[0] = "姓名";
		for (int i = 0; i < list.size(); i++) {
			head_cells[i + 1] = list.get(i).getName();
		}
		head_cells[list.size() + 1] = "发放日期";
		head_cells[list.size() + 2] = "工资月份";
		return head_cells;
	}

	@Override
	public void import_wage(String fileId) throws Exception {
		// TODO 导入工资
		String[] head_cells = this.getHeadCells();
		ExcelReadTools erTools = new ExcelReadTools();
		Map<String, Object> file = null;
		if (ChkUtil.isNotNull(fileId)) {
			file = feignService.get(fileId).getResult();
		}
		Map<String, Object> system = feignService.get().getResult();
		String DATA_PATH = system.get("value") + "";
		String filePath = DATA_PATH + java.io.File.separator + file.get("folder") + java.io.File.separator;
		File excelFile = new File(filePath, file.get("newFileName") + "");
		FileInputStream is = new FileInputStream(excelFile);
		List<Map<String, Object>> list = erTools.readExcel2ListMap(is, head_cells, 2);
		int length = head_cells.length;
		PersWageAlgorithmic persWageAlgorithmic = persWageAlgorithmicService.getOne(new QueryWrapper<>());
		ScriptEngine se = new ScriptEngineManager().getEngineByName("JavaScript");
		for (Map<String, Object> map : list) {
			String realName = map.get(head_cells[0]) + "";
			String baseWage = ChkUtil.isNull(map.get("基本工资")) ? "-" : map.get("基本工资") + "";
			String sendTime = map.get(head_cells[length - 2]) + "";
			String wageMonth = map.get(head_cells[length - 1]) + "";
			String realWage = persWageAlgorithmic.getTitle();
			for (String key : head_cells) {
				realWage = realWage.replaceAll("\\#\\{"+key+"\\}", map.get(key) + "");
			}
			
			PersWageLog wage = new PersWageLog();
			wage.setBaseWage(new BigDecimal(baseWage));
			wage.setRealName(realName);
			wage.setSendTime(DateTools.strToDate(sendTime));
			wage.setWageMonth(DateTools.strToDate(wageMonth,"yyyy-MM"));
			wage.setRealWage(new BigDecimal(se.eval(realWage) + ""));
			wage.setSendTime(new Date());
			this.save(wage);
		}
	}

}
