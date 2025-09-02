package me.afoolslove.metalmaxre.editors.title;

import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.Tile;
import me.afoolslove.metalmaxre.editors.map.tileset.XXTileSet;

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
}
