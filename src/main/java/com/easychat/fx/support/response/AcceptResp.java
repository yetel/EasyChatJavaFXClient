package com.easychat.fx.support.response;

import com.easychat.fx.bean.User;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class AcceptResp extends Packet {
    /** 邀请方*/
    private String inviterId;
    /** 被邀请方*/
    private String invitedId;
    /** 是否同意*/
    private boolean success;
    @Override
    public byte getCommand() {
        return Command.ACCEPT_RESP;
    }

    @Override
    public String toString() {
        User user = Cache.cacheUserMap.get(invitedId);
        String name = user == null ? invitedId : user.getUserName();
        if (success) {
            return DateUtils.convertShowTime(dateTime) + ":" + name + "接受了你的添加好友邀请";
        } else {
            return DateUtils.convertShowTime(dateTime) + ":" + name + "拒绝了你的添加好友邀请";
        }
    }
}
