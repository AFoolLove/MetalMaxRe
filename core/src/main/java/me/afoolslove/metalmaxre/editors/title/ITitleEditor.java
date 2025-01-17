package me.afoolslove.metalmaxre.editors.title;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.Tile;
import me.afoolslove.metalmaxre.editors.map.tileset.XXTileSet;
import me.afoolslove.metalmaxre.utils.DataAddress;

/**
 * 标题/Logo界面编辑器
 *
 * @author AFoolLove
 */
public interface ITitleEditor extends IRomEditor {
    @Override
    default String getId() {
        return "titleEditor";
    }

    /**
     * 标题数据
     *
     * @return 标题数据
     */
    Tile[][] getTitleTiles();

    /**
     * logo数据
     *
     * @return logo数据
     */
    Tile[][] getLogoTiles();

    /**
     * logo图块数据
     * <p>
     * *该数据不属于chr rom，但在logo界面会设置到PPU的X80和XC0
     *
     * @return logo图块数据
     */
    XXTileSet[] getLogoTileSet();

    /**
     * 标题图块集地址
     *
     * @return 标题图块集地址
     */
    DataAddress getTitleTileSetAddress();

    /**
     * 标题图块集颜色索引地址
     *
     * @return 标题图块集颜色索引地址
     */
    DataAddress getTitleTileSetPaletteIndexAddress();

    /**
     * logo图块集地址
     *
     * @return logo图块集地址
     */
    DataAddress getLogoTileSetAddress();

    /**
     * logo图块集颜色索引地址
     *
     * @return logo图块集颜色索引地址
     */
    DataAddress getLogoTileSetPaletteIndexAddress();

    /**
     * logo图块图像集地址
     *
     * @return logo图块图像集地址
     */
    DataAddress getLogoTileImageAddress();
}
