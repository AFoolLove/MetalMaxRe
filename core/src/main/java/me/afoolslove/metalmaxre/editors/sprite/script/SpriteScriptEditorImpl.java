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
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 精灵脚本编辑器
 *
 * @author AFoolLove
 */
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

        // 加载动态脚本
        loadIndexedData(DYNAMIC_SCRIPT_INDEX_ADDRESS, DYNAMIC_SCRIPT_ADDRESS, getDynamicScripts(), getDynamicScriptMaxCount());
        // 加载静态脚本
        loadIndexedData(STATIC_SCRIPT_INDEX_ADDRESS, STATIC_SCRIPT_ADDRESS, getStaticScripts(), getStaticScriptMaxCount());
    }

    @Editor.Apply
    public void onApply() {
        // 写入动态脚本
        int end = applyIndexedData(DYNAMIC_SCRIPT_INDEX_ADDRESS, DYNAMIC_SCRIPT_ADDRESS, getDynamicScripts(), getDynamicScriptMaxCount(), (byte) 0x00);
        if (end >= 0) {
            LOGGER.info("脚本编辑器：动态脚本数据剩余{}个空闲字节", end);
        } else {
            LOGGER.error("脚本编辑器：动态脚本数据错误！超出了数据上限{}字节", -end);
        }
        // 写入静态脚本
        end = applyIndexedData(STATIC_SCRIPT_INDEX_ADDRESS, STATIC_SCRIPT_ADDRESS, getStaticScripts(), getStaticScriptMaxCount(), (byte) 0x00);
        if (end >= 0) {
            LOGGER.info("脚本编辑器：静态脚本数据剩余{}个空闲字节", end);
        } else {
            LOGGER.error("脚本编辑器：静态脚本数据错误！超出了数据上限{}字节", -end);
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
