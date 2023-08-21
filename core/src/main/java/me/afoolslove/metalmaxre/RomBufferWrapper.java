package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.utils.DataAddress;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Path;

/**
 * {@link RomBuffer}的包装类，与{@link RomBuffer}无异
 */
public class RomBufferWrapper extends RomBuffer {
    private final RomBuffer romBuffer;

    public RomBufferWrapper(@NotNull RomBuffer romBuffer) {
        super(romBuffer);
        if (romBuffer instanceof RomBufferWrapper) {
            throw new RuntimeException("don't pass in RomBufferWrapper");
        }
        this.romBuffer = romBuffer;
    }

    @Override
    public void close() {
        romBuffer.close();
    }

    @Override
    public @NotNull RomVersion getVersion() {
        return romBuffer.getVersion();
    }

    @Override
    public @Nullable Path getPath() {
        return romBuffer.getPath();
    }

    @Override
    public @NotNull GameHeader getHeader() {
        return romBuffer.getHeader();
    }

    @Override
    public @Nullable Trainer getTrainer() {
        return romBuffer.getTrainer();
    }

    @Override
    public @NotNull ByteBuffer getPrgRom() {
        return romBuffer.getPrgRom();
    }

    @Override
    public @NotNull ByteBuffer getChrRom() {
        return romBuffer.getChrRom();
    }

    @Override
    public void getChr(int index, byte[] bytes, int offset, int length) {
        romBuffer.getChr(index, bytes, offset, length);
    }

    @Override
    public void getChr(int index, byte[] bytes) {
        romBuffer.getChr(index, bytes);
    }

    @Override
    public byte getChr(int index) {
        return romBuffer.getChr(index);
    }

    @Override
    public int getChrToInt(int index) {
        return romBuffer.getChrToInt(index);
    }

    @Override
    public char getChrToChar(int index) {
        return romBuffer.getChrToChar(index);
    }

    @Override
    public void putChr(int index, byte[] bytes, int offset, int length) {
        romBuffer.putChr(index, bytes, offset, length);
    }

    @Override
    public void putChr(int index, byte[]... bytes) {
        romBuffer.putChr(index, bytes);
    }

    @Override
    public void putChr(int index, byte[] bytes) {
        romBuffer.putChr(index, bytes);
    }

    @Override
    public void putChr(int index, byte b) {
        romBuffer.putChr(index, b);
    }

    @Override
    public void putChrInt(int index, int n) {
        romBuffer.putChrInt(index, n);
    }

    @Override
    public void putChrChar(int index, char c) {
        romBuffer.putChrChar(index, c);
    }

    @Override
    public void getPrg(int index, byte[] bytes, int offset, int length) {
        romBuffer.getPrg(index, bytes, offset, length);
    }

    @Override
    public void getPrg(int index, byte[] bytes) {
        romBuffer.getPrg(index, bytes);
    }

    @Override
    public byte getPrg(int index) {
        return romBuffer.getPrg(index);
    }

    @Override
    public int getPrgToInt(int index) {
        return romBuffer.getPrgToInt(index);
    }

    @Override
    public char getPrgToChar(int index) {
        return romBuffer.getPrgToChar(index);
    }

    @Override
    public void getLastPrg(int index, byte[] bytes, int offset, int length) {
        romBuffer.getLastPrg(index, bytes, offset, length);
    }

    @Override
    public void getLastPrg(int index, byte[] bytes) {
        romBuffer.getLastPrg(index, bytes);
    }

    @Override
    public byte getLastPrg(int index) {
        return romBuffer.getLastPrg(index);
    }

    @Override
    public int getLastPrgToInt(int index) {
        return romBuffer.getLastPrgToInt(index);
    }

    @Override
    public char getLastPrgToChar(int index) {
        return romBuffer.getLastPrgToChar(index);
    }

    @Override
    public void putPrg(int index, byte[] bytes, int offset, int length) {
        romBuffer.putPrg(index, bytes, offset, length);
    }

    @Override
    public void putPrg(int index, byte[]... bytes) {
        romBuffer.putPrg(index, bytes);
    }

    @Override
    public void putPrg(int index, byte[] bytes) {
        romBuffer.putPrg(index, bytes);
    }

    @Override
    public void putPrg(int index, byte b) {
        romBuffer.putPrg(index, b);
    }

    @Override
    public void putPrgInt(int index, int n) {
        romBuffer.putPrgInt(index, n);
    }

    @Override
    public void putPrgChar(int index, char c) {
        romBuffer.putPrgChar(index, c);
    }

    @Override
    public void putLastPrg(int index, byte[] bytes, int offset, int length) {
        romBuffer.putLastPrg(index, bytes, offset, length);
    }

    @Override
    public void putLastPrg(int index, byte[]... bytes) {
        romBuffer.putLastPrg(index, bytes);
    }

    @Override
    public void putLastPrg(int index, byte[] bytes) {
        romBuffer.putLastPrg(index, bytes);
    }

    @Override
    public void putLastPrg(int index, byte b) {
        romBuffer.putLastPrg(index, b);
    }

    @Override
    public void putLastPrgInt(int index, int n) {
        romBuffer.putPrgInt(index, n);
    }

    @Override
    public void putLastPrgChar(int index, char c) {
        romBuffer.putLastPrgChar(index, c);
    }

    @Override
    public void get(int index, byte[] bytes, int offset, int length) {
        romBuffer.get(index, bytes, offset, length);
    }

    @Override
    public void get(int index, byte[] bytes) {
        romBuffer.get(index, bytes);
    }

    @Override
    public byte get(int index) {
        return romBuffer.get(index);
    }

    @Override
    public void get(@NotNull DataAddress address, byte[] bytes, int offset, int length) {
        romBuffer.get(address, bytes, offset, length);
    }

    @Override
    public void get(@NotNull DataAddress address, byte[] bytes) {
        romBuffer.get(address, bytes);
    }

    @Override
    public byte get(@NotNull DataAddress address, int offset) {
        return romBuffer.get(address, offset);
    }

    @Override
    public byte get(@NotNull DataAddress address) {
        return romBuffer.get(address);
    }

    @Override
    public int getToInt(int index) {
        return romBuffer.getToInt(index);
    }

    @Override
    public char getToChar(int index) {
        return romBuffer.getToChar(index);
    }

    @Override
    public void put(int index, byte[] bytes, int offset, int length) {
        romBuffer.put(index, bytes, offset, length);
    }

    @Override
    public void put(int index, byte[]... bytes) {
        romBuffer.put(index, bytes);
    }

    @Override
    public void put(int index, byte[] bytes) {
        romBuffer.put(index, bytes);
    }

    @Override
    public void put(int index, byte b) {
        romBuffer.put(index, b);
    }

    @Override
    public void putInt(int index, int n) {
        romBuffer.putInt(index, n);
    }

    @Override
    public void putChar(int index, char c) {
        romBuffer.putChar(index, c);
    }

    @Override
    public void put(@NotNull DataAddress dataAddress, byte[] bytes, int offset, int length) {
        romBuffer.put(dataAddress, bytes, offset, length);
    }

    @Override
    public void put(@NotNull DataAddress dataAddress, byte[]... bytes) {
        romBuffer.put(dataAddress, bytes);
    }

    @Override
    public void put(@NotNull DataAddress dataAddress, byte[] bytes) {
        romBuffer.put(dataAddress, bytes);
    }

    @Override
    public void getWholeBytes(int index, int offset, int length, byte[]... aaBytes) {
        romBuffer.getWholeBytes(index, offset, length, aaBytes);
    }

    @Override
    public void putWholeBytes(int index, int offset, int length, byte[]... aaBytes) {
        romBuffer.putWholeBytes(index, offset, length, aaBytes);
    }

    @Override
    public void getAABytes(int index, int offset, int length, byte[]... aaBytes) {
        romBuffer.getAABytes(index, offset, length, aaBytes);
    }

    @Override
    public void putAABytes(int index, int offset, int length, byte[]... aaBytes) {
        romBuffer.putAABytes(index, offset, length, aaBytes);
    }

    @Override
    public void save(@NotNull OutputStream outputStream) throws IOException {
        romBuffer.save(outputStream);
    }

    @Override
    public byte[] toByteArray() {
        return romBuffer.toByteArray();
    }

    @Override
    public void save(@NotNull Path path) throws IOException {
        romBuffer.save(path);
    }
}
