package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;

import javax.swing.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * 编辑器异步加载
 * <p>
 * K：是否正常加载完毕，为true时，可能部分读取失败，但并不严重，为false时，无法正常加载
 * V：中间数据，某个编辑器是否加载成功
 *
 * @author AFoolLove
 */
public abstract class EditorWorker extends SwingWorker<Boolean, Map.Entry<EditorWorker.ProcessState, Object>> {

    @Override
    protected abstract void process(List<Map.Entry<ProcessState, Object>> chunks);

    @Override
    protected Boolean doInBackground() throws Exception {
        // 暂时不进行同时加载
        try {
            MetalMaxRe instance = MetalMaxRe.getInstance();
            byte[] bytes = Files.readAllBytes(instance.getTarget().toPath());
            if (instance.getBuffer() != null) {
                instance.getBuffer().clear(); // 怎么释放呢？
            }

            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            instance.setBuffer(buffer);
            // 写入到 ByteBuffer
            buffer.put(bytes);

            // 编辑器重新读取数据

            long start = 0, count = 0;
            int successful = 0, failed = 0;
            if (!EditorManager.getEditors().isEmpty()) {
                publish(Map.entry(ProcessState.MESSAGE, "开始加载编辑器"));
                for (AbstractEditor editor : EditorManager.getEditors().values()) {
                    start = System.currentTimeMillis();
                    publish(Map.entry(ProcessState.MESSAGE, "加载编辑器：" + editor.getClass().getSimpleName()));

                    boolean state = false;
                    try {
                        state = editor.onRead(buffer);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (state) {
                        successful++;
                        long time = System.currentTimeMillis() - start;
                        count += time;
                        publish(Map.entry(ProcessState.MESSAGE, String.format(" 加载成功！耗时：%dms", time)));
                        publish(Map.entry(ProcessState.RESULT, true));
                    } else {
                        failed++;
                        long time = System.currentTimeMillis() - start;
                        count += time;
                        publish(Map.entry(ProcessState.MESSAGE, String.format(" 加载失败！耗时：%dms", time)));
                        publish(Map.entry(ProcessState.RESULT, false));
                    }
                }
                publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器结束，共%d个编辑器，成功%d个，失败%d个", successful + failed, successful, failed)));
                publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器共计耗时：%dms", count)));
                // 全部加载失败，那还得了？
                return successful != 0;
            } else {
                System.out.println("没有可用的编辑器！");
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public enum ProcessState {
        /**
         * 消息，String
         */
        MESSAGE,
        /**
         * 编辑器的加载结果，Boolean
         */
        RESULT,
        /**
         * 其它
         */
        UNKNOWN;
    }
}
