package app.domain.entity

import java.math.BigDecimal

/**
 * Currency rate.
 *
 * @param rateToBase rate to convert to base
 * @param rateFromBase rate to convert from base
 */
class CurrencyRate(val rateToBase: BigDecimal,
                   val rateFromBase: BigDecimal)
