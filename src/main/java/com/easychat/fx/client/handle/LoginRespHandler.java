package com.easychat.fx.client.handle;

import com.easychat.fx.bean.User;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.service.UserService;
import com.easychat.fx.support.Session;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.response.LoginResp;
import com.easychat.fx.util.SessionUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.util.List;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
@ChannelHandler.Sharable
public class LoginRespHandler extends SimpleChannelInboundHandler<LoginResp> {
    private LoginRespHandler() {}
    public static LoginRespHandler INSTANCE = new LoginRespHandler();
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResp msg) throws Exception {
        if (msg.isSuccess()) {
            System.out.println("恭喜你登录成功了, 你的userId：" + msg.getUserId());
            SessionUtil.bindSession(new Session(msg.getUserId(), msg.getUserName()), ctx.channel());
            Stage login = Cache.ControllerMap.get("login");
            UiBaseService.INSTANCE.runTaskInFxThread(()->{
                login.close();
                Stage main = Cache.ControllerMap.get("main");
                Cache.currentUser.setUserId(msg.getUserId());
                Cache.currentUser.setUserName(msg.getUserName());
                Cache.currentUser.setToken(msg.getToken());
                ListView userListView = (ListView)main.getScene().getRoot().lookup("#userListView");
                List<User> users = UserService.getInstance().getFriendByUserId(msg.getUserId());
                ObservableList userList = userListView.getItems();
                userList.clear();
                if (users != null) {
                    userList.addAll(users);
                }
                main.setTitle("撩骚app       " + msg.getUserName() + " --在线， id: " + msg.getUserId());
                main.show();
            });

        } else {
            Stage login = Cache.ControllerMap.get("login");
            UiBaseService.INSTANCE.printErrorMsg(login, msg.getReason());
        }
    }
}
