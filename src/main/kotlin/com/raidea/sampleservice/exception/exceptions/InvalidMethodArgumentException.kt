package com.raidea.sampleservice.exception.exceptions


import com.raidea.sampleservice.exception.GlobalException
import com.raidea.sampleservice.exception.property.GlobalExceptionErrorCode

class InvalidMethodArgumentException private constructor(): GlobalException(GlobalExceptionErrorCode.INVALID_METHOD_ARGUMENT){
    companion object {
        @JvmField
        val EXCEPTION = InvalidMethodArgumentException()
    }
}
