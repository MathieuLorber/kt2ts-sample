package io.github.kt2tssample

import io.github.kt2tssample.subpackage.BaseDataClass
import java.time.LocalDate
import kt2ts.annotation.GenerateTypescript

enum class MyEnum(val value: Int) {
    First(1),
    Second(2),
    Third(3)
}