package com.easychat.fx.service;

import com.easychat.fx.service.impl.FileServiceImpl;

import java.io.File;
import java.util.List;

/**
 * @author Zed
 * date: 2019/09/02.
 * description:
 */
public interface FileService {
    FileService DEFAULT = FileServiceImpl.getInstance();
    /**
     * 获取用户的本地消息文件
     * @param userId 用户id
     * @return 按照时间先后顺序返回map， key : 文件绝对路径 value ：用户的本文件的消息记录数。
     */
    List<String> getUserMessageFiles(String currentUser, String userId, String userType);

    /**
     * 给定文件写入消息记录
     * @param file 消息文件
     * @param messageLine 消息内容
     */
    void writeMessage(File file, String messageLine);

    /**
     * 给定文件读取 其中一段消息记录
     * @param file 消息文件
     * @param start 开始记录数
     * @param end 结束记录数
     * @return 消息记录
     */
    List<String> readMessage(File file, int start, int end);


    /**
     * 获取文件的行数
     * @param file 文件
     * @return
     */
    public int getLineNum(File file);
}
