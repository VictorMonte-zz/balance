package com.victormonte.balance.presentation.controller

import com.victormonte.balance.application.service.BalanceService
import com.victormonte.balance.domain.command.CreditCommand
import com.victormonte.balance.domain.state.BalanceState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono

@RestController
class BalanceController{

    @Autowired
    private lateinit var balanceService: BalanceService

    @GetMapping("/customer/{id}/balance")
    fun get(@PathVariable id: Int) : ResponseEntity<Mono<BalanceState>>{
        val result = balanceService.get(id)
        return ResponseEntity(result, HttpStatus.OK)
    }

    @PostMapping("/balance/credit")
    fun credit(@RequestBody command: CreditCommand): ResponseEntity<Void> {
        balanceService.credit(command)
        return ResponseEntity(HttpStatus.OK)
    }

}