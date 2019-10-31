package com.easychat.fx.support.response;

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
public class AddUserSelfResp extends Packet {
    /** 被邀请方id*/
    private String invitedId;
    /** 不能添加的原因*/
    private String reason;
    /** 发出邀请是否成功*/
    private boolean success;
    @Override
    public byte getCommand() {
        return Command.ADD_USER_SELF_RESP;
    }
    @Override
    public String toString() {
        if (success) {
            return DateUtils.convertShowTime(dateTime) + ":" + "添加好友" + invitedId + "请求已经发出";
        } else {
            return DateUtils.convertShowTime(dateTime) + ":" + "添加好友" + invitedId + "失败, 原因是 ： " + reason;
        }
    }
}
