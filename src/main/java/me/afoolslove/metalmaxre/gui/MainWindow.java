package me.afoolslove.metalmaxre.gui;

import javax.swing.*;

/**
 * 主窗口
 *
 * @author AFoolLove
 */
public class MainWindow extends JFrame {

    private JPanel contentPane;

    public MainWindow() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        setContentPane(contentPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
}
