package com.easychat.fx.support.response;


import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;

public class HertBeatResp extends Packet {
    @Override
    public byte getCommand() {
        return Command.HEAT_BEAT_RESP;
    }
}
