package com.raidea.sampleservice.exception.property

enum class GlobalExceptionErrorCode (
        override val code: Int,
        override val message: String,
        override val result: Any
) : ExceptionProperty
{
        UNEXPECTED(500,"Unexpected Exception Occurred",  ""),
        INVALID_METHOD_ARGUMENT(400, "Invalid Method Argument", ""),
        REQUEST_NOT_FOUND(404,"Cannot Find Valid Controller",  "")
}
