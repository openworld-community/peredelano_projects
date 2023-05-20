package com.dashboards.peredelano.api.model.exchange.cbr

import com.fasterxml.jackson.annotation.JsonProperty

data class Valute(

    @JsonProperty("GEL")
    val gel: CurrencyModel,

    @JsonProperty("USD")
    val usd: CurrencyModel,

    @JsonProperty("EUR")
    val eur: CurrencyModel
)