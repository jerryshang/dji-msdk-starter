# Dji MSDK 样例项目

使用 MSDK 与 PSDK 数据交互的样例项目。

- 使用较新版本的开发包和工具包
- 使用 Jetpack Compose
- 集成 Dji Mobile SDK 
- 初始化 Dji Payload SDK 相关的数据监听和发送

不过，

- 未集成 uxsdk
- 未定义 release 时的 proguard 规则 

使用该项目作为初始模板时，几处需要留意的修改内容：

- `AndroidManifest.xml` 中的 `com.dji.sdk.API_KEY`
- `res/values/strings.xml` 中的 `app_name_override` 

## 参考

- [Official Doc](https://developer.dji.com/doc/mobile-sdk-tutorial/cn/)
- [MSDK 版本升级常见问题](https://tireless.dev/blog/pitfalls-dji-msdk-upgrade/)