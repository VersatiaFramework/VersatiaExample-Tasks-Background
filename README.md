# VersatiaExample-Tasks-Background

A class implementing `BackgroundTask` needs no bean scope: the runtime creates it with its constructor injected, runs it from the tick after the plugin starts, restarts it with the new values at every reload and cancels it when the plugin stops; this one broadcasts the lines of `config.yml` at a period written as a duration and logs through the injected `Logger`.

Part of the [Versatia examples](https://github.com/VersatiaFramework/VersatiaExamples); documentation: [Background tasks](https://versatiaframework.github.io/VersatiaDocs/docs/tasks#background-tasks).

**Start with** [`AutoBroadcastTask.kt`](src/main/kotlin/com/github/marcoral/versatia/example/background/AutoBroadcastTask.kt); the configuration is [`resources/config.yml`](src/main/resources/resources/config.yml).

## What the log shows

```
[VersatiaExample-Tasks-Background] Broadcasting 3 lines every 30s
(every 30 s) Tip: edit config.yml and run /versatia reload broadcast
```

Every player reads the same lines in their chat, one every 30 seconds, in turn.

## Things to try

- Edit `plugins/VersatiaExample-Tasks-Background/config.yml`, set `period: 10s` and run
  `/versatia reload broadcast`: the running broadcast is cancelled and a new run logs
  `Broadcasting 3 lines every 10s`, starting again from the first line. No code restarts it.
- Set `lines: []` and reload: `No lines to broadcast; add some to config.yml and reload`. `run`
  returned, so the task stays idle until the next reload.
- Set `period: 5 minutes` and reload: the command reports
  `broadcast failed to reload: ...AutoBroadcastTask.period could not be read from config.yml: Value at "auto-broadcast.period" is not a duration such as "30s", "5m" or "1h 30m" (units d, h, m, s, ms): "5 minutes"`,
  the previous values stay and the running broadcast goes on. `5m`, `1h 30m` and the ISO form
  `PT5M` are accepted.
- Add `@DisableVersatiaFeatures(runBackgroundTasks = true)` to `BackgroundPlugin`: the build fails
  with `AutoBroadcastTask.period cannot be enhanced: AutoBroadcastTask is neither the plugin's main class, an object, a bean nor a class a subclass enhancement creates, ...`,
  since without the feature nothing creates the task. Put `@LocalBean` on `AutoBroadcastTask` and
  it builds again: the bean is created and its values are set, but `run` is never called.

## Building

Requires JDK 21 and VersatiaStarter with what it exports (VersatiaAPI, the module APIs) and
VersatiaCatalog, which supplies every version, in your local Maven repository (`./gradlew publishToMavenLocal` in each checkout; see
[Building the framework](https://versatiaframework.github.io/VersatiaDocs/docs/contributing/building));
running it requires a Paper 1.21.4 server with VersatiaCore installed, and the
[CommandAPI](https://commandapi.jorel.dev) 12.x plugin for `/versatia reload`.

```bash
./gradlew build
```

To have the JAR copied to your server after every build, copy `local.properties.example` to
`local.properties` and set `pluginsDir`. The generated descriptor is in
`build/generated/ksp/main/kotlin`; open it to see exactly what the runtime executes.

The Versatia version is `versatiaVersion` in `gradle.properties`; `settings.gradle.kts` imports
the matching [VersatiaCatalog](https://versatiaframework.github.io/VersatiaDocs/docs/getting-started/installation#versatia-catalog),
so the build script writes `libs.versatia.starter` and `alias(libs.plugins.ksp)` instead of version
numbers.
