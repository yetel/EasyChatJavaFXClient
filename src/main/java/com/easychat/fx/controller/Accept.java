package com.easychat.fx.controller;

import com.easychat.fx.bean.AcceptCache;
import com.easychat.fx.bean.Constants;
import com.easychat.fx.client.Client;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.support.request.AcceptGroupReq;
import com.easychat.fx.support.request.AcceptReq;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class Accept extends AbstractController {
    @FXML
    private RadioButton yes;
    @FXML
    private RadioButton no;
    
    private ToggleGroup group = new ToggleGroup();
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        yes.setToggleGroup(group);
        no.setToggleGroup(group);
    }
    
    public void commit() {
        Toggle selectedToggle = group.getSelectedToggle();
        if (selectedToggle == null) {
            errorMsg.setText("请选择 yes or no");
            return;
        }
        AcceptCache acceptCache = Cache.acceptCache;
        if (acceptCache.getGroupId() == null || acceptCache.getGroupId().equals("")) {
            AcceptReq req = new AcceptReq();
            String senderId = acceptCache.getSenderId();
            req.setReceiver(senderId);
            req.setAccept(yes.isSelected());
            req.setDateTime(DateUtils.now());
            Client.channelCache.writeAndFlush(req);
            Main.showUserGroupList(Constants.message_type_user, true);
        } else {
            AcceptGroupReq req = new AcceptGroupReq();
            String senderId = acceptCache.getSenderId();
            req.setInviterId(senderId);
            req.setAccept(yes.isSelected());
            req.setGroupId(acceptCache.getGroupId());
            req.setDateTime(DateUtils.now());
            Client.channelCache.writeAndFlush(req);
            Main.showUserGroupList(Constants.message_type_group, true);
        }
        
        Stage accept = Cache.ControllerMap.get("accept");
        accept.close();
    }
}
