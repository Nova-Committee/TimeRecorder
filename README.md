<div align="center">
  <img width="512" src="https://github.com/xia-mc/TimeRecorder/assets/108219418/d655c66e-c3a7-4f8e-aa2f-13f14b0b36a6" alt="logo">

# 光阴：莉拉提娅
一个服务端模组，允许您统计服务器的一些信息。

从[`Release 1.0.0`](https://github.com/xia-mc/TimeRecorder/releases/tag/1.0.0)开始，本Mod是[McBot](https://github.com/Nova-Committee/McBot)的附属Mod。
</div>

## 它可以做什么
```
----------服务器日报----------
 玩家日活：2 | 机器人日活：1
 上线最久的玩家：xia__mc 10分钟
 上线最久的机器人：bot 1分钟
 今日活跃玩家：
  xia__mc
  A0000_Xz
 感谢各位玩家对服务器做出的贡献！
```
- 生成服务器日报
- **[仅限管理员]** 发送到游戏和群聊里。


```
管理员: xia__mc  离线
 上线时长：10分钟
 使用指令：tp

玩家: A0000_Xz  在线
 上线时长：10秒

机器人: bot  在线
 上线时长：1分钟
```
- **[仅限管理员]** 生成每个玩家的报告


```
{
    "version": "1.0.0",
    "hash": 0,
    "time": 1710675809885,
    "onlineMap": {
        "326aafd8-78b5-32ee-b653-4a9b189472b0": true
    },
    "playerDataMap": {
        "326aafd8-78b5-32ee-b653-4a9b189472b0": {
            "name": "xia__mc",
            "UUID": "326aafd8-78b5-32ee-b653-4a9b189472b0",
            "OP": true,
            "fakePlayer": false,
            "playTime": 1397,
            "OPCommandUsed": []
        }
    }
}
```
- **[仅限管理员]** 存储/加载统计数据

## 兼容性
支持**Vanish** Mod：隐身玩家不会被标记为在线，也不会统计它们的在线时间。

支持**Carpet** Mod：通过 ```/player``` 召唤的假人会被列入“机器人”。

## 如何使用
从[Github Action](https://github.com/xia-mc/TimeRecorder/actions)获取开发版。

从[Github Release](https://github.com/xia-mc/TimeRecorder/releases)获取稳定版。

- 安装mod到服务端 **（1.20.1 fabric）**
- 启动支持 ```onebot``` 协议的QQ机器人。
- 启动服务器，调整配置文件里的设置。
- 通过 ```/tr``` 控制Mod（普通玩家只可使用部分指令）。

## 贡献
欢迎提issue或提交pull request。

## 相关项目
- [OneBot Client](https://github.com/cnlimiter/onebot-client)
- [AtomConfig Toml](https://github.com/TheRandomLabs/AutoConfig-TOML)
