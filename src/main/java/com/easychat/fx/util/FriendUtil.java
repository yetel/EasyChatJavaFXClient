package com.easychat.fx.util;

import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: Zed
 * date: 2019/08/21.
 * description:
 */
public class FriendUtil {
    private static Map<String, List<String>> friendMap = new ConcurrentHashMap<>();
    private static Map<String, List<String>> invitefriendMap = new ConcurrentHashMap<>();
    
    public static void addFriend(String userId, String friendId) {
        List<String> list = friendMap.get(userId);
        if (list == null || list.size() == 0) {
            friendMap.putIfAbsent(userId, new Vector<>());
        }

        List<String> friendlist = friendMap.get(userId);
        friendlist.add(friendId);
    }

    public static void removeFriend(String userId, String friendId) {
        List<String> list = friendMap.get(userId);
        if (list == null || list.size() == 0) {
            return;
        }

        list.remove(friendId);
    }

    public static void removeAllFriend(String userId) {
        friendMap.remove(userId);
    }
    
    public static boolean queryHasFriend(String userId, String friendId) {
        List<String> list = friendMap.get(userId);
        if (list == null || list.size() == 0) {
            return false;
        }
        
        return list.contains(friendId);
    }
    
    public static List<String> getFriends(String userId) {
        return friendMap.get(userId);
    }

    public static void addInviteFriend(String userId, String friendId) {
        List<String> list = invitefriendMap.get(userId);
        if (list == null || list.size() == 0) {
            invitefriendMap.putIfAbsent(userId, new Vector<>());
        }

        List<String> inviteFriendlist = invitefriendMap.get(userId);
        inviteFriendlist.add(friendId);
    }

    public static void removeInviteFriend(String userId, String friendId) {
        List<String> list = invitefriendMap.get(userId);
        if (list == null || list.size() == 0) {
            return;
        }

        list.remove(friendId);
    }

    public static void removeAllInviteFriend(String userId) {
        invitefriendMap.remove(userId);
    }

    public static boolean queryHasInviteFriend(String userId, String friendId) {
        List<String> list = invitefriendMap.get(userId);
        if (list == null || list.size() == 0) {
            return false;
        }

        return list.contains(friendId);
    }
}
