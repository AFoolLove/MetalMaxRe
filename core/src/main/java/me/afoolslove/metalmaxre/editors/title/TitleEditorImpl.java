package me.afoolslove.metalmaxre.editors.title;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.map.tileset.Tile;
import me.afoolslove.metalmaxre.editors.map.tileset.TileImage;
import me.afoolslove.metalmaxre.editors.map.tileset.XXTileSet;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;

public class TitleEditorImpl extends RomBufferWrapperAbstractEditor implements ITitleEditor {
    private final DataAddress logoTileSetAddress;
    private final DataAddress logoTileSetPaletteIndexAddress;
    private final DataAddress titleTileSetAddress;
    private final DataAddress titleTileSetPaletteIndexAddress;
    private final DataAddress logoTileImageAddress;

    private final Tile[][] titleTiles = new Tile[30][32];
    private final Tile[][] logoTiles = new Tile[30][32];
    private final XXTileSet[] logoTileSet = new XXTileSet[0x02];

    public TitleEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
        this.logoTileSetAddress = DataAddress.fromCHR(0x3F000, 0x3F3BF);
        this.logoTileSetPaletteIndexAddress = DataAddress.fromCHR(0x3F3C0, 0x3F3FF);
        this.titleTileSetAddress = DataAddress.fromCHR(0x3F400, 0x3F7BF);
        this.titleTileSetPaletteIndexAddress = DataAddress.fromCHR(0x3F7C0, 0x3F7FF);
        this.logoTileImageAddress = DataAddress.fromPRG(0x47810 - 0x10, 0x48010 - 0x10);
    }

    @Editor.Load
    public void onLoad() {
        // 初始化数据
        for (int i = 0; i < logoTileSet.length; i++) {
            XXTileSet xxTileSet = logoTileSet[i];
            if (xxTileSet == null) {
                logoTileSet[i] = new XXTileSet();
            }
            logoTileSet[i].fillZero();
        }


        byte[][] titleTiles = new byte[30][32];
        getBuffer().getAABytes(getTitleTileSetAddress(), 0, 32, titleTiles);

        byte[] titlePaletteIndexes = new byte[64];
        getBuffer().get(getTitleTileSetPaletteIndexAddress(), titlePaletteIndexes);

        byte[][] logoTiles = new byte[30][32];
        getBuffer().getAABytes(getLogoTileSetAddress(), 0, 32, logoTiles);
        byte[] logoPaletteIndexes = new byte[64];
        getBuffer().get(getLogoTileSetPaletteIndexAddress(), logoPaletteIndexes);

        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 32; x++) {
                this.titleTiles[y][x] = new Tile(titleTiles[y][x], (byte) 0);
                this.logoTiles[y][x] = new Tile(logoTiles[y][x], (byte) 0);
            }
        }

        for (int i = 0; i < titlePaletteIndexes.length; i++) {
            byte titlePaletteIndex = titlePaletteIndexes[i];
            byte logoPaletteIndex = logoPaletteIndexes[i];
            int offsetY = (i / 0x08) * 0x04;
            int offsetX = (i % 0x08) * 0x04;

            for (int paletteIndexOffset = 0; paletteIndexOffset < 0x04; paletteIndexOffset++) {
                int x = offsetX + (paletteIndexOffset % 2) * 2;
                int y = offsetY + (paletteIndexOffset / 2) * 2;
                byte atTitlePaletteIndex = NumberR.at(titlePaletteIndex, ((paletteIndexOffset + 1) * 2) - 1, 2, true);
                byte atLogoPaletteIndex = NumberR.at(logoPaletteIndex, ((paletteIndexOffset + 1) * 2) - 1, 2, true);
                if (y < 30) {
                    this.titleTiles[y + 0][x + 0].setPalette(atTitlePaletteIndex);
                    this.titleTiles[y + 0][x + 1].setPalette(atTitlePaletteIndex);
                    this.titleTiles[y + 1][x + 0].setPalette(atTitlePaletteIndex);
                    this.titleTiles[y + 1][x + 1].setPalette(atTitlePaletteIndex);

                    this.logoTiles[y + 0][x + 0].setPalette(atLogoPaletteIndex);
                    this.logoTiles[y + 0][x + 1].setPalette(atLogoPaletteIndex);
                    this.logoTiles[y + 1][x + 0].setPalette(atLogoPaletteIndex);
                    this.logoTiles[y + 1][x + 1].setPalette(atLogoPaletteIndex);
                }
            }
        }

        // 读取logo的额外图块
        position(getLogoTileImageAddress());
        for (XXTileSet xxTileSet : logoTileSet) {
            for (TileImage tile : xxTileSet.getTiles()) {
                getBuffer().get(tile.getTileData());
            }
        }
    }

    @Editor.Apply
    public void onApply() {
        byte[][] titleTiles = new byte[30][32];
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 32; x++) {
                titleTiles[y][x] = this.titleTiles[y][x].getTile();
            }
        }
        getBuffer().putAABytes(getTitleTileSetAddress(), 0, 32, titleTiles);

        byte[][] logoTiles = new byte[30][32];
        for (int y = 0; y < 30; y++) {
            for (int x = 0; x < 32; x++) {
                logoTiles[y][x] = this.logoTiles[y][x].getTile();
            }
        }
        getBuffer().putAABytes(getLogoTileSetAddress(), 0, 32, logoTiles);


        byte[] titlePaletteIndexes = new byte[64];
        byte[] logoPaletteIndexes = new byte[64];
        for (int i = 0; i < titlePaletteIndexes.length; i++) {
            int offsetY = (i / 0x08) * 0x04;
            int offsetX = (i % 0x08) * 0x04;

            for (int paletteIndexOffset = 0; paletteIndexOffset < 0x04; paletteIndexOffset++) {
                int y = offsetY + (paletteIndexOffset / 2) * 2;
                int x = offsetX + (paletteIndexOffset % 2) * 2;
                if (y < 30) {
                    titlePaletteIndexes[i] = NumberR.set(titlePaletteIndexes[i], this.titleTiles[y][x].getPalette(), ((paletteIndexOffset + 1) * 2) - 1, 2);
                    logoPaletteIndexes[i] = NumberR.set(logoPaletteIndexes[i], this.logoTiles[y][x].getPalette(), ((paletteIndexOffset + 1) * 2) - 1, 2);
                }
            }
        }
        getBuffer().put(getTitleTileSetPaletteIndexAddress(), titlePaletteIndexes);
        getBuffer().put(getTitleTileSetPaletteIndexAddress(), logoPaletteIndexes);

        // 写入logo的额外图块
        position(getLogoTileImageAddress());
        for (XXTileSet xxTileSet : logoTileSet) {
            for (TileImage tile : xxTileSet.getTiles()) {
                getBuffer().put(tile.getTileData());
            }
        }
    }

    @Override
    public Tile[][] getTitleTiles() {
        return titleTiles;
    }

    @Override
    public Tile[][] getLogoTiles() {
        return logoTiles;
    }

    @Override
    public XXTileSet[] getLogoTileSet() {
        return logoTileSet;
    }

    @Override
    public DataAddress getTitleTileSetAddress() {
        return titleTileSetAddress;
    }

    @Override
    public DataAddress getTitleTileSetPaletteIndexAddress() {
        return titleTileSetPaletteIndexAddress;
    }

    @Override
    public DataAddress getLogoTileSetAddress() {
        return logoTileSetAddress;
    }

    @Override
    public DataAddress getLogoTileSetPaletteIndexAddress() {
        return logoTileSetPaletteIndexAddress;
    }

    @Override
    public DataAddress getLogoTileImageAddress() {
        return logoTileImageAddress;
    }
}
