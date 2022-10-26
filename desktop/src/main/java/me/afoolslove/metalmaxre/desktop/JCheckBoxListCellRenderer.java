package me.afoolslove.metalmaxre.desktop;

import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.computer.shop.IShopEditor;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.map.IMapEntranceEditor;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.monster.IMonsterEditor;
import me.afoolslove.metalmaxre.editors.monster.MonsterModelImpl;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerExpEditor;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.tank.ITankEditor;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class JCheckBoxListCellRenderer extends JCheckBox implements ListCellRenderer<Class<?>> {

    private final MultipleMetalMaxRe multipleMetalMaxRe;

    private final Map<Class<?>, String> editorNames = Map.ofEntries(
            Map.entry(IComputerEditor.class, "计算机编辑器"),
            Map.entry(IShopEditor.class, "商店编辑器"),
            Map.entry(IDataValueEditor.class, "数据值编辑器"),
            Map.entry(IItemEditor.class, "物品编辑器"),
            Map.entry(IDogSystemEditor.class, "犬系统编辑器"),
            Map.entry(IPaletteEditor.class, "调色板编辑器"),
            Map.entry(IPlayerEditor.class, "玩家编辑器"),
            Map.entry(IPlayerExpEditor.class, "玩家经验编辑器"),
            Map.entry(ISpriteEditor.class, "精灵编辑器"),
            Map.entry(ITankEditor.class, "坦克编辑器"),
            Map.entry(ITreasureEditor.class, "宝藏编辑器"),
            Map.entry(IMapEditor.class, "地图编辑器"),
            Map.entry(IMapPropertiesEditor.class, "地图属性编辑器"),
            Map.entry(IEventTilesEditor.class, "事件图块编辑器"),
            Map.entry(IWorldMapEditor.class, "世界地图编辑器"),
            Map.entry(IMapEntranceEditor.class, "边界和出入口编辑器"),
            Map.entry(ITileSetEditor.class, "图块编辑器"),
            Map.entry(ITextEditor.class, "文本编辑器"),
            Map.entry(IMonsterEditor.class, "怪物编辑器"),
            Map.entry(MonsterModelImpl.class, "怪物模型编辑器")
    );

    public JCheckBoxListCellRenderer(MultipleMetalMaxRe multipleMetalMaxRe) {
        this.multipleMetalMaxRe = multipleMetalMaxRe;
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends Class<?>> list, Class<?> value, int index, boolean isSelected, boolean cellHasFocus) {
        setText(editorNames.get(value));
        setSelected(isSelected);
        return this;
    }
}
