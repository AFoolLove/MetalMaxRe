package me.afoolslove.metalmaxre.editors.sprite.script;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.RomBufferWrapperAbstractEditor;
import me.afoolslove.metalmaxre.editors.Editor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SpriteScriptEditorImpl extends RomBufferWrapperAbstractEditor implements ISpriteScriptEditor {
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

    private final DataAddress spriteScriptIndexAddress;
    private final DataAddress spriteScriptAddress;

    private final Map<Integer, SpriteScript> spriteScripts = new HashMap<>();

    private final Map<Integer, SpriteScriptAction> actions = new HashMap<>();


    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe) {
        this(metalMaxRe,
                DataAddress.fromPRG(0x25177 - 0x10, 0x252C8 - 0x10),
                DataAddress.fromPRG(0x252C9 - 0x10, 0x2600F - 0x10));
    }

    public SpriteScriptEditorImpl(@NotNull MetalMaxRe metalMaxRe,
                                  @NotNull DataAddress spriteScriptIndexAddress,
                                  @NotNull DataAddress spriteScriptAddress) {
        super(metalMaxRe, false);
        this.spriteScriptAddress = spriteScriptAddress;
        this.spriteScriptIndexAddress = spriteScriptIndexAddress;

        byte[] bytes = ResourceManager.getAsBytes("/sprite_script_actions.json");
        SpriteScriptAction[] spriteScriptActions = SPRITE_SCRIPT_GSON.fromJson(new String(bytes, StandardCharsets.UTF_8), new TypeToken<>() {
        });
        for (SpriteScriptAction spriteScriptAction : spriteScriptActions) {
            actions.put(spriteScriptAction.getCode(), spriteScriptAction);
        }
    }

    @Editor.Load
    public void onLoad() {
        getSpriteScripts().clear();

        char[] indexes = new char[getSpriteScriptMaxCount()];
        position(getSpriteScriptIndexAddress());
        for (int i = 0; i < indexes.length; i++) {
            indexes[i] = getBuffer().getChar();
        }


    }

    @Editor.Apply
    public void onApply() {

    }

    @Override
    public Map<Integer, SpriteScriptAction> getSpriteScriptActions() {
        return actions;
    }

    @Override
    public Map<Integer, SpriteScript> getSpriteScripts() {
        return spriteScripts;
    }

    @Override
    public DataAddress getSpriteScriptIndexAddress() {
        return spriteScriptIndexAddress;
    }

    @Override
    public DataAddress getSpriteScriptAddress() {
        return spriteScriptAddress;
    }
}
