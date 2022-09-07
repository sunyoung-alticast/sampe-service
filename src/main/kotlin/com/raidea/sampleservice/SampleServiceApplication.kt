package com.raidea.sampleservice

import org.springframework.boot.autoconfigure.SpringBootApplication
//import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.boot.runApplication
import org.springframework.context.annotation.EnableAspectJAutoProxy
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient

//@EnableDiscoveryClient
//@EnableEurekaClient
@SpringBootApplication
@EnableAspectJAutoProxy

class SampleServiceApplication

fun main(args: Array<String>) {
	runApplication<SampleServiceApplication>(*args)
}
