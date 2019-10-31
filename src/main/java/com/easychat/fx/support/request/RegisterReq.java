package com.easychat.fx.support.request;

import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class RegisterReq extends Packet {
    /** 账号*/
    private String userName;
    /** 密码*/
    private String password;
    private String nickName;
    @Override
    public byte getCommand() {
        return Command.REGISTER_REQ;
    }
}
