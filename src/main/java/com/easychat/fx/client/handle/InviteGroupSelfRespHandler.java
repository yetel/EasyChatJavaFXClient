package com.easychat.fx.client.handle;

import com.easychat.fx.controller.Cache;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.InviteGroupSelfResp;
import com.easychat.fx.util.DateUtils;
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
public class InviteGroupSelfRespHandler extends SimpleChannelInboundHandler<InviteGroupSelfResp> {
    private InviteGroupSelfRespHandler() {}
    public static InviteGroupSelfRespHandler INSTANCE = new InviteGroupSelfRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, InviteGroupSelfResp msg) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(DateUtils.convertShowTime(msg.getDateTime()) + ":" + "\n");
        System.out.println("邀请好友入群id: " + msg.getGroupId() + " 名称：" + msg.getGroupName() + " 已经发出");
        sb.append("邀请好友入群id: " + msg.getGroupId() + " 名称：" + msg.getGroupName() + " 已经发出!\n");
        if (msg.getSuccessUser() != null && msg.getSuccessUser().size() != 0) {
            System.out.print("成功接收的邀请的好友有 ：");
            sb.append("成功接收的邀请的好友有 ：");
            for (String user : msg.getSuccessUser()) {
                System.out.print(user + " ");
                sb.append(user +"\t");
            }
            System.out.println();
            sb.append("\n");
        }

        if (msg.getFailedUser() != null && msg.getFailedUser().size() != 0) {
            System.out.print("发送邀请失败的用户有 ：");
            sb.append("发送邀请失败的用户有 ：");
            for (String user : msg.getFailedUser()) {
                System.out.print(user + " ");
                sb.append(user +"\t");
            }
            System.out.println();
            sb.append("\n");
        }
        Stage inviteGroup = Cache.ControllerMap.get("inviteGroup");
        UiBaseService.INSTANCE.printMsg(inviteGroup, sb.toString());
    }
}
