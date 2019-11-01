# EasyChat JavaFx Client

* 该应用采用JavaFx + etty框架实现，JavaFx实现了简单显示界面，Netty实现了跟后台的TCP通信

## 使用说明
1. 需要结合EasyChat Server后端一起使用
2. 按照后端的要求先将后端启动起来
3. 然后配置ServerConf类里面的后端服务器ip
4. 具体流程参考后端提供的接口文档，有较为详细的说明

## 启动说明
> 环境要求：电脑拥有JDK 8及以上版本的环境，执行下面的命令
``` dos
java -jar EasyChatJavaFXClient.jar
```

## EasyChat相关开源项目

#### 服务端：[EasyChatServer](https://github.com/yetel/EasyChatServer)
#### Android客户端：[EasyChatAndroidClient](https://github.com/yetel/EasyChatAndroidClient)
