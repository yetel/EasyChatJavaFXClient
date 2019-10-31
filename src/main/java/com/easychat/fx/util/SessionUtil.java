package com.easychat.fx.util;

import com.easychat.fx.support.Attributes;
import com.easychat.fx.support.Session;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
public class SessionUtil {
    private static Map<String, Channel> userChannelMap = new ConcurrentHashMap<>();
    
    public static boolean hasLogin(Channel channel) {
       return channel.attr(Attributes.SESSION).get() != null;
    }
    
    public static Channel getChannel(String userId) {
        return userChannelMap.get(userId);
    }

    public static void bindSession(Session session, Channel channel) {
        userChannelMap.put(session.getUserId(), channel);
        channel.attr(Attributes.SESSION).set(session);
    }

    public static void unbindSession(Channel channel) {
        if (hasLogin(channel)) {
            Session session = channel.attr(Attributes.SESSION).get();
            userChannelMap.remove(session.getUserId());
            channel.attr(Attributes.SESSION).set(null);
        }
    }
}
