package me.afoolslove.metalmaxre.event.editors.computer.vendor;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.editors.computer.shop.IShopEditor;
import me.afoolslove.metalmaxre.event.editors.EditorEvent;
import org.jetbrains.annotations.NotNull;

public class EditorVendorEvent extends EditorEvent {

    public EditorVendorEvent(@NotNull MetalMaxRe metalMaxRe, @NotNull IShopEditor editor) {
        super(metalMaxRe, editor);
    }
}
