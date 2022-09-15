package com.ydw.oa.wechat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

public interface WxClockInMapper extends BaseMapper<WxClockIn> {

    List<Map<String,Object>> sumWxClockInData();

    List<Map<String,Object>> sumWxClockInDataByYearMonth(String clockInYearMonth);

    Map<String,Object> sumWxClockInDataByUseridAndYearMonth(String userid,String clockInYearMonth);
}
