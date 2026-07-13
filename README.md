# Vivarium Libera

[Russian version](README.ru.md)

Vivarium Libera is a NeoForge mod for Minecraft `1.21.1` that expands the world with living natural details: herbs, wetland plants, plum trees, streams, decorative objects, and herbalist crafting.

The mod is in early alpha and active development. Unfinished mechanics, balance changes, and version-to-version incompatibilities are expected. The current version already adds new plant content, tools, recipes, localization, and first integrations with other mods.

## Overview

- Minecraft: `1.21.1`
- Loader: NeoForge `21.1.x`
- Java: `21`
- Mod ID: `vivariumlibera`
- Required dependency: GeckoLib
- Optional integration: Farmer's Delight
- Dev integration: JEI API/runtime for recipe checks

## Content

- Decorative and functional plants: wormwood, nettle, henbane, St. John's wort, datura, fireweed, chicory, calamus, cattail, calendula, comfrey, eyebright, sage, crow's eye, lycoris, mint, thistle, belladonna, wood sorrel, yarrow, bellflowers, and valerian.
- Different placement rules for regular, moisture-loving, and aquatic plants.
- Cuttable plants with regrowth and separate drop items.
- Herbalist knife that cuts suitable plants and supports tool tags.
- Mortar with a block entity, animation, and herbal ingredient processing.
- Herbalist book with an in-game screen and reference HTML document.
- Plum wood set: logs, stripped logs, planks, stairs, slabs, button, pressure plate, fence, fence gate, trapdoor, leaves, and sapling.
- Plum sapling grows a compact tree with several foliage stages.
- Decorative jugs: regular, water, oil, and wine variants in empty and full states.
- Jugs can be held as items, placed in the world, and picked back up when broken.
- Stream worldgen feature with configured/placed features and a biome tag.
- Poison effect is preserved when trying to remove it with milk.

## Integrations

- Farmer's Delight: plant cutting recipes and compatible ingredient tags.
- JEI: dev dependency for checking recipe display in the client.
- Common `c` tags: fruits, herbs, knives, and compatible item groups.
- NeoForge data maps: compostables for organic items.

## Localization

Supported languages:

- `en_us`
- `ru_ru`

Main block, item, creative tab, and herbalist book names are stored in lang files.

## Build

On Windows:

```bat
gradlew.bat build
```

On Linux/macOS:

```bash
./gradlew build
```

The built `.jar` is generated in `build/libs`.

## Development Run

Client:

```bat
gradlew.bat runClient
```

Server:

```bat
gradlew.bat runServer
```

Data generation:

```bat
gradlew.bat runData
```

## Project Structure

- `src/main/java/com/iwosw/vivariumlibera` - main Java mod code.
- `src/main/java/com/iwosw/vivariumlibera/block` - custom blocks and behavior.
- `src/main/java/com/iwosw/vivariumlibera/block/entity` - block entities.
- `src/main/java/com/iwosw/vivariumlibera/client` - client screens and renderers.
- `src/main/java/com/iwosw/vivariumlibera/compat` - integrations with other mods.
- `src/main/java/com/iwosw/vivariumlibera/datagen` - model, tag, recipe, and loot table generators.
- `src/main/java/com/iwosw/vivariumlibera/item` - custom items.
- `src/main/java/com/iwosw/vivariumlibera/registry` - block, item, tab, block entity, and worldgen feature registration.
- `src/main/resources/assets/vivariumlibera` - models, textures, blockstates, animations, geometry, and localization.
- `src/main/resources/data` - recipes, tags, loot tables, data maps, and worldgen data.
- `docs` - supporting documentation and HTML materials.

## Development Notes

- The project uses Gradle Wrapper, so a local Gradle installation is not required.
- `build/`, `.gradle/`, `run/`, and IDE files should not be committed.
- `.bbmodel` files are excluded from final build resources.
- Generated resources are connected through `src/generated/resources`; asset duplicates and `.cache` are excluded during `processResources`.
