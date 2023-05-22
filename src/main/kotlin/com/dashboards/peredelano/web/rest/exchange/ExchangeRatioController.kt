package com.dashboards.peredelano.web.rest.exchange

import com.dashboards.peredelano.service.exchange.ExchangeService
import com.dashboards.peredelano.web.dto.exchange.ExchangeRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Контроллер веб запросов для курсов валют.
 */
@RestController
@RequestMapping("/exchange")
class ExchangeRatioController(
    private val exchangeService: ExchangeService
) {

    @GetMapping("/currency/available")
    fun getExchange() = exchangeService.getAvailableCurrencies()

    @PostMapping("/ratio")
    fun getExchange(
        @RequestBody
        request: ExchangeRequest
    ) = exchangeService.getExchange(request)
}