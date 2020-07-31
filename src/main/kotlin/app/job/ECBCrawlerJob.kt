package app.job

import app.domain.Currency
import app.domain.CurrencyRate
import app.domain.CurrencyService
import app.domain.CurrencyService.Companion.EUR_BASE_CURRENCY_RATE
import io.micronaut.scheduling.annotation.Scheduled
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import java.io.BufferedInputStream
import java.io.FileNotFoundException
import java.io.InputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.net.ConnectException
import java.net.URL
import java.net.URLConnection
import java.net.UnknownHostException
import javax.inject.Singleton
import javax.xml.parsers.DocumentBuilderFactory

/**
 * This job is in charge crawl currency exchange rates from the European Central Bank.
 */
@Singleton
class ECBCrawlerJob(private val currencyService: CurrencyService) {

    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    @Scheduled(initialDelay = "2s")
    fun loadCurrencyRates() = refreshCurrencyRates()

    @Scheduled(cron = "0 2 * * *")
    fun refreshCurrencyRates() {
        logger.info("Refresh currency exchange rate from European Central Bank")
        try {
            val url = URL("https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml")
            val urlConnection: URLConnection = url.openConnection()
            val inputStream: InputStream = BufferedInputStream(urlConnection.getInputStream())
            val dbFactory = DocumentBuilderFactory.newInstance()
            val dBuilder = dbFactory.newDocumentBuilder()
            val doc: Document = dBuilder.parse(inputStream)

            val nodes = doc.getElementsByTagName("Cube")
            for (idx in 0 until nodes.length) {
                val node = nodes.item(idx)

                if (node.attributes.getNamedItem("currency") != null) {
                    val currency = Currency.valueOf(node.attributes.getNamedItem("currency").nodeValue)
                    val rate = BigDecimal(node.attributes.getNamedItem("rate").nodeValue)

                    val currencyRate = CurrencyRate(EUR_BASE_CURRENCY_RATE.divide(rate, 10, RoundingMode.HALF_DOWN), rate)
                    currencyService.updateCurrencyRate(currency, currencyRate)
                }
            }
        } catch (ex: ConnectException) {
            logger.error("Cannot refresh currency exchange rate from European Central Bank: {}", ex.message)
        } catch (_: FileNotFoundException) {
            logger.error("Cannot refresh currency exchange rate from European Central Bank: 404 File Not Found")
        } catch (ex: UnknownHostException) {
            logger.error("Cannot refresh currency exchange rate from European Central Bank: Unknown Host {}", ex.message)
        }
    }
}
