package com.victormonte.balance.infrastructure

import akka.actor.Actor
import akka.actor.IndirectActorProducer
import org.springframework.context.ApplicationContext

class SpringActorProducer(private val applicationContext: ApplicationContext,
                          private val beanActorName: String,
                          private val id: String) : IndirectActorProducer {

    override fun produce(): Actor {
        return applicationContext.getBean(beanActorName, id) as Actor
    }

    override fun actorClass(): Class<out Actor> {
        return applicationContext
                .getType(beanActorName) as Class<out Actor>
    }
}


class SpringActorProducer2(private val applicationContext: ApplicationContext,
                          private val beanActorName: String) : IndirectActorProducer {

    override fun produce(): Actor {
        return applicationContext.getBean(beanActorName) as Actor
    }

    override fun actorClass(): Class<out Actor> {
        return applicationContext
                .getType(beanActorName) as Class<out Actor>
    }
}
