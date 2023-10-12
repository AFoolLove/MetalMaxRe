package me.afoolslove.metalmaxre.patch;

import com.google.gson.*;
import me.afoolslove.metalmaxre.editors.text.TextBuilder;
import me.afoolslove.metalmaxre.utils.DataAddress;
import me.afoolslove.metalmaxre.utils.NumberR;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PatchManagerImpl implements IPatchManager {
    public static Gson GSON = new GsonBuilder()
            .registerTypeAdapter(DataAddress.class, (JsonSerializer<DataAddress>) (src, typeOfSrc, context) -> {
                // 序列化 DataAddress
                /*
                 * PRG-00000-00000
                 */
                return new JsonPrimitive("%s-%05X-%05X".formatted(src.getType(), src.getStartAddress(), src.getEndAddress()));
            })
            .registerTypeAdapter(DataAddress.class, (JsonDeserializer<DataAddress>) (json, typeOfT, context) -> {
                // 反序列化 DataAddress
                String[] split = json.getAsString().split("-");
                DataAddress.Type type = DataAddress.Type.valueOf(split[0]);
                int start = Integer.parseInt(split[1], 16);
                int end = Integer.parseInt(split[2], 16);
                return DataAddress.from(type, start, end);
            })

            .registerTypeAdapter(PatchSegmentImpl.class, (JsonSerializer<PatchSegmentImpl>) (src, typeOfSrc, context) -> {
                /*
                 * 序列化 PatchSegmentImpl
                 * {
                 *     "address": "PRG-00000-00000",
                 *     "rawData": "00010203",
                 *     "patchData": "03020100"
                 * }
                 */
                JsonObject object = new JsonObject();
                object.add("address", context.serialize(src.getAddress(), DataAddress.class));
                object.addProperty("rawData", NumberR.toPlainHexString(src.getRawData()));
                object.addProperty("patchData", NumberR.toPlainHexString(src.getPatchData()));
                return object;
            })
            .registerTypeAdapter(PatchSegmentImpl.class, (JsonDeserializer<PatchSegmentImpl>) (json, typeOfT, context) -> {
                // 反序列化 PatchSegmentImpl
                JsonObject object = json.getAsJsonObject();
                DataAddress address = context.deserialize(object.get("address"), DataAddress.class);
                byte[] rawData = TextBuilder.readBracketHexToBytes(object.get("rawData").getAsString().toCharArray());
                byte[] patchData = TextBuilder.readBracketHexToBytes(object.get("patchData").getAsString().toCharArray());
                return new PatchSegmentImpl(address, rawData, patchData);
            })
            .registerTypeAdapter(PatchSegmentWrapperImpl.class, (JsonSerializer<PatchSegmentWrapperImpl>) (src, typeOfSrc, context) -> {
                // 序列化 PatchSegmentWrapperImpl
                JsonObject object = new JsonObject();
                object.addProperty("description", src.getDescription());

                JsonArray value = new JsonArray();
                for (IPatchSegment patchSegment : src.getPatchSegments()) {
                    value.add(context.serialize(patchSegment));
                }
                object.add("value", value);
                return object;
            })
            .registerTypeAdapter(PatchSegmentWrapperImpl.class, (JsonDeserializer<PatchSegmentWrapperImpl>) (json, typeOfT, context) -> {
                // 反序列化 PatchSegmentWrapperImpl
                JsonObject object = json.getAsJsonObject();
                String description = object.get("description").getAsString();

                JsonArray value = object.get("value").getAsJsonArray();
                List<IPatchSegment> patchSegments = new ArrayList<>();
                for (JsonElement element : value) {
                    patchSegments.add(context.deserialize(element, PatchSegmentImpl.class));
                }
                return new PatchSegmentWrapperImpl(description, patchSegments);
            })

            .registerTypeAdapter(RomPatchImpl.class, (JsonSerializer<RomPatchImpl>) (src, typeOfSrc, context) -> {
                // 序列化 RomPatchImpl
                /*
                 * {
                 *     "key": "test",
                 *     "description": "测试描述",
                 *     "preKey": null,
                 *     "segments": {
                 *          "chinese,japanese": [
                 *              {
                 *                  "description": "描述",
                 *                  "value": {
                 *                      "address": "PRG-00000-00000",
                 *                      "rawData": "00010203",
                 *                      "patchData": "03020100"
                 *                  }
                 *              }
                 *          ]
                 *     }
                 * }
                 *
                 */
                JsonObject object = new JsonObject();
                object.addProperty("key", src.getKey());
                object.addProperty("description", src.getDescription());
                object.addProperty("preKey", src.getPreKey());

                JsonObject patches = new JsonObject();

                for (Map.Entry<String, List<IPatchSegmentWrapper>> entry : src.getPatchSegments().entrySet()) {
                    patches.add(entry.getKey(), context.serialize(entry.getValue()));
                }

                object.add("patches", patches);
                return object;
            })
            .registerTypeAdapter(RomPatchImpl.class, (JsonDeserializer<RomPatchImpl>) (json, typeOfT, context) -> {
                // 反序列化 RomPatchImpl
                JsonObject object = json.getAsJsonObject();
                String key = object.get("key").getAsString();
                String description = object.has("description") ? object.get("description").getAsString() : null;
                String preKey = object.has("object") ? object.get("preKey").getAsString() : null;
                JsonObject segmentsObject = object.get("patches").getAsJsonObject();

                Map<String, List<IPatchSegmentWrapper>> segments = new HashMap<>();
                for (Map.Entry<String, JsonElement> entry : segmentsObject.entrySet()) {
                    // 不同版本但相同的补丁
                    String[] versions = entry.getKey().split(",");
                    for (String version : versions) {
                        List<IPatchSegmentWrapper> patchSegmentWrappers = new ArrayList<>();
                        for (JsonElement jsonElement : entry.getValue().getAsJsonArray()) {
                            patchSegmentWrappers.add(context.deserialize(jsonElement, PatchSegmentWrapperImpl.class));
                        }
                        segments.put(version, patchSegmentWrappers);
                    }
                }
                return new RomPatchImpl(key, description, preKey, segments);
            })
            .create();

    private final Map<String, IRomPatch> patches = new HashMap<>();

    @Override
    public @NotNull Map<String, IRomPatch> getPatches() {
        return patches;
    }

    @Override
    public @Nullable IRomPatch getPatch(String key) {
        return patches.get(key);
    }

    @Override
    public void putPatch(@NotNull IRomPatch patch) {
        patches.put(patch.getKey(), patch);
    }

    @Override
    public IRomPatch removePatch(String key) {
        return patches.remove(key);
    }
}
