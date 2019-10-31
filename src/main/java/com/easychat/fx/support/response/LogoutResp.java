package com.easychat.fx.support.response;

import com.easychat.fx.support.Packet;
import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public class LogoutResp extends Packet {
    @Override
    public byte getCommand() {
        return 0;
    }
}
