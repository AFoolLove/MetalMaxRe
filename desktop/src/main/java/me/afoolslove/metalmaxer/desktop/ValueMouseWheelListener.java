package me.afoolslove.metalmaxer.desktop;

import javax.swing.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * 鼠标滚轮上滑下滑 切换到上一个值或下一个值
 */
public class ValueMouseWheelListener implements MouseWheelListener {
    private static final ValueMouseWheelListener INSTANCE = new ValueMouseWheelListener();

    public static ValueMouseWheelListener getInstance() {
        return INSTANCE;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        Object source = e.getSource();
        if (source instanceof JSpinner spinner) {
            int wheelRotation = e.getWheelRotation();
            if (wheelRotation == 1) {
                spinner.setValue(spinner.getNextValue());
            } else if (wheelRotation == -1) {
                spinner.setValue(spinner.getPreviousValue());
            }
        }
    }
}
