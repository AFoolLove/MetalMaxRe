package me.afoolslove.metalmaxre.editors.monster;

/**
 * 怪物图像数据格式类型
 */
public enum MonsterModelType {
    /**
     * 怪物图像通过顺序摆放，一般用于复杂贴图不重复的怪物
     */
    A,
    /**
     * 怪物图像可以自定义每个位置的图块id，多用于重复贴图高的怪物
     */
    B
}
