package com.ydw.oa.auth.business.attendance.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ydw.oa.auth.business.asset.entity.AssetContent;
import com.ydw.oa.auth.business.attendance.entity.PersAttendanceLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author hxj
 * @since 2020-07-02
 */
public interface PersAttendanceLogMapper extends BaseMapper<PersAttendanceLog> {

    List<Map<String,Object>> sumAttendanceLogDataByMonth(String month);

    List<Map<String,Object>> sumAttendanceLogDataByUsrIdAndMonth(String usrId,String month);

    IPage<Map<String,Object>> pageAttendanceLogByStatuzAndUsrId(Page<PersAttendanceLog> query, String usrId);

    List<PersAttendanceLog> listAttendanceLogMonthByStatuzAndDeptId(String month, String statuz, String deptId);
}
