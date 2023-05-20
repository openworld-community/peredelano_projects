package com.dashboards.peredelano.web.dto.exchange

import java.math.BigDecimal

data class ExchangeRequest(
    /**
     * Конвертируемая валюта.
     */
    val currencyFrom: Currency,
    /**
     * Количество конвертируемой валюты
     */
    val amountFrom: BigDecimal,
    /**
     * Валюты в которую конвертируем.
     */
    val currenciesTo: List<Currency>
)
