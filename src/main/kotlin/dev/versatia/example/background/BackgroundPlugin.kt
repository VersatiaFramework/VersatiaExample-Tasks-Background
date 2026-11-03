package dev.versatia.example.background

import dev.versatia.FriendlyName
import dev.versatia.VersatiaPlugin

/** Registered as `broadcast`, so `/versatia reload broadcast` reloads it. */
@FriendlyName("broadcast")
class BackgroundPlugin : VersatiaPlugin()
