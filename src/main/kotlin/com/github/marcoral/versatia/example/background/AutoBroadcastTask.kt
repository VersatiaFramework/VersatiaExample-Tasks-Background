package com.github.marcoral.versatia.example.background

import com.github.marcoral.versatia.VersatiaEngine
import com.github.marcoral.versatia.broadcast
import com.github.marcoral.versatia.config.Value
import com.github.marcoral.versatia.di.LocalBean
import com.github.marcoral.versatia.task.BackgroundTask
import com.github.marcoral.versatia.task.every
import com.github.marcoral.versatia.util.collection.cycle
import kotlin.time.Duration.Companion.seconds
import net.kyori.adventure.text.Component.text

/**
 * Started at the tick after the plugin starts, never called by plugin code; every reload cancels
 * the run and starts a new one with the new values. A bean rather than an object, so that it can
 * take the plugin for logging; `@Value` needs one of the two.
 */
@LocalBean
class AutoBroadcastTask(private val plugin: BackgroundPlugin) : BackgroundTask {
    @Value("auto-broadcast.period")
    var period = 30.seconds

    @Value("auto-broadcast.lines")
    var lines = emptyList<String>()

    override suspend fun run() {
        if (lines.isEmpty()) {
            plugin.logger.info("No lines to broadcast; add some to config.yml and reload")
            return
        }
        plugin.logger.info("Broadcasting ${lines.size} lines every $period")
        val rotation = lines.cycle()
        every(period) { VersatiaEngine.broadcast(text(rotation.next())) }
    }
}
