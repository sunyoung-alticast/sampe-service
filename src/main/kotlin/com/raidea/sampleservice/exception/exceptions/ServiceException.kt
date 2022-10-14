package com.raidea.sampleservice.exception.exceptions

import com.raidea.sampleservice.exception.property.ServiceMessage

class ServiceException(val serviceMessage: ServiceMessage) : RuntimeException()