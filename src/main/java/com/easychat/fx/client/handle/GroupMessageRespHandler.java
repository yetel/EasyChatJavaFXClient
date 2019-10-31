package com.easychat.fx.client.handle;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.MessageCache;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.controller.Main;
import com.easychat.fx.support.Packet;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.GroupMessageResp;
import com.easychat.fx.service.MessageService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class GroupMessageRespHandler extends SimpleChannelInboundHandler<GroupMessageResp> {
    private GroupMessageRespHandler() {}
    public static GroupMessageRespHandler INSTANCE = new GroupMessageRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResp msg) throws Exception {
        Stage main = Cache.ControllerMap.get("main");
        List<Packet> packets = Cache.groupMessageMap.computeIfAbsent(msg.getGroupId(), k -> new LinkedList<>());
        Integer messageNum = Cache.groupMessageNumMap.computeIfAbsent(msg.getGroupId(), k -> 0);
        packets.add(msg);
        MessageService.DEFAULT.writeGroupMessage(msg.getGroupId(), msg);
        MessageCache messageCache = Cache.messageCache;
        if (Constants.message_type_group.equals(messageCache.getMessageType()) && msg.getGroupId().equals(messageCache.getMessageGroup().getGroupId())) {
            UiBaseService.INSTANCE.runTaskInFxThread(()->{
                ListView<Object> listView = (ListView<Object>) main.getScene().getRoot().lookup("#messageListView");
                Main.addItems(msg, listView, msg.getMessageType(), msg.getMessage(), msg.getSenderName());
            });
        } else {
            Cache.groupMessageNumMap.put(msg.getGroupId(), messageNum + 1);
            Cache.allMessageNum.incrementAndGet();
        }
        UiBaseService.INSTANCE.runTaskInFxThread(()-> {
            main.toFront();
            Main.showUserGroupList(Constants.message_type_group);
        });
        System.out.println("你的群聊 id：" + msg.getGroupId() + "用户id 为：" + msg.getSenderId() + " 发来消息： " + msg.getMessage());
    }
}
