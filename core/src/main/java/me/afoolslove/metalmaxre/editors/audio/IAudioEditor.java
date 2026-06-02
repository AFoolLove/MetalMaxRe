package me.afoolslove.metalmaxre.editors.audio;

import me.afoolslove.metalmaxre.editors.IRomEditor;

public interface IAudioEditor extends IRomEditor {
    @Override
    default String getId() {
        return "audioEditor";
    }
}
