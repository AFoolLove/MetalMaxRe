package me.afoolslove.metalmaxre.editors.monster;

import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.editors.palette.PaletteRow;
import me.afoolslove.metalmaxre.editors.text.ITextEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.util.*;

public class MonsterModelImpl extends RomBufferWrapperAbstractEditor {
    @Override
    public String getId() {
        return "monsterModelEditor";
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(MonsterModelImpl.class);

    private final DataAddress monsterModelPaletteIndex = DataAddress.fromPRG(0x229CF - 0x10, 0x22A51 - 0x10);
    private final DataAddress monsterModelDoublePalette = DataAddress.fromPRG(0x22A52 - 0x10, 0x22A87 - 0x10);
    private final DataAddress monsterModelPalette = DataAddress.fromPRG(0x22A88 - 0x10, 0x22B65 - 0x10);
    private final DataAddress monsterModelDoublePaletteDataIndex = DataAddress.fromPRG(0x22BC6 - 0x10, 0x22C30 - 0x10);
    private final DataAddress monsterModelIndex = DataAddress.fromPRG(0x22C31 - 0x10, 0x22CB3 - 0x10);
    private final DataAddress monsterModelTileSet = DataAddress.fromPRG(0x22CB4 - 0x10, 0x22D02 - 0x10);
    private final DataAddress monsterModelIncrementalTileSet = DataAddress.fromPRG(0x22D03 - 0x10, 0x22D09 - 0x10);
    private final DataAddress monsterModelSize = DataAddress.fromPRG(0x22D0A - 0x10, 0x22D57 - 0x10);
    private final DataAddress monsterModelLayoutIndex = DataAddress.fromPRG(0x22EB4 - 0x10, 0x22F51 - 0x10);
    //    private final DataAddress monsterModelLayout = DataAddress.fromPRG(0x22F52 - 0x10, 0x23223 - 0x10);
    private final DataAddress monsterModelLayout = DataAddress.fromPRG(0x22F52 - 0x10, 0x232AF - 0x10);

    private final DataAddress monsterModelLayoutTileIndex = DataAddress.fromPRG(0x22D59 - 0x10, 0x22DA7 - 0x10);

    private final List<MonsterModel> monsterModels = new ArrayList<>();


    public MonsterModelImpl(@NotNull MetalMaxRe metalMaxRe) {
        super(metalMaxRe);
    }

    @Editor.Load
    public void onLoad() {
        getMonsterModels().clear();

        byte[] modelPaletteIndexes = new byte[0x83];
        byte[] modelDoublePalettes = new byte[0x1B * 0x02];
        PaletteRow[] modelPalettes = new PaletteRow[0x4A];
        byte[] modelIndexes = new byte[0x83];
        byte[] modelTileSets = new byte[0x4F];
        byte[] incrementalTileSet = new byte[0x07];
        byte[] modelSizes = new byte[0x4F];
        byte[] modelLayouts = new byte[getMonsterModelLayout().length()];
        char[] modelLayoutIndexes = new char[0x4F];
        byte[] modelLayoutTileIndexes = new byte[0x4F];


        getBuffer().get(getMonsterModelPaletteIndex(), modelPaletteIndexes);
        getBuffer().get(getMonsterModelDoublePalette(), modelDoublePalettes);
        position(getMonsterModelPalette());
        for (int i = 0; i < modelPalettes.length; i++) {
            modelPalettes[i] = new PaletteRow(getBuffer(), position());
            offsetPosition(3);
        }
        getBuffer().get(getMonsterModelIndex(), modelIndexes);
        getBuffer().get(getMonsterModelTileSet(), modelTileSets);
        getBuffer().get(getMonsterModelIncrementalTileSet(), incrementalTileSet);
        getBuffer().get(getMonsterModelSize(), modelSizes);
        getBuffer().get(getMonsterModelLayout(), modelLayouts);
        getBuffer().get(getMonsterModelLayoutTileIndex(), modelLayoutTileIndexes);

        position(getMonsterModelLayoutIndex());
        for (int i = 0; i < modelLayoutIndexes.length; i++) {
            modelLayoutIndexes[i] = getBuffer().getChar();
        }

        position(getMonsterModelDoublePaletteDataIndex());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        List<byte[]> customPaletteYsList = new ArrayList<>();
        while (getMonsterModelDoublePaletteDataIndex().range(position() - 0x10)) {
            int b;
            while ((b = getBuffer().getToInt()) != 0xFF) {
                outputStream.write(b);
            }

            customPaletteYsList.add(outputStream.toByteArray());

            outputStream.reset();
        }

//        char[] data = Arrays.copyOfRange(modelLayoutIndexes, 0x00, modelLayoutIndexes.length);
//        Arrays.sort(data);
//        for (char datum : data) {
//            int modelLayoutIndex = datum - (0x8000 + 0x0F52 - 0x10);
//            LOGGER.debug("{}",
//                    NumberR.toHex(5, getMonsterModelLayout().getStartAddress(modelLayoutIndex + 1) + 0x10)
//            );
//        }

        // 将数据分配到 MonsterModel 中
        for (int monsterId = 0; monsterId < 0x83; monsterId++) {
            // 怪物的调色板索引
            int paletteIndex = modelPaletteIndexes[monsterId] & 0xFF;
            // 怪物的模型索引
            int modelIndex = modelIndexes[monsterId] & 0xFF;

            MonsterModel monsterModel = new MonsterModel(modelSizes[modelIndex]);
            // 设置调色板
            if ((paletteIndex & 0B1000_0000) != 0x00) {
                paletteIndex &= 0x7F;
                paletteIndex <<= 1;

                // 读取双调色板的索引
                byte modelDoublePaletteIndexA = modelDoublePalettes[paletteIndex];
                byte modelDoublePaletteIndexB = modelDoublePalettes[paletteIndex + 1];

                monsterModel.setDoublePalette(modelPalettes[modelDoublePaletteIndexA & 0xFF], modelPalettes[modelDoublePaletteIndexB & 0xFF]);
            } else {
                monsterModel.setSinglePalette(modelPalettes[paletteIndex]);
            }

            // 设置模型索引
            monsterModel.setModelIndex(modelIndex);
            // 设置模型图块表(chr)索引
            if ((modelTileSets[modelIndex] & 0xF0) == 0xF0) {
                monsterModel.setChrIndex(incrementalTileSet[modelTileSets[modelIndex] & 0x0F]);
                monsterModel.setChrIndexIncremental(true);
            } else {
                monsterModel.setChrIndex(modelTileSets[modelIndex]);
            }
            // 读取并设置模型数据
            byte[] modelData;
            if (modelIndex < 0x48) {
                modelData = new byte[(int) Math.ceil((monsterModel.intWidth() * monsterModel.intHeight()) / 8.0)];
                monsterModel.setModelType(MonsterModelType.A);
            } else {
                modelData = new byte[monsterModel.intWidth() * monsterModel.intHeight()];
                monsterModel.setModelType(MonsterModelType.B);
            }
            int modelLayoutIndex = modelLayoutIndexes[modelIndex] - (0x8000 + 0x0F52 - 0x10);
            System.arraycopy(modelLayouts, modelLayoutIndex + 1, modelData, 0x00, modelData.length);
            monsterModel.setModelData(modelData);

            // 图块起始id
            monsterModel.setTileIndex(modelLayoutTileIndexes[modelIndex]);

            // 拥有自定义调色板Y值的模型
            if (customPaletteYsList.size() > modelIndex) {
                monsterModel.setCustomPaletteYs(customPaletteYsList.get(modelIndex));
            }

            // 添加怪物模型
            getMonsterModels().add(monsterModel);
        }
    }

    @Editor.Apply
    public void onApply() {
        ITextEditor textEditor = getMetalMaxRe().getEditorManager().getEditor(ITextEditor.class);

        byte[] modelPaletteIndexes = new byte[0x83];
        byte[] modelIndexes = new byte[0x83];

        PaletteRow[][] modelDoublePalettes = new PaletteRow[0x1B][0x02];
        PaletteRow[] modelPalettes = new PaletteRow[0x4A];

        byte[] modelTileSets = new byte[0x4F];
        byte[] incrementalTileSet = new byte[0x07];
        byte[] modelSizes = new byte[0x4F];
        char[] modelLayoutIndexes = new char[0x4F];
        byte[] modelLayoutTileIndexes = new byte[0x4F];

        // 基本怪物模型属性
        MonsterModel[] monsterAModels = new MonsterModel[0x48];
        MonsterModel[] monsterBModels = new MonsterModel[0x07];

        int modelALength = 0x00;
        int modelBLength = 0x00;
        for (int monsterId = 0; monsterId < getMonsterModels().size(); monsterId++) {
            MonsterModel model = getMonsterModels().get(monsterId);
            boolean hasModel = false;
            if (model.getModelType() == MonsterModelType.A) {
                for (MonsterModel monsterModel : monsterAModels) {
                    if (monsterModel == null) {
                        continue;
                    }

                    if (model.getChrIndex() == monsterModel.getChrIndex()
                        && model.isChrIndexIncremental() == monsterModel.isChrIndexIncremental()
                        && model.getModelSize() == monsterModel.getModelSize()
                        && Arrays.equals(model.getModelData(), monsterModel.getModelData())
                        && model.getTileIndex() == monsterModel.getTileIndex()
                        && Arrays.equals(model.getCustomPaletteYs(), monsterModel.getCustomPaletteYs())
                    ) {
                        // 已有的怪物模型属性
                        hasModel = true;
                        break;
                    }
                }
            } else if (model.getModelType() == MonsterModelType.B) {
                for (MonsterModel monsterModel : monsterBModels) {
                    if (monsterModel == null) {
                        continue;
                    }

                    if (model.getChrIndex() == monsterModel.getChrIndex()
                        && model.isChrIndexIncremental() == monsterModel.isChrIndexIncremental()
                        && model.getModelSize() == monsterModel.getModelSize()
                        && Arrays.equals(model.getModelData(), monsterModel.getModelData())
                        && model.getTileIndex() == monsterModel.getTileIndex()
                        && Arrays.equals(model.getCustomPaletteYs(), monsterModel.getCustomPaletteYs())
                    ) {
                        // 已有的怪物模型属性
                        hasModel = true;
                        break;
                    }
                }
            }

            if (!hasModel) {
                if (model.getModelType() == MonsterModelType.A) {
                    // 新的怪物模型属性
                    monsterAModels[modelALength++] = model;
                    // 设置怪物模型
//                    modelIndexes[monsterId] = (byte) (modelALength - 1);
                } else if (model.getModelType() == MonsterModelType.B) {
                    // 新的怪物模型属性
                    monsterBModels[modelBLength++] = model;
                    // 设置怪物模型
//                    modelIndexes[monsterId] = (byte) (modelBLength - 1);
                }
            }
        }
        // 填充空的模型
        for (int i = 0; i < monsterAModels.length; i++) {
            MonsterModel monsterModel = monsterAModels[i];
            if (monsterModel == null) {
                monsterAModels[i] = new MonsterModel((byte) 0x00);
                monsterAModels[i].setModelData(MonsterModelType.A, new byte[0x00]);
            }
        }
        for (int i = 0; i < monsterBModels.length; i++) {
            MonsterModel monsterModel = monsterBModels[i];
            if (monsterModel == null) {
                monsterBModels[i] = new MonsterModel((byte) 0x00);
                monsterBModels[i].setModelData(MonsterModelType.B, new byte[0x00]);
            }
        }

        // 将自定义调色板Y值排在前面
        Arrays.sort(monsterAModels, (o1, o2) -> {
            if (o1.getCustomPaletteYs() == null && o2.getCustomPaletteYs() == null) {
                return 0;
            }
            if (o1.getCustomPaletteYs() == null && o2.getCustomPaletteYs() != null) {
                return 1;
            }
            return -1;
        });

        // 更新怪物模型索引
        for (int monsterId = 0; monsterId < getMonsterModels().size(); monsterId++) {
            MonsterModel model = getMonsterModels().get(monsterId);
            if (model.getModelType() == MonsterModelType.A) {
                for (int modelIndex = 0; modelIndex < monsterAModels.length; modelIndex++) {
                    MonsterModel monsterModel = monsterAModels[modelIndex];
                    if (model.getChrIndex() == monsterModel.getChrIndex()
                        && model.isChrIndexIncremental() == monsterModel.isChrIndexIncremental()
                        && model.getModelSize() == monsterModel.getModelSize()
                        && Arrays.equals(model.getModelData(), monsterModel.getModelData())
                        && model.getTileIndex() == monsterModel.getTileIndex()
                        && Arrays.equals(model.getCustomPaletteYs(), monsterModel.getCustomPaletteYs())
                    ) {
                        modelIndexes[monsterId] = (byte) modelIndex;
                        break;
                    }
                }
            } else if (model.getModelType() == MonsterModelType.B) {
                for (int modelIndex = 0; modelIndex < monsterBModels.length; modelIndex++) {
                    MonsterModel monsterModel = monsterBModels[modelIndex];
                    if (model.getChrIndex() == monsterModel.getChrIndex()
                        && model.isChrIndexIncremental() == monsterModel.isChrIndexIncremental()
                        && model.getModelSize() == monsterModel.getModelSize()
                        && Arrays.equals(model.getModelData(), monsterModel.getModelData())
                        && model.getTileIndex() == monsterModel.getTileIndex()
                        && Arrays.equals(model.getCustomPaletteYs(), monsterModel.getCustomPaletteYs())
                    ) {
                        modelIndexes[monsterId] = (byte) (modelIndex + 0x48);
                        break;
                    }
                }
            }
        }

        // 将两种模型数据类型合并
        MonsterModel[] monsterModels = new MonsterModel[0x48 + 0x07];
        System.arraycopy(monsterAModels, 0x00, monsterModels, 0x00, monsterAModels.length);
        System.arraycopy(monsterBModels, 0x00, monsterModels, 0x48, monsterBModels.length);

        int chrIncrementalLength = 0x00;
        // 所有模型数据
        Map<Integer, byte[]> modelData = new HashMap<>();
        // 将属性转换为数据
        for (int modelIndex = 0; modelIndex < monsterModels.length; modelIndex++) {
            MonsterModel monsterModel = monsterModels[modelIndex];
            if (monsterModel == null) {
                // 模型数量少于原版数量
                // 暂不处理

                modelTileSets[modelIndex] = 0x00;
                modelSizes[modelIndex] = 0x00;
                modelLayoutTileIndexes[modelIndex] = 0x00;
                modelLayoutIndexes[modelIndex] = 0x8000 + 0x0F52 - 0x10;
                continue;
            }
            // 将自增chr提取出来
            if (monsterModel.isChrIndexIncremental()) {
                boolean hasChr = false;
                for (int i = 0; i < incrementalTileSet.length; i++) {
                    if (incrementalTileSet[i] == monsterModel.getChrIndex()) {
                        // 已有自增的chr
                        modelTileSets[modelIndex] = (byte) (0xF0 | i);
                        hasChr = true;
                        break;
                    }
                }
                if (!hasChr) {
                    incrementalTileSet[chrIncrementalLength] = monsterModel.getChrIndex();
                    modelTileSets[modelIndex] = (byte) (0xF0 | chrIncrementalLength);

                    chrIncrementalLength++;
                }
            } else {
                modelTileSets[modelIndex] = monsterModel.getChrIndex();
            }
            modelSizes[modelIndex] = monsterModel.getModelSize();
            modelLayoutTileIndexes[modelIndex] = monsterModel.getTileIndex();

            modelData.put(Arrays.hashCode(monsterModel.getModelData()), monsterModel.getModelData());
        }
        // 更新模型索引
        int layoutIndex = 0x8000 + 0x0F52 - 0x10;
        List<Map.Entry<Integer, byte[]>> sortModelData = modelData.entrySet().stream()
                .sorted(Comparator.comparingInt(o -> o.getValue().length))
                .toList();


        for (Map.Entry<Integer, byte[]> entry : sortModelData) {
            for (int modelIndex = 0; modelIndex < monsterModels.length; modelIndex++) {
                MonsterModel monsterModel = monsterModels[modelIndex];
                if (monsterModel == null) {
                    continue;
                }
                if (Arrays.hashCode(monsterModel.getModelData()) == entry.getKey()) {
                    modelLayoutIndexes[modelIndex] = (char) (layoutIndex - 1);
                }
            }
            layoutIndex += entry.getValue().length;
        }

        // 整合调色板
        int paletteLength = 0;          // 单调色板
        int doublePaletteLength = 0;    // 双调色板
        byte[] modelDoublePaletteIndexes = new byte[0x1B * 0x02];
        for (int monsterId = 0; monsterId < getMonsterModels().size(); monsterId++) {
            MonsterModel model = getMonsterModels().get(monsterId);
            if (model.isSinglePalette()) {
                // 单调色板
                PaletteRow singlePalette = model.getSinglePalettes();
                boolean hasPalette = false;
                // 查找是否存在调色板
                for (int j = 0; j < modelPalettes.length; j++) {
                    if (Objects.equals(singlePalette, modelPalettes[j])) {
                        hasPalette = true;

                        // 已存在调色板，设置调色板索引
                        modelPaletteIndexes[monsterId] = (byte) j;
                        break;
                    }
                }
                if (!hasPalette) {
                    if (paletteLength < modelPalettes.length) {
                        // 添加新的调色板
                        modelPalettes[paletteLength++] = singlePalette;
                        modelPaletteIndexes[monsterId] = (byte) (paletteLength - 1);
                    } else {
                        // 没有空间添加新的调色板
                        LOGGER.error("怪物模型编辑器：调色板空间不足！无法添加 {} 的调色板 {}。", textEditor.getMonsterName(monsterId), singlePalette);
                    }
                }
            } else {
                // 双调色板
                PaletteRow[] doublePalettes = model.getDoublePalettes();
                byte[] palettes = {-1, -1};
                // 查找是否存在调色板
                for (int j = 0; j < modelPalettes.length; j++) {
                    PaletteRow modelPalette = modelPalettes[j];
                    if (modelPalette == null) {
                        continue;
                    }

                    if (palettes[0x00] == -1 && Objects.equals(modelPalette, doublePalettes[0x00])) {
                        palettes[0x00] = (byte) j;
                    }
                    if (palettes[0x01] == -1 && Objects.equals(modelPalette, doublePalettes[0x01])) {
                        palettes[0x01] = (byte) j;
                    }

                    if (palettes[0x00] != -1 && palettes[0x01] != -1) {
                        // 两个调色板已找到
                        break;
                    }
                }
                if (palettes[0x00] == -1) {
                    // 新的调色板
                    modelPalettes[paletteLength++] = doublePalettes[0x00];
                    palettes[0x00] = (byte) (paletteLength - 1);
                }
                if (palettes[0x01] == -1) {
                    // 新的调色板
                    modelPalettes[paletteLength++] = doublePalettes[0x01];
                    palettes[0x01] = (byte) (paletteLength - 1);
                }

                boolean hasPalette = false;
                for (int i = 0; i < 0x1B; i++) {
                    if (modelDoublePaletteIndexes[i * 0x02] == palettes[0x00]
                        && modelDoublePaletteIndexes[(i * 0x02) + 1] == palettes[0x01]) {
                        // 已存在的双调色板
                        modelPaletteIndexes[monsterId] = (byte) (0B1000_0000 | i);
                        hasPalette = true;
                        break;
                    }
                }
                if (!hasPalette) {
                    if (doublePaletteLength < modelDoublePalettes.length) {
                        // 添加新的调色板
                        modelDoublePaletteIndexes[doublePaletteLength * 0x02] = palettes[0x00];
                        modelDoublePaletteIndexes[(doublePaletteLength * 0x02) + 1] = palettes[0x01];
                        modelPaletteIndexes[monsterId] = (byte) (0B1000_0000 | doublePaletteLength);
                        doublePaletteLength++;
                    } else {
                        // 没有空间添加新的双调色板
                        LOGGER.error("怪物模型编辑器：双调色板空间不足！无法添加 {} 的双调色板 {}。", textEditor.getMonsterName(monsterId), Arrays.toString(doublePalettes));
                    }
                }
            }
        }

        if (modelPalettes.length - paletteLength > 0x00) {
            LOGGER.info("剩余{}个空闲调色板", modelPalettes.length - paletteLength);
        }
        if ((modelDoublePaletteIndexes.length / 0x02) - doublePaletteLength > 0x00) {
            LOGGER.info("剩余{}个空闲双调色板", (modelDoublePaletteIndexes.length / 0x02) - doublePaletteLength);
        }

        // 写入所有怪物的调色板索引
        getBuffer().put(getMonsterModelPaletteIndex(), modelPaletteIndexes);
        // 写入怪物使用的双调色板索引集
        getBuffer().put(getMonsterModelDoublePalette(), modelDoublePaletteIndexes);
        // 写入所有怪物的调色板
        position(getMonsterModelPalette());
        for (PaletteRow modelPalette : modelPalettes) {
            if (modelPalette != null) {
                getBuffer().put(modelPalette.toPalette());
            }
        }
        // 写入怪物使用的模型索引
        getBuffer().put(getMonsterModelIndex(), modelIndexes);
        // 写入怪物模型使用的chr
        getBuffer().put(getMonsterModelTileSet(), modelTileSets);
        // 写入怪物自增chr
        getBuffer().put(getMonsterModelIncrementalTileSet(), incrementalTileSet);
        // 写入怪物模型的大小
        getBuffer().put(getMonsterModelSize(), modelSizes);
        // 写入所有怪物模型数据
        position(getMonsterModelLayout());

        ArrayList<Map.Entry<Integer, byte[]>> modelLayouts = new ArrayList<>(sortModelData);
        ArrayList<byte[]> newModelLayouts = new ArrayList<>();
        for (int i = 0; i < modelLayouts.size(); i++) {
            byte[] modelLayout = modelLayouts.get(i).getValue();
            if (modelLayout != null) {
                // 在已有的模型数据中判断是否包含当前模型数据
                boolean hasModel = false;
                for (int j = 0; j < newModelLayouts.size(); j++) {
                    byte[] bytes = newModelLayouts.get(j);
                    if (bytes.length < modelLayout.length) {
                        // 比当前模型数据小，跳过
                        continue;
                    }
                    int k = 0; // 该数据在已有模型数据中的索引
                    int l = 0; // 连续匹配的数量，最终需要等于当前模型数据大小
                    for (; bytes.length - k >= modelLayout.length; k++) { // 循环保证剩余数量大于等于当前模型数据数量
                        if (bytes[k] == modelLayout[l]) {
                            l++;
                            if (modelLayout.length == l) {
                                // 匹配完成
                                break;
                            }
                        } else if (l != 0) {
                            l = 0;
                        }
                    }

                    if (modelLayout.length == l) {
                        // 匹配完成
                        // 更新模型数据索引
                        modelLayoutIndexes[i] = (char) (modelLayoutIndexes[j] + k);
                        hasModel = true;
                        LOGGER.info("模型数据({})包含模型数据({})", NumberR.toHex(2, j), NumberR.toHex(2, i));
                        break;
                    }
                }

                if (hasModel) {
                    // 包含，跳过写入
                    continue;
                }
                if (getMonsterModelLayout().range((position() - 0x10) + modelLayout.length)) {
                    getBuffer().put(modelLayout);
                    newModelLayouts.add(modelLayout);
                } else {
                    LOGGER.error("模型索引({})写入失败，没有多余的空间写入{}/{}", NumberR.toHex(2, i),
                            modelLayout.length, getMonsterModelLayout().getEndAddress() - (position() - 0x10));
                }

            }
        }
        // 写入怪物图像起始id
        getBuffer().put(getMonsterModelLayoutTileIndex(), modelLayoutTileIndexes);

        // 写入所有怪物模型数据索引
        position(getMonsterModelLayoutIndex());
        for (char modelLayoutIndex : modelLayoutIndexes) {
            getBuffer().putChar(NumberR.toChar(modelLayoutIndex));
        }

        // 写入自定义调色板Y值
        List<byte[]> customPaletteYsList = new ArrayList<>();
        for (MonsterModel monsterModel : monsterModels) {
            if (monsterModel == null || monsterModel.getCustomPaletteYs() == null) {
                break;
            }
            customPaletteYsList.add(monsterModel.getCustomPaletteYs());
        }
        position(getMonsterModelDoublePaletteDataIndex());
        int ysLength = getMonsterModelDoublePaletteDataIndex().length();
        for (byte[] bytes : customPaletteYsList) {
            ysLength -= bytes.length + 1;
            if (ysLength >= 0x00) {
                getBuffer().put(bytes);
                getBuffer().put(0xFF);
            } else {
                LOGGER.error("怪物模型编辑器：自定义调色板Y值数据无法写入：{}", NumberR.toHexString(bytes));
            }
        }
    }

    public List<MonsterModel> getMonsterModels() {
        return monsterModels;
    }

    public MonsterModel getMonsterModel(int monsterId) {
        return monsterModels.get(monsterId);
    }

    public DataAddress getMonsterModelIndex() {
        return monsterModelIndex;
    }

    public DataAddress getMonsterModelPaletteIndex() {
        return monsterModelPaletteIndex;
    }

    public DataAddress getMonsterModelDoublePalette() {
        return monsterModelDoublePalette;
    }

    public DataAddress getMonsterModelPalette() {
        return monsterModelPalette;
    }

    public DataAddress getMonsterModelDoublePaletteDataIndex() {
        return monsterModelDoublePaletteDataIndex;
    }

    public DataAddress getMonsterModelTileSet() {
        return monsterModelTileSet;
    }

    public DataAddress getMonsterModelIncrementalTileSet() {
        return monsterModelIncrementalTileSet;
    }

    public DataAddress getMonsterModelSize() {
        return monsterModelSize;
    }

    public DataAddress getMonsterModelLayoutIndex() {
        return monsterModelLayoutIndex;
    }

    public DataAddress getMonsterModelLayout() {
        return monsterModelLayout;
    }

    public DataAddress getMonsterModelLayoutTileIndex() {
        return monsterModelLayoutTileIndex;
    }
}
