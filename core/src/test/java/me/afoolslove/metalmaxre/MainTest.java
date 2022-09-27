package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.computer.Computer;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.computer.shop.IShopEditor;
import me.afoolslove.metalmaxre.editors.computer.shop.VendorItemList;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import me.afoolslove.metalmaxre.editors.map.IMapEntranceEditor;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapPoint;
import me.afoolslove.metalmaxre.editors.map.events.EventTile;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.editors.palette.PaletteRow;
import me.afoolslove.metalmaxre.editors.player.IPlayerEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerExpEditor;
import me.afoolslove.metalmaxre.editors.player.PlayerWeapon;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.Sprite;
import me.afoolslove.metalmaxre.editors.tank.ITankEditor;
import me.afoolslove.metalmaxre.editors.tank.Tank;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.Treasure;
import me.afoolslove.metalmaxre.event.editors.editor.EditorApplyEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.*;

public class MainTest {

    @Test
    void test() throws Exception {
        var list = new HashMap<String, MetalMaxRe>();

        // 创建所有预设版本的实例
        for (Map.Entry<String, RomVersion> entry : RomVersion.getVersions().entrySet()) {
            RomBuffer romBuffer = new RomBuffer(entry.getValue(), (Path) null);
            var metalMaxRe = new MetalMaxRe(romBuffer);
            var editorManager = new EditorManagerImpl(metalMaxRe);
            metalMaxRe.setEditorManager(editorManager);

            editorManager.registerDefaultEditors();

            editorManager.loadEditors().get();

            list.put(entry.getKey(), metalMaxRe);
        }
        System.out.println();

        var editors = list.entrySet().parallelStream()
                .map(e -> new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue().getEditorManager().getEditor(IMapEntranceEditor.class)))
                .toList();
        System.out.println();
    }

    @Test
    void romTest() throws Exception {
        class TestEventListener implements EventListener {
            public void test(EditorLoadEvent.Pre event) {
                System.out.println(String.format("准备加载编辑器[%s]", event.getEditor().getClass().getSimpleName()));
            }

            public void test(EditorLoadEvent.Post event) {
                System.out.println(String.format("加载编辑器[%s]完毕", event.getEditor().getClass().getSimpleName()));
            }

            public void test(EditorApplyEvent.Pre event) {
                System.out.println(String.format("准备应用编辑器[%s]", event.getEditor().getClass().getSimpleName()));
            }

            public void test(EditorApplyEvent.Post event) {
                System.out.println(String.format("应用编辑器[%s]完毕", event.getEditor().getClass().getSimpleName()));
            }

            public void test(@Editor.TargetEditor({IDogSystemEditor.class}) EditorApplyEvent event) {
                System.out.println(String.format("target dogSystemEditor %s %s", event.getEditor().getClass().getSimpleName(), event.getClass().getCanonicalName()));
            }
        }
        TestEventListener eventListener = new TestEventListener();


        var rom = Path.of("E:/emulator/fceux/roms/MetalMax_Chinese.nes");
        var romBuffer = new RomBuffer(RomVersion.getChinese(), rom);
//        var rom = Path.of("E:/emulator/fceux/roms/MetalMax_Japanese.nes");
//        var romBuffer = new RomBuffer(RomVersion.getJapanese(), rom);
//        var rom = Path.of("E:/emulator/fceux/roms/MetalMax_SuperHackGeneral.nes");
//        var romBuffer = new RomBuffer(RomVersion.getSuperHackGeneral(), rom);
//        var rom = Path.of("E:/emulator/fceux/roms/MetalMax_SuperHack.nes");
//        var romBuffer = new RomBuffer(RomVersion.getSuperHack(), rom);
        var metalMaxRe = new MetalMaxRe(romBuffer);

        var editorManager = new EditorManagerImpl(metalMaxRe);
        metalMaxRe.setEditorManager(editorManager);
        editorManager.registerDefaultEditors();

        metalMaxRe.getEventHandler().register(eventListener);

        editorManager.loadEditors().get();

//        testIComputerEditor(editorManager);
//        testIVendorEditor(editorManager);
//        testIDataValueEditor(editorManager);
//        testIItemEditor(editorManager);
//        testIEventTilesEditor(editorManager); // 不兼容或前置不兼容
//        // 图块集编辑器
//        // 要是导出的游戏地图显示错误就是它有问题了
//        // 世界地图编辑器
//        // 这玩意儿和上面差不多，直接修改困难
//        testIDogSystemEditor(editorManager);
//        testIMapEntranceEditor(editorManager);
//        testIMapPropertiesEditor(editorManager);
//        testIPaletteEditor(editorManager);
//        testIPlayerEditor(editorManager);
//        testIPlayerExpEditor(editorManager);
//        testISpriteEditor(editorManager);
//        testITankEditor(editorManager);
//        testITreasureEditor(editorManager);
//        testITextEditor(editorManager);

        editorManager.applyEditors().get();

//        TextEditorImpl textEditor = editorManager.getEditor(ITextEditor.class);
//
//        Path textPath = Path.of("C:\\Users\\AFoolLove\\Desktop\\text.txt");
//        StringBuilder builder = new StringBuilder();
//        for (Map.Entry<Integer, List<TextBuilder>> listEntry : textEditor.getIndexPages().entrySet()) {
//            int indexPage = textEditor.getIndexPagexx(listEntry.getKey());
//            DataAddress dataAddress = textEditor.getTextAddresses().get(indexPage);
//
//            builder.append(String.format("%02X:%05X-%05X\n", listEntry.getKey(), dataAddress.getKey() + 0x10, dataAddress.getValue() + 0x10));
//
//            for (int i = 0; i < listEntry.getValue().size(); i++) {
//                builder.append(String.format("%02X:%02X  ", listEntry.getKey(), i));
//                builder.append(listEntry.getValue().get(i));
//                builder.append('\n');
//            }
//        }
//
//        try (OutputStream outputStream = Files.newOutputStream(textPath)) {
//            outputStream.write(builder.toString().getBytes());
//        }

        /**
         * 文本编辑器：36000-37FFF 剩余2个字节
         * 文本编辑器：18000-19FFF 剩余92个字节
         * 文本编辑器：124DE-1330F 剩余2个字节
         *
         *
         * 文本编辑器：14000-15FFF 剩余4个字节
         * 文本编辑器：1F98A-1FFFF 剩余1个字节
         * 文本编辑器：10119-10DA2 剩余1个字节
         * 文本编辑器：36000-37FFF 剩余7个字节
         * 文本编辑器：18000-19FFF 剩余97个字节
         * 文本编辑器：13310-13FFF 剩余3个字节
         * 文本编辑器：124DE-1330F 剩余4个字节
         * 文本编辑器：10DA3-112E1 剩余1个字节
         *
         */

        romBuffer.save(rom.resolveSibling("MetalMax_ChineseC.nes"));
//        romBuffer.save(rom.resolveSibling("MetalMax_JapaneseC.nes"));
//        romBuffer.save(rom.resolveSibling("MetalMax_SuperHackGeneralC.nes"));
//        romBuffer.save(rom.resolveSibling("MetalMax_SuperHackC.nes"));
    }

    void testIVendorEditor(IEditorManager editorManager) {
        // 售货机编辑器
        // 将所有售货机物品的第二个物品改成 犬系统（传真）
        IShopEditor iShopEditor = editorManager.getEditor(IShopEditor.class);
        for (VendorItemList vendorItemList : iShopEditor.getVendorItemLists()) {
            vendorItemList.get(1).setItem(0xCB);
        }
    }

    void testIComputerEditor(IEditorManager editorManager) {
        // 计算机编辑器
        // 将楼下的所有计算机都设置为 售货机
        IComputerEditor<Computer> iComputerEditor = editorManager.getEditor(IComputerEditor.class);
        for (Computer computer : iComputerEditor.getComputers()) {
            if (computer.intMap() == 0x02) {
                computer.setType(0x00);
            }
        }
    }

    void testIDataValueEditor(IEditorManager editorManager) {
        // 数据值编辑器
        // 将数据值的 0x00 的值从 5 改成 6，弹弓的攻击力等
        IDataValueEditor iDataValueEditor = editorManager.getEditor(IDataValueEditor.class);
        iDataValueEditor.getValues().put(0, 6);
    }

    void testIItemEditor(IEditorManager editorManager) {
        // 物品编辑器
        // 将人类武器 弩 的价格变更为 0xD0(85100)，通过 IDataValueEditor 可以修改价格
        IItemEditor iItemEditor = editorManager.getEditor(IItemEditor.class);
        // 人类武器 弩，设置价格
        iItemEditor.getItem(0x2A).setPrice(0xD0);
        // 人类武器 弹筒，设置攻击力
        ((PlayerWeapon) iItemEditor.getItem(0x39)).setAttack(0xD0);
    }

    void testIEventTilesEditor(IEditorManager editorManager) {
        // 事件图块编辑器
        // 将地图id 1的第一个事件图块坐标改为5,5，并将图块设置为当前地图的图块集的第一个图块，一般为纯黑色图块
        IEventTilesEditor iEventTilesEditor = editorManager.getEditor(IEventTilesEditor.class);
        for (Map.Entry<Integer, List<EventTile>> entry : iEventTilesEditor.getEventTile(0x01).entrySet()) {
//            entry.getValue().get(0x00).offsetX(-1);
//            entry.getValue().get(0x00).setTile(entry.getValue().get(0x00).getTile());
            entry.getValue().add(new EventTile(0x05, 0x05, 0x00));
            break;
        }
    }

    void testIDogSystemEditor(IEditorManager editorManager) {
        // 犬系统编辑器
        // 使用传真时，传送的位置X偏移2
        IDogSystemEditor iDogSystemEditor = editorManager.getEditor(IDogSystemEditor.class);
        iDogSystemEditor.getTownLocation(0).offsetX(2);
    }

    void testIMapEntranceEditor(IEditorManager editorManager) {
        // 地图出入口和边界编辑器
        // 拉多的所有入口的目的地（酒吧等）X偏移
        IMapEntranceEditor iMapEntranceEditor = editorManager.getEditor(IMapEntranceEditor.class);
        for (Map.Entry<MapPoint, MapPoint> entry : iMapEntranceEditor.getMapEntrance(0x01).getEntrances().entrySet()) {
            entry.getValue().offsetX(1);
        }
    }

    void testIMapPropertiesEditor(IEditorManager editorManager) {
        // 地图属性编辑器
        // 将家里的门后图块改成黑块（开门后是黑块）
        IMapPropertiesEditor iMapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        iMapPropertiesEditor.getMapProperties(0x03).setHideTile(0x00);
    }

    void testIPaletteEditor(IEditorManager editorManager) {
        // 调色板编辑器
        // 将精灵的调色板第二个颜色修改成 绿色的
        IPaletteEditor iPaletteEditor = editorManager.getEditor(IPaletteEditor.class);
        for (PaletteRow paletteRow : iPaletteEditor.getSpritePalette()) {
            paletteRow.getPaletteRow()[1] = 0x19;
        }
    }

    void testIPlayerEditor(IEditorManager editorManager) {
        // 玩家编辑器
        // 将玩家的初始金钱修改为 9999
        IPlayerEditor iPlayerEditor = editorManager.getEditor(IPlayerEditor.class);
        iPlayerEditor.setMoney(9999);
    }

    void testIPlayerExpEditor(IEditorManager editorManager) {
        // 玩家经验值编辑器
        // 玩家升级至2级只需要1点经验值
        IPlayerExpEditor iPlayerExpEditor = editorManager.getEditor(IPlayerExpEditor.class);
        iPlayerExpEditor.setLevelExp(0x02, 0x01);
    }

    void testISpriteEditor(IEditorManager editorManager) {
        // 精灵编辑器
        // 所有有精灵的地图的第一个精灵X+1
        ISpriteEditor iSpriteEditor = editorManager.getEditor(ISpriteEditor.class);
        for (Map.Entry<Integer, List<Sprite>> entry : iSpriteEditor.getSprites().entrySet()) {
            if (!entry.getValue().isEmpty()) {
                entry.getValue().get(0).offsetX(1);
            }
        }
    }

    void testITankEditor(IEditorManager editorManager) {
        // 坦克编辑器
        // 将NO.1的默认位置放在楼下的修车位置并将地盘防御力修改为99
        ITankEditor iTankEditor = editorManager.getEditor(ITankEditor.class);
        iTankEditor.getTankInitAttribute(Tank.NO_1).set(0x02, 0x08, 0x0B);
        iTankEditor.getTankInitAttribute(Tank.NO_1).setDefense(99);
    }

    void testITreasureEditor(IEditorManager editorManager) {
        // 宝藏编辑器
        // 将家里的宝藏修改为 犬系统（传真）
        ITreasureEditor iTreasureEditor = editorManager.getEditor(ITreasureEditor.class);
        for (Treasure treasure : iTreasureEditor.getTreasures()) {
            if (treasure.getMap() == 0x03) {
                treasure.setItem(0xCB);
                break;
            }
        }
    }

    void testITextEditor(IEditorManager editorManager) {
        ITextEditor textEditor = editorManager.getEditor(ITextEditor.class);
        // 将城镇名称 拉多 更改为 本地
        textEditor.setTownName(0x00, "本地");
        // 将人类武器名称 弩 更名为 BB
        textEditor.setItemName(0x2A, "BB");
    }
}
