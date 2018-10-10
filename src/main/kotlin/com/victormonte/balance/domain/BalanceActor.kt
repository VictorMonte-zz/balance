package com.victormonte.balance.domain

import akka.persistence.AbstractPersistentActor
import akka.persistence.SnapshotOffer
import com.victormonte.balance.domain.command.CreditBalanceCommand
import com.victormonte.balance.domain.command.DebitBalanceCommand
import com.victormonte.balance.domain.command.GetBalanceCommand
import com.victormonte.balance.domain.event.AmountCredited
import com.victormonte.balance.domain.event.AmountDebited
import com.victormonte.balance.domain.state.BalanceState
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.config.ConfigurableBeanFactory
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.util.*
import java.util.logging.Logger


//TODO: Check factory configuration
@Component("balanceActor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
class BalanceActor(id: String) : AbstractPersistentActor(){

    companion object {
        private val logger = LoggerFactory.getLogger(this::class.java.name)
    }

    private val id = id
    private var state: BalanceState = BalanceState()
    private var lastSnapshotDate: LocalDate? = LocalDate.now()

    override fun persistenceId(): String = this.id

    override fun createReceiveRecover(): Receive {
        return receiveBuilder()
                .match(AmountCredited::class.java){
                    state.update(it)
                }
                .match(AmountDebited::class.java){
                    state.update(it)
                }
                .match(SnapshotOffer::class.java){
                    state = it.snapshot() as BalanceState
                }
                .build()
    }

    override fun createReceive(): Receive {
        return receiveBuilder()
                .match(GetBalanceCommand::class.java) {
                    sender().tell(state, self)
                }
                .match(CreditBalanceCommand::class.java){

                    logger.info("Credit ${it.amount} to account ${it.customerId}")

                    val evt = AmountCredited(it.customerId, it.amount)
                    persist(evt) { e: AmountCredited ->
                        state.update(e)
                        context.system.eventStream().publish(e)
                        if(lastSnapshotDate!!.isBefore(LocalDate.now())){
                            logger.debug("Taking a snapshot of state $state for account $id")
                            saveSnapshot(state.copy())
                        }
                    }

                }
                .match(DebitBalanceCommand::class.java){

                    logger.info("Debit ${it.amount} to account ${it.customerId}")

                    val evt = AmountDebited(it.customerId, it.amount)
                    persist(evt) { e: AmountDebited ->
                        state.update(e)
                        context.system.eventStream().publish(e)
                        if(lastSnapshotDate!!.isBefore(LocalDate.now())){
                            logger.debug("Taking a snapshot of state $state for account $id")
                            saveSnapshot(state.copy())
                        }
                    }
                }
                .build()
    }

}