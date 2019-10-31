package com.easychat.fx.bean;

import com.easychat.fx.controller.Cache;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: Zed
 * date: 2019/08/22.
 * description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String userId;
    private String userName;
    private String password;
    private String nickName;
    private String avatar;
    private String signature;
    private String token;
    private String remark;
    
    @Override
    public String toString() {
        Integer num = Cache.userMessageNumMap.get(userId);
        if (num == null || num == 0) {
            if (remark != null && !"".equals(remark)) {
                return remark;
            } else {
                return userName;
            }
        } else {
            if (remark != null && !"".equals(remark)) {
                return remark + "        " + num + "未读";
            } else {
                return userName + "        " + num + "未读";
            }
        }
    }
}
