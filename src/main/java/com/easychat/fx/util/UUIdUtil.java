package com.easychat.fx.util;

import java.util.UUID;

/**
 * @author Zed
 * date: 2019/08/20.
 * description:
 */
public class UUIdUtil {
    
    public static String getUUid() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
