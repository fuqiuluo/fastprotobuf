package net.oicq

import kotlinx.serialization.encodeToString
import moe.fuqiuluo.proto2json.*

private val pb3 = """0a210800109eb0d3a70618e8cef9f3052000280a300038860140024a06e5ae8be4bd93126daa026a08005000600068008001009a015c080b20cb5078808004c80100f00100f80100900200c802009803d302a00340b00301b80300c0038c9f01d00300e803008a0406080308011064900480c8808004b8048202c00400ca040408003007f804cc8408800555880514800600121eaa031b0821121508950212072fe6b1aae6b1aa1a072fe6b1aae6b1aa1801121eaa031b0821121508950212072fe6b1aae6b1aa1a072fe6b1aae6b1aa1801121eaa031b0821121508950212072fe6b1aae6b1aa1a072fe6b1aae6b1aa1801121eaa031b0821121508950212072fe6b1aae6b1aa1a072fe6b1aae6b1aa180112084a0608b9da7e4001122e82012b121be6b2a1e69c89e4babae6af94e68891e69bb4e68782e691b8e9b1bc1801200828033a06e5ba9fe789a9""".trimIndent().hex2ByteArray(true)

fun main() {
    val obj = ProtoUtils.decodeFromByteArray(pb3)
    // 打印成json
    println(json.encodeToString(obj.toJson()))

    val face = obj[2].asList.value.first { elem ->
        elem.asMap.let { 53 in it && it[53, 1].asInt == 33 }
    }
    println("表情ID: " + face[53, 2, 1].asInt)
    println("表情简介: " + face[53, 2, 2].asUtf8String)

    val enc = ProtoUtils.encodeToByteArray(obj)
    val obj2 = ProtoUtils.decodeFromByteArray(enc)
    val enc2 = ProtoUtils.encodeToByteArray(obj)

    println("数据一致性：" + pb3.contentEquals(enc))
    println("二次数据一致性：" + enc.contentEquals(enc2))
    println("内容一致性：" + (json.encodeToString(obj2.toJson()) == json.encodeToString(obj.toJson())))
}
