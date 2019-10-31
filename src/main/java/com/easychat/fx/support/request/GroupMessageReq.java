package com.easychat.fx.support.request;

import com.easychat.fx.controller.Cache;
import com.easychat.fx.util.AES;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class GroupMessageReq extends Packet {
    /** 消息接收方*/
    private String groupId;
    /** 消息内容*/
    private String message;
    /**
     * 消息类型
     */
    private int messageType = 0;
    @Override
    public byte getCommand() {
        return Command.GROUP_MESSAGE_REQ;
    }
    @Override
    public String toString(){
        return Cache.currentUser.getUserName() + " " + DateUtils.convertShowTime(dateTime) + ": \n    " + AES.decrypt(message, dateTime+"ab");
    }
}
