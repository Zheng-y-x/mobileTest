package booking

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object DataManager {
    private val flowInternal = MutableSharedFlow<List<Booking>>(replay = 1)
    val bookingsFlow = flowInternal.asSharedFlow()

    suspend fun refresh() = getBookings(forceRefresh = true)

    suspend fun getBookings(forceRefresh: Boolean = false): List<Booking> {
        val cached = LocalCache.get()
        val need = forceRefresh || cached == null || isExpired()
        if (!need) {
            cached?.let { flowInternal.emit(it) }
            return cached!!
        }
        // 先给旧数据
        cached?.let { flowInternal.emit(it) }
        return try {
            val fresh = BookingService.fetch()
            // 根据shipToken合并去重
            val merged = (cached!! + fresh).distinctBy { it.shipToken }
            LocalCache.put(merged)
            flowInternal.emit(merged)
            merged
        } catch (e: Exception) {
            System.err.println("【DataManager】刷新失败: ${e.message}")
            if (cached != null) {
                flowInternal.emit(cached)
                cached
            } else throw e
        }
    }

    private fun isExpired(): Boolean {
        val f = LocalCache.file
        if (!f.exists()) return true
        val last = f.lastModified()
        val now = System.currentTimeMillis()
        return (now - last) >= 5 * 60 * 1000L   // 5 分钟
    }
}