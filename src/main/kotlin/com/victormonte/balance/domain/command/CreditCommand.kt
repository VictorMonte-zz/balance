package com.victormonte.balance.domain.command

data class CreditCommand(val customerId: Int, val amount: Long)
