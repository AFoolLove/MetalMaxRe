import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.editors.map.MapProperties;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.tiled.TiledMap;
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
import java.util.concurrent.ExecutionException;

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
            TileSet tsx = TiledMap.createTsx(metalMaxRe, tmxMapWriter, output, mapProperties, new File(selectedFile, "tsx"));
            tsx.setName(tileSetName);
            BufferedImage spriteBufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateSpriteTileSet(metalMaxRe, mapProperties.spriteIndex));
            String spriteName = String.format("0405%02X%02X", mapProperties.spriteIndex, mapProperties.spriteIndex + 1);
            output = new File(selectedFile, String.format("/sprite/%s.png", spriteName));
            output.getParentFile().mkdirs();
            ImageIO.write(spriteBufferedImage, "PNG", output);

            TileSet spriteTileSet = new TileSet();
            spriteTileSet.setName(spriteName);
            spriteTileSet.importTileBitmap(output.getAbsolutePath(), new BasicTileCutter(0x10, 0x10, 0, 0));

            tmxMapWriter.writeMap(TiledMap.create(metalMaxRe, mapId, tsx, spriteTileSet), new File(selectedFile, String.format("%02X.tmx", mapId)).getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}