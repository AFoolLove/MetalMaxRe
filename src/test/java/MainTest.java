import me.afoolslove.metalmaxre.MetalMaxRe;
import me.afoolslove.metalmaxre.NumberR;
import me.afoolslove.metalmaxre.editor.EditorManager;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MainTest {
    int x = 0xFF;

    static class Tree<T, V> {
        public Tree<T, V> first;
        public Tree<T, V> later;
        public V value;
    }

    public static void main(String[] args) {
        // 得到实例
        MetalMaxRe instance = MetalMaxRe.getInstance();

        // 加载内置ROM文件
        instance.loadInitGame();
        // 如果需要增加监听器
        // loadInitGame(@Nullable EditorManager.LoadListener)
        instance.loadInitGame(new EditorManager.LoadListener() {
        });

        // ----------------
        // 加载外部ROM文件
        // loadGame(@NotNull URI)
        instance.loadGame(new File("xxxx.nes").toURI());
        // 如果需要增加监听器
        // loadGame(@NotNull URI, @Nullable EditorManager.LoadListener)
        instance.loadGame(new File("xxxx.nes").toURI(), new EditorManager.LoadListener() {
        });

        instance.saveAs(new ByteArrayOutputStream(), new EditorManager.ApplyListener() {
        });

        instance.saveAs(Path.of("xxxx.nes"), new EditorManager.ApplyListener() {
        });
    }

    int[] is = {2, 2, 3, 5, 6, 7};
    int max = 10;

    public Tree<Integer, Integer> tree(int i, int c) {
        if (is.length <= i) {
            return null;
        }
        if (c == 0) {
            // 没有空间可以装了
            return null;
        }
        if (is[i] > c) {
            // 装不下
            return null;
        } else {
            Tree<Integer, Integer> tree = new Tree<>();
            tree.first = tree(i, c - is[i]);
            tree.later = tree(i + 1, c - is[i]);
            tree.value = i;
//            if (tree.first == null && tree.later == null) {
//                return null;
//            }
            // 装得下
            return tree;
        }
    }
    int i = 0;

    @Test
    public void test() throws IOException {
//        ASM6.compile(new File("C:\\Users\\AFoolLove\\IdeaProjects\\MetalMaxRe\\src\\main\\resources\\patches\\test.asm"), System.out::println);

        if (i!=0) {
            return;
        }
        List<Character> list = new ArrayList<>();
        list.add(NumberR.toChar(0x5E8F));
        list.add(NumberR.toChar(0x9E8F));
        list.add(NumberR.toChar(0xB08F));
        list.add(NumberR.toChar(0xB98F));
        list.add(NumberR.toChar(0x3E91));
        list.add(NumberR.toChar(0x568F));
        list.add(NumberR.toChar(0xC58F));
        list.add(NumberR.toChar(0xCD8F));
        list.add(NumberR.toChar(0x4891));
        list.add(NumberR.toChar(0x5E8F));
        list.add(NumberR.toChar(0xE28F));
        list.add(NumberR.toChar(0xE68F));
        list.add(NumberR.toChar(0xFD8F));
        list.add(NumberR.toChar(0x0690));
        list.add(NumberR.toChar(0xA88F));
        list.add(NumberR.toChar(0x0990));
        list.add(NumberR.toChar(0x0F90));
        list.add(NumberR.toChar(0x1F90));
        list.add(NumberR.toChar(0x2590));
        list.add(NumberR.toChar(0x2A90));
        list.add(NumberR.toChar(0x3290));
        list.add(NumberR.toChar(0x3590));
        list.add(NumberR.toChar(0x3990));
        list.add(NumberR.toChar(0x2690));
        list.add(NumberR.toChar(0x4391));
        list.add(NumberR.toChar(0x4E91));
        list.add(NumberR.toChar(0x3E90));
        list.add(NumberR.toChar(0x4390));
        list.add(NumberR.toChar(0x0390));
        list.add(NumberR.toChar(0x4690));
        list.add(NumberR.toChar(0x9C8F));
        list.add(NumberR.toChar(0xB58F));
        list.add(NumberR.toChar(0x4C90));
        list.add(NumberR.toChar(0x1E90));
        list.add(NumberR.toChar(0x5090));
        list.add(NumberR.toChar(0x5890));
        list.add(NumberR.toChar(0xE08F));
        list.add(NumberR.toChar(0x5E90));
        list.add(NumberR.toChar(0x6290));
        list.add(NumberR.toChar(0x9E8F));
        list.add(NumberR.toChar(0x3090));
        list.add(NumberR.toChar(0x6590));
        list.add(NumberR.toChar(0x6390));
        list.add(NumberR.toChar(0x2790));
        list.add(NumberR.toChar(0xF790));
        list.add(NumberR.toChar(0xFA90));
        list.add(NumberR.toChar(0xFD90));
        list.add(NumberR.toChar(0x0091));
        list.add(NumberR.toChar(0x6890));
        list.add(NumberR.toChar(0xA08F));
        list.add(NumberR.toChar(0xBE8F));
        list.add(NumberR.toChar(0x0291));
        list.add(NumberR.toChar(0x0891));
        list.add(NumberR.toChar(0xC18F));
        list.add(NumberR.toChar(0x0D91));
        list.add(NumberR.toChar(0x1091));
        list.add(NumberR.toChar(0x1691));
        list.add(NumberR.toChar(0xD98F));
        list.add(NumberR.toChar(0x1C91));
        list.add(NumberR.toChar(0x418F));
        list.add(NumberR.toChar(0xB38F));
        list.add(NumberR.toChar(0x2291));
        list.add(NumberR.toChar(0x458F));
        list.add(NumberR.toChar(0x2790));
        list.add(NumberR.toChar(0x2791));
        list.add(NumberR.toChar(0x3391));
        list.add(NumberR.toChar(0x4D90));
        list.add(NumberR.toChar(0x2D90));
        list.add(NumberR.toChar(0x2D90));
        list.add(NumberR.toChar(0x2D90));
        list.add(NumberR.toChar(0x3991));
        list.add(NumberR.toChar(0xC98F));
        list.add(NumberR.toChar(0x6C90));
        list.add(NumberR.toChar(0x9A90));
        list.add(NumberR.toChar(0x5391));
        list.add(NumberR.toChar(0xC890));
        list.add(NumberR.toChar(0xDF90));
        list.add(NumberR.toChar(0x6D8F));
        list.add(NumberR.toChar(0x1392));

        list.stream().min(Comparator.comparingInt(o -> o)).ifPresent(character -> System.out.println((int) NumberR.toChar(character)));
    }

    public int get() {
        return x;
    }
}
