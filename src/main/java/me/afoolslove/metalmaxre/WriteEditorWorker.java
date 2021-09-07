package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.editor.AbstractEditor;
import me.afoolslove.metalmaxre.editor.EditorManager;
import me.afoolslove.metalmaxre.editor.WriteBefore;

import javax.swing.*;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 编辑器异步写入
 * <p>
 * K：是否正常写入完毕，为true时，可能部分写入失败，但并不严重，为false时，无法正常写入
 * V：中间数据，某个编辑器是否写入成功
 */
public abstract class WriteEditorWorker extends SwingWorker<Boolean, Map.Entry<EditorProcess, Object>> {
    @Override
    protected abstract void process(List<Map.Entry<EditorProcess, Object>> chunks);

    @Override
    protected Boolean doInBackground() throws Exception {
        MetalMaxRe instance = MetalMaxRe.getInstance();

        // index 0 = totalTime
        // index 1 = realTime
        long[] totalTime = {0, 0};
        // index 0 = successful
        // index 1 = failed
        int[] result = {0, 0};

        if (!EditorManager.getEditors().isEmpty()) {
            publish(Map.entry(EditorProcess.MESSAGE, "开始写入编辑器的数据"));

            List<Set<Class<? extends AbstractEditor<?>>>> editorLists = new ArrayList<>();
            editorLists.add(new HashSet<>(EditorManager.getEditors().keySet()));

            do {
                Set<Class<? extends AbstractEditor<?>>> editors = new HashSet<>();
                Set<Class<? extends AbstractEditor<?>>> classes = editorLists.get(editorLists.size() - 1);

                // 过滤无序的编辑器
                for (var clazz : classes) {
                    WriteBefore annotation = clazz.getAnnotation(WriteBefore.class);
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

            // 倒序，优先写入前置
            Collections.reverse(editorLists);

            final Method onWriteBeforeMethod = AbstractEditor.Listener.class.getMethod("onWriteBefore", AbstractEditor.class);
            final Method onWriteAfterMethod = AbstractEditor.Listener.class.getMethod("onWriteAfter", AbstractEditor.class);
            // 开始计时
            totalTime[0x01] = System.currentTimeMillis();
            for (Set<Class<? extends AbstractEditor<?>>> editorList : editorLists) {
                // 将class转换为编辑器实例
                List<AbstractEditor<?>> collect = editorList.parallelStream()
                        .map(editorClazz -> EditorManager.getEditors().get(editorClazz))
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                collect.parallelStream().forEach(abstractEditor -> {
                    long start = 0, end = 0;
                    boolean state = false;
                    try {
                        // 防止监听器抛出异常后无法得到编辑器写入起始时间
                        start = System.currentTimeMillis();
                        for (AbstractEditor.Listener<?> listener : abstractEditor.getListeners()) {
                            onWriteBeforeMethod.invoke(listener, abstractEditor);
                        }
                        // 真正的编辑器写入起始时间
                        start = System.currentTimeMillis();
                        state = abstractEditor.onWrite(instance.getBuffer());
                        // 真正的编辑器写入结束时间
                        end = System.currentTimeMillis();
                        for (AbstractEditor.Listener<?> listener : abstractEditor.getListeners()) {
                            onWriteAfterMethod.invoke(listener, abstractEditor);
                        }
                    } catch (Exception e) {
                        if (end == 0) {
                            // 编辑器未正常写入完毕
                            end = System.currentTimeMillis();
                        }
                        e.printStackTrace();
                    }

                    long time = end - start;
                    totalTime[0] += time;
                    if (state) {
                        result[0]++;
                        publish(Map.entry(EditorProcess.MESSAGE, String.format("写入编辑器数据：%s 成功！耗时：%dms", abstractEditor.getClass().getSimpleName(), time)));
                        publish(Map.entry(EditorProcess.RESULT, true));
                    } else {
                        result[1]++;
                        publish(Map.entry(EditorProcess.MESSAGE, String.format("写入编辑器数据：%s 失败！耗时：%dms", abstractEditor.getClass().getSimpleName(), time)));
                        publish(Map.entry(EditorProcess.RESULT, false));
                    }
                });
            }
            totalTime[0x01] = System.currentTimeMillis() - totalTime[0x01];
            // 所有编辑器写入完毕
            publish(Map.entry(EditorProcess.MESSAGE, String.format("写入编辑器数据结束，共%d个编辑器，成功%d个，失败%d个", result[0] + result[1], result[0], result[1])));
            publish(Map.entry(EditorProcess.MESSAGE, String.format("写入编辑器数据共计耗时：%dms，实际耗时：%dms", totalTime[0], totalTime[1])));
            return result[0] != 0;
        } else {
            System.out.println("没有可用的编辑器！");
        }
        return false;
    }
}
