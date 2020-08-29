package app.domain.entity

import app.domain.enumeration.Currency
import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal

/**
 * Exchange information entity.
 *
 * @param fromCurrency from currency (ex: EUR)
 * @param fromValue value to convert (ex: 123.45)
 * @param toCurrency to currency (ex: USD)
 * @param toValue value converted (ex: 90.15)
 */
@Introspected
class ExchangeInformationEntity(val fromCurrency: Currency,
                                val fromValue: BigDecimal,
                                val toCurrency: Currency,
                                val toValue: BigDecimal)
