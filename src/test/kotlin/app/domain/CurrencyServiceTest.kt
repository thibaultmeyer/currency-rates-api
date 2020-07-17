package app.domain

import io.micronaut.test.annotation.MicronautTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CurrencyServiceTest {

    @Inject
    lateinit var currencyService: CurrencyService

    @BeforeAll
    fun beforeAll() {
        val rateUsd = BigDecimal.valueOf(1.14274)
        currencyService.updateCurrencyRate(
            Currency.USD,
            CurrencyRate(CurrencyService.EUR_BASE_CURRENCY_RATE.divide(rateUsd, 10, RoundingMode.HALF_DOWN), rateUsd)
        )
    }

    @Test
    fun noCurrency() {
        // When
        val resultOptional = currencyService.convertCurrency(Currency.EUR, Currency.CZK, BigDecimal.ONE)

        // Then
        Assertions.assertFalse(resultOptional.isPresent)
    }

    @Test
    fun convertEurToUsdFromRateUsd() {
        // When
        val resultOptional = currencyService.convertCurrency(Currency.EUR, Currency.USD, BigDecimal.ONE)

        // Then
        Assertions.assertTrue(resultOptional.isPresent)
        Assertions.assertEquals(BigDecimal.valueOf(1.14274), resultOptional.orElse(null))
    }

    @Test
    fun convertUsdToEurFromRateUsd() {
        // When
        val resultOptional = currencyService.convertCurrency(Currency.USD, Currency.EUR, BigDecimal.ONE)

        // Then
        Assertions.assertTrue(resultOptional.isPresent)
        Assertions.assertEquals(
            CurrencyService.EUR_BASE_CURRENCY_RATE.divide(BigDecimal.valueOf(1.14274), 10, RoundingMode.HALF_DOWN),
            resultOptional.orElse(null)
        )
    }
}
