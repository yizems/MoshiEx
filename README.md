## Moshi Extensions

[![](https://jitpack.io/v/yizems/MoshiEx.svg)](https://jitpack.io/#yizems/MoshiEx)

### 特性

- "android-json":添加对 安卓 `JsonObject` `JsonArray`的支持
- "jobj": 添加对 `JsonObject`的支持,仿 `FastJson`,支持`Serializable`
- "moshiex": 添加一些常用的数据类型适配器
    - `Base64Qualifier` 支持Base64编解码
    - `EnumJsonInt` 和 `EnumJsonString` 支持枚举类型的json转换支持
    -  优化: 非double数值反序列化为long, 序列化 无小数位的double为long
    -  `Boolean` 支持 "1","true" 的兼容
    
    

