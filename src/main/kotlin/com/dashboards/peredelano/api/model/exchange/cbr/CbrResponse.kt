package com.dashboards.peredelano.api.model.exchange.cbr

import com.fasterxml.jackson.annotation.JsonProperty

data class CbrResponse(

    @JsonProperty("Valute")
    val valute: Valute
)
