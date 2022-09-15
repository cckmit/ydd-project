package com.ydw.oa.wechat.consts;

public interface WechatConfigConstant {
    String OAUTH_API_URI = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=#wechat_redirect";
    String REDIRECT_URI = "http://yun.eqbidding.com/wechat/index.html";
//    String REDIRECT_URI = "http://cwlc.sxymlc.com/wechat/index.html";
    String TEXT_CARD_BTN_TXT = "查看详情";
}
