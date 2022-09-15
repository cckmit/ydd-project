package com.ydw.oa.wechat.entity.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydw.oa.wechat.entity.DataModel;
import lombok.Data;

import java.math.BigInteger;

@TableName("t_wx_attendance")
@Data
public class WxAttendance extends DataModel {

    //微信用户id
    private String userid;
    //打卡年月
    private String attendanceYearMonth;
    //打卡日
    private String attendanceDay;
    //假勤类型 1-请假；2-补卡；3-出差；4-外出；5-加班
    private int attendanceType;
    //假勤范围 1-am（上午）；2-pm（下午）；3-all（全天）
    private int attendanceRange;
    //对应审批表id
    private BigInteger approveId;
    //假勤状态 1-正常 2-撤销（撤销无效）
    private int attendanceStatus;
}
