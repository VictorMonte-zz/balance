package com.victormonte.balance.application.service

import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.pattern.Patterns.ask
import com.victormonte.balance.domain.command.CreditCommand
import com.victormonte.balance.domain.command.DebitCommand
import com.victormonte.balance.domain.command.GetBalanceActorCommand
import com.victormonte.balance.domain.command.GetBalanceCommand
import com.victormonte.balance.domain.state.BalanceState
import com.victormonte.balance.infrastructure.SpringExtension
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import scala.compat.java8.FutureConverters.toJava
import scala.concurrent.Future

@Component
class BalanceService(system: ActorSystem) {

    //TODO: Try with Autowired
    //TODO: Check mov to constructor
    //TODO: Always create a supervisor?
    // Supervisor Akka Actor
    private val supervisor: ActorRef =
            system.actorOf(
                    SpringExtension.SPRING_EXTENSION_PROVIDER.get(system).props("balanceActorSupervisor")
                    , "balanceActorSupervisor")

    fun get(id: Int) : Mono<BalanceState> {

        val actorRef: ActorRef = getBalanceActor(id)
        val command = GetBalanceCommand(id)

        val result = ask(actorRef, command, 3000)

        return toMono(result)
    }

    fun credit(command: CreditCommand) {
        val actorRef = getBalanceActor(command.customerId)
        ask(actorRef, command, 3000)
    }

    fun debit(command: DebitCommand) {
        val actorRef = getBalanceActor(command.customerId)
        ask(actorRef, command, 3000)
    }

    private fun toMono(future: Future<Any>?): Mono<BalanceState> {

        val completableFuture = toJava(future)
                .toCompletableFuture()
                .thenApplyAsync { obj -> obj as BalanceState }

        return Mono.fromFuture(completableFuture)
    }

    private fun getBalanceActor(id: Int): ActorRef {

        val command = GetBalanceActorCommand(id)

        val result = ask(supervisor, command, 3000)

        return toJava(result).toCompletableFuture().get() as ActorRef
    }

}
