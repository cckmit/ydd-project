package com.ydw.oa.wechat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ydw.oa.wechat.entity.business.WxApprove;
import com.ydw.oa.wechat.entity.business.WxClockIn;
import com.ydw.oa.wechat.mapper.WxApproveMapper;
import com.ydw.oa.wechat.mapper.WxClockInMapper;
import com.ydw.oa.wechat.service.WxApproveService;
import com.ydw.oa.wechat.service.WxClockInService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WxApproveServiceImpl extends ServiceImpl<WxApproveMapper, WxApprove> implements WxApproveService {

    @Autowired
    private WxApproveMapper wxApproveMapper;

}
