package com.ydw.oa.wechat.entity.wechat;

import com.ydw.oa.wechat.entity.wechat.oa.WxCorpOAClockInInfo;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WxCorpOAClockInResult extends WxCorpResult {

    private List<WxCorpOAClockInInfo> checkindata;
}
