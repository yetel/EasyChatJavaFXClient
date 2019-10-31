package com.easychat.fx.controller;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.Group;
import com.easychat.fx.bean.MessageCache;
import com.easychat.fx.bean.User;
import com.easychat.fx.client.Client;
import com.easychat.fx.util.AES;
import com.easychat.fx.util.DateUtils;
import com.easychat.fx.service.ImageService;
import com.easychat.fx.service.MessageService;
import com.easychat.fx.service.UserService;
import com.easychat.fx.support.Packet;
import com.easychat.fx.support.UiBaseService;
import com.easychat.fx.support.request.GroupMessageReq;
import com.easychat.fx.support.request.MessageReq;
import com.easychat.fx.support.response.AddUserResp;
import com.easychat.fx.support.response.GroupMessageResp;
import com.easychat.fx.support.response.InviteGroupResp;
import com.easychat.fx.support.response.MessageResp;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.Clipboard;
import javafx.scene.input.DataFormat;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class Main extends AbstractController {
    private static KeyCode inputLast = null;
    private static KeyCode pasteLast = null;
    private static KeyCode pasteLast1 = null;
    private static KeyCode inputLast2 = null;
    @FXML
    private ListView userListView;
    @FXML
    private ListView<Object> textArea;
    @FXML
    private Button sendMessage;
    @FXML
    private ListView<Object> messageListView;
    @FXML
    private Label label;
    @FXML
    private Button userList;
    @FXML
    private Button groupList;
    @FXML
    private Button addUser;
    @FXML
    private Button createGroup;
    @FXML
    private Button inviteGroup;
    @FXML
    private ListView groupMember;
    @FXML
    private Button message;
    @FXML
    private Label remind;
    @FXML
    private Label remind2;
    @FXML
    private Label messageNum;
    private TextArea textArea1;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorMsg.setTextFill(Color.RED);
        errorMsg.setAlignment(Pos.CENTER);
        messageNum.setTextFill(Color.RED);
        messageNum.setAlignment(Pos.BASELINE_LEFT);
        remind.setText("Enter转行");
        remind.setTextFill(Color.RED);
        remind.setAlignment(Pos.CENTER);
        remind2.setText("Ctrl+Enter");
        remind2.setTextFill(Color.RED);
        remind2.setAlignment(Pos.CENTER);
        ObservableList items = textArea.getItems();
        String property = System.getProperty("os.name");
        boolean isMac = (property.contains("Mac"));

        textArea1 = new TextArea();
        items.add(textArea1);
        addListen();


        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode last = isMac ? KeyCode.COMMAND : KeyCode.CONTROL;
            if (pasteLast == last && KeyCode.V == event.getCode()) {
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                Object content = systemClipboard.getContent(DataFormat.IMAGE);
                if (content == null) {
                    content = systemClipboard.getContent(DataFormat.PLAIN_TEXT);
                }
                if(content instanceof String){
                    items.clear();
                    TextArea textArea = new TextArea();
                    textArea.setText((String) content);
                    items.add(textArea);
                }
                //检查文本内容是否为文本内容
                if(content instanceof Image){
                    Image image=(Image) content;
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    items.clear();
                    items.add(imageView);
                }
            }
            pasteLast = event.getCode();
        });

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (textArea.getItems().get(0) instanceof TextArea) {
                return;
            }
            KeyCode last = isMac ? KeyCode.COMMAND : KeyCode.CONTROL;
            if (KeyCode.V == event.getCode() && inputLast2 == last) {
                return;
            }
            if (KeyCode.ENTER == event.getCode() ||( KeyCode.A.impl_getCode() <= event.getCode().impl_getCode() && KeyCode.Z.impl_getCode() >= event.getCode().impl_getCode())) {
                ImageView imageView = (ImageView) textArea.getItems().get(0);
                if (imageView.getImage() == null) {
                    items.clear();
                    TextArea textArea = new TextArea();
                    items.add(textArea);
                    textArea.requestFocus();
                }

            }
            inputLast2 = event.getCode();
        });

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (inputLast == KeyCode.CONTROL && KeyCode.ENTER == event.getCode()) {
                sendMessage();
            }
            inputLast = event.getCode();
        });

        textArea.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (KeyCode.BACK_SPACE == event.getCode()) {
                Object o = items.get(0);
                if (o instanceof TextArea) {
                    return;
                }

                ImageView o1 = (ImageView) o;
                o1.setImage(null);

            }
        });
    }

    private void addListen() {
        ObservableList<Object> items = textArea.getItems();
        String property = System.getProperty("os.name");
        boolean isMac = (property.contains("Mac"));
        textArea1.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            KeyCode last = isMac ? KeyCode.COMMAND : KeyCode.CONTROL;
            if (pasteLast1 == last && KeyCode.V == event.getCode()) {
                Clipboard systemClipboard = Clipboard.getSystemClipboard();
                Object content = systemClipboard.getContent(DataFormat.IMAGE);
                if (content == null) {
                    content = systemClipboard.getContent(DataFormat.PLAIN_TEXT);
                }
                //检查文本内容是否为文本内容
                if(content instanceof Image){
                    Image image=(Image) content;
                    ImageView imageView = new ImageView();
                    imageView.setImage(image);
                    items.clear();
                    items.add(imageView);
                }
            }
            pasteLast1 = event.getCode();
        });
    }

    public void sendMessage() {
        errorMsg.setText("");
        ObservableList<Object> items = textArea.getItems();
        if (items == null || items.size() == 0) {
            return;
        }

        String text;
        Object content = items.get(0);
        int inputMessageType = Constants.STRING_TYPE;
        if (content instanceof ImageView) {
            inputMessageType = Constants.IMAGE_TYPE;
            Image image = ((ImageView) content).getImage();

            String imageBase64 = ImageService.getInstance().getImageBase64(image);
            text = ImageService.getInstance().uploadImage(imageBase64, ".jpg");
        } else {
            text = ((TextArea) content).getText();
        }

        if (text == null || "".equals(text)) {
            return;
        }
        if (text.trim().equals("")) {
            return;
        }
        String now = DateUtils.now();

        //messageitems.add(Cache.currentUser.getUserName() + " " + DateUtils.convertShowTime(now) + ": \n    " + text);



        //将消息发送出去记录到缓存
        String messageType = Cache.messageCache.getMessageType();
        Packet packet;
        if (Constants.message_type_user.equals(messageType)) {
            User messageUser = Cache.messageCache.getMessageUser();
            if (messageUser == null) {
                textArea.getItems().clear();
                textArea1 = new TextArea();
                items.add(textArea1);
                addListen();
                return;
            }
            MessageReq req = new MessageReq();
            req.setReceiver(messageUser.getUserId());
            req.setMessage(AES.encrypt(text, now + "ab"));
            req.setMessageType(inputMessageType);
            req.setDateTime(now);
            addItems(req, messageListView, inputMessageType, req.getMessage(), Cache.currentUser.getUserName());
            Client.channelCache.writeAndFlush(req);
            LinkedList<Packet> packets = Cache.userMessageMap.computeIfAbsent(messageUser.getUserId(), k -> new LinkedList<>());
            packets.add(req);
            MessageService.DEFAULT.writeUserMessage(messageUser.getUserId(), req);
            
        } else {
            Group group = Cache.messageCache.getMessageGroup();
            if (group == null) {
                textArea.getItems().clear();
                textArea1 = new TextArea();
                items.add(textArea1);
                addListen();
                return;
            }
            GroupMessageReq req = new GroupMessageReq();
            req.setGroupId(group.getGroupId());
            req.setMessage(AES.encrypt(text, now + "ab"));
            req.setMessageType(inputMessageType);
            req.setDateTime(now);
            addItems(req, messageListView, inputMessageType, req.getMessage(), Cache.currentUser.getUserName());
            Client.channelCache.writeAndFlush(req);
            LinkedList<Packet> packets = Cache.groupMessageMap.computeIfAbsent(group.getGroupId(), k -> new LinkedList<>());
            packets.add(req);
            MessageService.DEFAULT.writeGroupMessage(group.getGroupId(), req);
        }


        
        
        textArea.getItems().clear();
        textArea1 = new TextArea();
        items.add(textArea1);
        textArea1.requestFocus();
        addListen();

    }

    public void showUserList() {
        errorMsg.setText("");
        showUserGroupList(Constants.message_type_user);
    }

    public void showGroupList() {
        errorMsg.setText("");
        showUserGroupList(Constants.message_type_group);
    }

    public void showMessage() {
        errorMsg.setText("");
        Set<String> userIds = Cache.userMessageMap.keySet();
        Set<String> groupIds = Cache.groupMessageMap.keySet();
        Map<String, Object> userGroupMap = new HashMap<>();
        for (String userId : userIds) {
            Packet last = Cache.userMessageMap.get(userId).getLast();
            if (last != null && last.getDateTime() != null) {
                User user = Cache.cacheUserMap.get(userId);
                if (user == null && Cache.system.getUserId().equals(userId)) {
                    user = Cache.system;
                }
                userGroupMap.put(last.getDateTime(), user);
            }
        }

        for (String groupId : groupIds) {
            Packet last = Cache.groupMessageMap.get(groupId).getLast();
            if (last != null && last.getDateTime() != null) {
                Group group = Cache.cacheGroupMap.get(groupId);
                userGroupMap.put(last.getDateTime(), group);
            }
        }

        List<Object> userGroups = userGroupMap.keySet().stream().sorted(String::compareTo).map(userGroupMap::get).collect(Collectors.toList());
        ObservableList userListViewItems = userListView.getItems();
        userListViewItems.clear();
        Collections.reverse(userGroups);
        userListViewItems.addAll(userGroups);
    }

    public static void showUserGroupList(String messageType) {
        showUserGroupList(messageType, false);
    }

    public static void showUserGroupList(String messageType, boolean newData) {
        
        UiBaseService.INSTANCE.runTaskInFxThread(()->{
            Stage main = Cache.ControllerMap.get("main");
            ListView listView = (ListView) main.getScene().getRoot().lookup("#userListView");
            int num = Cache.allMessageNum.get();
            Button message = (Button) main.getScene().getRoot().lookup("#message");
            if (num > 0) {
                if (num > 99) {
                    num = 99;
                }
                message.setText("消息 "+num);
                message.setTextFill(Color.RED);
                message.setAlignment(Pos.BASELINE_LEFT);
            } else if (num == 0) {
                message.setTextFill(Color.BLACK);
                message.setText("消息");
            } else {
                message.setTextFill(Color.BLACK);
                message.setText("消息");
                Cache.allMessageNum.set(0);
            }
            
            if (Constants.message_type_user.equals(messageType)) {
                String userId = Cache.currentUser.getUserId();
                List<User> users = UserService.getInstance().getFriendByUserId(userId, newData);
                ObservableList userList = listView.getItems();
                userList.clear();
                if (users != null) {
                    userList.addAll(users);
                }
            } else if (Constants.message_type_group.equals(messageType)){
                String userId = Cache.currentUser.getUserId();
                List<Group> groups = UserService.getInstance().getGroupByUserId(userId, newData);
                ObservableList groupList = listView.getItems();
                groupList.clear();
                if (groups != null) {
                    groupList.addAll(groups);
                }
            }
        });
        
    }

    public void addUser() {
        errorMsg.setText("");
        UiBaseService.INSTANCE.runTaskInFxThread(()->{
            Stage addUser = Cache.ControllerMap.get("addUser");
            addUser.show();
            addUser.toFront();
        });
    }

    public void createGroup() {
        errorMsg.setText("");
        UiBaseService.INSTANCE.runTaskInFxThread(()->{
            Stage createGroup = Cache.ControllerMap.get("createGroup");
            ListView selectView = (ListView) createGroup.getScene().getRoot().lookup("#selectView");
            List<User> users = UserService.getInstance().getFriendByUserId(Cache.currentUser.getUserId());
            selectView.getItems().clear();
            selectView.getItems().addAll(users);
            createGroup.show();
            createGroup.toFront();
        });
    }

    public void inviteGroup() {
        errorMsg.setText("");
        UiBaseService.INSTANCE.runTaskInFxThread(()->{
            Stage inviteGroup = Cache.ControllerMap.get("inviteGroup");
            ListView selectView = (ListView) inviteGroup.getScene().getRoot().lookup("#selectView");
            List<User> users = UserService.getInstance().getFriendByUserId(Cache.currentUser.getUserId());
            selectView.getItems().clear();
            if (users != null) {
                selectView.getItems().addAll(users);
            }

            ChoiceBox choiceBox = (ChoiceBox)inviteGroup.getScene().getRoot().lookup("#choiceBox");
            List<Group> groups = UserService.getInstance().getGroupByUserId(Cache.currentUser.getUserId());
            choiceBox.getItems().clear();
            if (groups != null) {
                choiceBox.getItems().addAll(groups);
            }
            inviteGroup.show();
            inviteGroup.toFront();
        });
    }
    
    public void listViewClicked() {
        errorMsg.setText("");
        int selectedIndex = userListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        Object o = userListView.getItems().get(selectedIndex);
        String text;
        if (o instanceof User) {
            User user = (User) o;
            text = user.getUserName();
            Cache.messageCache.setMessageType(Constants.message_type_user);
            Cache.messageCache.setMessageUser(user);
            Integer num = Cache.userMessageNumMap.get(user.getUserId());
            if (num != null) {
                Cache.allMessageNum.getAndAdd(-num);
            }
            Cache.userMessageNumMap.remove(user.getUserId());
            showMessageViesList(messageListView, user.getUserId(), Constants.message_type_user);
            showUserGroupList(Constants.message_type_user);
        } else if (o instanceof Group){
            Group group = (Group) o; 
            text = group.getGroupName();
            Cache.messageCache.setMessageType(Constants.message_type_group);
            Cache.messageCache.setMessageGroup(group);
            Integer num = Cache.groupMessageNumMap.get(group.getGroupId());
            if (num != null) {
                Cache.allMessageNum.getAndAdd(-num);
            }
            Cache.groupMessageNumMap.remove(group.getGroupId());
            showMessageViesList(messageListView, group.getGroupId(), Constants.message_type_group);
            showUserGroupList(Constants.message_type_group);

        } else {
            text = "某某";
        }
        label.setText("与" + text + "聊天中");
        System.out.println("与" + text + "聊天中");
    }
    
    public void messagViewClicked() {
        errorMsg.setText("");
        int selectedIndex = messageListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return;
        }
        
        if (Constants.message_type_group.equals(Cache.messageCache.getMessageType())) {
            return;
        }
        
        if (Cache.system.getUserId().equals(Cache.messageCache.getMessageUser().getUserId())) {
            Object packet = messageListView.getItems().get(selectedIndex);
            if (packet instanceof AddUserResp) {
                AddUserResp userResp = (AddUserResp) packet;
                Cache.acceptCache.setSenderId(userResp.getInviterId());
                Cache.acceptCache.setDateTime(userResp.getDateTime());
            } else if (packet instanceof InviteGroupResp) {
                InviteGroupResp inviteGroupResp = (InviteGroupResp) packet;
                Cache.acceptCache.setSenderId(inviteGroupResp.getInviteId());
                Cache.acceptCache.setDateTime(inviteGroupResp.getDateTime());
                Cache.acceptCache.setGroupId(inviteGroupResp.getGroupId());
            } else {
                return;
            }

            UiBaseService.INSTANCE.runTaskInFxThread(()->{
                Stage accept = Cache.ControllerMap.get("accept");
                accept.show();
                accept.toFront();
            });
            
        }
    }
    
    public static void showMessageViesList(ListView<Object> messageListView, String id, String messageType) {
        LinkedList<Packet> packets = new LinkedList<>();
        if (Constants.message_type_user.equals(messageType)) {
            LinkedList<Packet> packets1 = Cache.userMessageMap.get(id);
            if (packets1 != null) {
                packets.addAll(packets1);
            }
            if (packets.size() == 0) {
                packets.addAll(MessageService.DEFAULT.getLastUserCacheMessage(id, Constants.CACHE_PAGESIZE));
                if (packets.size() != 0) {
                    Cache.userMessageMap.put(id, packets);
                }
                
            }
        } else {
            LinkedList<Packet> packets1 = Cache.groupMessageMap.get(id);
            if (packets1 != null) {
                packets.addAll(packets1);
            }
            if (packets.size() == 0) {
                packets.addAll(MessageService.DEFAULT.getLastUserCacheMessage(id, Constants.CACHE_PAGESIZE));
                if (packets.size() != 0) {
                    Cache.groupMessageMap.put(id, packets);
                }
            }
        }
        
        UiBaseService.INSTANCE.runTaskInFxThread(()->{
            messageListView.getItems().clear();
            if (packets.size()!=0) {
                packets.sort(Comparator.comparing(Packet::getDateTime));
                for (Packet packet : packets) {
                    if (packet instanceof MessageReq ) {
                        MessageReq msg = (MessageReq) packet;
                        String userName = Cache.currentUser.getUserName();
                        addItems(msg, messageListView, msg.getMessageType(), msg.getMessage(), userName);
                    } else if (packet instanceof MessageResp) {
                        MessageResp msg = (MessageResp) packet;
                        addItems(msg, messageListView, msg.getMessageType(), msg.getMessage(), msg.getSenderName());
                    } else if (packet instanceof GroupMessageReq) {
                        GroupMessageReq msg = (GroupMessageReq) packet;
                        String userName = Cache.currentUser.getUserName();
                        addItems(msg, messageListView, msg.getMessageType(), msg.getMessage(), userName);
                    } else if (packet instanceof GroupMessageResp) {
                        GroupMessageResp msg = (GroupMessageResp) packet;
                        addItems(msg, messageListView, msg.getMessageType(), msg.getMessage(), msg.getSenderName());
                    } else {
                        messageListView.getItems().add(packet);
                    }
                }
            }
        });
        
    }

    public static void addItems(Packet msg, ListView<Object> messageListView, int msgType, String message, String userName) {
        ObservableList<Object> items = messageListView.getItems();
        if (msgType == Constants.IMAGE_TYPE) {
            ImageView imageView = new ImageView();
            String url = AES.decrypt(message, msg.getDateTime() + "ab");
            imageView.setImage(ImageService.getInstance().getImageByUrl(url));
            items.add(userName + " " + DateUtils.convertShowTime(msg.getDateTime()) + ": \n");
            items.add(imageView);
        } else {
            items.add(msg);
        }
        
        messageListView.scrollTo(items.size()-1);
        
        
    }

    public void clear() {
        errorMsg.setText("");
        messageListView.getItems().clear();
    }

    public void enter() {
        errorMsg.setText("");
        if (textArea.isFocused()) {
            sendMessage();
        }
    }

    public void messageList() throws Exception{
        errorMsg.setText("");
        MessageCache messageCache = Cache.messageCache;
        String messageType = messageCache.getMessageType();
        if (messageType == null) {
            errorMsg.setText("请选择用户!");
            return;
        }
        Stage messageList = new Stage();
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("messageList.fxml"));
        messageList.setTitle("消息记录");
        messageList.setScene(new Scene(root));
        messageList.show();
    }

}