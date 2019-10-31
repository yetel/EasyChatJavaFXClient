package com.easychat.fx.util;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.MessageCache;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.controller.Main;
import com.easychat.fx.support.Packet;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.service.MessageService;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.LinkedList;

/**
 * @author: Zed
 * date: 2019/08/28.
 * description:
 */
public class ShowUtil {
    public static void addSystemMessage(Packet msg) {
        LinkedList<Packet> packets = Cache.userMessageMap.computeIfAbsent(Cache.system.getUserId(), k -> new LinkedList<>());
        Stage main = Cache.ControllerMap.get("main");
        Integer messageNum = Cache.userMessageNumMap.computeIfAbsent(Cache.system.getUserId(), k -> 0);
        packets.add(msg);
        MessageService.DEFAULT.writeUserMessage(Cache.system.getUserId(), msg);
        MessageCache messageCache = Cache.messageCache;
        if (Constants.message_type_user.equals(messageCache.getMessageType()) && messageCache.getMessageUser() != null && Cache.system.getUserId().equals(messageCache.getMessageUser().getUserId())) {
            UiBaseService.INSTANCE.runTaskInFxThread(()->{
                ListView<Object> listView = (ListView<Object>) main.getScene().getRoot().lookup("#messageListView");
                listView.getItems().add(msg);
            });
        } else {
            Cache.userMessageNumMap.put(Cache.system.getUserId(), messageNum+1);
            Cache.allMessageNum.incrementAndGet();
        }
        UiBaseService.INSTANCE.runTaskInFxThread(()-> {
            main.toFront();
            Main.showUserGroupList(Constants.message_type_user);
        });
    }
}
