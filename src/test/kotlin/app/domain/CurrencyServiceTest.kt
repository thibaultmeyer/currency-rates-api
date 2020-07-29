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
        val rateUsd: BigDecimal = BigDecimal.valueOf(1.14274)
        currencyService.updateCurrencyRate(
            Currency.USD,
            CurrencyRate(CurrencyService.EUR_BASE_CURRENCY_RATE.divide(rateUsd, 10, RoundingMode.HALF_DOWN), rateUsd)
        )

        val rateAud: BigDecimal = BigDecimal.valueOf(1.6348)
        currencyService.updateCurrencyRate(
            Currency.AUD,
            CurrencyRate(CurrencyService.EUR_BASE_CURRENCY_RATE.divide(rateAud, 10, RoundingMode.HALF_DOWN), rateAud)
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
        val valueOneEurToUsdExpected = BigDecimal.valueOf(1.14274)

        Assertions.assertTrue(resultOptional.isPresent)
        Assertions.assertEquals(valueOneEurToUsdExpected, resultOptional.get().toValue)
    }

    @Test
    fun convertUsdToEurFromRateUsd() {
        // When
        val resultOptional = currencyService.convertCurrency(Currency.USD, Currency.EUR, BigDecimal.ONE)

        // Then
        val valueOneUsdToEurExpected = BigDecimal.valueOf(0.8750896967)

        Assertions.assertTrue(resultOptional.isPresent)
        Assertions.assertEquals(valueOneUsdToEurExpected, resultOptional.get().toValue)
    }

    @Test
    fun convertEurToAllCurrencies() {
        // When
        val resultList = currencyService.convertCurrencyToAll(Currency.EUR, BigDecimal.ONE)

        // Then
        val valueOneEurToAudExpected = BigDecimal.valueOf(1.6348)
        val valueOneEurToUsdExpected = BigDecimal.valueOf(1.14274)

        Assertions.assertTrue(resultList.isNotEmpty())
        Assertions.assertEquals(valueOneEurToAudExpected, resultList[0].toValue)
        Assertions.assertEquals(valueOneEurToUsdExpected, resultList[1].toValue)
    }
}
