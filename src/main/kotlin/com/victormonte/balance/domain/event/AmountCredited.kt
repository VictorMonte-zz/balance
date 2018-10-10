package com.victormonte.balance.domain.event

data class AmountCredited(override val customerId: Int, override val amount: Long) : Event(customerId, amount)