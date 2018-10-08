package com.victormonte.balance.presentation.controller

import com.victormonte.balance.application.service.BalanceService
import com.victormonte.balance.domain.state.BalanceState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class BalanceController{

    @Autowired
    private lateinit var balanceService: BalanceService

    @GetMapping("/customer/{id}/balance")
    fun get(@PathVariable id: Int) : ResponseEntity<Mono<BalanceState>>{
        var result = balanceService.get(id)
        return ResponseEntity(result, HttpStatus.OK)
    }

}