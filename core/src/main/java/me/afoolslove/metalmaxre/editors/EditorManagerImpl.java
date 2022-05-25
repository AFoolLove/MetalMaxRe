package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.editors.computer.ComputerEditorImpl;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.items.ItemEditorImpl;
import me.afoolslove.metalmaxre.editors.map.DogSystemEditorImpl;
import me.afoolslove.metalmaxre.editors.map.IDogSystemEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerEditor;
import me.afoolslove.metalmaxre.editors.player.IPlayerExpEditor;
import me.afoolslove.metalmaxre.editors.player.PlayerEditorImpl;
import me.afoolslove.metalmaxre.editors.player.PlayerExpEditorImpl;
import me.afoolslove.metalmaxre.editors.sprite.ISpriteEditor;
import me.afoolslove.metalmaxre.editors.sprite.SpriteEditorImpl;
import me.afoolslove.metalmaxre.editors.tank.ITankEditor;
import me.afoolslove.metalmaxre.editors.tank.TankEditorImpl;
import me.afoolslove.metalmaxre.editors.treasure.ITreasureEditor;
import me.afoolslove.metalmaxre.editors.treasure.TreasureEditorImpl;
import me.afoolslove.metalmaxre.event.editors.editor.EditorApplyEvent;
import me.afoolslove.metalmaxre.event.editors.editor.EditorLoadEvent;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;

/**
 * @author AFoolLove
 */
public class EditorManagerImpl implements IEditorManager {
    private final MetalMaxRe metalMaxRe;

    private final Map<Class<? extends IRomEditor>, IRomEditor> editors = new LinkedHashMap<>();

    private final Map<Class<? extends IRomEditor>, Function<MetalMaxRe, ? extends IRomEditor>> editorBuilders = new ConcurrentHashMap<>();

    private final Map<Class<? extends IRomEditor>, Method> loadMethods = new ConcurrentHashMap<>();
    private final Map<Class<? extends IRomEditor>, Method> applyMethods = new ConcurrentHashMap<>();

    /**
     * 加载或应用编辑器数据
     */
    private final ExecutorService EDITOR_EXECUTOR = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    /**
     * 调用加载或应用编辑器数据，同时只能进行一个，所以单线程
     */
    private final ExecutorService LOAD_OR_APPLY_EXECUTOR = Executors.newSingleThreadExecutor();

    public EditorManagerImpl(@NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;
    }

    @Override
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }

    public void registerDefaultEditors() {
        register(IComputerEditor.class, ComputerEditorImpl::new);
        register(IDogSystemEditor.class, DogSystemEditorImpl::new);
        register(ITreasureEditor.class, TreasureEditorImpl::new);
        register(ISpriteEditor.class, SpriteEditorImpl::new);
        register(IItemEditor.class, ItemEditorImpl::new);
        register(IPlayerEditor.class, PlayerEditorImpl::new);
        register(IPlayerExpEditor.class, PlayerExpEditorImpl::new);
        register(ITankEditor.class, TankEditorImpl::new);
    }

    @Override
    public synchronized <E extends IRomEditor> void register(@NotNull Class<E> editorType, @NotNull Function<MetalMaxRe, E> builder) {
        if (editorBuilders.get(editorType) != null) {
            return;
        }
        editorBuilders.put(editorType, builder);

        // 创建并储存编辑器实例
        var editor = builder.apply(getMetalMaxRe());
        editors.put(editorType, editor);


        // 寻找加载和应用方法
        Method loadMethod = null, applyMethod = null;
        for (Method method : editor.getClass().getDeclaredMethods()) {
            if (loadMethod != null && applyMethod != null) {
                // 加载和应用方法都已经找到
                break;
            }
            if (loadMethod == null) {
                // 加载方法
                Annotation annotation = method.getAnnotation(Editor.Load.class);
                if (annotation != null) {
                    loadMethod = method;
                    continue;
                }
            }
            if (applyMethod == null) {
                // 应用方法
                Annotation annotation = method.getAnnotation(Editor.Apply.class);
                if (annotation != null) {
                    applyMethod = method;
                }
            }
        }
        if (loadMethod == null || applyMethod == null) {
            //TODO 没有找到加载或应用数据的方法
            return;
        }

        loadMethods.put(editorType, loadMethod);
        applyMethods.put(editorType, applyMethod);
    }

    @Override
    public synchronized <E extends IRomEditor> IRomEditor unregister(@NotNull Class<E> editorType) {
        editorBuilders.remove(editorType);
        loadMethods.remove(editorType);
        applyMethods.remove(editorType);
        return editors.remove(editorType);
    }

    @Override
    public synchronized Future<?> loadEditors() {
        return LOAD_OR_APPLY_EXECUTOR.submit(() -> {
            var editors = new LinkedList<Set<SingleMapEntry<Class<? extends IRomEditor>, Method>>>();
            editors.addFirst(new HashSet<>());

            for (var entry : loadMethods.entrySet()) {
                editors.getFirst().add(SingleMapEntry.create(entry));
            }

            do {
                var nextEditors = new HashSet<SingleMapEntry<Class<? extends IRomEditor>, Method>>();
                // 过滤前置编辑器
                var tmpList = new LinkedList<>(editors.getLast());
                while (!tmpList.isEmpty()) {
                    // 每次移除第一个，并将被移除所需要的编辑器添加到当前set的前置中
                    var first = tmpList.removeFirst();
                    if (first == null || tmpList.isEmpty()) {
                        break;
                    }

                    // 将参数中的编辑器作为前置编辑器
                    for (Class<?> parameterType : first.getValue().getParameterTypes()) {
                        var iterator = tmpList.iterator();
                        while (iterator.hasNext()) {
                            var next = iterator.next();
                            if (next.getKey() == parameterType) {
                                iterator.remove();
                                nextEditors.add(next);
                                break;
                            }
                        }
                    }
                }

                if (nextEditors.isEmpty()) {
                    break;
                }
                // 移除前置编辑器
                editors.getLast().removeAll(nextEditors);
                // 添加前置编辑器
                editors.add(nextEditors);
            } while (true);

            // 倒序，优先加载前置
            Collections.reverse(editors);

            CountDownLatch latch = new CountDownLatch(editors.stream().mapToInt(Set::size).sum());
            // 加载无序编辑器
            editors.removeFirst().parallelStream().forEach(editor -> {
                try {
                    loadEditor(editor.getKey()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            // 加载有序编辑器
            editors.parallelStream().forEach(editorList ->
                    editorList.parallelStream().forEach(editor -> {
                                try {
                                    loadEditor(editor.getKey()).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                } finally {
                                    latch.countDown();
                                }
                            }
                    )
            );

            try {
                // 等待加载完毕
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public synchronized Future<?> applyEditors() {
        return LOAD_OR_APPLY_EXECUTOR.submit(() -> {
            var editors = new LinkedList<Set<SingleMapEntry<Class<? extends IRomEditor>, Method>>>();
            editors.addFirst(new HashSet<>());

            for (var entry : applyMethods.entrySet()) {
                editors.getFirst().add(SingleMapEntry.create(entry));
            }

            do {
                var nextEditors = new HashSet<SingleMapEntry<Class<? extends IRomEditor>, Method>>();
                // 过滤前置编辑器
                var tmpList = new LinkedList<>(editors.getLast());
                while (!tmpList.isEmpty()) {
                    // 每次移除第一个，并将被移除所需要的编辑器添加到当前set的前置中
                    var first = tmpList.removeFirst();
                    if (first == null || tmpList.isEmpty()) {
                        break;
                    }

                    // 将参数中的编辑器作为前置编辑器
                    for (Class<?> parameterType : first.getValue().getParameterTypes()) {
                        var quoteOnly = parameterType.getAnnotation(Editor.QuoteOnly.class);
                        if (quoteOnly != null) {
                            // 作为引用，不作为前置
                            continue;
                        }
                        var iterator = tmpList.iterator();
                        while (iterator.hasNext()) {
                            var next = iterator.next();
                            if (next.getKey() == parameterType) {
                                iterator.remove();
                                nextEditors.add(next);
                                break;
                            }
                        }
                    }
                }

                if (nextEditors.isEmpty()) {
                    break;
                }
                // 移除前置编辑器
                editors.getLast().removeAll(nextEditors);
                // 添加前置编辑器
                editors.add(nextEditors);
            } while (true);

            // 倒序，优先应用前置
            Collections.reverse(editors);

            CountDownLatch latch = new CountDownLatch(editors.stream().mapToInt(Set::size).sum());
            // 应用无序编辑器
            editors.removeFirst().parallelStream().forEach(editor -> {
                try {
                    applyEditor(editor.getKey()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            // 应用有序编辑器
            editors.parallelStream().forEach(editorList ->
                    editorList.parallelStream().forEach(editor -> {
                                try {
                                    applyEditor(editor.getKey()).get();
                                } catch (InterruptedException | ExecutionException e) {
                                    e.printStackTrace();
                                } finally {
                                    latch.countDown();
                                }
                            }
                    )
            );

            try {
                // 等待应用完毕
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public Future<?> reloadEditors() {
        return loadEditors();
    }

    @NotNull
    @Override
    public Future<IRomEditor> loadEditor(@NotNull Class<? extends IRomEditor> type) {
        return loadEditor(type, false);
    }

    @NotNull
    @Override
    public Future<IRomEditor> applyEditor(@NotNull Class<? extends IRomEditor> type) {
        return EDITOR_EXECUTOR.submit(() -> {
            var editor = getEditor(type);
            if (editor != null) {
                Method applyMethod = applyMethods.get(type);

                if (applyMethod == null) {
                    // 获取方法失败。。。？
                    return null;
                }

                Object[] pars = new Object[applyMethod.getParameterCount()];
                var parameters = applyMethod.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    var parameter = parameters[i];
                    if (RomBuffer.class.isAssignableFrom(parameter.getType())) {
                        pars[i] = editor.getBuffer();
                        continue;
                    }
                    if (!IRomEditor.class.isAssignableFrom(parameter.getType())) {
                        // 错误的参数，参数只能是编辑器
                        continue;
                    }
                    pars[i] = getEditor((Class<? extends IRomEditor>) parameter.getType());
                }

                try {
                    // 准备应用数据
                    getMetalMaxRe().getEventHandler().callEvent(new EditorApplyEvent.Pre(getMetalMaxRe(), editor));

                    // 开始应用数据并计时
                    final long start = System.currentTimeMillis();
                    applyMethod.invoke(editor, pars);
                    final long end = System.currentTimeMillis() - start;

                    // 应用数据完成
                    getMetalMaxRe().getEventHandler().callEvent(new EditorApplyEvent.Post(getMetalMaxRe(), editor));
                } catch (Exception exception) {
                    exception.printStackTrace();

                    // 应用数据失败
                    getMetalMaxRe().getEventHandler().callEvent(new EditorApplyEvent.Post(getMetalMaxRe(), editor, exception));
                }
            }
            return editor;
        });
    }

    @Override
    public Future<IRomEditor> reloadEditor(@NotNull Class<? extends IRomEditor> type) {
        return loadEditor(type, true);
    }

    @NotNull
    private Future<IRomEditor> loadEditor(@NotNull Class<? extends IRomEditor> type, boolean reload) {
        return EDITOR_EXECUTOR.submit(() -> {
            var editor = getEditor(type);
            if (editor == null) {
                // 没有这个类型的编辑器
                return null;
            }

            Method loadMethod = loadMethods.get(type);

            if (loadMethod == null) {
                // 获取方法失败。。。？
                return null;
            }

            Object[] pars = new Object[loadMethod.getParameterCount()];
            var parameters = loadMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                var parameter = parameters[i];
                if (RomBuffer.class.isAssignableFrom(parameter.getType())) {
                    pars[i] = editor.getBuffer();
                    continue;
                }
                if (!IRomEditor.class.isAssignableFrom(parameter.getType())) {
                    // 错误的参数，参数只能是编辑器
                    continue;
                }
                pars[i] = getEditor((Class<? extends IRomEditor>) parameter.getType());
            }

            try {
                // 准备加载
                metalMaxRe.getEventHandler().callEvent(new EditorLoadEvent.Pre(metalMaxRe, editor, reload));

                // 开始加载并计时
                final long start = System.currentTimeMillis();
                loadMethod.invoke(editor, pars);
                final long end = System.currentTimeMillis() - start;

                // 加载完毕
                metalMaxRe.getEventHandler().callEvent(new EditorLoadEvent.Post(metalMaxRe, editor, reload));
            } catch (Exception exception) {
                exception.printStackTrace();

                // 加载失败
                metalMaxRe.getEventHandler().callEvent(new EditorLoadEvent.Post(metalMaxRe, editor, exception, reload));
            }
            return editor;
        });
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor) {
        return (E) editors.get(editor);
    }

}
