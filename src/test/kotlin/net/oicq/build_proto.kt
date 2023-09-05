package net.oicq

import moe.fuqiuluo.proto2json.ProtoMap
import moe.fuqiuluo.proto2json.protobufMapOf
import moe.fuqiuluo.proto2json.protobufOf
import moe.fuqiuluo.proto2json.toHexString
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis {
        repeat(1) {
            val proto = protobufOf(
                1 to 2 to 3 to "666",
                2 to 1
            )
            proto.toByteArray()
            //println(proto.toByteArray().toHexString())
        }
    }.toString() + "ms")
}


