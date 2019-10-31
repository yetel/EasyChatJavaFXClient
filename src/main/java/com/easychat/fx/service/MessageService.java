package com.easychat.fx.service;

import com.easychat.fx.bean.PageResult;
import com.easychat.fx.service.impl.MessageServiceImpl;
import com.easychat.fx.support.Packet;

import java.util.List;

public interface MessageService {
    MessageService DEFAULT = MessageServiceImpl.getInstance();
    /**
     * 根据用户id获取最后一页消息
     * @param userId 用户id
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getLatestUserMessage(String userId, int pageSize);

    /**
     * 根据群组id获取最后一页消息
     * @param groupId 群组id
     * @param pageSize 一页的记录数          
     * @return 返回消息记录
     */
    PageResult<Packet> getLatestGroupMessage(String groupId, int pageSize);

    /**
     * 根据用户id获取最早一页消息
     * @param userId 用户id
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getEarliestUserMessage(String userId, int pageSize);

    /**
     * 根据群组id获取最早一页消息
     * @param groupId 群组id
     * @param pageSize 一页的记录数                
     * @return 返回消息记录
     */
    PageResult<Packet> getEarliestGroupMessage(String groupId, int pageSize);

    /**
     * 根据用户id获取上一页消息
     * @param userId 用户id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getLastUserMessage(String userId, int currentPage, int pageSize);

    /**
     * 根据群组id获取上一页消息
     * @param groupId 群组id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getLastGroupMessage(String groupId, int currentPage, int pageSize);

    /**
     * 根据用户id获取下一页消息
     * @param userId 用户id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getNextUserMessage(String userId, int currentPage, int pageSize);

    /**
     * 根据群组id获取下一页消息
     * @param groupId 群组id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    PageResult<Packet> getNextGroupMessage(String groupId, int currentPage, int pageSize);

    /**
     * 写入消息到用户本地记录
     * @param userId 用户id
     * @param packet 消息体
     */
    void writeUserMessage(String userId, Packet packet);
    
    /**
     * 写入消息到群组本地记录
     * @param groupId 群组id
     * @param packet 消息体
     */
    void writeGroupMessage(String groupId, Packet packet);


    /**
     * 获取最后几条用户聊天记录
     * @param userId
     * @return
     */
    List<Packet> getLastUserCacheMessage(String userId, int pageSize);

    /**
     * 获取最后几条用户聊天记录
     * @param userId
     * @return
     */
    List<Packet> getLastGroupCacheMessage(String userId, int pageSize);
}
