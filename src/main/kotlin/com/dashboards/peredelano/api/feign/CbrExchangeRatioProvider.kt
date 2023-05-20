package com.dashboards.peredelano.api.feign

import com.dashboards.peredelano.api.model.exchange.cbr.CbrResponse
import com.dashboards.peredelano.api.model.exchange.cbr.Valute
import com.dashboards.peredelano.web.dto.exchange.ExchangeRatio
import com.dashboards.peredelano.web.dto.exchange.Currency
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.math.BigDecimal
import java.time.LocalDate

/**
 * Компонент позволяет получать курс обмена из ЦБР
 */
@Component
class CbrExchangeRatioProvider(
    private val cbrRestTemplate: RestTemplate
) {
    // Подобие кеша. в ЦБР курс валют обновляется раз в день. нет смысла каждый раз стучаться в ЦБР.
    val cbrDailyExchange = mutableMapOf<LocalDate, Collection<ExchangeRatio>>()

    /**
     * Получение Курса обмена.
     */
    fun getCbrExchangeRatios(): Collection<ExchangeRatio> {
        val now = LocalDate.now()
        return cbrDailyExchange[now] ?: getDailyExchangeAndAddToCache(now)
    }

    /**
     * Заполнение кеша данными из ЦБР
     */
    private fun getDailyExchangeAndAddToCache(
        today: LocalDate
    ): Collection<ExchangeRatio> {
        val cbrDaily = getDailyRatios().body
            ?.let { computeExchangeRatios(it.valute) }
            ?: emptyList()
        cbrDailyExchange.clear()
        cbrDailyExchange[today] = cbrDaily
        return cbrDaily
    }

    /**
     * Получение дневного курса обмена из ЦБР
     */
    private fun getDailyRatios() = cbrRestTemplate.getForEntity("https://www.cbr-xml-daily.ru/daily_json.js", CbrResponse::class.java)

    private fun computeExchangeRatios(
        dailyExchanges: Valute
    ): Collection<ExchangeRatio> {
        val usdToRubRatio = dailyExchanges.usd.value.divide(dailyExchanges.usd.nominal.toBigDecimal())
        val result = mutableListOf(getOneUsdChangeRatio(Currency.RUB, usdToRubRatio))

        val usdToGelRatio = usdToRubRatio / dailyExchanges.gel.value
        result.add(getOneUsdChangeRatio(Currency.GEL, usdToGelRatio.multiply(dailyExchanges.gel.nominal.toBigDecimal())))

        val usdToEurRatio = usdToRubRatio / dailyExchanges.eur.value
        result.add(getOneUsdChangeRatio(Currency.EUR, usdToEurRatio.multiply(dailyExchanges.eur.nominal.toBigDecimal())))

        return result
    }

    /**
     * Получение курса обмена одного доллара к другой валюте
     */
    private fun getOneUsdChangeRatio(
        currencyTo: Currency,
        amountTo: BigDecimal
    ) = ExchangeRatio(
        currencyFrom = Currency.USD,
        amountFrom = BigDecimal.ONE,
        currencyTo = currencyTo,
        amountTo = amountTo
    )
}