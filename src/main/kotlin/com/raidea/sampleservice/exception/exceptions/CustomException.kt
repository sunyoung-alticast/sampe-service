package com.raidea.sampleservice.exception.exceptions

class CustomException : Exception {
    var errorCode = 0
    var code = 0
    override var message : String? = null

    var callMethod:String? = null

    constructor(callMethod:String ?= "", commonError : CommonErrorCode) : super(){
        this.callMethod = callMethod
        this.code    = commonError.code
        this.message = commonError.message
    }

    constructor(commonError : CommonErrorCode) : super(){
        this.code    = commonError.code
        this.message = commonError.message
    }

    companion object {
        fun throwException(callMethod: String ?= "", commError : CommonErrorCode) : CustomException =
            CustomException(callMethod, commError)

        fun throwException(commError : CommonErrorCode) : CustomException =
            CustomException(commError)
    }
}