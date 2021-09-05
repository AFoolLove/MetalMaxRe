package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.ReadBefore;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

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
            byte[] bytes;
            if (instance.isIsInitTarget()) {
                // 直接获取流
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("MetalMax.nes");
                if (resourceAsStream == null) {
                    // 读取失败可还行
                    return false;
                }
                resourceAsStream.transferTo(byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
            } else {
                // 外部路径
                bytes = Files.readAllBytes(instance.getTarget().toPath());
            }
            if (instance.getBuffer() != null) {
                instance.getBuffer().clear(); // 怎么释放呢？
            }

            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            instance.setBuffer(buffer);
            // 写入到 ByteBuffer
            buffer.put(bytes);

            // 读取头属性
            instance.setHeader(new GameHeader(buffer));

            // 编辑器重新读取数据

            // index 0 = totalTime
            // index 1 = realTime
            long[] totalTime = {0, 0};
            // index 0 = successful
            // index 1 = failed
            int[] result = {0, 0};

            if (!EditorManager.getEditors().isEmpty()) {
                publish(Map.entry(ProcessState.MESSAGE, "开始加载编辑器"));

                List<Set<Class<? extends AbstractEditor<?>>>> editorLists = new ArrayList<>();
                editorLists.add(new HashSet<>(EditorManager.getEditors().keySet()));

                do {
                    Set<Class<? extends AbstractEditor<?>>> editors = new HashSet<>();
                    Set<Class<? extends AbstractEditor<?>>> classes = editorLists.get(editorLists.size() - 1);

                    // 过滤无序的编辑器
                    for (var clazz : classes) {
                        ReadBefore annotation = clazz.getAnnotation(ReadBefore.class);
                        if (annotation == null || annotation.value().length == 0) {
                            continue;
                        }
                        editors.addAll(Arrays.asList(annotation.value()));
                    }

                    if (editors.isEmpty()) {
                        break;
                    }
                    editorLists.add(editors);
                } while (true);

                // 将排序的编辑器去重
                // 越前面的编辑器在后面出现的话以后面为准
                for (Set<Class<? extends AbstractEditor<?>>> editorList : editorLists) {
                    var sets = new ArrayList<>(editorLists);
                    sets.remove(editorList);
                    Collections.reverse(sets);

                    for (Set<Class<? extends AbstractEditor<?>>> set : sets) {
                        set.forEach(editorList::remove);
                    }
                }

                // 倒序，优先加载前置
                Collections.reverse(editorLists);

                final Method onReadBeforeMethod = AbstractEditor.Listener.class.getMethod("onReadBefore", AbstractEditor.class);
                final Method onReadAfterMethod = AbstractEditor.Listener.class.getMethod("onReadAfter", AbstractEditor.class);
                totalTime[0x01] = System.currentTimeMillis();
                for (Set<Class<? extends AbstractEditor<?>>> editorList : editorLists) {
                    List<AbstractEditor<?>> collect = editorList.parallelStream()
                            .map(editorClazz -> EditorManager.getEditors().get(editorClazz))
                            .collect(Collectors.toList());

                    collect.parallelStream().forEach(abstractEditor -> {
                        long start = 0, end = 0;
                        boolean state = false;
                        try {
                            // 防止监听器抛出异常后无法得到编辑器读取起始时间
                            start = System.currentTimeMillis();
                            for (AbstractEditor.Listener<?> listener : abstractEditor.getListeners()) {
                                onReadBeforeMethod.invoke(listener, abstractEditor);
                            }
                            // 真正的编辑器读取起始时间
                            start = System.currentTimeMillis();
                            state = abstractEditor.onRead(buffer);
                            // 真正的编辑器读取起始时间
                            end = System.currentTimeMillis();
                            for (AbstractEditor.Listener<?> listener : abstractEditor.getListeners()) {
                                onReadAfterMethod.invoke(listener, abstractEditor);
                            }
                        } catch (Exception e) {
                            if (end == 0) {
                                // 编辑器未正常读取完毕
                                end = System.currentTimeMillis();
                            }
                            e.printStackTrace();
                        }

                        long time = end - start;
                        totalTime[0] += time;
                        if (state) {
                            result[0]++;
                            EditorWorker.this.publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器：%s 成功！耗时：%dms", abstractEditor.getClass().getSimpleName(), time)));
                            EditorWorker.this.publish(Map.entry(ProcessState.RESULT, true));
                        } else {
                            result[1]++;
                            EditorWorker.this.publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器：%s 失败！耗时：%dms", abstractEditor.getClass().getSimpleName(), time)));
                            EditorWorker.this.publish(Map.entry(ProcessState.RESULT, false));
                        }
                    });
                }
                totalTime[0x01] = System.currentTimeMillis() - totalTime[0x01];
                // 所有编辑器读取完毕
                EditorWorker.this.publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器结束，共%d个编辑器，成功%d个，失败%d个", result[0] + result[1], result[0], result[1])));
                EditorWorker.this.publish(Map.entry(ProcessState.MESSAGE, String.format("加载编辑器共计耗时：%dms，实际耗时：%dms", totalTime[0], totalTime[1])));
                return result[0] != 0;
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
