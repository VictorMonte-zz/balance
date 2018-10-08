package com.victormonte.balance.infrastructure

import akka.actor.AbstractExtensionId
import akka.actor.ExtendedActorSystem
import akka.actor.Extension
import akka.actor.Props
import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware

class SpringExtension : AbstractExtensionId<SpringExt>(), ApplicationContextAware {

    companion object {
        val SPRING_EXTENSION_PROVIDER = SpringExtension()
    }
    
    private var applicationContext: ApplicationContext? = null

    override fun createExtension(system: ExtendedActorSystem): SpringExt {
        return SpringExt()
    }

    @Throws(BeansException::class)
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }
}

class SpringExt : Extension {

    @Volatile
    private var applicationContext: ApplicationContext? = null

    fun initialize(applicationContext: ApplicationContext) {
        this.applicationContext = applicationContext
    }

    fun props(actorBeanName: String, accountId: String): Props {
        return Props.create(SpringActorProducer::class.java, applicationContext, actorBeanName, accountId)
    }

    fun props(actorBeanName: String): Props {
        return Props.create(SpringActorProducer2::class.java, applicationContext, actorBeanName)
    }
}