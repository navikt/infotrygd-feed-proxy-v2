package no.nav.infotrygd.feed.proxy.integration.http.klient

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.infotrygd.feed.proxy.integration.http.mapper.objectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.*
import java.net.URI

/**
 * Abstract klasse for å kalle rest-tjenester med metrics og utpakking av ev. body.
 */
abstract class AbstractRestClient(
    val operations: RestOperations,
) {
    private val secureLogger: Logger = LoggerFactory.getLogger("secureLogger")
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    inline fun <reified T : Any> getForEntity(
        uri: URI,
        httpHeaders: HttpHeaders? = null,
    ): T = execute(uri) { operations.exchange<T>(uri, HttpMethod.GET, HttpEntity(null, httpHeaders)) }

    inline fun <reified T : Any, reified U : Any> postForEntity(
        uri: URI,
        httpHeaders: HttpHeaders? = null,
        requestBody: U,
    ): T = execute(uri) { operations.exchange<T>(uri, HttpMethod.POST, HttpEntity(requestBody, httpHeaders)) }

    inline fun <reified T : Any, reified U : Any> patchForEntity(
        uri: URI,
        httpHeaders: HttpHeaders? = null,
        requestBody: U,
    ): T = execute(uri) { operations.exchange<T>(uri, HttpMethod.PATCH,
        HttpEntity(requestBody, httpHeaders)) }

    @Suppress("UNCHECKED_CAST")
    private fun <T> validerOgPakkUt(
        respons: ResponseEntity<T>,
        uri: URI,
    ): T {
        if (!respons.statusCode.is2xxSuccessful) {
            secureLogger.info("Kall mot $uri feilet:  ${respons.body}")
            log.info("Kall mot $uri feilet: ${respons.statusCode}")
            throw HttpServerErrorException(respons.statusCode, "", respons.body?.toString()?.toByteArray(), Charsets.UTF_8)
        }
        return respons.body as T
    }

    fun <T> execute(
        uri: URI,
        function: () -> ResponseEntity<T>,
    ): T {
        try {
            val responseEntity = function.invoke()
            return validerOgPakkUt(responseEntity, uri)
        } catch (e: RestClientResponseException) {
            secureLogger.warn("RestClientResponseException ved kall mot uri=$uri", e)
            lesRessurs(e)?.let { throw RessursException(it, e) } ?: throw e
        } catch (e: HttpClientErrorException) {
            secureLogger.warn("HttpClientErrorException ved kall mot uri=$uri", e)
            lesRessurs(e)?.let { throw RessursException(it, e) } ?: throw e
        } catch (e: Exception) {
            secureLogger.warn("Feil ved kall mot uri=$uri", e)
            throw RuntimeException("Feil ved kall mot uri=$uri", e)
        }
    }

    private fun lesRessurs(e: RestClientResponseException): Ressurs<Any>? =
        try {
            if (e.responseBodyAsString.contains("status")) {
                objectMapper.readValue<Ressurs<Any>>(e.responseBodyAsString)
            } else {
                null
            }
        } catch (ex: Exception) {
            null
        }

    override fun toString(): String = this::class.simpleName + " [operations=" + operations + "]"
}
