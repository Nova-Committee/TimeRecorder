<div align="center">
  <img width="512" src="https://github.com/xia-mc/TimeRecorder/assets/108219418/d655c66e-c3a7-4f8e-aa2f-13f14b0b36a6" alt="logo">

# 光阴：莉拉提娅
<p align="center">
    <a href="https://github.com/Nova-Committee/TimeRecorder/issues">
      <img src="https://img.shields.io/github/issues/Nova-Committee/TimeRecorder?style=flat" alt="issues" />
    </a>
    <a href="https://www.curseforge.com/minecraft/mc-mods/TimeRecorder">
      <img src="http://cf.way2muchnoise.eu/timerecorder.svg" alt="CurseForge Download">
    </a>
    <img src="https://img.shields.io/badge/license-GPLV3-green" alt="License">
    <a href="https://github.com/Nova-Committee/TimeRecorder/actions/workflows/gradle.yml">
      <img src="https://github.com/Nova-Committee/TimeRecorder/actions/workflows/gradle.yml/badge.svg">
    </a>  
</p>

一个服务端模组，允许您统计服务器的一些信息。

从[`Release 1.0.0`](https://github.com/xia-mc/TimeRecorder/releases/tag/1.0.0)开始，本Mod是[McBot](https://github.com/Nova-Committee/McBot)的附属Mod。
</div>

## 亮点
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

## 指令
- /tr help - 显示此帮助信息
- /tr report - 显示当日服务器日报
- /tr recall - 撤回上一条消息
- /tr reportQQ - 显示和发送当日服务器日报到QQ **\[仅限管理员]**
- /tr reportAll - 显示当日所有玩家的统计信息 **\[仅限管理员]**
- /tr reload - 重载配置文件 **\[仅限管理员]**
- /tr dump save - 保存统计信息
- /tr dump load \[\<filename>] - 还原统计信息

## 兼容性
支持**Vanish** Mod：隐身玩家不会被标记为在线，也不会统计它们的在线时间。

支持**Carpet** Mod：通过 ```/player``` 召唤的假人会被列入“机器人”。

## 如何使用
从[Github Action](https://github.com/xia-mc/TimeRecorder/actions)获取开发版。 **(需要最新开发版McBot)**

从[Github Release](https://github.com/xia-mc/TimeRecorder/releases)获取正式版。 **(需要最新正式版McBot)**

- 安装mod到服务端 **（Fabric）**
- 启动服务器，调整配置文件里的设置。
- 通过 ```/tr``` 控制Mod（普通玩家只可使用部分指令）。

## 贡献
欢迎提issue或提交pull request。

## 相关项目
- [McBot](https://github.com/Nova-Committee/McBot)
- [AutoConfig Toml](https://github.com/TheRandomLabs/AutoConfig-TOML)
