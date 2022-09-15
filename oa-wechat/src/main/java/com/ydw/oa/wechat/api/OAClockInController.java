package com.ydw.oa.wechat.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tmsps.fk.common.wrapper.WrapMapper;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import com.ydw.oa.wechat.service.WxClockInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/oa/clockin")
@Slf4j
public class OAClockInController {

    @Autowired
    private WxClockInService wxClockInService;


    /**
     * 获取所有用户某天的考勤数据
     * @param yearMonth 年月
     * @param day   日
     * @return
     */
    @GetMapping("/{yearMonth}/{day}")
    public Wrapper<List<WxClockIn>> clockins(@PathVariable String yearMonth,@PathVariable String day) {
        List<WxClockIn> wxClockIns = wxClockInService.listWxClockInDataByYearMonthAndDay(yearMonth, day);
        return WrapMapper.ok(wxClockIns);
    }

    /**
     * 获取某个微信用户当月的考勤统计数据
     * @param yearMonth 年月
     * @param userid   微信用户id
     * @return
     */
    @GetMapping("/{yearMonth}")
    public Wrapper<Map<String,Object>> getClockin(@PathVariable String yearMonth,String userid) {

        Map<String, Object> stringObjectMap = wxClockInService.countWxClockInDataGroupByUseridAndYearMonth(userid, yearMonth);
        return WrapMapper.ok(stringObjectMap);
    }

    /**
     * 获取某个微信用户当月的考勤详情数据
     * @param yearMonth 年月
     * @param userid   微信用户id
     * @return
     */
    @PostMapping("/list")
    public Wrapper<IPage<WxClockIn>> getClockinDetail(@RequestParam String userid, @RequestParam String yearMonth,@RequestBody Page<WxClockIn> page) {
        QueryWrapper<WxClockIn> wxClockInQueryWrapper = new QueryWrapper<>();
        wxClockInQueryWrapper.eq("clock_in_year_month", yearMonth);
        wxClockInQueryWrapper.eq("userid",userid);
        wxClockInQueryWrapper.orderByAsc("clock_in_day");
        IPage<WxClockIn> pageClockIns = wxClockInService.page(page, wxClockInQueryWrapper);
        return WrapMapper.ok(pageClockIns);
    }
}
