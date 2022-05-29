package me.afoolslove.metalmaxre.editors.palette;

/**
 * 基本颜色
 *
 * @author AFoolLove
 */
public class Color {
    private final int argb;

    public Color(int a, int r, int g, int b) {
        a &= 0xFF;
        r &= 0xFF;
        g &= 0xFF;
        b &= 0xFF;

        int argb = 0x00_00_00_00;
        argb += (a << 24);
        argb += (r << 16);
        argb += (g << 8);
        argb += b;
        this.argb = argb;
    }

    public Color(int r, int g, int b) {
        this(0, r, g, b);
    }

    public Color(byte a, byte r, byte g, byte b) {
        this(a & 0xFF, r & 0xFF, g & 0xFF, b & 0xFF);
    }

    public Color(byte r, byte g, byte b) {
        this(0, r, g, b);
    }

    public byte getAlpha() {
        return (byte) ((argb & 0xFF_000000) >>> 24);
    }

    public byte getRed() {
        return (byte) ((argb & 0x00FF_0000) >>> 16);
    }

    public byte getGreen() {
        return (byte) ((argb & 0x0000FF_00) >>> 8);
    }

    public byte getBlue() {
        return (byte) ((argb & 0x000000FF));
    }
}
