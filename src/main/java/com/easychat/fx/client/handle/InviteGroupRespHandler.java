package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.InviteGroupResp;
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
public class InviteGroupRespHandler extends SimpleChannelInboundHandler<InviteGroupResp> {
    private InviteGroupRespHandler() {}
    public static InviteGroupRespHandler INSTANCE = new InviteGroupRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InviteGroupResp msg) throws Exception {
        ShowUtil.addSystemMessage(msg);
        System.out.println("你好，用户id： " + msg.getInviteId() + " 向你发来邀请加入群聊邀请， 群id： " + msg.getGroupId() + " 群名称： " + msg.getGroupName());
    }
}
