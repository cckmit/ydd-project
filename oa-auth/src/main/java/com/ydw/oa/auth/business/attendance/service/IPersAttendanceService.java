package com.ydw.oa.auth.business.attendance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
public interface IPersAttendanceService {

    //导出考核考勤数据Excel
    void export(String yearMonth,String dataPath,String startTime,String endTime,String pubTime);
}
