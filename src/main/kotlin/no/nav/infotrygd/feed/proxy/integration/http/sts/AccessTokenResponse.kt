package no.nav.infotrygd.feed.proxy.integration.http.sts

class AccessTokenResponse(
    val access_token: String,
    val token_type: String,
    val expires_in: Long,
)
