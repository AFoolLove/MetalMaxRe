package me.afoolslove.metalmaxre.patch;

import me.afoolslove.metalmaxre.GameHeader;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

/**
 * 补丁数据段
 */
public interface IPatchSegment {
    /**
     * @return 应用补丁的地址
     */
    DataAddress getAddress();

    /**
     * @return 原始数据
     */
    byte[] getRawData();

    /**
     * @return 补丁数据
     */
    byte[] getPatchData();

    /**
     * @return 是否已经应用过补丁
     */
    default boolean isApplied(@NotNull RomBuffer romBuffer) {
        DataAddress address = getAddress();
        byte[] patchData = getPatchData();

        for (int offset = 0; offset < patchData.length; offset++) {
            byte b = romBuffer.get(address.getStartAddress(offset) + GameHeader.HEADER_LENGTH);
            if (b != patchData[offset]) {
                // 数据不一致，判断失败
                return false;
            }
        }
        return true;
    }

    /**
     * @return 判断是否已撤销或未应用补丁
     */
    default boolean isRevoked(@NotNull RomBuffer romBuffer) {
        DataAddress address = getAddress();
        byte[] rawData = getRawData();

        for (int offset = 0; offset < rawData.length; offset++) {
            byte b = romBuffer.get(address.getStartAddress(offset) + GameHeader.HEADER_LENGTH);
            if (b != rawData[offset]) {
                // 数据不一致，判断失败
                return false;
            }
        }
        return true;
    }

    /**
     * 应用补丁
     */
    default void apply(@NotNull RomBuffer romBuffer) {
        romBuffer.put(getAddress(), getPatchData());
    }

    /**
     * 撤销补丁
     */
    default void revoke(@NotNull RomBuffer romBuffer) {
        romBuffer.put(getAddress(), getRawData());
    }
}
