package io.github.kt2tssample

import com.fasterxml.jackson.annotation.JsonTypeInfo
import kt2ts.annotation.GenerateTypescript

@GenerateTypescript
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType")
sealed class SomeSealedClass

data class SomeClassImpl(val someValue: String) : SomeSealedClass()

data class AnotherClassImpl(val anotherValue: Int) : SomeSealedClass()
