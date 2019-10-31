package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.MessageSelfResp;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class MessageSelfRespHandler extends SimpleChannelInboundHandler<MessageSelfResp> {
    private MessageSelfRespHandler() {}
    public static MessageSelfRespHandler INSTANCE = new MessageSelfRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageSelfResp msg) throws Exception {
        if (msg.getReceiverId() != null && !"".equals(msg.getReceiverId())) {

            System.out.println("给 " + msg.getReceiverId() + " 发送消息失败， 原因为：" + msg.getReason());
            return;
        }

        if (msg.getGroupId()!= null && !"".equals(msg.getGroupId())) {
            System.out.println("给群 " + msg.getGroupId() + " 发送消息失败， 原因为：" + msg.getReason());
        }
    }
}
