package me.afoolslove.metalmaxre.editors.text.mapping;

import me.afoolslove.metalmaxre.utils.ResourceManager;
import me.afoolslove.metalmaxre.utils.SingleMapEntry;
import org.jetbrains.annotations.NotNull;
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
 * 中文字库
 * 文字（char）对应游戏中的文字（byte）
 *
 * @author AFoolLove
 */
public class CharMapCN implements ICharMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(CharMapCN.class);

    private final Map<Byte, Integer> opcodes = createDefaultOpcodes();
    private final List<SingleMapEntry<Character, Object>> values = load(null);


    /**
     * 加载映射表
     *
     * @param path 路径，传入null使用默认映射表
     */
    public List<SingleMapEntry<Character, Object>> load(@Nullable String path) {
        List<SingleMapEntry<Character, Object>> values = new ArrayList<>();
        // 添加 0-9 A-Z
        for (byte i = 0; i <= 9; i++) {
            values.add(SingleMapEntry.create((char) ('0' + i), i));
        }
        for (char i = 'A', j = 0x0A; i <= 'Z'; i++) {
            values.add(SingleMapEntry.create(i, (byte) (j + (i - 'A'))));
        }
        values.add(SingleMapEntry.create('-', (byte) (0x62)));
        values.add(SingleMapEntry.create('▼', (byte) (0x63)));  // 下标
        values.add(SingleMapEntry.create('。', (byte) (0x64)));
        values.add(SingleMapEntry.create('.', (byte) (0x65)));
        values.add(SingleMapEntry.create('*', (byte) (0x8E)));  // 类似于 *
        values.add(SingleMapEntry.create('…', (byte) (0x8F)));  // 两个 ..
        values.add(SingleMapEntry.create('^', (byte) (0x90)));  // 对话时名称与文字中间的符号
        values.add(SingleMapEntry.create('!', (byte) (0x91)));
        values.add(SingleMapEntry.create('?', (byte) (0x92)));
        values.add(SingleMapEntry.create(':', (byte) (0x93)));
//        values.add(SingleMapEntry.create('弹', (byte) (0x94)));
//        values.add(SingleMapEntry.create('货', (byte) (0x95)));
        values.add(SingleMapEntry.create('#', (byte) (0x96)));
//        values.add(SingleMapEntry.create('无', (byte) (0x97)));
//        values.add(SingleMapEntry.create('攻', (byte) (0x98)));
        values.add(SingleMapEntry.create('/', (byte) (0x99)));
//        values.add(SingleMapEntry.create('击', (byte) (0x9A)));
//        values.add(SingleMapEntry.create('防', (byte) (0x9B)));
//        values.add(SingleMapEntry.create('t', (byte) (0x9C)));
//        values.add(SingleMapEntry.create('@', (byte) (0x9D)));
//        values.add(SingleMapEntry.create('', (byte) (0x9E))); // 可自定义图块
        values.add(SingleMapEntry.create(' ', (byte) (0xFF))); // 空格，占一个英文字符，全角空格占两个英文字符'　'
        values.add(SingleMapEntry.create('\t', (byte) (0xE5))); // 换行
//        values.add(SingleMapEntry.create('\r', (byte) (0xFE))); // 带名称换行
        values.add(SingleMapEntry.create('\n', (byte) (0x9F))); // 文本结束
//        values.add(SingleMapEntry.create('\b', (byte) (0x42))); // 忽略字符

        // 使用小写英文字母对应另外一组相同字符
        // 数据不一样，但显示一样
        values.add(SingleMapEntry.create('a', (byte) 0x0A));
        values.add(SingleMapEntry.create('b', (byte) 0x0B));
        values.add(SingleMapEntry.create('c', (byte) 0x0C));
        values.add(SingleMapEntry.create('d', (byte) 0x0D));
        values.add(SingleMapEntry.create('e', (byte) 0x0E));
        values.add(SingleMapEntry.create('f', (byte) 0x0F));
        values.add(SingleMapEntry.create('g', (byte) 0x10));
        values.add(SingleMapEntry.create('h', (byte) 0x11));
        values.add(SingleMapEntry.create('i', (byte) 0x12));
        values.add(SingleMapEntry.create('j', (byte) 0x13));
        values.add(SingleMapEntry.create('k', (byte) 0x14));
        values.add(SingleMapEntry.create('l', (byte) 0x15));
        values.add(SingleMapEntry.create('m', (byte) 0x80));
        values.add(SingleMapEntry.create('n', (byte) 0x81));
        values.add(SingleMapEntry.create('o', (byte) 0x82));
        values.add(SingleMapEntry.create('p', (byte) 0x83));
        values.add(SingleMapEntry.create('q', (byte) 0x84));
        values.add(SingleMapEntry.create('r', (byte) 0x85));
        values.add(SingleMapEntry.create('s', (byte) 0x86));
        values.add(SingleMapEntry.create('t', (byte) 0x87));
        values.add(SingleMapEntry.create('u', (byte) 0x88));
        values.add(SingleMapEntry.create('v', (byte) 0x89));
        values.add(SingleMapEntry.create('w', (byte) 0x8A));
        values.add(SingleMapEntry.create('x', (byte) 0x8B));
        values.add(SingleMapEntry.create('y', (byte) 0x8C));
        values.add(SingleMapEntry.create('z', (byte) 0x8D));


        InputStream resourceAsStream;
        if (path == null) {
            // 默认映射表
            resourceAsStream = ResourceManager.getAsStream("/fonts.txt");
        } else {
            // 加载外部映射表
            try {
                resourceAsStream = Files.newInputStream(Path.of(path));
            } catch (IOException e) {
                return null;
            }
        }
        if (resourceAsStream == null) {
            return null;
        }

        Map<Character, Object> FONTS = new HashMap<>();
        Map<Character, Object> FONTS_REPEATED = new HashMap<>();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8))) {
            bufferedReader.readLine(); // 忽略第一行

            byte row; // 行数，代表文字的第二个byte
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                // 读取第二个byte
                row = (byte) Integer.parseInt(line.substring(0, 2), 16);

                // 1行最多12个文字
                for (int i = 0; i < 12; i++) {
                    // 3byte前面的字节，之后每隔2byte就是一个文字
                    int column = 3 + (i * 2);
                    if (line.length() <= column) {
                        // 后面没有字符可以读取了，小于12个文字
                        break;
                    }

                    // 数据值起始为0x24
                    byte[] value = {(byte) (0x24 + i), row};

                    // 添加文字
                    char key = line.charAt(column);
                    if (key != ' ') { // 空格就算了吧


                        if (FONTS.containsKey(key)) {
                            // 如果已经添加过，添加进重复Map中
                            FONTS_REPEATED.put(key, value);
                        } else {
                            // 直接添加
                            FONTS.putIfAbsent(key, value);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    @NotNull
    public Object[] getValues(char ch) {
        List<Object> values = new ArrayList<>();
        for (SingleMapEntry<Character, Object> value : getValues()) {
            if (value.getKey() == ch) {
                values.add(value.getValue());
            }
        }
        return values.toArray();
    }

    @Override
    public @Nullable Object getValue(char ch) {
        for (SingleMapEntry<Character, Object> value : getValues()) {
            if (value.getKey() == ch) {
                return value.getValue();
            }
        }
        return null;
    }
}
