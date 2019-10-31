package com.easychat.fx.service.impl;

import com.alibaba.fastjson.JSON;
import com.easychat.fx.bean.Constants;
import com.easychat.fx.bean.PageResult;
import com.easychat.fx.code.PacketCode;
import com.easychat.fx.controller.Cache;
import com.easychat.fx.service.FileService;
import com.easychat.fx.service.MessageService;
import com.easychat.fx.support.Packet;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author Zed
 * date: 2019/09/02.
 * description:
 */
public class MessageServiceImpl implements MessageService {
    private static MessageService ourInstance = new MessageServiceImpl();

    public static MessageService getInstance() {
        return ourInstance;
    }
    /**
     * 根据用户id获取最后一页消息
     * @param userId 用户id
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getLatestUserMessage(String userId, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        int i = allRecord % pageSize == 0 ? 0 : 1;
        int allPage = allRecord/pageSize + i;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, allPage, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    private void getPackets(PageResult<Packet> pageResult, FileService service, List<String> files, PageResult<Integer> pageResult1, Integer start, Integer end) {
        int i = 0;
        List<Page> pages = new ArrayList<>();
        for (int j = 0; j<files.size(); j++) {
            i += Constants.MAX_LINE;
            if (i <start) {
                continue;
            }
            int i1 = j * Constants.MAX_LINE;
            int startRecord = start - i1;
            addPage(files, end, i, (List<Page>) pages, j, startRecord);

        }
        List<String> messages = new ArrayList<>();
        pages.forEach(p->{
            List<String> list = service.readMessage(new File(p.getFileAbsPath()), p.start, p.end);
            messages.addAll(list);
        });
        List<Packet> collect = messages.stream().map(PacketCode.INSTANCE::decode).collect(Collectors.toList());

        pageResult.setCurrentPage(pageResult1.getCurrentPage());
        pageResult.setRecords(collect);
        pageResult.setTotalRecord(pageResult1.getTotalRecord());
        pageResult.setPages(pageResult1.getPages());
        pageResult.setHasNext(pageResult1.isHasNext());
    }

    private void addPage(List<String> files, Integer end, int i, List<Page> pages, int j, int startRecord) {
        int endRecord;
        if (i > end) {
            endRecord = end-j*Constants.MAX_LINE;
        } else {
            endRecord = i;
        }
        Page page = new Page();
        page.setStart(startRecord);
        page.setEnd(endRecord);
        page.setFileAbsPath(files.get(j));
        pages.add(page);
    }

    /**
     * 根据群组id获取最后一页消息
     * @param groupId 群组id
     * @param pageSize 一页的记录数          
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getLatestGroupMessage(String groupId, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        int i = allRecord % pageSize == 0 ? 0 : 1;
        int allPage = allRecord/pageSize + i;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, allPage, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据用户id获取最早一页消息
     * @param userId 用户id
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getEarliestUserMessage(String userId, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, 1, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据群组id获取最早一页消息
     * @param groupId 群组id
     * @param pageSize 一页的记录数                
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getEarliestGroupMessage(String groupId, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, 1, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据用户id获取上一页消息
     * @param userId 用户id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getLastUserMessage(String userId, int currentPage, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, currentPage-1, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据群组id获取上一页消息
     * @param groupId 群组id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getLastGroupMessage(String groupId, int currentPage, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, currentPage-1, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据用户id获取下一页消息
     * @param userId 用户id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getNextUserMessage(String userId, int currentPage, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, currentPage, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 根据群组id获取下一页消息
     * @param groupId 群组id
     * @param currentPage 当前页
     * @param pageSize 一页的记录数
     * @return 返回消息记录
     */
    @Override
    public PageResult<Packet> getNextGroupMessage(String groupId, int currentPage, int pageSize) {
        PageResult<Packet> pageResult = new PageResult<>();
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        int size = files.size();
        String s = files.get(size-1);
        File file = new File(s);
        int lineNum = service.getLineNum(file);
        int allRecord = (size - 1) * Constants.MAX_LINE + lineNum;
        PageResult<Integer> pageResult1 = getPageResult(allRecord, currentPage, pageSize);
        List<Integer> records = pageResult1.getRecords();
        if (records.size() == 0) {
            return pageResult;
        }
        Integer start = records.get(0);
        Integer end = records.get(records.size()-1);
        getPackets(pageResult, service, files, pageResult1, start, end);
        return pageResult;
    }

    /**
     * 写入消息到用户本地记录
     * @param userId 用户id
     * @param packet 消息体
     */
    @Override
    public void writeUserMessage(String userId, Packet packet) {
        List<String> files = FileService.DEFAULT.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        String s = files.get(files.size()-1);
        FileService.DEFAULT.writeMessage(new File(s), JSON.toJSONString(packet));
    }

    /**
     * 写入消息到群组本地记录
     * @param groupId 群组id
     * @param packet 消息体
     */
    @Override
    public void writeGroupMessage(String groupId, Packet packet) {
        List<String> files = FileService.DEFAULT.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        String s = files.get(files.size()-1);
        FileService.DEFAULT.writeMessage(new File(s), JSON.toJSONString(packet));
    }

    @Override
    public List<Packet> getLastUserCacheMessage(String userId, int pageSize) {
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), userId, Constants.message_type_user);
        return getLastCacheMessage(pageSize, service, files);
    }

    @NotNull
    private List<Packet> getLastCacheMessage(int pageSize, FileService service, List<String> files) {
        Collections.reverse(files);
        List<Page> pages = new ArrayList<>();
        int lineNum = 0;
        for (String filePath : files) {
            int lineNum1 = service.getLineNum(new File(filePath));
            lineNum += lineNum1;
            if (lineNum >= pageSize) {
                int oldLineNum = lineNum - lineNum1;
                int needLineNum = pageSize - oldLineNum;
                Page page = new Page();
                page.setFileAbsPath(filePath);
                page.setStart(lineNum1 - needLineNum);
                page.setEnd(lineNum1);
                pages.add(page);
                break;
            }
            Page page = new Page();
            page.setFileAbsPath(filePath);
            page.setStart(1);
            page.setEnd(2);
            pages.add(page);
        }

        Collections.reverse(pages);
        List<String> messages = new ArrayList<>();
        for (Page page : pages) {
            List<String> list = service.readMessage(new File(page.getFileAbsPath()), page.start, page.end);
            messages.addAll(list);
        }

        return messages.stream().map(PacketCode.INSTANCE::decode).collect(Collectors.toList());
    }

    @Override
    public List<Packet> getLastGroupCacheMessage(String groupId, int pageSize) {
        FileService service = FileService.DEFAULT;
        List<String> files = service.getUserMessageFiles(Cache.currentUser.getUserId(), groupId, Constants.message_type_group);
        return getLastCacheMessage(pageSize, service, files);
    }

    private static PageResult<Integer> getPageResult(int totalRecord, int page, int size) {
        PageResult<Integer> pageResult = new PageResult<>();
        int pages = (int) (totalRecord/size + ((totalRecord%size)==0 ? 0 :1));
        pageResult.setPages(pages);
        pageResult.setTotalRecord(totalRecord);
        if (totalRecord == 0 || totalRecord <= size *(page-1)) {
            pageResult.setHasNext(false);
            pageResult.setRecords(new ArrayList<>());
            return pageResult;
        }

        boolean hasNext = totalRecord > size * page;
        List<Integer> integers = new ArrayList<>();
        IntStream.range((page - 1) * size +1, hasNext ? page * size +1: totalRecord+1).forEach(integers::add);
        pageResult.setHasNext(hasNext);
        pageResult.setRecords(integers);
        pageResult.setCurrentPage(page);

        return pageResult;
    }
    
    
    @Data
    public static class Page {
        private int start;
        private int end;
        private String fileAbsPath;
    }
}
