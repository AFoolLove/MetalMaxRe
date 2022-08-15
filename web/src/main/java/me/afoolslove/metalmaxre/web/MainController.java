package me.afoolslove.metalmaxre.web;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.RomVersion;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.map.IMapPropertiesEditor;
import me.afoolslove.metalmaxre.helper.TileSetHelper;
import me.afoolslove.metalmaxre.utils.BufferedImageUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
public class MainController {
    private final Map<Integer, MetalMaxRe> map = new ConcurrentHashMap<>();

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    @GetMapping(value = "/tileSet", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getTileSet(@RequestParam(required = false) String xx,
                             @RequestParam(required = false) String mapId,
                             @CookieValue("session") String session,
                             HttpServletRequest httpRequest) throws IOException {
        var metalMaxRe = map.get(Objects.hash(httpRequest.getRemotePort(), httpRequest.getRemoteAddr()));
        if (metalMaxRe == null) {
            if (session != null) {
                metalMaxRe = map.get(Integer.parseInt(session));
            }
        }

        if (metalMaxRe == null) {
            return null;
        }

        if (mapId != null) {
            IMapPropertiesEditor mapPropertiesEditor = metalMaxRe.getEditorManager().getEditor(IMapPropertiesEditor.class);
            var mapProperties = mapPropertiesEditor.getMapProperties(Integer.parseInt(mapId, 16));
            var bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, mapProperties, null));
            var outputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "PNG", outputStream);
            return outputStream.toByteArray();
        }

        if (xx == null) {
            return null;
        }

        int x = Integer.parseInt(xx, 16);
        int x00, x40, x80, xC0;
        x00 = (x & 0xFF000000) >>> 24;
        x40 = (x & 0x00FF0000) >>> 16;
        x80 = (x & 0x0000FF00) >>> 8;
        xC0 = (x & 0x000000FF) >>> 0;
        var bufferedImage = BufferedImageUtils.fromColors(TileSetHelper.generateTileSet(metalMaxRe, x00, x40, x80, xC0, 0x00, 0x01, null));
        var outputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    @GetMapping(value = "/map", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getMapPng(@RequestParam String mapId,
                            @CookieValue("session") String session, HttpServletRequest httpRequest) throws IOException {
        var metalMaxRe = map.get(Objects.hash(httpRequest.getRemotePort(), httpRequest.getRemoteAddr()));
        if (metalMaxRe == null) {
            if (session != null) {
                metalMaxRe = map.get(Integer.parseInt(session));
            }
        }

        if (metalMaxRe == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        var bufferedImage = TileSetHelper.generateMapImage(metalMaxRe, Integer.parseInt(mapId, 16));
        ImageIO.write(bufferedImage, "PNG", outputStream);
        return outputStream.toByteArray();
    }

    @GetMapping("/hasCreate")
    public boolean hasCreate(@CookieValue("session") String session, HttpServletRequest httpRequest) {
        var hash = Objects.hash(httpRequest.getRemotePort(), httpRequest.getRemoteAddr());
        if (map.containsKey(hash)) {
            return true;
        }
        if (session != null && !session.isEmpty()) {
            if (map.containsKey(Integer.parseInt(session))) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/list")
    public String onList() {
        return Arrays.toString(map.keySet().toArray());
    }

    @PostMapping("/upload")
    public String onUpload(@RequestParam(value = "ver", defaultValue = "chinese") String ver,
                           MultipartFile uploadFile, HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ExecutionException, InterruptedException {
        return executorService.submit(() -> {
            var version = RomVersion.getVersion(ver);
            if (version == null) {
                version = RomVersion.getChinese();
            }
            var romBuffer = new RomBuffer(version, uploadFile.getBytes());
            var metalMaxRe = new MetalMaxRe(romBuffer);
            metalMaxRe.useDefault();
            ((EditorManagerImpl) metalMaxRe.getEditorManager()).registerDefaultEditors();

            metalMaxRe.getEditorManager().loadEditors().get();

            var hash = Objects.hash(httpRequest.getRemotePort(), httpRequest.getRemoteAddr());
            map.put(hash, metalMaxRe);
            httpResponse.addCookie(new Cookie("session", Objects.toString(hash)));
            return "OK";
        }).get();
    }

    @GetMapping("/load")
    public boolean onLoad(@RequestParam(value = "ver", defaultValue = "chinese") String ver,
                          HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws ExecutionException, InterruptedException {
        return executorService.submit(() -> {
            var version = RomVersion.getVersion(ver);
            if (version == null) {
                version = RomVersion.getChinese();
            }
            var romBuffer = new RomBuffer(version, (Path) null);
            var metalMaxRe = new MetalMaxRe(romBuffer);
            metalMaxRe.useDefault();
            ((EditorManagerImpl) metalMaxRe.getEditorManager()).registerDefaultEditors();

            metalMaxRe.getEditorManager().loadEditors().get();

            var hash = Objects.hash(httpRequest.getRemotePort(), httpRequest.getRemoteAddr());
            map.put(hash, metalMaxRe);
            httpResponse.addCookie(new Cookie("session", Objects.toString(hash)));
            return true;
        }).get();
    }
}
