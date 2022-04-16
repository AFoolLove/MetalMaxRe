package me.afoolslove.metalmaxre.event;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 事件处理器
 *
 * @author AFoolLove
 */
public class EventHandler {
    private final Map<Class<Event>, Set<EventListener>> eventListeners = new HashMap<>();

    /**
     * 注册这个监听器里的所有有效的事件监听器
     *
     * @param listener 被注册的监听器
     */
    public void register(@NotNull EventListener listener) {
        for (Method declaredMethod : listener.getClass().getDeclaredMethods()) {
            if (declaredMethod.getParameterCount() == 1) {
                var parameter = declaredMethod.getParameters()[0];
                if (Event.class.isAssignableFrom(parameter.getType())) {
                    register((Class<Event>) parameter.getType(), listener);
                }
            }
        }
    }

    /**
     * 注册一个事件监听器
     *
     * @param eventType 事件类型
     * @param listener  被注册的监听器
     */
    public void register(@NotNull Class<Event> eventType, @NotNull EventListener listener) {
        var listeners = eventListeners.computeIfAbsent(eventType, k -> new HashSet<>());
        listeners.add(listener);
    }

    /**
     * 注销事件监听器里所有有效的事件监听器
     *
     * @param listener 被注销所有事件监听的监听器
     */
    public void unregister(@NotNull EventListener listener) {

    }

    /**
     * 注销一个事件监听器
     *
     * @param eventType 事件类型
     * @param listener  被注销的监听器
     */
    public void unregister(@NotNull Class<Event> eventType, @NotNull EventListener listener) {
        var listeners = eventListeners.get(eventType);
        if (listeners == null || listeners.isEmpty()) {
            return;
        }
        listeners.remove(listener);
    }

    /**
     * 通知一个事件的发生
     *
     * @param event 发生的事件
     */
    public void callEvent(@NotNull Event event) {
        eventListeners.entrySet().stream()
                // 目标事件类的它父类才能接受事件
                .filter(entry -> entry.getKey().isAssignableFrom(event.getClass()))
                .forEach(e -> {
                    for (EventListener eventListener : e.getValue()) {
                        for (Method declaredMethod : eventListener.getClass().getDeclaredMethods()) {
                            if (declaredMethod.getParameterCount() == 1) {
                                var parameter = declaredMethod.getParameters()[0];
                                if (e.getKey().isAssignableFrom(parameter.getType())) {
                                    try {
                                        declaredMethod.setAccessible(true);
                                        declaredMethod.invoke(eventListener, event);
                                    } catch (IllegalAccessException | InvocationTargetException ex) {
                                        // throw new RuntimeException(ex);
                                        ex.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });

    }
}
