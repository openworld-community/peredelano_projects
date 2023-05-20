package com.dashboards.peredelano.api.model.exchange

import java.math.BigDecimal

data class ExchageCryptoResponse(
    val rates: Map<String, BigDecimal>
)
