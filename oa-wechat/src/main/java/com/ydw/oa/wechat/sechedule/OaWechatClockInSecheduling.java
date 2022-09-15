package com.ydw.oa.wechat.sechedule;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.tmsps.fk.common.wrapper.Wrapper;
import com.ydw.oa.wechat.client.OAApiClient;
import com.ydw.oa.wechat.service.SyncOADataService;
import com.ydw.oa.wechat.service.WxClockInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;


@Component
@RestController
public class OaWechatClockInSecheduling {

    @Autowired
    private SyncOADataService syncOADataService;
    @Autowired
    private WxClockInService wxClockInService;
    @Autowired
    private OAApiClient oaApiClient;


    /**
     * 获取考勤数据并同步缓存计算考核
     */
    //凌晨12点半更新一次
    @Scheduled(cron = "0 30 0 * * ?")
    private void syncOAClockInData(){
        DateTime yesterdayDate = DateUtil.offsetDay(new Date(), -1);
        Long beginTime = DateUtil.beginOfDay(yesterdayDate).getTime()/1000;
        Long endTime = DateUtil.endOfDay(yesterdayDate).getTime()/1000;
        Wrapper<List<Map<String, Object>>> listWrapper = oaApiClient.listAll();
        //从远程获取所有员工的微信id列表
        List<String> useridLists = new LinkedList<>();
        List<Map<String, Object>> users = listWrapper.getResult();
        for (Map<String, Object> user : users) {
            String wxUserId = MapUtil.getStr(user, "WX_USER_ID");
            if (StrUtil.isNotBlank(wxUserId)) {
                useridLists.add(wxUserId);
            }
        }
        //同步考勤（出勤+假勤）数据
        syncOADataService.syncOAClockInData(useridLists,beginTime, endTime);
        //统计考勤数据并同步缓存
        wxClockInService.countWxClockInDataGroupByYearMonth(DateUtil.format(yesterdayDate, "yyyyMM"));
    }

    /**
     * 获取考勤数据并同步缓存计算考核
     * @param beginTime
     * @param endTime
     */
    //每晚22点半更新一次
    @RequestMapping("/sync/clockin/data")
    private void syncOAClockInData(@RequestParam Long beginTime, @RequestParam Long endTime){

        //当天开始时间
        if (ObjectUtil.isNotNull(beginTime)) {
            if (ObjectUtil.isNull(endTime)) {
                endTime = DateUtil.offsetDay(new Date(beginTime), 31).getTime()/1000;
            }
        }

        //当天结束时间
        if (ObjectUtil.isNotNull(endTime)) {
            if (ObjectUtil.isNull(beginTime)) {
                beginTime = DateUtil.offsetDay(new Date(endTime),-31).getTime()/1000;
            }
        }

        //从远程获取所有员工的微信id列表
        Wrapper<List<Map<String, Object>>> listWrapper = oaApiClient.listAll();
        //从远程获取所有员工的微信id列表
        List<Map<String, Object>> users = listWrapper.getResult();
        List<String> useridLists = new LinkedList<>();
        for (Map<String, Object> user : users) {
            String wxUserId = MapUtil.getStr(user, "WX_USER_ID");
            if (StrUtil.isNotBlank(wxUserId)) {
                useridLists.add(wxUserId);
            }
        }

        //同步考勤（出勤+假勤）数据
        syncOADataService.syncOAClockInData(useridLists,beginTime, endTime);
        //统计考勤数据并同步缓存
        wxClockInService.countWxClockInDataAll();

    }

    /**
     * 获取假勤（已审批）数据
     */
    //每晚22点更新一次
    @Scheduled(cron = "0 0 22 * * ?")
    private void syncOAApproveData(){
        DateTime dateNow = DateUtil.date();
        //当天开始时间
        Long beginTime = DateUtil.beginOfDay(dateNow).getTime()/1000;
//       //当天结束时间
        Long endTime = DateUtil.endOfDay(dateNow).getTime()/1000;
        syncOADataService.syncOAApproveData(beginTime, endTime);
    }

    @RequestMapping("/sync/approve/data")
    private void syncOAApproveDataApi(@RequestParam Long beginTime, @RequestParam Long endTime){
        //当天开始时间
        if (ObjectUtil.isNotNull(beginTime)) {
            if (ObjectUtil.isNull(endTime)) {
                endTime = DateUtil.offsetDay(new Date(beginTime), 31).getTime()/1000;
            }
        }
       //当天结束时间
        if (ObjectUtil.isNotNull(endTime)) {
            if (ObjectUtil.isNull(beginTime)) {
                beginTime = DateUtil.offsetDay(new Date(endTime),-31).getTime()/1000;
            }
        }
        syncOADataService.syncOAApproveData(beginTime, endTime);
    }

    /**
     * 获取假勤（已撤销）数据
     */
    //每晚22点更新一次
    @Scheduled(cron = "0 0 22 * * ?")
    private void revokeOAApproveData(){
        DateTime dateNow = DateUtil.date();
        //当天开始时间
        Long beginTime = DateUtil.beginOfDay(dateNow).getTime()/1000;
//       //当天结束时间
        Long endTime = DateUtil.endOfDay(dateNow).getTime()/1000;

        syncOADataService.revokeOAApproveData(beginTime, endTime);
    }

    @RequestMapping("/sync/approve/revoke")
    private void revokeOAApproveDataApi(@RequestParam Long beginTime, @RequestParam Long endTime){
        //当天开始时间
        if (ObjectUtil.isNotNull(beginTime)) {
            if (ObjectUtil.isNull(endTime)) {
                endTime = DateUtil.offsetDay(new Date(beginTime), 31).getTime()/1000;
            }
        }
       //当天结束时间
        if (ObjectUtil.isNotNull(endTime)) {
            if (ObjectUtil.isNull(beginTime)) {
                beginTime = DateUtil.offsetDay(new Date(endTime),-31).getTime()/1000;
            }
        }

        syncOADataService.revokeOAApproveData(beginTime, endTime);
    }

}
