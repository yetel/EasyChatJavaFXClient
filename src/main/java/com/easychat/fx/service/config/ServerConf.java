package com.easychat.fx.service.config;

/**
 * @author: Zed
 * date: 2019/08/26.
 * description:
 */
public interface ServerConf {
    String ip = "120.79.175.213";
    String port = "9004";
    int netty_port = 8000;
    String users_url = "http://ip:port/users";
    String groups_url = "http://ip:port/groups";

    String user_url = "http://ip:port/user/{userId}";
    String group_url = "http://ip:port/group/{groupId}";
    String upload_image_url = "http://ip:port/image/upload";

}
