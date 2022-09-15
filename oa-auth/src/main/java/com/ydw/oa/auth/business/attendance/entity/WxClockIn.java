package com.ydw.oa.auth.business.attendance.entity;

import lombok.Data;

@Data
public class WxClockIn {

    //微信用户id
    private String userid;
    //打卡年月
    private String clockInYearMonth;
    //打卡日
    private String clockInDay;
    //是否正常打卡  是1 否0
    private int normalClockIn;
    //是否出差  是1 否0
    private int businessTrip;
    //是否外出  是1 否0
    private int beOut;
    //是否请假  是1 否0
    private int askLeave;
    //是否迟到  是1 否0
    private int arriveLate;
    //是否早退  是1 否0
    private int leaveEarly;
    //是否缺卡  是1 否0
    private int cardShortage;
    //是否旷工  是1 否0
    private int absent;
    //是否范围外打卡 是1 否0
    private int outside;
    //是否上班打卡 是1 否0
    private int onClockIn;
    //是否下班打卡 是1 否0
    private int offClockIn;
    //是否异常打卡 是1 否0
    private int exceptionClockIn;
    //假勤范围 0-正常上班；1-am（上午）；2-pm（下午）；3-all（全天）
    private int attendanceRange;
}
