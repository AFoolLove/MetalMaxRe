package me.afoolslove.metalmaxre.tiled;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.events.WorldEventTile;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.LineDirection;
import me.afoolslove.metalmaxre.editors.monster.IMonsterEditor;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mapeditor.core.Map;
import org.mapeditor.core.Properties;
import org.mapeditor.core.*;
import org.mapeditor.io.TMXMapWriter;
import org.mapeditor.util.BasicTileCutter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Tiled地图
 * <p>
 * 可以生成 Tiled 能够读取的地图数据
 *
 * @author AFoolLove
 */
public class TiledMapUtils {
    // FFFFFFFF-FFFF-FFFF
    public static final Pattern TILESET_NAME_PATTERN = Pattern.compile("([0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4})$");
    // FFFFFFFF
    public static final Pattern SPRITE_NAME_PATTERN = Pattern.compile("([0-9a-fA-F]{8})$");
    public static final Pattern ENTRANCE_NAME_PATTERN = Pattern.compile("([0-9a-fA-F]{8})$");

    /**
     * 生成Tiled地图
     */
    public static Map create(@NotNull MetalMaxRe metalMaxRe, int map, @NotNull TileSet tileSet, @NotNull TileSet spriteTileSet) {
        var editorManager = metalMaxRe.getEditorManager();

        ITextEditor textEditor = editorManager.getEditor(ITextEditor.class);
        IMapPropertiesEditor mapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        IMapEditor mapEditor = editorManager.getEditor(IMapEditor.class);
        IEventTilesEditor eventTilesEditor = editorManager.getEditor(IEventTilesEditor.class);
        ITreasureEditor treasureEditor = editorManager.getEditor(ITreasureEditor.class);
//        IItemEditor itemsEditor = editorManager.getEditor(IItemEditor.class);
        IMapEntranceEditor mapEntranceEditor = editorManager.getEditor(IMapEntranceEditor.class);
        IComputerEditor<Computer> computerEditor = editorManager.getEditor(IComputerEditor.class);
        ISpriteEditor spriteEditor = editorManager.getEditor(ISpriteEditor.class);


        int nextLayerId = 0;
        int nextObjectId = 0;

        // 获取目标地图属性
        MapProperties mapProperties = mapPropertiesEditor.getMapProperties(map);

        // 地图宽度和高度
        int width = mapProperties.intWidth();
        int height = mapProperties.intHeight();

        // 创建TiledMap，设置地图宽度和高度
        Map tiledMap = new Map(width, height);
        // Tile的宽度和高度
        tiledMap.setTileWidth(16);
        tiledMap.setTileHeight(16);

        // 添加图块集
        if (!tiledMap.getTileSets().contains(tileSet)) {
            tiledMap.getTileSets().add(tileSet);
        }
        // 添加精灵图块集
        if (!tiledMap.getTileSets().contains(spriteTileSet)) {
            tiledMap.getTileSets().add(spriteTileSet);
        }

        // ----------------

        // 设置地图属性
        tiledMap.getProperties().setProperty("D3|地下地图", Boolean.toString(mapProperties.isUnderground()));
        tiledMap.getProperties().setProperty("D2|事件图块", Boolean.toString(mapProperties.hasEventTile()));
        tiledMap.getProperties().setProperty("D1|", Boolean.toString((mapProperties.getHead() & 0B0000_0010) == 0B0000_0010));
        tiledMap.getProperties().setProperty("D0|传送带", Boolean.toString(mapProperties.hasDyTile()));
        tiledMap.getProperties().setProperty("music", Integer.toString(mapProperties.intMusic()));

        // ----------------

        // 填充图块层，比原地图大6*6，以便观察
        TileLayer fillTileLayer = new TileLayer(3 + width + 3, 3 + height + 3);
        fillTileLayer.setName("fillTile");
        fillTileLayer.setId(nextLayerId++);
        // 左上角偏移3tile(1tile=16)，四周均匀的分布为3tile
        fillTileLayer.setOffsetX(-(0x10 * 3));
        fillTileLayer.setOffsetY(-(0x10 * 3));

        // 获取填充的Tile，并将该图层全部设置为该Tile
        Tile fillTile = tileSet.getTile(mapProperties.intFillTile());
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
        hideTileLayer.setName("hideTile");

        // 获取隐藏的Tile，并将该图层全部设置为该Tile
        Tile hideTile = tileSet.getTile(mapProperties.getHideTile() & 0x7F);
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
//                eventLayer.setName(String.format("%04X:%01X", 0x0441 + ((entry.getKey() & 0xF8) >>> 3), (entry.getKey() & 0x07)));
                eventLayer.setName(String.format("%02X", entry.getKey() & 0xFF));

                // 设置该事件影响的图块
                for (EventTile eventTile : entry.getValue()) {
                    eventLayer.setTileAt(eventTile.intX(), eventTile.intY(), tileSet.getTile(eventTile.intTile()));
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
        for (Treasure treasure : treasureEditor.getTreasures().stream()
                .filter(t -> t.intMap() == map)
                .toList()) {
            MapObject mapObject = new MapObject(treasure.intX() * 0x10, treasure.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 添加标志，Tiled程序可以识别
            mapObject.setType("treasure");
            // 设置物品ID和物品当前的名称
            mapObject.setName(String.format("%02X|%s", treasure.getItem(), textEditor.getItemName(treasure.intItem())));

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
            MapObject mapObject;
            MapPoint inPoint = entry.getKey();
            MapPoint outPoint = entry.getValue();

            // 设置视觉辅助用的出入口线段，仅在同地图有效
            if (outPoint.intMap() == 0x00) {
                mapObject = new MapObject(inPoint.intX() * 0x10, inPoint.intY() * 0x10, 0, 0, 0);
                mapObject.setId(nextObjectId++);
                // 默认隐藏
                mapObject.setVisible(false);
                Polyline polyline = new Polyline();
                polyline.setPoints(
                        String.format("0,0 %d,%d",
                                (outPoint.intX() - inPoint.intX()) * 0x10,
                                (outPoint.intY() - inPoint.intY()) * 0x10));
                mapObject.setPolyline(polyline);
                // 添加
                entrances.addObject(mapObject);
            }

            // 设置入口坐标
            mapObject = new MapObject(inPoint.intX() * 0x10, inPoint.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 设置出口坐标为名称 Map:X:Y
            mapObject.setName(String.format("%02X:%02X:%02X", outPoint.getMap(), outPoint.intX(), outPoint.intY()));

            // 添加到入口的对象层
            entrances.addObject(mapObject);
        }

        // ----------------

        // 计算机层
        ObjectGroup computerGroup = new ObjectGroup();
        computerGroup.setId(nextObjectId++);
        computerGroup.setName("computers");
        // 获取当前地图的所有计算机，并使用 16*16 的矩形标出
        for (Computer computer : computerEditor.getComputers().stream()
                .filter(c -> c.intMap() == map)
                .toList()) {
            MapObject mapObject = new MapObject(computer.intX() * 0x10, computer.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 将计算机类型设置为名称
            mapObject.setName(String.format("%02X", computer.getType()));

            // 添加计算机到计算机层
            computerGroup.addObject(mapObject);
        }

        // ----------------

        // 精灵层
        ObjectGroup spriteGroup = new ObjectGroup();
        spriteGroup.setId(nextObjectId++);
        spriteGroup.setName("sprites");

        // 获取当前地图的所有精灵，添加到精灵组中
        for (Sprite sprite : spriteEditor.getSprites().get(map)) {
            MapObject mapObject = new MapObject();
            mapObject.setId(nextObjectId++);
            // 将精灵对话类型和动作为名称
            mapObject.setName(String.format("%02X:%02X:%02X", sprite.getTalk1(), sprite.getTalk2(), sprite.getAction()));
            // 添加标志，Tiled程序可以识别
            mapObject.setType("sprite");
            // 0x80 为fistID，tileSet的结尾
            // 0x01为软件地址偏移
            mapObject.setGid(0x80 + sprite.intType() + 0x01);
            // 精灵最大X、Y为0x3F
            mapObject.setX((sprite.getRawX() & 0x3F) * 0x10);
            // Y值需要额外增加1格，软件才能正确的显示位置
            mapObject.setY(((sprite.getRawY() & 0x3F) + 1) * 0x10);

            // 固定大小为 0x10*0x10
            mapObject.setWidth((double) 0x10);
            mapObject.setHeight((double) 0x10);

            // 添加其它属性
            Properties properties = mapObject.getProperties();
            if (sprite.hasLockDirection()) {
                properties.setProperty("XD7|锁定朝向", "true");
            }
            if (sprite.hasDisableMovingAnim()) {
                properties.setProperty("XD6|禁用移动动画", "true");
            }
            if (sprite.hasCanPushed()) {
                properties.setProperty("YD7|可被推动", "true");
            }
            if (sprite.hasIgnoreTerrain()) {
                properties.setProperty("YD6|无视地形", "true");
            }
            // 添加精灵到精灵组
            spriteGroup.addObject(mapObject);
        }

        // ----------------

        // 地图边界传送方式
        ObjectGroup borderGroup = new ObjectGroup();
        borderGroup.setId(nextObjectId++);
        borderGroup.setName("border");

        // 边界的四个方向
        MapObject borderUP = new MapObject(0x00, -(0x03 * 0x10), width * 0x10, 0x03 * 0x10, 0);
        borderUP.setId(nextObjectId++);
        borderUP.setType("border");
        MapObject borderDOWN = new MapObject(0x00, height * 0x10, width * 0x10, 0x03 * 0x10, 0);
        borderDOWN.setId(nextObjectId++);
        borderDOWN.setType("border");
        MapObject borderLEFT = new MapObject(-(0x03 * 0x10), 0x00, 0x03 * 0x10, height * 0x10, 0);
        borderLEFT.setId(nextObjectId++);
        borderLEFT.setType("border");
        MapObject borderRIGHT = new MapObject(width * 0x10, 0x00, 0x03 * 0x10, height * 0x10, 0);
        borderRIGHT.setId(nextObjectId++);
        borderRIGHT.setType("border");
        borderGroup.addObject(borderRIGHT);
        borderGroup.addObject(borderLEFT);
        borderGroup.addObject(borderDOWN);
        borderGroup.addObject(borderUP);

        MapBorder border = mapEntrance.getBorder();
        switch (border.type) {
            case LAST -> {
                borderUP.setName("LAST");
                borderDOWN.setName("LAST");
                borderLEFT.setName("LAST");
                borderRIGHT.setName("LAST");
            }
            case FIXED -> {
                MapPoint fixed = border.getFirst();
                String fixedStr = String.format("%02X:%02X:%02X", fixed.getMap(), fixed.intX(), fixed.intY());
                borderUP.setName(fixedStr);
                borderDOWN.setName(fixedStr);
                borderLEFT.setName(fixedStr);
                borderRIGHT.setName(fixedStr);
            }
            case DIRECTION -> {
                MapPoint up = border.get(0x00);
                MapPoint down = border.get(0x01);
                MapPoint left = border.get(0x02);
                MapPoint right = border.get(0x03);

                borderUP.setName(String.format("%02X:%02X:%02X", up.getMap(), up.intX(), up.intY()));
                borderDOWN.setName(String.format("%02X:%02X:%02X", down.getMap(), down.intX(), down.intY()));
                borderLEFT.setName(String.format("%02X:%02X:%02X", left.getMap(), left.intX(), left.intY()));
                borderRIGHT.setName(String.format("%02X:%02X:%02X", right.getMap(), right.intX(), right.intY()));
            }
        }


        // ----------------

        // 区域，地图区域
        // 可移动区域，超出后按走出边界处理
        ObjectGroup areas = new ObjectGroup();
        areas.setId(nextObjectId++);
        areas.setName("areas");
        // 默认隐藏
        areas.setVisible(false);

        // 可移动区域偏移量
        int movableWidthOffset = (mapProperties.movableWidthOffset & 0xFF) * 0x10;
        int movableHeightOffset = (mapProperties.movableHeightOffset & 0xFF) * 0x10;
        // 可移动区域实际宽度
        int movableWidth = (mapProperties.movableWidth & 0xFF) * 0x10;
        movableWidth -= movableWidthOffset;
        // 可移动区域实际高度
        int movableHeight = (mapProperties.movableHeight & 0xFF) * 0x10;
        movableHeight -= movableHeightOffset;
        // 可移动区域
        MapObject movableObject = new MapObject(movableWidthOffset, movableHeightOffset, movableWidth, movableHeight, 0);
        movableObject.setId(nextObjectId++);
        movableObject.setName("movable");
        // 添加
        areas.addObject(movableObject);
        // 地图区域
        MapObject area = new MapObject(0x00, 0x00, width * 0x10, height * 0x10, 0);
        area.setId(nextObjectId++);
        area.setName("area");
        areas.addObject(area);

        // ----------------


        tiledMap.addLayer(fillTileLayer);
        tiledMap.addLayer(hideTileLayer);
        tiledMap.addLayer(mapLayer);
        tiledMap.addLayer(eventGroup);
        tiledMap.addLayer(treasureGroup);
        tiledMap.addLayer(entrances);
        tiledMap.addLayer(computerGroup);
        tiledMap.addLayer(spriteGroup);
        tiledMap.addLayer(borderGroup);
        tiledMap.addLayer(areas);

        // 不可抗力的强制属性（写入文件时）
        // 旧版本没有的
        tiledMap.setTiledversion("");
        tiledMap.setNextlayerid(nextLayerId);
        tiledMap.setNextobjectid(nextObjectId);
        if (tiledMap.getInfinite() == null) {
            tiledMap.setInfinite(0);
        }
        // 旧版本有的
        for (Tile tile : tileSet) {
            if (tile.getType() == null) {
                tile.setType("");
            }
        }
        for (Tile tile : spriteTileSet) {
            if (tile.getType() == null) {
                tile.setType("");
            }
        }
        // --- END 可以看出来，新的bug增加了
        return tiledMap;
    }

    /**
     * 生成Tiled的世界地图
     */
    public static Map createWorld(@NotNull MetalMaxRe metalMaxRe, @NotNull java.util.Map<Rectangle, Integer> pieces, java.util.Map<Integer, TileSet> tileSetMap, TileSet spriteTileSet) {
        var editorManager = metalMaxRe.getEditorManager();

        ITextEditor textEditor = editorManager.getEditor(ITextEditor.class);
        IMapPropertiesEditor mapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        IEventTilesEditor eventTilesEditor = editorManager.getEditor(IEventTilesEditor.class);
        ITreasureEditor treasureEditor = editorManager.getEditor(ITreasureEditor.class);
        IMapEntranceEditor mapEntranceEditor = editorManager.getEditor(IMapEntranceEditor.class);
        ISpriteEditor spriteEditor = editorManager.getEditor(ISpriteEditor.class);
        IWorldMapEditor worldMapEditor = editorManager.getEditor(IWorldMapEditor.class);
        IMonsterEditor monsterEditor = editorManager.getEditor(IMonsterEditor.class);

        WorldMapProperties worldMapProperties = mapPropertiesEditor.getWorldMapProperties();


        Map world = new Map(0x100, 0x100);
        world.setInfinite(0);
        world.setTiledversion("");
        int nextLayerId = 0;
        int nextObjectId = 0;

        // 添加所有图块组合到该世界地图
        for (TileSet tileSet : tileSetMap.values()) {
            world.addTileset(tileSet);
            for (Tile tile : tileSet) {
                if (tile.getType() == null) {
                    tile.setType("");
                }
            }
        }

        world.addTileset(spriteTileSet);
        for (Tile tile : spriteTileSet) {
            if (tile.getType() == null) {
                tile.setType("");
            }
        }

        byte[][] map = worldMapEditor.getMap();


        TileLayer worldLayer = new TileLayer(0x100, 0x100);
        worldLayer.setId(nextLayerId++);
        worldLayer.setName("world");

        for (java.util.Map.Entry<Rectangle, Integer> entry : pieces.entrySet()) {
            Rectangle rectangle = entry.getKey();
            TileSet tiles = tileSetMap.get(entry.getValue());
            int x = (int) rectangle.getX();
            int y = (int) rectangle.getY();
            int width = (int) rectangle.getWidth();
            int height = (int) rectangle.getHeight();
            for (int offsetY = 0; offsetY < height; offsetY++) {
                for (int offsetX = 0; offsetX < width; offsetX++) {
                    // 世界地图的图块集一共有192块图块
                    Tile tile = tiles.getTile(map[y + offsetY][x + offsetX] & 0xFF);
                    worldLayer.setTileAt(x + offsetX, y + offsetY, tile);
                }
            }
        }

        // ----------------

        // 事件图块，显示时会覆盖对应的图块，相当于事件条件达成
        Group eventGroup = new Group();
        eventGroup.setId(nextLayerId++);
        eventGroup.setName("events");
        // 默认隐藏
        eventGroup.setVisible(false);
        // 获取该地图的所有事件图块
        for (var entry : eventTilesEditor.getWorldEventTile().entrySet()) {
            TileLayer eventLayer = new TileLayer(0x100, 0x100);
            eventLayer.setId(nextLayerId++);
            // event:index
            // 事件内存地址和地址的bit位
//            eventLayer.setName(String.format("%04X:%01X", 0x0441 + ((entry.getKey() & 0xF8) >>> 3), (entry.getKey() & 0x07)));
            eventLayer.setName(String.format("%02X", entry.getKey() & 0xFF));
            // 设置该事件影响的图块
            for (EventTile eventTile : entry.getValue()) {
                // 为不同地方的图块集使用不同的图块源
                for (java.util.Map.Entry<Rectangle, Integer> rectangleIntegerEntry : pieces.entrySet()) {
                    int x = eventTile.intX() * 4;
                    int y = eventTile.intY() * 4;
                    // 找到该坐标的图块集
                    // TODO 没有判断跨域4*4tile
                    if (rectangleIntegerEntry.getKey().contains(x, y)) {
                        TileSet tileSet = tileSetMap.get(rectangleIntegerEntry.getValue());
                        byte[] tiles = worldMapEditor.getIndex(eventTile.intX(), eventTile.intY(), eventTile.intTile());
                        // 世界地图的事件图块为 4*4 个tile
                        for (int offsetY = 0; offsetY < 0x04; offsetY++) {
                            for (int offsetX = 0; offsetX < 0x04; offsetX++) {
                                Tile tile = tileSet.getTile(tiles[(offsetY * 4) + offsetX] & 0xFF);
                                eventLayer.setTileAt(x + offsetX, y + offsetY, tile);
                            }
                        }
                        break;
                    }
                }
            }
            // 添加到事件图块组中
            eventGroup.getLayers().add(eventLayer);
        }

        // ----------------

        // 领域，怪物出没的组合
        ObjectGroup realms = new ObjectGroup();
        realms.setId(nextObjectId++);
        realms.setName("realms");
        realms.setVisible(false);
        for (int i = 0; i < monsterEditor.getWorldMapMonsterRealmMaxCount(); i++) {
            byte value = monsterEditor.getWorldMapRealms().get(i);
            MapObject realm = new MapObject((i % 0x10) * 0x10 * 0x10, (int) (i / 0x10) * 0x10 * 0x10, 0x10 * 0x10, 0x10 * 0x10, 0x00);
            realm.setId(nextObjectId++);
            realm.setName(String.format("%02X:%02X", i, value));
            if (value == 0x00) {
                // 0x00为没有怪物出没
                realm.setVisible(false);
            }
            realms.addObject(realm);
        }

        // ----------------

        // 地雷层，总共4个
        ObjectGroup mineGroup = new ObjectGroup();
        mineGroup.setId(nextObjectId++);
        mineGroup.setName("mines");
        for (int i = 0; i < 0x04; i++) {
            MapPoint minePoint = worldMapEditor.getMines().get(i);
            MapObject mineObject = new MapObject(0x00, 0x00, 0x10, 0x10, 0);
            mineObject.setId(nextObjectId++);
            if (minePoint != null) {
                mineObject.setX(minePoint.intX() * 0x10);
                mineObject.setY(minePoint.intY() * 0x10);
                mineGroup.addObject(mineObject);
            }
        }

        // ----------------

        // 航线层
        ObjectGroup lineGroup = new ObjectGroup();
        lineGroup.setId(nextObjectId++);
        lineGroup.setName("line");

        // 路径点为相对路径，所以起始点为原版坐标
        // 归航航线和目的地
        MapObject backLineObject = new MapObject(0x6B * 0x10, 0x47 * 0x10, 0, 0, 0);
        backLineObject.setId(nextObjectId++);
        backLineObject.setPolyline(new Polyline());
        backLineObject.getPolyline().setPoints(lineToPoints(0, 0, worldMapEditor.getShippingLineBack().getKey()));

        MapPoint backLinePoint = worldMapEditor.getShippingLineBack().getValue();
        backLineObject.setName(String.format("%02X:%02X:%02X", backLinePoint.getMap(), backLinePoint.intX(), backLinePoint.intY()));
        lineGroup.addObject(backLineObject);

        // 出航航线和目的地
        MapObject outLineObject = new MapObject(0x81 * 0x10, 0x38 * 0x10, 0, 0, 0);
        outLineObject.setId(nextObjectId++);
        outLineObject.setPolyline(new Polyline());
        outLineObject.getPolyline().setPoints(lineToPoints(0, 0, worldMapEditor.getShippingLineOut().getKey()));

        MapPoint outLinePoint = worldMapEditor.getShippingLineOut().getValue();
        outLineObject.setName(String.format("%02X:%02X:%02X", outLinePoint.getMap(), outLinePoint.intX(), outLinePoint.intY()));
        lineGroup.addObject(outLineObject);

        // ----------------

        // 宝藏层
        ObjectGroup treasureGroup = new ObjectGroup();
        treasureGroup.setId(nextObjectId++);
        treasureGroup.setName("treasure");

        // 查找世界地图的宝藏，并使用 16*16 的矩形对象标出
        for (Treasure treasure : treasureEditor.getTreasures().stream()
                .filter(t -> t.getMap() == 0x00)
                .toList()) {
            MapObject mapObject = new MapObject(treasure.intX() * 0x10, treasure.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 添加标志，Tiled程序可以识别
            mapObject.setType("treasure");
            // 设置物品ID和物品当前的名称
            mapObject.setName(String.format("%02X|%s", treasure.getItem(), textEditor.getItemName(treasure.intItem())));

            // 添加宝藏
            treasureGroup.addObject(mapObject);
        }

        // ----------------

        // 当前地图的边界和出入口
        ObjectGroup entrances = new ObjectGroup();
        entrances.setId(nextObjectId++);
        entrances.setName("entrances");

        // 获取当前地图的边界和出入口
        MapEntrance mapEntrance = mapEntranceEditor.getWorldMapEntrance();
        for (var entry : mapEntrance.getEntrances().entrySet()) {
            MapObject mapObject;
            MapPoint inPoint = entry.getKey();
            MapPoint outPoint = entry.getValue();

            // 设置视觉辅助用的出入口线段，仅在同地图有效
            if (outPoint.intMap() == 0x00) {
                mapObject = new MapObject(inPoint.intX() * 0x10, inPoint.intY() * 0x10, 0, 0, 0);
                mapObject.setId(nextObjectId++);
                // 默认隐藏，还是启用吧，懒得点
                mapObject.setVisible(true);
                Polyline polyline = new Polyline();
                polyline.setPoints(
                        String.format("0,0 %d,%d",
                                (outPoint.intX() - inPoint.intX()) * 0x10,
                                (outPoint.intY() - inPoint.intY()) * 0x10));
                mapObject.setPolyline(polyline);
                // 添加
                entrances.addObject(mapObject);
            }

            // 设置入口坐标
            mapObject = new MapObject(inPoint.intX() * 0x10, inPoint.intY() * 0x10, 0x10, 0x10, 0);
            mapObject.setId(nextObjectId++);
            // 设置出口坐标为名称 Map:X:Y
            mapObject.setName(String.format("%02X:%02X:%02X", outPoint.getMap(), outPoint.intX(), outPoint.intY()));

            // 添加到入口的对象层
            entrances.addObject(mapObject);
        }

        // ----------------

        // 精灵层
        ObjectGroup spriteGroup = new ObjectGroup();
        spriteGroup.setId(nextObjectId++);
        spriteGroup.setName("sprites");

        // 获取当前地图的所有精灵，添加到精灵组中
        for (Sprite sprite : spriteEditor.getWorldSprites()) {
            MapObject mapObject = new MapObject();
            mapObject.setId(nextObjectId++);
            // 将精灵对话类型和动作为名称
            mapObject.setName(String.format("%02X:%02X:%02X", sprite.getTalk1(), sprite.getTalk2(), sprite.getAction()));
            // 添加标志，Tiled程序可以识别
            mapObject.setType("sprite");
            // 0x80 为fistID，tileSet的结尾
            // 0x01为软件地址偏移
//            mapObject.setGid(0x80 + (sprite.type & 0xFF) + 0x01);
            mapObject.setGid((tileSetMap.size() * 0xC0) + sprite.intType() + 0x01);
            // 精灵最大X、Y为0x3F
            mapObject.setX((sprite.getRawX() & 0x3F) * 0x10);
            // Y值需要额外增加1格，软件才能正确的显示位置
            mapObject.setY(((sprite.getRawY() & 0x3F) + 1) * 0x10);

            // 固定大小为 0x10*0x10
            mapObject.setWidth((double) 0x10);
            mapObject.setHeight((double) 0x10);

            // 添加其它属性
            Properties properties = mapObject.getProperties();
            if (sprite.hasLockDirection()) {
                properties.setProperty("XD7|锁定朝向", "true");
            }
            if (sprite.hasDisableMovingAnim()) {
                properties.setProperty("XD6|禁用移动动画", "true");
            }
            if (sprite.hasCanPushed()) {
                properties.setProperty("YD7|可被推动", "true");
            }
            if (sprite.hasIgnoreTerrain()) {
                properties.setProperty("YD6|无视地形", "true");
            }
            // 添加精灵到精灵组
            spriteGroup.addObject(mapObject);
        }

        // ----------------

        // 地图边界传送方式
        ObjectGroup borderGroup = new ObjectGroup();
        borderGroup.setId(nextObjectId++);
        borderGroup.setName("border");

        // 边界的四个方向
        MapObject borderUP = new MapObject(0x00, -(0x03 * 0x10), 0x100 * 0x10, 0x03 * 0x10, 0);
        borderUP.setId(nextObjectId++);
        borderUP.setType("border");
        MapObject borderDOWN = new MapObject(0x00, 0x100 * 0x10, 0x100 * 0x10, 0x03 * 0x10, 0);
        borderDOWN.setId(nextObjectId++);
        borderDOWN.setType("border");
        MapObject borderLEFT = new MapObject(-(0x03 * 0x10), 0x00, 0x03 * 0x10, 0x100 * 0x10, 0);
        borderLEFT.setId(nextObjectId++);
        borderLEFT.setType("border");
        MapObject borderRIGHT = new MapObject(0x100 * 0x10, 0x00, 0x03 * 0x10, 0x100 * 0x10, 0);
        borderRIGHT.setId(nextObjectId++);
        borderRIGHT.setType("border");
        borderGroup.addObject(borderRIGHT);
        borderGroup.addObject(borderLEFT);
        borderGroup.addObject(borderDOWN);
        borderGroup.addObject(borderUP);

        MapBorder border = mapEntrance.getBorder();
        switch (border.type) {
            case LAST -> {
                borderUP.setName("LAST");
                borderDOWN.setName("LAST");
                borderLEFT.setName("LAST");
                borderRIGHT.setName("LAST");
            }
            case FIXED -> {
                MapPoint fixed = border.getFirst();
                String fixedStr = String.format("%02X:%02X:%02X", fixed.getMap(), fixed.intX(), fixed.intY());
                borderUP.setName(fixedStr);
                borderDOWN.setName(fixedStr);
                borderLEFT.setName(fixedStr);
                borderRIGHT.setName(fixedStr);
            }
            case DIRECTION -> {
                MapPoint up = border.get(0x00);
                MapPoint down = border.get(0x01);
                MapPoint left = border.get(0x02);
                MapPoint right = border.get(0x03);

                borderUP.setName(String.format("%02X:%02X:%02X", up.getMap(), up.intX(), up.intY()));
                borderDOWN.setName(String.format("%02X:%02X:%02X", down.getMap(), down.intX(), down.intY()));
                borderLEFT.setName(String.format("%02X:%02X:%02X", left.getMap(), left.intX(), left.intY()));
                borderRIGHT.setName(String.format("%02X:%02X:%02X", right.getMap(), right.intX(), right.intY()));
            }
        }

        // ----------------

        // 可移动区域，超出后按走出边界处理
        ObjectGroup movable = new ObjectGroup();
        movable.setId(nextObjectId++);
        movable.setName("movable");
        // 默认隐藏
        movable.setVisible(false);
        // 可移动区域偏移量
        int movableWidthOffset = (worldMapProperties.movableWidthOffset & 0xFF) * 0x10;
        int movableHeightOffset = (worldMapProperties.movableHeightOffset & 0xFF) * 0x10;
        // 可移动区域实际宽度
        int movableWidth = (worldMapProperties.movableWidth & 0xFF) * 0x10;
        movableWidth -= movableWidthOffset;
        // 可移动区域实际高度
        int movableHeight = (worldMapProperties.movableHeight & 0xFF) * 0x10;
        movableHeight -= movableHeightOffset;
        // 只有这一个对象
        MapObject movableObject = new MapObject(movableWidthOffset, movableHeightOffset, movableWidth, movableHeight, 0);
        movableObject.setId(nextObjectId++);
        movableObject.setName("movable");
        // 添加
        movable.addObject(movableObject);

        // ----------------

        // 图块集分割
        ObjectGroup piecesGroup = new ObjectGroup();
        piecesGroup.setId(nextObjectId++);
        piecesGroup.setName("pieces");
        piecesGroup.setVisible(false);
        for (java.util.Map.Entry<Rectangle, Integer> entry : pieces.entrySet()) {
            Rectangle rectangle = entry.getKey();
            MapObject mapObject = new MapObject();
            mapObject.setId(nextObjectId++);
            mapObject.setName(String.format("%08X", entry.getValue()));
            mapObject.setX(rectangle.getX() * 0x10);
            mapObject.setY(rectangle.getY() * 0x10);
            mapObject.setWidth(rectangle.getWidth() * 0x10);
            mapObject.setHeight(rectangle.getHeight() * 0x10);

            // 添加图块集矩形
            piecesGroup.addObject(mapObject);
        }


        world.addLayer(worldLayer);
        world.addLayer(eventGroup);
        world.addLayer(realms);
        world.addLayer(mineGroup);
        world.addLayer(lineGroup);
        world.addLayer(treasureGroup);
        world.addLayer(entrances);
        world.addLayer(spriteGroup);
        world.addLayer(movable);
        world.addLayer(borderGroup);
        world.addLayer(piecesGroup);


        world.setNextobjectid(nextObjectId);
        world.setNextlayerid(nextLayerId);
        return world;
    }


    /**
     * 创建tileSet图片的tsx文件
     * 文件名称格式：X00+X40+X80+XC0-combinationA+combinationB-PALETTE.tsx，均为十六进制
     * e.g: 1C0E1E1F-0001-9AD9.tsx
     */
    public static TileSet createTsx(@NotNull MetalMaxRe metalMaxRe, @Nullable TMXMapWriter tmxMapWriter, @NotNull File tileBitmap, int xXX, int combinationA, int combinationB, char palette, @NotNull File outputDir) throws IOException {
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
        TileSet tileSet = new TileSet();
        tileSet.setName(String.format("%02X%02X%02X%02X-%02X%02X-%04X",
                NumberR.at(xXX, 3),
                NumberR.at(xXX, 2),
                NumberR.at(xXX, 1),
                NumberR.at(xXX, 0),
                combinationA, combinationB,
                palette & 0xFFFF));
        tileSet.importTileBitmap(tileBitmap.getPath(), new BasicTileCutter(0x10, 0x10, 0, 0));

        // 获取所有图块属性
        byte[] tileEffect = new byte[0x40 + 0x40];
        System.arraycopy(tileSetEditor.getAttributes()[combinationA], 0, tileEffect, 0, 0x40);
        System.arraycopy(tileSetEditor.getAttributes()[combinationB], 0, tileEffect, 0x40, 0x40);

        for (int i = 0; i < tileEffect.length; i++) {
            Tile tile = tileSet.getTile(i);
            tile.setType("tile");

            // 添加图块属性
            Properties properties = tile.getProperties();
            byte effect = tileEffect[i];
            if ((effect & 0B1000_0000) != 0x00) {
                properties.setProperty("D7|墙壁", "true");
            }
            if ((effect & 0B0100_0000) != 0x00) {
                properties.setProperty("D6|", "true");
            }
            if ((effect & 0B0010_0000) != 0x00) {
                properties.setProperty("D5|", "true");
            }
            if ((effect & 0B0001_0000) != 0x00) {
                properties.setProperty("D4|", "true");
            }
            if ((effect & 0B0000_1000) != 0x00) {
                properties.setProperty("D3|", "true");
            }
            if ((effect & 0B0000_0100) != 0x00) {
                properties.setProperty("D2|", "true");
            }
            if ((effect & 0B0000_0010) != 0x00) {
                properties.setProperty("D1|color", "true");
            }
            if ((effect & 0B0000_0001) != 0x00) {
                properties.setProperty("D0|color", "true");
            }
        }
        if (tmxMapWriter == null) {
            tmxMapWriter = new TMXMapWriter();
        }
        File source = new File(outputDir, String.format("%s.tsx", tileSet.getName()));
        tmxMapWriter.writeTileset(tileSet, source.getPath());
        tileSet.setSource(source.getPath());
        return tileSet;
    }

    public static TileSet createTsx(@NotNull MetalMaxRe metalMaxRe, @Nullable TMXMapWriter tmxMapWriter, @NotNull File tileBitmap, @NotNull MapProperties mapProperties, @NotNull File outputDir) throws IOException {
        return createTsx(metalMaxRe, tmxMapWriter, tileBitmap, mapProperties.getIntTiles(), mapProperties.combinationA, mapProperties.combinationB, mapProperties.palette, outputDir);
    }

    public static TileSet createWorldTsx(@NotNull MetalMaxRe metalMaxRe, @Nullable TMXMapWriter tmxMapWriter, @NotNull File tileBitmap, int xXX, @NotNull File outputDir) throws IOException {
        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
        TileSet tileSet = new TileSet();
        tileSet.setName(String.format("%08X-%04X", xXX, 0x9AD0));
        tileSet.importTileBitmap(tileBitmap.getPath(), new BasicTileCutter(0x10, 0x10, 0, 0));

        // 获取所有图块属性
        byte[] tileEffect = new byte[0x40 + 0x40 + 0x40 + 0x40];
        System.arraycopy(tileSetEditor.getWorldAttributes()[0x00], 0, tileEffect, 0x00, 0x40);
        System.arraycopy(tileSetEditor.getWorldAttributes()[0x01], 0, tileEffect, 0x40, 0x40);
        System.arraycopy(tileSetEditor.getWorldAttributes()[0x02], 0, tileEffect, 0x80, 0x40);
        System.arraycopy(tileSetEditor.getWorldAttributes()[0x03], 0, tileEffect, 0xC0, 0x40);

        for (int i = 0; i < tileEffect.length; i++) {
            Tile tile = tileSet.getTile(i);
            tile.setType("tile");

            // 添加图块属性
            Properties properties = tile.getProperties();
            byte effect = tileEffect[i];
            if ((effect & 0B1000_0000) != 0x00) {
                properties.setProperty("D7|墙壁", "true");
            }
            if ((effect & 0B0100_0000) != 0x00) {
                properties.setProperty("D6|", "true");
            }
            if ((effect & 0B0010_0000) != 0x00) {
                properties.setProperty("D5|", "true");
            }
            if ((effect & 0B0001_0000) != 0x00) {
                properties.setProperty("D4|", "true");
            }
            if ((effect & 0B0000_1000) != 0x00) {
                properties.setProperty("D3|", "true");
            }
            if ((effect & 0B0000_0100) != 0x00) {
                properties.setProperty("D2|", "true");
            }
            if ((effect & 0B0000_0010) != 0x00) {
                properties.setProperty("D1|color", "true");
            }
            if ((effect & 0B0000_0001) != 0x00) {
                properties.setProperty("D0|color", "true");
            }
        }
        if (tmxMapWriter == null) {
            tmxMapWriter = new TMXMapWriter();
        }
        File source = new File(outputDir, String.format("%s.tsx", tileSet.getName()));
        tmxMapWriter.writeTileset(tileSet, source.getPath());
        tileSet.setSource(source.getPath());
        return tileSet;
    }

    /**
     * 将已有的Tiled地图作为某个地图的数据
     * 部分不存在的数据保持不变
     * <p>
     * tileMap的TileSet名称会影响地图图块
     * tileMap的精灵TileSet名称会影响精灵图块
     */
    public static void importMap(@NotNull MetalMaxRe metalMaxRe, int map, @NotNull Map tiledMap) {
        var editorManager = metalMaxRe.getEditorManager();

        IMapPropertiesEditor mapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        IMapEditor mapEditor = editorManager.getEditor(IMapEditor.class);
        IEventTilesEditor eventTilesEditor = editorManager.getEditor(IEventTilesEditor.class);
        ITreasureEditor treasureEditor = editorManager.getEditor(ITreasureEditor.class);
        IMapEntranceEditor mapEntranceEditor = editorManager.getEditor(IMapEntranceEditor.class);
        IComputerEditor<Computer> computerEditor = editorManager.getEditor(IComputerEditor.class);
        ISpriteEditor spriteEditor = editorManager.getEditor(ISpriteEditor.class);

        // 地图属性
        MapProperties mapProperties = mapPropertiesEditor.getMapProperties(map);

        for (TileSet tileSet : tiledMap.getTileSets()) {
            String name = tileSet.getName();
            if (name == null || name.isEmpty()) {
                continue;
            }
            // 地图图块集
            if (TILESET_NAME_PATTERN.matcher(name).find()) {
                int xXX = (int) Long.parseLong(name.substring(0, 8), 16);
                int combination = Integer.parseInt(name.substring(9, 13), 16);
                int palette = Integer.parseInt(name.substring(14, 18), 16);
                mapProperties.setIntTiles(xXX);
                mapProperties.setCombination(combination);
                mapProperties.setPalette((char) palette);
            }
            // 精灵图块集
            if (SPRITE_NAME_PATTERN.matcher(name).find()) {
                int xXX = (int) Long.parseLong(name.substring(0, 8), 16);
                // 只有 0000FF00有效，其它是无法更改的
                mapProperties.setSpriteIndex(NumberR.at(xXX, 1));
            }
        }

        // 读取地图属性
        for (Property property : tiledMap.getProperties().getProperties()) {
            String name = property.getName();
            // 名称必须有效且长度大于3
            if (name != null && name.length() >= 3 && Boolean.parseBoolean(property.getValue())) {
                name = name.substring(0, 3);
                switch (name.toUpperCase()) {
                    case "D0|" -> // 传送带
                            mapProperties.setHead((byte) (mapProperties.getHead() | MapProperties.FLAG_DY_TILE));
                    case "D1|" -> // emm?不知道是什么
                            mapProperties.setHead((byte) (mapProperties.getHead() | 0B0000_0010));
                    case "D2|" -> // 存在事件图块
                            mapProperties.setHead((byte) (mapProperties.getHead() | MapProperties.FLAG_EVENT_TILE));
                    case "D3|" -> // 地图为地下地图（使用犬系统提示返回地面
                            mapProperties.setHead((byte) (mapProperties.getHead() | MapProperties.FLAG_UNDERGROUND));
                    case "MUSIC" -> // 地图的背景音乐
                            mapProperties.setMusic(Byte.parseByte(property.getName()));
                }
            }
        }

        // 获取areas中的area和moving，得到地图相对位置与大小
        // 地图区域层
        MapObject areaObject = null;
        // 地图可移动区域层
        MapObject movableObject = null;

        areas:
        for (MapLayer layer : tiledMap.getLayers()) {
            if (layer instanceof ObjectGroup mapObjects && Objects.equals("areas", layer.getName())) {
                for (MapObject mapObject : mapObjects) {
                    if (Objects.equals("area", mapObject.getName())) {
                        areaObject = mapObject;
                    }
                    if (Objects.equals("movable", mapObject.getName())) {
                        movableObject = mapObject;
                    }
                    if (areaObject != null && movableObject != null) {
                        // 获取完毕
                        break areas;
                    }
                }
            }
        }

        if (areaObject == null || movableObject == null) {
            // 获取失败
            System.err.println("导入地图失败，丢失area或moving");
            return;
        }

        // 地图宽高
        int mapOffsetX = (int) (areaObject.getX() / 0x10);
        int mapOffsetY = (int) (areaObject.getY() / 0x10);
        int mapWidth = (int) (areaObject.getWidth() / 0x10);
        int mapHeight = (int) (areaObject.getHeight() / 0x10);


        // 获取移动区域对象的第一个数据的宽高和偏移作为可移动区域
        // X、Y作为偏移值
        mapProperties.movableWidthOffset = (byte) (((int) movableObject.getX()) / 0x10);
        mapProperties.movableHeightOffset = (byte) (((int) movableObject.getY()) / 0x10);
        mapProperties.movableWidth = (byte) (movableObject.getWidth() / 0x10);
        mapProperties.movableWidth += mapProperties.movableWidthOffset;
        mapProperties.movableHeight = (byte) (movableObject.getHeight() / 0x10);
        mapProperties.movableHeight += mapProperties.movableHeightOffset;


        // 遍历所有图层
        for (MapLayer layer : new ArrayList<>(tiledMap.getLayers())) {
            if (layer instanceof ObjectGroup objectGroup) {
                // 对象层
                switch (layer.getName()) {
                    case "border": // 边界传送方式
                        MapBorder mapBorder = mapEntranceEditor.getMapEntrance(map).getBorder();
                        // 移除原本的边界传送方式
                        mapBorder.clear();

                        List<MapObject> objects = new ArrayList<>(objectGroup.getObjects());
                        Collections.reverse(objects);

                        MapObject mapObject = objects.get(0x00);
                        // 根据四个方向的目的地，设置不同的类型
                        // 四个方向目的地相同：MapBorderType.FIXED
                        // 四个方向目的地不同：MapBorderType.DIRECTION
                        // 上方目的地空白或为LAST：MapBorderType.LAST
                        if (mapObject.getName() == null || mapObject.getName().isEmpty() || "LAST".equalsIgnoreCase(mapObject.getName())) {
                            // 名称无效或为 LAST 时
                            // 返回入口
                            mapBorder.setType(MapBorderType.LAST);
                        } else {
                            for (MapObject object : objects) {
                                if (!mapObject.getName().equalsIgnoreCase(object.getName())) {
                                    // 设置类型为根据不同方向不同出口
                                    mapBorder.setType(MapBorderType.DIRECTION);
                                    for (int i = 0; i < 0x04; i++) {
                                        mapObject = objects.get(i);
                                        String[] split = mapObject.getName().split(":");
                                        mapBorder.add(new MapPoint(
                                                Integer.parseInt(split[0], 16),
                                                Integer.parseInt(split[1], 16),
                                                Integer.parseInt(split[2], 16)
                                        ));
                                    }
                                    mapObject = null; // 设置为null表示已经设置过类型了
                                    break;
                                }
                            }
                            if (mapObject != null) {
                                // 设置为固定出口
                                mapBorder.setType(MapBorderType.FIXED);
                                String[] split = objects.get(0x00).getName().split(":");
                                mapBorder.add(new MapPoint(
                                        Integer.parseInt(split[0], 16),
                                        Integer.parseInt(split[1], 16),
                                        Integer.parseInt(split[2], 16)
                                ));
                            }
                        }
                        break;
                    case "sprites": // 精灵层
                        // 获取当前地图的精灵
                        List<Sprite> sprites = spriteEditor.getSprites().get(map);
                        // 清除已有的精灵
                        sprites.clear();

                        for (MapObject sprite : objectGroup.getObjects()) {
                            if (sprite.isVisible() != null && !sprite.isVisible()) {
                                // 不添加被隐藏的精灵
                                continue;
                            }
                            // 精灵坐标
                            int x = (int) sprite.getX() / 0x10;
                            int y = ((int) sprite.getY() / 0x10) - 1;

                            // 通过图块ID获取精灵的朝向和图像
                            // 精灵图像和朝向
                            int type = sprite.getTile().getId();

                            // 通过对象名称设置行动方式和对话模式
                            // FF:FF:FF
                            int talk1 = Integer.parseInt(sprite.getName().substring(0, 2), 16);
                            int talk2 = Integer.parseInt(sprite.getName().substring(3, 5), 16);
                            int action = Integer.parseInt(sprite.getName().substring(6, 8), 16);

                            // 其它精灵属性
                            for (Property property : sprite.getProperties().getProperties()) {
                                String name = property.getName();
                                // 名称必须有效且长度大于4
                                if (name != null && name.length() >= 4 && Boolean.parseBoolean(property.getValue())) {
                                    name = name.substring(0, 4).toUpperCase();

                                    switch (name) {
                                        case "YD7|" -> // 精灵可以被玩家推动
                                                y |= Sprite.FLAG_CAN_PUSHED;
                                        case "YD6|" -> // 精灵可以无视地形移动
                                                y |= Sprite.FLAG_IGNORE_TERRAIN;
                                        case "XD7|" -> // 精灵的朝向被锁定在初始的朝向
                                                x |= Sprite.FLAG_LOCK_DIRECTION;
                                        case "XD6|" -> // 精灵移动时不会播放正在移动的动画
                                                x |= Sprite.FLAG_DISABLE_MOVING_ANIM;
                                    }
                                }
                            }
                            // 创建并添加精灵
                            sprites.add(new Sprite(type, x - mapOffsetX, y - mapOffsetY, talk1, talk2, action));
                        }
                        break;
                    case "computers": // 计算机层
                        // 移除当前地图的所有计算机
                        computerEditor.getComputers().removeIf(computer -> computer.intMap() == map);

                        for (MapObject computer : objectGroup.getObjects()) {
                            if (computer.isVisible() != null && !computer.isVisible()) {
                                // 排除被隐藏的计算机
                                continue;
                            }
                            // 通过名称设置计算机的功能
                            int type = Integer.parseInt(computer.getName().substring(0, 2), 16);
                            int x = (int) (computer.getX() / 0x10);
                            int y = (int) (computer.getY() / 0x10);

                            // 添加计算机
                            computerEditor.addComputer(new Computer(map, type, x - mapOffsetX, y - mapOffsetY));
                        }
                        break;
                    case "entrances": // 出入口层（不含边界
                        // 获取当前地图的边界和出入口
                        MapEntrance mapEntrance = mapEntranceEditor.getMapEntrance(map);
                        java.util.Map<MapPoint, MapPoint> entrances = mapEntrance.getEntrances();
                        // 移除所有出入口
                        entrances.clear();

                        for (MapObject entrance : objectGroup.getObjects()) {
                            if (entrance.isVisible() != null && !entrance.isVisible()) {
                                // 排除被隐藏的入口
                                continue;
                            }
                            // 入口X、Y
                            int inX = (int) (entrance.getX() / 0x10);
                            int inY = (int) (entrance.getY() / 0x10);
                            // 出口Map、X、Y ，从名称中解析
                            // FF:FF:FF
                            String[] split = entrance.getName().split(":");
                            int outMap = Integer.parseInt(split[0], 16);
                            int outX = Integer.parseInt(split[1], 16);
                            int outY = Integer.parseInt(split[2], 16);

                            // 入口和出口
                            entrances.put(new MapPoint(map, inX - mapOffsetX, inY - mapOffsetY), new MapPoint(outMap, outX, outY));
                        }
                        break;
                    case "treasure":
                        // 移除当前地图的所有宝藏
                        treasureEditor.getTreasures().removeIf(treasure -> treasure.intMap() == map);

                        for (MapObject treasure : objectGroup.getObjects()) {
                            if (treasure.isVisible() != null && !treasure.isVisible()) {
                                // 不添加被隐藏的宝藏
                                continue;
                            }
                            // 通过名称获取宝藏
                            int item = Integer.parseInt(treasure.getName().substring(0, 2), 16);
                            int x = (int) treasure.getX() / 0x10;
                            int y = (int) treasure.getY() / 0x10;
                            // 添加宝藏
                            treasureEditor.getTreasures().add(new Treasure(map, x - mapOffsetX, y - mapOffsetY, item));
                        }
                        break;
                }
            } else if (layer instanceof TileLayer tileLayer) {
                // Tile层
                switch (tileLayer.getName()) {
                    case "map" -> {
                        mapProperties.setWidth(mapWidth);
                        mapProperties.setHeight(mapHeight);
                        // 构建地图
                        MapBuilder mapBuilder = new MapBuilder();

                        for (int y = 0; y < mapHeight; y++) {
                            for (int x = 0; x < mapWidth; x++) {
                                Tile tileAt = tileLayer.getTileAt(x + mapOffsetX, y + mapOffsetY);
                                if (tileAt != null) {
                                    mapBuilder.add(tileAt.getId());
                                } else {
                                    mapBuilder.add(0x00);
                                }
                            }
                        }
                        mapEditor.getMaps().put(map, mapBuilder);
                    }
                    case "hideTile" ->
                    // 通过hideTile层的 (0,0) 图块作为隐藏图块
                    {
                        Tile tileAt = ((TileLayer) layer).getTileAt(0, 0);
                        if (tileAt != null) {
                            mapProperties.setHideTile(tileAt.getId() & 0x7F);
                        } else {
                            mapProperties.setHideTile(0x00);
                        }
                    }
                    case "fillTile" ->
                    // 通过fillTile层的 (0,0) 图块作为填充图块（地图外填充的图块
                    {
                        Tile tileAt = ((TileLayer) layer).getTileAt(0, 0);
                        if (tileAt != null) {
                            mapProperties.setFillTile(tileAt.getId() & 0x7F);
                        } else {
                            mapProperties.setFillTile(0x00);
                        }
                    }
                }

            } else if (layer instanceof Group group) {
                // 组
                // 目前只储存 events
                if (Objects.equals("events", group.getName())) {
                    var eventTile = eventTilesEditor.getEventTile(map);
                    // 移除当前地图所有的事件图块
                    eventTile.clear();

                    for (MapLayer mapLayer : group.getLayers()) {
                        if (mapLayer.isVisible() != null && !mapLayer.isVisible()) {
                            // 排除被隐藏的事件图块
                            continue;
                        }
                        if (mapLayer instanceof TileLayer eventLayer) {
//                            // 通过名称获取事件数据
//                            // 0x0441为实际内存地址起始
//                            int event = Integer.parseInt(eventLayer.getName().substring(0, 4), 16) - 0x0441;
//                            // 左移3bit，空出末尾的bit位
//                            event <<= 0x03;
//                            // 末尾为bit位
//                            event += Byte.parseByte(eventLayer.getName().substring(5));
                            int event = Integer.parseInt(eventLayer.getName(), 16) & 0xFF;

                            // 通过地图内容获取事件影响的图块
                            List<EventTile> events = new ArrayList<>();

                            Rectangle bounds = eventLayer.getBounds();
                            for (byte y = 0; y < bounds.height; y++) {
                                for (byte x = 0; x < bounds.width; x++) {
                                    Tile tileAt = eventLayer.getTileAt(x, y);
                                    // 将所有有效的图块作为事件图块
                                    if (tileAt != null) {
                                        events.add(new EventTile(x - mapOffsetX, y - mapOffsetY, tileAt.getId().byteValue()));
                                    }
                                }
                            }
                            eventTile.put(event, events);
                        }
                    }

                }

            }

        }

    }


    /**
     * 将已有的Tiled地图作为世界地图
     * 地图大小必须为0x100*0x100
     */
    public static void importWorldMap(@NotNull MetalMaxRe metalMaxRe, @NotNull Map worldMap) {
        var editorManager = metalMaxRe.getEditorManager();

        ITextEditor textEditor = editorManager.getEditor(ITextEditor.class);
        IMapPropertiesEditor mapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        IEventTilesEditor eventTilesEditor = editorManager.getEditor(IEventTilesEditor.class);
        ITreasureEditor treasureEditor = editorManager.getEditor(ITreasureEditor.class);
        IMapEntranceEditor mapEntranceEditor = editorManager.getEditor(IMapEntranceEditor.class);
        ISpriteEditor spriteEditor = editorManager.getEditor(ISpriteEditor.class);
        IWorldMapEditor worldMapEditor = editorManager.getEditor(IWorldMapEditor.class);
        IMonsterEditor monsterEditor = editorManager.getEditor(IMonsterEditor.class);

        WorldMapProperties worldMapProperties = mapPropertiesEditor.getWorldMapProperties();


        byte[][] map = worldMapEditor.getMap();


        for (TileSet tileSet : worldMap.getTileSets()) {
            // TODO 导入世界地图的 图块属性更新

        }

        for (MapLayer layer : worldMap.getLayers()) {
            if (layer instanceof TileLayer tileLayer) {
                if ("world".equals(layer.getName())) { // 世界地图
                    // 导入世界地图
                    for (int y = 0; y < 0x100; y++) {
                        for (int x = 0; x < 0x100; x++) {
                            Tile tileAt = tileLayer.getTileAt(x, y);
                            byte tileId = 0x00;
                            if (tileAt != null) {
                                tileId = tileAt.getId().byteValue();
                            }

                            if (tileId == 0x03 || tileId == 0x0A || tileId == 0x0B) {
                                // 0x03 山脉
                                // 0x0A 山脉起始
                                // 0x0B 山脉结束
                                // 这些在数据中都属于 0x02，游戏中会自动替换
                                tileId = 0x02;
                            }
                            map[y][x] = tileId;
                        }
                    }
                }
            } else if (layer instanceof ObjectGroup objectGroup) {
                switch (objectGroup.getName()) {
                    case "movable": // 可移动区域层
                        Iterator<MapObject> iterator = objectGroup.iterator();
                        if (iterator.hasNext()) {
                            WorldMapProperties mapProperties = mapPropertiesEditor.getWorldMapProperties();

                            // 获取移动区域对象的第一个数据的宽高和偏移作为可移动区域
                            MapObject movable = iterator.next();
                            // X、Y作为偏移值
                            mapProperties.movableWidthOffset = (byte) (((int) movable.getX()) / 0x10);
                            mapProperties.movableHeightOffset = (byte) (((int) movable.getY()) / 0x10);

                            mapProperties.movableWidth = (byte) (movable.getWidth() / 0x10);
                            mapProperties.movableWidth += mapProperties.movableWidthOffset;
                            mapProperties.movableHeight = (byte) (movable.getHeight() / 0x10);
                            mapProperties.movableHeight += mapProperties.movableHeightOffset;
                        } // 不会没有吧？？？
                        break;
                    case "border": // 边界传送方式
                        MapBorder mapBorder = mapEntranceEditor.getWorldMapEntrance().getBorder();
                        // 移除原本的边界传送方式
                        mapBorder.clear();

                        MapObject mapObject = objectGroup.getObjects().get(0x00);
                        // 根据四个方向的目的地，设置不同的类型
                        // 四个方向目的地相同：MapBorderType.FIXED
                        // 四个方向目的地不同：MapBorderType.DIRECTION
                        // 上方目的地空白或为LAST：MapBorderType.LAST
                        if (mapObject.getName() == null || mapObject.getName().isEmpty() || "LAST".equalsIgnoreCase(mapObject.getName())) {
                            // 名称无效或为 LAST 时
                            // 返回入口
                            mapBorder.setType(MapBorderType.LAST);
                        } else {
                            for (MapObject object : objectGroup.getObjects()) {
                                if (!mapObject.getName().equalsIgnoreCase(object.getName())) {
                                    mapObject = null; // 设置为null表示已经设置过类型了
                                    // 设置类型为根据不同方向不同出口
                                    mapBorder.setType(MapBorderType.DIRECTION);
                                    break;
                                }
                            }
                            if (mapObject != null) {
                                // 设置为固定出口
                                mapBorder.setType(MapBorderType.FIXED);
                            }
                        }
                        break;
                    case "sprites": // 精灵层
                        // 获取当前地图的精灵
                        List<Sprite> sprites = spriteEditor.getWorldSprites();
                        // 清除已有的精灵
                        sprites.clear();

                        for (MapObject sprite : objectGroup.getObjects()) {
                            if (sprite.isVisible() != null && !sprite.isVisible()) {
                                // 不添加被隐藏的精灵
                                continue;
                            }
                            // 精灵坐标
                            int x = (int) sprite.getX() / 0x10;
                            int y = ((int) sprite.getY() / 0x10) - 1;

                            // 通过图块ID获取精灵的朝向和图像
                            // 精灵图像和朝向
                            int type = sprite.getTile().getId();

                            // 通过对象名称设置行动方式和对话模式
                            // FF:FF:FF
                            int talk1 = Integer.parseInt(sprite.getName().substring(0, 2), 16);
                            int talk2 = Integer.parseInt(sprite.getName().substring(3, 5), 16);
                            int action = Integer.parseInt(sprite.getName().substring(6, 8), 16);

                            // 其它精灵属性
                            for (Property property : sprite.getProperties().getProperties()) {
                                String name = property.getName();
                                // 名称必须有效且长度大于4
                                if (name != null && name.length() >= 4 && Boolean.parseBoolean(property.getValue())) {
                                    name = name.substring(0, 4).toUpperCase();

                                    switch (name) {
                                        case "YD7|" -> // 精灵可以被玩家推动
                                                y |= Sprite.FLAG_CAN_PUSHED;
                                        case "YD6|" -> // 精灵可以无视地形移动
                                                y |= Sprite.FLAG_IGNORE_TERRAIN;
                                        case "XD7|" -> // 精灵的朝向被锁定在初始的朝向
                                                x |= Sprite.FLAG_LOCK_DIRECTION;
                                        case "XD6|" -> // 精灵移动时不会播放正在移动的动画
                                                x |= Sprite.FLAG_DISABLE_MOVING_ANIM;
                                    }
                                }
                            }
                            // 创建并添加精灵
                            sprites.add(new Sprite(type, x, y, talk1, talk2, action));
                        }
                        break;
                    case "treasure": // 宝藏
                        // 清除世界地图的宝藏
                        treasureEditor.getTreasures().removeIf(treasure -> treasure.intMap() == 0x00);

                        // 读取所有宝藏
                        for (MapObject treasureObject : objectGroup.getObjects()) {
                            if (treasureObject.isVisible() != null && !treasureObject.isVisible()) {
                                // 不添加被隐藏的宝藏
                                continue;
                            }
                            int x = (int) treasureObject.getX() / 0x10;
                            int y = (int) treasureObject.getY() / 0x10;
                            int item = Integer.parseInt(treasureObject.getName().substring(0, 2), 16) & 0xFF;
                            // 添加宝藏
                            treasureEditor.getTreasures().add(new Treasure(0x00, x, y, item));
                        }
                        break;
                    case "entrances": // 出入口
                        MapEntrance worldMapEntrance = mapEntranceEditor.getWorldMapEntrance();
                        // 清除所有出入口
                        worldMapEntrance.getEntrances().clear();

                        // 读取所有出入口
                        for (MapObject entranceObject : objectGroup.getObjects()) {
                            if (entranceObject.isVisible() != null && !entranceObject.isVisible()) {
                                // 排除被隐藏的出入口
                                continue;
                            }

                            String[] split = entranceObject.getName().split(":");
                            if (split.length != 3) {
                                // 忽略3个数据以外的数据
                                continue;
                            }
                            MapPoint inPoint = new MapPoint(0x00, (int) entranceObject.getX() / 0x10, (int) entranceObject.getY() / 0x10);
                            MapPoint outPoint = new MapPoint(
                                    Integer.parseInt(split[0], 16) & 0xFF,
                                    Integer.parseInt(split[1], 16) & 0xFF,
                                    Integer.parseInt(split[2], 16) & 0xFF
                            );
                            // 添加出入口
                            worldMapEntrance.getEntrances().put(inPoint, outPoint);
                        }
                        break;
                    case "line":
                        List<MapObject> lineObjects = new ArrayList<>(objectGroup.getObjects());
                        // 移除隐藏的航线
                        lineObjects.removeIf(o -> !o.isVisible());

                        // 因为是反的需要倒序摆正
                        Collections.reverse(lineObjects);
                        if (lineObjects.size() > 0) {
                            // 设置出航路径点和目的地
                            MapObject out = lineObjects.get(0);
                            String[] split = out.getName().split(":");
                            String[] points = out.getPolyline().getPoints().split(" ");

                            List<java.util.Map.Entry<LineDirection, Byte>> outLine = worldMapEditor.getShippingLineOut().getKey();
                            // 清除当前出航路径点
                            outLine.clear();
                            List<MapPoint> tempOutLine = new ArrayList<>();
                            for (String point : points) {
                                String[] split1 = point.split(",");
                                tempOutLine.add(new MapPoint(0x00,
                                        (byte) (Integer.parseInt(split1[0]) / 0x10),
                                        (byte) (Integer.parseInt(split1[1]) / 0x10)
                                ));
                            }
                            // 转换为相对路径
                            for (int i = Math.min(0x10, outLine.size() - 1); i > 0; i--) {
                                MapPoint point = tempOutLine.get(i);
                                MapPoint previousPoint = tempOutLine.get(i - 1);
                                point.offset(-previousPoint.getX(), -previousPoint.getY());
                            }
                            // 第一个路径点为填充点，无效移除
                            tempOutLine.remove(0x00);

                            // 设置出航目的地
                            worldMapEditor.getShippingLineOut().getValue().set(
                                    Integer.parseInt(split[0], 16),
                                    Integer.parseInt(split[1], 16),
                                    Integer.parseInt(split[2], 16)
                            );
                        }
                        if (lineObjects.size() > 1) {
                            // 设置归航路径点和目的地
//                            MapObject back = lineObjects.get(1);
//                            String[] split = back.getName().split(":");
//                            String[] points = back.getPolyline().getPoints().split(" ");
//
//                            List<MapPoint> backLine = worldMapEditor.getShippingLineBack().getKey();
//                            // 清除当前归航路径点
//                            backLine.clear();
//                            for (String point : points) {
//                                String[] split1 = point.split(",");
//                                backLine.add(new MapPoint(0x00,
//                                        (byte) (Integer.parseInt(split1[0]) / 0x10),
//                                        (byte) (Integer.parseInt(split1[1]) / 0x10)
//                                ));
//                            }
//                            // 转换为相对路径
//                            for (int i = Math.min(0x10, backLine.size() - 1); i > 0; i--) {
//                                MapPoint point = backLine.get(i);
//                                MapPoint previousPoint = backLine.get(i - 1);
//                                point.offset(-previousPoint.getX(), -previousPoint.getY());
//                            }
//                            // 第一个路径点为填充点，无效移除
//                            backLine.remove(0x00);
//
//                            // 设置归航目的地
//                            worldMapEditor.getShippingLineBack().getValue().set(
//                                    Integer.parseInt(split[0], 16),
//                                    Integer.parseInt(split[1], 16),
//                                    Integer.parseInt(split[2], 16)
//                            );

                        }
                        break;
                    case "mines": // 4个地雷
                        List<MapPoint> mines = worldMapEditor.getMines();
                        // 清除地雷
                        mines.clear();
                        // 读取所有地雷，尽管最多写入4个
                        for (MapObject mine : objectGroup.getObjects()) {
                            mines.add(new MapPoint(0x00, (int) mine.getX() / 0x10, (int) mine.getY() / 0x10));
                        }
                        break;
                    case "realms": // 怪物领域
                        List<Byte> realms = monsterEditor.getWorldMapRealms();
                        // 不需要清除领域
                        // realms.clear();
                        for (MapObject realm : objectGroup.getObjects()) {
//                            int index = (int) ((realm.getX() / 0x10) + ((realm.getY() / 0x10) * 0x40));
                            String[] split = realm.getName().split(":");
                            if (split.length >= 2) {
                                if (realm.isVisible() != null && !realm.isVisible()) {
                                    realms.set(Integer.parseInt(split[0], 16), (byte) 0x00);
                                } else {
                                    realms.set(Integer.parseInt(split[0], 16), (byte) Integer.parseInt(split[1], 16));
                                }
                            }
                        }
                        break;
                }
            }
        }

        // 事件图块需要地图解析完毕才能开始
        for (MapLayer layer : worldMap.getLayers()) {
            if (layer instanceof Group group && "events".equals(layer.getName())) {
                var worldEventTile = eventTilesEditor.getWorldEventTile();
                // 移除原事件图块
                worldEventTile.clear();

                for (MapLayer groupLayer : group.getLayers()) {
                    if (groupLayer.isVisible() != null && !groupLayer.isVisible()) {
                        // 排除被隐藏的事件图块
                        continue;
                    }
                    if (groupLayer instanceof TileLayer eventTileLayer) {
//                        // 得到事件
//                        int event = Integer.parseInt(eventTileLayer.getName().substring(0, 4), 16) - 0x0441;
//                        // 得到bit位
//                        int offset = Integer.parseInt(eventTileLayer.getName().substring(5, 6), 16);
//                        // 合并
//                        event <<= 3;
//                        event |= offset;
                        int event = Integer.parseInt(eventTileLayer.getName(), 16) & 0xFF;

                        ArrayList<EventTile> events = new ArrayList<>();

                        // 读取所有事件的4*4tile
                        for (int y = 0; y < 0x40; y++) {
                            for (int x = 0; x < 0x40; x++) {
                                Tile tileAt = eventTileLayer.getTileAt(x * 4, y * 4);
                                if (tileAt == null) {
                                    // 该部分没有图块
                                    continue;
                                }
                                // 读取4*4tile
                                byte[] tiles = new byte[0x10];
                                for (int offsetY = 0; offsetY < 0x04; offsetY++) {
                                    for (int offsetX = 0; offsetX < 0x04; offsetX++) {
                                        Tile tileAt1 = eventTileLayer.getTileAt((x * 4) + offsetX, (y * 4) + offsetY);
                                        if (tileAt1 != null) {
                                            tiles[(offsetY * 0x04) + offsetX] = tileAt1.getId().byteValue();
                                        } else {
                                            tiles[(offsetY * 0x04) + offsetX] = 0x00;
                                        }
                                    }
                                }
                                events.add(new WorldEventTile(x, y, 0, tiles));
                            }
                        }

                        if (!events.isEmpty()) {
                            // 添加事件图块
                            worldEventTile.put(event, events);
                        }
                    }
                }
                break;
            }
        }
    }

    /**
     * 将tiled的线转换为可操作的对象
     */
    public static List<java.util.Map.Entry<LineDirection, Byte>> pointsToLine(String points) {
        List<java.util.Map.Entry<LineDirection, Byte>> line = new ArrayList<>();

        List<MapPoint> tempOutLine = new ArrayList<>();
        for (String point : points.split(" ")) {
            String[] split1 = point.split(",");
            tempOutLine.add(new MapPoint(0x00,
                    (byte) (Integer.parseInt(split1[0]) / 0x10),
                    (byte) (Integer.parseInt(split1[1]) / 0x10)
            ));
        }

        // 转换为相对路径
        for (int i = Math.min(0x10, tempOutLine.size() - 1); i > 0; i--) {
            MapPoint point = tempOutLine.get(i);
            MapPoint previousPoint = tempOutLine.get(i - 1);
            point.offset(-previousPoint.getX(), -previousPoint.getY());
            int x = point.getX() - previousPoint.getX();
            int y = point.getY() - previousPoint.getY();
            if (x == 0 && y == 0) {
                // 玩呢
                continue;
            }

            LineDirection dir;
            // X或Y等于0，就是另一个的方向
            if (x == 0x00) {
                // 上或下移动
                if (y > 0) {
                    // 不是负数，下方移动
                    dir = LineDirection.DOWN;
                } else {
                    // 是负数，上方移动
                    dir = LineDirection.UP;
                }
                // 得到正数的移动格数
                line.add(java.util.Map.entry(dir, (byte) Math.abs(y)));
            } else {
                // 左或右移动
                if (x > 0) {
                    // 不是负数，下方移动
                    dir = LineDirection.DOWN;
                } else {
                    // 是负数，上方移动
                    dir = LineDirection.UP;
                }
                // 得到正数的移动格数
                line.add(java.util.Map.entry(dir, ((byte) Math.abs(x))));
            }
        }
        // 第一个路径点为填充点，无效移除
        tempOutLine.remove(0x00);
        line.remove(0x00);
        return line;
    }

    /**
     * 将航线转换为tiled使用的点
     */
    public static String lineToPoints(int x, int y, List<java.util.Map.Entry<LineDirection, Byte>> line) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("%d,%d", x, y));
        Rectangle lastOffsetLinePoint = new Rectangle();
        for (java.util.Map.Entry<LineDirection, Byte> linePoint : line) {
            // 注：& 0xFF 负数会变成正数，所以使用强制转换保留负数
            switch (linePoint.getKey()) {
                case UP:
                    lastOffsetLinePoint.setLocation(
                            (int) lastOffsetLinePoint.getX(),
                            (int) lastOffsetLinePoint.getY() + (-(linePoint.getValue() & 0xFF) * 0x10)
                    );
                    break;
                case DOWN:
                    lastOffsetLinePoint.setLocation(
                            (int) lastOffsetLinePoint.getX(),
                            (int) lastOffsetLinePoint.getY() + ((linePoint.getValue() & 0xFF) * 0x10)
                    );
                    break;
                case LEFT:
                    lastOffsetLinePoint.setLocation(
                            (int) lastOffsetLinePoint.getX() + (-(linePoint.getValue() & 0xFF) * 0x10),
                            (int) lastOffsetLinePoint.getY()
                    );
                    break;
                case RIGHT:
                    lastOffsetLinePoint.setLocation(
                            (int) lastOffsetLinePoint.getX() + ((linePoint.getValue() & 0xFF) * 0x10),
                            (int) lastOffsetLinePoint.getY()
                    );
                    break;
                default:
                    continue;
            }
            builder.append(String.format(" %d,%d",
                    (int) lastOffsetLinePoint.getX(),
                    (int) lastOffsetLinePoint.getY()
            ));
        }
        return builder.toString();
    }
}