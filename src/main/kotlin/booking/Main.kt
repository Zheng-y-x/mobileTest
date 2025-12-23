package booking

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


fun main() = runBlocking {
    val view = BookingListView()

    // 启动观察协程
    launch { view.observe() }

    delay(200)   // 给 collect 一点注册时间，打印更整齐

    // 第一次触发 → 无缓存 → 拉新
    println(">>> 第一次 onShow（强制刷新）")
    DataManager.refresh()

    println(">>> 等 10 秒（模拟 5 分钟）后再次刷新")
    delay(10_000)
    DataManager.refresh()

    delay(10_000)
    println(">>> 10 秒，程序结束")

}