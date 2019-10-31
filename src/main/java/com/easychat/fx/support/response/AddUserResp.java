package com.easychat.fx.support.response;

import com.easychat.fx.bean.User;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.service.UserService;
import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class AddUserResp extends Packet {
    /** 邀请方id*/
    private String inviterId;
    @Override
    public byte getCommand() {
        return Command.ADD_USER_RESP;
    }

    @Override
    public String toString() {
        User user = UserService.getInstance().getUserById(inviterId);
        String name = user == null ? inviterId : user.getUserName();
        return DateUtils.convertShowTime(dateTime) + ":" + name + "向你发送了一条好友申请";
    }
}
