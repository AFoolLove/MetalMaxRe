package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.EditorManagerImpl;
import me.afoolslove.metalmaxre.editors.IEditorListener;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import org.jetbrains.annotations.NotNull;
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
            var editorManager = new EditorManagerImpl(metalMaxRe);
            metalMaxRe.setEditorManager(editorManager);

            var editorListener = new IEditorListener() {
                @Override
                public void onPreLoad(@NotNull IRomEditor editor) {
                    System.out.println("开始加载" + editor.getClass().getSimpleName());
                }

                @Override
                public void onPostLoad(@NotNull IRomEditor editor, long time) {
                    System.out.format("加载%s结束，用时%.3fs\n", editor.getClass().getSimpleName(), time / 1000.F);
                }
            };

            editorManager.registerDefaultEditors();

            var computerEditor = editorManager.getEditor(IComputerEditor.class);
            var dogSystemEditor = editorManager.getEditor(IDogSystemEditor.class);
            computerEditor.getListeners().add(editorListener);
            dogSystemEditor.getListeners().add(editorListener);

            editorManager.loadEditors();


            list.put(entry.getKey(), metalMaxRe);
        }
        System.out.println();
    }
}
