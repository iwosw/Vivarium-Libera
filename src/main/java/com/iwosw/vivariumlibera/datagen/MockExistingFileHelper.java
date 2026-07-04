package com.iwosw.vivariumlibera.datagen;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Collections;

public class MockExistingFileHelper extends ExistingFileHelper {
    public MockExistingFileHelper() {
        super(Collections.emptyList(), Collections.emptySet(), false, null, null);
    }

    @Override
    public boolean exists(ResourceLocation loc, PackType type) {
        return true;
    }

    @Override
    public boolean exists(ResourceLocation loc, PackType type, String pathSuffix, String directory) {
        return true;
    }
}
