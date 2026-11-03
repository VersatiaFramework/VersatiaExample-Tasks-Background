package dev.versatia.example.background

import dev.versatia.VersatiaEngine
import dev.versatia.broadcast
import dev.versatia.config.Value
import dev.versatia.task.BackgroundTask
import dev.versatia.task.every
import dev.versatia.util.collection.cycle
import java.util.logging.Logger
import kotlin.time.Duration.Companion.seconds
import net.kyori.adventure.text.Component.text

/**
 * Started at the tick after the plugin starts, never called by plugin code; every reload cancels
 * the run and starts a new one with the new values. The runtime creates it with the plugin's
 * logger injected, no bean scope needed.
 */
class AutoBroadcastTask(private val logger: Logger) : BackgroundTask {
    @Value("auto-broadcast.period")
    var period = 30.seconds

    @Value("auto-broadcast.lines")
    var lines = emptyList<String>()

    override suspend fun run() {
        if (lines.isEmpty()) {
            logger.info("No lines to broadcast; add some to config.yml and reload")
            return
        }
        logger.info("Broadcasting ${lines.size} lines every $period")
        val rotation = lines.cycle()
        every(period) { VersatiaEngine.broadcast(text(rotation.next())) }
    }
}
