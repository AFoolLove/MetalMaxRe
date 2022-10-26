package me.afoolslove.metalmaxre.desktop.adapter;

import org.jetbrains.annotations.NotNull;

import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/**
 * 得到焦点后全选文本
 */
public class FocusSelectAllAdapter extends FocusAdapter {
    private static final FocusSelectAllAdapter INSTANCE = new FocusSelectAllAdapter();

    public static FocusSelectAllAdapter getInstance() {
        return INSTANCE;
    }

    public static void addAdapter(@NotNull Component component) {
        FocusListener[] focusListeners = component.getFocusListeners();
        for (FocusListener focusListener : focusListeners) {
            if (focusListener == getInstance()) {
                return;
            }
        }
        // 未添加过
        component.addFocusListener(getInstance());
    }

    @Override
    public void focusGained(FocusEvent e) {
        if (e.getComponent() instanceof JTextComponent textField) {
            textField.selectAll();
        }
    }
}
