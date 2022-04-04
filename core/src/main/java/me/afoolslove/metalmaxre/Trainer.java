package me.afoolslove.metalmaxre;

import java.nio.ByteBuffer;

public class Trainer {
    public static final int TRAINER_LENGTH = 0x00200;

    private final ByteBuffer trainer;

    public Trainer() {
        trainer = ByteBuffer.allocate(TRAINER_LENGTH);
    }

    public Trainer(byte[] trainer) {
        this.trainer = ByteBuffer.wrap(trainer, 0x00000, TRAINER_LENGTH);
    }

    public byte[] getTrainer() {
        var dst = new byte[TRAINER_LENGTH];
        trainer.get(0x00000, dst);
        return dst;
    }

    public ByteBuffer getBuffer() {
        return trainer;
    }
}