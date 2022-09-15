package com.ydw.oa.wechat.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.wechat.config.RedisUtil;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import com.ydw.oa.wechat.mapper.WxClockInMapper;
import com.ydw.oa.wechat.service.WxClockInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WxClockInServiceImpl extends ServiceImpl<WxClockInMapper,WxClockIn> implements WxClockInService {

    @Autowired
    private WxClockInMapper wxClockInMapper;
    @Autowired
    private RedisUtil redisUtil;

    private void putClockInDataToCache(Map<String, Object> clockin,String userid,String clockInYearMonth) {
        String redisKey = userid.concat("#").concat(clockInYearMonth);
        HashMap<String, String> clockinMap = new HashMap<>();
        clockinMap.put("normal_clock_in",MapUtil.getStr(clockin,"normal_clock_in"));
        clockinMap.put("arrive_late",MapUtil.getStr(clockin,"arrive_late"));
        clockinMap.put("leave_early",MapUtil.getStr(clockin,"leave_early"));
        clockinMap.put("card_shortage",MapUtil.getStr(clockin,"card_shortage"));
        clockinMap.put("absent",MapUtil.getStr(clockin,"absent"));
        clockinMap.put("ask_leave",MapUtil.getStr(clockin,"ask_leave"));
        clockinMap.put("be_out", MapUtil.getStr(clockin, "be_out"));
        clockinMap.put("business_trip",MapUtil.getStr(clockin,"business_trip"));
        redisUtil.hPutAll(redisKey,clockinMap);
        Map<Object, Object> objectObjectMap = redisUtil.hGetAll(redisKey);
        System.err.println(JSONUtil.toJsonStr(objectObjectMap));
    }

    @Override
    public Map<String, Object> countWxClockInDataGroupByUseridAndYearMonth(String userid,String clockInYearMonth) {
        Map<String, Object> clockin = wxClockInMapper.sumWxClockInDataByUseridAndYearMonth(userid, clockInYearMonth);
        if (MapUtil.isNotEmpty(clockin)) {
            putClockInDataToCache(clockin,userid,clockInYearMonth);
        }
        return clockin;
    }

    @Override
    public List<Map<String, Object>> countWxClockInDataGroupByYearMonth(String clockInYearMonth) {
        List<Map<String, Object>> clockins = wxClockInMapper.sumWxClockInDataByYearMonth(clockInYearMonth);
        for (Map<String, Object> clockin : clockins) {
            if (MapUtil.isNotEmpty(clockin)) {
                String userid = MapUtil.getStr(clockin,"userid");
                putClockInDataToCache(clockin,userid,clockInYearMonth);
            }
        }
        return clockins;
    }

    @Override
    public List<Map<String, Object>> countWxClockInDataAll() {
        List<Map<String, Object>> clockins = wxClockInMapper.sumWxClockInData();
        for (Map<String, Object> clockin : clockins) {
            String clockInYearMonth = MapUtil.getStr(clockin,"clock_in_year_month");
            String userid = MapUtil.getStr(clockin,"userid");
            if (MapUtil.isNotEmpty(clockin)) {
                putClockInDataToCache(clockin,userid,clockInYearMonth);
            }
        }
        return clockins;
    }

    @Override
    public List<WxClockIn> listWxClockInDataByYearMonthAndDay(String clockInYearMonth, String clockInDay) {
        QueryWrapper<WxClockIn> wxClockInQueryWrapper = new QueryWrapper<>();
        wxClockInQueryWrapper.eq("clock_in_year_month", clockInYearMonth).eq("clock_in_day", clockInDay);
        return this.list(wxClockInQueryWrapper);
    }
}
