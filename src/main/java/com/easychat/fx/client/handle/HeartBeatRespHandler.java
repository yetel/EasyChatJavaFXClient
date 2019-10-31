package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.HertBeatResp;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class HeartBeatRespHandler extends SimpleChannelInboundHandler<HertBeatResp> {
    private HeartBeatRespHandler() {}
    public static HeartBeatRespHandler INSTANCE = new HeartBeatRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HertBeatResp msg) throws Exception {
        //System.out.println("收到心跳返回消息, 消息指令为" + msg.getCommand());
    }
}
