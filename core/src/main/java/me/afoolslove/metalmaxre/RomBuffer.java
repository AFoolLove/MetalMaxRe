package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * 提供了基本的ROM结构
 *
 * @author AFoolLove
 */
public class RomBuffer implements AutoCloseable, Closeable {

    /**
     * NES头属性
     */
    @NotNull
    private final GameHeader header;

    /**
     * Trainer
     */
    @Nullable
    private Trainer trainer;

    /**
     * 程序ROM
     */
    @NotNull
    private ByteBuffer prgRom;
    /**
     * 图像ROM
     */
    @NotNull
    private ByteBuffer chrRom;

    /**
     * ROM版本
     */
    @NotNull
    private final RomVersion version;

    /**
     * ROM所在的路径，如果为null则使用的内部ROM
     */
    @Nullable
    private final Path path;

    /**
     * PRG ROM大小变更监听器
     */
    private final GameHeader.PrgRomChangeListener prgRomChangeListener = (header, oldValue, newValue) -> {
        if (oldValue != newValue) {
            var oldPrgRom = prgRom;
            prgRom = ByteBuffer.allocate(newValue * 0x4000);
            if (oldValue > newValue) {
                prgRom.put(0x00000, oldPrgRom, 0x00000, prgRom.capacity());
            } else {
                prgRom.put(0x00000, oldPrgRom, 0x00000, oldPrgRom.capacity());
            }
        }
    };
    /**
     * CHR ROM大小变更监听器
     */
    private final GameHeader.ChrRomChangeListener chrRomChangeListener = (header, oldValue, newValue) -> {
        if (oldValue != newValue) {
            var oldChrRom = chrRom;
            chrRom = ByteBuffer.allocate(newValue * 0x4000);
            if (oldValue > newValue) {
                chrRom.put(0x00000, oldChrRom, 0x00000, chrRom.capacity());
            } else {
                chrRom.put(0x00000, oldChrRom, 0x00000, oldChrRom.capacity());
            }
        }
    };

    public RomBuffer(@NotNull RomVersion version, @Nullable Path path) throws IOException {
        this.version = version;
        this.path = path;

        byte[] bytes;
        if (path == null) {
            bytes = ResourceManager.getAsBytes("/roms/" + version.getPath());
        } else {
            // 读取外部文件
            bytes = Files.readAllBytes(path);
        }

        this.header = new GameHeader(Arrays.copyOfRange(bytes, 0x00000, GameHeader.HEADER_LENGTH));
        if (getHeader().isTrained()) {
            this.trainer = new Trainer(Arrays.copyOfRange(bytes, GameHeader.HEADER_LENGTH, GameHeader.HEADER_LENGTH + Trainer.TRAINER_LENGTH));
        }
        this.prgRom = ByteBuffer.allocate(getHeader().getPrgRomLength());
        this.chrRom = ByteBuffer.allocate(getHeader().getChrRomLength());

        prgRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getPrgRomStart(), getHeader().getPrgRomEnd()));
        chrRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getChrRomStart(), getHeader().getChrRomEnd()));

        // 监听PrgRom和ChrRom变更
        getHeader().addPrgRomChangeListener(prgRomChangeListener);
        getHeader().addChrRomChangeListener(chrRomChangeListener);
    }

    @Override
    public void close() {
        getHeader().removePrgRomChangeListener(prgRomChangeListener);
        getHeader().removeChrRomChangeListener(chrRomChangeListener);
    }

    /**
     * 获取ROM的版本
     *
     * @return ROM版本
     */
    @NotNull
    public RomVersion getVersion() {
        return version;
    }

    @Nullable
    public Path getPath() {
        return path;
    }

    /**
     * 获取ROM的头数据
     *
     * @return ROM头数据
     */
    @NotNull
    public GameHeader getHeader() {
        return header;
    }

    /**
     * 获取ROM的Trainer
     *
     * @return ROM的Trainer
     */
    @Nullable
    public Trainer getTrainer() {
        return trainer;
    }

    /**
     * 获取PRG ROM的数据
     *
     * @return PRG ROM的数据
     */
    @NotNull
    public ByteBuffer getPrgRom() {
        return prgRom;
    }

    /**
     * 获取CHR ROM的数据
     *
     * @return CHR ROM的数据
     */
    @NotNull
    public ByteBuffer getChrRom() {
        return chrRom;
    }

    public void getChr(int index, byte[] bytes, int offset, int length) {
        getChrRom().get(index, bytes, offset, length);
    }

    public void getChr(int index, byte[] bytes) {
        getChr(index, bytes, 0x00000, bytes.length);
    }

    public byte getChr(int index) {
        return getChrRom().get(index);
    }

    public int getChrToInt(int index) {
        return getChrRom().get(index) & 0xFF;
    }

    public char getChrToChar(int index) {
        return (char) getChrToInt(index);
    }

    public void putChr(int index, byte[] bytes, int offset, int length) {
        getChrRom().put(index, bytes, offset, length);
    }

    public void putChr(int index, byte[]... bytes) {
        for (int i = 0, offset = 0; i < bytes.length; offset += bytes[i].length, i++) {
            putChr(index + offset, bytes[i], 0x00000, bytes[i].length);
        }
    }

    public void putChr(int index, byte[] bytes) {
        getChrRom().put(index, bytes, 0x00000, bytes.length);
    }

    public void putChr(int index, byte b) {
        getChrRom().put(index, b);
    }

    public void putChrInt(int index, int n) {
        getChrRom().putInt(index, n);
    }

    public void putChrChar(int index, char c) {
        getChrRom().putChar(index, c);
    }


    public void getPrg(int index, byte[] bytes, int offset, int length) {
        getPrgRom().get(index, bytes, offset, length);
    }

    public void getPrg(int index, byte[] bytes) {
        getPrg(index, bytes, 0x00000, bytes.length);
    }

    public byte getPrg(int index) {
        return getPrgRom().get(index);
    }

    public int getPrgToInt(int index) {
        return getPrgRom().get(index) & 0xFF;
    }

    public char getPrgToChar(int index) {
        return (char) getPrgToInt(index);
    }

    public void putPrg(int index, byte[] bytes, int offset, int length) {
        getPrgRom().put(index, bytes, offset, length);
    }

    public void putPrg(int index, byte[]... bytes) {
        for (int i = 0, offset = 0; i < bytes.length; offset += bytes[i].length, i++) {
            putPrg(index + offset, bytes[i], 0x00000, bytes[i].length);
        }
    }

    public void putPrg(int index, byte[] bytes) {
        getPrgRom().put(index, bytes, 0x00000, bytes.length);
    }

    public void putPrg(int index, byte b) {
        getPrgRom().put(index, b);
    }

    public void putPrgInt(int index, int n) {
        getPrgRom().putInt(index, n);
    }

    public void putPrgChar(int index, char c) {
        getPrgRom().putChar(index, c);
    }


    public void get(int index, byte[] bytes, int offset, int length) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            getChr(index, bytes, offset, length);
        } else {
            getPrg(index, bytes, offset, length);
        }
    }

    public void get(int index, byte[] bytes) {
        get(index, bytes, 0x00000, bytes.length);
    }

    public byte get(int index) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            return getChrRom().get(index);
        } else {
            return getPrgRom().get(index);
        }
    }

    public int getToInt(int index) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            return getChrRom().get(index) & 0xFF;
        } else {
            return getPrgRom().get(index) & 0xFF;
        }
    }

    public char getToChar(int index) {
        return (char) getToInt(index);
    }

    public void put(int index, byte[] bytes, int offset, int length) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            getChrRom().put(index, bytes, offset, length);
        } else {
            getPrgRom().put(index, bytes, offset, length);
        }
    }

    public void put(int index, byte[]... bytes) {
        for (int i = 0, offset = 0; i < bytes.length; offset += bytes[i].length, i++) {
            put(index + offset, bytes[i], 0x00000, bytes[i].length);
        }
    }

    public void put(int index, byte[] bytes) {
        put(index, bytes, 0x00000, bytes.length);
    }

    public void put(int index, byte b) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            getChrRom().put(index, b);
        } else {
            getPrgRom().put(index, b);
        }
    }

    public void putInt(int index, int n) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            getChrRom().putInt(index, n);
        } else {
            getPrgRom().putInt(index, n);
        }
    }

    public void putChar(int index, char c) {
        if (index > getPrgRom().capacity()) {
            index -= getPrgRom().capacity();
            getChrRom().putChar(index, c);
        } else {
            getPrgRom().putChar(index, c);
        }
    }


    public void put(@NotNull DataAddress dataAddress, byte[] bytes, int offset, int length) {
        switch (dataAddress.getType()) {
            case PRG -> putPrg(dataAddress.getStartAddress(), bytes, offset, length);
            case CHR -> putChr(dataAddress.getStartAddress(), bytes, offset, length);
        }
    }

    public void put(@NotNull DataAddress dataAddress, byte[]... bytes) {
        switch (dataAddress.getType()) {
            case PRG -> putPrg(dataAddress.getStartAddress(), bytes);
            case CHR -> putChr(dataAddress.getStartAddress(), bytes);
        }
    }

    public void put(@NotNull DataAddress dataAddress, byte[] bytes) {
        switch (dataAddress.getType()) {
            case PRG -> putPrg(dataAddress.getStartAddress(), bytes);
            case CHR -> putChr(dataAddress.getStartAddress(), bytes);
        }
    }

    /**
     * 获取相同大小的数据到数组中
     * <p>
     * 格式：(X1+X2+X3+...)*N
     */
    public void getWholeBytes(int index, int offset, int length, byte[]... aaBytes) {
        byte[] tmp = new byte[aaBytes.length * length];
        get(index, tmp, offset, tmp.length);

        for (index = 0; index < tmp.length; index++) {
            aaBytes[index % aaBytes.length][index / aaBytes.length] = tmp[index];
        }
    }

    /**
     * 获取相同大小的数据到数组中
     * <p>
     * 格式：(X1+X2+X3+...)*N
     */
    public void putWholeBytes(int index, int offset, int length, byte[]... aaBytes) {
        byte[] tmp = new byte[aaBytes.length * length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = aaBytes[i % aaBytes.length][i / aaBytes.length];
        }
        put(index, tmp, offset, tmp.length);
    }

    /**
     * 获取相同大小的数据到数组中
     * <p>
     * 格式：(X1*N)+(X2*N)+(X3*N)+...
     */
    public void getAABytes(int index, int offset, int length, byte[]... aaBytes) {
        for (int i = 0; i < aaBytes.length; i++) {
            get(index + (i * length), aaBytes[i], offset, length);
        }
    }

    public void putAABytes(int index, int offset, int length, byte[]... aaBytes) {
        for (int i = 0; i < aaBytes.length; i++) {
            put(index + (i * length), aaBytes[i], offset, length);
        }
    }
}
