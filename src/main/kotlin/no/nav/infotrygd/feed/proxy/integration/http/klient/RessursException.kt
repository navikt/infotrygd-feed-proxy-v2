package no.nav.infotrygd.feed.proxy.integration.http.klient

import org.springframework.http.HttpStatus
import org.springframework.web.client.RestClientResponseException

class RessursException(
    val ressurs: Ressurs<Any>,
    cause: RestClientResponseException,
    val httpStatus: HttpStatus = HttpStatus.valueOf(cause.statusCode.value()),
) : RuntimeException(cause)
