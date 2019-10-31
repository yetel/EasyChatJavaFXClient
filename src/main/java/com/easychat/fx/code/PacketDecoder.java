package com.easychat.fx.code;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
public class PacketDecoder extends MessageToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, Object msg, List out) throws Exception {
        out.add(PacketCode.INSTANCE.decode((ByteBuf)msg));
    }
}
