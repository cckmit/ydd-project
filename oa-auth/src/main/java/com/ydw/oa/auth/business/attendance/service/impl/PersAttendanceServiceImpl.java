package com.ydw.oa.auth.business.attendance.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.entity.WxClockIn;
import com.ydw.oa.auth.business.attendance.mapper.PersAttendanceLogMapper;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceRuleService;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.config.RedisUtil;
import com.ydw.oa.auth.util.WebUtil;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.MapUtils;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
@Service
public class PersAttendanceServiceImpl implements IPersAttendanceService {

    @Autowired
    private IAuUsrService usrService;
    @Autowired
    private IPersAttendanceLogService persAttendanceLogService;
    @Autowired
    private IPersAttendanceRuleService persAttendanceRuleService;
    @Autowired
    private WxFeignService wxFeignService;
    @Autowired
    private PersAttendanceLogMapper persAttendanceLogMapper;
    @Autowired
    private RedisUtil redisUtil;


    @Override
    public void export(String yearMonth, String dataPath,String startTime,String endTime,String pubTime) {
        String modelPath = dataPath.concat("/oa_model").concat("/attendance.xlsx");
        String destPath = dataPath.concat("/oa_model").concat("/attendance").concat("-").concat(yearMonth).concat(".xlsx");
        ServletOutputStream outputStream = null;
//        FileInputStream fis = null;
        ExcelWriter writer = null;

        DateTime startDate = DateUtil.parse(startTime, "MM月dd日");
        DateTime endDate = DateUtil.parse(endTime, "MM月dd日");
        long diffDay = DateUtil.between(startDate,endDate, DateUnit.DAY);
        try {
            outputStream = WebUtil.getReponse().getOutputStream();
            FileUtil.copy(modelPath,destPath,true);
            writer = ExcelUtil.getWriter(destPath);
            String year = yearMonth.substring(0,4);
            String monthStr = yearMonth.substring(4);
            String yearMonthStr = year.concat("年").concat(monthStr).concat("月");
            writer.merge(13, "财务公司".concat(yearMonthStr).concat("考核通报（公示期".concat(startTime).concat("-").concat(endTime).concat("）")));
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
            List<List<Object>> rows = getCellList(yearMonth);
            writer.write(rows);
            int rowCount = writer.getRowCount();
            for(int i=3;i<rowCount;i++){
                writer.setRowHeight(i,23);
            }
            writer.merge(3, rows.size() + 3 - 1, 13, 13, "张贴日期：".concat(pubTime).concat("       公示期为").concat(StrUtil.toString(diffDay)).concat("个工作日，如有问题，请在公示期内联系综合办公室，公示期过后视为无异议。"), false);
            BorderStyle borderRight = writer.getCellStyle().getBorderRight();
            CellStyle cellStyle = writer.createCellStyle(13, 3);
            cellStyle.setRotation((short) 255);
            cellStyle.setBorderRight(borderRight);
            cellStyle.setAlignment(HorizontalAlignment.CENTER); // 居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
            Font font3 = writer.createFont();
            this.setFont(font3,10,false,"宋体");
            cellStyle.setFont(font3);
            WebUtil.getReponse().setContentType("application/vnd.ms-excel;charset=utf-8");
            WebUtil.getReponse().setHeader("Content-Disposition","attachment;filename="+URLEncoder.encode("财务公司".concat(yearMonthStr).concat("考勤考核公示表.xls"),"UTF-8"));
            writer.flush(outputStream);

//            fis = new FileInputStream(destPath);
//            byte[] b = new byte[10240];
//            int len = -1;
//            while ((len = fis.read(b)) != -1) {
//                outputStream.write(b, 0, len);
//            }


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

    private List<List<Object>> getCellList(String month){
        List<List<Object>> rows = CollUtil.newArrayList();
        List<AuUsr> list = usrService.list();
        BigDecimal arriveLate = persAttendanceRuleService.getScoreByRule("arrive_late");
        BigDecimal absent = persAttendanceRuleService.getScoreByRule("absent");
        BigDecimal cardShortage = persAttendanceRuleService.getScoreByRule("card_shortage");
        int index = 0;
        for (AuUsr auUsr : list) {
            String usrId = auUsr.getObjectId();
            String wxUserId = auUsr.getWxUserId();
            String userWxKey = "";
            if (StrUtil.isNotEmpty(wxUserId)) {
                userWxKey = wxUserId.concat("#").concat(month);
            }
            List<Object> row = new LinkedList<>();
            row.add(++index);
            row.add(auUsr.getRealName());
            row.add("-");
            row.add("-");
            row.add("-");
            //计算迟到
            BigDecimal arriveLateScore = BigDecimal.ZERO;
            if(StrUtil.isNotEmpty(wxUserId)){

                int lateCount = Integer.parseInt(String.valueOf(redisUtil.hGet(userWxKey, "arrive_late")));
                if (lateCount > 0) {
                    arriveLateScore = arriveLate.multiply(new BigDecimal(lateCount));
                    row.add(lateCount);
                    arriveLateScore = arriveLateScore.setScale(0, RoundingMode.HALF_UP);
                    row.add(NumberUtil.toStr(arriveLateScore));
                }else{
                    row.add("-");
                    row.add("-");
                }
            }else{
                row.add("无");
                row.add("无");
            }
            BigDecimal cardShortageAndAbsentScore = BigDecimal.ZERO;
            int cardShortageAndAbsentCount = 0;
            BigDecimal cardShortageScore = BigDecimal.ZERO;
            BigDecimal absentScore = BigDecimal.ZERO;

            if(StrUtil.isNotEmpty(wxUserId)){
                int cardShortageCount = Integer.parseInt(String.valueOf(redisUtil.hGet(userWxKey, "card_shortage")));
                int absentCount = Integer.parseInt(String.valueOf(redisUtil.hGet(userWxKey, "absent")));
                if (cardShortageCount > 0) {
                    cardShortageScore = cardShortage.multiply(new BigDecimal(cardShortageCount));
                    cardShortageScore = cardShortageScore.setScale(0, RoundingMode.HALF_UP);

                }
                if (absentCount > 0) {
                    absentScore = absent.multiply(new BigDecimal(absentCount));
                    absentScore = absentScore.setScale(0, RoundingMode.HALF_UP);
                }
                cardShortageAndAbsentCount =  cardShortageCount + absentCount;
                if(cardShortageAndAbsentCount > 0){
                    cardShortageAndAbsentScore = cardShortageScore.add(absentScore);
                    cardShortageAndAbsentScore = cardShortageAndAbsentScore.setScale(0, RoundingMode.HALF_UP);
                    row.add(cardShortageAndAbsentCount);
                    row.add(NumberUtil.toStr(cardShortageAndAbsentScore));
                }else{
                    row.add("-");
                    row.add("-");
                }
            }else{
                row.add("无");
                row.add("无");
            }


//            if(StrUtil.isNotEmpty(wxUserId)){
//                int absentCount = Integer.parseInt(String.valueOf(redisUtil.hGet(userWxKey, "absent")));
//                if (absentCount > 0) {
//                    absentScore = absent.multiply(new BigDecimal(absentCount));
//                    row.add(absentCount);
//                    absentScore = absentScore.setScale(0, RoundingMode.HALF_UP);
//                    row.add(NumberUtil.toStr(absentScore));
//                }else{
//                    row.add("-");
//                    row.add("-");
//                }
//            }else{
//                row.add("无");
//                row.add("无");
//            }
            QueryWrapper<PersAttendanceLog> persAttendanceLogQueryWrapper = new QueryWrapper<>();
            persAttendanceLogQueryWrapper.eq("USR_ID",usrId).eq("MONTH",month).eq("STATUZ","已确认").eq("TYPE","公司考核");
            List<PersAttendanceLog> persAttendanceLogCompanys = persAttendanceLogService.list(persAttendanceLogQueryWrapper);
            BigDecimal totalCompany = BigDecimal.ZERO;
            if (persAttendanceLogCompanys.size() > 0) {
                String notes = "";
                for (PersAttendanceLog persAttendanceLogCompany : persAttendanceLogCompanys) {
                    BigDecimal score = persAttendanceLogCompany.getScore();
                    totalCompany = totalCompany.add(score);
                    String note = persAttendanceLogCompany.getNote();
                    notes.concat(note).concat("\r\n");
                }
                row.add(notes);
                totalCompany = totalCompany.setScale(0, RoundingMode.HALF_UP);
                row.add(NumberUtil.toStr(totalCompany));
            }else{
                row.add("-");
                row.add("-");
            }
            QueryWrapper<PersAttendanceLog> persAttendanceLogQueryWrapper2 = new QueryWrapper<>();
            persAttendanceLogQueryWrapper2.eq("USR_ID",usrId).eq("MONTH",month).eq("STATUZ","已确认").eq("TYPE","部门考核");
            List<PersAttendanceLog> persAttendanceLogDepts = persAttendanceLogService.list(persAttendanceLogQueryWrapper2);
            BigDecimal totalDept = BigDecimal.ZERO;
            if (persAttendanceLogDepts.size() > 0) {
                for (PersAttendanceLog persAttendanceLogDept : persAttendanceLogDepts) {
                    BigDecimal score = persAttendanceLogDept.getScore();
                    totalDept = totalDept.add(score);
                }
                totalDept = totalDept.setScale(0, RoundingMode.HALF_UP);
                row.add(NumberUtil.toStr(totalDept));
            }else{
                row.add("-");
            }

            BigDecimal result = NumberUtil.add(new BigDecimal(100), totalCompany, totalDept,arriveLateScore,absentScore);
            result = result.setScale(0, RoundingMode.HALF_UP);
            row.add(NumberUtil.toStr(result));
            rows.add(row);
        }
        return rows;
    }
}
