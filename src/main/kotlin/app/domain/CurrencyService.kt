package app.domain

import app.entity.ExchangeInformationEntity
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
                        value: BigDecimal): Optional<ExchangeInformationEntity> {
        val rateToBase = currencyRateMap[fromCurrency]?.rateToBase ?: return Optional.empty()
        val currencyRateTo = currencyRateMap[targetCurrency]?.rateFromBase ?: return Optional.empty()
        val valueInEUR = rateToBase * value

        return Optional.of(ExchangeInformationEntity(fromCurrency, value, targetCurrency, valueInEUR * currencyRateTo))
    }

    /**
     * Convert currency to  all other available currencies.
     *
     * @param fromCurrency from currency (ex: EUR)
     * @param value value to convert (ex: 123.45)
     * @return a list with values converter in named currency
     */
    fun convertCurrencyToAll(fromCurrency: Currency,
                             value: BigDecimal): List<ExchangeInformationEntity> {
        val results = mutableListOf<ExchangeInformationEntity>()

        Currency.values().iterator().forEach { toCurrency ->
            if (fromCurrency != toCurrency) {
                convertCurrency(fromCurrency, toCurrency, value).ifPresent { exchangeInformation ->
                    results.add(exchangeInformation)
                }
            }
        }

        return results
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
