package no.nav.infotrygd.feed.proxy.integration.http.sts

class StsAccessTokenFeilException(
    message: String,
    cause: Throwable? = null,
) : RuntimeException(message, cause)
