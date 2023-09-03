# fastprotobuf
像oicq-js哪样快速的解析protobuf，本项目不支持序列化，仅支持反序列化。

# 愉快写法

## 获取字符串/数字/字节组

```kotlin
val obj = ProtoUtils.parseProto(pb)
// 打印成json
println(json.encodeToString(obj.toJson()))

val uid = obj[1][1][6].asUtf8String
println(uid)

val uid2 = obj[1, 1, 6].asUtf8String
println(uid2)
````

上面的两个获取uid等价。

## 获取List里面的内容

```kotlin
val obj = ProtoUtils.parseProto(pb3)
// 打印成json
println(json.encodeToString(obj.toJson()))

val face = obj[2].asList.first { elem ->
    elem.asMap.let { 53 in it && it[53, 1].asInt == 33 }
}
println("表情ID: " + face[53, 2, 1].asInt)
println("表情简介: " + face[53, 2, 2].asUtf8String)
```
