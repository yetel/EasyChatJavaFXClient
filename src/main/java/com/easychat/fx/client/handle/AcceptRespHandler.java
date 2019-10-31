package com.easychat.fx.client.handle;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.controller.Main;
import com.easychat.fx.support.response.AcceptResp;
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
public class AcceptRespHandler extends SimpleChannelInboundHandler<AcceptResp> {
    private AcceptRespHandler() {}
    public static AcceptRespHandler INSTANCE = new AcceptRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AcceptResp msg) throws Exception {
        ShowUtil.addSystemMessage(msg);
        if (msg.isSuccess()) {
            Main.showUserGroupList(Constants.message_type_user, true);
            System.out.println("恭喜你，添加好友成功， 好友id： " + msg.getInvitedId());
        } else {
            System.out.println("对方拒绝了你的添加好友请求， id: "+ msg.getInvitedId());
        }
    }
}
