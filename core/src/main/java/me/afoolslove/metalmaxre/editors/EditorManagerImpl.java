package me.afoolslove.metalmaxre.editors;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.IEditorListener;
import me.afoolslove.metalmaxre.editors.IEditorManager;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * @author AFoolLove
 */
public class EditorManagerImpl implements IEditorManager {
    private final MetalMaxRe metalMaxRe;

    private final Map<Class<? extends IRomEditor>, IRomEditor> editors = new LinkedHashMap<>();

    private final Map<Class<? extends IRomEditor>, Function<MetalMaxRe, ? extends IRomEditor>> editorBuilders = new HashMap<>();

    private final Map<Class<? extends IRomEditor>, String> loadMethodNames = new HashMap<>();
    private final Map<Class<? extends IRomEditor>, String> applyMethodNames = new HashMap<>();

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

        loadMethodNames.put(editorType, loadMethod.getName());
        applyMethodNames.put(editorType, applyMethod.getName());
    }

    @Override
    public synchronized <E extends IRomEditor> IRomEditor unregister(@NotNull Class<E> editorType) {
        editorBuilders.remove(editorType);
        loadMethodNames.remove(editorType);
        applyMethodNames.remove(editorType);
        return editors.remove(editorType);
    }

    @Override
    public <R> R loadEditors() {
        try {
            // 多线程，强行单线程
            for (var entry : editors.keySet()) {
                loadEditor(entry).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R applyEditors() {
        try {
            // 多线程，强行单线程
            for (var entry : editors.keySet()) {
                applyEditor(entry).get();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <R> R reloadEditors() {
        return null;
    }

    @NotNull
    @Override
    public Future<IRomEditor> loadEditor(@NotNull Class<? extends IRomEditor> type) {
        return EDITOR_EXECUTOR.submit(() -> {
            var editor = getEditor(type);
            if (editor == null) {
                // 没有这个类型的编辑器
                return null;
            }

            var loadMethodName = loadMethodNames.get(type);

            Method loadMethod = null;

            for (Method declaredMethod : editor.getClass().getDeclaredMethods()) {
                if (Objects.equals(declaredMethod.getName(), loadMethodName)) {
                    Annotation annotation = declaredMethod.getAnnotation(Editor.Load.class);
                    if (annotation != null) {
                        loadMethod = declaredMethod;
                        break;
                    }
                }
            }
            if (loadMethod == null) {
                // 获取方法失败。。。？
                return null;
            }

            Object[] pars = new Object[loadMethod.getParameterCount()];
            var parameters = loadMethod.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                if (!IRomEditor.class.isAssignableFrom(parameters[i].getType())) {
                    // 错误的参数，参数只能是编辑器
                    continue;
                }
                pars[i] = getEditor((Class<? extends IRomEditor>) parameters[i].getType());
            }

            try {
                // 准备加载
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPreLoad(editor);
                }

                // 开始加载并计时
                final long start = System.currentTimeMillis();
                loadMethod.invoke(editor, pars);
                final long end = System.currentTimeMillis() - start;

                // 加载完毕
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostLoad(editor, end);
                }
            } catch (Exception ex) {
                ex.printStackTrace();

                // 加载失败
                for (IEditorListener listener : editor.getListeners()) {
                    listener.onPostLoad(editor, -1);
                }
            }
            return editor;
        });
    }

    @NotNull
    @Override
    public Future<IRomEditor> applyEditor(@NotNull Class<? extends IRomEditor> type) {
        return EDITOR_EXECUTOR.submit(() -> {
            var editor = getEditor(type);
            if (editor != null) {
                var applyMethodName = applyMethodNames.get(type);

                Method applyMethod = null;

                for (Method declaredMethod : editor.getClass().getDeclaredMethods()) {
                    if (Objects.equals(declaredMethod.getName(), applyMethodName)) {
                        Annotation annotation = declaredMethod.getAnnotation(Editor.Load.class);
                        if (annotation != null) {
                            applyMethod = declaredMethod;
                            break;
                        }
                    }
                }
                if (applyMethod == null) {
                    // 获取方法失败。。。？
                    return null;
                }

                Object[] pars = new Object[applyMethod.getParameterCount()];
                var parameters = applyMethod.getParameters();
                for (int i = 0; i < parameters.length; i++) {
                    var parameter = parameters[i];
                    if (!IRomEditor.class.isAssignableFrom(parameter.getType())) {
                        // 错误的参数，参数只能是编辑器
                        continue;
                    }
//                var quoteOnly = parameter.getAnnotation(Editor.QuoteOnly.class);
//                if (quoteOnly == null) {
//
//                }
                    pars[i] = getEditor((Class<? extends IRomEditor>) parameter.getType());
                }

                try {
                    // 准备应用数据
                    for (IEditorListener listener : editor.getListeners()) {
                        listener.onPreApply(editor);
                    }
                    // 开始应用数据并计时
                    final long start = System.currentTimeMillis();
                    applyMethod.invoke(editor, pars);
                    final long end = System.currentTimeMillis() - start;

                    // 应用数据完成
                    for (IEditorListener listener : editor.getListeners()) {
                        listener.onPostApply(editor, end);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    // 应用数据失败
                    for (IEditorListener listener : editor.getListeners()) {
                        listener.onPostLoad(editor, -1);
                    }
                }
            }
            return editor;
        });
    }

    @Override
    public Future<IRomEditor> reloadEditor(@NotNull Class<? extends IRomEditor> type) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <E extends IRomEditor> E getEditor(Class<? extends IRomEditor> editor) {
        return (E) editors.get(editor);
    }

}
