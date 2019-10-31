package com.easychat.fx.support.response;

import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

import java.util.List;

/**
 * @author Zed
 * date: 2019/08/19.
 * description: 服务端发送给被邀请方的响应
 */
@Data
public class InviteGroupSelfResp extends Packet {
    /** 发出邀请成功用户*/
    private List<String> successUser;
    private List<String> failedUser;
    /** 群名*/
    private String groupName;
    /** 群Id*/
    private String groupId;

    @Override
    public byte getCommand() {
        return Command.INVITE_GROUP_SELF_RESP;
    }
}
