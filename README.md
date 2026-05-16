# Vivarium-Libera

NeoForge mod scaffold for Minecraft 1.21.1.

## Structure

- `build.gradle` - NeoForge/Gradle build configuration
- `gradle.properties` - Minecraft, NeoForge and mod metadata versions
- `src/main/java/com/iwosw/vivariumlibera` - mod bootstrap and Java sources
- `src/main/java/com/iwosw/vivariumlibera/block` - reusable block behavior classes
- `src/main/java/com/iwosw/vivariumlibera/registry` - block, item and creative tab registration
- `src/main/resources/META-INF/neoforge.mods.toml` - mod metadata
- `src/main/resources/assets/vivariumlibera` - assets namespace

## Requirements

- JDK 21
- Gradle wrapper (`gradlew` / `gradlew.bat`)

## Useful commands

- `./gradlew runClient`
- `./gradlew runServer`
- `./gradlew build`
