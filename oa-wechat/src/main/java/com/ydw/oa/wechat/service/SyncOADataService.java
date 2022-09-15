package com.ydw.oa.wechat.service;


import java.util.List;

public interface SyncOADataService {

    boolean syncOAApproveData(Long beginTime, Long endTime);

    boolean syncOAClockInData(List<String> useridLists, Long beginTime, Long endTime);

    boolean revokeOAApproveData(Long beginTime, Long endTime);
}
