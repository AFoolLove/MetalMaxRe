package me.afoolslove.metalmaxre.editors.sprite;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.utils.SystemSprite;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * 系统精灵模型
 * <p>
 * 类型为0x04的精灵模型
 * <p>
 * 可以通过 {@link me.afoolslove.metalmaxre.helper.SpriteModelHelper} 来生成一张图像
 *
 * @author AFoolLove
 * @see me.afoolslove.metalmaxre.helper.SpriteModelHelper#generateSpriteModel(MetalMaxRe, int, SystemSpriteModel)
 */
public class SystemSpriteModel extends ArrayList<SystemSprite> {

    /**
     * 获取全部数据长度
     *
     * @return 全部数据长度
     */
    public int length() {
        return 1 + (size() * 0x04);
    }

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

    /**
     * 转换为ROM格式的字节数组
     *
     * @return 字节数组
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(length());
        outputStream.write(size());
        for (SystemSprite systemSprite : this) {
            outputStream.writeBytes(systemSprite.toByteArray());
        }
        return outputStream.toByteArray();
    }
}
