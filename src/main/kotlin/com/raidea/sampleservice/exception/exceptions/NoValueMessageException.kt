package com.raidea.sampleservice.exception.exceptions

import com.raidea.sampleservice.exception.GlobalException

class NoValueMessageException private constructor() : GlobalException(CommonErrorCode.NO_VALUE_MESSAGE) {
    companion object {
        @JvmField
        val EXCEPTION = NoValueMessageException()
    }
}