package me.afoolslove.metalmaxre.desktop.adapter;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.MultipleMetalMaxRe;
import me.afoolslove.metalmaxre.desktop.MapSelectListModel;
import me.afoolslove.metalmaxre.editors.map.IMapEditor;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMapUtils;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapReader;
import org.mapeditor.io.TMXMapWriter;
import org.mapeditor.util.BasicTileCutter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class MapImagePopupMenuMouseAdapter extends MouseAdapter {
    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JPopupMenu worldPopupMenu = new JPopupMenu();
    private final JLabel mapImage;
    private final MapSelectListModel mapSelectListModel;

    public MapImagePopupMenuMouseAdapter(JLabel mapImage,
                                         JList<String> mapList,
                                         MultipleMetalMaxRe multipleMetalMaxRe,
                                         MapSelectListModel mapSelectListModel,
                                         Map<Integer, ImageIcon> mapImages) {
        this.mapImage = mapImage;
        this.mapSelectListModel = mapSelectListModel;

        JMenuItem copyPng = new JMenuItem("复制图片");
        copyPng.setEnabled(false);
        copyPng.addActionListener(e -> {

        });
        JMenuItem copyMapPropertiesData = new JMenuItem("复制地图属性数据");
        copyMapPropertiesData.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance() || mapSelectListModel.getSelectedMap() == -1) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);

            StringBuilder builder = new StringBuilder();
            byte[] build = mapPropertiesEditor.getMapProperties(mapSelectListModel.getSelectedMap()).toByteArray();
            for (byte b : build) {
                builder.append(String.format("%02X", b));
            }
            popupMenu.getToolkit().getSystemClipboard().setContents(new StringSelection(builder.toString()), null);
        });
        JMenuItem copyMapData = new JMenuItem("复制地图数据");
        copyMapData.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            IMapEditor mapEditor = metalMaxRe.getEditorManager().getEditor(IMapEditor.class);
            StringBuilder builder = new StringBuilder();
            byte[] build = mapEditor.getMap(mapSelectListModel.getSelectedMap()).build();
            for (byte b : build) {
                builder.append(String.format("%02X", b));
            }
            popupMenu.getToolkit().getSystemClipboard().setContents(new StringSelection(builder.toString()), null);
        });

        JMenuItem exportMap = new JMenuItem("导出该地图");
        exportMap.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int state = fileChooser.showSaveDialog(mapImage);
            if (state == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                int selectMap = mapSelectListModel.getSelectedMap();

                // selectedDir -> tsx
                //                -> png
                //             -> sprite
                //                -> png
                //             -> [MapId].tmx
                selectedFile.mkdirs();

                final TMXMapWriter tmxMapWriter = new TMXMapWriter();
//                        ITileSetEditor tileSetEditor = metalMaxRe.getEditorManager().getEditor(ITileSetEditor.class);
                IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);

                MapProperties mapProperties = mapPropertiesEditor.getMapProperties(selectMap);
                String tileSetName = String.format("%08X-%02X%02X-%04X", mapProperties.getIntTiles(), mapProperties.combinationA, mapProperties.combinationB, (int) mapProperties.palette);

                try {
                    BufferedImage tileSetBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));
                    File output = new File(selectedFile, String.format("/tsx/png/%s.png", tileSetName));
                    output.getParentFile().mkdirs();
                    ImageIO.write(tileSetBufferedImage, "PNG", output);
                    TileSet tsx = TiledMapUtils.createTsx(metalMaxRe, tmxMapWriter, output, mapProperties, new File(selectedFile, "tsx"));
                    tsx.setName(tileSetName);
                    BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
                    String spriteName = String.format("0405%02X%02X", mapProperties.spriteIndex, (byte) (mapProperties.spriteIndex + 1));
                    output = new File(selectedFile, String.format("/sprite/%s.png", spriteName));
                    output.getParentFile().mkdirs();
                    ImageIO.write(spriteBufferedImage, "PNG", output);

                    TileSet spriteTileSet = new TileSet();
                    spriteTileSet.setName(spriteName);
                    spriteTileSet.importTileBitmap(output.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));

                    tmxMapWriter.writeMap(TiledMapUtils.create(metalMaxRe, selectMap, tsx, spriteTileSet), new File(selectedFile, String.format("%02X.tmx", selectMap)).getAbsolutePath());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                System.out.printf("导出地图%02X完成\n", selectMap);
            }
        });

        JMenuItem importAndReplaceMap = new JMenuItem("导入并替换地图");
        importAndReplaceMap.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }
            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();

            // 文件选择器
            JFileChooser fileChooser = new JFileChooser();
            // 添加一个nes后缀的文件过滤器
            fileChooser.setFileFilter(new FileNameExtensionFilter("Tiled Map(*.tmx)", "TMX"));
            // 开始选择文件
            int state = fileChooser.showOpenDialog(mapImage);
            if (state == JFileChooser.APPROVE_OPTION) {
                try {
                    TMXMapReader tmxMapReader = new TMXMapReader();
                    org.mapeditor.core.Map map = tmxMapReader.readMap(fileChooser.getSelectedFile().toString());
                    int selectMap = mapSelectListModel.getSelectedMap();
                    TiledMapUtils.importMap(metalMaxRe, selectMap, map);

                    if (mapImages.containsKey(selectMap) && mapImages.get(selectMap) != null) {
                        mapImages.remove(selectMap);
                        // 重新选中一次，加载图片
                        mapList.removeSelectionInterval(selectMap, selectMap);
                        mapList.setSelectedIndex(selectMap);
                    }
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        });


        popupMenu.add(copyPng);                 // 复制图片
        popupMenu.add(copyMapPropertiesData);   // 复制地图属性数据
        popupMenu.add(copyMapData);             // 复制地图数据
        popupMenu.addSeparator();               // ----------------
        popupMenu.add(exportMap);               // 导出该地图
        popupMenu.add(importAndReplaceMap);     // 导入并替换地图


        JMenuItem worldMapCapacity = new JMenuItem("世界地图容量");
        worldMapCapacity.addActionListener(e -> {
            if (!multipleMetalMaxRe.hasInstance()) {
                return;
            }

            MetalMaxRe metalMaxRe = multipleMetalMaxRe.current();
            IWorldMapEditor worldMapEditor = metalMaxRe.getEditorManager().getEditor(IWorldMapEditor.class);
            int sum = Arrays.stream(worldMapEditor.getIndexesCapacity()).sum();

            Set<Integer> tileHashCodes = new HashSet<>();
            byte[][] map = worldMapEditor.getMap();
            for (int y = 0; y < 0x40; y++) {
                for (int x = 0; x < 0x40; x++) {
                    tileHashCodes.add(Arrays.hashCode(IWorldMapEditor.getTiles4x4(map, x, y)));
                }
            }
            // 根据剩余容量不同提示不同的文本
            int capacity = sum - tileHashCodes.size();
            if (sum > 0) {
                JOptionPane.showConfirmDialog(null, String.format("剩余空间容量：%d/%d", capacity, sum), "世界地图剩余容量", JOptionPane.OK_CANCEL_OPTION);
            } else if (sum < 0) {
                JOptionPane.showConfirmDialog(null, String.format("超出空间容量：%d/%d\n超出了%d，请确保剩余空间足够储存后再储存。", tileHashCodes.size(), sum, Math.abs(capacity)), "世界地图剩余容量", JOptionPane.OK_CANCEL_OPTION);
            } else {
                JOptionPane.showConfirmDialog(null, "空间刚好使用完毕", "世界地图剩余容量", JOptionPane.OK_CANCEL_OPTION);
            }

        });

        worldPopupMenu.add(worldMapCapacity);   // 世界地图容量
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            int selectedMap = mapSelectListModel.getSelectedMap();
            if (selectedMap == -1) {
                return;
            }

            if (selectedMap == 0x00) {
                worldPopupMenu.show(mapImage, e.getX(), e.getY());
            } else {
                popupMenu.show(mapImage, e.getX(), e.getY());
            }
        }
    }
}
