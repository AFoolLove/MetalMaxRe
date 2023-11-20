package me.afoolslove.metalmaxre;

import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static java.nio.file.StandardOpenOption.CREATE;

/**
 * 提供了基本的ROM结构
 *
 * @author AFoolLove
 */
public class RomBuffer implements AutoCloseable, Closeable {
    private static final byte[] ZIP_HEADER_1 = {0x50, 0x4B, 0x03, 0x04};
    private static final byte[] ZIP_HEADER_2 = {0x50, 0x4B, 0x05, 0x06};
    public static final byte[] NES_HEADER = {0x4E, 0x45, 0x53, 0x1A};

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

    private boolean isZip = false;
    private String zipRomName;

    /**
     * PRG ROM大小变更监听器
     */
    private final GameHeader.PrgRomChangeListener prgRomChangeListener = (header, oldValue, newValue) -> {
        if (oldValue != newValue) {
            ByteBuffer oldPrgRom = prgRom;
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
            ByteBuffer oldChrRom = chrRom;
            chrRom = ByteBuffer.allocate(newValue * 0x4000);
            if (oldValue > newValue) {
                chrRom.put(0x00000, oldChrRom, 0x00000, chrRom.capacity());
            } else {
                chrRom.put(0x00000, oldChrRom, 0x00000, oldChrRom.capacity());
            }
        }
    };

    protected RomBuffer(@NotNull RomBuffer romBuffer) {
        this.header = romBuffer.getHeader();
        this.version = romBuffer.getVersion();
        this.path = romBuffer.getPath();
        this.prgRom = romBuffer.prgRom;
        this.chrRom = romBuffer.chrRom;
    }

    public RomBuffer(@NotNull RomVersion version, @NotNull byte[] bytes) {
        this.version = version;
        this.path = null;

        this.header = new GameHeader(Arrays.copyOfRange(bytes, 0x00000, GameHeader.HEADER_LENGTH));
        if (getHeader().isTrained()) {
            this.trainer = new Trainer(Arrays.copyOfRange(bytes, GameHeader.HEADER_LENGTH, GameHeader.HEADER_LENGTH + Trainer.TRAINER_LENGTH));
        }
        this.prgRom = ByteBuffer.allocate(getHeader().getPrgRomLength());
        this.chrRom = ByteBuffer.allocate(getHeader().getChrRomLength());

        prgRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getPrgRomStart(), getHeader().getPrgRomEnd() + 1));
        chrRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getChrRomStart(), getHeader().getChrRomEnd() + 1));

        // 监听PrgRom和ChrRom变更
        getHeader().addPrgRomChangeListener(prgRomChangeListener);
        getHeader().addChrRomChangeListener(chrRomChangeListener);
    }

    public RomBuffer(@NotNull RomVersion version, @Nullable Path path) throws IOException {
        this.version = version;
        this.path = path;

        byte[] bytes;
        if (path == null) {
            bytes = ResourceManager.getAsBytes(version.getPath());
        } else {
            // 读取外部文件
            bytes = Files.readAllBytes(path);

            // 验证是否为zip
            byte[] fileHeader = Arrays.copyOfRange(bytes, 0x00, 0x04);
            if (!Arrays.equals(fileHeader, NES_HEADER) && (Arrays.equals(fileHeader, ZIP_HEADER_1) || Arrays.equals(fileHeader, ZIP_HEADER_2))) {
                // ZIP文件，打开并搜索nes文件
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try (ZipFile zipFile = new ZipFile(path.toFile())) {
                    Enumeration<? extends ZipEntry> entries = zipFile.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry zipEntry = entries.nextElement();
                        if (zipEntry.isDirectory()) {
                            continue;
                        }
                        if (zipEntry.getName().endsWith(".nes")) {
                            zipFile.getInputStream(zipEntry).transferTo(outputStream);
                            isZip = true;
                            zipRomName = zipEntry.getName();
                            break;
                        }
                    }
                }

                if (!isZip) {
                    // zip中没有nes文件
                    throw new RuntimeException(String.format("nes file was not found in the zip file(%s)", path));
                }
                bytes = outputStream.toByteArray();
            }
        }

        this.header = new GameHeader(Arrays.copyOfRange(bytes, 0x00000, GameHeader.HEADER_LENGTH));
        if (getHeader().isTrained()) {
            this.trainer = new Trainer(Arrays.copyOfRange(bytes, GameHeader.HEADER_LENGTH, GameHeader.HEADER_LENGTH + Trainer.TRAINER_LENGTH));
        }
        this.prgRom = ByteBuffer.allocate(getHeader().getPrgRomLength());
        this.chrRom = ByteBuffer.allocate(getHeader().getChrRomLength());

        prgRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getPrgRomStart(), getHeader().getPrgRomEnd() + 1));
        chrRom.put(0x00000, Arrays.copyOfRange(bytes, getHeader().getChrRomStart(), getHeader().getChrRomEnd() + 1));

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
     * @return 是否为在Zip中加载的ROM
     */
    public boolean isZip() {
        return isZip;
    }

    /**
     * @return zip中的ROM文件名称，需要 {@link #isZip()} == true 才能使用，否则返回{ @code null}
     */
    public String getZipRomName() {
        return zipRomName;
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

    public void getLastPrg(int index, byte[] bytes, int offset, int length) {
        getPrgRom().get(getHeader().getLastPrgRomLength() + index, bytes, offset, length);
    }

    public void getLastPrg(int index, byte[] bytes) {
        getLastPrg(index, bytes, 0x00000, bytes.length);
    }

    public byte getLastPrg(int index) {
        return getPrgRom().get(getHeader().getLastPrgRomLength() + index);
    }

    public int getLastPrgToInt(int index) {
        return getLastPrg(index) & 0xFF;
    }

    public char getLastPrgToChar(int index) {
        return (char) getLastPrgToInt(index);
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


    public void putLastPrg(int index, byte[] bytes, int offset, int length) {
        getPrgRom().put(getHeader().getLastPrgRomLength() + index, bytes, offset, length);
    }

    public void putLastPrg(int index, byte[]... bytes) {
        for (int i = 0, offset = 0; i < bytes.length; offset += bytes[i].length, i++) {
            putLastPrg(index + offset, bytes[i], 0x00000, bytes[i].length);
        }
    }

    public void putLastPrg(int index, byte[] bytes) {
        getPrgRom().put(getHeader().getLastPrgRomLength() + index, bytes, 0x00000, bytes.length);
    }

    public void putLastPrg(int index, byte b) {
        getPrgRom().put(getHeader().getLastPrgRomLength() + index, b);
    }

    public void putLastPrgInt(int index, int n) {
        getPrgRom().putInt(getHeader().getLastPrgRomLength() + index, n);
    }

    public void putLastPrgChar(int index, char c) {
        getPrgRom().putChar(getHeader().getLastPrgRomLength() + index, c);
    }


    public void get(int index, byte[] bytes, int offset, int length) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            getChr(index, bytes, offset, length);
        } else {
            index -= getHeader().getPrgRomStart();
            getPrg(index, bytes, offset, length);
        }
    }

    public void get(int index, byte[] bytes) {
        get(index, bytes, 0x00000, bytes.length);
    }

    public byte get(int index) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            return getChrRom().get(index);
        } else {
            index -= getHeader().getPrgRomStart();
            return getPrgRom().get(index);
        }
    }

    public void get(@NotNull DataAddress address, byte[] bytes, int offset, int length) {
        switch (address.getType()) {
            case PRG -> getPrg(address.getStartAddress(), bytes, offset, length);
            case CHR -> getChr(address.getStartAddress(), bytes, offset, length);
        }
    }

    public void get(@NotNull DataAddress address, byte[] bytes) {
        get(address, bytes, 0x00000, bytes.length);
    }

    public byte get(@NotNull DataAddress address, int offset) {
        return switch (address.getType()) {
            case PRG -> getPrg(address.getStartAddress() + offset);
            case CHR -> getChr(address.getStartAddress() + offset);
        };
    }

    public byte get(@NotNull DataAddress address) {
        return get(address, 0);
    }

    public int getToInt(int index) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            return getChrRom().get(index) & 0xFF;
        } else {
            index -= getHeader().getPrgRomStart();
            return getPrgRom().get(index) & 0xFF;
        }
    }

    public char getToChar(int index) {
        return (char) getToInt(index);
    }

    public char getChar(int index) {
        int tmp;
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            tmp = getChrRom().getChar(index);
        } else {
            index -= getHeader().getPrgRomStart();
            tmp = getPrgRom().getChar(index);
        }

        if (getChrRom().order() == ByteOrder.BIG_ENDIAN) {
            // 字节倒序
            tmp += tmp << 16;
            //FF32
            //    FF32
            //FF32FF32
            tmp >>>= 8; //00FF32FF
        }
        return (char) (tmp & 0xFFFF);
    }

    public void put(int index, byte[] bytes, int offset, int length) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            getChrRom().put(index, bytes, offset, length);
        } else {
            index -= getHeader().getPrgRomStart();
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
            index -= getHeader().getChrRomStart();
            getChrRom().put(index, b);
        } else {
            index -= getHeader().getPrgRomStart();
            getPrgRom().put(index, b);
        }
    }

    public void putInt(int index, int n) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            getChrRom().putInt(index, n);
        } else {
            index -= getHeader().getPrgRomStart();
            getPrgRom().putInt(index, n);
        }
    }

    public void putChar(int index, char c) {
        if (index > getPrgRom().capacity()) {
            index -= getHeader().getChrRomStart();
            getChrRom().putChar(index, c);
        } else {
            index -= getHeader().getPrgRomStart();
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

    public void getWholeBytes(@NotNull DataAddress address, int offset, int length, byte[]... aaBytes) {
        getWholeBytes(address.getAbsStartAddress(this), offset, length, aaBytes);
    }

    public void putWholeBytes(@NotNull DataAddress address, int offset, int length, byte[]... aaBytes) {
        putWholeBytes(address.getAbsStartAddress(this), offset, length, aaBytes);
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

    public void getAABytes(@NotNull DataAddress address, int offset, int length, byte[]... aaBytes) {
        getAABytes(address.getAbsStartAddress(this), offset, length, aaBytes);
    }

    public void putAABytes(@NotNull DataAddress address, int offset, int length, byte[]... aaBytes) {
        putAABytes(address.getAbsStartAddress(this), offset, length, aaBytes);
    }


    /**
     * 保存到输出流
     *
     * @param outputStream 输出流
     */
    public void save(@NotNull OutputStream outputStream) throws IOException {
        outputStream.write(header.getHeader());
        if (trainer != null) {
            outputStream.write(trainer.getTrainer());
        }
        outputStream.write(getPrgRom().array());
        outputStream.write(getChrRom().array());
        outputStream.flush();
    }

    /**
     * 将ROM数据转换为字节数组
     *
     * @return 转换为字节数组的ROM
     */
    public byte[] toByteArray() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(
                GameHeader.HEADER_LENGTH
                + (trainer == null ? 0 : Trainer.TRAINER_LENGTH)
                + header.getPrgRomLength()
                + header.getChrRomLength());
        try {
            save(byteArrayOutputStream);
        } catch (IOException ignored) {
            // ?
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 保存到文件
     *
     * @param path 路径
     */
    public void save(@NotNull Path path) throws IOException {
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path);
        }

        Files.write(path, toByteArray(), CREATE);
    }
}
