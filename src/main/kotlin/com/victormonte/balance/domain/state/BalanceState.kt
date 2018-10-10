package com.victormonte.balance.domain.state

import com.victormonte.balance.domain.event.AmountCredited
import com.victormonte.balance.domain.event.AmountDebited
import com.victormonte.balance.domain.event.Event
import java.io.Serializable

data class BalanceState(var current: Long = 0) : Serializable {

    fun update(e: Event) {
        when(e){
            is AmountCredited -> current += e.amount
            is AmountDebited -> current -= e.amount
            else -> throw Exception("Invalid event domain executed.")
        }
    }

    override fun toString(): String {
        return "BalanceState(current=$current)"
    }


}
