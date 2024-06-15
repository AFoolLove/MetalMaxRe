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
public class SpecialMonsterGroup extends AbstractMonsterGroup {
    private final byte[] counts;

    protected SpecialMonsterGroup() {
        super();
        this.counts = null;
    }

    public SpecialMonsterGroup(byte[] monsters) {
        this(monsters, new byte[]{1, 1, 1, 1});
    }

    public SpecialMonsterGroup(byte[] monsters, byte[] counts) {
        super(new byte[0x04]);
        setMonsters(monsters);
        this.counts = counts;
    }

    /**
     * 获取指定特殊怪物ID
     *
     * @param index 索引
     * @return 特殊怪物ID
     */
    @Override
    public byte getMonster(@Range(from = 0x00, to = 0x03) int index) {
        return super.getMonster(index);
    }

    /**
     * 通过怪物编辑器将特殊怪物组转换为{@link Monster}对象
     * <p>
     * 0x00-0x03 特殊怪物组ID
     *
     * @param monsterEditor 怪物编辑器
     * @return 特殊怪物组怪物
     */
    public Monster getMonster(@NotNull IMonsterEditor monsterEditor,
                              @Range(from = 0x00, to = 0x03) int index) {
        return monsterEditor.getMonster(getMonster(index));
    }

    /**
     * 通过怪物编辑器将特殊怪物转换为{@link Monster}对象
     *
     * @param monsterEditor 怪物编辑器
     * @return 所有普通怪物
     */
    public Monster[] getMonsters(@NotNull IMonsterEditor monsterEditor) {
        Monster[] monsters = new Monster[0x04];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = getMonster(monsterEditor, i);
        }
        return monsters;
    }

    /**
     * 获取所有特殊怪物对应的数量
     *
     * @return 所有特殊怪物对应的数量
     */
    public byte[] getCounts() {
        return Arrays.copyOf(this.counts, this.counts.length);
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
     * 设置特殊怪物组
     *
     * @param monsters 特殊怪物组
     */
    @Override
    public void setMonsters(byte[] monsters,
                            @Range(from = 0x00, to = 0x03) int offset,
                            @Range(from = 0x00, to = 0x04) int length) {
        super.setMonsters(monsters, offset, length);
    }

    /**
     * 设置特殊怪物ID
     *
     * @param index   索引
     * @param monster 怪物ID
     */
    @Override
    public void setMonster(@Range(from = 0x00, to = 0x03) int index, byte monster) {
        super.setMonster(index, monster);
    }

    /**
     * 通过GroupEntry设置怪物和怪物对应的数量
     *
     * @param index 索引
     * @param entry GroupEntry
     */
    public void setMonster(@Range(from = 0x00, to = 0x03) int index, @NotNull GroupEntry entry) {
        setMonster(index, entry.getKey());
        setCount(index, entry.getValue());
    }

    /**
     * 设置特殊怪物对应的数量
     *
     * @param count 数量
     */
    public void setCounts(byte[] count) {
        setCounts(count, 0x00, count.length);
    }

    /**
     * 设置特殊怪物对应的数量
     *
     * @param count 数量
     */
    public void setCounts(byte[] count,
                          @Range(from = 0x00, to = 0x03) int offset,
                          @Range(from = 0x00, to = 0x04) int length) {
        System.arraycopy(count, 0, this.counts, offset, length);
    }

    /**
     * 设置特殊怪物对应的数量
     *
     * @param index 索引
     * @param count 数量
     */
    public void setCount(@Range(from = 0x00, to = 0x03) int index, byte count) {
        this.counts[index] = count;
    }

    /**
     * 是否为空怪物组，不会遇敌的区域
     *
     * @return 是否为空怪物组
     */
    @Override
    public boolean isEmptyMonsterGroup() {
        return this instanceof SpecialMonsterGroup.EmptySpecialMonsterGroup;
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

    /**
     * 空的怪物组，不要对其进行任何修改！
     */
    public static class EmptySpecialMonsterGroup extends SpecialMonsterGroup {
        public static final EmptySpecialMonsterGroup INSTANCE = new EmptySpecialMonsterGroup();

        private EmptySpecialMonsterGroup() {
            super();
        }

        @Override
        public boolean isEmptyMonsterGroup() {
            return true;
        }
    }
}
