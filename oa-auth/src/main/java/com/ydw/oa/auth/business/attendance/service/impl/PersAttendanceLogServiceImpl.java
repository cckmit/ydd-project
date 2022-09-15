package com.ydw.oa.auth.business.attendance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.tmsps.fk.common.util.ChkUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.ydw.oa.auth.business.attendance.entity.WxClockIn;
import com.ydw.oa.auth.business.attendance.mapper.PersAttendanceLogMapper;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceLogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.auth.business.attendance.service.IPersAttendanceRuleService;
import com.ydw.oa.auth.business.usr.entity.AuUsr;
import com.ydw.oa.auth.business.usr.service.IAuUsrService;
import com.ydw.oa.auth.business_wx.WxFeignService;
import com.ydw.oa.auth.config.RedisUtil;
import com.ydw.oa.auth.util.DateTools;
import io.swagger.models.auth.In;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.peer.CanvasPeer;
import java.math.BigDecimal;
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
public class PersAttendanceLogServiceImpl extends ServiceImpl<PersAttendanceLogMapper, PersAttendanceLog> implements IPersAttendanceLogService {

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
    public void autoSaveAttendanceLog(String month,String day) {
        Wrapper<List<WxClockIn>> wxClockInsByYearMonthAndDay = wxFeignService.getClockinsByYearMonthAndDay(month, day);
        if (wxClockInsByYearMonthAndDay.getCode() != 200) {
            return;
        }
        List<WxClockIn> wxClockInList = wxClockInsByYearMonthAndDay.getResult();
        for (WxClockIn wxClockIn : wxClockInList) {
            QueryWrapper<AuUsr> auUsrQueryWrapper = new QueryWrapper<>();
            auUsrQueryWrapper.eq("WX_USER_ID",wxClockIn.getUserid());
            AuUsr auUsr = usrService.getOne(auUsrQueryWrapper);
            if(ChkUtil.isNull(auUsr)){
                continue;
            }

            BigDecimal score;
            String note = "";
            // 计算系统考勤分数
            int arriveLate = wxClockIn.getArriveLate();// 是否迟到 是1 否0
            if (arriveLate == 1) {
                score = persAttendanceRuleService.getScoreByRule("arrive_late");
                note = "【迟到】扣除".concat(score.abs().toString()).concat("分");
                savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
            }
//            int leaveEarly = wxClockIn.getLeaveEarly();// 是否早退 是1 否0
//            if (leaveEarly == 1) {
//                score = persAttendanceRuleService.getScoreByRule("leave_early");
//                note = "【早退】扣除".concat(score.abs().toString()).concat("分");
//                savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
//            }
            int cardShortage = wxClockIn.getCardShortage();// 是否缺卡 是1 否0
            if (cardShortage == 1) {
                score = persAttendanceRuleService.getScoreByRule("card_shortage");
                note = "【缺卡】扣除".concat(score.abs().toString()).concat("分");
                savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
            }
            int absent = wxClockIn.getAbsent();// 是否旷工 是1 否0
            if (absent == 1) {
                score = persAttendanceRuleService.getScoreByRule("absent");
                note = "【旷工】扣除".concat(score.abs().toString()).concat("分");
                savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
            }
//            int askLeave = wxClockIn.getAskLeave();// 是否请假 是1 否0
//            if (askLeave == 1) {
//                score = persAttendanceRuleService.getScoreByRule("ask_leave");
//                note = "【请假】扣除".concat(score.abs().toString()).concat("分");
//                savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
//            }
//					String be_out = map.get("be_out") + "";// 是否外出 是1 否0
//					if ("1".equals(be_out)) {
//						score = persAttendanceRuleService.getScoreByRule(be_out, score, "be_out");
//						note = "【外出】扣除".concat(score.toString()).concat("分");
//						savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
//					}
//
//					String business_trip = map.get("business_trip") + "";// 是否出差 是1 否0
//					if ("1".equals(business_trip)) {
//						score = persAttendanceRuleService.getScoreByRule(business_trip, score, "business_trip");
//						note = "【外出】扣除".concat(score.toString()).concat("分");
//						savePersAttendanceLog(auUsr.getObjectId(), month, day,score,note);
//					}

        }


    }

    @Override
    public void countAttendanceLogDataGroupByYearMonth(String yearMonth) {

        List<Map<String, Object>> logs = persAttendanceLogMapper.sumAttendanceLogDataByMonth(yearMonth);
        Set<String> redisKeys = new HashSet<>();
        for (Map<String, Object> log : logs) {
            String usrId = MapUtils.getString(log, "USR_ID");
            String redisKey = usrId.concat("#").concat(yearMonth);
            if(!redisKeys.contains(redisKey)){
                redisUtil.delete(redisKey);
                redisKeys.add(redisKey);
            }
            putAttendanceLogToCache(log,redisKey);
            redisKeys.add(usrId);
        }

    }

    @Override
    public void countAttendanceLogDataGroupByUsrIdAndYearMonth(String usrId, String yearMonth) {
        List<Map<String, Object>> logs = persAttendanceLogMapper.sumAttendanceLogDataByUsrIdAndMonth(usrId, yearMonth);
        Set<String> redisKeys = new HashSet<>();
        for (Map<String, Object> log : logs) {
            String redisKey = usrId.concat("#").concat(yearMonth);
            if(!redisKeys.contains(redisKey)){
                redisUtil.delete(redisKey);
                redisKeys.add(redisKey);
            }
            putAttendanceLogToCache(log,redisKey);
            redisKeys.add(usrId);
        }
    }

    @Override
    public IPage<Map<String,Object>> pageAttendanceLogByStatuzAndUserId(Page<PersAttendanceLog> query, String userId) {
        return persAttendanceLogMapper.pageAttendanceLogByStatuzAndUsrId(query, userId);
    }

    @Override
    public List<PersAttendanceLog> listAttendanceLogMonthByStatuzAndDeptId(String month, String statuz, String deptId) {
        return persAttendanceLogMapper.listAttendanceLogMonthByStatuzAndDeptId(month,statuz,deptId);
    }

    private void putAttendanceLogToCache(Map<String, Object> log,String redisKey) {
        String operationType = MapUtils.getString(log, "OPERATION_TYPE");
        if ("加分".equals(operationType)) {
            Integer score = MapUtils.getInteger(log, "SCORE");
            redisUtil.hPut(redisKey,"add_score",score);
            Object del_score = redisUtil.hGet(redisKey, "del_score");
            if (ChkUtil.isNotNull(del_score)) {
                Integer all_score = (Integer)del_score + score +100;
                redisUtil.hPut(redisKey,"all_score",all_score);
            }else{
                redisUtil.hPut(redisKey,"del_score",0);
                redisUtil.hPut(redisKey,"all_score",100+score);
            }
        }
        if ("扣分".equals(operationType)) {
            Integer score = MapUtils.getInteger(log, "SCORE");
            redisUtil.hPut(redisKey,"del_score",score);
            Object add_score = redisUtil.hGet(redisKey, "add_score");
            if (ChkUtil.isNotNull(add_score)) {
                Integer all_score = (Integer)add_score + score +100;
                redisUtil.hPut(redisKey,"all_score",all_score);
            }else{
                redisUtil.hPut(redisKey,"add_score",0);
                redisUtil.hPut(redisKey,"all_score",100+score);
            }
        }
    }

    private boolean savePersAttendanceLog(String userId, String month, String day,BigDecimal score,String note){
        PersAttendanceLog persAttendanceLog = new PersAttendanceLog();
        persAttendanceLog.setUsrId(userId);
        persAttendanceLog.setMonth(month);
        persAttendanceLog.setDay(day);
        persAttendanceLog.setType("系统考核");
        persAttendanceLog.setOperationType("扣分");
        persAttendanceLog.setScore(score);
        persAttendanceLog.setNote(note);
        persAttendanceLog.setStatuz("已确认");
        return persAttendanceLogService.save(persAttendanceLog);
    }

}
