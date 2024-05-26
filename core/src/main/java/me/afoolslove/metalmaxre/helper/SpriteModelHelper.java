package me.afoolslove.metalmaxre.helper;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.editors.palette.SystemPalette;
import me.afoolslove.metalmaxre.editors.sprite.BattleSpriteModel;
import me.afoolslove.metalmaxre.editors.sprite.SpriteModel;
import me.afoolslove.metalmaxre.editors.sprite.SystemSpriteModel;
import me.afoolslove.metalmaxre.utils.NumberR;
import me.afoolslove.metalmaxre.utils.SystemSprite;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpriteModelHelper {


    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe,
                                               @NotNull MapProperties mapProperties,
                                               @NotNull SpriteModel spriteModel) {
        return generateSpriteModel(metalMaxRe, mapProperties.getIntSpriteTiles(), spriteModel);
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                               @NotNull SpriteModel spriteModel) {
        int width = spriteModel.getWidth();
        int height = spriteModel.getHeight();
        byte[] model = spriteModel.getModel();
        return generateSpriteModel(metalMaxRe, xXX, width, height, spriteModel.isEvenHorizontalFlip(), spriteModel.isOddHorizontalFlip(), model);
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                               int width, int height, byte[] model) {
        return generateSpriteModel(metalMaxRe, xXX, width, height, false, false, model);
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                               int width, int height, boolean evenHorizontalFlip, boolean oddHorizontalFlip, byte[] model) {
        return generateSpriteModel(metalMaxRe,
                NumberR.at(xXX, 3) & 0xFF,
                NumberR.at(xXX, 2) & 0xFF,
                NumberR.at(xXX, 1) & 0xFF,
                NumberR.at(xXX, 0) & 0xFF,
                width, height, evenHorizontalFlip, oddHorizontalFlip, model
        );
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                               @NotNull SpriteModel spriteModel) {
        int width = spriteModel.getWidth();
        int height = spriteModel.getHeight();
        byte[] model = spriteModel.getModel();
        return generateSpriteModel(metalMaxRe, x00, x40, x80, xC0, width, height, spriteModel.isEvenHorizontalFlip(), spriteModel.isOddHorizontalFlip(), model);
    }


    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                               int width, int height, byte[] model) {
        return generateSpriteModel(metalMaxRe, x00, x40, x80, xC0, width, height, false, false, model);
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                               int width, int height, boolean evenHorizontalFlip, boolean oddHorizontalFlip, byte[] model) {
        byte[][] spriteModelData = TileSetHelper.generate(metalMaxRe, x00, x40, x80, xC0);
        List<byte[][]> diced = TileSetHelper.diced(0x08, 0x08, spriteModelData);

        byte[][] modelBytes = new byte[height * 0x08][width * 0x08];

        for (int i = 0; i < model.length; i++) {
            int x = (i % width) * 0x08;
            int y = (i / width) * 0x08;

            byte[][] piece = diced.get(model[i] & 0xFF);
            boolean flip = false;
            if (i % 2 == 1) {
                // 偶数水平翻转
                if (evenHorizontalFlip) {
                    flip = true;
                }
            } else {
                // 奇数水平翻转
                if (oddHorizontalFlip) {
                    flip = true;
                }
            }

            if (!flip) {
                for (int pieceY = 0; pieceY < piece.length; pieceY++) {
                    byte[] pieceColumn = piece[pieceY];
                    for (int pieceX = 0; pieceX < pieceColumn.length; pieceX++) {
                        modelBytes[y + pieceY][x + pieceX] = pieceColumn[pieceX];
                    }
                }
            } else {
                for (int pieceY = 0; pieceY < piece.length; pieceY++) {
                    byte[] pieceColumn = piece[pieceY];
                    for (int pieceX = pieceColumn.length - 1; pieceX >= 0; pieceX--) {
                        modelBytes[y + pieceY][x + (pieceColumn.length - 1 - pieceX)] = pieceColumn[pieceX];
                    }
                }
            }
        }
        return modelBytes;
    }

    public static Color[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe,
                                                @NotNull MapProperties mapProperties,
                                                @NotNull SystemSpriteModel model) {
        return generateSpriteModel(metalMaxRe, mapProperties.getIntSpriteTiles(), model);
    }

    public static Color[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                                @NotNull SystemSpriteModel model) {
        return generateSpriteModel(metalMaxRe,
                NumberR.at(xXX, 3) & 0xFF,
                NumberR.at(xXX, 2) & 0xFF,
                NumberR.at(xXX, 1) & 0xFF,
                NumberR.at(xXX, 0) & 0xFF,
                model);
    }

    public static Color[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                                @NotNull SystemSpriteModel model) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        IPaletteEditor paletteEditor = metalMaxRe.getEditorManager().getEditor(IPaletteEditor.class);

        Color[][] spritePalette = {
                paletteEditor.getSpritePalette().get(0x00).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x01).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x02).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x03).toColors(systemPalette, true),
        };


        byte[][] spriteModelData = TileSetHelper.generate(metalMaxRe, x00, x40, x80, xC0);
        List<byte[][]> diced = TileSetHelper.diced(0x08, 0x08, spriteModelData);

        Color[][] modelBytes = new Color[model.getHeight()][model.getWidth()];

        byte[][] piece = new byte[0x08][0x08];
        for (SystemSprite systemSprite : model) {
            int offsetX = systemSprite.intX();
            int offsetY = systemSprite.intY();
            byte[][] tile = diced.get(systemSprite.intTile());
            System.arraycopy(tile, 0x00, piece, 0x00, tile.length);

            // 水平翻转
            if (systemSprite.isHorizontalFlip()) {
                TileSetHelper.horizontalFlip(tile);
            }
            // 垂直翻转
            if (systemSprite.isVerticalFlip()) {
                TileSetHelper.verticalFlip(tile);
            }

            Color[][] palette = TileSetHelper.palette(piece, spritePalette[systemSprite.getPaletteIndex()]);

            for (int y = 0; y < palette.length; y++) {
                Color[] tileRow = palette[y];
                System.arraycopy(tileRow, 0, modelBytes[offsetY + y], offsetX, tileRow.length);
            }
        }
        return modelBytes;
    }

    public static Color[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                                @NotNull BattleSpriteModel model) {
        SystemPalette systemPalette = metalMaxRe.getSystemPalette();
        IPaletteEditor paletteEditor = metalMaxRe.getEditorManager().getEditor(IPaletteEditor.class);

        Color[][] spritePalette = {
                paletteEditor.getSpritePalette().get(0x00).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x01).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x02).toColors(systemPalette, true),
                paletteEditor.getSpritePalette().get(0x03).toColors(systemPalette, true),
        };


        byte[][] spriteModelData = TileSetHelper.generate(metalMaxRe, x00, x40, x80, xC0);
        List<byte[][]> diced = TileSetHelper.diced(0x08, 0x08, spriteModelData);

        int width = model.getWidth();
        int height = model.getHeight();
        int offsetX = model.getOffsetX();
        int offsetY = model.getOffsetY();
        byte[] modelData = model.getModel();

        Color[][] modelBytes = new Color[(height * 0x08) + offsetY][(width * 0x08) + offsetX];

        byte[][] piece = new byte[0x08][0x08];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                byte tileId = modelData[(y * width) + x];
                if (tileId == 0) {
                    // ID为0的不会显示
                    continue;
                }
                byte[][] tile = diced.get(tileId);
                System.arraycopy(tile, 0x00, piece, 0x00, tile.length);

                Color[][] palette = TileSetHelper.palette(piece, spritePalette[model.getPaletteIndex()]);
                for (int row = 0; row < palette.length; row++) {
                    Color[] tileRow = palette[row];
                    System.arraycopy(tileRow, 0, modelBytes[(y * 0x08) + offsetY + row], (x * 0x08) + offsetX, tileRow.length);
                }
            }
        }
        return modelBytes;
    }
}
