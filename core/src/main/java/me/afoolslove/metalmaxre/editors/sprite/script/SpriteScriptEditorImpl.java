package me.afoolslove.metalmaxre.editors.sprite.script;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class SpriteScriptEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteScriptEditor {
    private final DataAddress spriteScriptIndexAddress;
    private final DataAddress spriteScriptAddress;

    private final Map<Integer, SpriteScript> spriteScripts = new HashMap<>();


    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x25177 - 0x10, 0x252C8 - 0x10),
                DataAddress.fromPRG(0x252C9 - 0x10, 0x2600F - 0x10));
    }

    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                  @NotNull DataAddress spriteScriptIndexAddress,
                                  @NotNull DataAddress spriteScriptAddress) {
        super(metalMaxRe);
        this.spriteScriptAddress = spriteScriptAddress;
        this.spriteScriptIndexAddress = spriteScriptIndexAddress;
    }

    @Editor.Load
    public void onLoad() {
        getSpriteScripts().clear();

        char[] indexes = new char[getSpriteScriptMaxCount()];
        position(getSpriteScriptIndexAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }


    }

    @Editor.Apply
    public void onApply() {

    }

    @Override
    public Map<Integer, SpriteScript> getSpriteScripts() {
        return spriteScripts;
    }

    @Override
    public DataAddress getSpriteScriptIndexAddress() {
        return spriteScriptIndexAddress;
    }

    @Override
    public DataAddress getSpriteScriptAddress() {
        return spriteScriptAddress;
    }
}
