package com.easychat.fx.client.handle;

import com.easychat.fx.controller.Cache;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.RegisterResp;
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
public class RegisterRespHandler extends SimpleChannelInboundHandler<RegisterResp> {
    private RegisterRespHandler() {}
    public static RegisterRespHandler INSTANCE = new RegisterRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RegisterResp msg) throws Exception {
        Stage register = Cache.ControllerMap.get("login");
        if (msg.isSuccess()) {
            UiBaseService.INSTANCE.printMsg(register, "恭喜你注册成功了, 现在就登录吧！");
            System.out.println("恭喜你注册成功了, 现在就登录吧！");
        } else {
            UiBaseService.INSTANCE.printErrorMsg(register, "不好意思， 注册失败了， 原因为" + msg.getReason());
            System.out.println("不好意思， 注册失败了， 原因为" + msg.getReason());
        }
    }
}
