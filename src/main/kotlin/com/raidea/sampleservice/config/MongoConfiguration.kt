package com.raidea.sampleservice.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Bean
import org.springframework.transaction.reactive.TransactionalOperator
import org.springframework.transaction.ReactiveTransactionManager
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory


/* AOP Transaction 설정을 위한 Mongo 설정  */

@Configuration
class MongoConfiguration {
    @Bean
    fun transactionManager(rdbf: ReactiveMongoDatabaseFactory): ReactiveMongoTransactionManager {
        return ReactiveMongoTransactionManager(rdbf)
    }

    @Bean
    fun transactionOperator(rtm: ReactiveTransactionManager): TransactionalOperator {
        return TransactionalOperator.create(rtm)
    }
}

