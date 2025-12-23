package booking

import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json



object BookingService {

    suspend fun fetch(): List<Booking> {
        delay(800)

        val json = javaClass.getResourceAsStream("/booking.json")
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: error("booking.json not found")
        return try {
            val response = Json.decodeFromString<Booking>(json)   // 单个对象
            listOf(response)
        } catch (e: Exception) {
            println("JSON 格式不对: ${e.message}")
            emptyList()
        }
    }
}