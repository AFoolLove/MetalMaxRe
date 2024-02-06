package me.afoolslove.metalmaxre.helper;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.monster.MonsterModel;
import me.afoolslove.metalmaxre.editors.monster.MonsterModelImpl;
import me.afoolslove.metalmaxre.editors.monster.MonsterModelType;
import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.palette.PaletteRow;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MonsterModelHelper {
    public static Color[][] generateMonsterModelPaletteFromId(@NotNull MetalMaxRe metalMaxRe, int monsterId) {
        MonsterModelImpl monsterModel = metalMaxRe.getEditorManager().getEditor(MonsterModelImpl.class);
        MonsterModel model = monsterModel.getMonsterModel(monsterId);
        return generateMonsterModelPaletteFromModel(metalMaxRe, model);
    }

    public static Color[][] generateMonsterModelPaletteFromModel(@NotNull MetalMaxRe metalMaxRe, @NotNull MonsterModel model) {
        return generateMonsterModelPalette(metalMaxRe, model.isDoublePalette(), model.getDoublePalettes());
    }

    public static Color[][] generateMonsterModelPalette(@NotNull MetalMaxRe metalMaxRe, boolean isDoublePalette, PaletteRow[] palettes) {
        MonsterModelImpl monsterModel = metalMaxRe.getEditorManager().getEditor(MonsterModelImpl.class);

        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        Color[][] colors = new Color[][]{
                {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)},
                {systemPalette.getBlack(), systemPalette.getWhite(), systemPalette.getColor(0x10), systemPalette.getColor(0x00)}
        };

        // 单调色板
        for (int i = 0; i < 0x03; i++) {
            colors[0x00][i + 1] = systemPalette.getColor(palettes[0x00].getPaletteRow()[i + 1] & 0xFF);
        }
        if (isDoublePalette) {
            // 双调色板
            for (int i = 0; i < 0x03; i++) {
                colors[0x01][i + 1] = systemPalette.getColor(palettes[0x01].getPaletteRow()[i + 1] & 0xFF);
            }
        }
        return colors;
    }

    public static byte[][] generateMonsterModel(@NotNull MetalMaxRe metalMaxRe, int monsterId) {
        MonsterModelImpl monsterModel = metalMaxRe.getEditorManager().getEditor(MonsterModelImpl.class);
        return generateMonsterModel(metalMaxRe, monsterModel.getMonsterModel(monsterId));
    }

    public static byte[][] generateMonsterModel(@NotNull MetalMaxRe metalMaxRe, @NotNull MonsterModel model) {
        int modelSize = model.intModelSize();                // 模型大小
        int chrIndex = model.intChrIndex();                  // 图形表
        Byte tileIndex = model.getTileIndex();               // 图块起始索引
        MonsterModelType modelType = model.getModelType();   // 模型数据类型
        byte[] modelData = model.getModelData();             // 模型数据
        byte[] customPaletteYs = model.getCustomPaletteYs(); // 自定义调色板Y值
        return generateMonsterModel(metalMaxRe, modelSize, model.isChrIndexIncremental(), chrIndex, tileIndex, modelType, modelData, customPaletteYs);
    }

    public static byte[][] generateMonsterModel(@NotNull MetalMaxRe metalMaxRe,
                                                int modelSize,
                                                boolean isChrIncremental,
                                                int chrIndex,
                                                Byte tileIndex,
                                                @NotNull MonsterModelType modelType,
                                                byte[] model,
                                                byte[] customPaletteYs
    ) {

        int width = (((modelSize & 0xF0) >>> 4) * 2);
        int height = ((modelSize & 0x0F) * 2);
        return generateMonsterModel(metalMaxRe, width, height, isChrIncremental, chrIndex, tileIndex, modelType, model, customPaletteYs);
    }

    public static byte[][] generateMonsterModel(@NotNull MetalMaxRe metalMaxRe,
                                                int width,
                                                int height,
                                                boolean isChrIncremental,
                                                int chrIndex,
                                                Byte tileIndex,
                                                @NotNull MonsterModelType modelType,
                                                byte[] model,
                                                byte[] customPaletteYs
    ) {
        byte[][] modelBytes = new byte[height * 0x08][width * 0x08];

        int[] xXX = {0x13, 0x13, 0x13, 0x13};
        if (isChrIncremental) {
            // 多图块组
            xXX[0] = chrIndex++;
            xXX[1] = chrIndex++;
            xXX[2] = chrIndex;
        } else {
            // 单图块组
            xXX[0] = chrIndex & 0xFF;
        }
//        if (isChrIncremental) {
//            // 多图块组
//            xXX[0] = metalMaxRe.getBuffer().getToInt(0x22D03 + (chrIndex & 0x0F));
//            xXX[1] = xXX[0x00] + 1;
//            xXX[2] = xXX[0x01] + 1;
//        } else {
//            // 单图块组
//            xXX[0] = chrIndex & 0xFF;
//        }

//        byte[][] monsterModelColor = TileSetHelper.generateMonsterModel(metalMaxRe, xXX[0], xXX[1], xXX[2], xXX[3]);
//        byte[][] monsterModelTile = new byte[0x100][0x40];
//        for (int y = 0; y < 0x10; y++) {
//            for (int x = 0; x < 0x10; x++) {
//                int tmpX = x * 8;
//                int tmpY = y * 8;
//                byte[] bytes = new byte[0x40];
//                for (int offsetY = 0; offsetY < 4; offsetY++) {
//                    System.arraycopy(monsterModelColor[tmpY + offsetY], tmpX + 0, bytes, (offsetY * 4) + 0, 4);
//                }
//                monsterModelTile[(y * 0x10) + x] = bytes;
//            }
//        }
        byte[][] monsterModelData = TileSetHelper.generate(metalMaxRe, xXX[0], xXX[1], xXX[2], xXX[3]);

        List<byte[][]> diced = TileSetHelper.diced(0x08, 0x08, monsterModelData);

//        byte[] paletteIndex = new byte[0x6B];
//        metalMaxRe.getBuffer().getPrg(0x22BB6, paletteIndex);

        if (modelType == MonsterModelType.B) {
            for (int i = 0; i < model.length; i++) {
                byte[][] piece = diced.get(model[i] & 0xFF);
                int x = (i % width) * 0x08;
                int y = (i / width) * 0x08;

                for (int pieceY = 0; pieceY < piece.length; pieceY++) {
                    byte[] pieceColumn = piece[pieceY];
                    for (int pieceX = 0; pieceX < pieceColumn.length; pieceX++) {
                        modelBytes[y + pieceY][x + pieceX] = pieceColumn[pieceX];
                    }
                }
            }
        } else if (modelType == MonsterModelType.A) { // 00-47
            int _tileIndex = tileIndex & 0xFF;
            _tileIndex++;

            List<byte[][]> tiles = new ArrayList<>();

            // 根据bit是否显示当前图块
            for (int i = 0; i < model.length; i++) {
                for (int k = 0, d = 0x80; k < 0x08; k++, d >>>= 1) { // D7-D0
                    int l = (model[i] & d) >>> (7 - k);

                    if (l == 0) {
                        tiles.add(null);
                        continue;
                    }
                    tiles.add(diced.get(_tileIndex & 0xFF));
                    _tileIndex++;
                }
            }
            byte[][] blackPiece = null;
            // 绘制
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    byte[][] piece = tiles.get((y * width) + x);
                    if (piece == null) {
                        if (blackPiece == null) {
                            blackPiece = new byte[0x08][0x08];
                        }
                        piece = blackPiece;
                    }

                    for (int pieceY = 0; pieceY < piece.length; pieceY++) {
                        byte[] pieceColumn = piece[pieceY];
                        for (int pieceX = 0; pieceX < pieceColumn.length; pieceX++) {
                            modelBytes[(y * 0x08) + pieceY][(x * 0x08) + pieceX] = pieceColumn[pieceX];
                        }
                    }
                }
            }
        }


        // 该列表的怪物模型索引可以自定义使用双调色板的Y值
        if (customPaletteYs != null) {
            int len = width * height;
            len /= 0x04;
            for (int i = 0; i < len; i++) {
                // 新的调色板Y值
                byte at = (byte) NumberR.at(customPaletteYs[i / 4] & 0xFF, 7 - (i % 4) * 2, 2, true);
                at <<= 4;

                int w = (i % (width / 0x02)) * 0x10;
                int h = (i / (width / 0x02)) * 0x10;

                // 将0x10*0x10的Y值重新设置
                for (int y = 0; y < 0x10; y++) {
                    for (int x = 0; x < 0x10; x++) {
                        int tmp = modelBytes[h + y][w + x];
                        tmp &= 0B0000_0011; // 保留X
                        modelBytes[h + y][w + x] = (byte) (tmp | at);
                    }
                }
            }
        }
        return modelBytes;
    }
}
