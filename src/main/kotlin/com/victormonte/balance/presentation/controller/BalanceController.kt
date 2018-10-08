package com.victormonte.balance.presentation.controller

import com.victormonte.balance.application.service.BalanceService
import com.victormonte.balance.domain.command.CreditCommand
import com.victormonte.balance.domain.command.DebitCommand
import com.victormonte.balance.domain.state.BalanceState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
@RequestMapping("balance")
class BalanceController{

    @Autowired
    private lateinit var balanceService: BalanceService

    @GetMapping
    fun get(@RequestHeader customerId: Int) : ResponseEntity<Mono<BalanceState>>{
        val result = balanceService.get(customerId)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping("/credit")
    fun credit(@RequestBody command: CreditCommand): ResponseEntity<Void> {
        balanceService.credit(command)
        return ResponseEntity(HttpStatus.OK)
    }

    @PostMapping("/debit")
    fun credit(@RequestBody command: DebitCommand): ResponseEntity<Void> {
        balanceService.debit(command)
        return ResponseEntity(HttpStatus.OK)
    }

}