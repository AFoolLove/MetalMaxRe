package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.desktop.HexJSpinnerEditor;
import me.afoolslove.metalmaxre.desktop.ValueMouseWheelListener;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ToolsFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JSpinner eventCode;
    private JTextField eventRam;
    private JButton eventCodeToRam;
    private JButton ramToEventCode;
    private JComboBox<String> eventCodes;
    private JCheckBox alwaysOnTop;

    public ToolsFrame(@NotNull Frame frame) {
        super(frame);
        init("工具", contentPane);

        setAlwaysOnTop(true);
    }

    @Override
    protected void createLayout() {
        alwaysOnTop.addChangeListener(e -> setAlwaysOnTop(alwaysOnTop.isSelected()));

        Map<Integer, String> codes = new HashMap<>();
        // 读取字库
        InputStream resourceAsStream = ResourceManager.getAsStream("/event_codes.txt");
        if (resourceAsStream != null) {
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8))) {
                bufferedReader.lines().forEach(line -> {
                    int code = Integer.parseInt(line, 0, 2, 16);
                    codes.put(code, line);
                });
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "事件描述文件读取失败！", "事件", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "事件描述文件不存在！", "事件", JOptionPane.ERROR_MESSAGE);
        }

        String[] codeStrings = new String[0x100];
        for (int i = 0; i < codeStrings.length; i++) {
            String s = codes.get(i);
            if (s == null) {
                codeStrings[i] = String.format("%02X", i);
            } else {
                codeStrings[i] = s;
            }
        }
        eventCodes.setModel(new DefaultComboBoxModel<>(codeStrings));


        eventCode.addChangeListener(e -> {
            eventCodes.setSelectedIndex(((Number) eventCode.getValue()).intValue());
        });
        eventCodeToRam.addActionListener(e -> {
            int event = ((Number) eventCode.getValue()).intValue();
            eventRam.setText(String.format("%04X:%d", 0x441 + (event >>> 3), event & 0B0000_0111));
        });

        ramToEventCode.addActionListener(e -> {
            try {
                int event = Integer.parseInt(eventRam.getText(0, 4), 16);
                event -= 0x441;
                event <<= 3;
                event += Integer.parseInt(eventRam.getText(5, 1)) & 0B0000_0111;
                eventCode.setValue(event);
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
        });


        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                eventCode
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }
    }
}
