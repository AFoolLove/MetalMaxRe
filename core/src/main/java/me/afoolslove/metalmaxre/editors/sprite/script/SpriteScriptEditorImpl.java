package me.afoolslove.metalmaxre.editors.sprite.script;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SpriteScriptEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteScriptEditor {
    public static final Logger LOGGER = LoggerFactory.getLogger(SpriteScriptEditorImpl.class);

    public static final Gson SPRITE_SCRIPT_GSON = new GsonBuilder().registerTypeAdapter(SpriteScriptAction.class, new JsonDeserializer<>() {
        private final Pattern HEX_BYTE = Pattern.compile("^[0-9a-fA-F]{1,2}$");

        @Override
        public SpriteScriptAction deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            if (!jsonObject.has("code")) {
                // 没有 code
                return null;
            }
            String code = jsonObject.get("code").getAsString();
            if (!HEX_BYTE.matcher(code).find()) {
                // code 格式错误
                return null;
            }

            SpriteScriptAction spriteScriptAction = new SpriteScriptAction(Integer.parseInt(code, 16));
            if (jsonObject.has("type")) {
                spriteScriptAction.setType(SpriteScriptActionType.valueOf(jsonObject.get("type").getAsString().toUpperCase()));
            }

            if (jsonObject.has("name")) {
                spriteScriptAction.setName(jsonObject.get("name").getAsString());
            }

            if (jsonObject.has("length")) {
                spriteScriptAction.setLength(jsonObject.get("length").getAsInt());
            }

            if (jsonObject.has("format")) {
                spriteScriptAction.setFormat(jsonObject.get("format").getAsString());
            }
            if (jsonObject.has("formatText")) {
                spriteScriptAction.setFormatText(jsonObject.get("formatText").getAsString());
            }

            if (jsonObject.has("shortDescription")) {
                spriteScriptAction.setShortDescription(jsonObject.get("shortDescription").getAsString());
                spriteScriptAction.setDetailedDescription(spriteScriptAction.getShortDescription());
            }

            if (jsonObject.has("detailedDescription")) {
                spriteScriptAction.setDetailedDescription(jsonObject.get("detailedDescription").getAsString());
            }

            if (jsonObject.has("gotoIndex")) {
                spriteScriptAction.setGotoIndex(jsonObject.get("gotoIndex").getAsInt());
            }

            if (jsonObject.has("notLeaf")) {
                spriteScriptAction.setNotLeaf(jsonObject.get("notLeaf").getAsBoolean());
            }
            return spriteScriptAction;
        }
    }).create();

    /**
     * 动态脚本索引地址
     */
    public static final String DYNAMIC_SCRIPT_INDEX_ADDRESS = "dynamicScriptIndex";
    /**
     * 动态脚本地址
     */
    public static final String DYNAMIC_SCRIPT_ADDRESS = "dynamicScript";

    /**
     * 静态脚本索引地址
     */
    public static final String STATIC_SCRIPT_INDEX_ADDRESS = "staticScriptIndex";
    /**
     * 静态脚本地址
     */
    public static final String STATIC_SCRIPT_ADDRESS = "staticScript";


    private final Map<Integer, SpriteScriptAction> actions = new HashMap<>();
    private final Map<Integer, byte[]> dynamicScripts = new HashMap<>();
    private final Map<Integer, byte[]> staticScripts = new HashMap<>();


    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x25177 - 0x10, 0x252C8 - 0x10),
                DataAddress.fromPRG(0x252C9 - 0x10, 0x2600F - 0x10),
                DataAddress.fromPRG(0x27389 - 0x10, 0x27460 - 0x10),
                DataAddress.fromPRG(0x27461 - 0x10, 0x27911 - 0x10)
        );
    }

    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                  @NotNull DataAddress dynamicScriptIndexAddress,
                                  @NotNull DataAddress dynamicScriptAddress,
                                  @NotNull DataAddress staticScriptIndexAddress,
                                  @NotNull DataAddress staticScriptAddress) {
        super(metalMaxRe, false);
        putDataAddress(DYNAMIC_SCRIPT_INDEX_ADDRESS, dynamicScriptIndexAddress);
        putDataAddress(DYNAMIC_SCRIPT_ADDRESS, dynamicScriptAddress);
        putDataAddress(STATIC_SCRIPT_INDEX_ADDRESS, staticScriptIndexAddress);
        putDataAddress(STATIC_SCRIPT_ADDRESS, staticScriptAddress);

        byte[] bytes = ResourceManager.getAsBytes("/sprite_script_actions.json");
        SpriteScriptAction[] spriteScriptActions = SPRITE_SCRIPT_GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), new TypeToken<>() {
        });
        for (SpriteScriptAction spriteScriptAction : spriteScriptActions) {
            actions.put(spriteScriptAction.getCode(), spriteScriptAction);
        }
    }

    @Editor.Load
    public void onLoad() {
        getDynamicScripts().clear();
        getStaticScripts().clear();

        loadScripts(DYNAMIC_SCRIPT_INDEX_ADDRESS, DYNAMIC_SCRIPT_ADDRESS, getDynamicScripts(), getDynamicScriptMaxCount());
        loadScripts(STATIC_SCRIPT_INDEX_ADDRESS, STATIC_SCRIPT_ADDRESS, getStaticScripts(), getStaticScriptMaxCount());
    }

    /**
     * 加载脚本数据（动态/静态共用）
     *
     * @param indexAddressKey 索引地址key
     * @param dataAddressKey  数据地址key
     * @param dataMap         存储结果的Map
     * @param maxCount        最大数量
     */
    private void loadScripts(@NotNull String indexAddressKey,
                             @NotNull String dataAddressKey,
                             @NotNull Map<Integer, byte[]> dataMap,
                             int maxCount) {
        // 读取原始索引
        char[] indexes = new char[maxCount];
        position(getDataAddress(indexAddressKey));
        getBuffer().getCharArray(indexes);

        // 排序后的索引副本
        char[] sorted = Arrays.copyOf(indexes, indexes.length);
        Arrays.sort(sorted);

        // 计算每个原始索引在排序数组中的排名
        int[] rank = new int[maxCount];
        for (int i = 0; i < maxCount; i++) {
            rank[i] = Arrays.binarySearch(sorted, indexes[i]);
        }

        // 根据相邻排序索引的差值计算每个脚本的长度并读取数据
        int bankStartAddress = getDataAddress(dataAddressKey).getBankStartAddress() + getBuffer().getHeader().getPrgRomStart();
        int endOffset = getDataAddress(dataAddressKey).getBankEndOffset() + 0x8000;

        for (int i = 0; i < maxCount; i++) {
            int nextOffset = (rank[i] + 1 < maxCount) ? sorted[rank[i] + 1] : endOffset;
            int length = nextOffset - sorted[rank[i]];
            if (length >= 0) {
                byte[] bytes = new byte[length];
                getBuffer().get(bankStartAddress + indexes[i] - 0x8000, bytes);
                dataMap.put(i, bytes);
            }
        }
    }

    @Editor.Apply
    public void onApply() {
        applyScripts(DYNAMIC_SCRIPT_INDEX_ADDRESS, DYNAMIC_SCRIPT_ADDRESS, getDynamicScripts(), getDynamicScriptMaxCount());
        applyScripts(STATIC_SCRIPT_INDEX_ADDRESS, STATIC_SCRIPT_ADDRESS, getStaticScripts(), getStaticScriptMaxCount());
    }

    /**
     *
     */
    private void applyScripts(@NotNull String indexAddressKey,
                              @NotNull String dataAddressKey,
                              @NotNull Map<Integer, byte[]> dataMap,
                              int maxCount) {
        final int maxLength = dataMap.values().stream().mapToInt(bytes -> bytes.length).sum();

        // 储存新的索引
        char[] indexes = new char[maxCount];

        // 用于储存新的数据
        ByteBuffer dataBuffer = ByteBuffer.allocate(maxLength);

        byte[][] sortData = new byte[maxCount][];
        for (Map.Entry<Integer, byte[]> entry : dataMap.entrySet()) {
            sortData[entry.getKey()] = entry.getValue();
        }
        // 按数组长度大到小排序
        Arrays.sort(sortData, (o1, o2) -> o2.length - o1.length);
        DataAddress dataAddress = getDataAddress(dataAddressKey);
        int bankOffset = dataAddress.getBankOffset();
        // 通过遍历数据获取对应的索引，将索引目标值设置为数据起始地址
        for (int i = 0; i < maxCount; i++) {
            for (Map.Entry<Integer, byte[]> entry : dataMap.entrySet()) {
                if (entry.getValue() == sortData[i]) {
                    indexes[entry.getKey()] = (char) (dataBuffer.position() + 0x8000 + bankOffset);
                    dataBuffer.put(entry.getValue());
                    break;
                }
            }
        }
        position(getDataAddress(indexAddressKey));
        getBuffer().putCharsR(indexes);

        position(dataAddress);
        getBuffer().put(dataBuffer.array(), 0, Math.min(dataBuffer.position(), dataAddress.length()));

        int end = dataAddress.getEndAddress(-position() + 0x10 + 1);
        if (end >= 0) {
            if (end > 0) {
                // 使用0x00填充未使用的数据
                byte[] fillBytes = new byte[end];
                Arrays.fill(fillBytes, (byte) 0x00);
                getBuffer().put(fillBytes);
            }
            LOGGER.info("精灵脚本编辑器：{}剩余{}个空闲字节", dataAddressKey, end);
        } else {
            LOGGER.error("精灵脚本编辑器：{}错误！超出了数据上限{}字节", dataAddressKey, -end);
        }
    }

    @Override
    public Map<Integer, byte[]> getDynamicScripts() {
        return dynamicScripts;
    }

    @Override
    public Map<Integer, byte[]> getStaticScripts() {
        return staticScripts;
    }

    @Override
    public Map<Integer, SpriteScriptAction> getSpriteScriptActions() {
        return actions;
    }
}
