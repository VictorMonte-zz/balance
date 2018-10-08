package com.victormonte.balance.domain

import akka.actor.AbstractActor
import com.victormonte.balance.domain.command.GetBalanceCommand
import com.victormonte.balance.domain.state.BalanceState
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

//TODO: Check factory configuration
@Component("balanceActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class BalanceActor(val id: String) : AbstractActor(){

    private var state: BalanceState = BalanceState()

    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(GetBalanceCommand::class.java) {
                    state.current++
                    sender().tell(state, self)
                }
                .build()
    }

}