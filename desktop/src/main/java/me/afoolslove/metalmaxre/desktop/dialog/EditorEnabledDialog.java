package me.afoolslove.metalmaxre.desktop.dialog;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.desktop.JCheckBoxListCellRenderer;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class EditorEnabledDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<Class<?>> editorList;

    private final MultipleMetalMaxRe multipleMetalMaxRe;

    public EditorEnabledDialog(@NotNull Frame frame, @NotNull MultipleMetalMaxRe multipleMetalMaxRe) {
        super(frame);
        this.multipleMetalMaxRe = multipleMetalMaxRe;

        MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
        Path path = metalMaxRe.getBuffer().getPath();
        if (path != null) {
            setTitle(String.format("启用/禁用编辑器 [%s] - %s", path.getName(path.getNameCount() - 1), path));
        } else {
            setTitle(String.format("启用/禁用编辑器 [%s]", metalMaxRe.getBuffer().getVersion().getName()));
        }

        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setLocation(frame.getX(), frame.getY());

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);


        editorList.setSelectionModel(new DefaultListSelectionModel() {


            @Override
            public void setSelectionInterval(int index0, int index1) {
                if (super.isSelectedIndex(index0)) {
                    super.removeSelectionInterval(index0, index1);
                } else {
                    super.addSelectionInterval(index0, index1);
                }
            }
        });
        editorList.setCellRenderer(new JCheckBoxListCellRenderer(multipleMetalMaxRe));
        editorList.setListData(new Vector<>(metalMaxRe.getEditorManager().getEditors().keySet()));

        // 初始化选中状态
        ListModel<Class<?>> editorListModel = editorList.getModel();
        IEditorManager editorManager = multipleMetalMaxRe.current().getEditorManager();
        for (int i = 0, size = editorList.getModel().getSize(); i < size; i++) {
            IRomEditor romEditor = editorManager.getEditor((Class<? extends IRomEditor>) editorListModel.getElementAt(i));
            if (romEditor.isEnabled()) {
                editorList.setSelectedIndex(i);
            }
        }
    }

    private void onOK() {
        MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
        IEditorManager editorManager = metalMaxRe.getEditorManager();

        List<Class<?>> selectedValuesList = editorList.getSelectedValuesList();
        for (Map.Entry<Class<? extends IRomEditor>, IRomEditor> editorEntry : editorManager.getEditors().entrySet()) {
            editorEntry.getValue().setEnabled(selectedValuesList.contains(editorEntry.getKey()));
            System.out.printf("%s%s%n", editorEntry.getValue().isEnabled() ? "启用" : "禁用", editorEntry.getKey().getSimpleName());
        }
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }
}
