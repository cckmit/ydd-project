package com.ydw.oa.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ydw.oa.wechat.client.WechatApiProxyClient;
import com.ydw.oa.wechat.entity.business.WxApprove;
import com.ydw.oa.wechat.entity.business.WxAttendance;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAApproveDetailResult;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAApproveResult;
import com.ydw.oa.wechat.entity.wechat.WxCorpOAClockInResult;
import com.ydw.oa.wechat.entity.wechat.oa.WxCorpOAClockInInfo;
import com.ydw.oa.wechat.service.SyncOADataService;
import com.ydw.oa.wechat.service.WxApproveService;
import com.ydw.oa.wechat.service.WxAttendanceService;
import com.ydw.oa.wechat.service.WxClockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.*;
@Service
public class SyncOADataServiceImpl implements SyncOADataService {

    @Autowired
    private WechatApiProxyClient wechatApiProxyClient;

    @Autowired
    private WxClockInService wxClockInService;
    @Autowired
    private WxApproveService wxApproveService;
    @Autowired
    private WxAttendanceService wxAttendanceService;

    @Override
    @Transactional
    public boolean syncOAApproveData(Long beginTime, Long endTime) {
        //获取打卡数据
        Map<String,Object> map = new HashMap();
        map.put("starttime",beginTime);
        map.put("endtime",endTime);
        map.put("cursor",0);
        map.put("size",100);
        JSONArray jsonArray = JSONUtil.createArray();
        jsonArray.add(JSONUtil.createObj().putOnce("key", "sp_status").putOnce("value", "2"));
        map.put("filters", jsonArray);

        WxCorpOAApproveResult approveSpNoList = wechatApiProxyClient.getApproveSpNoList(map);
        List<String> spNoList = approveSpNoList.getSp_no_list();
        if (CollUtil.isNotEmpty(spNoList)) {
            for (String spNo : spNoList) {

                WxCorpOAApproveDetailResult approveDetail = wechatApiProxyClient.getApproveDetail(spNo);
                JSONObject detailInfo = approveDetail.getInfo();
                //获取申请人userid
                JSONObject applyer = detailInfo.getJSONObject("applyer");
                String userid = applyer.getStr("userid");
                Integer spStatus = detailInfo.getInt("sp_status");

                JSONArray contents = detailInfo.getJSONObject("apply_data").getJSONArray("contents");

                for (int i = 0; i < contents.size(); i++) {
                    JSONObject controlObject = contents.getJSONObject(i);
                    String control = controlObject.getStr("control");
                    JSONObject valueObject = controlObject.getJSONObject("value");
                    JSONObject attendanceObject = null;
                    if (!"Vacation".equals(control) && !"Attendance".equals(control)) {
                        continue;
                    }
                    if ("Vacation".equals(control)) {
                        attendanceObject = valueObject.getJSONObject("vacation").getJSONObject("attendance");
                    }

                    if ("Attendance".equals(control)) {
                        attendanceObject = valueObject.getJSONObject("attendance");
                    }

                    // 假勤组件类型：1-请假；2-补卡；3-出差；4-外出；5-加班
                    int attendanceType = attendanceObject.getInt("type");

                    QueryWrapper<WxApprove> wxApproveQueryWrapper = new QueryWrapper<>();
                    wxApproveQueryWrapper.eq("sp_no",spNo);

                    WxApprove wxApprove = wxApproveService.getOne(wxApproveQueryWrapper);
                    if (ObjectUtil.isNull(wxApprove)) {
                        wxApprove = new WxApprove();
                        wxApprove.setSpNo(spNo);
                        wxApprove.setUserid(userid);
                    }
                    wxApprove.setAttendanceType(attendanceType);
                    JSONObject dateRange = attendanceObject.getJSONObject("date_range");
                    if (!JSONUtil.isNull(dateRange)) {
                        //时间类型
                        String dateType = dateRange.getStr("type");
                        int newDuration = dateRange.getInt("new_duration");
                        Long newBegin = dateRange.getLong("new_begin");
                        Long newEnd = dateRange.getLong("new_end");
                        wxApprove.setDateType(dateType);
                        wxApprove.setNewBegin(newBegin);
                        wxApprove.setNewEnd(newEnd);
                        wxApprove.setNewDuration(newDuration);
                        wxApprove.setApproveStatus(spStatus);
                        wxApproveService.saveOrUpdate(wxApprove);
                        parseVacation(newBegin,newEnd,wxApprove);
//                        if(spStatus == 2 ){
//                        } else {
//                            revokeVacation(wxApprove.getId());
//                        }
                    }
                }

            }
        }
        return true;
    }

    @Override
    @Transactional
    public boolean syncOAClockInData(List<String> useridLists,Long beginTime, Long endTime) {


        //出勤情况
        Map<String,WxClockIn> wxClockInMap = new HashMap<>();
        //判断是否进行过查询
        Map<String,Boolean> attendanceAssertMap = new HashMap<>();

        //获取打卡数据
        Map<String,Object> map = new HashMap();
        map.put("opencheckindatatype",3);
        map.put("starttime",beginTime);
        map.put("endtime",endTime);
        map.put("useridlist",useridLists);
        WxCorpOAClockInResult clockInResult = wechatApiProxyClient.getClockInData(map);
        //分析打卡结果
        if (clockInResult.getErrcode() == 0) {
            //打开结果  正常打卡  迟到  早退   缺卡 旷工
            List<WxCorpOAClockInInfo> wxCorpOAClockInInfos = clockInResult.getCheckindata();
            for (WxCorpOAClockInInfo wxCorpOAClockInInfo : wxCorpOAClockInInfos) {
                String userid = wxCorpOAClockInInfo.getUserid();
                Long checkinTime = wxCorpOAClockInInfo.getCheckinTime();
                Date checkinDate = new Date(checkinTime * 1000L);
                QueryWrapper<WxAttendance> wxAttendanceQueryWrapper = new QueryWrapper<>();
                String checkinYearMonth = DateUtil.format(checkinDate, "yyyyMM");
                String checkinDay = DateUtil.format(checkinDate, "dd");
                String key = checkinYearMonth.concat(checkinDay).concat("#").concat(userid);
                WxClockIn wxClockIn = wxClockInMap.get(key);
                if (ObjectUtil.isNull(wxClockIn)) {
                    wxClockIn = new WxClockIn();
                    wxClockIn.setUserid(userid);
                    wxClockIn.setClockInYearMonth(checkinYearMonth);
                    wxClockIn.setClockInDay(checkinDay);
                    wxClockInMap.put(key, wxClockIn);
                    attendanceAssertMap.put(key, false);
                }
                if (wxClockIn.getAttendanceRange() == 0 && !attendanceAssertMap.get(key)) {
                    wxAttendanceQueryWrapper.eq("userid",userid).eq("attendance_year_month",checkinYearMonth).eq("attendance_day",checkinDay).eq("attendance_status",1);
//                    WxAttendance wxAttendance = wxAttendanceService.getOne(wxAttendanceQueryWrapper);
                    List<WxAttendance> wxAttendanceList = wxAttendanceService.list(wxAttendanceQueryWrapper);
                    if (CollUtil.isNotEmpty(wxAttendanceList)) {
                        boolean attendanceRangeAm = false;
                        boolean attendanceRangePm = false;
                        for (WxAttendance wxAttendance : wxAttendanceList) {
                            int attendanceType = wxAttendance.getAttendanceType();
                            int attendanceRange = wxAttendance.getAttendanceRange();
                            if (attendanceType == 1) {//请假
                                wxClockIn.setAskLeave(1);
                            } else if (attendanceType == 3) {//出差
                                wxClockIn.setBusinessTrip(1);
                            } else if (attendanceType == 4) {//外出
                                wxClockIn.setBeOut(1);
                            }
                            if (attendanceRange == 1){
                                attendanceRangeAm = true;
                                wxClockIn.setAttendanceRange(1);
                            }
                            if (attendanceRange == 2){
                                attendanceRangePm = true;
                                wxClockIn.setAttendanceRange(2);
                            }
                            if (attendanceRange == 3){
                                wxClockIn.setAttendanceRange(3);
                            }

                        }

                        wxClockIn.setAttendanceRange(attendanceRangeAm && attendanceRangePm?3:wxClockIn.getAttendanceRange());
                    }
//                    if (ObjectUtil.isNotNull(wxAttendance)) {
//                        int attendanceType = wxAttendance.getAttendanceType();
//                        if (attendanceType == 1) {//请假
//                            wxClockIn.setAskLeave(1);
//                        } else if (attendanceType == 3) {//出差
//                            wxClockIn.setBusinessTrip(1);
//                        } else if (attendanceType == 4) {//外出
//
//                        }
//                        wxClockIn.setAttendanceRange(wxAttendance.getAttendanceRange());
//                    }
                    attendanceAssertMap.put(key, true);
                }



                String checkinType = wxCorpOAClockInInfo.getCheckinType();
                String exceptionTypeStr = wxCorpOAClockInInfo.getExceptionType();




                if ("上班打卡".equals(checkinType)) {
                    if (StrUtil.isNotEmpty(exceptionTypeStr)) {
                        if (wxClockIn.getAttendanceRange() == 0 || wxClockIn.getAttendanceRange() == 2) {
                            String[] exceptionTypes = exceptionTypeStr.split(";");
                            for (String exceptionType : exceptionTypes) {
                                if ("未打卡".equals(exceptionType)){
                                    wxClockIn.setOnClockIn(1);
                                    wxClockIn.setExceptionClockIn(1);
                                }else if ("时间异常".equals(exceptionType)) {
                                    wxClockIn.setArriveLate(1);
                                    wxClockIn.setExceptionClockIn(1);
                                } else if ("地点异常".equals(exceptionType)) {
                                    wxClockIn.setOutside(1);
                                    wxClockIn.setExceptionClockIn(1);
                                }
                            }
                        }
                    }
//                    if (ObjectUtil.isNull(wxClockIn)) {
//                        wxClockInMap.put(userid,wxClockIn);
//                    }
                }

                if ("下班打卡".equals(checkinType)) {
                    if (StrUtil.isNotEmpty(exceptionTypeStr)) {
                        if (wxClockIn.getAttendanceRange() == 0 || wxClockIn.getAttendanceRange() == 1) {
                            String[] exceptionTypes = exceptionTypeStr.split(";");
                            for (String exceptionType : exceptionTypes) {
                                if ("未打卡".equals(exceptionType)){
                                    wxClockIn.setOffClockIn(1);
                                    wxClockIn.setExceptionClockIn(1);
                                } else if ("时间异常".equals(exceptionType)) {
                                    wxClockIn.setLeaveEarly(1);
                                    wxClockIn.setExceptionClockIn(1);
                                } else if ("地点异常".equals(exceptionType)) {
                                    wxClockIn.setOutside(1);
                                    wxClockIn.setExceptionClockIn(1);
                                }
                            }
                        }
                    }
//                    if (ObjectUtil.isNull(wxClockIn)) {
//                        wxClockInMap.put(userid,wxClockIn);
//                    }
                }

//                if ("外出打卡".equals(checkinType)) {
//
//                }
            }
            for(String key : wxClockInMap.keySet()){
                WxClockIn wxClockIn = wxClockInMap.get(key);
                int onWork = wxClockIn.getOnClockIn();
                int offWork = wxClockIn.getOffClockIn();
                int attendanceRange = wxClockIn.getAttendanceRange();
                if(onWork == 1 && offWork == 1){
                    wxClockIn.setAbsent(1);
                }else if (onWork == 1 || offWork == 1){
                    if((attendanceRange == 1 && offWork == 1) || (attendanceRange == 2 && onWork == 1)){
                        wxClockIn.setAbsent(1);
                    }else{
                        wxClockIn.setCardShortage(1);
                    }
                }

                if(onWork == 0 && offWork == 0){
                    if(wxClockIn.getExceptionClockIn() != 1){
                        wxClockIn.setNormalClockIn(1);
                    }
                }
                wxClockInService.save(wxClockIn);
            }
        }else{

        }
        return true;
    }

    @Override
    @Transactional
    public boolean revokeOAApproveData(Long beginTime, Long endTime) {
        //获取审批数据
        Map<String,Object> map = new HashMap();
        map.put("starttime",beginTime);
        map.put("endtime",endTime);
        map.put("cursor",0);
        map.put("size",100);
        JSONArray jsonArray = JSONUtil.createArray();
        jsonArray.add(JSONUtil.createObj().putOnce("key", "sp_status").putOnce("value", "6"));
        map.put("filters", jsonArray);

        WxCorpOAApproveResult approveSpNoList = wechatApiProxyClient.getApproveSpNoList(map);
        List<String> spNoList = approveSpNoList.getSp_no_list();
        if (CollUtil.isNotEmpty(spNoList)) {
            for (String spNo : spNoList) {

                WxCorpOAApproveDetailResult approveDetail = wechatApiProxyClient.getApproveDetail(spNo);
                JSONObject detailInfo = approveDetail.getInfo();
                //获取申请人userid
                JSONObject applyer = detailInfo.getJSONObject("applyer");
                String userid = applyer.getStr("userid");
                Integer spStatus = detailInfo.getInt("sp_status");
                JSONArray contents = detailInfo.getJSONObject("apply_data").getJSONArray("contents");
                for (int i = 0; i < contents.size(); i++) {
                    JSONObject controlObject = contents.getJSONObject(i);
                    String control = controlObject.getStr("control");
                    JSONObject valueObject = controlObject.getJSONObject("value");
                    JSONObject attendanceObject = null;
                    if (!"Vacation".equals(control) && !"Attendance".equals(control)) {
                        continue;
                    }
                    if ("Vacation".equals(control)) {
                        attendanceObject = valueObject.getJSONObject("vacation").getJSONObject("attendance");
                    }

                    if ("Attendance".equals(control)) {
                        attendanceObject = valueObject.getJSONObject("attendance");
                    }

                    // 假勤组件类型：1-请假；2-补卡；3-出差；4-外出；5-加班
                    int attendanceType = attendanceObject.getInt("type");

                    QueryWrapper<WxApprove> wxApproveQueryWrapper = new QueryWrapper<>();
                    wxApproveQueryWrapper.eq("sp_no",spNo);

                    WxApprove wxApprove = wxApproveService.getOne(wxApproveQueryWrapper);
                    if (ObjectUtil.isNull(wxApprove)) {
                        continue;
                    }
                    wxApprove.setAttendanceType(attendanceType);
                    JSONObject dateRange = attendanceObject.getJSONObject("date_range");
                    if (!JSONUtil.isNull(dateRange)) {
                        //时间类型
                        String dateType = dateRange.getStr("type");
                        int newDuration = dateRange.getInt("new_duration");
                        Long newBegin = dateRange.getLong("new_begin");
                        Long newEnd = dateRange.getLong("new_end");
                        wxApprove.setDateType(dateType);
                        wxApprove.setNewBegin(newBegin);
                        wxApprove.setNewEnd(newEnd);
                        wxApprove.setNewDuration(newDuration);
                        wxApprove.setApproveStatus(spStatus);
                        wxApproveService.saveOrUpdate(wxApprove);
                        revokeVacation(wxApprove.getId());
                    }
                }

            }
        }
        return true;
    }

    private void revokeVacation(BigInteger approveId) {
        QueryWrapper<WxAttendance> wxAttendanceQueryWrapper = new QueryWrapper<>();
        wxAttendanceQueryWrapper.eq("approve_id", approveId);
        List<WxAttendance> wxAttendances = wxAttendanceService.list(wxAttendanceQueryWrapper);
        if (CollUtil.isNotEmpty(wxAttendances)) {
            for (WxAttendance wxAttendance : wxAttendances) {
                wxAttendance.setAttendanceStatus(2);
            }
            wxAttendanceService.updateBatchById(wxAttendances);
        }
    }

    private String parseVacation(Long newBegin,Long newEnd,WxApprove wxApprove) {
        QueryWrapper<WxAttendance> wxAttendanceQueryWrapper = new QueryWrapper<>();
        wxAttendanceQueryWrapper.eq("approve_id", wxApprove.getId());
        wxAttendanceService.remove(wxAttendanceQueryWrapper);
//        String day = DateUtil.format(new Date(),"dd");
        Date dateBegin = new Date(newBegin * 1000L);
//        if(!DateUtil.isSameTime(dateBegin,DateUtil.beginOfDay(dateBegin))){
//            dateBegin = new Date(newBegin * 1000L-1000L);
//        }
        Date dateEnd = new Date(newEnd*1000L);
        //首先判断两个时间是否一致
        if (newBegin - newEnd == 0) {
            //若一致，说明是请的半天假，判断是上午假还是下午假
            String yearMonth = DateUtil.format(dateBegin, "yyyyMM");
            String dd = DateUtil.format(dateBegin, "dd");
            WxAttendance  wxAttendance = new WxAttendance();
            int attendanceRange;
            if(DateUtil.isAM(dateBegin)){
                attendanceRange = 1;
            }else{
                attendanceRange = 2;
            }
            wxAttendance.setAttendanceYearMonth(yearMonth);
            wxAttendance.setAttendanceDay(DateUtil.format(dateBegin,"dd"));
            wxAttendance.setAttendanceRange(attendanceRange);
            saveWxAttendance(wxAttendance,wxApprove);
        }else{
            long betweenDay = DateUtil.betweenDay(dateBegin, dateEnd, true);
            if (betweenDay == 0) {
                boolean beginResult = DateUtil.isAM(dateBegin);
                boolean endResult = DateUtil.isAM(dateEnd);
                WxAttendance wxAttendance = new WxAttendance();
                int attendanceRange = 0;
                if(beginResult && endResult){
                    attendanceRange = 1;
                }else if (beginResult && !endResult){
                    attendanceRange = 3;
                }else{
                    attendanceRange = 2;
                }
                wxAttendance.setAttendanceYearMonth(DateUtil.format(dateBegin,"yyyyMM"));
                wxAttendance.setAttendanceDay(DateUtil.format(dateBegin,"dd"));
                wxAttendance.setAttendanceRange(attendanceRange);
                saveWxAttendance(wxAttendance,wxApprove);
            }
            if (betweenDay > 0) {
                boolean beginResult = DateUtil.isAM(dateBegin);
                boolean endResult = DateUtil.isAM(dateEnd);
                WxAttendance wxAttendance = new WxAttendance();

                int attendanceRange = 0;
                if (beginResult) {
                    //如果是上午请假，那就是请一天
                    attendanceRange = 3;
                } else{
                    //如果是下午请假，那就是请一下午
                    attendanceRange = 2;
                }
                wxAttendance.setAttendanceRange(attendanceRange);
                wxAttendance.setAttendanceYearMonth(DateUtil.format(dateBegin,"yyyyMM"));
                wxAttendance.setAttendanceDay(DateUtil.format(dateBegin,"dd"));
                saveWxAttendance(wxAttendance,wxApprove);
                wxAttendance = new WxAttendance();
                if (endResult) {
                    //如果是上午请假，那就是请一上午
                    attendanceRange = 1;
                }else{
                    //如果是下午请假，那就是请一天
                    attendanceRange = 3;
                }
                wxAttendance.setAttendanceYearMonth(DateUtil.format(dateEnd,"yyyyMM"));
                wxAttendance.setAttendanceDay(DateUtil.format(dateEnd,"dd"));
                wxAttendance.setAttendanceRange(attendanceRange);
                saveWxAttendance(wxAttendance,wxApprove);

                for (long i = 1; i < betweenDay; i++) {
                    wxAttendance = new WxAttendance();
                    DateTime dateTime = DateUtil.offsetDay(dateBegin, (int) (i));
                    wxAttendance.setAttendanceYearMonth(DateUtil.format(dateTime,"yyyyMM"));
                    wxAttendance.setAttendanceDay(DateUtil.format(dateTime,"dd"));
                    wxAttendance.setAttendanceRange(3);
                    saveWxAttendance(wxAttendance,wxApprove);
                }
            }
        }
        return "";
    }

    private void saveWxAttendance(WxAttendance attendance, WxApprove wxApprove) {
        attendance.setApproveId(wxApprove.getId());
        attendance.setUserid(wxApprove.getUserid());
        attendance.setAttendanceStatus(wxApprove.getApproveStatus() == 2 ? 1 : 2);
        attendance.setAttendanceType(wxApprove.getAttendanceType());
        wxAttendanceService.save(attendance);
    }
}
