package com.dashboards.peredelano.service.exchange

import com.dashboards.peredelano.api.model.exchange.ExchageCryptoResponse
import com.dashboards.peredelano.web.dto.exchange.Currency
import com.dashboards.peredelano.web.dto.exchange.ExchangeRatio
import com.dashboards.peredelano.web.dto.exchange.ExchangeRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal

@Service
class ExchangeService(
    private val exchangeRatioRestTemplate: RestTemplate
) {

    fun getAvailableCurrencies() = Currency.values()

    /**
     * Получение Курса обмена из exchangerate.com.
     */
    fun getExchange(request: ExchangeRequest): Collection<ExchangeRatio> {
        val containUSDT = request.currenciesTo.contains(Currency.USDT)
        val currenciesWithoutUSDT = request.currenciesTo.filter { it != Currency.USDT }
        val result = getExchangeWithoutUSDT(
            currenciesWithoutUSDT,
            request.amountFrom,
            request.currencyFrom
        )
        if (containUSDT) {
            result.add(
                ExchangeRatio(
                    currencyFrom = request.currencyFrom,
                    amountFrom = request.amountFrom,
                    currencyTo = Currency.USDT,
                    amountTo = request.amountFrom
                )
            )
        }
        return result
    }

    private fun getExchangeWithoutUSDT(
        currenciesWithoutUSDT : List<Currency>,
        amount: BigDecimal,
        base: Currency
    ): MutableList<ExchangeRatio> {
        if (currenciesWithoutUSDT.isEmpty()) {
            return mutableListOf()
        }
        val symbols = currenciesWithoutUSDT.joinToString(",")
        val uri = "https://api.exchangerate.host/latest?base=$base&symbols=$symbols&amount=$amount"
        val rates = exchangeRatioRestTemplate.getForEntity(uri, ExchageCryptoResponse::class.java)
        return rates?.body
            ?.rates
            ?.mapNotNull { rate -> ExchangeRatio(
                currencyFrom = base,
                amountFrom = amount,
                currencyTo = Currency.valueOf(rate.key),
                amountTo = rate.value
            ) }?.toMutableList()
            ?: mutableListOf()
    }
}
