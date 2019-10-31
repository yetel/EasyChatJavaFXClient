package com.easychat.fx.support.response;

import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class LoginResp extends Packet {
    /** userId*/
    private String userId;
    private String userName;
    private String token;
    private boolean success;

    private String reason;
    @Override
    public byte getCommand() {
        return Command.LOGIN_RESP;
    }
}
