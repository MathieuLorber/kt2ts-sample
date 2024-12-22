package io.github.kt2tssample

import java.util.UUID

interface SampleId<T> {
    val rawId: T
}

// UUID(29d902e1-c234-4e54-b305-8b7a885a560e) -> 29d902e1c2344e54b3058b7a885a560e
fun UUID.stringUuid(): String =
    toString().let {
        it.substring(0, 8) +
                it.substring(9, 13) +
                it.substring(14, 18) +
                it.substring(19, 23) +
                it.substring(24, 36)
    }

abstract class SampleUuidId : SampleId<UUID> {
    final override fun toString() = "${javaClass.simpleName}(${stringUuid()})"

    fun stringUuid() = rawId.stringUuid()
}

data class MySampleId(override val rawId: UUID) : SampleUuidId()