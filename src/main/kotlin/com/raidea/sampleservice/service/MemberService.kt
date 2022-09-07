package com.raidea.sampleservice.service

import com.raidea.sampleservice.proxy.AuthProxy
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import org.springframework.stereotype.Service
import org.springframework.beans.factory.annotation.Autowired

interface MemberServiceInterface {
    fun findAllAsync(soId :String): Mono<String>
    fun findAllSync(soId :String): Mono<String>
}

@Service
class MemberService : MemberServiceInterface {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var authProxy : AuthProxy


    override fun findAllSync(soId :String): Mono<String> {
        return authProxy.fetchUserAndItemSync(soId, "newID")

    }

    override fun findAllAsync(soId :String): Mono<String> {
        return authProxy.fetchUserAndItemAsync(soId, "newID")
    }

}