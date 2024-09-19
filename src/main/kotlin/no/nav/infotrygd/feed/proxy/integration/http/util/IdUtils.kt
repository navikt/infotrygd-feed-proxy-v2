package no.nav.infotrygd.feed.proxy.integration.http.util

object IdUtils {
    @JvmStatic fun generateId(): String {
        val uuid = java.util.UUID.randomUUID()
        return java.lang.Long.toHexString(uuid.mostSignificantBits) + java.lang.Long.toHexString(uuid.leastSignificantBits)
    }
}
