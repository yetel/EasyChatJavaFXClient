package com.easychat.fx.controller;

import com.easychat.fx.bean.Group;
import com.easychat.fx.bean.User;
import com.easychat.fx.client.Client;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.support.request.InviteGroupReq;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class InviteGroup extends AbstractController {
    @FXML
    private ChoiceBox choiceBox;
    @FXML
    private ListView selectView;
    @FXML
    private ListView confirmView;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void commit() {
        InviteGroupReq req = new InviteGroupReq();
        int selectedIndex = choiceBox.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            errorMsg.setText("请选择群聊");
            return;
        }
        Group group = (Group) choiceBox.getItems().get(selectedIndex);
        req.setGroupId(group.getGroupId());
        req.setDateTime(DateUtils.now());
        ObservableList items = confirmView.getItems();
        List<String> users = new ArrayList<>();
        for (Object obj : items) {
            User user = (User) obj;
            users.add(user.getUserId());
        }
        req.setUsers(users);

        Client.channelCache.writeAndFlush(req);
    }

    public void selectViewClicked() {
        int selectedIndex = selectView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        Object o = selectView.getItems().get(selectedIndex);
        ObservableList items = confirmView.getItems();
        if (!items.contains(o)) {
            confirmView.getItems().add(o);
        }
    }

    public void confirmViewClicked() {
        int selectedIndex = confirmView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        confirmView.getItems().remove(selectedIndex);
    }
}
