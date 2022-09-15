package com.ydw.oa.wechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.wechat.entity.business.WxApprove;
import com.ydw.oa.wechat.entity.business.WxAttendance;
import com.ydw.oa.wechat.mapper.WxApproveMapper;
import com.ydw.oa.wechat.mapper.WxAttendanceMapper;
import com.ydw.oa.wechat.service.WxApproveService;
import com.ydw.oa.wechat.service.WxAttendanceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxAttendanceServiceImpl extends ServiceImpl<WxAttendanceMapper, WxAttendance> implements WxAttendanceService {


}
