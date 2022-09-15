package com.ydw.oa.wechat.entity.business;

import com.baomidou.mybatisplus.annotation.TableName;
import com.ydw.oa.wechat.entity.DataModel;
import lombok.Data;

import java.sql.Timestamp;

@TableName("t_wx_approve")
@Data
public class WxApprove extends DataModel {

    //微信用户id
    private String userid;
    //审批编号
    private String spNo;
    //假勤类型 1-请假；2-补卡；3-出差；4-外出；5-加班
    private int attendanceType;
    //假勤组件时间类型
    private String dateType;
    //假勤开始时间
    private Long newBegin;
    //假勤结束时间
    private Long newEnd;
    //假勤时长
    private int newDuration;
    //假勤状态 1-正常 2-撤销
    private int approveStatus;
}
