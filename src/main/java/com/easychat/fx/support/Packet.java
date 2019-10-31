package com.easychat.fx.support;

import lombok.Data;

/**
 * @author Zed
 * date: 2019/08/19.
 * description:
 */
@Data
public abstract class Packet {
    private transient byte version = 1;
    /** 时间*/
    protected String dateTime;
    
    public abstract byte getCommand();
}
