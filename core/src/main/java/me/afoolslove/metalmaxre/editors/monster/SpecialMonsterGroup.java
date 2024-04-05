package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.util.Arrays;

/**
 * 特殊怪物组合，一共4种怪物
 * (怪物ID+怪物数量) * 4
 *
 * @author AFoolLove
 */
public class SpecialMonsterGroup {
    private final byte[] monsters;
    private final byte[] counts;

    public SpecialMonsterGroup(byte[] monsters) {
        this.monsters = monsters;
        this.counts = new byte[]{1, 1, 1, 1};
    }

    public SpecialMonsterGroup(byte[] monsters, byte[] counts) {
        this.monsters = monsters;
        this.counts = counts;
    }

    /**
     * 获取所有特殊怪物ID
     *
     * @return 所有特殊怪物ID
     */
    public byte[] monsters() {
        return Arrays.copyOf(monsters, monsters.length);
    }

    /**
     * 获取所有特殊怪物对应的数量
     *
     * @return 所有特殊怪物对应的数量
     */
    public byte[] counts() {
        return Arrays.copyOf(counts, counts.length);
    }

    /**
     * 获取指定特殊怪物ID
     *
     * @param index 索引
     * @return 特殊怪物ID
     */
    public byte getMonster(@Range(from = 0x00, to = 0x03) int index) {
        return monsters[index];
    }

    /**
     * 获取指定特殊怪物对应的数量
     *
     * @param index 索引
     * @return 特殊怪物对应的数量
     */
    public byte getCount(@Range(from = 0x00, to = 0x03) int index) {
        return counts[index];
    }

    /**
     * 获取指定特殊怪物，并转换为{@link GroupEntry}对象
     *
     * @param index 索引
     * @return 特殊怪物
     */
    public GroupEntry getEntry(@Range(from = 0x00, to = 0x03) int index) {
        return new GroupEntry(getMonster(index), getCount(index));
    }

    /**
     * 获取所有特殊怪物，并转换为{@link GroupEntry}对象
     *
     * @return 所有特殊怪物
     */
    public GroupEntry[] getEntries() {
        GroupEntry[] entries = new GroupEntry[4];
        for (int i = 0; i < 4; i++) {
            entries[i] = getEntry(i);
        }
        return entries;
    }

    /**
     * 设置特殊怪物ID
     *
     * @param index   索引
     * @param monster 怪物ID
     */
    public void setMonster(@Range(from = 0x00, to = 0x03) int index, byte monster) {
        monsters[index] = monster;
    }

    public void setMonster(@Range(from = 0x00, to = 0x03) int index, @NotNull GroupEntry entry) {
        setMonster(index, entry.getKey());
        setCount(index, entry.getValue());
    }

    /**
     * 设置特殊怪物对应的数量
     *
     * @param index 索引
     * @param count 数量
     */
    public void setCount(@Range(from = 0x00, to = 0x03) int index, byte count) {
        counts[index] = count;
    }

    public static class GroupEntry extends SingleMapEntry<Byte, Byte> {

        public GroupEntry(@NotNull Byte key, @NotNull Byte value) {
            super(key, value);
        }

        /**
         * 获取特殊怪物ID
         *
         * @return 特殊怪物ID
         */
        public byte getMonster() {
            return getKey();
        }

        /**
         * 获取特殊怪物ID
         *
         * @return 特殊怪物ID
         */
        public int intMonster() {
            return getMonster() & 0xFF;
        }

        /**
         * 获取特殊怪物对应的数量
         *
         * @return 特殊怪物对应的数量
         */
        public byte getCount() {
            return getValue();
        }

        /**
         * 获取特殊怪物对应的数量
         *
         * @return 特殊怪物对应的数量
         */
        public int intCount() {
            return getCount() & 0xFF;
        }
    }
}
