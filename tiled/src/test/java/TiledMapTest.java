import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.WorldMapEditorImpl;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMapUtils;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import org.junit.jupiter.api.Test;
import org.mapeditor.core.TileSet;
import org.mapeditor.io.TMXMapWriter;
import org.mapeditor.util.BasicTileCutter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class TiledMapTest {
    @Test
    void tiledMapTest() throws IOException, ExecutionException, InterruptedException {
        var romBuffer = new RomBuffer(RomVersion.getChinese(), (Path) null);
        var metalMaxRe = new MetalMaxRe(romBuffer);

        metalMaxRe.useDefault();
        ((EditorManagerImpl) metalMaxRe.getEditorManager()).registerDefaultEditors();

        metalMaxRe.getEditorManager().loadEditors().get();

        IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);

        int mapId = 0x01;
        File selectedFile = new File("C:/Users/AFoolLove/Desktop/map");

        MapProperties mapProperties = mapPropertiesEditor.getMapProperties(mapId);
        String tileSetName = String.format("%08X-%02X%02X-%04X", mapProperties.getIntTiles(), mapProperties.combinationA, mapProperties.combinationB, (int) mapProperties.palette);

        try {

            var tmxMapWriter = new TMXMapWriter();

            BufferedImage tileSetBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));
            File output = new File(selectedFile, String.format("/tsx/png/%s.png", tileSetName));
            output.getParentFile().mkdirs();
            ImageIO.write(tileSetBufferedImage, "PNG", output);
            TileSet tsx = TiledMapUtils.createTsx(metalMaxRe, tmxMapWriter, output, mapProperties, new File(selectedFile, "tsx"));
            tsx.setName(tileSetName);
            BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
            String spriteName = String.format("0405%02X%02X", mapProperties.spriteIndex, mapProperties.spriteIndex + 1);
            output = new File(selectedFile, String.format("/sprite/%s.png", spriteName));
            output.getParentFile().mkdirs();
            ImageIO.write(spriteBufferedImage, "PNG", output);

            TileSet spriteTileSet = new TileSet();
            spriteTileSet.setName(spriteName);
            spriteTileSet.importTileBitmap(output.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));

            tmxMapWriter.writeMap(TiledMapUtils.create(metalMaxRe, mapId, tsx, spriteTileSet), new File(selectedFile, String.format("%02X.tmx", mapId)).getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void worldTiledMapTest() throws IOException, ExecutionException, InterruptedException {
        var romBuffer = new RomBuffer(RomVersion.getSuperHackGeneral(), Path.of("C:\\Users\\AFoolLove\\IdeaProjects\\MetalMaxRe\\core\\src\\main\\resources\\roms\\MetalMax_SuperHackGeneral.nes"));
        var metalMaxRe = new MetalMaxRe(romBuffer);

        metalMaxRe.useDefault();
        var editorManager = metalMaxRe.getEditorManager();
        ((EditorManagerImpl) editorManager).registerDefaultEditors();

        editorManager.loadEditors().get();

        IMapPropertiesEditor mapPropertiesEditor = editorManager.getEditor(IMapPropertiesEditor.class);
        IWorldMapEditor worldMapEditor = editorManager.getEditor(IWorldMapEditor.class);
        ITileSetEditor tileSetEditor = editorManager.getEditor(ITileSetEditor.class);

        var tmxMapWriter = new TMXMapWriter();

        File selectedFile = new File("C:/Users/AFoolLove/Desktop/map/sh_world");


        Map<Integer, TileSet> collect = WorldMapEditorImpl.DEFAULT_PIECES.values().parallelStream().distinct().map(integer -> {
                    String name = String.format("%08X", integer);
                    File out = new File(selectedFile, "tsx/png/" + name + ".png");
                    if (!out.exists()) {
                        out.getParentFile().mkdirs();
                        try {
                            if (out.createNewFile()) {
                                BufferedImage bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateWorldTileSet(metalMaxRe, integer));
                                ImageIO.write(bufferedImage, "PNG", out);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    File tsxOut = new File(selectedFile, "tsx");
                    tsxOut.mkdirs();
                    try {
                        return Map.entry(integer, TiledMapUtils.createWorldTsx(metalMaxRe, tmxMapWriter, out, integer, tsxOut));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull) // 移除 null
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


        String spriteName = String.format("0405%02X%02X", 0x94, 0x95);
        File spritePng = new File(selectedFile, "sprite/png/" + spriteName + ".png");
        try {
            if (!spritePng.exists()) {
                spritePng.getParentFile().mkdirs();
                if (spritePng.createNewFile()) {
                    BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, 0x94));
                    ImageIO.write(spriteBufferedImage, "PNG", spritePng);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        TileSet spriteTileSet = new TileSet();
        spriteTileSet.setName(spriteName);
        spriteTileSet.setTileSpacing(0);
        spriteTileSet.setTileMargin(0);
        try {
            spriteTileSet.importTileBitmap(spritePng.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        org.mapeditor.core.Map world = TiledMapUtils.createWorld(metalMaxRe, WorldMapEditorImpl.DEFAULT_PIECES, collect, spriteTileSet);
        try {
            File worldTmx = new File(selectedFile, "sh_world.tmx");
            if (!worldTmx.exists()) {
                if (!worldTmx.createNewFile()) {
                    return;
                }
            }
            tmxMapWriter.writeMap(world, worldTmx.getAbsolutePath());
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}