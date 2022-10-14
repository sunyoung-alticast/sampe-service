package com.raidea.sampleservice.exception.exceptions


import com.raidea.sampleservice.exception.GlobalException
import com.raidea.sampleservice.exception.property.GlobalExceptionErrorCode

class RequestNotFoundException private constructor(): GlobalException(GlobalExceptionErrorCode.REQUEST_NOT_FOUND){
    companion object {
        @JvmField
        val EXCEPTION = RequestNotFoundException()
    }
}
