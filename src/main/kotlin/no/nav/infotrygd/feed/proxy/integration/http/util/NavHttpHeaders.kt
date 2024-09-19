package no.nav.infotrygd.feed.proxy.integration.http.util

enum class NavHttpHeaders(
    private val header: String,
) {
    NAV_CALL_ID("Nav-Call-Id"),
    NGNINX_REQUEST_ID("X-Request-Id"),
    NAV_CONSUMER_ID("Nav-Consumer-Id"),
    ;

    fun asString(): String = header
}
