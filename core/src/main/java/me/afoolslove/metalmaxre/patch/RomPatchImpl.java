package me.afoolslove.metalmaxre.patch;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RomPatchImpl implements IRomPatch, Serializable {
    private final String key;
    private final String description;
    private final String preKey;
    private final Map<String, List<IPatchSegmentWrapper>> patchSegments = new HashMap<>();

    public RomPatchImpl(@NotNull String key, @NotNull IPatchSegmentWrapper segments) {
        this(key, null, null, segments);
    }

    /**
     * 创建通用补丁
     */
    public RomPatchImpl(@NotNull String key, @Nullable String description, @Nullable String preKey, @NotNull IPatchSegmentWrapper segments) {
        this.key = key;
        this.description = description;
        this.preKey = preKey;
        this.patchSegments.put("*", new ArrayList<>(List.of(segments)));
    }

    /**
     * 可根据版本定制不同的补丁
     */
    public RomPatchImpl(@NotNull String key, @Nullable String description, @Nullable String preKey, @NotNull Map<String, List<IPatchSegmentWrapper>> segments) {
        this.key = key;
        this.description = description;
        this.preKey = preKey;
        this.patchSegments.putAll(segments);
    }

    @Override
    public @NotNull String getKey() {
        return key;
    }

    @Override
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    public @Nullable String getPreKey() {
        return preKey;
    }

    @Override
    public @NotNull Map<String, List<IPatchSegmentWrapper>> getPatchSegments() {
        return patchSegments;
    }
}
