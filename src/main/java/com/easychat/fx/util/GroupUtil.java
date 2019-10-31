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
public class GroupUtil {
    private static Map<String, List<String>> groupMemberMap = new ConcurrentHashMap<>();
    private static Map<String, List<String>> invitegroupMemberMap = new ConcurrentHashMap<>();
    private static Map<String, String> groupNameMap = new ConcurrentHashMap<>();
    
    public static String addGroup(String groupName) {
        String groupId = UUIdUtil.getUUid();
        List<String> group = new Vector<>();
        List<String> inviteGroup = new Vector<>();
        groupMemberMap.put(groupId, group);
        invitegroupMemberMap.put(groupId, inviteGroup);
        groupNameMap.put(groupId, groupName);
        return groupId;
    }

    public static List<String> getMembers(String groupId) {
        return groupMemberMap.get(groupId);
    }
    
    public static String getGroupName(String groupId) {
        return groupNameMap.get(groupId);
    }
    
    public static void addmember(String groupId, String userId) {

        List<String> friendlist = groupMemberMap.get(groupId);
        friendlist.add(userId);
    }

    public static void removemember(String groupId, String userId) {
        List<String> list = groupMemberMap.get(groupId);
        if (list == null || list.size() == 0) {
            return;
        }

        list.remove(userId);
    }

    public static void removeAllmember(String groupId) {
        groupMemberMap.remove(groupId);
    }
    
    public static boolean queryHasmember(String groupId, String userId) {
        List<String> list = groupMemberMap.get(groupId);
        if (list == null || list.size() == 0) {
            return false;
        }
        
        return list.contains(userId);
    }

    public static void addInviteMember(String groupId, String userId) {

        List<String> friendlist = invitegroupMemberMap.get(groupId);
        friendlist.add(userId);
    }

    public static void removeInviteMember(String groupId, String userId) {
        List<String> list = invitegroupMemberMap.get(groupId);
        if (list == null || list.size() == 0) {
            return;
        }

        list.remove(userId);
    }

    public static void removeAllInviteMember(String groupId) {
        invitegroupMemberMap.remove(groupId);
    }

    public static boolean queryHasInviteMember(String groupId, String userId) {
        List<String> list = invitegroupMemberMap.get(groupId);
        if (list == null || list.size() == 0) {
            return false;
        }

        return list.contains(userId);
    }
}
