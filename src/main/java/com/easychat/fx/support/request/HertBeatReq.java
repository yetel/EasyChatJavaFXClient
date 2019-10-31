package com.easychat.fx.support.request;


import com.easychat.fx.support.Command;
import com.easychat.fx.support.Packet;

public class HertBeatReq extends Packet {
    @Override
    public byte getCommand() {
        return Command.HEAT_BEAT_REQ;
    }
}
