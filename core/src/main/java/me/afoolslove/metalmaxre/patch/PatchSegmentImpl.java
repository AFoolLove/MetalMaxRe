package me.afoolslove.metalmaxre.patch;

import me.afoolslove.metalmaxre.utils.DataAddress;

public class PatchSegmentImpl implements IPatchSegment {
    private final DataAddress address;
    private final byte[] rawData;
    private final byte[] patchData;

    public PatchSegmentImpl(DataAddress address, byte[] rawData, byte[] patchData) {
        this.address = address;
        this.rawData = rawData;
        this.patchData = patchData;
    }

    @Override
    public DataAddress getAddress() {
        return address;
    }

    @Override
    public byte[] getPatchData() {
        return patchData;
    }

    @Override
    public byte[] getRawData() {
        return rawData;
    }
}
