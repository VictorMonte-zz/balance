package com.victormonte.balance.domain.event

import java.io.Serializable

open class Event(open val customerId: Int, open val amount: Long) : Serializable