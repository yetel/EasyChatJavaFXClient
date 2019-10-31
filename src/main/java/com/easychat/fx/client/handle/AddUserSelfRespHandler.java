package com.easychat.fx.client.handle;

import com.easychat.fx.controller.Cache;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.AddUserSelfResp;
import com.easychat.fx.util.ShowUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.stage.Stage;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class AddUserSelfRespHandler extends SimpleChannelInboundHandler<AddUserSelfResp> {
    private AddUserSelfRespHandler() {}
    public static AddUserSelfRespHandler INSTANCE = new AddUserSelfRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AddUserSelfResp msg) throws Exception {
        ShowUtil.addSystemMessage(msg);
        if (msg.isSuccess()) {
            System.out.println("您添加好友id: " + msg.getInvitedId() + " 的邀请已经发出！");
            Stage addUser = Cache.ControllerMap.get("addUser");
            UiBaseService.INSTANCE.printMsg(addUser, "您添加好友id: " + msg.getInvitedId() + " 的邀请已经发出！");
        } else {
            Stage addUser = Cache.ControllerMap.get("addUser");
            UiBaseService.INSTANCE.printErrorMsg(addUser, msg.getReason());
            System.out.println(msg.getReason());
        }
    }
}
