package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.editors.text.mapping.ICharMap;
import me.afoolslove.metalmaxre.event.Event;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.PreferencesUtils;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.prefs.Preferences;

/**
 * 提供了编辑器部分所需的方法
 *
 * @author AFoolLove
 */
public interface IRomEditor extends Serializable {
    /**
     * 获取主实例
     *
     * @return 主实例
     */
    @NotNull
    MetalMaxRe getMetalMaxRe();

    /**
     * 获取编辑器的id
     *
     * @return 编辑器的id
     */
    String getId();

    /**
     * 获取当前编辑器配置
     *
     * @return 当前编辑器配置
     */
    default Preferences getPreferences() {
        return PreferencesUtils.getPreferences().node("editor_manager").node(getId());
    }

    /**
     * 获取字符映射
     *
     * @return 字符映射
     */
    @NotNull
    default ICharMap getCharMap() {
        return getMetalMaxRe().getCharMap();
    }

    /**
     * 设置启用或禁用编辑器
     * <p>
     * *禁用后依然会加载，但不会进行写入
     *
     * @param enabled 启用或禁用编辑器
     */
    void setEnabled(boolean enabled);

    /**
     * 当前编辑器是否已启用
     *
     * @return 当前编辑器是否已启用
     */
    boolean isEnabled();

    /**
     * 获取当前编辑器的数据指针位置
     *
     * @return 指针位置
     */
    int position();

    /**
     * 设置当前编辑器的数据指针位置
     *
     * @param position 目标指针位置
     * @return 指针位置
     */
    int position(int position);

    /**
     * 从CHR ROM起始位置开始定位
     *
     * @param chrPosition 目标位置
     * @return 指针位置
     */
    int chrPosition(int chrPosition);

    /**
     * 从PRG ROM起始位置开始定位
     *
     * @param prgPosition 目标位置
     * @return 指针位置
     */
    int prgPosition(int prgPosition);


    /**
     * 根据 {@link DataAddress} 定位到目标位置
     * <p>
     * *通过调用 {@link IRomEditor#prgPosition(int)} 和 {@link IRomEditor#chrPosition(int)} 实现
     *
     * @param position 目标位置
     * @return 指针位置
     */
    default int position(@NotNull DataAddress position) {
        return position(position, 0);
    }

    /**
     * 根据 {@link DataAddress} 定位到目标位置
     * <p>
     * *通过调用 {@link IRomEditor#prgPosition(int)} 和 {@link IRomEditor#chrPosition(int)} 实现
     *
     * @param position 目标位置
     * @param offset   目标位置偏移
     * @return 指针位置
     */
    default int position(@NotNull DataAddress position, int offset) {
        return switch (position.getType()) {
            case PRG -> prgPosition(position.getStartAddress() + offset);
            case CHR -> chrPosition(position.getStartAddress() + offset);
        };
    }

    /**
     * 偏移当前编辑器的数据指针位置
     *
     * @param offset 偏移值
     * @return 指针位置
     */
    default int offsetPosition(int offset) {
        return position(position() + offset);
    }

    /**
     * 编辑器的所有数据地址
     *
     * @return 所有数据地址
     */
    default Map<String, Object> getDataAddresses() {
        return Collections.emptyMap();
    }

    /**
     * 添加数据地址
     *
     * @param dataAddressKey 数据地址键
     * @param dataAddress    数据地址，可传入 {@link DataAddress}和 {@link List<DataAddress>}
     */
    default void putDataAddress(@NotNull String dataAddressKey, @NotNull Object dataAddress) {
        getDataAddresses().put(dataAddressKey, dataAddress);
    }

    /**
     * 获取数据地址对象
     *
     * @param dataAddressKey 数据地址键
     * @return 数据地址
     */
    default Object getDataAddressObject(@NotNull String dataAddressKey) {
        return getDataAddresses().get(dataAddressKey);
    }

    /**
     * 获取数据地址
     *
     * @param dataAddressKey 数据地址键
     * @return 数据地址
     */
    default DataAddress getDataAddress(@NotNull String dataAddressKey) {
        Object address = getDataAddressObject(dataAddressKey);
        return address instanceof DataAddress ? (DataAddress) address : null;
    }

    /**
     * 获取数据地址列表
     *
     * @param dataAddressKey 数据地址键
     * @return
     */
    default List<DataAddress> getDataAddressList(@NotNull String dataAddressKey) {
        Object address = getDataAddressObject(dataAddressKey);
        return address instanceof List<?> ? (List<DataAddress>) address : null;
    }

    /**
     * 获取数据地址列表
     *
     * @param dataAddressKey 数据地址键
     * @return
     */
    default <K, V> Map<K, V> getDataAddressMap(@NotNull String dataAddressKey) {
        Object address = getDataAddressObject(dataAddressKey);
        return address instanceof Map<?, ?> ? (Map<K, V>) address : null;
    }

    @NotNull
    default RomBuffer getBuffer() {
        return getMetalMaxRe().getBuffer();
    }

    default void callEvent(@NotNull Event event) {
        getMetalMaxRe().getEventHandler().callEvent(event);
    }

    default void callEvent(@NotNull EditorEvent event, long completionTime) {
        getMetalMaxRe().getEventHandler().callEvent(event, completionTime);
    }
}
