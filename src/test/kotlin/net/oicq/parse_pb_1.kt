package net.oicq

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import moe.fuqiuluo.proto2json.ProtoUtils
import moe.fuqiuluo.proto2json.asUtf8String
import moe.fuqiuluo.proto2json.hex2ByteArray

private val pb = "0AB6010A4408BDACFAB302120936343538333032303528D0FEEA92063218755F334267755A7A355F363062536C786B705262464335774211420F08DE4910E4FBD2A70622040A021000121B08DC05100C180C28A89F0330E4FBD2A70660DC85E08080808080021A510A00124D08BDACFAB302100C18012A2708E4FBD2A70610011A1D0A18755F4D67594D696671416E6F4F334E6D3545326B6578646710D8042218755F30717A6346633756744F524E5A6E636162624F796E77180122300A0D392E3134382E3231372E31323510FE9D011A1B10DC0518A89F0320DC85E0808080808002300C380140BDACFAB302".hex2ByteArray()

val json = Json {
    prettyPrint = true
}

fun main() {
    val obj = ProtoUtils.decodeFromByteArray(pb)
    // 打印成json
    println(json.encodeToString(obj.toJson()))

    val uid = obj[1][1][6].asUtf8String
    println(uid)

    val uid2 = obj[1, 1, 6].asUtf8String
    println(uid2)
}