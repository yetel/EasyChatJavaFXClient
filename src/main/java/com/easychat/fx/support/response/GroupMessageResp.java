package com.easychat.fx.support.response;

import com.easychat.fx.util.AES;
import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class GroupMessageResp extends Packet {
    /** 消息发送方*/
    private String senderId;
    /** 消息发送人姓名*/
    private String senderName;
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
        return Command.GROUP_MESSAGE_RESP;
    }
    @Override
    public String toString(){
        return senderName + " " + dateTime + ": \n    " + AES.decrypt(message, dateTime+"ab");
    }
}
