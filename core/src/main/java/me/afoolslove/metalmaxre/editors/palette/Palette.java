package me.afoolslove.metalmaxre.editors.palette;

import org.jetbrains.annotations.NotNull;

public class Palette {
    private final PaletteRow[] rows = new PaletteRow[0x04];

    public Palette(PaletteRow row0, PaletteRow row1, PaletteRow row2, PaletteRow row3) {
        this.rows[0x00] = row0;
        this.rows[0x01] = row1;
        this.rows[0x02] = row2;
        this.rows[0x03] = row3;
    }

    public PaletteRow getRow(int index) {
        return rows[index];
    }

    public void setRow(int index, PaletteRow row) {
        rows[index] = row;
    }

    public PaletteRow[] getRows() {
        return rows;
    }

    public Color[][] toColors(@NotNull SystemPalette systemPalette) {
        Color[][] colors = new Color[0x04][0x04];
        for (int row = 0; row < 0x04; row++) {
            colors[row] = getRow(row).toColors(systemPalette);
        }
        return colors;
    }

    public byte[] toByteArray() {
        byte[] bytes = new byte[0x04 * 0x03];
        for (int row = 0; row < 0x04; row++) {
            System.arraycopy(getRow(row).getRawPaletteRow(), 0x01, bytes, row * 0x03, 0x03);
        }
        return bytes;
    }
}
