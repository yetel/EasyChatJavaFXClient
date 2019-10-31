package com.easychat.fx.client;

import com.easychat.fx.client.handle.*;
import com.easychat.fx.code.PacketDecoder;
import com.easychat.fx.code.PacketEncoder;
import com.easychat.fx.code.Spliter;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.service.config.ServerConf;
import com.easychat.fx.support.request.LoginReq;
import com.easychat.fx.util.DateUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
public class Client extends Application {
    public static volatile Channel channelCache = null;
    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();
        NioEventLoopGroup group = new NioEventLoopGroup();
        bootstrap
                .group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<NioSocketChannel>(){

                    @Override
                    protected void initChannel(NioSocketChannel ch) {
                        //ch.pipeline().addLast(new IMIdleStateHandler());
                        ch.pipeline().addLast(new Spliter());
                        ch.pipeline().addLast(new PacketDecoder());
                        
                        ch.pipeline().addLast(LoginRespHandler.INSTANCE);
                        ch.pipeline().addLast(MessageRespHandler.INSTANCE);
                        ch.pipeline().addLast(AcceptGroupRespHandler.INSTANCE);
                        ch.pipeline().addLast(AcceptRespHandler.INSTANCE);
                        ch.pipeline().addLast(AddUserRespHandler.INSTANCE);
                        ch.pipeline().addLast(AddUserSelfRespHandler.INSTANCE);
                        ch.pipeline().addLast(CreateGroupRespHandler.INSTANCE);
                        ch.pipeline().addLast(InviteGroupRespHandler.INSTANCE);
                        ch.pipeline().addLast(InviteGroupSelfRespHandler.INSTANCE);
                        ch.pipeline().addLast(GroupMessageRespHandler.INSTANCE);
                        ch.pipeline().addLast(RegisterRespHandler.INSTANCE);
                        ch.pipeline().addLast(UpdatePasswordRespHandler.INSTANCE);
                        ch.pipeline().addLast(MessageSelfRespHandler.INSTANCE);
                        ch.pipeline().addLast(HeartBeatTimerHandler.INSTANCE);
                        ch.pipeline().addLast(HeartBeatRespHandler.INSTANCE);

                        ch.pipeline().addLast(new PacketEncoder());
                    }
                });
        connect(bootstrap, 5);
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (channelCache == null || !channelCache.isActive() || !channelCache.isOpen() || !channelCache.isWritable()) {
                    System.out.println(channelCache.id());
                    connect(bootstrap, 1);
                }
            }
        }, 30, 30, TimeUnit.SECONDS);
        launch(args);
       
    }

    private static void reLogin(Channel channelCache) {
        LoginReq req = new LoginReq();
        req.setUserName(Cache.currentUser.getUserName());
        req.setPassword(Cache.currentUser.getPassword());
        req.setDateTime(DateUtils.now());
        System.out.println("重新登录！");
        System.out.println(channelCache.id());
        channelCache.writeAndFlush(req);
    }

    private static void connect(Bootstrap bootstrap, final int retry) {
        bootstrap.connect(ServerConf.ip, ServerConf.netty_port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("启动连接成功");
                boolean needLogin = false;
                if (channelCache != null) {
                    channelCache.close();
                    needLogin = true;
                }
                channelCache = ((ChannelFuture) future).channel();
                if (needLogin) {
                    reLogin(channelCache);
                }
                //startCommand(channel);

            } else if (retry == 0){
                System.out.println("不在重试连接");
            } else {
                int sleepSecond = 1<<retry;
                Thread.sleep(sleepSecond);
                connect(bootstrap,retry - 1);
            }
        });
    }

    @Override
    public void start(Stage accept) throws Exception {

        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("accept.fxml"));
        accept.setTitle("accept");
        accept.setScene(new Scene(root));
        Cache.ControllerMap.put("accept", accept);
        accept.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Label label = (Label)accept.getScene().getRoot().lookup("#errorMsg");
                label.setText(null);
            }
        });

        Stage addUser = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("addUser.fxml"));
        addUser.setTitle("addUser");
        addUser.setScene(new Scene(root));
        Cache.ControllerMap.put("addUser", addUser);
        addUser.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Label label = (Label)addUser.getScene().getRoot().lookup("#errorMsg");
                TextField textField = (TextField)addUser.getScene().getRoot().lookup("#textField");
                label.setText(null);
                textField.setText(null);
            }
        });

        Stage createGroup = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("createGroup.fxml"));
        createGroup.setTitle("createGroup");
        createGroup.setScene(new Scene(root));
        Cache.ControllerMap.put("createGroup", createGroup);
        createGroup.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Label label = (Label)createGroup.getScene().getRoot().lookup("#errorMsg");
                TextField textField = (TextField)createGroup.getScene().getRoot().lookup("#textField");
                ListView selectView = (ListView)createGroup.getScene().getRoot().lookup("#selectView");
                ListView confirmView = (ListView)createGroup.getScene().getRoot().lookup("#confirmView");
                
                label.setText(null);
                textField.setText(null);
                selectView.getItems().clear();
                confirmView.getItems().clear();
            }
        });

        Stage inviteGroup = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("inviteGroup.fxml"));
        inviteGroup.setTitle("inviteGroup");
        inviteGroup.setScene(new Scene(root));
        Cache.ControllerMap.put("inviteGroup", inviteGroup);
        inviteGroup.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Label label = (Label)inviteGroup.getScene().getRoot().lookup("#errorMsg");
                ChoiceBox choiceBox = (ChoiceBox)inviteGroup.getScene().getRoot().lookup("#choiceBox");
                ListView selectView = (ListView)createGroup.getScene().getRoot().lookup("#selectView");
                ListView confirmView = (ListView)createGroup.getScene().getRoot().lookup("#confirmView");
                
                label.setText(null);
                label.setText(null);
                selectView.getItems().clear();
                confirmView.getItems().clear();
                choiceBox.getItems().clear();
            }
        });

        Stage main = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("main.fxml"));
        main.setTitle("main");
        main.setScene(new Scene(root));
        Cache.ControllerMap.put("main", main);
        main.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });

        Stage updatePassword = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("updatePassword.fxml"));
        updatePassword.setTitle("updatePassword");
        updatePassword.setScene(new Scene(root));
        Cache.ControllerMap.put("updatePassword", updatePassword);

        Stage login = new Stage();
        root = FXMLLoader.load(getClass().getClassLoader().getResource("login.fxml"));
        login.setTitle("login");
        login.setScene(new Scene(root));
        Cache.ControllerMap.put("login", login);
        login.show();
        login.toFront();
        login.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }
}
