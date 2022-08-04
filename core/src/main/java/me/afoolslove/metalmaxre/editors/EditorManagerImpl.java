package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBuffer;
import me.afoolslove.metalmaxre.editors.computer.ComputerEditorImpl;
import me.afoolslove.metalmaxre.editors.computer.IComputerEditor;
import me.afoolslove.metalmaxre.editors.computer.vendor.IVendorEditor;
import me.afoolslove.metalmaxre.editors.computer.vendor.VendorEditorImpl;
import me.afoolslove.metalmaxre.editors.data.DataValueEditorImpl;
import me.afoolslove.metalmaxre.editors.data.IDataValueEditor;
import me.afoolslove.metalmaxre.editors.items.IItemEditor;
import me.afoolslove.metalmaxre.editors.items.ItemEditorImpl;
import me.afoolslove.metalmaxre.editors.map.*;
import me.afoolslove.metalmaxre.editors.map.events.EventTilesEditorImpl;
import me.afoolslove.metalmaxre.editors.map.events.IEventTilesEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.ITileSetEditor;
import me.afoolslove.metalmaxre.editors.map.tileset.TileSetEditorImpl;
import me.afoolslove.metalmaxre.editors.map.world.IWorldMapEditor;
import me.afoolslove.metalmaxre.editors.map.world.WorldMapEditorImpl;
import me.afoolslove.metalmaxre.editors.palette.IPaletteEditor;
import me.afoolslove.metalmaxre.editors.palette.PaletteEditorImpl;
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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        register(IVendorEditor.class, VendorEditorImpl::new);
        register(IDataValueEditor.class, DataValueEditorImpl::new);
        register(IItemEditor.class, ItemEditorImpl::new);
        register(IDogSystemEditor.class, DogSystemEditorImpl::new);
        register(IPaletteEditor.class, PaletteEditorImpl.class);
        register(IPlayerEditor.class, PlayerEditorImpl::new);
        register(IPlayerExpEditor.class, PlayerExpEditorImpl::new);
        register(ISpriteEditor.class, SpriteEditorImpl::new);
        register(ITankEditor.class, TankEditorImpl::new);
        register(ITreasureEditor.class, TreasureEditorImpl::new);
        register(IMapEditor.class, MapEditorImpl::new);
        register(IMapPropertiesEditor.class, MapPropertiesEditorImpl.class);
        register(IEventTilesEditor.class, EventTilesEditorImpl::new);
        register(IWorldMapEditor.class, WorldMapEditorImpl::new);
        register(IMapEntranceEditor.class, MapEntranceEditorImpl.class);
        register(ITileSetEditor.class, TileSetEditorImpl::new);
    }

    @Override
    public synchronized <E extends IRomEditor> void register(@NotNull Class<E> editorType, @NotNull Function<MetalMaxRe, E> builder) {
        if (editorBuilders.get(editorType) != null) {
            return;
        }
        // 创建并储存编辑器实例
        var editor = builder.apply(getMetalMaxRe());
        if (editor == null) {
            throw new IllegalArgumentException(String.format("editor(%s) is null.", editorType.getSimpleName()));
        }
        editorBuilders.put(editorType, builder);

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

    @SuppressWarnings("unchecked")
    public <E extends IRomEditor, CE extends E> void register(@NotNull Class<E> editorType, @NotNull Class<CE> editor) {
        register(editorType, metalMaxRe -> {
            Class<CE> editorClazz = editor;
            final var versionId = metalMaxRe.getBuffer().getVersion().getId();
            if (editorClazz.getAnnotation(Editor.TargetVersions.class) != null) {
                for (Class<?> declaredClass : editorClazz.getDeclaredClasses()) {
                    if (!IRomEditor.class.isAssignableFrom(declaredClass)) {
                        // 排除未实现IRomEditor接口的类
                        continue;
                    }
                    var targetVersion = declaredClass.getAnnotation(Editor.TargetVersion.class);
                    if (targetVersion == null || targetVersion.value().length == 0) {
                        // 该类没有目标版本的标识
                        continue;
                    }

                    for (String id : targetVersion.value()) {
                        if (Objects.equals(id, versionId)) {
                            // 找到目标编辑器对应的版本
                            editorClazz = (Class<CE>) declaredClass;
                            break;
                        }
                    }
                }
            } else if (editorClazz.getAnnotation(Editor.TargetVersion.class) != null) {
                var targetVersion = editorClazz.getAnnotation(Editor.TargetVersion.class);
                if (targetVersion.value().length > 0) {
                    boolean has = false;
                    for (String id : targetVersion.value()) {
                        if (Objects.equals(id, versionId)) {
                            has = true;
                            break;
                        }
                    }
                    if (!has) {
                        // 目标编辑器不是对应的版本
                        throw new RuntimeException(String.format("editor(%s) target:%s current:%s.", editorType.getSimpleName(), versionId, Arrays.toString(targetVersion.value())));
                    }
                }
            }

            // 创建该对象
            Constructor<CE> constructor;
            try {
                constructor = editorClazz.getConstructor(MetalMaxRe.class);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(String.format("editor(%s) no constructor \"Constructor(me.afoolslove.metalmaxre.MetalMaxRe)\".", editorClazz.getSimpleName()), e);
            }
            try {
                return constructor.newInstance(metalMaxRe);
            } catch (InvocationTargetException | InstantiationException |
                     IllegalAccessException e) {
                throw new RuntimeException(String.format("editor(%s) failed to create instance.", editorClazz.getSimpleName()), e);
            }
        });
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

            // 无序的编辑器，加载与否都不会影响到其它编辑器
            // 在接下来分类中会移除有影响的编辑器，只留下无序的编辑器
            var freeEditors = new HashSet<>(editors.getFirst());
            do {
                Set<Class<? extends IRomEditor>> nextList = new HashSet<>();
                for (SingleMapEntry<Class<? extends IRomEditor>, Method> entry : editors.getLast()) {
                    if (nextList.contains(entry.getKey())) {
                        // 已经是前置，跳过
                        continue;
                    }

                    // 将参数中的编辑器作为前置编辑器，带有@Editor.QuoteOnly注解只是引用，不算作前置
                    var parameterTypes = entry.getValue().getParameterTypes();
                    if (parameterTypes.length > 0) {
                        var parameterAnnotations = entry.getValue().getParameterAnnotations();
                        pars:
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            if (!IRomEditor.class.isAssignableFrom(parameterType)) {
                                // 参数仅针对编辑器
                                continue;
                            }
                            Annotation[] annotations = parameterAnnotations[i];

                            // 如果带有@Editor.QuoteOnly注解，那它只是引用，不算作前置
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof Editor.QuoteOnly) {
                                    // 只引用，不算作前置
                                    continue pars;
                                }
                            }

                            // 将前置放进下一个Set中
                            nextList.add((Class<? extends IRomEditor>) parameterType);

                            // 有前置编辑器，不属于无序编辑器
                            freeEditors.remove(entry);
                        }
                    }
                }
                if (nextList.isEmpty()) {
                    // 没有找到有前置的编辑器，跳出循环
                    break;
                }
                var nextEditors = new HashSet<SingleMapEntry<Class<? extends IRomEditor>, Method>>();
                editors.getLast().removeIf(next -> {
                    if (nextList.contains(next.getKey())) {
                        nextEditors.add(next);
                        freeEditors.remove(next);
                        return true;
                    }
                    return false;
                });
                editors.add(nextEditors);
            } while (true);

            // 移除无序的编辑器，无序编辑器与其它编辑器分开进行
            editors.getFirst().removeAll(freeEditors);
            // 倒序，优先加载前置
            Collections.reverse(editors);

            CountDownLatch latch = new CountDownLatch(editors.stream().mapToInt(Set::size).sum());
            // 加载无序编辑器
            freeEditors.parallelStream().forEach(editor -> {
                try {
                    loadEditor(editor.getKey()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            // 加载有序编辑器
            editors.forEach(editorList ->
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

            // 无序的编辑器，应用与否都不会影响到其它编辑器
            // 在接下来分类中会移除有影响的编辑器，只留下无序的编辑器
            var freeEditors = new HashSet<>(editors.getFirst());
            do {
                Set<Class<? extends IRomEditor>> nextList = new HashSet<>();
                for (SingleMapEntry<Class<? extends IRomEditor>, Method> entry : editors.getLast()) {
                    if (nextList.contains(entry.getKey())) {
                        // 已经是前置，跳过
                        continue;
                    }

                    // 将参数中的编辑器作为前置编辑器，带有@Editor.QuoteOnly注解只是引用，不算作前置
                    var parameterTypes = entry.getValue().getParameterTypes();
                    if (parameterTypes.length > 0) {
                        var parameterAnnotations = entry.getValue().getParameterAnnotations();
                        pars:
                        for (int i = 0; i < parameterTypes.length; i++) {
                            Class<?> parameterType = parameterTypes[i];
                            if (!IRomEditor.class.isAssignableFrom(parameterType)) {
                                // 参数仅针对编辑器
                                continue;
                            }
                            Annotation[] annotations = parameterAnnotations[i];

                            // 如果带有@Editor.QuoteOnly注解，那它只是引用，不算作前置
                            for (Annotation annotation : annotations) {
                                if (annotation instanceof Editor.QuoteOnly) {
                                    // 只引用，不算作前置
                                    continue pars;
                                }
                            }

                            // 将前置放进下一个Set中
                            nextList.add((Class<? extends IRomEditor>) parameterType);

                            // 有前置编辑器，不属于无序编辑器
                            freeEditors.remove(entry);
                        }
                    }
                }
                if (nextList.isEmpty()) {
                    // 没有找到有前置的编辑器，跳出循环
                    break;
                }
                var nextEditors = new HashSet<SingleMapEntry<Class<? extends IRomEditor>, Method>>();
                editors.getLast().removeIf(next -> {
                    if (nextList.contains(next.getKey())) {
                        nextEditors.add(next);
                        freeEditors.remove(next);
                        return true;
                    }
                    return false;
                });
                editors.add(nextEditors);
            } while (true);

            // 移除无序的编辑器，无序编辑器与其它编辑器分开进行
            editors.getFirst().removeAll(freeEditors);
            // 倒序，优先应用前置
            Collections.reverse(editors);

            CountDownLatch latch = new CountDownLatch(editors.stream().mapToInt(Set::size).sum());
            // 应用无序编辑器
            freeEditors.parallelStream().forEach(editor -> {
                try {
                    applyEditor(editor.getKey()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
            // 应用有序编辑器
            editors.forEach(editorList ->
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

    @SuppressWarnings("unchecked")
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

    @SuppressWarnings("unchecked")
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
