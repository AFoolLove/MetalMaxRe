package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.impl.EditorManagerImpl;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainTest {

    @Test
    void test() throws IOException {
        var list = new HashMap<String, MetalMaxRe>();

        for (Map.Entry<String, RomVersion> entry : RomVersion.getVersions().entrySet()) {
            RomBuffer romBuffer = new RomBuffer(entry.getValue(), null);
            var metalMaxRe = new MetalMaxRe(romBuffer);
            metalMaxRe.setEditorManager(new EditorManagerImpl(metalMaxRe));

            var editorManager = metalMaxRe.getEditorManager();
            editorManager.loadEditors();
            list.put(entry.getKey(), metalMaxRe);
        }
        System.out.println();
    }
}
