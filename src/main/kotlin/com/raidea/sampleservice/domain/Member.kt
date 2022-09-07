package com.raidea.sampleservice.domain

import org.springframework.data.mongodb.core.mapping.Document

//= User
@Document(collection = "user")
data class Member(
    var uuid: String? = null,
    var userId: String? = null,
    var username: String? = null,
    var phone: String? = null,
    var email: String? = null,
    var address: String? = null
)