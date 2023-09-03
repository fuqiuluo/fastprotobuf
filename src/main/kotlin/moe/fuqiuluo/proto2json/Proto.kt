package moe.fuqiuluo.proto2json

import com.google.protobuf.ByteString
import kotlinx.serialization.json.JsonElement

sealed interface Proto {
    fun toJson(): JsonElement

    operator fun contains(tag: Int): Boolean {
        if (this is ProtoMap) {
            return value.containsKey(tag)
        }
        error("Instance is not ProtoMap")
    }

    operator fun set(tag: Int, v: Proto) {
        if (this is ProtoMap) {
            if (!contains(tag)) {
                value[tag] = v
            } else {
                val oldValue = value[tag]!!
                if (oldValue is ProtoList) {
                    oldValue.add(v)
                } else {
                    value[tag] = ProtoList(arrayListOf(oldValue, v))
                }
            }
            return
        }
        error("Instance is not ProtoMap, it is ${this::class.simpleName}")
    }

    operator fun set(tag: Int, v: Number) {
        if (this is ProtoMap) {
            if (!contains(tag)) {
                value[tag] = ProtoNumber(v)
            } else {
                val oldValue = value[tag]!!
                if (oldValue is ProtoList) {
                    oldValue.add(v.proto)
                } else {
                    value[tag] = ProtoList(arrayListOf(oldValue, v.proto))
                }
            }
            return
        }
        error("Instance is not ProtoMap")
    }

    operator fun get(vararg tags: Int): Proto {
        if (this is ProtoMap) {
            var curMap = value
            tags.forEachIndexed { index, tag ->
                if (index == tags.size - 1) {
                    return curMap[tag] ?: error("Tag $tag not found")
                }
                curMap[tag]?.let { v ->
                    if (v is ProtoMap) {
                        curMap = v.value
                    } else {
                        return v
                    }
                } ?: error("Tag $tag not found")
            }
        }
        error("Instance is not ProtoMap")
    }

    fun add(v: Proto) {
        if (this is ProtoList) {
            value.add(v)
            return
        }
        error("Instance is not ProtoList")
    }
}

class ProtoNumber(
    val value: Number
): Proto {
    override fun toJson(): JsonElement {
        return value.json
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

    override fun toString(): String {
        return "Number($value)"
    }
}

class ProtoByteString(
    val value: ByteString
): Proto, Iterable<Byte> by value {
    override fun toJson(): JsonElement {
        return toByteArray().toHexString().json
    }

    fun toByteArray(): ByteArray {
        return value.toByteArray()
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

    fun toUtfString(): String {
        return value.toStringUtf8()
    }

    override fun toString(): String {
        return "ByteString(${value.toByteArray().toHexString()})"
    }
}

class ProtoList(
    val value: ArrayList<Proto>
): Proto, Collection<Proto> by value {
    override fun toJson(): JsonElement {
        val array = arrayListOf<JsonElement>()
        forEach {
            array.add(it.toJson())
        }
        return array.jsonArray
    }

    override fun equals(other: Any?): Boolean {
        return this === other
    }

    override fun hashCode(): Int {
        return System.identityHashCode(this)
    }

    override fun toString(): String {
        return toJson().toString()
    }
}

class ProtoMap(
    val value: HashMap<Int, Proto>
): Proto {
    override fun toJson(): JsonElement {
        val hashMap = hashMapOf<String, JsonElement>()
        value.forEach { (tag, field) ->
            hashMap[tag.toString()] = field.toJson()
        }
        return hashMap.jsonObject
    }

    override fun toString(): String {
        return toJson().toString()
    }
}

val Number.proto: ProtoNumber
    get() = ProtoNumber(this)

val ByteString.proto: ProtoByteString
    get() = ProtoByteString(this)

val Proto.asString: ByteString
    get() = (this as ProtoByteString).value

val Proto.asNumber: Number
    get() = (this as ProtoNumber).value

val Proto.asMap: ProtoMap
    get() = (this as ProtoMap)

val Proto.asList: ProtoList
    get() = (this as ProtoList)

val Proto.asByteArray: ByteArray
    get() = (this as ProtoByteString).toByteArray()

val Proto.asUtf8String: String
    get() = (this as ProtoByteString).toUtfString()
