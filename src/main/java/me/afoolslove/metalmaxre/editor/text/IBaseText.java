package me.afoolslove.metalmaxre.editor.text;

import org.jetbrains.annotations.NotNull;

public interface IBaseText extends Comparable<IBaseText> {

//    @NotNull
//    String getType();

    default int priority() {
        return 0;
    }

//    IBaseText priority(byte priority);

    @NotNull
    byte[] toByteArray();

    @Override
    default int compareTo(@NotNull IBaseText o) {
        return priority();
    }
}
