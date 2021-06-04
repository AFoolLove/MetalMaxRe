package me.afoolslove.metalmaxre.editor.player;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * 经验值编辑器
 * <p>
 * 2-99级，0级、1级和99级以上不可更改（会破坏代码）
 * 可以修改玩家的升级的所需经验
 * <p>
 * 起始：0x27C52
 * 结束：0x27D7A
 * <p>
 * 2021年6月5日：已完成并通过测试基本编辑功能
 *
 * @author AFoolLove
 */
public class PlayerExperienceEditor extends AbstractEditor {
    /**
     * K：Level
     * V：exp required
     */
    private final Map<Integer, Integer> experiences = new HashMap<>();

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
        buffer.position(0x27C52);

        // 读取升级到2-99级的所需经验
        for (int i = 2; i <= 99; i++) {
            experiences.put(i, buffer.get() + (buffer.get() << 8) + (buffer.get() << 16));
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        buffer.position(0x27C52);

        // 写入升级到2-99级的所需经验
        for (int i = 2; i <= 99; i++) {
            int experience = experiences.get(i);
            buffer.put((byte) (experience & 0x0000FF));
            buffer.put((byte) ((experience & 0x00FF00) >> 8));
            buffer.put((byte) ((experience & 0xFF0000) >> 16));
        }
        return true;
    }

    /**
     * 设置升级到指定等级所需的经验值
     */
    public void setExperience(@Range(from = 2, to = 99) int level, @Range(from = 0x00, to = 0xFFFFFF) int experience) {
        experiences.put(level % 100, experience & 0xFFFFFF);
    }


    /**
     * @return 所有等级（2-99）的所需的经验值
     */
    public Map<Integer, Integer> getExperiences() {
        return experiences;
    }

    /**
     * @return 指定等级所需的经验值
     */
    public int getExperience(int level) {
        return experiences.get(level);
    }
}
