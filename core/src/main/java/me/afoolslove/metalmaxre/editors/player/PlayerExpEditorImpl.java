package me.afoolslove.metalmaxre.editors.player;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

/**
 * 经验值编辑器
 * <p>
 * 2-99级，0级、1级和99级以上不可更改（会破坏代码）
 * 可以修改玩家的升级的所需经验
 *
 * @author AFoolLove
 */
public class PlayerExpEditorImpl extends RomBufferWrapperAbstractEditor implements IPlayerExpEditor {
    private final DataAddress playerLevelExpAddress;

    /**
     * K：Level<p>
     * V：exp required
     */
    private final Map<Integer, Integer> experiences = new HashMap<>();

    public PlayerExpEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x27C52 - 0x10, 0x27D7A - 0x10));
    }

    public PlayerExpEditorImpl(@NotNull MetalMaxRe metalMaxRe, DataAddress playerLevelExpAddress) {
        super(metalMaxRe);
        this.playerLevelExpAddress = playerLevelExpAddress;
    }

    @Editor.Load
    public boolean onLoad(IPlayerEditor playerEditor) {
        // 读取前清空数据
        experiences.clear();

        // 读取升级到2-99级的所需经验，默认等级至少为1，0级和1级的经验值数据无法修改（会破坏ROM程序）
        // 一个等级经验值用3个字节表示
        byte[] levelExps = new byte[(playerEditor.getMaxLevel() - 1) * 3];
        getBuffer().get(getPlayerLevelExpAddress(), levelExps);
        for (int i = 0, maxLevel = playerEditor.getMaxLevel() - 2; i <= maxLevel; i++) {
            experiences.put(i + 2, NumberR.toInt(levelExps[i * 3], levelExps[(i * 3) + 1], levelExps[(i * 3) + 2]));
        }
        return true;
    }

    @Editor.Apply
    public boolean onApply() {
        // 写入升级到2-99级的所需经验
        // 使用experiences的大小来确认有效的等级数量
        // 一个等级经验值用3个字节表示
        byte[] levelExps = new byte[experiences.size() * 3];
        for (int i = 0, maxLevel = experiences.size(), index = 0; i < maxLevel; i++, index += 3) {
            int experience = experiences.get(i + 2);
            // 使用arraycopy将该等级的3个字节复制到levelExps中
            byte[] levelExp = NumberR.toByteArray(experience, 3, false);
            System.arraycopy(levelExp, 0, levelExps, index, 3);
        }
        getBuffer().put(getPlayerLevelExpAddress(), levelExps);
        return true;
    }

    /**
     * 设置升级到指定等级所需的经验值
     */
    @Override
    public void setLevelExp(int level, int experience) {
        experiences.put(level, experience & 0xFFFFFF);
    }

    /**
     * @return 所有等级（2-99）的所需的经验值
     */
    @Override
    public Map<Integer, Integer> getExperiences() {
        return experiences;
    }

    /**
     * @return 指定等级所需的经验值
     */
    @Override
    public int getLevelExp(int level) {
        return experiences.get(level);
    }

    @Override
    public DataAddress getPlayerLevelExpAddress() {
        return playerLevelExpAddress;
    }
}
