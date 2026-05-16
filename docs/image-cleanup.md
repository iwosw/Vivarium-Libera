# Image Cleanup Workflow

This is the workflow I use to turn AI-generated plant/item images into Minecraft-ready textures.

## Goal

Take a generated image and end up with a clean in-game texture:

- transparent background
- no detached junk fragments
- no white fringe
- correct scale and placement
- readable silhouette in Minecraft

## Step-by-step

### 1. Keep source and result separate

- Keep the raw generated image as a source reference.
- Save the final game texture under the real asset name, for example `calamus.png`.
- Do not rely on the raw `Gemini_Generated_...` file as the final asset.

### 2. Remove the background

If the image has a white background:

- detect pixels close to white
- convert them to transparent
- restore edge colors from the white matte so leaves or petals do not become washed out

In practice this means alpha is derived from distance from white, then the RGB is "unmatted" so the object keeps its real color.

### 3. Crop tightly

- find the opaque bounds of the object
- crop around them with a small padding
- keep enough margin so the silhouette does not touch the image border

### 4. Remove detached fragments

If the generator produced floating pieces or a second disconnected clump:

- either keep only the main connected component
- or rebuild the texture from the source instead of trying to patch the broken one

This is the cleanest way to remove random side branches, duplicated leaf clusters, and tiny floating artifacts.

### 5. Remove unwanted details

If the generator added details that should not exist in the final texture:

- recolor or replace them using nearby valid colors
- remove seed heads, buds, stems, or highlights that do not belong
- simplify noisy areas into cleaner pixel clusters

### 6. Fix the white fringe

This is the most important cleanup step.

To remove the white outline around the object:

- harden the edge alpha when needed
- replace overly bright edge pixels with colors sampled from nearby opaque pixels
- darken edge pixels slightly if they still read as glowing
- fill RGB in fully transparent pixels with nearby object colors

That last point matters because mipmaps can sample transparent pixels and pull white from the old background if the transparent RGB is not cleaned.

### 7. Rebuild the silhouette if needed

For plants, sometimes the best result is not a direct crop, but a rebuilt sprite.

Examples:

- layer multiple slightly shifted copies of the cleaned plant to make a denser clump
- trim away noisy side leaves and rebuild a wider central shape
- keep the silhouette readable from a distance instead of preserving every generated detail

### 8. Resize for the game

Usually I bring plant textures to `128x128` in this project.

Rules:

- scale with nearest-neighbor behavior
- place the plant so it sits on the bottom edge naturally
- keep the center of mass readable
- do not leave the plant too tiny inside the canvas

### 9. Add shading only after cleanup

Shading should be added after:

- background removal
- fragment removal
- fringe cleanup

Otherwise the highlights and shadows make white fringe cleanup harder.

### 10. Validate in game

- use `F3+T` if only textures changed
- fully restart the client if block classes, blockstates, or models changed
- test on grass and on a dark background
- check both single placement and dense placement

## Rules of thumb

- One clear main silhouette is better than many clever details.
- For flowers, avoid grass tint unless the whole sprite is meant to be biome-colored.
- For dense marsh plants, a plain `cross` can look too thin; a custom multi-plane model can read better.
- If a source image creates warnings in assets, do not keep uppercase `Gemini_Generated_...` files in the shipped resource path.

## Prompt for future auto-cropping

Use this when asking another model to clean an already generated image:

```text
Take this generated image and isolate only the main object. Remove the background completely, crop tightly around the object with small clean margins, and center it on a plain white background. Keep the original proportions and silhouette. Remove detached fragments, duplicate parts, floating artifacts, stray pixels, and unwanted decorative details. Preserve crisp pixel edges, no blur, no glow, no anti-aliasing, no soft transparency, no white fringe.
```

Transparent background version:

```text
Take this generated image and isolate only the main object. Remove the background completely and place the object on a transparent background. Crop tightly with small clean margins, keep the original proportions, remove floating fragments and stray artifacts, preserve crisp pixel edges, and avoid white fringe or semi-transparent blur around the object.
```
