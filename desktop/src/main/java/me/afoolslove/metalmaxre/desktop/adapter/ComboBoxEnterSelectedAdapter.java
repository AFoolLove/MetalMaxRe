package me.afoolslove.metalmaxre.desktop.adapter;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 按下回车截取前两个字符作为为十六进制转换为数字并索引到该位置
 */
public class ComboBoxEnterSelectedAdapter extends KeyAdapter {
    private final JComboBox<?> comboBox;

    public ComboBoxEnterSelectedAdapter(@NotNull JComboBox<?> comboBox) {
        this.comboBox = comboBox;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // 按下回车截取前两个字符作为为十六进制转换为数字并索引到该位置
            String hexIndex = comboBox.getEditor().getItem().toString();
            hexIndex = hexIndex.trim();
            int index = comboBox.getSelectedIndex();
            try {
                index = Integer.parseInt(hexIndex, 16);
            } catch (NumberFormatException ignored) {
            }
            // 不能太小，不能太大
            index = Math.max(0x00, index);
            index = Math.min(index, comboBox.getModel().getSize() - 1);
            comboBox.setSelectedIndex(index);
        }
    }
}
