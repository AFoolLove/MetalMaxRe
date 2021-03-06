package me.afoolslove.metalmaxre.editor.monster;

import java.util.Arrays;

/**
 * 怪物组合
 * <p>
 * 0x00-0x09 普通怪物，值为怪物ID
 * 0x0A-0x0D 特殊怪物，值为特殊怪物组合ID
 *
 * @author AFoolLove
 */
public class MonsterGroup {
    public byte[] monsters;

    public MonsterGroup(byte[] monsters) {
        this.monsters = monsters;
    }

    public byte[] getMonsters() {
        return Arrays.copyOf(monsters, 0x0A);
    }

    public byte[] getSpecialMonsterGroups() {
        return Arrays.copyOf(monsters, 0x04);
    }
}
