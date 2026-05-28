# Water Generation Plan

## Goal

Make water feel like it belongs to the terrain instead of being a flat decorative layer. The first versions should improve streams and small rivers without replacing Minecraft's chunk generator or adding expensive water simulation.

## Core Rule

Rivers are generated as terrain features, not live fluid physics. Generation should create the river shape, carve a shallow bed, place normal water source blocks, and decorate the banks.

## Current Project State

- The mod targets Minecraft `1.21.1` with NeoForge `21.1.172`.
- Existing code registers blocks, items, creative tabs, plant events, and one client-side Iris mixin.
- There is no current worldgen package, feature registry, biome modifier, configured feature, or placed feature.
- The safest integration point for the first MVP is a custom worldgen `Feature` added to selected biomes through a NeoForge biome modifier.

## Non-Goals For MVP

- Do not replace the vanilla chunk generator.
- Do not add mixins into `NoiseBasedChunkGenerator` or terrain noise in the first pass.
- Do not simulate flowing water across chunks.
- Do not build region-wide river networks yet.
- Do not generate deltas, large lakes, waterfalls, or tributary merging yet.

## Phase 0: Architecture Spike

Status: done by subagents.

Findings:

- `Feature + biome modifier` is the right first implementation path.
- Surface rules are useful later for bank cosmetics, not for carving a stream path.
- Chunk-generator mixins are powerful but too risky for the first pass.
- The MVP must be deterministic from world coordinates so neighboring chunks can independently recreate the same stream path.

## Phase 1: Stream MVP

Create a small stream generator that works inside normal worldgen.

Files to add:

- `src/main/java/com/iwosw/vivariumlibera/registry/ModFeatures.java`
- `src/main/java/com/iwosw/vivariumlibera/worldgen/feature/StreamFeature.java`
- `src/main/java/com/iwosw/vivariumlibera/worldgen/feature/StreamFeatureConfiguration.java`
- `src/main/resources/data/vivariumlibera/worldgen/configured_feature/stream.json`
- `src/main/resources/data/vivariumlibera/worldgen/placed_feature/stream.json`
- `src/main/resources/data/vivariumlibera/neoforge/biome_modifier/add_streams.json`
- `src/main/resources/data/vivariumlibera/tags/worldgen/biome/has_streams.json`

Files to change:

- `src/main/java/com/iwosw/vivariumlibera/VivariumLibera.java`

Behavior:

- Divide the world into deterministic stream cells, initially `64x64` blocks.
- Each cell has a seed-based chance to contain a stream.
- Each stream is a short curved line crossing part of the cell.
- Each chunk processes only the stream positions that fall inside its own `16x16` area.
- Find terrain height with `WORLD_SURFACE_WG`.
- Skip positions outside a safe height range, initially around `55..120`.
- Skip steep local jumps.
- Carve a shallow bed, place source water, and use gravel/dirt/mud-like bank blocks where safe.

Performance limits:

- No pathfinding across chunks.
- No forced loading of neighboring chunks.
- No writing outside the active chunk.
- Small radius only: `2` blocks for the current debug pass.
- Small depth only: `1` block for the first pass.
- Check only nearby stream cells that can intersect the current chunk.

Acceptance checks:

- `./gradlew compileJava` succeeds.
- A new world loads without registry or datapack errors.
- Streams appear in selected land biomes.
- Streams do not consistently stop at chunk borders.
- Chunk generation remains responsive while flying or teleporting.

Temporary debug check:

- `debug_markers` can be enabled in `stream.json`.
- When enabled, generated stream banks use yellow wool so our streams are visually obvious.
- Disable it before keeping worlds or making release builds.

## Phase 2: Better Terrain Fit

Improve the stream shape after the MVP works.

- Prefer downhill or mostly-flat direction when selecting stream endpoints.
- Widen the stream slightly when the path gets longer or lower.
- Add smoother banks and less blocky transitions.
- Avoid trees, structures, lava, caves, and existing large water bodies more carefully.
- Add biome-specific bed materials.

## Phase 3: Lakes And Overflow

Add simple water storage behavior without full simulation.

- Detect closed low areas along a stream path.
- Create small ponds or lakes instead of forcing a channel through hills.
- Add overflow exits from lakes at the lowest nearby edge.
- Keep lakes local and bounded to avoid expensive searches.

## Phase 4: River Networks

Only after streams are stable, add larger connected rivers.

- Generate coarse drainage cells larger than chunks.
- Connect cells by approximate downhill direction.
- Let small streams merge into medium rivers.
- Reserve wider valleys before carving water.
- Add mouths where rivers meet ocean or large lakes.

## Phase 5: Visual Polish

- Add reeds, cattails, calamus, mud, gravel bars, and occasional exposed roots around water.
- Add colder, muddy, forest, and meadow variants.
- Add rare waterfalls only where a clear cliff drop exists.
- Tune density per biome.

## First Implementation Decision

Start with Phase 1 only. The first code should prove that the mod can add deterministic, chunk-safe stream segments through NeoForge worldgen without destabilizing world creation.
