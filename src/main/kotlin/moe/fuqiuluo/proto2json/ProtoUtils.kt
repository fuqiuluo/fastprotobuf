package moe.fuqiuluo.proto2json

import com.google.protobuf.UnknownFieldSet

object ProtoUtils {

    fun parseProto(data: ByteArray): ProtoMap {
        val unknownFieldSet = UnknownFieldSet.parseFrom(data)
        val dest = ProtoMap(hashMapOf())
        printUnknownFieldSet(unknownFieldSet, dest)
        return dest
    }

    private fun printUnknownFieldSet(set: UnknownFieldSet, dest: ProtoMap) {
        set.asMap().forEach { (tag, field) ->
            field.varintList.forEach {
                dest[tag] = it
            }
            field.fixed32List.forEach {
                dest[tag] = it
            }
            field.fixed64List.forEach {
                dest[tag] = it
            }
            field.lengthDelimitedList.forEach {
                try {
                    val unknownFieldSet = UnknownFieldSet.parseFrom(it)
                    val map = ProtoMap(hashMapOf())
                    printUnknownFieldSet(unknownFieldSet, map)
                    dest[tag] = map
                } catch (e: Throwable) {
                    dest[tag] = it.proto
                }
            }
            field.groupList.forEach {
                val map = ProtoMap(hashMapOf())
                printUnknownFieldSet(it, map)
                dest[tag] = map
            }
        }
    }

}

