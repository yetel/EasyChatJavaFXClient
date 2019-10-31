package com.easychat.fx.client.handle;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.MessageCache;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.controller.Main;
import com.easychat.fx.support.Packet;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.MessageResp;
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
public class MessageRespHandler extends SimpleChannelInboundHandler<MessageResp> {
    private MessageRespHandler() {}
    public static MessageRespHandler INSTANCE = new MessageRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResp msg) throws Exception {
        Stage main = Cache.ControllerMap.get("main");
        System.out.println(msg);
        List<Packet> packets = Cache.userMessageMap.computeIfAbsent(msg.getSender(), k -> new LinkedList<>());
        Integer messageNum = Cache.userMessageNumMap.computeIfAbsent(msg.getSender(), k -> 0);

        packets.add(msg);
        MessageService.DEFAULT.writeUserMessage(msg.getSender(), msg);
        MessageCache messageCache = Cache.messageCache;
        if (Constants.message_type_user.equals(messageCache.getMessageType()) && messageCache.getMessageUser() != null && msg.getSender().equals(messageCache.getMessageUser().getUserId())) {
            UiBaseService.INSTANCE.runTaskInFxThread(()->{
                ListView<Object> listView = (ListView<Object>) main.getScene().getRoot().lookup("#messageListView");
                Main.addItems(msg, listView, msg.getMessageType(), msg.getMessage(), msg.getSenderName());
            });
        } else {
            Cache.userMessageNumMap.put(msg.getSender(), messageNum+1);
            Cache.allMessageNum.incrementAndGet();

        }
        UiBaseService.INSTANCE.runTaskInFxThread(()-> {
            main.toFront();
            Main.showUserGroupList(Constants.message_type_user);
        });
        System.out.println("你的好友" + msg.getSenderName() + "id 为：" + msg.getSender() + " 发来消息： " + msg.getMessage());
    }
}
