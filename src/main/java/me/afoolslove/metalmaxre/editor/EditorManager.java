package me.afoolslove.metalmaxre.editor;

import me.afoolslove.metalmaxre.GameHeader;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.ResourceManager;
import me.afoolslove.metalmaxre.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author AFoolLove
 */
public final class EditorManager {
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ReentrantLock LOCK = new ReentrantLock();

    /**
     * 储存所有编辑器的实例
     */
    private static final HashMap<Class<AbstractEditor<?>>, AbstractEditor<?>> EDITORS = new LinkedHashMap<>();

    /**
     * 储存所有编辑器的监听器
     */
    private static final HashMap<Class<AbstractEditor<?>>, List<AbstractEditor.Listener<?>>> EDITOR_LISTENERS = new HashMap<>();

    private EditorManager() {
    }

    public static HashMap<Class<AbstractEditor<?>>, AbstractEditor<?>> getEditors() {
        return EDITORS;
    }

    /**
     * 通过实例注册一个编辑器
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends AbstractEditor<T>> void register(@NotNull T editor) {
        EDITORS.put((Class<AbstractEditor<?>>) editor.getClass(), editor);
    }

    /**
     * 注册一个编辑器
     */
    @SuppressWarnings("unchecked")
    public static synchronized <T extends AbstractEditor<T>> void register(Class<T> editor) {
        if (EDITORS.get(editor) == null) {
            EDITORS.put((Class<AbstractEditor<?>>) editor, null);
        }
    }

    /**
     * 不考虑 {@code null} 的情况
     *
     * @return 获取相应的编辑器
     */
    public static <T extends AbstractEditor<T>> T getEditor(Class<T> editor) {
        return editor.cast(EDITORS.get(editor));
    }

    /**
     * 获取该编辑器的所有监听器，如果不存在或没有返回：{@link Collections#emptyList()}
     * <p>
     * * 请不要在返回的List中增、删监听器，没用
     *
     * @param <E> 编辑器
     * @return 目标编辑器的所有编辑器
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static <E extends AbstractEditor<E>> List<AbstractEditor.Listener<E>> getListeners(@NotNull Class<E> editor) {
        List<AbstractEditor.Listener<?>> listeners = EDITOR_LISTENERS.get(editor);
        if (listeners == null || listeners.isEmpty()) {
            return Collections.emptyList();
        }
        List<AbstractEditor.Listener<E>> list = new ArrayList<>();
        for (AbstractEditor.Listener<?> listener : listeners) {
            list.add((AbstractEditor.Listener<E>) listener);
        }
        return list;
    }

    /**
     * 为编辑器添加监听器
     *
     * @param editor   被监听的编辑器
     * @param listener 监听器
     * @param <E>      监听器
     */
    @SuppressWarnings("unchecked")
    public static synchronized <E extends AbstractEditor<E>> void addListener(@NotNull Class<E> editor, @NotNull AbstractEditor.Listener<E> listener) {
        List<AbstractEditor.Listener<?>> listeners = EDITOR_LISTENERS.get(editor);
        if (listeners == null) {
            listeners = new ArrayList<>();
            EDITOR_LISTENERS.put((Class<AbstractEditor<?>>) editor, listeners);
        }
        listeners.add(listener);
    }

    /**
     * 移除编辑器的某个监听器
     *
     * @param editor   编辑器
     * @param listener 监听器
     * @param <E>      编辑器
     */
    public static synchronized <E extends AbstractEditor<E>> boolean removeListener(@NotNull Class<E> editor, @NotNull AbstractEditor.Listener<E> listener) {
        List<AbstractEditor.Listener<?>> listeners = EDITOR_LISTENERS.get(editor);
        if (listeners == null) {
            return true;
        }
        return listeners.remove(listener);
    }

    /**
     * 移除编辑器的所有监听器
     *
     * @param editor 编辑器
     * @param <E>    编辑器
     */
    @SuppressWarnings("unchecked")
    @NotNull
    public static synchronized <E extends AbstractEditor<E>> List<AbstractEditor.Listener<E>> removeAllListener(@NotNull Class<E> editor) {
        List<AbstractEditor.Listener<?>> remove = EDITOR_LISTENERS.remove(editor);
        if (remove == null) {
            return Collections.emptyList();
        }

        List<AbstractEditor.Listener<E>> list = new ArrayList<>(remove.size());
        for (AbstractEditor.Listener<?> listener : remove) {
            list.add((AbstractEditor.Listener<E>) listener);
        }
        return list;
    }

    /**
     * 加载未加载的编辑器
     *
     * @return 现在加载的所有编辑器（不包含调用该方法之前的编辑器
     */
    public static <E extends AbstractEditor<E>> List<SingleMapEntry<Class<E>, E>> loadEditors() {
        return loadEditors(false);
    }

    /**
     * 加载还未加载的编辑器
     *
     * @param reload 所有编辑器是否全部重新加载
     * @return 现在加载的所有编辑器（根据reload确定是否包含调用该方法之前的编辑器
     */
    public static <E extends AbstractEditor<E>> List<SingleMapEntry<Class<E>, E>> loadEditors(boolean reload) {
        return loadEditors(reload, null);
    }

    /**
     * 加载还未加载的编辑器
     *
     * @param loadListener 监听器
     * @return 现在加载的所有编辑器（根据reload确定是否包含调用该方法之前的编辑器
     */
    public static <E extends AbstractEditor<E>> List<SingleMapEntry<Class<E>, E>> loadEditors(@Nullable LoadListener loadListener) {
        return loadEditors(false, loadListener);
    }

    /**
     * 加载还未加载的编辑器
     *
     * @param reload       所有编辑器是否全部重新加载
     * @param loadListener 监听器
     * @return 现在加载的所有编辑器（根据reload确定是否包含调用该方法之前的编辑器
     */
    public static <E extends AbstractEditor<E>> List<SingleMapEntry<Class<E>, E>> loadEditors(boolean reload, @Nullable LoadListener loadListener) {
        try {
            LOCK.lock();

            MetalMaxRe instance = MetalMaxRe.getInstance();
            byte[] bytes;
            if (instance.isIsInitTarget()) {
                // 直接获取流
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                InputStream resourceAsStream = ResourceManager.getAsStream("/MetalMax.nes");
                if (resourceAsStream == null) {
                    // 读取失败可还行
                    System.out.println("读取失败");
                    return Collections.emptyList();
                }
                resourceAsStream.transferTo(byteArrayOutputStream);
                bytes = byteArrayOutputStream.toByteArray();
            } else {
                // 外部路径
                if (instance.getTarget() == null) {
                    System.out.println("未指定ROM文件");
                    return Collections.emptyList();
                }
                Path path = Paths.get(instance.getTarget());
                try {
                    bytes = Files.readAllBytes(path);
                } catch (IOException e) {
                    e.printStackTrace();
                    // 读取失败
                    return Collections.emptyList();
                }
            }

            if (instance.getBuffer() != null) {
                instance.getBuffer().clear(); // 怎么释放呢？
            }

            // 读取头属性
            GameHeader header = new GameHeader(bytes);
            instance.setHeader(header);
            ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
            instance.setBuffer(buffer);
            // 写入到 ByteBuffer
            buffer.put(bytes);

            // 编辑器重新读取数据

            // index 0 = totalTime
            // index 1 = realTime
            long[] totalTime = {0, 0};
            // index 0 = successful
            // index 1 = failed
            int[] result = {0, 0};
            List<Set<Class<E>>> editorLists = new ArrayList<>();
            if (reload) {
                // 重新加载所有编辑器
                HashSet<Class<E>> hashSet = new HashSet<>();
                for (Class<AbstractEditor<?>> abstractEditorClass : EDITORS.keySet()) {
                    hashSet.add((Class<E>) abstractEditorClass);
                }
                editorLists.add(hashSet);
            } else {
                // 只加载未加载的编辑器
                editorLists.add(
                        EDITORS.entrySet().parallelStream()
                                .map(entry -> entry.getValue() == null ? (Class<E>) entry.getKey() : null)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toSet())
                );
            }

            long count = editorLists.get(0).parallelStream().count();
            if (count == 0) {
                // 没有编辑器可以加载
                return Collections.emptyList();
            }

            System.out.println("开始加载编辑器");

            do {
                Set<Class<E>> editors = new HashSet<>();
                Set<Class<E>> classes = editorLists.get(editorLists.size() - 1);

                // 过滤无序的编辑器
                for (Class<E> clazz : classes) {
                    ReadBefore annotation = clazz.getAnnotation(ReadBefore.class);
                    if (annotation == null || annotation.value().length == 0) {
                        continue;
                    }
                    for (Class<? extends AbstractEditor<?>> aClass : annotation.value()) {
                        editors.add((Class<E>) aClass);
                    }
                }

                if (editors.isEmpty()) {
                    break;
                }
                editorLists.add(editors);
            } while (true);

            // 将排序的编辑器去重
            // 越前面的编辑器在后面出现的话以后面为准
            for (Set<Class<E>> editorList : editorLists) {
                List<Set<Class<E>>> sets = new ArrayList<>(editorLists);
                sets.remove(editorList);
                Collections.reverse(sets);

                for (Set<Class<E>> set : sets) {
                    set.forEach(editorList::remove);
                }
            }

            // 倒序，优先加载前置
            Collections.reverse(editorLists);

            if (loadListener != null) {
                loadListener.onLoadBefore();
            }
            // 开始计时
            totalTime[0x01] = System.currentTimeMillis();

            var editors = editorLists.stream().map(editorList ->
                            editorList.parallelStream().map(editor -> {
                                try {
                                    E e = loadEditor(editor, reload).get();
                                    if (loadListener != null) {
                                        loadListener.onLoadSucceed(e);
                                    }
                                    return new SingleMapEntry<>(editor, e);
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                if (loadListener != null) {
                                    loadListener.onLoadFailed(editor);
                                }
                                return new SingleMapEntry<>(editor, (E) null);
                            }).toList())
                    .flatMap(Collection::stream)
                    .toList();

            totalTime[0x01] = System.currentTimeMillis() - totalTime[0x01];
            // 所有编辑器读取完毕
            if (loadListener != null) {
                loadListener.onLoadAfter();
            }

            // 计算读取成功的编辑器数量
            result[0] = editors.parallelStream().mapToInt(value -> value.getValue() == null ? 0 : 1).sum();
            // 计算读取失败的编辑器数量
            result[1] = editors.size() - result[0];
            System.out.println(String.format("加载编辑器结束，共%d个编辑器，成功%d个，失败%d个", editors.size(), result[0x00], result[0x01]));
            System.out.println(String.format("加载编辑器耗时：%dms", totalTime[0x01]));
            return editors;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            LOCK.unlock();
        }
        return null;
    }

    /**
     * 加载编辑器
     *
     * @param editor 被加载的编辑器
     * @return Future<E>
     */
    public static <E extends AbstractEditor<E>> Future<E> loadEditor(@NotNull Class<E> editor) {
        return loadEditor(editor, false);
    }

    /**
     * 加载编辑器
     *
     * @param editor 被加载的编辑器
     * @param reload 如果已经加载，是否重新加载该编辑器
     * @return Future<E>
     */
    public static <E extends AbstractEditor<E>> Future<E> loadEditor(@NotNull Class<E> editor, boolean reload) {
        return EXECUTOR.submit(() -> {
            E e = getEditor(editor);
            // 已经加载，且不需要重新加载
            if (e != null && !reload) {
                return e;
            }

            MetalMaxRe metalMaxRe = MetalMaxRe.getInstance();

            // 开始加载编辑器

            E instance = e; // 重载不需要重新创建实例
            if (instance == null) {
                // 创建编辑器实例，使用默认无构造参数
                instance = editor.getConstructor().newInstance();
            }

            // 获取该编辑器的所有监听器
            List<AbstractEditor.Listener<E>> listeners = getListeners(editor);

            // 执行编辑器开始读取之前的监听器
            for (AbstractEditor.Listener<E> listener : listeners) {
                if (listener != null) {
                    try {
                        listener.onReadBefore(instance);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

//            System.out.println(instance.getClass() + " ST");
            // 编辑器开始读取
            instance.onRead(metalMaxRe.getBuffer());

//            System.out.println(instance.getClass() + " OK");

            // 执行编辑器读取完毕之后的监听器
            for (AbstractEditor.Listener<E> listener : listeners) {
                if (listener != null) {
                    try {
                        listener.onReadAfter(instance);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            EDITORS.put((Class<AbstractEditor<?>>) editor, instance);
            return instance;
        });
    }


    /**
     * 应用所有编辑器，不包含未加载的编辑器
     */
    public static void applyEditors() {
        applyEditors(false);
    }

    /**
     * 应用所有编辑器
     *
     * @param load 如果有违背加载的编辑器，是否加载并应用
     */
    public static void applyEditors(boolean load) {
        applyEditors(load, null);
    }

    /**
     * 应用所有编辑器
     *
     * @param applyListener 监听器
     */
    public static void applyEditors(@Nullable ApplyListener applyListener) {
        applyEditors(false, applyListener);
    }

    /**
     * 应用所有编辑器
     *
     * @param applyListener 监听器
     * @param load          如果有违背加载的编辑器，是否加载并应用
     */
    public static <E extends AbstractEditor<E>> void applyEditors(boolean load, @Nullable ApplyListener applyListener) {
        // index 0 = totalTime
        // index 1 = realTime
        long[] totalTime = {0, 0};
        // index 0 = successful
        // index 1 = failed
        int[] result = {0, 0};

        if (!EditorManager.getEditors().isEmpty()) {
            System.out.println("开始写入编辑器的数据");

            List<Set<Class<E>>> editorLists = new ArrayList<>();
            HashSet<Class<E>> hashSet = new HashSet<>();
            for (Class<AbstractEditor<?>> editorClass : EDITORS.keySet()) {
                hashSet.add((Class<E>) editorClass);
            }
            editorLists.add(hashSet);

            do {
                Set<Class<E>> editors = new HashSet<>();
                Set<Class<E>> classes = editorLists.get(editorLists.size() - 1);


                // 过滤无序的编辑器
                for (Class<E> clazz : classes) {
                    WriteBefore annotation = clazz.getAnnotation(WriteBefore.class);
                    if (annotation == null || annotation.value().length == 0) {
                        continue;
                    }
                    for (Class<? extends AbstractEditor<?>> aClass : annotation.value()) {
                        editors.add((Class<E>) aClass);
                    }
                }

                if (editors.isEmpty()) {
                    break;
                }
                editorLists.add(editors);
            } while (true);

            // 将排序的编辑器去重
            // 越前面的编辑器在后面出现的话以后面为准
            for (Set<Class<E>> editorList : editorLists) {
                List<Set<Class<E>>> sets = new ArrayList<>(editorLists);
                sets.remove(editorList);
                Collections.reverse(sets);

                for (Set<Class<E>> set : sets) {
                    set.forEach(editorList::remove);
                }
            }

            // 倒序，优先写入前置
            Collections.reverse(editorLists);

            if (applyListener != null) {
                applyListener.onApplyBefore();
            }
            // 开始计时
            totalTime[0x01] = System.currentTimeMillis();
            var editors = editorLists.stream().map(editorList ->
                            editorList.parallelStream().map(editor -> {
                                try {
                                    E e = applyEditor(editor, load).get();
                                    if (applyListener != null) {
                                        applyListener.onApplySucceed(e);
                                    }
                                    return new SingleMapEntry<>(editor, e);
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                }
                                if (applyListener != null) {
                                    applyListener.onApplyFailed(editor);
                                }
                                return new SingleMapEntry<>(editor, (E) null);
                            }).toList())
                    .flatMap(Collection::stream)
                    .toList();
            totalTime[0x01] = System.currentTimeMillis() - totalTime[0x01];
            // 所有编辑器写入完毕
            if (applyListener != null) {
                applyListener.onApplyAfter();
            }

            // 计算读取成功的编辑器数量
            result[0] = editors.parallelStream().mapToInt(value -> value.getValue() == null ? 0 : 1).sum();
            // 计算读取失败的编辑器数量
            result[1] = editors.size() - result[0];
            System.out.println(String.format("应用编辑器数据结束，共%d个编辑器，成功%d个，失败%d个", editors.size(), result[0], result[1]));
            System.out.println(String.format("应用编辑器数据耗时：%dms", totalTime[1]));
        } else {
            System.out.println("没有可用的编辑器！");
        }
    }

    /**
     * 应用编辑器
     *
     * @param editor 应用的编辑器
     * @param load   如果编辑器未加载，是否加载该编辑器
     * @param <E>    编辑器
     * @return Future<E>
     */
    public static <E extends AbstractEditor<E>> Future<E> applyEditor(@NotNull Class<E> editor, boolean load) {
        return EXECUTOR.submit(() -> {
            E e = getEditor(editor);
            if (e == null && load) {
                // 编辑器未加载，加载该编辑器
                e = loadEditor(editor, false).get();
            } else if (e == null) {
                // 编辑器未加载且不加载该编辑器，不进行任何操作
                return null;
            }

            if (e.isNotEnabled()) {
                // 未启用
                return e;
            }

            MetalMaxRe metalMaxRe = MetalMaxRe.getInstance();

            // 开始应用编辑器

            // 获取该编辑器的所有监听器
            List<AbstractEditor.Listener<E>> listeners = getListeners(editor);

            // 执行编辑器开始写入之前的监听器
            for (AbstractEditor.Listener<E> listener : listeners) {
                if (listener != null) {
                    try {
                        listener.onWriteBefore(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }

            // 编辑器开始写入
            e.onWrite(metalMaxRe.getBuffer());

            // 执行编辑器写入完毕之后的监听器
            for (AbstractEditor.Listener<E> listener : listeners) {
                if (listener != null) {
                    try {
                        listener.onWriteAfter(e);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
            }
            return e;
        });
    }

    public interface LoadListener {
        /**
         * 编辑器准备开始加载
         */
        default void onLoadBefore() {
        }

        /**
         * 编辑器加载完毕，无论是否加载成功
         */
        default void onLoadAfter() {
        }

        /**
         * 编辑器加载失败
         *
         * @param editor 加载失败的编辑器类
         */
        default <E extends AbstractEditor<E>> void onLoadFailed(@NotNull Class<E> editor) {
        }

        /**
         * 编辑器加载成功
         *
         * @param editor 加载成功的编辑器
         */
        default <E extends AbstractEditor<E>> void onLoadSucceed(@NotNull E editor) {
        }
    }

    public interface ApplyListener {
        /**
         * 准备应用编辑器数据
         */
        default void onApplyBefore() {
        }

        /**
         * 应用编辑器完毕，无论是否应用成功
         */
        default void onApplyAfter() {
        }

        /**
         * 编辑器应用失败
         *
         * @param editor 应用失败的编辑器
         */
        default <E extends AbstractEditor<E>> void onApplyFailed(@NotNull Class<E> editor) {
        }

        /**
         * 编辑器应用成功
         *
         * @param editor 应用成功的编辑器
         */
        default <E extends AbstractEditor<E>> void onApplySucceed(@NotNull E editor) {
        }
    }
}
