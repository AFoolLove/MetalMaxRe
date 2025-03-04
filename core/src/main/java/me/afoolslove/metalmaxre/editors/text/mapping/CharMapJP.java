package me.afoolslove.metalmaxre.editors.text.mapping;

import me.afoolslove.metalmaxre.utils.ResourceManager;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 日文字库
 * 文字（char）对应游戏中的文字（byte）
 *
 * @author AFoolLove
 */
public class CharMapJP implements ICharMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharMapJP.class);

    private final Map<Byte, Integer> opcodes = createDefaultOpcodes();
    private final List<SingleMapEntry<Character, Object>> values;

    public CharMapJP() {
        values = new ArrayList<>();
    }

    public CharMapJP(@Nullable String path) {
        values = load(path);
    }

    /**
     * 加载映射表
     *
     * @param path 路径，传入null使用默认映射表
     */
    public List<SingleMapEntry<Character, Object>> load(@Nullable String path) {
        List<SingleMapEntry<Character, Object>> values = new ArrayList<>();
        values.add(SingleMapEntry.create(' ', (byte) (0xFF))); // 空格，占一个英文字符，全角空格占两个英文字符'　'
        values.add(SingleMapEntry.create('\t', (byte) (0xE5))); // 换行
//        values.add(SingleMapEntry.create('\r', (byte) (0xFE))); // 带名称换行
        values.add(SingleMapEntry.create('\n', (byte) (0x9F))); // 文本结束
//        values.add(SingleMapEntry.create('\b', (byte) (0x42))); // 忽略字符

        InputStream resourceAsStream;
        if (path == null || path.isEmpty()) {
            // 默认映射表
            resourceAsStream = ResourceManager.getAsStream("/japanese_fonts.txt");
        } else {
            // 加载外部映射表
            try {
                resourceAsStream = Files.newInputStream(Path.of(path));
            } catch (IOException e) {
                LOGGER.error("字库加载失败：", e);
                return null;
            }
        }
        if (resourceAsStream == null) {
            LOGGER.error("字库加载失败。");
            return null;
        }
        Map<Character, Object> FONTS = new HashMap<>();
        Map<Character, Object> FONTS_REPEATED = new HashMap<>();
        // 读取字库
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8))) {
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // 读取第二个byte
                byte value = (byte) Integer.parseInt(line, 0, 2, 16);
                char key = line.charAt(3);

                if (FONTS.containsKey(key)) {
                    // 如果已经添加过，添加进重复Map中
                    FONTS_REPEATED.put(key, new byte[]{value});
                    LOGGER.warn("字库出现重复字符：{}", key);
                } else {
                    // 直接添加
                    FONTS.putIfAbsent(key, new byte[]{value});
                }
            }
        } catch (IOException e) {
            LOGGER.error("字库加载失败：", e);
        }

        values.addAll(FONTS.entrySet().stream().map(SingleMapEntry::create).toList());
        values.addAll(FONTS_REPEATED.entrySet().stream().map(SingleMapEntry::create).toList());
        return values;
    }

    @Override
    public Map<Byte, Integer> getOpcodes() {
        return opcodes;
    }

    @Override
    public List<SingleMapEntry<Character, Object>> getValues() {
        return values;
    }
}
