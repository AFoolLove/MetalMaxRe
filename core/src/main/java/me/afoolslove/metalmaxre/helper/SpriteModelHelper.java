package me.afoolslove.metalmaxre.helper;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.WorldMapProperties;
import me.afoolslove.metalmaxre.editors.palette.Color;
import me.afoolslove.metalmaxre.editors.sprite.SpriteModel;
import me.afoolslove.metalmaxre.utils.NumberR;
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
        int width = spriteModel.getWidth();
        int height = spriteModel.getHeight();
        byte[] model = spriteModel.getModel();

        int xXX = 0x04050000;
        if (mapProperties instanceof WorldMapProperties) {
            xXX |= 0x9495;
        } else {
            xXX |= (mapProperties.intSpriteIndex() << 8) + (mapProperties.intSpriteIndex() + 1);
        }
        return generateSpriteModel(metalMaxRe, xXX, width, height, model);
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
        return generateSpriteModel(metalMaxRe, x00, x40, x80, xC0, width, height, model);
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
        return generateSpriteModel(metalMaxRe, xXX, width, height, model);
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int x00, int x40, int x80, int xC0,
                                               int width, int height, byte[] model) {
        byte[][] spriteModelData = TileSetHelper.generate(metalMaxRe, x00, x40, x80, xC0);
        List<byte[][]> diced = TileSetHelper.diced(0x08, 0x08, spriteModelData);

        byte[][] modelBytes = new byte[height * 0x08][width * 0x08];

        for (int i = 0; i < model.length; i++) {
            int x = (i % width) * 0x08;
            int y = (i / width) * 0x08;

            byte[][] piece = diced.get(model[i] & 0xFF);
            for (int pieceY = 0; pieceY < piece.length; pieceY++) {
                byte[] pieceColumn = piece[pieceY];
                for (int pieceX = 0; pieceX < pieceColumn.length; pieceX++) {
                    modelBytes[y + pieceY][x + pieceX] = pieceColumn[pieceX];
                }
            }
        }
        return modelBytes;
    }

    /**
     * 生成精灵模型
     *
     * @see TileSetHelper#palette(byte[][], Color[])
     */
    public static byte[][] generateSpriteModel(@NotNull MetalMaxRe metalMaxRe, int xXX,
                                               int width, int height, byte[] model) {
        return generateSpriteModel(metalMaxRe,
                NumberR.at(xXX, 3) & 0xFF,
                NumberR.at(xXX, 2) & 0xFF,
                NumberR.at(xXX, 1) & 0xFF,
                NumberR.at(xXX, 0) & 0xFF,
                width, height, model
        );
    }
}
