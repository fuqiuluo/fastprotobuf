package net.oicq

import moe.fuqiuluo.proto2json.Protobuf
import moe.fuqiuluo.proto2json.protobufOf
import moe.fuqiuluo.proto2json.toHexString
import kotlin.system.measureTimeMillis

fun main() {
    println(measureTimeMillis {
        repeat(1) {
            val proto = protobufOf {
                it[4] = 7
            }
            proto[2, 1, 2] = 1

            proto[3] = {
                it[1] = "66666666666"
            }

            proto[1, 2, 1] = "1372362033"
            proto[1, 2, 2] = byteArrayOf(1, 2, 3, 4)

            proto[1, 3, 1] = arrayOf(
                "TestList",
                "TestList2",
            )
            proto[1, 3, 2] = arrayOf(
                666, 7777
            )
            //proto.toByteArray()
            println(proto.toByteArray().toHexString())
        }
    }.toString() + "ms")
}