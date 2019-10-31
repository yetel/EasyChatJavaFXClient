package com.easychat.fx.client.handle;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.controller.Main;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.CreateGroupResp;
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
public class CreateGroupRespHandler extends SimpleChannelInboundHandler<CreateGroupResp> {
    private CreateGroupRespHandler() {}
    public static CreateGroupRespHandler INSTANCE = new CreateGroupRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResp msg) throws Exception {
        Stage createGroup = Cache.ControllerMap.get("createGroup");
        ShowUtil.addSystemMessage(msg);
        if (msg.isSuccess()) {
            UiBaseService.INSTANCE.printMsg(createGroup, msg.getGroupName() + " \n已经创建成功");
            Main.showUserGroupList(Constants.message_type_group, true);
            System.out.println(msg.getGroupName() + " 已经创建成功");
        } else {
            UiBaseService.INSTANCE.printMsg(createGroup, msg.getGroupName() + " \n创建失败" + msg.getReason());
            System.out.println(msg.getGroupName() + " 创建失败");
        }
    }
}
