package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.AcceptGroupResp;
import com.easychat.fx.util.ShowUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class AcceptGroupRespHandler extends SimpleChannelInboundHandler<AcceptGroupResp> {
    private AcceptGroupRespHandler() {}
    public static AcceptGroupRespHandler INSTANCE = new AcceptGroupRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AcceptGroupResp msg) throws Exception {
        ShowUtil.addSystemMessage(msg);
        if (msg.isSuccess()) {
            System.out.println("欢迎 id: " + msg.getAcceptUser() + "加入群聊， 群id： " +msg.getGroupId());
        } else {
            System.out.println("您所邀请的好友 id: " + msg.getAcceptUser() + "拒绝了您的加入群聊邀请， 群id： " +msg.getGroupId());
        }
    }
}
