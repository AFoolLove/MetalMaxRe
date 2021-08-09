package me.afoolslove.metalmaxre.editor.map.tileset;

/**
 * 能够帮助你快速地做一些操作
 *
 * @author AFoolLove
 */
public class TileSetHelper {
    private TileSetHelper() {
    }

    /**
     * 将图块倒序
     * 0x00-0x07 倒序
     * 0x08-0x10 倒序
     * e.g:
     * 00 01 02 03 04 05 06 07 = 07 06 05 04 03 02 01 00
     * 08 09 0A 0B 0C 0D 0E 0F = 0F 0E 0D 0C 0B 0A 09 08
     *
     * @param tiles 长度必须为 0x10
     */
    public static void reverse(byte[] tiles) {
        byte temp;
        // 0x00-0x07 倒序
        for (int i = 0; i < 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x08 - i - 1];
            tiles[0x08 - i - 1] = temp;
        }

        // 0x08-0x10 倒序
        for (int i = 0x08; i < 0x08 + 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x10 - (i - 0x08) - 1];
            tiles[0x10 - (i - 0x08) - 1] = temp;
        }
    }

    /**
     * 将图块左右翻转
     * e.g:
     * 00 01 02 03 04 05 06 07 08 09 0A 0B 0C 0D 0E 0F = 0F 0E 0D 0C 0B 0A 09 08 07 06 05 04 03 02 01 00
     */
    public static void flip(byte[] tiles) {
        byte temp;
        for (int j = 0; j < tiles.length; j++) {
            temp = 0;
            for (int k = 7, h = 0x80; k >= 0; h >>>= 1, k--) {
                temp |= ((tiles[j] & h) >>> k) << (7 - k);
            }
            tiles[j] = temp;
        }
    }
}
