package me.afoolslove.metalmaxre.event;

import me.afoolslove.metalmaxre.MetalMaxRe;
import org.jetbrains.annotations.NotNull;


/**
 * 事件
 *
 * @author AFoolLove
 */
public class Event {
    @NotNull
    private final MetalMaxRe metalMaxRe;

    public Event(@NotNull MetalMaxRe metalMaxRe) {
        this.metalMaxRe = metalMaxRe;
    }

    @NotNull
    public MetalMaxRe getMetalMaxRe() {
        return metalMaxRe;
    }
}
