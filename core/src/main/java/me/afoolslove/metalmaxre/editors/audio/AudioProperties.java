package me.afoolslove.metalmaxre.editors.audio;

import me.afoolslove.metalmaxre.utils.NumberR;

public class AudioProperties {
    public static final int NOISE_MASK = 0B0000_0001;
    public static final int TRIANGLE_MASK = 0B0000_0010;
    public static final int SQUARE2_MASK = 0B0000_0100;
    public static final int SQUARE1_MASK = 0B0000_1000;

    private byte head;
    private char unknown;
    private Character noise;
    private Character triangle;
    private Character square2;
    private Character square1;

    public AudioProperties(byte head, char unknown, Character noise, Character triangle, Character square2, Character square1) {
        this.head = head;
        this.unknown = unknown;
        this.noise = noise;
        this.triangle = triangle;
        this.square2 = square2;
        this.square1 = square1;
    }

    public AudioProperties(byte[] data) {
        this.head = data[0x00];
        this.unknown = (char) NumberR.toInt(data[0x01], data[0x02]);
        int offset = 3;
        if (hasNoise()) {
            this.noise = (char) NumberR.toInt(data[offset], data[offset + 1]);
            offset += 2;
        }
        if (hasTriangle()) {
            this.triangle = (char) NumberR.toInt(data[offset], data[offset + 1]);
            offset += 2;
        }
        if (hasSquare2()) {
            this.square2 = (char) NumberR.toInt(data[offset], data[offset + 1]);
            offset += 2;
        }
        if (hasSquare1()) {
            this.square1 = (char) NumberR.toInt(data[offset], data[offset + 1]);
        }
    }

    public byte getHead() {
        return head;
    }

    public char getUnknown() {
        return unknown;
    }

    public Character getNoise() {
        return noise;
    }

    public Character getTriangle() {
        return triangle;
    }

    public Character getSquare2() {
        return square2;
    }

    public Character getSquare1() {
        return square1;
    }

    public boolean hasNoise() {
        return (getHead() & NOISE_MASK) != 0;
    }

    public boolean hasTriangle() {
        return (getHead() & TRIANGLE_MASK) != 0;
    }

    public boolean hasSquare2() {
        return (getHead() & SQUARE2_MASK) != 0;
    }

    public boolean hasSquare1() {
        return (getHead() & SQUARE1_MASK) != 0;
    }
}
