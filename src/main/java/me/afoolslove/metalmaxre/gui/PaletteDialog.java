package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.ColorTool;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.nio.Buffer;

/**
 * 调色板Dialog编辑器
 *
 * @author AFoolLove
 */
public class PaletteDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JList<ImageIcon> palettes;

    public PaletteDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        DefaultListModel<ImageIcon> model = new DefaultListModel<>();
        palettes.setModel(model);

        // 使用JLabel的背景显示颜色
        for (int i = 0; i < ColorTool.COLORS.length; i++) {
            BufferedImage bufferedImage = new BufferedImage(0x20, 0x20, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(ColorTool.getColor(i));
            graphics.fillRect(0x00, 0x00, 0x40, 0x40);
            model.add(i, new ImageIcon(bufferedImage));
        }
    }
}
