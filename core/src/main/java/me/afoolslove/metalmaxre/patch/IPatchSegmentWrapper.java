package me.afoolslove.metalmaxre.patch;

import me.afoolslove.metalmaxre.RomBuffer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface IPatchSegmentWrapper {
    /**
     * @return 获取补丁的描述
     */
    String getDescription();

    /**
     * @return 获取补丁数据段
     */
    List<IPatchSegment> getPatchSegments();

    /**
     * 应用补丁
     */
    default void apply(@NotNull RomBuffer romBuffer) {
        for (IPatchSegment patchSegment : getPatchSegments()) {
            patchSegment.apply(romBuffer);
        }
    }

    /**
     * 撤销补丁
     */
    default void revoke(@NotNull RomBuffer romBuffer) {
        for (IPatchSegment patchSegment : getPatchSegments()) {
            patchSegment.revoke(romBuffer);
        }
    }
}
