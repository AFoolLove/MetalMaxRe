package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import org.jetbrains.annotations.Range;

import java.util.Map;

/**
 * 经验值编辑器
 *
 * @author AFoolLove
 */
public interface IPlayerExpEditor extends IRomEditor {
    @Override
    default String getId() {
        return "playerExpEditor";
    }

    /**
     * 获取所有等级（2-99）的所需的经验值
     *
     * @return 所有等级（2-99）的所需的经验值
     */
    Map<Integer, Integer> getExperiences();

    /**
     * 设置升级到指定等级所需的经验值
     *
     * @param level 被设置经验的等级
     * @param exp   到达这个等级需要的经验
     */
    void setLevelExp(int level, @Range(from = 0x00, to = 0x00FF_FFFF) int exp);

    /**
     * 获取指定等级所需的经验值
     *
     * @return 指定等级所需的经验值
     */
    int getLevelExp(int level);
}
