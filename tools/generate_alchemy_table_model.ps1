param(
    [string]$OutputPath = "src/main/resources/assets/vivariumlibera/models/block/alchemy_table.json",
    [string]$LitOutputPath = "src/main/resources/assets/vivariumlibera/models/block/alchemy_table_lit.json",
    [string]$NoLecternOutputPath = "src/main/resources/assets/vivariumlibera/models/block/alchemy_table_no_lectern.json",
    [string]$NoLecternLitOutputPath = "src/main/resources/assets/vivariumlibera/models/block/alchemy_table_no_lectern_lit.json",
    [string]$InventoryOutputPath = "src/main/resources/assets/vivariumlibera/models/block/alchemy_table_inventory.json"
)

$ErrorActionPreference = "Stop"
$elements = [System.Collections.Generic.List[object]]::new()
$modelXOffset = 8

function Add-Cube {
    param(
        [string]$Name,
        [double[]]$From,
        [double[]]$To,
        [string]$Texture = "plaster",
        [hashtable]$FaceTextures = @{},
        [hashtable]$Rotation = $null,
        [string[]]$VisibleFaces = @("north", "east", "south", "west", "up", "down")
    )

    $From = @(($From[0] + $modelXOffset), $From[1], $From[2])
    $To = @(($To[0] + $modelXOffset), $To[1], $To[2])

    $width = $To[0] - $From[0]
    $height = $To[1] - $From[1]
    $depth = $To[2] - $From[2]
    $dimensions = [ordered]@{
        north = @($width, $height)
        east = @($depth, $height)
        south = @($width, $height)
        west = @($depth, $height)
        up = @($width, $depth)
        down = @($width, $depth)
    }
    $faces = [ordered]@{}

    foreach ($face in $VisibleFaces) {
        $faceTexture = if ($FaceTextures.ContainsKey($face)) { $FaceTextures[$face] } else { $Texture }
        $size = $dimensions[$face]
        $uvOrigin = switch ($face) {
            { $_ -in @("north", "south") } { @($From[0], $From[1]); break }
            { $_ -in @("east", "west") } { @($From[2], $From[1]); break }
            default { @($From[0], $From[2]) }
        }
        # Crop a different valid area of the texture for each model-space position.
        # This preserves one texel per model unit without stamping the same top-left
        # corner onto every small masonry piece.
        $maxU = [Math]::Max(0.0, (16.0 - $size[0]))
        $maxV = [Math]::Max(0.0, (16.0 - $size[1]))
        $uSteps = [Math]::Floor($maxU) + 1
        $vSteps = [Math]::Floor($maxV) + 1
        $u = if ($maxU -eq 0) { 0 } else { (($uvOrigin[0] % $uSteps) + $uSteps) % $uSteps }
        $v = if ($maxV -eq 0) { 0 } else { (($uvOrigin[1] % $vSteps) + $vSteps) % $vSteps }
        $u = [Math]::Min($maxU, [Math]::Max(0.0, [double]$u))
        $v = [Math]::Min($maxV, [Math]::Max(0.0, [double]$v))
        $faces[$face] = [ordered]@{
            uv = @($u, $v, ($u + $size[0]), ($v + $size[1]))
            texture = "#$faceTexture"
        }
    }

    $element = [ordered]@{
        name = $Name
        from = $From
        to = $To
        faces = $faces
    }
    if ($null -ne $Rotation) {
        $element.rotation = [ordered]@{
            origin = @(($Rotation.origin[0] + $modelXOffset), $Rotation.origin[1], $Rotation.origin[2])
            axis = $Rotation.axis
            angle = $Rotation.angle
            rescale = $true
        }
    }
    $elements.Add($element)
}

function Add-TiledBox {
    param(
        [string]$Name,
        [double[]]$From,
        [double[]]$To,
        [string]$Texture = "plaster",
        [hashtable]$FaceTextures = @{}
    )

    $index = 0
    for ($x = $From[0]; $x -lt $To[0]; $x = [Math]::Min($x + 16, $To[0])) {
        $nextX = [Math]::Min($x + 16, $To[0])
        for ($y = $From[1]; $y -lt $To[1]; $y = [Math]::Min($y + 16, $To[1])) {
            $nextY = [Math]::Min($y + 16, $To[1])
            for ($z = $From[2]; $z -lt $To[2]; $z = [Math]::Min($z + 16, $To[2])) {
            $nextZ = [Math]::Min($z + 16, $To[2])
                $visibleFaces = [System.Collections.Generic.List[string]]::new()
                if ($z -eq $From[2]) { $visibleFaces.Add("north") }
                if ($nextX -eq $To[0]) { $visibleFaces.Add("east") }
                if ($nextZ -eq $To[2]) { $visibleFaces.Add("south") }
                if ($x -eq $From[0]) { $visibleFaces.Add("west") }
                if ($nextY -eq $To[1]) { $visibleFaces.Add("up") }
                if ($y -eq $From[1]) { $visibleFaces.Add("down") }
                Add-Cube -Name "$Name`_$index" -From @($x, $y, $z) -To @($nextX, $nextY, $nextZ) `
                    -Texture $Texture -FaceTextures $FaceTextures -VisibleFaces $visibleFaces.ToArray()
                $index++
            }
        }
    }
}

# Main masonry body, tiled so every face keeps a constant vanilla texel density.
Add-TiledBox "left_body" @(-8, 0, 0) @(1, 16, 32)
Add-TiledBox "right_body" @(15, 0, 0) @(24, 16, 32)
Add-TiledBox "front_lintel" @(1, 12, 0) @(15, 16, 8)
Add-TiledBox "front_arch_left" @(1, 8.5, 0) @(3, 12, 8)
Add-TiledBox "front_arch_right" @(13, 8.5, 0) @(15, 12, 8)
Add-TiledBox "front_arch_inner_left" @(3, 10.5, 0) @(5, 12, 8)
Add-TiledBox "front_arch_inner_right" @(11, 10.5, 0) @(13, 12, 8)
Add-TiledBox "niche_floor" @(1, 0, 0) @(15, 1, 8) "plaster" @{ up = "soot" }
Add-TiledBox "niche_back" @(1, 1, 7) @(15, 12, 8) "plaster" @{ north = "soot" }
Add-TiledBox "center_masonry_core" @(1, 0, 8) @(15, 16, 32) "plaster"

# Worktop overhang, split into four tiles instead of stretching one sprite over 32x24 units.
Add-TiledBox "counter" @(-8, 16, 0) @(24, 17.5, 32)

# Central stepped rear arch with lower side sections instead of one giant flat wall.
Add-TiledBox "rear_left_low" @(-8, 17.5, 27) @(1, 22, 32)
Add-TiledBox "rear_right_low" @(15, 17.5, 27) @(24, 22, 32)
Add-TiledBox "rear_arch_left_pillar" @(1, 17.5, 27) @(3, 25, 32)
Add-TiledBox "rear_arch_right_pillar" @(13, 17.5, 27) @(15, 25, 32)
Add-TiledBox "rear_arch_top" @(3, 25, 27) @(13, 27, 32)
Add-TiledBox "rear_arch_left_step" @(3, 23, 27) @(5, 25, 32)
Add-TiledBox "rear_arch_right_step" @(11, 23, 27) @(13, 25, 32)
Add-TiledBox "rear_arch_inner_left" @(5, 24, 27) @(7, 25, 32)
Add-TiledBox "rear_arch_inner_right" @(9, 24, 27) @(11, 25, 32)
Add-TiledBox "rear_niche_back" @(1, 17.5, 30) @(15, 25, 32) "plaster" @{ north = "soot" }

# Wider stepped octagonal hearth; soot appears only on inner vertical faces.
Add-Cube "hearth_front" @(1, 17.5, 7) @(11, 20, 9) "plaster" @{ south = "soot" }
Add-Cube "hearth_front_left" @(-1, 17.5, 8) @(1, 20, 10) "plaster"
Add-Cube "hearth_front_right" @(11, 17.5, 8) @(13, 20, 10) "plaster"
Add-Cube "hearth_left" @(-3, 17.5, 10) @(-1, 20, 17) "plaster" @{ east = "soot" }
Add-Cube "hearth_right" @(13, 17.5, 10) @(15, 20, 17) "plaster" @{ west = "soot" }
Add-Cube "hearth_back_left" @(-1, 17.5, 17) @(1, 20, 19) "plaster"
Add-Cube "hearth_back_right" @(11, 17.5, 17) @(13, 20, 19) "plaster"
Add-Cube "hearth_back" @(1, 17.5, 18) @(11, 20, 20) "plaster" @{ north = "soot" }
Add-Cube "hearth_bed" @(-1, 17.55, 9) @(13, 18.0, 18) "coal"

# A low mound of separate unlit coals instead of one flat rectangle.
$coalPieces = @(
    @("coal_1", 0, 18.0, 10, 2.5, 19.1, 12),
    @("coal_2", 3.25, 18.0, 9.5, 5.75, 19.5, 12),
    @("coal_3", 6.5, 18.0, 10, 9, 19.2, 12.5),
    @("coal_4", 9.75, 18.0, 10.5, 12, 19.0, 12.5),
    @("coal_5", 1.5, 18.0, 13, 4, 19.4, 15.25),
    @("coal_6", 5, 18.0, 13.25, 7.5, 19.1, 16),
    @("coal_7", 8.5, 18.0, 13.25, 11, 19.35, 15.75),
    @("coal_8", 4.25, 19.1, 11.75, 7, 20.2, 14)
)
foreach ($piece in $coalPieces) {
    Add-Cube $piece[0] @($piece[1], $piece[2], $piece[3]) @($piece[4], $piece[5], $piece[6]) "coal"
}

# Two inward-leaning firewood logs. Their pivots sit on the niche floor, so the
# bottoms stay planted and the logs approach one another without intersecting.
Add-Cube "left_log" @(3.5, 1, 2) @(6.5, 9, 5) "log_side" `
    @{ up = "log_end"; down = "log_end" } @{ origin = @(5, 1, 3.5); axis = "z"; angle = -22.5 }
Add-Cube "right_log" @(9.5, 1, 2) @(12.5, 9, 5) "log_side" `
    @{ up = "log_end"; down = "log_end" } @{ origin = @(11, 1, 3.5); axis = "z"; angle = 22.5 }

# Compact tabletop book rest with one continuous writing surface. Splitting the
# board into offset strips made a visibly stepped surface and produced seams.
# It now sits fully on the table's right wing, leaving the centre worktop clear.
$lecternStart = $elements.Count
Add-Cube "lectern_base" @(17, 17.5, 0.75) @(23, 18.25, 6.25) "wood"
Add-Cube "lectern_left_support" @(17.5, 18.25, 3.25) @(19, 20.25, 5.25) "wood"
Add-Cube "lectern_right_support" @(21, 18.25, 3.25) @(22.5, 20.25, 5.25) "wood"
$lecternSlope = @{ origin = @(20, 20.5, 3.75); axis = "x"; angle = -22.5 }
Add-Cube "lectern_board" @(17, 20, 1) @(23, 20.75, 6.5) "wood" @{} $lecternSlope
Add-Cube "lectern_lip" @(17, 19.5, 1) @(23, 20.5, 1.75) "wood" @{} $lecternSlope

$model = [ordered]@{
    ambientocclusion = $true
    textures = [ordered]@{
        plaster = "vivariumlibera:block/alchemy_table/plaster"
        soot = "vivariumlibera:block/alchemy_table/soot"
        coal = "vivariumlibera:block/alchemy_table/coal"
        wood = "vivariumlibera:block/alchemy_table/wood"
        log_side = "vivariumlibera:block/alchemy_table/log_side"
        log_end = "vivariumlibera:block/alchemy_table/log_end"
        particle = "vivariumlibera:block/alchemy_table/plaster"
    }
    display = [ordered]@{
        thirdperson_righthand = [ordered]@{ rotation = @(75, 45, 0); translation = @(0, 1.5, 0); scale = @(0.22, 0.22, 0.22) }
        thirdperson_lefthand = [ordered]@{ rotation = @(75, 45, 0); translation = @(0, 1.5, 0); scale = @(0.22, 0.22, 0.22) }
        firstperson_righthand = [ordered]@{ rotation = @(0, 45, 0); translation = @(0, 0, -1); scale = @(0.23, 0.23, 0.23) }
        firstperson_lefthand = [ordered]@{ rotation = @(0, 225, 0); translation = @(0, 0, -1); scale = @(0.23, 0.23, 0.23) }
        ground = [ordered]@{ translation = @(0, 2, 0); scale = @(0.16, 0.16, 0.16) }
        gui = [ordered]@{ rotation = @(30, 225, 0); translation = @(0, -2, 0); scale = @(0.34, 0.34, 0.34) }
        fixed = [ordered]@{ scale = @(0.26, 0.26, 0.26) }
    }
    elements = $elements
}

foreach ($element in $elements) {
    if ($element.from.Count -ne 3 -or $element.to.Count -ne 3) {
        throw "Element '$($element.name)' must have exactly three from/to coordinates."
    }
    foreach ($face in $element.faces.Values) {
        if ($face.uv.Count -ne 4) {
            throw "Element '$($element.name)' has a face without exactly four UV coordinates: $($face.uv -join ', ')."
        }
        if (($face.uv | Measure-Object -Minimum).Minimum -lt 0 -or
            ($face.uv | Measure-Object -Maximum).Maximum -gt 16) {
            throw "Element '$($element.name)' has UV coordinates outside the 0..16 texture area: $($face.uv -join ', ')."
        }
    }
}

function Write-Model {
    param([string]$Path)

    $absoluteOutput = [System.IO.Path]::GetFullPath((Join-Path (Get-Location) $Path))
    [System.IO.Directory]::CreateDirectory([System.IO.Path]::GetDirectoryName($absoluteOutput)) | Out-Null
    [System.IO.File]::WriteAllText(
        $absoluteOutput,
        ($model | ConvertTo-Json -Depth 20),
        [System.Text.UTF8Encoding]::new($false))
}

function Rotate-Point {
    param([double[]]$Point, [int]$Turns)
    switch ($Turns % 4) {
        1 { return @((32 - $Point[2]), $Point[1], $Point[0]) }
        2 { return @((32 - $Point[0]), $Point[1], (32 - $Point[2])) }
        3 { return @($Point[2], $Point[1], (32 - $Point[0])) }
        default { return @($Point[0], $Point[1], $Point[2]) }
    }
}

function Rotate-Face {
    param([string]$Face, [int]$Turns)
    if ($Face -in @("up", "down")) { return $Face }
    $directions = @("north", "east", "south", "west")
    $index = [Array]::IndexOf($directions, $Face)
    return $directions[($index + $Turns) % 4]
}

function New-RotatedElements {
    param([System.Collections.Generic.List[object]]$Source, [int]$Turns)
    $rotated = [System.Collections.Generic.List[object]]::new()
    foreach ($sourceElement in $Source) {
        $fromPoint = Rotate-Point $sourceElement.from $Turns
        $toPoint = Rotate-Point $sourceElement.to $Turns
        $newFrom = @(
            [Math]::Min($fromPoint[0], $toPoint[0]),
            [Math]::Min($fromPoint[1], $toPoint[1]),
            [Math]::Min($fromPoint[2], $toPoint[2]))
        $newTo = @(
            [Math]::Max($fromPoint[0], $toPoint[0]),
            [Math]::Max($fromPoint[1], $toPoint[1]),
            [Math]::Max($fromPoint[2], $toPoint[2]))
        $newFaces = [ordered]@{}
        foreach ($face in $sourceElement.faces.Keys) {
            $newFaces[(Rotate-Face $face $Turns)] = $sourceElement.faces[$face]
        }
        $newElement = [ordered]@{
            name = $sourceElement.name
            from = $newFrom
            to = $newTo
            faces = $newFaces
        }
        if ($null -ne $sourceElement.rotation) {
            $axis = $sourceElement.rotation.axis
            $angle = $sourceElement.rotation.angle
            if ($Turns -eq 1) {
                if ($axis -eq "x") { $axis = "z" }
                elseif ($axis -eq "z") { $axis = "x"; $angle = -$angle }
            } elseif ($Turns -eq 2) {
                if ($axis -in @("x", "z")) { $angle = -$angle }
            } elseif ($Turns -eq 3) {
                if ($axis -eq "x") { $axis = "z"; $angle = -$angle }
                elseif ($axis -eq "z") { $axis = "x" }
            }
            $newElement.rotation = [ordered]@{
                origin = Rotate-Point $sourceElement.rotation.origin $Turns
                axis = $axis
                angle = $angle
                rescale = $sourceElement.rotation.rescale
            }
        }
        $rotated.Add($newElement)
    }
    return $rotated
}

function Write-ModelSet {
    param([string]$NorthPath)
    $northElements = $model.elements
    Write-Model $NorthPath
    $directory = [System.IO.Path]::GetDirectoryName($NorthPath)
    $stem = [System.IO.Path]::GetFileNameWithoutExtension($NorthPath)
    $suffixes = @("east", "south", "west")
    for ($turns = 1; $turns -le 3; $turns++) {
        $model.elements = New-RotatedElements $northElements $turns
        Write-Model (Join-Path $directory "$stem`_$($suffixes[$turns - 1]).json")
    }
    $model.elements = $northElements
}

function New-InventoryElements {
    param($Source)
    $inventoryElements = [System.Collections.Generic.List[object]]::new()
    foreach ($sourceElement in $Source) {
        $inventoryElement = [ordered]@{
            name = $sourceElement.name
            from = @(($sourceElement.from[0] - 8), $sourceElement.from[1], ($sourceElement.from[2] - 8))
            to = @(($sourceElement.to[0] - 8), $sourceElement.to[1], ($sourceElement.to[2] - 8))
            faces = $sourceElement.faces
        }
        if ($null -ne $sourceElement.rotation) {
            $inventoryElement.rotation = [ordered]@{
                origin = @(
                    ($sourceElement.rotation.origin[0] - 8),
                    $sourceElement.rotation.origin[1],
                    ($sourceElement.rotation.origin[2] - 8))
                axis = $sourceElement.rotation.axis
                angle = $sourceElement.rotation.angle
                rescale = $sourceElement.rotation.rescale
            }
        }
        $inventoryElements.Add($inventoryElement)
    }
    return $inventoryElements
}

Write-ModelSet $OutputPath
$worldElements = $model.elements
$model.elements = New-InventoryElements $worldElements
Write-Model $InventoryOutputPath
$model.elements = $worldElements
$model.textures.coal = "vivariumlibera:block/alchemy_table/coal_lit"
Write-ModelSet $LitOutputPath
$elements.RemoveRange($lecternStart, $elements.Count - $lecternStart)
$model.elements = $elements
$model.textures.coal = "vivariumlibera:block/alchemy_table/coal"
Write-ModelSet $NoLecternOutputPath
$model.textures.coal = "vivariumlibera:block/alchemy_table/coal_lit"
Write-ModelSet $NoLecternLitOutputPath
