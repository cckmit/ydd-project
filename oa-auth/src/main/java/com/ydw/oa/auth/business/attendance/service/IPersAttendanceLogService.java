package com.ydw.oa.auth.business.attendance.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.baomidou.mybatisplus.extension.service.IService;

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
public interface IPersAttendanceLogService extends IService<PersAttendanceLog> {

    //自动计算考核数据
    void autoSaveAttendanceLog(String yearMonth,String day);
    //统计考核数据并保存缓存
    void countAttendanceLogDataGroupByYearMonth(String yearMonth);
    //统计考核数据并保存缓存
    void countAttendanceLogDataGroupByUsrIdAndYearMonth(String usrId,String yearMonth);
    //查询当前用户需要审批的考核数据
    IPage<Map<String,Object>> pageAttendanceLogByStatuzAndUserId(Page<PersAttendanceLog> query, String userId);
    //查询部门需要上报的考核数据
    List<PersAttendanceLog>  listAttendanceLogMonthByStatuzAndDeptId(String month, String statuz,String deptId);
}
