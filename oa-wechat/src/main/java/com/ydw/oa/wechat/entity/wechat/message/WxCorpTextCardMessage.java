package com.ydw.oa.wechat.entity.wechat.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ydw.oa.wechat.enums.MsgSendTypeEnum;
import com.ydw.oa.wechat.enums.MsgTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class WxCorpTextCardMessage extends WxCorpMessage{

    @JsonProperty("textcard")
    private TextCard textCard;

    public WxCorpTextCardMessage(String agentid, MsgTypeEnum msgType, MsgSendTypeEnum msgSendTypeEnum, String sendTypeValue, TextCard textCard) {
        super(agentid, msgType, msgSendTypeEnum, sendTypeValue);
        this.textCard = textCard;
    }

    public WxCorpTextCardMessage(String agentid, MsgTypeEnum msgType, String touser, TextCard textCard) {
        this(agentid, msgType, touser,null,null, textCard);
    }

    public WxCorpTextCardMessage(String agentid, MsgTypeEnum msgType, String touser, String toparty, TextCard textCard) {
        this(agentid, msgType, touser,toparty,null, textCard);
    }

    public WxCorpTextCardMessage(String agentid, MsgTypeEnum msgType, String touser, String toparty, String totag, TextCard textCard) {
        super(agentid, msgType, touser, toparty, totag);
        this.textCard = textCard;
    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TextCard {
        @JsonProperty("title")
        private String title;
        @JsonProperty("description")
        private String description;
        @JsonProperty("url")
        private String url;
        @JsonProperty("btntxt")
        private String btntxt;
    }

}
