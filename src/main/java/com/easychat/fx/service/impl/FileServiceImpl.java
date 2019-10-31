package com.easychat.fx.service.impl;

import com.easychat.fx.bean.Constants;
import com.easychat.fx.service.FileService;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Zed
 * date: 2019/09/02.
 * description:
 */
public class FileServiceImpl implements FileService {

    private static FileService ourInstance = new FileServiceImpl();

    public static FileService getInstance() {
        return ourInstance;
    }
    /**
     * 获取用户的本地消息文件
     * @param userId 用户id
     * @return 按照时间先后顺序返回map， key : 文件绝对路径 value ：用户的本文件的消息记录数。
     */
    @Override
    public List<String> getUserMessageFiles(String currentUser, String userId, String userType) {
        List<String> fileList = new ArrayList<>();
        File file = new File(Constants.PATH);
        if (!file.exists()) {
            file.mkdir();
        }
        String path = Constants.message_type_user.equals(userType) ? Constants.PATH_USER : Constants.PATH_GROUP;
        path = Constants.PATH + File.separator + path;
        File fileUserGroup = new File(path);
        if (!fileUserGroup.exists()) {
            fileUserGroup.mkdir();
        }
        path = path + File.separator + currentUser;
        File fileCur = new File(path);
        if (!fileCur.exists()) {
            fileCur.mkdir();
        }
        File[] list = fileCur.listFiles();
        File userMessageFile = null;
        if (list == null || list.length == 0) {
            userMessageFile = new File(fileCur.getAbsolutePath() + File.separator + userId);
            userMessageFile.mkdir();
        } else {
            for (File file1 : list) {
                if (userId.equals(file1.getName())) {
                    userMessageFile = file1;
                    break;
                }
            }
            if (userMessageFile == null) {
                userMessageFile = new File(fileCur.getAbsolutePath() + File.separator + userId);
                userMessageFile.mkdir();
            }
        }

        File[] files = userMessageFile.listFiles();
        if (files == null || files.length == 0) {
            File file1 = new File(userMessageFile.getAbsolutePath() + File.separator + 0 + ".log");
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            fileList.add(file1.getAbsolutePath());
        } else {
            fileList = Arrays.stream(files).sorted(Comparator.comparing(this::getString)).map(File::getAbsolutePath).collect(Collectors.toList());
        }
        return fileList;
    }
    /**
     * 给定文件写入消息记录
     * @param file 消息文件
     * @param messageLine 消息内容
     */
    @Override
    public void writeMessage(File file, String messageLine) {
        try {
            if (getLineNum(file) >= Constants.MAX_LINE) {
                String index = getString(file);
                file = new File(file.getParentFile().getAbsolutePath() + File.separator + (Integer.parseInt(index) + 1) + ".log");
                if (!file.exists()) {
                    file.createNewFile();
                }
                writeMessage(file, messageLine);
                return;
            }
            Writer writer = new FileWriter(file, true);
            writer.write(messageLine+"\n");
            writer.flush();
            writer.close();
            
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    @NotNull
    private String getString(File file) {
        String absolutePath = file.getAbsolutePath();
        String absolutePath1 = file.getParentFile().getAbsolutePath();
        String substring = absolutePath.substring(absolutePath1.length());
        int i = substring.indexOf(".");
        return substring.substring(1, i);
    }

    public static void main(String[] args) {
//        for (int i  = 0; i <= 1000; i++) {
//            List<String> zhangsan = FileService.DEFAULT.getUserMessageFiles("zhangsan");
//            File file = new File(zhangsan.get(zhangsan.size() - 1));
//            FileService.DEFAULT.writeMessage(file, "第" + i + "行");
//        }

//        List<String> zhangsan = FileService.DEFAULT.getUserMessageFiles("zhangsan");
//        File file = new File(zhangsan.get(zhangsan.size() - 1));
//        List<String> list = FileService.DEFAULT.readMessage(file, 10, 20);
//        System.out.println(list);
    }

    @Override
    public int getLineNum(File file){
        try {
            return (int)Files.lines(Paths.get(file.getAbsolutePath())).count();
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 给定文件读取 其中一段消息记录
     * @param sourceFile 消息文件
     * @param start 开始记录数
     * @param end 结束记录数
     * @return 消息记录
     */
    @Override
    public List<String> readMessage(File sourceFile, int start, int end) {
        List<String> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(sourceFile));
            LineNumberReader reader = new LineNumberReader(in);
            String s;
            {
                while ((s = reader.readLine()) != null) {
                    int lineNumber = reader.getLineNumber();
                    if (lineNumber >= start && lineNumber <= end) {
                        list.add(s);
                    }
                    if (reader.getLineNumber() > end) {
                        break;
                    }
                }
            }
            reader.close();
            in.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
