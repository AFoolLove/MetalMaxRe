package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.utils.SystemSprite;

import java.util.ArrayList;

/**
 * 系统精灵模型
 * <p>
 * 可以通过 {@link me.afoolslove.metalmaxre.helper.SpriteModelHelper} 来生成一张图像
 *
 * @author AFoolLove
 * @see me.afoolslove.metalmaxre.helper.SpriteModelHelper#generateSpriteModel(MetalMaxRe, int, SystemSpriteModel)
 */
public class SystemSpriteModel extends ArrayList<SystemSprite> {

    public int getWidth() {
        int width = 0;
        for (SystemSprite systemSprite : this) {
            width = Math.max(width, systemSprite.intX());
        }
        return width + 0x08;
    }

    public int getHeight() {
        int height = 0;
        for (SystemSprite systemSprite : this) {
            height = Math.max(height, systemSprite.intY());
        }
        return height + 0x08;
    }
}
