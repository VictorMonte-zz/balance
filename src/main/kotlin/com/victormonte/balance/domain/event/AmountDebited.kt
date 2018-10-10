package com.victormonte.balance.domain.event

data class AmountDebited(override val customerId: Int, override val amount: Long) : Event(customerId, amount)