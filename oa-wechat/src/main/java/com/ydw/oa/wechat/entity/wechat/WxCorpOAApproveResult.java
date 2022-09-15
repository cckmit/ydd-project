package com.ydw.oa.wechat.entity.wechat;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WxCorpOAApproveResult extends WxCorpResult {

    private List<String> sp_no_list;
}
