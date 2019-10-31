package com.easychat.fx.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Description: 结果
 * Created by Zed on 2019/01/02.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    // 结果状态
    private String code;
    // 结果消息
    private String desc;
    private T data;
}
