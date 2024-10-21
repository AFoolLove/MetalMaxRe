package me.afoolslove.metalmaxre.editors.save;

import java.nio.ByteBuffer;

public class SaveData {
    private ByteBuffer buffer;

    public SaveData(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }
}
