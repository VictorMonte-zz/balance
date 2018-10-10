package com.victormonte.balance.domain.command

data class CreditBalanceCommand(val customerId: Int, val amount: Long) : Command
