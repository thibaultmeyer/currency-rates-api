package app.domain

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.math.BigDecimal
import java.util.*
import javax.inject.Singleton

/**
 * This service expose all currency related methods.
 */
@Singleton
class CurrencyService {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    private val currencyRateMap = mutableMapOf(
        Pair(Currency.EUR, CurrencyRate(rateFromBase = EUR_BASE_CURRENCY_RATE, rateToBase = EUR_BASE_CURRENCY_RATE))
    )

    /**
     * Convert currency to another.
     *
     * @param fromCurrency from currency (ex: EUR)
     * @param targetCurrency target currency (ex: USD)
     * @param value value to convert from currency to target currency (ex: 123.45)
     * @return the converted value
     */
    fun convertCurrency(fromCurrency: Currency,
                        targetCurrency: Currency,
                        value: BigDecimal): Optional<BigDecimal> {
        val rateToBase = currencyRateMap[fromCurrency]?.rateToBase ?: return Optional.empty()
        val currencyRateTo = currencyRateMap[targetCurrency]?.rateFromBase ?: return Optional.empty()
        val valueInEUR = rateToBase * value

        return Optional.of(valueInEUR * currencyRateTo)
    }

    /**
     * Update a single currency rate.
     *
     * @param currency the currency to update
     * @param currencyRate rate information
     */
    fun updateCurrencyRate(currency: Currency,
                           currencyRate: CurrencyRate) {
        val oldConversionRate = this.currencyRateMap[currency]?.rateFromBase ?: "<None>"
        this.currencyRateMap[currency] = currencyRate

        logger.info("Currency {} conversion rate changed from {} to {}",
            currency.name,
            oldConversionRate,
            currencyRate.rateFromBase
        )
    }

    companion object {

        /**
         * Base currency rate (EUR).
         */
        val EUR_BASE_CURRENCY_RATE: BigDecimal = BigDecimal.ONE
    }
}
