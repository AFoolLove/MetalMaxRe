package me.afoolslove.metalmaxre.gui;

import me.afoolslove.metalmaxre.EditorWorker;

import java.util.List;
import java.util.Map;

/**
 * 窗口版
 *
 * @author AFoolLove
 */
public class WindowEditorWorker extends EditorWorker {

    @Override
    protected void process(List<Map.Entry<EditorWorker.ProcessState, Object>> chunks) {
        for (Map.Entry<ProcessState, Object> chunk : chunks) {
            if (chunk.getKey() == ProcessState.MESSAGE) {
                System.out.println(chunk.getValue());
            }
        }
    }

    @Override
    protected void done() {

    }
}
