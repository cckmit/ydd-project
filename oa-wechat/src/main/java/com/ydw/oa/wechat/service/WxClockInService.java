package com.ydw.oa.wechat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

public interface WxClockInService extends IService<WxClockIn>{

    Map<String,Object> countWxClockInDataGroupByUseridAndYearMonth(String userid,String clockInYearMonth);

    List<Map<String,Object>>  countWxClockInDataGroupByYearMonth(String clockInYearMonth);

    List<Map<String,Object>> countWxClockInDataAll();

    List<WxClockIn> listWxClockInDataByYearMonthAndDay(String clockInYearMonth,String clockInDay);
}
