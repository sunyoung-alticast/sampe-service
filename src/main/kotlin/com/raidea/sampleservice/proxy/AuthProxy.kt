package com.raidea.sampleservice.proxy

import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.beans.factory.annotation.Autowired
import java.time.Duration


@Service
class AuthProxy {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Autowired
    lateinit var  webClientBuilder :WebClient.Builder

    companion object {
        const val BASE_URL = "lb://pauth-service/"
    }

    fun getMembers(member: String): Mono<String> {
        return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/members")
            .retrieve()
            .bodyToMono(HashMap::class.java)
            .flatMap {
                Mono.just(it.toString())
            }
    }

    fun getMember(memberId: String) : Mono<String> {
         return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/member/{memberId}", memberId)
            .retrieve().bodyToMono(HashMap::class.java)
            .flatMap {
                Mono.just(it.toString())
            }
    }


    // https://tedblob.com/mono-zip-example/
    fun getUser(userId: String): Mono<HashMap<*, *>> {
        return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/members")
            .retrieve()
            .bodyToMono(HashMap::class.java).flatMap {
                Mono.just(it)
            }.timeout(Duration.ofMinutes(1))
            .retry(1)
            .onErrorResume {
                myFallBackMethod(it, userId)
            }
    }

    fun getItem(itemId: String): Mono<HashMap<*,*>> {
        return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/member/{memberId}", itemId)
            .retrieve()
            .bodyToMono(HashMap::class.java).flatMap {
                Mono.just(it)
            }.timeout(Duration.ofMinutes(1))
            .retry(1)
            .onErrorResume {
                myFallBackMethod(it, itemId)
            }
    }

    fun getUserSync(userId: String): HashMap<*, *>? {
        return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/members")
            .retrieve()
            .bodyToMono(HashMap::class.java)
            .block()
    }

    fun getItemSync(itemId: String): HashMap<*, *>? {
        return webClientBuilder.baseUrl(BASE_URL)
            .build()
            .get()
            .uri("/pauth-service/user/v1.1/member/{memberId}", itemId)
            .retrieve()
            .bodyToMono(HashMap::class.java)
            .block()
    }

    fun myFallBackMethod (throws : Throwable, id : String) : Mono <HashMap<*,*>> {
        var result = hashMapOf<String, Any>()
        println("called fallback ! :  $throws")
        return Mono.just(result)
    }

    fun fetchUserAndItemAsync(userId: String, itemId: String) : Mono <String> {
        var user = getUser(userId)
        var item = getItem(itemId)
        return Mono.zip(user, item).flatMap {
            Mono.just(it.t1.toString() + it.t2.toString())
        }
    }

    fun fetchUserAndItemSync(userId: String, itemId: String) : Mono <String> {
        var user = getUserSync(userId)
        var item = getItemSync(itemId)

        return Mono.just(user.toString() + item.toString())
    }
}