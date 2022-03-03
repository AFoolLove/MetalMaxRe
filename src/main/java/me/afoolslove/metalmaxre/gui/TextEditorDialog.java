package me.afoolslove.metalmaxre.gui;

import javax.swing.*;
import java.awt.event.*;

public class TextEditorDialog extends JDialog {
    private JPanel contentPane;

    public TextEditorDialog() {
        setContentPane(contentPane);
        setModal(true);

        // call onCancel() when cross is clicked
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });

        // call onCancel() on ESCAPE
        contentPane.registerKeyboardAction(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    public static void main(String[] args) {
        TextEditorDialog dialog = new TextEditorDialog();
        dialog.pack();
        dialog.setVisible(true);
        System.exit(0);
    }
}
