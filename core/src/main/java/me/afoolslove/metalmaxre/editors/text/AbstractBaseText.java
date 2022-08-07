package me.afoolslove.metalmaxre.editors.text;

public abstract class AbstractBaseText implements IBaseText {
    protected byte priority = 0x00;

    @Override
    public byte priority() {
        return priority;
    }
}
