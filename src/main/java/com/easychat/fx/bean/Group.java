package com.easychat.fx.bean;

import com.easychat.fx.controller.Cache;
import lombok.Data;

/**
 * @author: Zed
 * date: 2019/08/22.
 * description:
 */
@Data
public class Group {
    private String groupId;
    private String groupName;
    private String mainUserId;
    
    
    @Override
    public String toString() {
        Integer num = Cache.groupMessageNumMap.get(groupId);
        if (num == null || num == 0) {
            return groupName;
        } else {
            return groupName + "        " + num + "未读";
        }
    }
}
