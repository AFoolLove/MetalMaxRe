package me.afoolslove.metalmaxre.desktop.adapter;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
import java.util.Set;

public class BoxSelectedAdapter extends MouseInputAdapter {
    private final JLabel component;
    private final Set<SelectListener> selectListeners = new HashSet<>();


    private Graphics graphics;
    private int lastX = -1, lastY = -1;
    private boolean hold;

    /**
     * 锁定特效
     */
    private int lockX = -1, lockY = -1;
    private int lockCount = 0;

    public BoxSelectedAdapter(JLabel component) {
        this.component = component;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (graphics == null) {
            graphics = component.getGraphics().create();
            graphics.setColor(new Color(0, 0, 255, 64));
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (hold) {
            // 不需要释放
            return;
        }
        if (graphics != null) {
            graphics.dispose();
            graphics = null;
        }
        // 清除框框
        component.repaint(lastX * 0x10, lastY * 0x10, 0x10, 0x10);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        mouseExited(e);
        mouseEntered(e);
        mouseMoved(e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            // Alt + 鼠标左键 锁定
            if (e.isAltDown() || e.isAltGraphDown()) {
                hold = false;
                mouseMoved(e);
                hold = true;
                for (SelectListener selectListener : getSelectListeners()) {
                    selectListener.selected(e.getX() / 0x10, e.getY() / 0x10);
                }
            } else {
                if (hold) {
                    hold = false;
                    mouseExited(e);
                    mouseEntered(e);
                    mouseMoved(e);
                }
            }
        }
//        if (e.getButton() == MouseEvent.BUTTON1) {
//            // 双击定住框框，否则取消定住
//            if (!hold) {
//                hold = e.getClickCount() == 2;
//                if (hold) {
//                    for (SelectListener selectListener : getSelectListeners()) {
//                        selectListener.selected(e.getX() / 0x10, e.getY() / 0x10);
//                    }
//                }
//            } else {
//                // 取消定住
//                hold = false;
//                mouseExited(e);
//                mouseEntered(e);
//                mouseMoved(e);
//            }
//        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            mouseExited(e);
            mouseEntered(e);
            mouseMoved(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (component.getIcon() == null || graphics == null || hold) {
            return;
        }
        int x = e.getX() / 0x10;
        int y = e.getY() / 0x10;
        if (lastX == x && lastY == y) {
            return;
        }
        component.repaint(lastX * 0x10, lastY * 0x10, 0x10, 0x10);
        lastX = x;
        lastY = y;
        graphics.fillRect(x * 0x10, y * 0x10, 0x10, 0x10);

        for (SelectListener selectListener : getSelectListeners()) {
            selectListener.select(x, y);
        }
    }

    public int getLastX() {
        return lastX;
    }

    public int getLastY() {
        return lastY;
    }

    public Set<SelectListener> getSelectListeners() {
        return selectListeners;
    }

    public void addListener(SelectListener listener) {
        selectListeners.add(listener);
    }

    public boolean removeListener(SelectListener listener) {
        return selectListeners.remove(listener);
    }

    /**
     * 选择监听器
     */
    public interface SelectListener {
        /**
         * 鼠标指向的实时位置
         */
        default void select(int x, int y) {
        }

        /**
         * 双击选中后执行
         */
        default void selected(int x, int y) {
        }
    }
}
