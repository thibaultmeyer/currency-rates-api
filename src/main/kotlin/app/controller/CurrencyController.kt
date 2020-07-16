package app.controller

import app.domain.Currency
import app.domain.CurrencyService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.validation.Validated
import java.math.BigDecimal
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotNull

/**
 * Expose endpoints to manipulate currencies.
 */
@Controller("/currency")
@Validated
class CurrencyController(private val currencyService: CurrencyService) {

    /**
     * Show all available currencies.
     *
     * @return an array of currencies
     */
    @Get("/")
    fun listAllCurrencies(): Array<Currency> {
        return Currency.values()
    }

    /**
     * Retrieve all conversion rate from a given base currency to others available currencies.
     *
     * @param baseCurrency base currency (ex: EUR)
     * @return a list of conversion rate
     */
    @Get("/{baseCurrency}")
    fun currencyRate(@NotNull baseCurrency: Currency): String {
        return baseCurrency.name
    }

    /**
     * Convert currency to another.
     *
     * @param fromCurrency from currency (ex: EUR)
     * @param targetCurrency target currency (ex: USD)
     * @param value value to convert from currency to target currency (ex: 123.45)
     * @return the converted value
     */
    @Get("/{fromCurrency}/{targetCurrency}/{value}")
    fun convertCurrency(@NotNull fromCurrency: Currency,
                        @NotNull targetCurrency: Currency,
                        @Min(0) value: BigDecimal): Optional<BigDecimal> {
        return currencyService.convertCurrency(fromCurrency, targetCurrency, value)
    }
}
