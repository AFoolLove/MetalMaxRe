package me.afoolslove.metalmaxre.editor.map.tileset;

/**
 * 能够帮助你快速的做一些操作
 *
 * @author AFoolLove
 */
public class TileSetHelper {
    private TileSetHelper() {
    }

    /**
     * 将图块倒序
     * 0x00-0x08 倒序
     * 0x09-0x10 倒序
     *
     * @param tiles 长度必须为 0x10
     */
    public static void reverse(byte[] tiles) {
        byte temp;
        // 0x00-0x08 倒序
        for (int i = 0; i < 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x08 - i - 1];
            tiles[0x08 - i - 1] = temp;
        }

        // 0x09-0x10 倒序
        for (int i = 0x08; i < 0x08 + 0x08 / 2; i++) {
            temp = tiles[i];
            tiles[i] = tiles[0x10 - (i - 0x08) - 1];
            tiles[0x10 - (i - 0x08) - 1] = temp;
        }
    }

    /**
     * 将图块左右翻转
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
