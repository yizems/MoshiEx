## Moshi Extensions

[![](https://jitpack.io/v/yizems/MoshiEx.svg)](https://jitpack.io/#yizems/MoshiEx)

### 特性

#### "android-json":添加对 安卓 `JsonObject` `JsonArray`的支持

```
// 初始化时调用以下方法添加适配器即可
/**
 * 支持 [MJsonObject] 和 [MJsonArray] ,并且实现了 [Serializable] 接口
 */
public fun Moshi.Builder.addAndroidJsonSupport(): Moshi.Builder
```

#### "jobj": 添加对 `JsonObject`的支持,仿 `FastJson`,支持`Serializable`

```
// 添加JsonAdapter
public fun Moshi.Builder.addMJsonAdapter(): Moshi.Builder
```

#### "moshiex": 添加一些常用的数据类型适配器

- `Base64Qualifier` 支持自动对Base64编解码
- `EnumJsonInt` 和 `EnumJsonString` 支持枚举类型的json转换支持
- 非double数值反序列化为long, 序列化 无小数位的double为long
- `Boolean` 支持 "1","true" 的兼容


### 使用

**引入依赖**

```
val version = "最新版本"

implementation 'com.github.yizems.MoshiEx:jobj:0.0.2'
implementation 'com.github.yizems.MoshiEx:moshi-android-ext:0.0.2'
implementation 'com.github.yizems.MoshiEx:moshi-ex:0.0.2'

```
    
**初始化**

```
// 建议使用 moshiInstances
moshiInstances.newBuilder().addAndroidJsonSupport()
    .addMJsonAdapter()
    .build()
    .setToDefault()
```
