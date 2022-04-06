package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editors.IEditorListener;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.computer.impl.ComputerEditorImpl;
import me.afoolslove.metalmaxre.editors.impl.EditorManagerImpl;
import me.afoolslove.metalmaxre.utils.ResourceManager;
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
            metalMaxRe.setEditorManager(new EditorManagerImpl(metalMaxRe));

            var editorManager = metalMaxRe.getEditorManager();

            editorManager.register(IComputerEditor.class, ComputerEditorImpl::new);

            var computerEditor = editorManager.getEditor(IComputerEditor.class);
            computerEditor.getListeners().add(new IEditorListener() {
                @Override
                public void onPreLoad(@NotNull IRomEditor editor) {
                    System.out.println("开始加载" + editor.getClass().getSimpleName());
                }

                @Override
                public void onPostLoad(@NotNull IRomEditor editor, long time) {
                    System.out.format("加载%s结束，用时%.3fs\n", editor.getClass().getSimpleName(), time / 1000.F);
                }
            });

            editorManager.loadEditors();



            list.put(entry.getKey(), metalMaxRe);
        }
        System.out.println();
    }
}
