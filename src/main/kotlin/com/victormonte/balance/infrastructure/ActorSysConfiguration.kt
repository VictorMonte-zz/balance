package com.victormonte.balance.infrastructure

import akka.actor.ActorSystem
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan
open class ActorSysConfiguration {

    @Autowired
    private lateinit var applicationContext: ApplicationContext

    @Bean
    open fun actorSystem(): ActorSystem {
        val system: ActorSystem = ActorSystem.create("balanceSystem")
        SpringExtension.SPRING_EXTENSION_PROVIDER.get(system).initialize(applicationContext)
        return system
    }
}