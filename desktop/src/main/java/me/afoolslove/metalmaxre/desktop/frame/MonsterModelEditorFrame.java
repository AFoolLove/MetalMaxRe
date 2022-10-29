package me.afoolslove.metalmaxre.desktop.frame;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.monster.MonsterModelImpl;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class MonsterModelEditorFrame extends AbstractEditorFrame {
    private JPanel contentPane;
    private JXList monsters;
    private JLabel monsterImage;
    private JXSearchField searchMonster;

    public MonsterModelEditorFrame(@NotNull Frame frame, @NotNull MetalMaxRe metalMaxRe) {
        super(frame, metalMaxRe);
        init("怪物编辑器", contentPane);
    }

    @Override
    protected void createLayout() {
        searchMonster.addActionListener(e -> {
            if (e.getActionCommand().isEmpty()) {
                monsters.setRowFilter(null);
            } else {
                monsters.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        return entry.getValue(0).toString().contains(e.getActionCommand());
                    }
                });
            }
        });

        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);

        MonsterModelImpl monsterModel = getMetalMaxRe().getEditorManager().getEditor(MonsterModelImpl.class);
        String[] monsterIDs = new String[monsterModel.getModelIndex().length];
        for (int i = 0; i < monsterIDs.length; i++) {
            monsterIDs[i] = String.format("%02X %s", i, textEditor.getMonsterName(i));
        }
        monsters.setListData(monsterIDs);

        monsters.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting() || monsters.getSelectedIndex() == -1) {
                return;
            }
            SystemPalette systemPalette = getMetalMaxRe().getSystemPalette();
            me.afoolslove.metalmaxre.editors.palette.Color[][] colors = new me.afoolslove.metalmaxre.editors.palette.Color[][]{
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                    {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)}
            };

            int monsterId = monsters.convertIndexToModel(monsters.getSelectedIndex());

            int modelIndex = monsterModel.getModelIndex()[monsterId] & 0xFF;
            int size = monsterModel.getModelSize()[modelIndex] & 0xFF;
            int modelLayoutIndexL = monsterModel.getModelLayoutIndex()[(modelIndex * 2)] & 0xFF;
            int modelLayoutIndexH = monsterModel.getModelLayoutIndex()[(modelIndex * 2) + 1] & 0xFF;
            int modelLayoutIndex = ((modelLayoutIndexH << 8) + modelLayoutIndexL) - 0x8000 + 0x22000;

            BufferedImage monsterImage = new BufferedImage((((size & 0xF0) >>> 4) * 2) * 0x08, ((size & 0x0F) * 2) * 0x08, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics1 = monsterImage.createGraphics();

            int[] xXX = {0x13, 0x13, 0x13, 0x13};
            byte ts = monsterModel.getModelTileSet()[modelIndex];
            if ((ts & 0xF0) == 0xF0) {
                // 多图块组
                xXX[0] = getMetalMaxRe().getBuffer().getToInt(0x22D03 + (ts & 0x0F));
                xXX[1] = xXX[0x00] + 1;
                xXX[2] = xXX[0x01] + 1;
            } else {
                // 单图块组
                xXX[0] = ts & 0xFF;
            }

            // TODO 双调色板怪物的调色板索引在怪物图像数据末尾3字节
            byte[][] monsterModelColor;
            int modelPaletteIndex = monsterModel.getModelPaletteIndex()[monsterId] & 0xFF;
            boolean isDoublePalette = (modelPaletteIndex & 0B1000_0000) != 0;
            if (isDoublePalette) {
                // 双调色板
                modelPaletteIndex &= 0x7F;
                modelPaletteIndex <<= 1;

                // 双调色板索引
                byte[] palettes = new byte[2];
                System.arraycopy(monsterModel.getModelDoublePalette(), modelPaletteIndex, palettes, 0, palettes.length);


                byte[] palette = new byte[0x03 + 0x03];
                getMetalMaxRe().getBuffer().getPrg(monsterModel.getMonsterModelPalette().getStartAddress(palettes[0] * 0x03), palette, 0, 3);
                getMetalMaxRe().getBuffer().getPrg(monsterModel.getMonsterModelPalette().getStartAddress(palettes[1] * 0x03), palette, 3, 3);

                for (int y = 0; y < 2; y++) {
                    for (int i = 0; i < 3; i++) {
                        colors[y][1 + i] = SystemPalette.DEFAULT_SYSTEM_PALETTE.getColor(palette[i]);
                    }
                }
            } else {
                // 单调色板
                byte[] palette = new byte[0x03];
                getMetalMaxRe().getBuffer().getPrg(monsterModel.getMonsterModelPalette().getStartAddress(modelPaletteIndex * 0x03), palette);
                for (int i = 0; i < palette.length; i++) {
                    colors[0][1 + i] = SystemPalette.DEFAULT_SYSTEM_PALETTE.getColor(palette[i]);
                }
            }

            monsterModelColor = TileSetHelper.generateMonsterModel(getMetalMaxRe(), xXX[0], xXX[1], xXX[2], xXX[3]);
            byte[][] monsterModelTile = new byte[0x100][0x40];
            for (int y = 0; y < 0x10; y++) {
                for (int x = 0; x < 0x10; x++) {
                    int tmpX = x * 8;
                    int tmpY = y * 8;
                    byte[] bytes = new byte[0x40];
                    for (int offsetY = 0; offsetY < 4; offsetY++) {
                        for (int offsetX = 0; offsetX < 4; offsetX++) {
                            bytes[(offsetY * 4) + offsetX] = monsterModelColor[tmpY + offsetY][tmpX + offsetX];
                        }
                    }
                    monsterModelTile[(y * 0x10) + x] = bytes;
                }
            }
            BufferedImage bufferedImage1;
            if (isDoublePalette) {
                bufferedImage1 = BufferedImageUtils.fromColors(TileSetHelper.generate(getMetalMaxRe(), xXX[0], xXX[1], xXX[2], xXX[3], colors));
            } else {
                bufferedImage1 = BufferedImageUtils.fromColors(TileSetHelper.generate(getMetalMaxRe(), xXX[0], xXX[1], xXX[2], xXX[3], colors[0]));
            }
            java.util.List<BufferedImage> diced = TileSetHelper.diced(0x08, 0x08, bufferedImage1);

            byte[] paletteIndex = new byte[0x6B];
            getMetalMaxRe().getBuffer().getPrg(0x22BB6, paletteIndex);

            if (modelIndex >= 0x48) {
                byte[] model = new byte[monsterImage.getWidth() * monsterImage.getHeight()];
                getMetalMaxRe().getBuffer().getPrg(modelLayoutIndex + 1, model);

                for (int i = 0; i < model.length; i++) {
                    BufferedImage bufferedImage = diced.get(model[i] & 0xFF);
                    int x = i % (monsterImage.getWidth() / 0x08);
                    int y = i / (monsterImage.getWidth() / 0x08);
                    graphics1.drawImage(bufferedImage, x * 0x08, y * 0x08, null);
                }
            } else {
                int w = monsterImage.getWidth() / 0x08;
                int h = monsterImage.getHeight() / 0x08;

                byte[] model = new byte[(int) Math.ceil((w * h) / 8.0)];
                getMetalMaxRe().getBuffer().getPrg(modelLayoutIndex + 1, model);
                byte[] paletteBytes = new byte[0x03];
                System.arraycopy(paletteIndex, modelIndex, paletteBytes, 0, paletteBytes.length);
                int palette = NumberR.toInt(true, paletteBytes);
                palette <<= 0x08;

                byte tileIndex = monsterModel.getModelLayout2()[modelIndex];
                tileIndex++;

                List<BufferedImage> tiles = new ArrayList<>();


                // 根据bit是否显示当前图块
                for (int i = 0; i < model.length; i++) {
                    for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                        int l = (model[i] & d) >>> (7 - k);

                        if (l == 0) {
                            tiles.add(null);
                            continue;
                        }
//                        byte[] bytes = monsterModelTile[tileIndex & 0xFF];
//                        me.afoolslove.metalmaxre.editors.palette.Color[] color = colors[palette >>> 30];
//                        palette <<= 0x02;
//
//                        BufferedImage tile = new BufferedImage(0x08, 0x08, BufferedImage.TYPE_INT_ARGB);
//                        Graphics2D graphics = tile.createGraphics();
//                        for (int y = 0; y < 0x08; y++) {
//                            for (int x = 0; x < 0x08; x++) {
//                                graphics.setColor(color[bytes[(y * 0x08) + x]].toAwtColor());
//                                graphics.fillRect(x, y, 1, 1);
//                            }
//                        }
//
//                        graphics.dispose();
//                        tiles.add(tile);
                        tiles.add(diced.get(tileIndex & 0xFF));
                        tileIndex++;
                    }
                }
                for (int y = 0; y < h; y++) {
                    for (int x = 0; x < w; x++) {
                        BufferedImage bufferedImage = tiles.get((y * w) + x);
                        if (bufferedImage == null) {
                            graphics1.setColor(Color.BLACK);
                            graphics1.fillRect((x * 0x08), (y * 0x08), 0x08, 0x08);
                            continue;
                        }
                        graphics1.drawImage(bufferedImage, (x * 0x08), (y * 0x08), null);
                    }
                }
            }

            graphics1.dispose();

            MonsterModelEditorFrame.this.monsterImage.setIcon(new ImageIcon(monsterImage));
        });
    }
}
