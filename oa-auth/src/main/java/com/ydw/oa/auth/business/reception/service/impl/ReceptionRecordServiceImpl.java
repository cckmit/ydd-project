package com.ydw.oa.auth.business.reception.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.util.WebUtil;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.ydw.oa.auth.business.dept.entity.AuDept;
import com.ydw.oa.auth.business.dept.service.IAuDeptService;
import com.ydw.oa.auth.business.reception.entity.ReceptionRecord;
import com.ydw.oa.auth.business.reception.mapper.ReceptionRecordMapper;
import com.ydw.oa.auth.business.reception.service.IReceptionRecordService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.util.DateTools;

import javax.servlet.ServletOutputStream;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-09-21
 */
@Service
public class ReceptionRecordServiceImpl extends ServiceImpl<ReceptionRecordMapper, ReceptionRecord>
		implements IReceptionRecordService {

	@Autowired
	private IAuDeptService deptService;
	@Autowired
	private IAuUsrService usrService;
	
	@Override
	@Transactional
	public void saveRecords(JSONObject records) {
		// TODO 保存申请记录
		String eatReceptionDate = records.getString("oa-text-field-3");// 就餐接待时间
		String liveReceptionDate = records.getString("oa-text-date-apply");// 住宿接待时间
		String deptName = records.getString("oa-text-dept-0");//接待部门
		QueryWrapper<AuDept> queryWrapper = new QueryWrapper<AuDept>();
		queryWrapper.eq("NAME", deptName);
		AuDept dept = deptService.getOne(queryWrapper);
		AuUsr usr = usrService.getById(dept.getUsrId());
		String deptLeader = usr.getRealName();//分管领导
		String reason = records.getString("oa-text-field-2");//接待人员事项
		// 就餐
		String address = records.getString("oa-text-field-12");//接待地点
		String type = records.getString("oa-text-field-13");//接待类别
		// 住宿
		String address1 = records.getString("oa-text-field-34");//接待地点
		String type1 = records.getString("oa-text-field-35");//接待类别
		
		String oagroup = records.getString("oagroup");//接待事项
		String code = records.getString("oa-text-code-0");//审批编号
		String pid = records.getString("pid");
		String formId = records.getString("formId");
		int num = records.getIntValue("oa-text-field-5");// 就餐来客人数
		int num1 = records.getIntValue("oa-text-field-6");// 就餐陪餐人数
		int num2 = records.getIntValue("oa-text-field-17");// 就餐其他工作人员人数
		int num3 = records.getIntValue("oa-text-field-8");// 住宿来客人数
		BigDecimal money = records.getBigDecimal("oa-text-field-16");//就餐支出
		BigDecimal money1 = records.getBigDecimal("oa-text-field-19");//其他人员就餐支出
		BigDecimal money2 = records.getBigDecimal("oa-text-field-20");//住宿支出
		
		if (ChkUtil.isNotNull(num3)) {
			QueryWrapper<ReceptionRecord> qw = new QueryWrapper<ReceptionRecord>();
			qw.eq("PID", pid);
			qw.eq("STYLE", "住宿");
			ReceptionRecord receptionRecord = this.getOne(qw);
			if(ChkUtil.isNull(receptionRecord)) {
				receptionRecord = new ReceptionRecord();
			}
			receptionRecord.setAddress(address1);
			receptionRecord.setDeptLeader(deptLeader);
			receptionRecord.setDeptName(deptName);
			receptionRecord.setReason(reason);
			receptionRecord.setStyle("住宿");
			receptionRecord.setTotalMoney(money2);
			receptionRecord.setReceptionDate(DateTools.strToDate(liveReceptionDate, "yyyy年MM月dd日"));
			receptionRecord.setTotalNum(num3);
			receptionRecord.setPid(pid);
			receptionRecord.setFormId(formId);
			receptionRecord.setReviewCode(code);
			receptionRecord.setMutiReview("待会签");
			receptionRecord.setMonth(DateTools.getToday("yyyyMM"));
			this.saveOrUpdate(receptionRecord);
		}
		if (ChkUtil.isNotNull(num)) {
			QueryWrapper<ReceptionRecord> qw = new QueryWrapper<ReceptionRecord>();
			qw.eq("PID", pid);
			qw.eq("STYLE", "就餐");
			ReceptionRecord receptionRecord = this.getOne(qw);
			if(ChkUtil.isNull(receptionRecord)) {
				receptionRecord = new ReceptionRecord();
			}
			receptionRecord.setAddress(address);
			receptionRecord.setDeptLeader(deptLeader);
			receptionRecord.setDeptName(deptName);
			receptionRecord.setReason(reason);
			receptionRecord.setStyle("就餐");
			receptionRecord.setTotalMoney(money.add(money1));
			receptionRecord.setReceptionDate(DateTools.strToDate(eatReceptionDate, "yyyy年MM月dd日"));
			receptionRecord.setTotalNum(num + num1 + num2);
			receptionRecord.setPid(pid);
			receptionRecord.setFormId(formId);
			receptionRecord.setReviewCode(code);
			receptionRecord.setMutiReview("待会签");
			receptionRecord.setMonth(DateTools.getToday("yyyyMM"));
			this.saveOrUpdate(receptionRecord);
		}
	}
	
	@Override
	@Transactional
	public String choseRecord(String objectIds) {
		// TODO 会签选择记录状态更改
		QueryWrapper<ReceptionRecord> qw = new QueryWrapper<ReceptionRecord>();
		qw.in("PID", Arrays.asList(objectIds.split(",")));
		List<ReceptionRecord> list = this.list(qw);
		BigDecimal totalMoney = new BigDecimal(0);
		for (ReceptionRecord receptionRecord : list) {
			receptionRecord.setMutiReview("会签中");
			this.saveOrUpdate(receptionRecord);
			
			totalMoney = totalMoney.add(receptionRecord.getTotalMoney());
		}
		return totalMoney.toString();
	}

	@Override
	@Transactional
	public void endRecordSign(String objectIds, boolean isReject) {
		// TODO 接待会签结束，变更状态
		QueryWrapper<ReceptionRecord> qw = new QueryWrapper<ReceptionRecord>();
		qw.in("PID", Arrays.asList(objectIds.split(",")));
		List<ReceptionRecord> list = this.list(qw);
		Date now = new Date();
		String review = "已会签";
		if(isReject) {
			review = "待会签";
		}
		for (ReceptionRecord receptionRecord : list) {
			receptionRecord.setMutiReview(review);
			receptionRecord.setSignDate(now);
			this.saveOrUpdate(receptionRecord);
		}
	}

	@Override
	public void export(String yearMonth, String dataPath) {
		String modelPath = dataPath.concat("/oa_model").concat("/reception.xlsx");
		String destPath = dataPath.concat("/oa_model").concat("/reception").concat("-").concat(yearMonth).concat(".xlsx");
		ServletOutputStream outputStream = null;
//        FileInputStream fis = null;
		ExcelWriter writer = null;
		try {
			outputStream = WebUtil.getReponse().getOutputStream();
			FileUtil.copy(modelPath,destPath,true);
			writer = ExcelUtil.getWriter(destPath);
			String year = yearMonth.substring(0,4);
			String monthStr = yearMonth.substring(4);
			String yearMonthStr = year.concat("年").concat(monthStr).concat("月");
			writer.merge(8, "财务公司".concat(yearMonthStr).concat("业务接待情况公示表"));
			//设置内容字体
			Font font = writer.createFont();
			this.setFont(font,20,true,"宋体");
			//第二个参数表示是否忽略头部样式
			writer.getHeadCellStyle().setFont(font);
			writer.getHeadCellStyle().setFillForegroundColor(IndexedColors.WHITE.getIndex());
			writer.	passRows(1);

			Font font2 = writer.createFont();
			this.setFont(font2,10,false,"宋体");
			//第二个参数表示是否忽略头部样式
			writer.getStyleSet().setFont(font2, true);
			writer.getCellStyle().setWrapText(true);
			StringBuilder total = new StringBuilder("");
			List<List<Object>> rows = getCellList(yearMonth,total);
			writer.write(rows);
			int rowCount = writer.getRowCount();
			for(int i=2;i<rowCount;i++){
				writer.setRowHeight(i,23);
			}
			writer.merge(7, "合计");
//			BorderStyle borderRight = writer.getCellStyle().getBorderRight();
//			cellStyle.setBorderRight(borderRight);
//			cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
//			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
//			Font font3 = writer.createFont();
//			this.setFont(font3,10,false,"宋体");
//			cellStyle.setFont(font3);
			writer.writeCellValue(8, rowCount, total.toString());
			WebUtil.getReponse().setContentType("application/vnd.ms-excel;charset=utf-8");
			WebUtil.getReponse().setHeader("Content-Disposition","attachment;filename="+ URLEncoder.encode("财务公司".concat(yearMonthStr).concat("业务接待情况公示表.xls"),"UTF-8"));
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

	private List<List<Object>> getCellList(String month, StringBuilder sb){
		List<List<Object>> rows = CollUtil.newArrayList();
		QueryWrapper<ReceptionRecord> receptionRecordQueryWrapper = new QueryWrapper<>();
		receptionRecordQueryWrapper.eq("MONTH", month).eq("MUTI_REVIEW","已会签");
		List<ReceptionRecord> receptionRecords = this.list(receptionRecordQueryWrapper);
		int index = 0;
		BigDecimal totalDecimal = BigDecimal.ZERO;
		for (ReceptionRecord receptionRecord : receptionRecords) {
			List<Object> row = new LinkedList<>();
			row.add(++index);
			row.add(DateUtil.format(receptionRecord.getReceptionDate(), "MM月dd日"));
			row.add(receptionRecord.getDeptName());
			row.add(receptionRecord.getDeptLeader());
			row.add(receptionRecord.getReason());
			row.add(receptionRecord.getAddress());
			row.add(receptionRecord.getStyle());
			row.add(receptionRecord.getTotalNum());
			BigDecimal totalMoney = receptionRecord.getTotalMoney();
			totalDecimal = totalDecimal.add(totalMoney);
			row.add(totalMoney.toString());
			rows.add(row);
		}
		sb.append(totalDecimal.toString());
		return rows;
	}




}
