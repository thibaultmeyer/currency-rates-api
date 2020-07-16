package app.domain

/**
 * Currency rate.
 *
 * @param rateToBase rate to convert to base
 * @param rateFromBase rate to convert from base
 */
class CurrencyRate(val rateToBase: Double,
                   val rateFromBase: Double)
