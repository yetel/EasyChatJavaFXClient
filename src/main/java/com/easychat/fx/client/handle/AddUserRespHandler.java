package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.AddUserResp;
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
public class AddUserRespHandler extends SimpleChannelInboundHandler<AddUserResp> {
    private AddUserRespHandler() {}
    public static AddUserRespHandler INSTANCE = new AddUserRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddUserResp msg) throws Exception {
        ShowUtil.addSystemMessage(msg);
        System.out.println("id: " + msg.getInviterId() + " 用户添加你为好友， 请及时处理！");
    }
}
