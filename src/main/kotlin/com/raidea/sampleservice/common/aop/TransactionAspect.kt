package com.raidea.sampleservice.common.aop

import org.springframework.aop.Advisor
import org.springframework.aop.aspectj.AspectJExpressionPointcut
import org.springframework.aop.support.DefaultPointcutAdvisor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource
import org.springframework.transaction.interceptor.RollbackRuleAttribute
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute
import org.springframework.transaction.interceptor.TransactionInterceptor
import java.lang.Exception
import org.springframework.data.mongodb.ReactiveMongoTransactionManager

@Configuration
class TransactionAspect (private val tm: ReactiveMongoTransactionManager) {
    //@Autowired
    //lateinit var transactionManager: ReactiveMongoTransactionManager


    @Bean
    fun transactionAdvice(): TransactionInterceptor {
        val rollbackRules = listOf(
            RollbackRuleAttribute(
                Exception::class.java
            )
        )
        val transactionAttribute = RuleBasedTransactionAttribute()
        transactionAttribute.rollbackRules = rollbackRules
        transactionAttribute.setName("*")
        val attributeSource = MatchAlwaysTransactionAttributeSource()
        attributeSource.setTransactionAttribute(transactionAttribute)
        return TransactionInterceptor(tm!!, attributeSource)
    }

    @Bean
    fun transactionAdvisor(): Advisor {
        val pointcut = AspectJExpressionPointcut()
        pointcut.expression = AOP_TRANSACTION_EXPRESSION
        return DefaultPointcutAdvisor(pointcut, transactionAdvice())
    }

    companion object {
        private const val AOP_TRANSACTION_EXPRESSION = "execution(* com.raidea.sampleservice.service.*ServiceIn.*(..))"
    }
}
