package booking

class BookingListView {
    suspend fun onShow() {
        println("【BookingListView】页面出现，获取数据中...")
        val list = DataManager.getBookings()
        println("【BookingListView】共 ${list.size} 条:")
        list.forEach { println(it) }
    }

    suspend fun observe() {
        println("【BookingListView】开始 collect SharedFlow...")
        try {
            DataManager.bookingsFlow.collect { list ->
                println("【BookingListView】收到新数据，共 ${list.size} 条:")
                list.forEach { println(it) }
                println("----------------------")
            }
        } catch (e: Exception) {
            println("【BookingListView】流被异常中断: ${e.message}")
        }
    }
}