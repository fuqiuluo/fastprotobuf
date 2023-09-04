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
val proto = Protobuf()
proto[1] = 10086
println(proto.toByteArray().toHexString())
```

这样貌似也可以，就是非常可惜，不够美观！

```kotlin
val proto = protobufMapOf {
    it[1] = 10086
}
println(proto.toByteArray().toHexString())
```

这样简单的提升了美观性，但是RepeatedField的处理还是不够好。

```kotlin
val proto = protobufMapOf {
    it[1] = arrayOf(1, 2, 3, 4)
}
println(proto.toByteArray().toHexString())
```

这样就实现了RepeatedField的处理，但是还是不够美观，那么复杂一点的结构呢？

```kotlin
val proto = protobufMapOf {
    it[1, 2, 4] = arrayOf(1, 2, 3, 4)
}
println(proto.toByteArray().toHexString())
```

这样就实现了，稍微复杂一点的结构，但是我觉得这种是很垃圾的写法。

```kotlin
val proto = protobufOf(
    1 to 2 to 3 to "666", 
    2 to 1,
    3 to arrayOf("1111111")
)
println(proto.toByteArray().toHexString())
// 单次运行耗时  100ms
// 1W次运行耗时  120ms
// 100W次运行耗时640ms
```

究极形态，固然这个项目就是整活，但是也绝非效率非常的低，JVM的逃逸分析可以降低大量的分配/GC消耗。

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