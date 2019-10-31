package com.easychat.fx.support.response;

import com.easychat.fx.bean.Group;
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
public class AcceptGroupResp extends Packet {
    private String acceptUser;
    /** 添加群邀请方*/
    private String groupId;
    /** 是否同意*/
    private boolean success;
    @Override
    public byte getCommand() {
        return Command.ACCEPT_GROUP_RESP;
    }
    
    @Override
    public String toString() {
        User user = Cache.cacheUserMap.get(acceptUser);
        String name = user == null ? acceptUser : user.getUserName();
        Group group = Cache.cacheGroupMap.get(groupId);
        String groupName = group == null ? groupId : group.getGroupName();
        
        if (success) {
            return DateUtils.convertShowTime(dateTime) + ":" + name + "接受了加入群聊 ：" + groupName + "的邀请";
        } else {
            return DateUtils.convertShowTime(dateTime) + ":" + name + "拒绝了加入群聊 ：" + groupName + "的邀请";
        }
    }
    
}
