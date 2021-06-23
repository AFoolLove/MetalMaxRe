package me.afoolslove.metalmaxre.tiled;

import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.items.ItemsEditor;
import me.afoolslove.metalmaxre.editor.map.*;
import me.afoolslove.metalmaxre.editor.map.events.EventTile;
import me.afoolslove.metalmaxre.editor.map.events.EventTilesEditor;
import me.afoolslove.metalmaxre.editor.treasure.Treasure;
import me.afoolslove.metalmaxre.editor.treasure.TreasureEditor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.mapeditor.core.*;

import java.awt.*;

/**
 * Tiled地图
 * <p>
 * 可以生成 Tiled 能够读取的地图数据
 * 不包含世界地图
 *
 * @author AFoolLove
 */
public class TiledMap {

    /**
     * 生成
     */
    public static Map create(@Range(from = 0x01, to = MapEditor.MAP_MAX_COUNT - 1) int map, @NotNull TileSet tileSet) {
        var mapPropertiesEditor = EditorManager.getEditor(MapPropertiesEditor.class);
        var mapEditor = EditorManager.getEditor(MapEditor.class);
        var eventTilesEditor = EditorManager.getEditor(EventTilesEditor.class);
        var treasureEditor = EditorManager.getEditor(TreasureEditor.class);
        var itemsEditor = EditorManager.getEditor(ItemsEditor.class);
        var mapEntranceEditor = EditorManager.getEditor(MapEntranceEditor.class);


        int nextLayerId = 0;
        int nextObjectId = 0;

        // 获取目标地图属性
        MapProperties mapProperties = mapPropertiesEditor.getMapProperties(map);

        // 地图宽度和高度
        int width = mapProperties.width & 0xFF;
        int height = mapProperties.height & 0xFF;

        // 创建TiledMap，设置地图宽度和高度
        Map tiledMap = new Map(width, height);
        // Tile的宽度和高度
        tiledMap.setTileWidth(16);
        tiledMap.setTileHeight(16);

        // 添加这个图块集
        if (!tiledMap.getTileSets().contains(tileSet)) {
            tiledMap.getTileSets().add(tileSet);
        }

        // ----------------

        // 填充图块层，比原地图大6*6，以便观察
        TileLayer fillTileLayer = new TileLayer(3 + width + 3, 3 + height + 3);
        fillTileLayer.setName("fillTile");
        fillTileLayer.setId(nextLayerId++);
        // 左上角偏移3tile(1tile=16)，四周均匀的分布为3tile
        fillTileLayer.setOffsetX(-(0x10 * 3));
        fillTileLayer.setOffsetY(-(0x10 * 3));

        // 获取填充的Tile，并将该图层全部设置为该Tile
        Tile fillTile = tileSet.getTile(mapProperties.fillTile & 0xFF);
        Rectangle fillTileLayerBounds = fillTileLayer.getBounds();
        for (int y = 0; y < fillTileLayerBounds.height; y++) {
            for (int x = 0; x < fillTileLayerBounds.width; x++) {
                fillTileLayer.setTileAt(x, y, fillTile);
            }
        }

        // ----------------

        // 隐藏tile层，（门打开后的图块等
        TileLayer hideTileLayer = new TileLayer(width, height);
        hideTileLayer.setId(nextLayerId++);
        hideTileLayer.setName("hideTileLayer");

        // 获取隐藏的Tile，并将该图层全部设置为该Tile
        Tile hideTile = tileSet.getTile(mapProperties.hideTile & 0x7F);
        Rectangle hideTileLayerBounds = hideTileLayer.getBounds();
        for (int y = 0; y < hideTileLayerBounds.height; y++) {
            for (int x = 0; x < hideTileLayerBounds.width; x++) {
                hideTileLayer.setTileAt(x, y, hideTile);
            }
        }

        // ----------------

        // 主地图
        TileLayer mapLayer = new TileLayer(width, height);
        mapLayer.setId(nextLayerId++);
        mapLayer.setName("map");

        // 写入地图
        MapBuilder mapBuilder = mapEditor.getMap(map);
        // 记录坐标
        int index = 0;
        for (MapTile mapTile : mapBuilder) {
            // 获取tile
            Tile tile = tileSet.getTile(mapTile.getTile() & 0x7F);
            for (int i = 0, count = mapTile.getCount(); i < count; i++, index++) {
                // 设置tile
                mapLayer.setTileAt(index % width, index / width, tile);
            }
        }

        // ----------------

        // 事件图块，显示时会覆盖对应的图块，相当于事件条件达成
        Group eventGroup = new Group();
        eventGroup.setId(nextLayerId++);
        eventGroup.setName("events");
        // 默认隐藏
        eventGroup.setVisible(false);
        // 判断该地图是否存在事件图块
        if (mapProperties.hasEventTile()) {
            // 获取该地图的所有事件图块
            for (var entry : eventTilesEditor.getEventTile(map).entrySet()) {
                TileLayer eventLayer = new TileLayer(width, height);
                eventLayer.setId(nextLayerId++);
                // event:index
                // 事件内存地址和地址的bit位
                eventLayer.setName(String.format("%04X:%01X", 0x0441 + ((entry.getKey() & 0xF8) >>> 3), (entry.getKey() & 0x07)));

                // 设置该事件影响的图块
                for (EventTile eventTile : entry.getValue()) {
                    eventLayer.setTileAt(eventTile.x & 0xFF, eventTile.y & 0xFF, tileSet.getTile(eventTile.tile & 0xfF));
                }
                // 添加到事件图块组中
                eventGroup.getLayers().add(eventLayer);
            }
        }

        // ----------------

        // 宝藏层
        ObjectGroup treasureGroup = new ObjectGroup();
        treasureGroup.setId(nextObjectId++);
        treasureGroup.setName("treasure");

        // 查找该地图的宝藏，并使用 16*16 的矩形对象标出
        for (Treasure treasure : treasureEditor.findMap(map)) {
            MapObject mapObject = new MapObject(treasure.x & 0xFF, treasure.y & 0xFF, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 添加标志，Tiled程序可以识别
            mapObject.setType("treasure");
            // 设置物品ID和物品当前的名称
            mapObject.setName(String.format("%02X|%s", treasure.item, itemsEditor.getItem(treasure.item)));

            // 添加宝藏
            treasureGroup.addObject(mapObject);
        }

        // ----------------

        // 当前地图的边界和出入口
        ObjectGroup entrances = new ObjectGroup();
        entrances.setId(nextObjectId++);
        entrances.setName("entrances");

        // 获取当前地图的边界和出入口
        MapEntrance mapEntrance = mapEntranceEditor.getMapEntrance(map);
        for (var entry : mapEntrance.getEntrances().entrySet()) {
            MapPoint inPoint = entry.getKey();
            MapPoint outPoint = entry.getValue();
            // 设置入口坐标
            MapObject mapObject = new MapObject(inPoint.intX() * 0x10, inPoint.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 设置出口坐标为名称 Map:X:Y
            mapObject.setName(String.format("%02X:%02X:%02X", outPoint.intMap(), outPoint.intX(), outPoint.intY()));

            // TODO 没有添加边界属性

            // 添加到入口的对象层
            entrances.addObject(mapObject);
        }


        // ----------------

        tiledMap.addLayer(fillTileLayer);
        tiledMap.addLayer(hideTileLayer);
        tiledMap.addLayer(mapLayer);
        tiledMap.addLayer(eventGroup);
        tiledMap.addLayer(treasureGroup);
        tiledMap.addLayer(entrances);

        // 不可抗力的强制属性（写入文件时）
        // 旧版本没有的
        tiledMap.setTiledversion("");
        tiledMap.setNextlayerid(nextLayerId);
        tiledMap.setNextobjectid(nextObjectId);
        tiledMap.setInfinite(0);
        // 旧版本有的
        for (Tile tile : tileSet) {
            if (tile.getType() == null) {
                tile.setType("");
            }
        }
        // --- END 可以看出来，新bug增加了
        return tiledMap;
    }
}






























