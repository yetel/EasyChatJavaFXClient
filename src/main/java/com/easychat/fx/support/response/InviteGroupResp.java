package com.easychat.fx.support.response;

import com.easychat.fx.bean.User;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.service.UserService;
import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description: 服务端发送给被邀请方的响应
 */
@Data
public class InviteGroupResp extends Packet {
    /** 群名*/
    private String groupName;
    /** 群Id*/
    private String groupId;
    /** 邀请方id*/
    private String inviteId;
    @Override
    public byte getCommand() {
        return Command.INVITE_GROUP_RESP;
    }
    @Override
    public String toString() {
        User user = Cache.cacheUserMap.get(inviteId);
        if (user == null) {
            user = UserService.getInstance().getUserById(inviteId);
        }
        String userName = user == null ?inviteId : user.getUserName();
        
        return DateUtils.convertShowTime(dateTime) + ":" + userName + "邀请你加入群聊：" + groupName;
    }
}
