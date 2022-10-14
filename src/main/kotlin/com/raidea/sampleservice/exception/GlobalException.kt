package com.raidea.sampleservice.exception


import com.raidea.sampleservice.exception.property.ExceptionProperty

open class GlobalException(private val property: ExceptionProperty): RuntimeException() {
    val code: Int
        get() = property.code

    val msg : String
        get() = property.message

    val result : Any
        get() = property.result
}
