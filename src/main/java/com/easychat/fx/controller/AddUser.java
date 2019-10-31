package com.easychat.fx.controller;

import com.easychat.fx.client.Client;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.support.request.AddUserReq;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class AddUser extends AbstractController {

    @FXML
    private TextField textField;
    @FXML
    private Button add;
    @FXML
    private Button cancel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void add() {
        AddUserReq req = new AddUserReq();
        String text = textField.getText();
        req.setAddUserId(text);
        req.setDateTime(DateUtils.now());
        Client.channelCache.writeAndFlush(req);
    }

    public void cancel() {
        Cache.ControllerMap.get("addUser").close();
    }
}
