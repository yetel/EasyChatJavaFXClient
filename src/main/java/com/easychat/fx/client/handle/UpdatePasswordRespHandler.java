package com.easychat.fx.client.handle;

import com.easychat.fx.support.response.UpdatePasswdResp;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class UpdatePasswordRespHandler extends SimpleChannelInboundHandler<UpdatePasswdResp> {
    private UpdatePasswordRespHandler() {}
    public static UpdatePasswordRespHandler INSTANCE = new UpdatePasswordRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, UpdatePasswdResp resp) throws Exception {
        if (resp.isSuccess()) {
            System.out.println("恭喜你修改密码成功, 你的新密码为：" + resp.getPassword());
        } else {
            System.out.println("不好意思， 修改密码失败了， 原因为： " + resp.getReason());
        }
    }
}
