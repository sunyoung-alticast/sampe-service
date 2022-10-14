package com.raidea.sampleservice.exception.exceptions

import com.raidea.sampleservice.exception.property.ExceptionProperty


enum class CommonErrorCode(
    override val code: Int,
    override val message: String,
    override val result: Any,
): ExceptionProperty {
    OK_CODE(200, "ok", ""),
    NO_VALUE_MESSAGE(1000001, "No Value Present", ""),
    REQUIRED_MSG(   1000002, "is required.", ""),
    PROXY_EXCEPTION_MSG(1000003, "Proxy Exception.", ""),
    LIMIT_EXCEEDED_MSG(1000004, "Limit Exceeded.", ""),
    ALREADY_EXIST_MSG(3090001, "is already exist.", ""),
    ERROR_TIME_MSG(3090002, "timeMin grater than timeMax", ""),
    NOT_VALID_LANGUAGE_MSG(3090003, "Not valid language code (ISO 639-1)", ""),
    INVALID_PARAMETER_MSG(3090004, "Invalid", ""),
    INVALID_FORMAT_MSG(3090005, "Invalid", ""),
    NO_LEAST_PAGE_INDEX(3090006, "Page index must not be less than one!", ""),
    ONLY_NUMBER_ACCEPTED_MSG(3090007, "Only numbers accepted. Wrong value:", ""),
    START_WITH_ERROR_MSG(3090008, "Country-code should start with +", ""),
    SEND_MESSAGE_FAIL_MSG(3090009, "send message fail", ""),
    PERIOD_OVERLAP_MSG(3090010, "period is overlap", ""),
    EDIT_FAIL_MSG(3090011, "Can't edit", ""),
}
