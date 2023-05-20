package com.dashboards.peredelano.web.dto.exchange

import java.math.BigDecimal

data class ExchangeRatio(
    /**
     * Конвертируемая валюта.
     */
    val currencyFrom: Currency,
    /**
     * Количество конвертируемой валюты
     */
    val amountFrom: BigDecimal,
    /**
     * Валюта в которую конвертируем.
     */
    val currencyTo: Currency,
    /**
     * Количество валюты которое получится после конвертации.
     */
    val amountTo: BigDecimal
)
