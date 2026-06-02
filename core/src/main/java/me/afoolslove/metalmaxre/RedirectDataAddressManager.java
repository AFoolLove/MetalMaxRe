package me.afoolslove.metalmaxre;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.afoolslove.metalmaxre.editors.IRomEditor;
import me.afoolslove.metalmaxre.utils.DataAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 重定向编辑器数据地址
 */
public class RedirectDataAddressManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedirectDataAddressManager.class);

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(DataAddress.class, DataAddress.SERIALIZER)
            .registerTypeAdapter(DataAddress.class, DataAddress.DESERIALIZER)
            .create();


    /**
     * K: version
     * V:
     * K: editorId
     * V: editorDataAddress
     */
    private final Map<String, Map<String, Map<String, Object>>> redirectDataAddresses = new HashMap<>();

    public void load(InputStream inputStream) {
        getRedirectDataAddresses().clear();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            JsonObject jsonObject = GSON.fromJson(reader, JsonObject.class);
            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                String version = entry.getKey();
                RomVersion romVersion = RomVersion.getVersion(version, null);
                if (romVersion == null) {
                    // 未知的版本
                    continue;
                }
                Map<String, Map<String, Object>> dataAddresses = new HashMap<>();

                for (Map.Entry<String, JsonElement> elementEntry : entry.getValue().getAsJsonObject().entrySet()) {
                    String editorId = elementEntry.getKey();
                    Map<String, Object> editorDataAddresses = new HashMap<>();
                    for (Map.Entry<String, JsonElement> addresses : elementEntry.getValue().getAsJsonObject().entrySet()) {
                        editorDataAddresses.put(addresses.getKey(), GSON.fromJson(addresses.getValue(), DataAddress.class));
                    }
                    dataAddresses.put(editorId, editorDataAddresses);
                }

                getRedirectDataAddresses().put(version, dataAddresses);
            }

        } catch (IOException e) {
            LOGGER.error("加载重定向数据地址失败", e);
        }
    }

    public void apply(MetalMaxRe metalMaxRe) {
        RomVersion romVersion = metalMaxRe.getBuffer().getVersion();
        Map<String, Map<String, Object>> redirectDataAddresses = getRedirectDataAddresses(romVersion.getId());
        if (redirectDataAddresses == null || redirectDataAddresses.isEmpty()) {
            return;
        }
        for (IRomEditor value : metalMaxRe.getEditorManager().getEditors().values()) {
            Map<String, Object> addresses = redirectDataAddresses.get(value.getId());
            if (addresses == null || addresses.isEmpty()) {
                continue;
            }
            // 替换数据地址
            for (Map.Entry<String, Object> entry : addresses.entrySet()) {
                value.putDataAddress(entry.getKey(), entry.getValue());
            }
        }
    }

    public Map<String, Map<String, Map<String, Object>>> getRedirectDataAddresses() {
        return redirectDataAddresses;
    }

    public Map<String, Map<String, Object>> getRedirectDataAddresses(String version) {
        return getRedirectDataAddresses().get(version);
    }

    public Map<String, Object> getRedirectDataAddresses(String version, String editorId) {
        Map<String, Map<String, Object>> dataAddresses = getRedirectDataAddresses(version);
        return dataAddresses == null ? null : dataAddresses.get(editorId);
    }
}
