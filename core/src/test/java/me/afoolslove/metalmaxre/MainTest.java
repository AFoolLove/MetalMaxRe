package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.map.CameraMapPoint;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import me.afoolslove.metalmaxre.event.editors.editor.EditorApplyEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

public class MainTest {

    @Test
    void test() throws IOException {
        var list = new HashMap<String, MetalMaxRe>();

        // 创建所有预设版本的实例
        for (Map.Entry<String, RomVersion> entry : RomVersion.getVersions().entrySet()) {
            RomBuffer romBuffer = new RomBuffer(entry.getValue(), null);
            var metalMaxRe = new MetalMaxRe(romBuffer);
            var editorManager = new EditorManagerImpl(metalMaxRe);
            metalMaxRe.setEditorManager(editorManager);

            editorManager.registerDefaultEditors();

            editorManager.loadEditors();

            list.put(entry.getKey(), metalMaxRe);
        }
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
        var metalMaxRe = new MetalMaxRe(romBuffer);

        var editorManager = new EditorManagerImpl(metalMaxRe);
        metalMaxRe.setEditorManager(editorManager);
        editorManager.registerDefaultEditors();

        metalMaxRe.getEventHandler().register(eventListener);

        editorManager.loadEditors().get();

        IDogSystemEditor dogSystemEditor = editorManager.getEditor(IDogSystemEditor.class);
        for (CameraMapPoint point : dogSystemEditor.getTownLocations()) {
            point.offset(1, 1);
        }

        editorManager.applyEditors().get();

        romBuffer.save(rom.resolveSibling("E:/emulator/fceux/roms/MetalMax_ChineseC.nes"));

    }
}
