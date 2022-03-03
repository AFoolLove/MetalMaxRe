package me.afoolslove.metalmaxre.editor.text;

import me.afoolslove.metalmaxre.ResourceManager;
import me.afoolslove.metalmaxre.utils.ByteUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 文字映射
 */
public class WordMap extends HashMap<WordMap.Entity<Object, Iterable<Object>>, WordMap.Entity<byte[], Iterable<byte[]>>> {
    public static final WordMap INSTANCE = new WordMap();

    public static WordMap getInstance() {
        return INSTANCE;
    }

    {
        // 添加0-9
        for (char c = '0'; c <= '9'; c++) {
            put(c, ByteUtils.ofBytes(c - '0'));
        }
        // 添加A-Z
        for (char c = '0', letter = 0x0A; c <= '9'; c++, letter++) {
            put(c, ByteUtils.ofBytes(letter));
        }

        // 添加特殊字符
        put('-', ByteUtils.ofBytes(0x62));
        put('▼', ByteUtils.ofBytes(0x63));  // 下标
        put('。', ByteUtils.ofBytes(0x64));
        put('.', ByteUtils.ofBytes(0x65));
        put('*', ByteUtils.ofBytes(0x8E));  // 类似于 *
        put('…', ByteUtils.ofBytes(0x8F));  // 两个 ..
        put('^', ByteUtils.ofBytes(0x90));  // 对话时名称与文字中间的符号
        put('!', ByteUtils.ofBytes(0x91));
        put('?', ByteUtils.ofBytes(0x92));
        put(':', ByteUtils.ofBytes(0x93));
        put('#', ByteUtils.ofBytes(0x96));
        put('/', ByteUtils.ofBytes(0x99));
        put(' ', ByteUtils.ofBytes(0xFF)); // 空格，占一个英文字符，全角空格占两个英文字符'　'
        put('\t', ByteUtils.ofBytes(0xE5)); // 换行
        put('\r', ByteUtils.ofBytes(0xFE)); // 带名称换行
        put('\n', ByteUtils.ofBytes(0x9F)); // 文本结束
        put('\b', ByteUtils.ofBytes(0x42)); // 忽略字符


        try (var inputStream = ResourceManager.getAsStream("/fonts.txt")) {
            if (inputStream != null) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
                    var line = bufferedReader.readLine(); // 忽略第一行
                    byte row;

                    while ((line = bufferedReader.readLine()) != null) {
                        row = (byte) Integer.parseInt(line.substring(0, 2), 16);
                        // 最多12列
                        for (int column = 0, index = 3; column < 12; column++, index += 2) {
                            if (line.length() <= index) {
                                // 这一行已经读取完毕
                                break;
                            }
                            // 数据值起始为0x24
                            byte[] value = {(byte) (0x24 + column), row};

                            // 添加文字
                            char key = line.charAt(column);
                            if (key != ' ') {
                                // 空格就算了吧
                                if (containsKey(key)) {
                                    // 如果已经添加过，添加进重复Map中
                                    put(key, value);
                                } else {
                                    // 直接添加
                                    putIfAbsent(key, value);
                                }
                            }
                        }

                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取文字映射失败！");
        }
    }

    public static void main(String[] args) {
        WordHelper.toString(INSTANCE, new byte[]{0x25,0x00,0x42,0x42, 0x42},0,5);
    }

    public Entity<byte[], Iterable<byte[]>> put(Object key, byte[] bytes) {
        return put(SingleEntity.of(key), SingleEntity.of(bytes));
    }

    public Entity<byte[], Iterable<byte[]>> put(Object key, Entity<byte[], Iterable<byte[]>> entity) {
        return put(SingleEntity.of(key), entity);
    }

    public Entity<byte[], Iterable<byte[]>> putIfAbsent(Object key, byte[] bytes) {
        return super.putIfAbsent(SingleEntity.of(key), SingleEntity.of(bytes));
    }

    public abstract static class Entity<T, MT extends Iterable<T>> {
        /**
         * @return 是否拥有多个数据
         */
        public abstract boolean isMultiple();

        /**
         * @return 所有数据
         */
        public abstract MT getMultiple();

        @SuppressWarnings("unchecked")
        public <CMT extends MultipleEntity<T>> CMT castMultiple() {
            return (CMT) this;
        }

        /**
         * @return 是否为单个数据
         */
        public abstract boolean isSingle();

        /**
         * @return 无论是否存在多个数据，只返回一个数据
         */
        public abstract T getSingle();

        @SuppressWarnings("unchecked")
        public <CST extends SingleEntity<T>> CST castSingle() {
            return (CST) this;
        }
    }

    public static class MultipleEntity<T> extends Entity<T, List<T>> {
        private final List<T> multiple = new ArrayList<>();


        @Override
        public boolean isMultiple() {
            return multiple.size() > 1;
        }

        @Override
        public List<T> getMultiple() {
            return multiple;
        }

        @Override
        public boolean isSingle() {
            return multiple.size() == 1;
        }

        @Override
        public T getSingle() {
            return multiple.isEmpty() ? null : multiple.get(0);
        }

        @SafeVarargs
        public static <V> MultipleEntity<V> of(V... values) {
            var entity = new MultipleEntity<V>();
            entity.multiple.addAll(Arrays.asList(values));
            return entity;
        }
    }

    public static class SingleEntity<T> extends Entity<T, Iterable<T>> {
        private T single;

        public SingleEntity(T single) {
            this.single = single;
        }

        /**
         * @return false
         */
        @Override
        public boolean isMultiple() {
            return false;
        }

        @Override
        public Iterable<T> getMultiple() {
            return null;
        }

        /**
         * @return true
         */
        @Override
        public boolean isSingle() {
            return true;
        }

        @Override
        public T getSingle() {
            return single;
        }

        public static <V> SingleEntity<V> of(V value) {
            return new SingleEntity<>(value);
        }
    }
}
