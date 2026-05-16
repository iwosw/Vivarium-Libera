# Vivarium Libera

NeoForge-мод для Minecraft `1.21.1`, добавляющий декоративные растения и базовый инструмент.

## Что умеет мод сейчас

- Добавляет растения: полынь, крапиву, белену, зверобой, дурман, кипрей, аир и рогоз.
- Растения имеют собственные модели, текстуры, blockstates, item-модели и loot tables.
- Полынь, крапива, белена, зверобой, дурман и кипрей ставятся в один или два слоя через состояние `amount`.
- У stackable-растений есть направление `facing` и альтернативный визуальный вариант `alt`.
- Аир и рогоз работают как простые декоративные bush-блоки.
- Добавляет предмет `knife` с собственной текстурой и моделью.
- Добавляет две creative-вкладки: растения Vivarium Libera и инструменты Vivarium Libera.
- Поддерживает русскую и английскую локализацию через `ru_ru.json` и `en_us.json`.

## Архитектура

- `src/main/java/com/iwosw/vivariumlibera/VivariumLibera.java` - точка входа мода.
- `src/main/java/com/iwosw/vivariumlibera/block` - переиспользуемое поведение блоков.
- `src/main/java/com/iwosw/vivariumlibera/registry` - регистрация блоков, предметов и creative-вкладок.
- `src/main/resources/assets/vivariumlibera` - модели, текстуры, blockstates и локализация.
- `src/main/resources/data/vivariumlibera` - игровые данные, включая loot tables.
- `src/main/resources/META-INF/neoforge.mods.toml` - metadata мода.

## Требования

- JDK `21`
- Gradle Wrapper: `gradlew` / `gradlew.bat`

## Команды

- `./gradlew build` - собрать мод.
- `./gradlew runClient` - запустить клиент для разработки.
- `./gradlew runServer` - запустить сервер для разработки.
