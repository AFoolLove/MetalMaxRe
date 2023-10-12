package me.afoolslove.metalmaxre.patch;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

/**
 * ROM的补丁
 */
public interface IRomPatch {
    /**
     * @return 获取补丁的key
     */
    @NotNull
    String getKey();

    /**
     * @return 获取补丁描述
     */
    @Nullable
    String getDescription();

    /**
     * @return 获取前置补丁key
     */
    @Nullable
    String getPreKey();

    /**
     * @return 获取所有补丁段
     */
    @NotNull
    Map<String, List<IPatchSegmentWrapper>> getPatchSegments();

    /**
     * @param romVersion 补丁适用的目标版本
     * @return 获取是否拥有适用目标版本的补丁
     */
    default boolean isTargetVersion(@NotNull String romVersion) {
        return getTargetPatchSegments(romVersion) != null;
    }

    /**
     * @param romVersion 补丁适用的目标版本
     * @return 获取目标版本的补丁
     */
    default List<IPatchSegmentWrapper> getTargetPatchSegments(@NotNull String romVersion) {
        // 判断是否拥有当前版本的补丁
        List<IPatchSegmentWrapper> patchSegmentWrappers = getPatchSegments().get(romVersion);
        if (patchSegmentWrappers == null) {
            // 不存在当前当前版本的补丁，获取是否拥有通用补丁
            patchSegmentWrappers = getPatchSegments().get("*");
        }
        return patchSegmentWrappers;
    }

    /**
     * @return 判断是否已应用补丁
     */
    default boolean isApplied(@NotNull RomBuffer romBuffer) {
        List<IPatchSegmentWrapper> patchSegmentWrappers = getTargetPatchSegments(romBuffer.getVersion().getId());
        if (patchSegmentWrappers == null) {
            // 没有适用该版本的补丁
            return false;
        }

        for (IPatchSegmentWrapper patchSegmentWrapper : patchSegmentWrappers) {
            // 判断所有补丁段
            for (IPatchSegment patchSegment : patchSegmentWrapper.getPatchSegments()) {
                if (patchSegment.isApplied(romBuffer)) {
                    continue;
                }
                // 没有应用这个补丁段
                return false;
            }
        }

        return true;
    }

    /**
     * @return 判断是否已撤销或未应用补丁
     */
    default boolean isRevoked(@NotNull RomBuffer romBuffer) {
        List<IPatchSegmentWrapper> patchSegmentWrappers = getTargetPatchSegments(romBuffer.getVersion().getId());
        if (patchSegmentWrappers == null) {
            // 没有适用该版本的补丁
            return false;
        }
        for (IPatchSegmentWrapper patchSegmentWrapper : patchSegmentWrappers) {
            // 判断所有原始数据段
            for (IPatchSegment patchSegment : patchSegmentWrapper.getPatchSegments()) {
                if (patchSegment.isRevoked(romBuffer)) {
                    continue;
                }
                // 没有撤销或未应用这个补丁段
                return false;
            }
        }
        return true;
    }

    /**
     * 应用补丁
     */
    default void apply(@NotNull RomBuffer romBuffer) {
        List<IPatchSegmentWrapper> patchSegmentWrappers = getTargetPatchSegments(romBuffer.getVersion().getId());
        if (patchSegmentWrappers == null) {
            // 没有适用该版本的补丁
            return;
        }
        for (IPatchSegmentWrapper patchSegmentWrapper : patchSegmentWrappers) {
            patchSegmentWrapper.apply(romBuffer);
        }
    }

    /**
     * 撤销补丁
     */
    default void revoke(@NotNull RomBuffer romBuffer) {
        List<IPatchSegmentWrapper> patchSegmentWrappers = getTargetPatchSegments(romBuffer.getVersion().getId());
        if (patchSegmentWrappers == null) {
            // 没有适用该版本的补丁
            return;
        }
        for (IPatchSegmentWrapper patchSegmentWrapper : patchSegmentWrappers) {
            patchSegmentWrapper.revoke(romBuffer);
        }
    }
}