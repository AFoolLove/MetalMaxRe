package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.desktop.HexJSpinnerEditor;
import me.afoolslove.metalmaxre.desktop.Main;
import me.afoolslove.metalmaxre.desktop.ValueMouseWheelListener;
import me.afoolslove.metalmaxre.desktop.adapter.BoxSelectedAdapter;
import me.afoolslove.metalmaxre.editors.map.CameraMapPoint;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class DogSystemEditorFrame extends AbstractEditorFrame {

    private JPanel contentPane;
    private JSpinner townMap00;
    private JSpinner townX00;
    private JSpinner townY00;
    private JSpinner townX01;
    private JSpinner townMap01;
    private JSpinner townY01;
    private JSpinner townMap02;
    private JSpinner townY02;
    private JSpinner townX02;
    private JSpinner townY03;
    private JSpinner townMap03;
    private JSpinner townX03;
    private JSpinner townMap04;
    private JSpinner townX04;
    private JSpinner townY04;
    private JSpinner townY05;
    private JSpinner townX05;
    private JSpinner townMap05;
    private JSpinner townMap06;
    private JSpinner townY06;
    private JSpinner townX06;
    private JSpinner townX07;
    private JSpinner townMap07;
    private JSpinner townY07;
    private JSpinner townMap08;
    private JSpinner townX08;
    private JSpinner townY08;
    private JSpinner townY09;
    private JSpinner townMap09;
    private JSpinner townX09;
    private JSpinner townX0A;
    private JSpinner townMap0A;
    private JSpinner townY0A;
    private JSpinner townMap0B;
    private JSpinner townY0B;
    private JSpinner townX0B;
    private JButton gotoTown00;
    private JButton gotoTown01;
    private JButton gotoTown02;
    private JButton gotoTown03;
    private JButton gotoTown04;
    private JButton gotoTown05;
    private JButton gotoTown06;
    private JButton gotoTown07;
    private JButton gotoTown08;
    private JButton gotoTown09;
    private JButton gotoTown0A;
    private JButton gotoTown0B;
    private JSpinner teleportMap00;
    private JSpinner teleportMap01;
    private JSpinner teleportMap02;
    private JSpinner teleportMap03;
    private JSpinner teleportMap04;
    private JSpinner teleportMap05;
    private JSpinner teleportMap06;
    private JSpinner teleportMap07;
    private JSpinner teleportMap08;
    private JSpinner teleportMap09;
    private JSpinner teleportMap0A;
    private JSpinner teleportMap0B;
    private JSpinner teleportMap0C;
    private JSpinner teleportX00;
    private JSpinner teleportX01;
    private JSpinner teleportX02;
    private JSpinner teleportX03;
    private JSpinner teleportX04;
    private JSpinner teleportX05;
    private JSpinner teleportX06;
    private JSpinner teleportX07;
    private JSpinner teleportX08;
    private JSpinner teleportX09;
    private JSpinner teleportX0A;
    private JSpinner teleportX0B;
    private JSpinner teleportX0C;
    private JSpinner teleportY00;
    private JSpinner teleportY01;
    private JSpinner teleportY02;
    private JSpinner teleportY03;
    private JSpinner teleportY04;
    private JSpinner teleportY05;
    private JSpinner teleportY06;
    private JSpinner teleportY07;
    private JSpinner teleportY08;
    private JSpinner teleportY09;
    private JSpinner teleportY0A;
    private JSpinner teleportY0B;
    private JSpinner teleportY0C;
    private JButton gotoTeleport00;
    private JButton gotoTeleport01;
    private JButton gotoTeleport02;
    private JButton gotoTeleport03;
    private JButton gotoTeleport04;
    private JButton gotoTeleport05;
    private JButton gotoTeleport06;
    private JButton gotoTeleport07;
    private JButton gotoTeleport08;
    private JButton gotoTeleport09;
    private JButton gotoTeleport0A;
    private JButton gotoTeleport0B;
    private JButton gotoTeleport0C;
    private JButton save;

    public DogSystemEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("犬系统编辑器", contentPane);
    }


    @Override
    protected void createLayout() {
        IDogSystemEditor dogSystemEditor = getMetalMaxRe().getEditorManager().getEditor(IDogSystemEditor.class);

        gotoTown00.addActionListener(createGoto("设置传真城镇一坐标", null, townX00, townY00, gotoTown00));
        gotoTown01.addActionListener(createGoto("设置传真城镇二坐标", null, townX01, townY01, gotoTown01));
        gotoTown02.addActionListener(createGoto("设置传真城镇三坐标", null, townX02, townY02, gotoTown02));
        gotoTown03.addActionListener(createGoto("设置传真城镇四坐标", null, townX03, townY03, gotoTown03));
        gotoTown04.addActionListener(createGoto("设置传真城镇五坐标", null, townX04, townY04, gotoTown04));
        gotoTown05.addActionListener(createGoto("设置传真城镇六坐标", null, townX05, townY05, gotoTown05));
        gotoTown06.addActionListener(createGoto("设置传真城镇七坐标", null, townX06, townY06, gotoTown06));
        gotoTown07.addActionListener(createGoto("设置传真城镇八坐标", null, townX07, townY07, gotoTown07));
        gotoTown08.addActionListener(createGoto("设置传真城镇九坐标", null, townX08, townY08, gotoTown08));
        gotoTown09.addActionListener(createGoto("设置传真城镇十坐标", null, townX09, townY09, gotoTown09));
        gotoTown0A.addActionListener(createGoto("设置传真城镇十一坐标", null, townX0A, townY0A, gotoTown0A));
        gotoTown0B.addActionListener(createGoto("设置传真城镇十二坐标", null, townX0B, townY0B, gotoTown0B));

        gotoTeleport00.addActionListener(createGoto("设置传送机器城镇一坐标", teleportMap00, teleportX00, teleportY00, gotoTeleport00));
        gotoTeleport01.addActionListener(createGoto("设置传送机器城镇二坐标", teleportMap01, teleportX01, teleportY01, gotoTeleport01));
        gotoTeleport02.addActionListener(createGoto("设置传送机器城镇三坐标", teleportMap02, teleportX02, teleportY02, gotoTeleport02));
        gotoTeleport03.addActionListener(createGoto("设置传送机器城镇四坐标", teleportMap03, teleportX03, teleportY03, gotoTeleport03));
        gotoTeleport04.addActionListener(createGoto("设置传送机器城镇五坐标", teleportMap04, teleportX04, teleportY04, gotoTeleport04));
        gotoTeleport05.addActionListener(createGoto("设置传送机器城镇六坐标", teleportMap05, teleportX05, teleportY05, gotoTeleport05));
        gotoTeleport06.addActionListener(createGoto("设置传送机器城镇七坐标", teleportMap06, teleportX06, teleportY06, gotoTeleport06));
        gotoTeleport07.addActionListener(createGoto("设置传送机器城镇八坐标", teleportMap07, teleportX07, teleportY07, gotoTeleport07));
        gotoTeleport08.addActionListener(createGoto("设置传送机器城镇九坐标", teleportMap08, teleportX08, teleportY08, gotoTeleport08));
        gotoTeleport09.addActionListener(createGoto("设置传送机器城镇十坐标", teleportMap09, teleportX09, teleportY09, gotoTeleport09));
        gotoTeleport0A.addActionListener(createGoto("设置传送机器城镇十一坐标", teleportMap0A, teleportX0A, teleportY0A, gotoTeleport0A));
        gotoTeleport0B.addActionListener(createGoto("设置传送机器城镇十二坐标", teleportMap0B, teleportX0B, teleportY0B, gotoTeleport0B));
        gotoTeleport0C.addActionListener(createGoto("设置传送机器传送失败坐标", teleportMap0C, teleportX0C, teleportY0C, gotoTeleport0C));


        JSpinner[] townMapJSpinner = {
                townMap00, townMap01, townMap02,
                townMap03, townMap04, townMap05,
                townMap06, townMap07, townMap08,
                townMap09, townMap0A, townMap0B
        };
        JSpinner[] townXJSpinner = {
                townX00, townX01, townX02,
                townX03, townX04, townX05,
                townX06, townX07, townX08,
                townX09, townX0A, townX0B
        };
        JSpinner[] townYJSpinner = {
                townY00, townY01, townY02,
                townY03, townY04, townY05,
                townY06, townY07, townY08,
                townY09, townY0A, townY0B
        };

        JSpinner[] teleportMapJSpinner = {
                teleportMap00, teleportMap01, teleportMap02,
                teleportMap03, teleportMap04, teleportMap05,
                teleportMap06, teleportMap07, teleportMap08,
                teleportMap09, teleportMap0A, teleportMap0B, teleportMap0C
        };
        JSpinner[] teleportXJSpinner = {
                teleportX00, teleportX01, teleportX02,
                teleportX03, teleportX04, teleportX05,
                teleportX06, teleportX07, teleportX08,
                teleportX09, teleportX0A, teleportX0B, teleportX0C
        };
        JSpinner[] teleportYJSpinner = {
                teleportY00, teleportY01, teleportY02,
                teleportY03, teleportY04, teleportY05,
                teleportY06, teleportY07, teleportY08,
                teleportY09, teleportY0A, teleportY0B, teleportY0C
        };

        save.addActionListener(e -> {
            for (int i = 0; i < dogSystemEditor.getTownMaxCount(); i++) {
                dogSystemEditor.getTown(i).setKey((byte) (((Number) townMapJSpinner[i].getValue()).intValue() & 0xFF));
                CameraMapPoint townLocation = dogSystemEditor.getTownLocation(i);
                townLocation.setX((byte) (((Number) townXJSpinner[i].getValue()).intValue() & 0xFF));
                townLocation.setY((byte) (((Number) townYJSpinner[i].getValue()).intValue() & 0xFF));
            }

            for (int i = 0; i < (dogSystemEditor.getTeleportMaxCount() - 0x03); i++) {
                CameraMapPoint teleportLocation = dogSystemEditor.getTeleportLocation(i);
                teleportLocation.setMap((byte) (((Number) teleportMapJSpinner[i].getValue()).intValue() & 0xFF));
                teleportLocation.setX((byte) (((Number) teleportXJSpinner[i].getValue()).intValue() & 0xFF));
                teleportLocation.setY((byte) (((Number) teleportYJSpinner[i].getValue()).intValue() & 0xFF));
            }
        });

        for (int i = 0; i < dogSystemEditor.getTownMaxCount(); i++) {
            townMapJSpinner[i].setValue(dogSystemEditor.getTown(i).getKey() & 0xFF);

            CameraMapPoint townLocation = dogSystemEditor.getTownLocation(i);
            townXJSpinner[i].setValue(townLocation.intX());
            townYJSpinner[i].setValue(townLocation.intY());
        }
        for (int i = 0; i < (dogSystemEditor.getTeleportMaxCount() - 0x03); i++) {
            CameraMapPoint teleportLocation = dogSystemEditor.getTeleportLocation(i);
            teleportMapJSpinner[i].setValue(teleportLocation.intMap());
            teleportXJSpinner[i].setValue(teleportLocation.intX());
            teleportYJSpinner[i].setValue(teleportLocation.intY());
        }

        // 对JSpinner添加十六进制编辑器和鼠标滚轮切换数值
        for (JSpinner spinner : new JSpinner[]{
                townMap00, townX00, townY00,
                townMap01, townX01, townY01,
                townMap02, townX02, townY02,
                townMap03, townX03, townY03,
                townMap04, townX04, townY04,
                townMap05, townX05, townY05,
                townMap06, townX06, townY06,
                townMap07, townX07, townY07,
                townMap08, townX08, townY08,
                townMap09, townX09, townY09,
                townMap0A, townX0A, townY0A,
                townMap0B, townX0B, townY0B,
                teleportMap00, teleportX00, teleportY00,
                teleportMap01, teleportX01, teleportY01,
                teleportMap02, teleportX02, teleportY02,
                teleportMap03, teleportX03, teleportY03,
                teleportMap04, teleportX04, teleportY04,
                teleportMap05, teleportX05, teleportY05,
                teleportMap06, teleportX06, teleportY06,
                teleportMap07, teleportX07, teleportY07,
                teleportMap08, teleportX08, teleportY08,
                teleportMap09, teleportX09, teleportY09,
                teleportMap0A, teleportX0A, teleportY0A,
                teleportMap0B, teleportX0B, teleportY0B,
                teleportMap0C, teleportX0C, teleportY0C
        }) {
            spinner.setEditor(new HexJSpinnerEditor(spinner));
            spinner.addMouseWheelListener(ValueMouseWheelListener.getInstance());
        }

    }

    private ActionListener createGoto(String title, JSpinner gotoMap, JSpinner gotoX, JSpinner gotoY, JButton button) {
        return e -> {
            button.setEnabled(false);
            Main frame = (Main) getFrame();
            frame.getSelectListeners().add(new BoxSelectedAdapter.SelectListener() {
                @Override
                public void select(int x, int y) {
                    if (!isVisible()) {
                        button.setEnabled(true);
                        frame.getSelectListeners().remove(this);
                    }
                }

                @Override
                public void selected(int x, int y) {
                    frame.getSelectListeners().remove(this);
                    button.setEnabled(true);

                    if (isVisible()) {
                        int option = JOptionPane.showConfirmDialog(getFrame(),
                                String.format("Map:%02X X:%02X Y:%02X", frame.getSelectedMap(), x, y),
                                String.format("%s", title), JOptionPane.YES_NO_OPTION);
                        if (option == JOptionPane.YES_OPTION) {
                            if (gotoMap != null) {
                                gotoMap.setValue(frame.getSelectedMap());
                            }
                            gotoX.setValue(x);
                            gotoY.setValue(y);
                        }
                    }
                }
            });

            if (!getFrame().isActive()) {
                getFrame().setState(JFrame.ICONIFIED);
                getFrame().setState(JFrame.NORMAL);
            }
        };
    }
}
