package booking

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.time.Instant
import java.time.temporal.ChronoUnit

object LocalCache {
    internal val file = File(System.getProperty("java.io.tmpdir"), "booking_cache.json")
    private var memory: Pair<List<Booking>, Instant>? = null
    private const val VALID_MIN = 5L

    fun get(): List<Booking>? {
        memory?.let { (list, time) ->
            if (time.until(Instant.now(), ChronoUnit.MINUTES) < VALID_MIN) return list
        }
        if (!file.exists()) return null
        return try {
            val list = Json.decodeFromString<List<Booking>>(file.readText())
            memory = list to Instant.now()
            list
        } catch (e: Exception) {
            null
        }
    }

    fun put(list: List<Booking>) {
        memory = list to Instant.now()
        file.writeText(Json.encodeToString(list))
    }
}