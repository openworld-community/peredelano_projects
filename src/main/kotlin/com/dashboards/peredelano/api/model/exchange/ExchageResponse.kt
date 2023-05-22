package com.dashboards.peredelano.api.model.exchange

import java.math.BigDecimal

data class ExchageResponse(
    val rates: Map<String, BigDecimal>
)
