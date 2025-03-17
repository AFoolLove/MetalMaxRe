package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.editors.palette.PaletteRow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class MonsterModel {
    public byte width;
    public byte height;

    private boolean isDoublePalette;
    private PaletteRow[] palettes;

    // 该数据会在写入数据时更新
    private byte modelIndex;

    private byte chrIndex;

    private boolean isChrIndexIncremental;

    private byte[] modelData;

    private MonsterModelType modelType;

    private Byte tileIndex; // 模型格式一有效，模型格式二为null

    private byte[] customPaletteYs; // 可能为null

    public MonsterModel(byte size) {
        this.width = (byte) ((size >>> 4) * 0x02); // F0
        this.height = (byte) ((size & 0x0F) * 0x02); // 0F
    }

    public MonsterModel(byte width, byte height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置怪物的宽度
     * <p>
     * *必须是2的倍数，不能小于2
     *
     * @param width 怪物的宽度
     */
    public void setWidth(byte width) {
        this.width = width;
    }

    public void setWidth(int width) {
        setWidth((byte) width);
    }

    /**
     * 设置怪物的高度
     * <p>
     * *必须是2的倍数，不能小于2
     *
     * @param height 怪物的高度
     */
    public void setHeight(byte height) {
        this.height = height;
    }

    public void setHeight(int height) {
        setHeight((byte) height);
    }

    /**
     * 设置是否为双调色板怪物
     *
     * @param isDoublePalette 是否为双调色板怪物
     */
    public void setDoublePalette(boolean isDoublePalette) {
        this.isDoublePalette = isDoublePalette;
    }

    /**
     * 设置双调色板
     * <p>
     * *自动切换为双调色板怪物
     */
    public void setDoublePalette(@NotNull PaletteRow paletteA, @NotNull PaletteRow paletteB) {
        setDoublePalette(true);

        palettes = new PaletteRow[0x02];
        palettes[0x00] = paletteA;
        palettes[0x01] = paletteB;
    }

    public boolean setDoublePalette(@NotNull PaletteRow[] palettes) {
        if (palettes.length < 0x02) {
            return false;
        }

        setDoublePalette(true); // 更新为双调色板
        this.palettes = new PaletteRow[0x02];
        System.arraycopy(palettes, 0x00, this.palettes, 0x00, 0x02);
        return true;
    }

    /**
     * 设置是否为单调色板怪物
     *
     * @param isSinglePalette 是否为单调色板怪物
     */
    public void setSinglePalette(boolean isSinglePalette) {
        this.isDoublePalette = !isSinglePalette;
    }

    /**
     * 设置单调色板
     * <p>
     * *自动设置为单调色板怪物
     */
    public void setSinglePalette(@NotNull PaletteRow palette) {
        setSinglePalette(true); // 设置为单调色板

        this.palettes = new PaletteRow[]{palette};
    }

    /**
     * 设置模型索引
     * <p>
     * *该值在应用编辑器数据后会更新
     *
     * @param modelIndex 模型索引
     */
    public void setModelIndex(byte modelIndex) {
        this.modelIndex = modelIndex;
    }

    public void setModelIndex(int modelIndex) {
        setModelIndex((byte) (modelIndex & 0xFF));
    }

    /**
     * 设置怪物图像(CHR)索引
     *
     * @param chrIndex 怪物图像(CHR)索引
     */
    public void setChrIndex(byte chrIndex) {
        this.chrIndex = chrIndex;
    }

    public void setChrIndex(int chrIndex) {
        setChrIndex((byte) (chrIndex & 0xFF));
    }


    /**
     * 怪物图像(CHR)是否自增，连续使用三个chr块
     *
     * @param isChrIndexIncremental 怪物图像(CHR)是否自增
     */
    public void setChrIndexIncremental(boolean isChrIndexIncremental) {
        this.isChrIndexIncremental = isChrIndexIncremental;
    }

    /**
     * 设置怪物模型数据
     *
     * @param modelData 怪物模型数据
     */
    public void setModelData(byte[] modelData) {
        this.modelData = modelData;
    }

    /**
     * 设置怪物模型数据的类型
     *
     * @param modelType 怪物模型数据的类型
     */
    public void setModelType(MonsterModelType modelType) {
        this.modelType = modelType;
    }


    /**
     * 设置怪物模型类型和数据
     *
     * @param modelType 怪物模型数据的类型
     * @param modelData 怪物模型数据
     */
    public void setModelData(@NotNull MonsterModelType modelType, byte[] modelData) {
        setModelType(modelType);
        setModelData(modelData);
    }

    /**
     * 设置怪物起始图像id
     * <p>
     * *仅模型格式一有效
     *
     * @param tileIndex 怪物起始图像id
     */
    public void setTileIndex(@Nullable Byte tileIndex) {
        this.tileIndex = tileIndex;
    }

    public void setTileIndex(int tileIndex) {
        setTileIndex(Byte.valueOf((byte) (tileIndex & 0xFF)));
    }

    /**
     * 设置怪物自定义调色板Y值数据
     *
     * @param customPaletteYs 怪物自定义调色板Y值数据
     */
    public void setCustomPaletteYs(byte[] customPaletteYs) {
        this.customPaletteYs = customPaletteYs;
    }


    /**
     * 获取怪物的宽度
     *
     * @return 怪物的宽度
     */
    public byte getWidth() {
        return width;
    }

    public int intWidth() {
        return getWidth() & 0xFF;
    }

    /**
     * 获取怪物的高度
     *
     * @return 怪物的高度
     */
    public byte getHeight() {
        return height;
    }


    public int intHeight() {
        return getHeight() & 0xFF;
    }

    /**
     * 获取数据中使用的怪物模型大小
     * <p>
     * 宽度：0B1111_0000
     * <p>
     * 高度：0B0000_1111
     *
     * @return 数据中使用的怪物模型大小
     */
    public byte getModelSize() {
        int width = (getWidth() >>> 1) & 0x0F;
        int height = (getHeight() >>> 1) & 0x0F;
        int size = width << 4;
        size |= height;
        return (byte) size;
    }

    public int intModelSize() {
        return getModelSize() & 0xFF;
    }

    /**
     * 获取是否为双调色板
     *
     * @return 是否为双调色板
     */
    public boolean isDoublePalette() {
        return isDoublePalette;
    }

    /**
     * 获取是否为单调色板
     *
     * @return 是否为单调色板
     */
    public boolean isSinglePalette() {
        return !isDoublePalette();
    }

    /**
     * 获取双调色板
     *
     * @return 双调色板
     */
    public PaletteRow[] getDoublePalettes() {
        return palettes;
    }

    /**
     * 获取单调色板
     *
     * @return 单调色板
     */
    public PaletteRow getSinglePalettes() {
        return palettes[0x00];
    }

    /**
     * 获取怪物模型索引
     *
     * <p>
     * *该值在应用编辑器数据后会更新
     *
     * @return 怪物模型索引
     */
    public byte getModelIndex() {
        return modelIndex;
    }

    public int intModelIndex() {
        return getModelIndex() & 0xFF;
    }

    /**
     * 获取怪物图像(CHR)索引
     *
     * @return 怪物图像(CHR)索引
     */
    public byte getChrIndex() {
        return chrIndex;
    }

    public int intChrIndex() {
        return getChrIndex() & 0xFF;
    }

    /**
     * 获取怪物图像(CHR)是否自增，连续使用三个chr块
     *
     * @return 怪物图像(CHR)是否自增，连续使用三个chr块
     */
    public boolean isChrIndexIncremental() {
        return isChrIndexIncremental;
    }

    /**
     * 获取怪物模型数据
     *
     * @return 怪物模型数据
     */
    public byte[] getModelData() {
        return modelData;
    }

    /**
     * 获取模型占用的图块数量
     *
     * @return 模型占用的图块数量
     */
    public int getModelDataCount() {
        if (getModelType() == MonsterModelType.A) {
            // 计算有多少个1
            int count = 0x00;
            for (byte modelDatum : getModelData()) {
                int tmp = modelDatum & 0xFF;
                while (tmp > 0x00) {
                    tmp &= tmp - 1;
                    count++;
                }
            }
            return count;
        }
        return getModelData().length;
    }

    /**
     * 获取怪物模型数据的类型
     *
     * @return 怪物模型数据的类型
     */
    public MonsterModelType getModelType() {
        return modelType;
    }

    /**
     * 获取怪物起始图像id
     * <p>
     * *仅模型格式一有效
     *
     * @return 怪物起始图像id
     */
    public Byte getTileIndex() {
        return tileIndex;
    }

    public int intTileIndex() {
        return getTileIndex() & 0xFF;
    }

    /**
     * 获取怪物自定义调色板Y值数据
     *
     * @return 怪物自定义调色板Y值数据
     */
    public byte[] getCustomPaletteYs() {
        return customPaletteYs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MonsterModel that)) return false;
        return getWidth() == that.getWidth()
               && getHeight() == that.getHeight()
               && isDoublePalette() == that.isDoublePalette()
               && getModelIndex() == that.getModelIndex()
               && getChrIndex() == that.getChrIndex()
               && isChrIndexIncremental() == that.isChrIndexIncremental()
               && Objects.deepEquals(palettes, that.palettes)
               && Objects.deepEquals(getModelData(), that.getModelData())
               && getModelType() == that.getModelType()
               && Objects.equals(getTileIndex(), that.getTileIndex())
               && Objects.deepEquals(getCustomPaletteYs(), that.getCustomPaletteYs());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getWidth(), getHeight(), isDoublePalette(), Arrays.hashCode(palettes), getModelIndex(), getChrIndex(), isChrIndexIncremental(), Arrays.hashCode(getModelData()), getModelType(), getTileIndex(), Arrays.hashCode(getCustomPaletteYs()));
    }
}
