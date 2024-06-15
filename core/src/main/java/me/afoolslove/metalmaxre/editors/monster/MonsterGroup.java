package me.afoolslove.metalmaxre.editors.monster;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * 普通怪物组
 *
 * @author AFoolLove
 * @see MonsterRealm
 */
public class MonsterGroup extends AbstractMonsterGroup {
    protected MonsterGroup() {
        super();
    }

    public MonsterGroup(byte[] monsters) {
        super(new byte[0x0A]);
        setMonsters(monsters);
    }

    /**
     * 获取普通怪物
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     *
     * @return 普通怪物
     */
    @Override
    public byte getMonster(@Range(from = 0x00, to = 0x09) int index) {
        return super.getMonster(index);
    }

    /**
     * 通过怪物编辑器将普通怪物转换为{@link Monster}对象
     * <p>
     * 0x00-0x09 普通怪物，值为怪物ID
     *
     * @param monsterEditor 怪物编辑器
     * @return 普通怪物
     */
    public Monster getMonster(@NotNull IMonsterEditor monsterEditor,
                              @Range(from = 0x00, to = 0x09) int index) {
        return monsterEditor.getMonster(getMonster(index));
    }

    /**
     * 通过怪物编辑器将普通怪物转换为{@link Monster}对象
     *
     * @param monsterEditor 怪物编辑器
     * @return 所有普通怪物
     */
    public Monster[] getMonsters(@NotNull IMonsterEditor monsterEditor) {
        Monster[] monsters = new Monster[0x0A];
        for (int i = 0; i < monsters.length; i++) {
            monsters[i] = getMonster(monsterEditor, i);
        }
        return monsters;
    }

    /**
     * 设置普通怪物组
     *
     * @param monsters 普通怪物组数据
     * @param offset   偏移
     * @param length   长度
     */
    @Override
    public void setMonsters(byte[] monsters,
                            @Range(from = 0x00, to = 0x09) int offset,
                            @Range(from = 0x00, to = 0x0A) int length) {
        super.setMonsters(monsters, offset, length);
    }

    /**
     * 设置普通怪物ID
     *
     * @param index   索引
     * @param monster 怪物ID
     */
    @Override
    public void setMonster(@Range(from = 0x00, to = 0x09) int index, byte monster) {
        super.setMonster(index, monster);
    }

    /**
     * 是否为空怪物组，不会遇敌的区域
     *
     * @return 是否为空怪物组
     */
    public boolean isEmptyMonsterGroup() {
        return this instanceof EmptyMonsterGroup;
    }

    /**
     * 空的怪物组，不要对其进行任何修改！
     */
    public static class EmptyMonsterGroup extends MonsterGroup {
        public static final EmptyMonsterGroup INSTANCE = new EmptyMonsterGroup();

        private EmptyMonsterGroup() {
            super();
        }

        @Override
        public boolean isEmptyMonsterGroup() {
            return true;
        }
    }
}
