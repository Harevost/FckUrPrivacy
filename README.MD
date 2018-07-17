# FckUrPrivacy

目前进度：

* 研究分析Android环境下的自动安全取证技术
    * 并没有。
* 静默形式实现证据信息监控、获取、证据内容安全转移。
    * 可在服务模式下进行监控与获取。 
* 监控取证设备基本物理信息（设备型号、序列号、WiFi MAC、蓝牙MAC、IMEI、ICCID等）。
    * 可全部获取。
* 监控取证通话记录（录音）。
    * 可读取通话记录、可录音。
* 特定条件下静默拨号、发送短信、自动清理通话记录和短信记录。
    * 可静默发短信与清理通话记录
    * TODO：静默拨号、清理短信记录
    * TODO：条件待定
* 监控取证照片内容
    * 尚未实现
* 监控取证IM通信活动（实时键盘记录）
    * 试图使用辅助功能获取键盘记录
* 监控设备活动位置（LBS）
    * 可调用百度地图API实现
* 将取证内容加密存储并在空闲时上传至特定服务器
    * TODO：加密存储内容
    * TODO：判断空闲
    * TODO：简单服务器搭建