package me.afoolslove.metalmaxre.editor;

import me.afoolslove.metalmaxre.GameHeader;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

/**
 * 注：使用该编辑器需要启用 {@link GameHeader#isTrained()}，未启用不会进行，并且会自动扩容
 * <p>
 * * 默认禁用
 * <p>
 * trained 数据格式：
 * 0x000-0x007 long 修改时间戳 {@code System.currentTimeMillis()}
 * 0x008-0x009 char HACK主程序的版本
 * 0x00A-0x200 保留
 *
 * @author AFoolLove
 */
public class PatchEditor extends AbstractEditor<PatchEditor> {
    private final ByteBuffer trained = ByteBuffer.allocate(0x200);

    public PatchEditor() {
        setEnabled(true);
    }

    public PatchEditor(boolean enable) {
        setEnabled(enable);
    }

    @Override
    public boolean onRead(@NotNull ByteBuffer buffer) {
//        if (!getHeader().isTrained()) {
//            return true;
//        }
        get(buffer, 0x010, trained.array());

        try {
            // 导出补丁
            try (InputStream patchesJson = getClass().getResourceAsStream("/patches/patches.json")) {
                if (patchesJson != null) {
                    String source = new String(patchesJson.readAllBytes());
                    JSONObject jsonObject = new JSONObject(source);

                    // 如果主程序补丁不存在则导出主程序补丁
                    File mainPatch = new File(jsonObject.getJSONObject("main_patch").getString("path"));
                    if (mainPatch.getParentFile().mkdirs() || !mainPatch.exists()) {
                        try (InputStream mainPatchInputStream = getClass().getResourceAsStream("/patches/" + mainPatch.getName())) {
                            if (mainPatchInputStream != null) {
                                mainPatchInputStream.transferTo(Files.newOutputStream(mainPatch.getCanonicalFile().toPath(), StandardOpenOption.CREATE_NEW));
                            }
                        }
                    }

                    // 导出不存在的补丁
                    JSONArray patches = jsonObject.getJSONArray("patches");
                    for (Object patchObject : patches) {
                        if (patchObject instanceof JSONObject patch) {
//                            String name = patch.getString("name");
                            File path = new File(patch.getString("path"));
                            if (!path.exists()) {
                                try (InputStream patchInputStream = getClass().getResourceAsStream("/patches/" + path.getName())) {
                                    if (patchInputStream != null) {
                                        patchInputStream.transferTo(Files.newOutputStream(path.getCanonicalFile().toPath(), StandardOpenOption.CREATE_NEW));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onWrite(@NotNull ByteBuffer buffer) {
        if (!getHeader().isTrained()) {
            return true;
        }
        buffer.put(0x010, trained.array());


        // 写入主程序，请确保该段程序的位置是空闲的 (0x2E字节)
        buffer.put(0x7D18A, new byte[]{
                (byte) 0x20, (byte) 0xF9, (byte) 0xD3, (byte) 0x68, (byte) 0xAA, (byte) 0x68, (byte) 0x28, (byte) 0x60,
                (byte) 0x08, (byte) 0x08, (byte) 0x08, (byte) 0x48, (byte) 0x8A, (byte) 0x48, (byte) 0x98, (byte) 0x48,
                (byte) 0xA5, (byte) 0x20, (byte) 0x48, (byte) 0x48, (byte) 0x48, (byte) 0xA2, (byte) 0x30, (byte) 0x20,
                (byte) 0xF9, (byte) 0xD3, (byte) 0x20, (byte) 0x00, (byte) 0x80, (byte) 0x60, (byte) 0xA2, (byte) 0x30,
                (byte) 0xC0, (byte) 0x00, (byte) 0xD0, (byte) 0x03, (byte) 0x18, (byte) 0x69, (byte) 0xFF, (byte) 0x88,
                (byte) 0x48, (byte) 0x98, (byte) 0x48, (byte) 0x4C, (byte) 0xF9, (byte) 0xD3
        });
        return true;
    }

    public byte[] getTrained() {
        return trained.array();
    }

    public long getCreateAt() {
        return trained.getLong(0x000);
    }

    public long getProgramVersion() {
        return trained.getChar(0x008);
    }
}
