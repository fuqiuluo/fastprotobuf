# fastprotobuf
像oicq-js哪样快速的解析protobuf，支持序列化与反序列化。

# 愉快写法

## 获取字符串/数字/字节组

```kotlin
val obj = ProtoUtils.decodeFromByteArray(pb)
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
val obj = ProtoUtils.decodeFromByteArray(pb3)
// 打印成json
println(json.encodeToString(obj.toJson()))

val face = obj[2].asList.first { elem ->
    elem.asMap.let { 53 in it && it[53, 1].asInt == 33 }
}
println("表情ID: " + face[53, 2, 1].asInt)
println("表情简介: " + face[53, 2, 2].asUtf8String)
```

## 序列化数据

```kotlin
val proto = ProtoMap()
proto[2, 1, 2] = 1 
// tag不能为0，这个是protobuf规范，别乱搞哦！

proto[3] = {
    it[1] = "66666666666"
}

proto[1, 2, 1] = "1372362033"
proto[1, 2, 2] = byteArrayOf(1, 2, 3, 4)

// proto[1][2][3] = 1
// 这可能是一个错误写法，这样你可能会得到空指针错误

proto[1, 3, 1] = arrayOf(
    "TestList",
    "TestList2"
)
proto[1, 3, 2] = arrayOf(
    666, 7777
)

println(proto.toByteArray().toHexString())
```

# 测试构建数据

```kotlin
import moe.fuqiuluo.proto2json.ProtoMap
import moe.fuqiuluo.proto2json.proto
import moe.fuqiuluo.proto2json.toHexString
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis {
        repeat(10000) {
            val proto = ProtoMap()
            proto[2, 1, 2] = 1

            proto[1, 2, 1] = "1372362033"
            proto[1, 2, 2] = byteArrayOf(1, 2, 3, 4)

            proto[1, 3, 1] = arrayOf(
                "TestList",
                "TestList2",
            )
            proto[1, 3, 2] = arrayOf(
                666, 7777
            )
            proto.toByteArray()
        }
    }.toString() + "ms")
}
```

> 测试平台: Windows11 Java17 默认GC策略
> 
> 1次执行耗时70ms左右,   0.07s
> 
> 1w次耗时在120ms左右,   0.12s
> 
> 100w耗时 1400ms左右,  1.4s
> 
> 1000w耗时10300ms左右, 10.3s