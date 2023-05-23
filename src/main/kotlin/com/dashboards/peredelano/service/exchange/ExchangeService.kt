package com.dashboards.peredelano.service.exchange

import com.dashboards.peredelano.api.model.exchange.ExchageResponse
import com.dashboards.peredelano.web.dto.exchange.Currency
import com.dashboards.peredelano.web.dto.exchange.ExchangeRatio
import com.dashboards.peredelano.web.dto.exchange.ExchangeRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

/**
 * Сервис конвертации валют.
 */
@Service
class ExchangeService(
    private val exchangeRatioRestTemplate: RestTemplate
) {

    fun getAvailableCurrencies() = Currency.values()

    /**
     * Получение Курса обмена из exchangerate.com.
     */
    fun getExchange(request: ExchangeRequest): Collection<ExchangeRatio> {
        return getRates(
            request.currenciesTo.map { getExchangeName(it) },
            request.amountFrom,
            request.currencyFrom
        )?.let { rates ->
            request.currenciesTo.mapNotNull { currencyRate ->
                getExchangeRatio(request, currencyRate, rates)
            }
        } ?: emptyList()
    }

    private fun getRates(
        currenciesNameAtExchange: List<String>,
        amount: BigDecimal,
        currency: Currency
    ): Map<String, BigDecimal>? {
        if (currenciesNameAtExchange.isEmpty()) {
            return emptyMap()
        }
        val base = getExchangeName(currency)
        val symbols = currenciesNameAtExchange
            .toSet()
            .joinToString(",")
        val uri = "https://api.exchangerate.host/latest?base=$base&symbols=$symbols&amount=$amount"
        val rates = exchangeRatioRestTemplate.getForEntity(uri, ExchageResponse::class.java)
        return rates.body?.rates
    }

    private fun getExchangeRatio(
        request: ExchangeRequest,
        currency: Currency,
        rates: Map<String, BigDecimal>
    ): ExchangeRatio? {
        val name = getExchangeName(currency)
        return rates[name]
            ?.let {
                ExchangeRatio(
                    currencyFrom = request.currencyFrom,
                    amountFrom = request.amountFrom,
                    currencyTo = currency,
                    amountTo = it
                )
            }
    }

    private fun getExchangeName(currency: Currency): String {
        if (currency == Currency.USDT) {
            return Currency.USD.toString()
        }
        return currency.toString()
    }
}
