package com.victormonte.balance.domain.command

data class DebitBalanceCommand(val amount: Long, val customerId: Int) : Command
