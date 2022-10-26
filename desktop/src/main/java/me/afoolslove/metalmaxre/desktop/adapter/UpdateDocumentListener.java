package me.afoolslove.metalmaxre.desktop.adapter;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * 将文本更新类型变更为单方法，并提供类型更新进行判断
 */
public abstract class UpdateDocumentListener implements DocumentListener {
    public enum Type {
        INSERT,
        REMOVE,
        CHANGED
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        update(Type.INSERT, e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        update(Type.REMOVE, e);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        update(Type.CHANGED, e);
    }

    public abstract void update(Type type, DocumentEvent event);
}
