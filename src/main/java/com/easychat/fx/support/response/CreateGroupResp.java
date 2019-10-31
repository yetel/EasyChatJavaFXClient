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
public class CreateGroupResp extends Packet {
    /** 创建的群名*/
    private String groupName;
    /** 新增的群id*/
    private String groupId;
    /** 是否创建群成功*/
    private boolean success;
    /** 创建失败的原因*/
    private String reason;
    @Override
    public byte getCommand() {
        return Command.CREATE_GROUP_RESP;
    }
    @Override
    public String toString() {
        if (success) {
            return DateUtils.convertShowTime(dateTime) + ":" + "恭喜你， 创建群聊：" + groupName + "成功";
        } else {
            return DateUtils.convertShowTime(dateTime) + ":" + "对不起， 创建群聊：" + groupName + "失败， 原因是 ：" + reason;
        }
    }
}
