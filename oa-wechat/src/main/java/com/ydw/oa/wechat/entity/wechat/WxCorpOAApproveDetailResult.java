package com.ydw.oa.wechat.entity.wechat;

import cn.hutool.json.JSONObject;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WxCorpOAApproveDetailResult extends WxCorpResult {

    private JSONObject info;
}
