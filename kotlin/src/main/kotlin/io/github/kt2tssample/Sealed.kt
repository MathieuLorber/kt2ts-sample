package io.github.kt2tssample

import com.fasterxml.jackson.annotation.JsonTypeInfo
import kt2ts.annotation.GenerateTypescript

@GenerateTypescript
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType")
sealed class SomeSealedClass

data class SomeClassImpl(
    val someValue: String,
    val someValue1: String,
    val someValue2: String,
    val someValue3: String,
    val someValue4: String,
    val someValue5: String,
    val someValue6: String,
    val someValue7: String,
    val someValue8: String,
    val someValue9: String,
) : SomeSealedClass()

data class AnotherClassImpl(val anotherValue: Int) : SomeSealedClass()
