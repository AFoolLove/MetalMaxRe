package me.afoolslove.metalmaxre.editor.monster;

/**
 * 特殊怪物组合，一共4种怪物
 * (怪物ID+怪物数量) * 4
 *
 * @author AFoolLove
 */
public class SpecialMonsterGroup {
    public byte[] monsters;
    public byte[] counts;

    public SpecialMonsterGroup(byte[] monsters) {
        this.monsters = monsters;
        this.counts = new byte[]{1, 1, 1, 1};
    }

    public SpecialMonsterGroup(byte[] monsters, byte[] counts) {
        this.monsters = monsters;
        this.counts = counts;
    }
}
