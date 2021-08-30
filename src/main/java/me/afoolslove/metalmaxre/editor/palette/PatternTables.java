package me.afoolslove.metalmaxre.editor.palette;

import java.awt.*;
import java.io.OutputStream;

/**
 * 图案表
 * <p>
 * 0x10 * 0x10 = 0x100 tile
 * 0x01 tile = 8 + 8 = 0x10 byte
 * <p>
 * x00  CHR $00-3F
 * x40  CHR $40-7F
 * x80  CHR $80-BF
 * xC0  CHR $C0-FF
 * compositionA CHR表的组成数据 $00-$3F
 * compositionB CHR表的组成数据 $40-$7F
 *
 * @author AFoolLove
 */
public class PatternTables {
    public byte[] chr;
    public byte[] combinations;
    public Color[][] colors;

    /**
     * 生成一张图片到输出流中
     */
    public void generate( OutputStream outputStream){


    }
}
