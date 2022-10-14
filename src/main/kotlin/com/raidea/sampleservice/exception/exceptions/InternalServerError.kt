package com.raidea.sampleservice.exception.exceptions

import com.raidea.sampleservice.exception.GlobalException
import com.raidea.sampleservice.exception.property.GlobalExceptionErrorCode

class InternalServerError private constructor() : GlobalException(GlobalExceptionErrorCode.UNEXPECTED) {
    companion object {
        @JvmField
        val EXCEPTION = InternalServerError()
    }
}