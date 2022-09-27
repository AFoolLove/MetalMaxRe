package me.afoolslove.metalmaxre.desktop.adapter;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MiddleMoveMouseAdapter extends MouseAdapter {
    private final JComponent comp;
    private Point origin;

    public MiddleMoveMouseAdapter(@NotNull JComponent comp) {
        this.comp = comp;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            comp.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            // 鼠标中键拖动
            origin = e.getPoint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        origin = null;
        comp.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (origin != null) {
            JViewport viewPort = (JViewport) SwingUtilities.getAncestorOfClass(JViewport.class, comp);
            if (viewPort != null) {
                int deltaX = origin.x - e.getX();
                int deltaY = origin.y - e.getY();

                Rectangle view = viewPort.getViewRect();
                view.x += deltaX;
                view.y += deltaY;

                comp.scrollRectToVisible(view);
            }
        }
    }
}
