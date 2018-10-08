package com.victormonte.balance.domain

import akka.actor.AbstractActor
import akka.actor.ActorNotFound
import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.victormonte.balance.domain.command.GetBalanceActorCommand
import com.victormonte.balance.infrastructure.SpringExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import scala.concurrent.Await
import scala.concurrent.duration.FiniteDuration
import java.util.concurrent.TimeUnit

@Component("balanceActorSupervisor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class BalanceActorSupervisor : AbstractActor() {

    @Autowired
    private lateinit var system: ActorSystem
    private val duration = FiniteDuration.create(LENGTH, TimeUnit.SECONDS)

    companion object {
        private const val LENGTH: Long = 10
    }


    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(GetBalanceActorCommand::class.java) {

                    val actorRef: ActorRef? = findBalanceActor(it) ?: createBalanceActor(it)

                    sender.tell(actorRef as ActorRef, self)

                }.build()
    }

    private fun findBalanceActor(command: GetBalanceActorCommand?): ActorRef? {

        return try {

            val id = command!!.id.toString()
            val path = "/user/balanceActorSupervisor/$id"
            val selectActor = context.actorSelection(path).resolveOne(duration)

            Await.result(selectActor, duration)

        } catch (e: ActorNotFound) {
            null
        }
    }

    fun createBalanceActor(command: GetBalanceActorCommand): ActorRef? {

        val actorSystem = SpringExtension.SPRING_EXTENSION_PROVIDER.get(system)
        val id = command.id.toString()
        val props = actorSystem.props("balanceActor", id)

        return context.actorOf(props, id)
    }


}