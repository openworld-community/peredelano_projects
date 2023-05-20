package com.dashboards.peredelano.api.model.exchange.cbr

import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CurrencyModel(

    @JsonProperty("ID")
    val id: String,

    /**
     * Сколько рублей можно получить за nominal единиц валюты.
     */
    @JsonProperty("Value")
    val value: BigDecimal,

    /**
     * Код валюты. Должен маппится в [com.dashboards.peredelano.web.dto.exchange.Currency].
     */
    @JsonProperty("CharCode")
    val charCode: String,

    /**
     * Сколько единиц валюты конвертируется, чтобы получилось value.
     */
    @JsonProperty("Nominal")
    val nominal: Int,
)