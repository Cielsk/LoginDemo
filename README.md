# LoginDemo
使用各种常见的架构模式实现登录与注册功能

---

## 项目目的

登录与注册功能十分常见，而且可简单可复杂，需要和云端交互也需要在本地存储状态，是一个比较好的功能演示模块。
因此，通过使用不同的架构模式实现它，从而学习架构模式的实现细节，也方便做出横向比较。

## 各个分支简介

- master：使用最基本的 MVC 结构，以 Activity 充当 Controller
- mvp：使用 [Android Architecture](https://github.com/googlesamples/android-architecture) todo-mvp 分支推荐的 mvp 架构方式
- mvvm：使用 [Android Architecture](https://github.com/googlesamples/android-architecture) todo-mvvm-live 推荐的 mvvm 架构方式

> 注：仅在 mvp 分支中提供了测试代码，其他分支均未进行测试。尤其注意本地单元测试无法通用，但是 UI 测试可通用。

## 实现功能

- 示例性启动页面
- 注册
- 登录
- Restful 后端云访问
- 记住登录状态

> 注：后端云使用 [LeanCloud](https://leancloud.cn/)，但没有使用短信推送功能，注册和登录均使用“邮箱+密码”的方式。

**注意**：
- 本 demo 不涉及数据库存储，为方便演示，本地数据结构简单，故只使用 SharedPreferences 存储
- 未提供登出功能，需要时必须手动清理应用数据
- 未对数据进行加密，方便调试查看数据内容

## 开源库

- [Butter Knife](http://jakewharton.github.io/butterknife/)
- [Dagger 2](https://github.com/google/dagger)
- [OkHttp](https://github.com/square/okhttp)（v0.1 之前单独使用它发送 HTTP 请求）
- [Gson](https://github.com/google/gson)
- [Retrofit](http://square.github.io/retrofit/)（v0.1 之后引入，方便调用 Restful API）
- [Logger](https://github.com/orhanobut/logger)
- [Toasty](https://github.com/GrenderG/Toasty)
- junit
- Espresso
- Data Binding
- Android Architecture Components（仅在 mvvm 分支中使用）
