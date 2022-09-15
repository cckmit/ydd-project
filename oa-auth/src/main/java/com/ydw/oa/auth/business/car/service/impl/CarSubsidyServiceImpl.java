package com.ydw.oa.auth.business.car.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.alibaba.fastjson.JSONArray;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.util.WebUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.car.entity.CarSubsidy;
import com.ydw.oa.auth.business.car.entity.CarSubsidyItem;
import com.ydw.oa.auth.business.car.entity.CarSubsidyStandard;
import com.ydw.oa.auth.business.car.entity.CarUse;
import com.ydw.oa.auth.business.car.mapper.CarSubsidyMapper;
import com.ydw.oa.auth.business.car.service.ICarSubsidyItemService;
import com.ydw.oa.auth.business.car.service.ICarSubsidyService;
import com.ydw.oa.auth.business.car.service.ICarSubsidyStandardService;
import com.ydw.oa.auth.business.car.service.ICarUseService;
import com.ydw.oa.auth.util.DateTools;

import javax.servlet.ServletOutputStream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-08-10
 */
@Service
public class CarSubsidyServiceImpl extends ServiceImpl<CarSubsidyMapper, CarSubsidy> implements ICarSubsidyService {

	@Autowired
	private ICarUseService carUseService;
	@Autowired
	private ICarSubsidyStandardService carSubsidyStandardService;
	@Autowired
	private ICarSubsidyItemService carSubsidyItemService;

	@Transactional
	@Override
	public JSONObject getSubsidyList(String time) {
		// TODO 获取车辆补助数据
		QueryWrapper<CarSubsidy> qw = new QueryWrapper<CarSubsidy>();
		qw.like("MONTH", time);
		List<Map<String, Object>> list = this.listMaps(qw);
		if (ChkUtil.isNull(list)) {
			// 生成补助数据
			list = this.createSubsidyList(time);
		}
		JSONObject result = this.getSubdidyInfos(list);
		return result;
	}

	private JSONObject getSubdidyInfos(List<Map<String, Object>> list) {
		// TODO 获取补助标准及个人补助详情
		QueryWrapper<CarSubsidyStandard> queryWrapper = new QueryWrapper<>();
		queryWrapper.orderByAsc("TYPE");
		List<CarSubsidyStandard> standards = carSubsidyStandardService.list(queryWrapper);
		for (Map<String, Object> map : list) {
			QueryWrapper<CarSubsidyItem> qw = new QueryWrapper<>();
			qw.eq("SUBSIDY_ID", map.get("OBJECT_ID"));
			List<CarSubsidyItem> items = carSubsidyItemService.list(qw);
			items.forEach(item->{
				map.put("totalDay"+item.getStandardType(), item.getTotalDay());
				map.put("standard"+item.getStandardType(), item.getStandard());
				map.put("totalMoney"+item.getStandardType(), item.getTotalMoney());
			});
		}
		JSONObject result = new JSONObject();
		result.put("standards", standards);
		result.put("data", list);
		return result;
	}

	@Transactional
	private List<Map<String, Object>> createSubsidyList(String time) {
		// TODO 生成补助数据
		// 获取车辆行程单数据
		QueryWrapper<CarUse> queryWrapper = new QueryWrapper<>();
		queryWrapper.like("KEY_BACK_TIME", time);
		queryWrapper.select("USR_ID usrId", "USER user", "STANDARD_TYPE standardType", "COUNT(1) num");
		queryWrapper.groupBy("USR_ID", "STANDARD_TYPE");
		queryWrapper.orderByAsc("USR_ID", "STANDARD_TYPE");
		List<Map<String, Object>> list = carUseService.listMaps(queryWrapper);
		for (Map<String, Object> map : list) {
			int day = Integer.parseInt(map.get("num") + "");
			String usrId = map.get("usrId") + "";
			int standardType = Integer.parseInt(map.get("standardType") + "");
			String user = map.get("user") + "";
			QueryWrapper<CarSubsidy> qw = new QueryWrapper<>();
			qw.eq("USR_ID", usrId);
			qw.like("MONTH", time);
			CarSubsidy subsidy = this.getOne(qw);
			if (ChkUtil.isNull(subsidy)) {
				subsidy = new CarSubsidy();
				subsidy.setMonth(DateTools.strToDate(time, "yyyy-MM"));
				subsidy.setUser(user);
				subsidy.setUsrId(usrId);
				subsidy.setTotalDay(0);
				subsidy.setTotalMoney(new BigDecimal(0));
				this.save(subsidy);
			}
			QueryWrapper<CarSubsidyStandard> sqw = new QueryWrapper<>();
			sqw.eq("TYPE", standardType);
			CarSubsidyStandard standard = carSubsidyStandardService.getOne(sqw);
			BigDecimal sd = standard.getStandard();
			BigDecimal money = sd.multiply(new BigDecimal(day));
			CarSubsidyItem item = new CarSubsidyItem();
			item.setStandard(sd);
			item.setStandardType(standardType);
			item.setSubsidyId(subsidy.getObjectId());
			item.setTotalDay(day);
			item.setTotalMoney(money);
			carSubsidyItemService.save(item);

			int totalDay = subsidy.getTotalDay() + day;
			BigDecimal totalMoney = subsidy.getTotalMoney().add(money);
			subsidy.setTotalDay(totalDay);
			subsidy.setTotalMoney(totalMoney);
			this.saveOrUpdate(subsidy);
		}

		QueryWrapper<CarSubsidy> qw = new QueryWrapper<>();
		qw.like("MONTH", time);
		List<Map<String, Object>> all = this.listMaps(qw);
		return all;
	}

	@Transactional
	@Override
	public JSONObject reloadSubsidyList(String time) {
		// TODO 重新生成车辆补助数据
		// 删除已生成的数据
		QueryWrapper<CarSubsidy> qw = new QueryWrapper<CarSubsidy>();
		qw.eq("MONTH", DateTools.strToDate(time, "yyyy-MM"));
		this.remove(qw);
		// 生成补助数据
		List<Map<String, Object>> list = this.createSubsidyList(time);
		JSONObject result = this.getSubdidyInfos(list);
		return result;
	}

	@Override
	public void export(String time, String dataPath) {
		String modelPath = dataPath.concat("/oa_model").concat("/caruse.xlsx");
		String destPath = dataPath.concat("/oa_model").concat("/caruse").concat(time).concat(".xlsx");
		ServletOutputStream outputStream = null;
//        FileInputStream fis = null;
		ExcelWriter writer = null;
		try {
			outputStream = WebUtil.getReponse().getOutputStream();
			FileUtil.copy(modelPath,destPath,true);
			writer = ExcelUtil.getWriter(destPath);
			String[] split = time.split("-");
			String yearMonthStr = split[0].concat("年").concat(split[1]).concat("月");
			writer.merge(15, "阳煤集团司机".concat(yearMonthStr).concat("出车补助明细表"));
			//设置内容字体
			Font font = writer.createFont();
			this.setFont(font,20,true,"宋体");
			//第二个参数表示是否忽略头部样式
			writer.getHeadCellStyle().setFont(font);
			writer.getHeadCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
			writer.	passRows(2);

			Font font2 = writer.createFont();
			this.setFont(font2,10,false,"宋体");
			//第二个参数表示是否忽略头部样式
			writer.getStyleSet().setFont(font2, true);
			writer.getCellStyle().setWrapText(true);
			List<List<Object>> rows = getCellList(time);
			writer.write(rows);
			int rowCount = writer.getRowCount();
			for(int i=3;i<rowCount;i++){
				writer.setRowHeight(i,23);
			}
			WebUtil.getReponse().setContentType("application/vnd.ms-excel;charset=utf-8");
			WebUtil.getReponse().setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("阳煤集团司机".concat(yearMonthStr).concat("出车补助明细表.xls"),"UTF-8"));
			writer.flush(outputStream);



		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			writer.close();
			try {
				outputStream.close();
//                if (fis != null) {
//                    fis.close();
//                }
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	private void setFont(Font font,int size,boolean isBold,String fontName) {
		font.setBold(isBold);
		font.setFontHeightInPoints((short) size);
		font.setFontName(fontName);
	}

	private List<List<Object>> getCellList(String time){
		List<List<Object>> rows = CollUtil.newArrayList();
		JSONObject result =  this.reloadSubsidyList(time);
		JSONArray dataArray = result.getJSONArray("data");
		JSONArray standardsArray = result.getJSONArray("standards");
		System.err.println(standardsArray.size());
		int standard1 = 0;
		int standard2 = 0;
		int standard3 = 0;
		int standard4 = 0;
		for (int i = 0; i < standardsArray.size(); i++) {
			JSONObject jsonObject = standardsArray.getJSONObject(i);
			System.err.println(jsonObject.toJSONString());
			Integer type = jsonObject.getInteger("type");
			int standard = jsonObject.getBigDecimal("standard").intValue();
			switch (type) {
				case 1:
					standard1 = standard;
					break;
				case 2:
					standard2 = standard;
					break;
				case 3:
					standard3 = standard;
					break;
				case 4:
					standard4 = standard;
					break;
				default:

			}
		}
		Integer userTotalDay1 = 0;
		Integer userTotalDay2 = 0;
		Integer userTotalDay3 = 0;
		Integer userTotalDay4 = 0;
		Integer userTotalMoney1 = 0;
		Integer userTotalMoney2 = 0;
		Integer userTotalMoney3 = 0;
		Integer userTotalMoney4 = 0;
		Integer userTotalMoney = 0;
		Integer userTotalDay = 0;
		for (int i = 0; i < dataArray.size(); i++) {
			List<Object> row = new LinkedList<>();
			JSONObject jsonObject = dataArray.getJSONObject(i);
			String user = jsonObject.getString("USER");
			row.add(user);
			Integer totalDay1 = jsonObject.getInteger("totalDay1");
			if (ObjectUtil.isNotNull(totalDay1) && totalDay1 > 0) {
				userTotalDay1 += totalDay1;
				row.add(totalDay1);
			}else{
				row.add("-");
			}
			row.add(standard1);
			Integer totalMoney1 = jsonObject.getInteger("totalMoney1");
			if (ObjectUtil.isNotNull(totalMoney1) && totalMoney1 > 0) {
				userTotalMoney1 += totalMoney1;
				row.add(totalMoney1);
			}else{
				row.add("-");
			}
			Integer totalDay2 = jsonObject.getInteger("totalDay2");
			if (ObjectUtil.isNotNull(totalDay2) && totalDay2 > 0) {
				userTotalDay2 += totalDay2;
				row.add(totalDay2);
			}else{
				row.add("-");
			}
			row.add(standard2);
			Integer totalMoney2= jsonObject.getInteger("totalMoney2");
			if (ObjectUtil.isNotNull(totalMoney2) && totalMoney2 > 0) {
				userTotalMoney2 += totalMoney2;
				row.add(totalMoney2);
			}else{
				row.add("-");
			}
			Integer totalDay3 = jsonObject.getInteger("totalDay3");
			if (ObjectUtil.isNotNull(totalDay3) && totalDay3 > 0) {
				userTotalDay3 += totalDay3;
				row.add(totalDay3);
			}else{
				row.add("-");
			}
			row.add(standard3);
			Integer totalMoney3 = jsonObject.getInteger("totalMoney3");
			if (ObjectUtil.isNotNull(totalMoney3) && totalMoney3 > 0) {
				userTotalMoney3 += totalMoney3;
				row.add(totalMoney3);
			}else{
				row.add("-");
			}
			Integer totalDay4 = jsonObject.getInteger("totalDay4");
			if (ObjectUtil.isNotNull(totalDay4) && totalDay4 > 0) {
				userTotalDay4 += totalDay4;
				row.add(totalDay4);
			}else{
				row.add("-");
			}
			row.add(standard4);
			Integer totalMoney4 = jsonObject.getInteger("totalMoney4");
			if (ObjectUtil.isNotNull(totalMoney4) && totalMoney4 > 0) {
				userTotalMoney4 += totalMoney4;
				row.add(totalMoney4);
			}else{
				row.add("-");
			}
			int totalDay = jsonObject.getBigDecimal("TOTAL_DAY").intValue();
			if (totalDay > 0) {
				userTotalDay += totalDay;
				row.add(totalDay);
			}else{
				row.add("-");
			}
			int totalMoney= jsonObject.getBigDecimal("TOTAL_MONEY").intValue();
			if (totalMoney > 0) {
				userTotalMoney += totalMoney;
				row.add(totalMoney);
			}else{
				row.add("-");
			}
			row.add("");
			rows.add(row);
		}
		List<Object> row = new LinkedList<>();
		row.add("合计");
		row.add(userTotalDay1);
		row.add(standard1);
		row.add(userTotalMoney1);
		row.add(userTotalDay2);
		row.add(standard2);
		row.add(userTotalMoney2);
		row.add(userTotalDay3);
		row.add(standard3);
		row.add(userTotalMoney3);
		row.add(userTotalDay4);
		row.add(standard4);
		row.add(userTotalMoney4);
		row.add(userTotalDay);
		row.add(userTotalMoney);
		row.add("");
		rows.add(row);
		return rows;
	}

}
